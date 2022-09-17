/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.internal.yaml;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com._1c.g5.resourcemanagement.ActivityDemandsDefaultAdapter;
import com._1c.g5.resourcemanagement.ActivityResourcePermissions;
import com._1c.g5.resourcemanagement.IActivityDemandsDefinition;
import com._1c.g5.resourcemanagement.IHostResourceManagementHandle;
import com._1c.g5.resourcemanagement.IHostResourceManagerListener;
import com._1c.g5.resourcemanagement.IResourcePlan;
import com._1c.g5.resourcemanagement.SystemLoadState;
import com._1c.g5.v8.dt.core.platform.IResourceLookup;
import com._1c.g5.v8.dt.core.platform.management.IDtHostResourceManager;
import com._1c.g5.v8.dt.platform.version.IRuntimeVersionSupport;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.inject.Inject;

import ru.capralow.dt.modeling.core.ExportDebugTrace;
import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.core.IExportArtifactBuilder;
import ru.capralow.dt.modeling.core.IExportService;
import ru.capralow.dt.modeling.yaml.IExportContext;
import ru.capralow.dt.modeling.yaml.IExporter;
import ru.capralow.dt.modeling.yaml.IExporterRegistry;
import ru.capralow.dt.modeling.yaml.SubObjectsManager;

