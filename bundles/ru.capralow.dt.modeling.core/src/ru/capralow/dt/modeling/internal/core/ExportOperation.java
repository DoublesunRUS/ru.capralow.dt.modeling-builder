/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.internal.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.PerformanceStats;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com._1c.g5.v8.activitytracking.core.ISystemIdleService;
import com._1c.g5.v8.derived.IDerivedDataManager;
import com._1c.g5.v8.dt.core.platform.IDerivedDataManagerProvider;
import com._1c.g5.v8.dt.core.platform.IResourceLookup;
import com._1c.g5.v8.dt.platform.version.Version;
import com._1c.g5.v8.dt.xml.LineFeedConverter;

import ru.capralow.dt.modeling.core.ExportDebugTrace;
import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.core.IExportArtifactBuilder;
import ru.capralow.dt.modeling.core.IExportArtifactBuilderFactory;
import ru.capralow.dt.modeling.core.IExportOperation;
import ru.capralow.dt.modeling.core.IExportService;
import ru.capralow.dt.modeling.core.IExportServiceRegistry;
import ru.capralow.dt.modeling.core.IExportStrategy;

public class ExportOperation
    implements IExportOperation
{
    private static final String[] REQUIRED_DD_PARTS;
    private static final String UNKNOWN_DIRECTORY_NAME = "unknown"; //$NON-NLS-1$
    private static final String CONFIGURATION_PART_FILE_NAME = "configuration.part"; //$NON-NLS-1$
    private IExportArtifactBuilderFactory artifactBuilderFactory;
    private EObject[] eObjects;
    private Version version;
    private IExportServiceRegistry exportServiceRegistry;
    private IResourceLookup resourceLookup;
    private ISystemIdleService systemIdleService;
    private IDerivedDataManagerProvider derivedDataManagerProvider;
    private IExportStrategy strategy;
    private ExportDebugTrace debugTrace;

    static
    {
        REQUIRED_DD_PARTS = new String[] { "MD", "FORM", "MD_PRE" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    public ExportOperation(final Version version, final Path targetPath,
        final IExportArtifactBuilderFactory artifactBuilderFactory, final IExportServiceRegistry exportServiceRegistry,
        final IResourceLookup resourceLookup, final ISystemIdleService systemIdleService,
        final IDerivedDataManagerProvider derivedDataManagerProvider, final IExportStrategy strategy,
        final EObject... eObjects)
    {
        this.debugTrace = ExportDebugTrace.getInstance();
        this.eObjects = eObjects;
        this.version = version;
        this.artifactBuilderFactory = artifactBuilderFactory;
        this.exportServiceRegistry = exportServiceRegistry;
        this.resourceLookup = resourceLookup;
        this.systemIdleService = systemIdleService;
        this.derivedDataManagerProvider = derivedDataManagerProvider;
        this.strategy = strategy;
        this.debugTrace.trace(IExportOperation.EXPORT_OPERATION_TRACE_OPTION, "Create export operation: " + toString()); //$NON-NLS-1$
        this.debugTrace.traceEntry(IExportOperation.EXPORT_OPERATION_TRACE_OPTION,
            new Object[] { version, (targetPath != null) ? targetPath : "n/a", eObjects }); //$NON-NLS-1$
    }

    @Override
    public IStatus run(final IProgressMonitor progressMonitor)
    {
        debugTrace.trace(IExportOperation.EXPORT_OPERATION_TRACE_OPTION, "Start export operation: " + toString());
        MultiStatus operationStatus = new MultiStatus("com._1c.g5.v8.dt.export", 0,
            Messages.ExportOperation_export_operation_success, (Throwable)null);

        try
        {
            progressMonitor.beginTask(Messages.ExportOperation_exporting, -1);
            IProject project = resourceLookup.getProject(EcoreUtil.getURI(eObjects[0]));
            IDerivedDataManager manager = derivedDataManagerProvider.get(project);
            progressMonitor.subTask(Messages.ExportOperation_waiting_for_build_of_configuration);
            manager.waitComputation(600000L, true, REQUIRED_DD_PARTS);
            systemIdleService.setForceSystemBusy(true);
            IExportService exportService = exportServiceRegistry.getExportService(version);
            PerformanceStats perfStats =
                PerformanceStats.getStats("ru.capralow.dt.modeling.core/perf/exportObjectsToXml." + version.toString(),
                    getClass().getSimpleName());
            perfStats.startRun(strategy.getClass().getName());

            Throwable t = null;

            try
            {
                IExportArtifactBuilder artifactBuilder = artifactBuilderFactory.createArtifactBuilder();

                try
                {
                    EObject[] var13 = eObjects;
                    int var12 = eObjects.length;

                    for (int var11 = 0; var11 < var12; ++var11)
                    {
                        EObject eObject = var13[var11];
                        boolean exportSubordinatesObjects = strategy.exportSubordinatesObjects(eObject);
                        boolean exportExternalProperties = strategy.exportExternalProperties(eObject);
                        debugTrace.trace(IExportOperation.EXPORT_OPERATION_TRACE_OPTION, "Start export service");
                        debugTrace.traceEntry(IExportService.EXPORT_SERVICE_TRACE_OPTION, new Object[] { exportService,
                            eObject, exportSubordinatesObjects, exportExternalProperties });
                        IStatus status = exportService.work(eObject, artifactBuilder, exportSubordinatesObjects,
                            exportExternalProperties, progressMonitor);
                        debugTrace.traceExit(IExportService.EXPORT_SERVICE_TRACE_OPTION, status);
                        operationStatus.merge(status);
                    }

                    if (project != null && !progressMonitor.isCanceled() && strategy.exportUnknown())
                    {
                        debugTrace.trace(IExportOperation.EXPORT_OPERATION_TRACE_OPTION, "Export Unknown artifacts");
                        IStatus exportUnknownArtifacts =
                            exportUnknownArtifacts(project, artifactBuilder, progressMonitor);
                        debugTrace.traceExit(IExportOperation.EXPORT_OPERATION_TRACE_OPTION, exportUnknownArtifacts);
                        operationStatus.merge(exportUnknownArtifacts);
                    }
                }
                finally
                {
                    if (artifactBuilder != null)
                    {
                        artifactBuilder.close();
                    }

                }
            }
            catch (Throwable exception)
            {
//                if (t == null) {
//                    t = exception;
//                } else if (t != exception) {
//                    t.addSuppressed(exception);
//                }
//
//                throw t;
            }

            perfStats.endRun();

        }
        catch (InterruptedException | ExportException e)
        {
            debugTrace.trace(IExportOperation.EXPORT_OPERATION_TRACE_OPTION, "Export operation error", e);
            operationStatus.merge(CorePlugin.createErrorStatus(e.getMessage(), e));

        }
        finally
        {
            progressMonitor.done();
            systemIdleService.setForceSystemBusy(false);
        }

        IStatus result = sortStatuses(operationStatus);
        debugTrace.traceExit(IExportOperation.EXPORT_OPERATION_TRACE_OPTION, result);
        return result;
    }

    private IStatus exportUnknownArtifacts(final IProject project, final IExportArtifactBuilder artifactBuilder,
        final IProgressMonitor progressMonitor)
    {
        final Path unknownFolder = Paths.get(project.getLocationURI()).resolve(UNKNOWN_DIRECTORY_NAME);
        if (Files.isDirectory(unknownFolder, new LinkOption[0]))
        {
            try
            {
                final UnsupportedFilesCopyVisitor visitor =
                    new UnsupportedFilesCopyVisitor.Builder(unknownFolder, artifactBuilder)
                        .addExclusion(path -> CONFIGURATION_PART_FILE_NAME.equals(path.getFileName().toString()))
                        .putModifier(
                            UnsupportedFilesCopyVisitor.FileExtensionPredicateBuilder.build(new String[] { ".xml" }), //$NON-NLS-1$
                            new LineFeedConverter.ConvertOption[] { LineFeedConverter.ConvertOption.TO_LF,
                                LineFeedConverter.ConvertOption.WRITE_BOM })
                        .putModifier(
                            UnsupportedFilesCopyVisitor.FileExtensionPredicateBuilder
                                .build(new String[] { ".bsl", ".txt" }), //$NON-NLS-1$ //$NON-NLS-2$
                            new LineFeedConverter.ConvertOption[] { LineFeedConverter.ConvertOption.TO_CRLF,
                                LineFeedConverter.ConvertOption.WRITE_BOM })
                        .putModifier(
                            UnsupportedFilesCopyVisitor.FileExtensionPredicateBuilder
                                .build(new String[] { ".html", ".htm" }), //$NON-NLS-1$ //$NON-NLS-2$
                            new LineFeedConverter.ConvertOption[] { LineFeedConverter.ConvertOption.TO_LF })
                        .build();
                Files.walkFileTree(unknownFolder, visitor);
            }
            catch (IOException ex)
            {
                debugTrace.trace(IExportOperation.EXPORT_OPERATION_TRACE_OPTION, "Export Unknown artifacts error", ex);
                return CorePlugin.createErrorStatus(ex.getMessage(), ex.getCause());
            }
        }
        return Status.OK_STATUS;
    }

    private IStatus sortStatuses(final MultiStatus operationStatus)
    {
        final List<IStatus> statuses = Stream.of(operationStatus.getChildren())
            .filter(s -> s.getSeverity() > 0)
            .sorted((s1, s2) -> s1.getSeverity() - s2.getSeverity())
            .collect(Collectors.toList());
        return new MultiStatus(CorePlugin.ID, operationStatus.getCode(), statuses.toArray(new IStatus[0]),
            getMessage(operationStatus, statuses), (Throwable)null);
    }

    private String getMessage(final IStatus status, final List<IStatus> statuses)
    {
        switch (status.getSeverity())
        {
        case Status.OK: {
            return status.getMessage();
        }
        case Status.CANCEL: {
            return Messages.ExportOperation_export_operation_canceled_by_user;
        }
        case Status.INFO: {
            return Messages.ExportOperation_export_operation_success;
        }
        default: {
            return (statuses.size() > 1) ? Messages.ExportOperation_export_operation_has_several_errors : MessageFormat
                .format(Messages.ExportOperation_export_operation_has_error__0, statuses.get(0).getMessage());
        }
        }
    }
}