public class ExportService
    implements IExportService
{
    private static final String EXPORT_YAML_ACTIVITY = "EXPORT_YAML"; //$NON-NLS-1$

    private static final long FLAG_POLLING_PERIOD = 100L;

    private final ExportDebugTrace debugTrace = ExportDebugTrace.getInstance();

    @Inject
    private IExporterRegistry exporterRegistry;

    @Inject
    private IRuntimeVersionSupport versionSupport;

    @Inject
    private IResourceLookup resourceLookup;

    @Inject
    private SubObjectsManager subObjectsManager;

    @Inject
    private IDtHostResourceManager hostResourceManager;

    private ThreadPoolExecutor threadPoolExecutor;

    private volatile boolean active;

    @Override
    public void activate()
    {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        this.threadPoolExecutor = new ThreadPoolExecutor(availableProcessors, availableProcessors, 0L,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new ThreadFactory()
            {
                private AtomicInteger index = new AtomicInteger(0);

                @Override
                public Thread newThread(Runnable r)
                {
                    Thread t = new Thread(r);
                    t.setName("Exporter-" + this.index.incrementAndGet()); //$NON-NLS-1$
                    return t;
                }
            });
        IHostResourceManagementHandle hostResourceManagementHandle =
            this.hostResourceManager.registerResourcePlan(new ResourcePlan());
        hostResourceManagementHandle.addListener(new IHostResourceManagerListener()
        {
            @Override
            public synchronized void permissionsChanged(String activityName,
                ActivityResourcePermissions updatedPermissions)
            {
                int newCores = updatedPermissions.getCores();
                int currentCores = ExportService.this.threadPoolExecutor.getCorePoolSize();
                if (newCores < currentCores)
                {
                    ExportService.this.threadPoolExecutor.setCorePoolSize(newCores);
                    ExportService.this.threadPoolExecutor.setMaximumPoolSize(newCores);
                }
                else if (newCores > currentCores)
                {
                    ExportService.this.threadPoolExecutor.setMaximumPoolSize(newCores);
                    ExportService.this.threadPoolExecutor.setCorePoolSize(newCores);
                }
            }
        });
        hostResourceManagementHandle.startActivity(EXPORT_YAML_ACTIVITY);
        this.active = true;
    }

    @Override
    public void deactivate()
    {
        checkActive();
        this.active = false;
        this.threadPoolExecutor.shutdown();
        this.threadPoolExecutor = null;
    }

    @Override
    public IStatus work(EObject eObject, IExportArtifactBuilder artifactBuilder, boolean exportSubordinateObject,
        boolean exportExternalProperties, IProgressMonitor progressMonitor)
    {
        checkActive();
        SubMonitor monitor = SubMonitor.convert(progressMonitor);
        Version version = this.versionSupport.getRuntimeVersion(eObject);
        IProject project = this.resourceLookup.getProject(EcoreUtil.getURI(eObject));
        IExportContext exportContext = new ExportContext(project, version);
        try
        {
            List<IStatus> statusList = export(eObject, exportSubordinateObject, exportExternalProperties,
                artifactBuilder, exportContext, monitor);
            MultiStatus status =
                new MultiStatus(YamlPlugin.ID, 0, statusList.toArray(new IStatus[0]), getMessage(statusList), null);
            return status;
        }
        catch (OperationCanceledException ex)
        {
            this.debugTrace.trace(IExportService.EXPORT_SERVICE_TRACE_OPTION, "Export service canceled", ex);
            return new Status(IStatus.CANCEL, YamlPlugin.ID, Messages.ExportService_export_operation_canceled_by_user);
        }
    }

    private boolean awaitCompletion(CompletionService<IStatus> completionService, AtomicInteger activeTaskCount,
        List<IStatus> status, SubMonitor monitor)
    {
        long deadline = System.currentTimeMillis() + FLAG_POLLING_PERIOD;
        monitor.setWorkRemaining(activeTaskCount.get());
        long now = System.currentTimeMillis();
        if (now > deadline)
        {
            return false;
        }
        try
        {
            Future<IStatus> completedTaskFuture = completionService.poll(deadline - now, TimeUnit.MILLISECONDS);
            if (completedTaskFuture == null)
            {
                return false;
            }
            IStatus taskStatus = completedTaskFuture.get();
            if (!taskStatus.isOK())
            {
                status.add(taskStatus);
            }
        }
        catch (InterruptedException e)
        {
            this.debugTrace.trace(IExportService.EXPORT_SERVICE_TRACE_OPTION,
                "Export service task executing error, thread interrupt", e);
            Thread.currentThread().interrupt();
            return false;
        }
        catch (ExecutionException e)
        {
            this.debugTrace.trace(IExportService.EXPORT_SERVICE_TRACE_OPTION, "Export service task executing error", e);
            status.add(YamlPlugin.createErrorStatus(e.getMessage(), e));
        }
        monitor.worked(1);
        return activeTaskCount.decrementAndGet() == 0;
    }

    private void checkActive()
    {
        if (!this.active)
        {
            throw new IllegalStateException("The service is not active");
        }
    }

    private List<IStatus> export(EObject paramEObject, boolean exportSubordinateObject,
        boolean exportExternalProperties, IExportArtifactBuilder artifactBuilder, IExportContext exportContext,
        SubMonitor progress) throws OperationCanceledException
    {
        CompletionService<IStatus> completionService = new ExecutorCompletionService<>(this.threadPoolExecutor);
        TaskMonitor taskMonitor = new TaskMonitor();
        AtomicInteger activeTaskCount = new AtomicInteger();
        completionService.submit(new ExportObjectTask(() -> paramEObject, exportSubordinateObject,
            exportExternalProperties, artifactBuilder, exportContext, this.exporterRegistry, this.subObjectsManager,
            completionService, activeTaskCount, taskMonitor));
        activeTaskCount.incrementAndGet();
        List<IStatus> status = new ArrayList<>();
        while (!awaitCompletion(completionService, activeTaskCount, status, progress))
        {
            if (Thread.interrupted())
            {
                Thread.currentThread().interrupt();
                taskMonitor.setCanceled(true);
                throw new OperationCanceledException();
            }
            if (progress.isCanceled())
            {
                taskMonitor.setCanceled(true);
                throw new OperationCanceledException();
            }
            String taskName = taskMonitor.getTaskName();
            if (taskName != null)
            {
                progress.setTaskName(taskName);
            }
        }
        return status;
    }

    private String getMessage(List<IStatus> statusList)
    {
        Integer severity = Integer.valueOf(statusList.stream().mapToInt(IStatus::getSeverity).max().orElse(0));
        switch (severity.intValue())
        {
        case IStatus.OK:
        case IStatus.INFO:
            return Messages.ExportService_export_operation_success;
        case IStatus.CANCEL:
            return Messages.ExportService_export_operation_canceled_by_user;
        case IStatus.WARNING:
            return Messages.ExportService_export_operation_completed_with_warning;
        default:
            return (statusList.size() > 1) ? Messages.ExportService_export_operation_has_several_errors
                : Messages.ExportService_export_operation_has_error;
        }
    }

    private static class ExportObjectTask
        implements Callable<IStatus>
    {
        private Supplier<EObject> eObjectProvider;

        private final boolean exportSubordinateObject;

        private final boolean exportExternalProperties;

        private final IExportArtifactBuilder artifactBuilder;

        private final IExportContext exportContext;

        private final IExporterRegistry exporterRegistry;

        private final SubObjectsManager subObjectsManager;

        private final CompletionService<IStatus> completionService;

        private final AtomicInteger activeTaskCount;

        private final IProgressMonitor monitor;

        private final ExportDebugTrace debugTrace = ExportDebugTrace.getInstance();

        ExportObjectTask(Supplier<EObject> eObjectProvider, boolean exportSubordinateObject,
            boolean exportExternalProperties, IExportArtifactBuilder artifactBuilder, IExportContext exportContext,
            IExporterRegistry exporterRegistry, SubObjectsManager subObjectsManager,
            CompletionService<IStatus> completionService, AtomicInteger activeTaskCount, IProgressMonitor monitor)
        {
            this.eObjectProvider = eObjectProvider;
            this.exportSubordinateObject = exportSubordinateObject;
            this.exportExternalProperties = exportExternalProperties;
            this.artifactBuilder = artifactBuilder;
            this.exportContext = exportContext;
            this.exporterRegistry = exporterRegistry;
            this.subObjectsManager = subObjectsManager;
            this.completionService = completionService;
            this.activeTaskCount = activeTaskCount;
            this.monitor = monitor;
        }

        @Override
        public IStatus call()
        {
            if (this.monitor.isCanceled())
            {
                return Status.CANCEL_STATUS;
            }
            EObject object = this.eObjectProvider.get();
            for (Supplier<EObject> subordinateObjectProvider : this.subObjectsManager.getSubordinateObjects(object,
                this.exportSubordinateObject, this.exportExternalProperties, this.exportContext.getProjectVersion()))
            {
                this.completionService
                    .submit(new ExportObjectTask(subordinateObjectProvider, this.exportSubordinateObject,
                        this.exportExternalProperties, this.artifactBuilder, this.exportContext, this.exporterRegistry,
                        this.subObjectsManager, this.completionService, this.activeTaskCount, this.monitor));
                this.activeTaskCount.incrementAndGet();
            }
            if (this.monitor.isCanceled())
            {
                return Status.CANCEL_STATUS;
            }
            try
            {
                IExporter exporter = this.exporterRegistry.getExporter(this.exportContext.getProjectVersion(), object);
                this.debugTrace.traceEntry(IExporter.EXPORTER_TRACE_OPTION,
                    new Object[] { exporter, this.exportContext.getProjectVersion(), EcoreUtil.getURI(object) });
                IStatus workStatus = exporter.work(object, this.exportContext, this.artifactBuilder, this.monitor);
                this.debugTrace.traceExit(IExporter.EXPORTER_TRACE_OPTION, workStatus);
                return workStatus;
            }
            catch (ExportException ex)
            {
                this.debugTrace.trace(IExportService.EXPORT_SERVICE_TRACE_OPTION,
                    "Export service receiving exporter error", ex);
                return YamlPlugin.createWarningStatus(ex.getMessage());
            }
            catch (Exception e)
            {
                this.debugTrace.trace(IExporter.EXPORTER_TRACE_OPTION,
                    MessageFormat.format("Error in exporter {0}", new Object[] { getClass().getName() }), e);
                throw e;
            }
        }
    }

    private static class ResourcePlan
        implements IResourcePlan
    {
        @Override
        public Map<String, IActivityDemandsDefinition> gatherActivities()
        {
            Map<String, IActivityDemandsDefinition> definitions = new HashMap<>(1);
            ActivityDemandsDefaultAdapter activityDemandsDefaultAdapter = new ActivityDemandsDefaultAdapter()
            {
                @Override
                public ActivityResourcePermissions gatherPermissions(long availableSharedMemory, int availableCores,
                    SystemLoadState overloadState)
                {
                    int cores = Runtime.getRuntime().availableProcessors() - 1;
                    if (overloadState == SystemLoadState.OVERLOAD)
                    {
                        cores /= 2;
                    }
                    if (overloadState == SystemLoadState.CRITICAL)
                    {
                        cores = 1;
                    }
                    if (cores == 0)
                    {
                        cores = 1;
                    }
                    return new ActivityResourcePermissions(cores, overloadState);
                }
            };
            definitions.put(EXPORT_YAML_ACTIVITY, activityDemandsDefaultAdapter);
            return definitions;
        }
    }

    private static class TaskMonitor
        extends NullProgressMonitor
    {
        private volatile String taskName;

        public String getTaskName()
        {
            return this.taskName;
        }

        @Override
        public void setTaskName(String name)
        {
            this.taskName = name;
        }
    }
}
