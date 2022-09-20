/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.yaml;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.EcoreUtil2;

import com._1c.g5.v8.bm.integration.IBmModel;
import com._1c.g5.v8.dt.core.platform.IBmModelManager;
import com._1c.g5.v8.dt.md.IExternalPropertyManagerRegistry;
import com._1c.g5.v8.dt.metadata.IExternalPropertyManager;
import com._1c.g5.v8.dt.metadata.mdclass.Configuration;
import com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;
import com._1c.g5.v8.dt.metadata.mdclass.Subsystem;
import com._1c.g5.v8.dt.metadata.mdclass.util.MdClassUtil;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Provider;

import ru.capralow.dt.modeling.core.ExportDebugTrace;
import ru.capralow.dt.modeling.core.IExportFileSystemSupport;

public abstract class BaseYamlExportFileSystemSupport
    implements IExportFileSystemSupport
{
    protected static final String EXT = "Ext"; //$NON-NLS-1$

    private static final String ROOT = ""; //$NON-NLS-1$

    public static final Map<EClass, String> TOP_OBJECT_FOLDER_NAMES;

    static
    {
        ImmutableMap.Builder<EClass, String> foldersBuilder = ImmutableMap.builder();
        foldersBuilder.put(MdClassPackage.Literals.COMMON_MODULE, "-"); //$NON-NLS-1$
        foldersBuilder.put(MdClassPackage.Literals.HTTP_SERVICE, "HTTPСервис"); //$NON-NLS-1$
        foldersBuilder.put(MdClassPackage.Literals.CATALOG, "Справочник"); //$NON-NLS-1$
        foldersBuilder.put(MdClassPackage.Literals.DOCUMENT, "Документ"); //$NON-NLS-1$
        foldersBuilder.put(MdClassPackage.Literals.ENUM, "Перечисление"); //$NON-NLS-1$
        foldersBuilder.put(MdClassPackage.Literals.REPORT, "Отчет"); //$NON-NLS-1$
        foldersBuilder.put(MdClassPackage.Literals.DATA_PROCESSOR, "Обработка"); //$NON-NLS-1$
        foldersBuilder.put(MdClassPackage.Literals.INFORMATION_REGISTER, "РегистрСведений"); //$NON-NLS-1$
        foldersBuilder.put(MdClassPackage.Literals.ACCUMULATION_REGISTER, "РегистрНакопления"); //$NON-NLS-1$
        foldersBuilder.put(MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES, "ПланВидовХарактеристик"); //$NON-NLS-1$
        foldersBuilder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS, "ПланСчетов"); //$NON-NLS-1$
        foldersBuilder.put(MdClassPackage.Literals.ACCOUNTING_REGISTER, "РегистрБухгалтерии"); //$NON-NLS-1$
        foldersBuilder.put(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES, "ПланВидовРасчета"); //$NON-NLS-1$
        foldersBuilder.put(MdClassPackage.Literals.CALCULATION_REGISTER, "РегистрРасчета"); //$NON-NLS-1$
        TOP_OBJECT_FOLDER_NAMES = foldersBuilder.build();
    }

    public static <T extends EObject> boolean containsBmObject(List<? extends T> list, T object)
    {
        return list.contains(object);
    }

    public static Collection<Subsystem> getSubsystemsContainingMdObject(Configuration configuration, MdObject mdObject)
    {
        List<Subsystem> result = new ArrayList<>();
        collectSubsystemsByFilter(configuration.getSubsystems(), new ContainsMdObjectPredicate(mdObject), result);
        return result;
    }

    private static void collectSubsystemsByFilter(Collection<Subsystem> subsystems, Predicate<Subsystem> predicate,
        Collection<Subsystem> result)
    {
        for (Subsystem subsystem : subsystems)
        {
            collectSubsystemsByFilter(subsystem.getSubsystems(), predicate, result);
            if (predicate == null || predicate.apply(subsystem))
            {
                result.add(subsystem);
            }
        }
    }

    @Inject
    private Provider<IExternalPropertyManagerRegistry> externalPropertyManagerRegistryProvider;

    @Inject
    private Provider<IBmModelManager> bmModelManagerProvider;

    private IBmModel getBmModel(EObject referenceObject)
    {
        if (referenceObject.eIsProxy())
        {
            return this.bmModelManagerProvider.get().getModel(EcoreUtil.getURI(referenceObject));
        }
        return this.bmModelManagerProvider.get().getModel(referenceObject);
    }

    protected IExternalPropertyManager getExternalPropertyManager(EObject referenceObject)
    {
        IBmModel bmModel = getBmModel(referenceObject);
        return this.externalPropertyManagerRegistryProvider.get().getExternalPropertyManager(bmModel);
    }

    protected Path getMdObjectTargetDirectory(MdObject mdObject)
    {
        if (mdObject.eIsProxy())
        {
            ExportDebugTrace exportDebugTrace = ExportDebugTrace.getInstance();
            exportDebugTrace.trace(IExporter.EXPORTER_TRACE_OPTION,
                MessageFormat.format("Trying gets target directory for proxy metadata object with uri: {0}",
                    new Object[] { EcoreUtil.getURI(mdObject) }));
            exportDebugTrace.traceDumpStack(IExporter.EXPORTER_TRACE_OPTION);
        }

        if (mdObject instanceof com._1c.g5.v8.dt.metadata.mdclass.Configuration)
        {
            return Paths.get(ROOT, new String[0]);
        }

        Collection<Subsystem> mdObjectsubsystems =
            getSubsystemsContainingMdObject(MdClassUtil.getConfiguration(mdObject), mdObject);

        Path path;
        if (!mdObjectsubsystems.isEmpty())
        {
            Subsystem subsystem = mdObjectsubsystems.stream().findFirst().get();

            path = Paths.get(subsystem.getName(), new String[0]);
            while (subsystem.getParentSubsystem() != null)
            {
                subsystem = subsystem.getParentSubsystem();
                path = Paths.get(subsystem.getName(), new String[] { subsystem.getName() }).resolve(path);
            }

        }
        else
        {
            path = Paths.get("Основной", new String[0]); //$NON-NLS-1$
        }

        String target = TOP_OBJECT_FOLDER_NAMES.get(mdObject.eClass());
        if (target != null)
        {
            path = path.resolve(target);
            return path;
        }

        MdObject container = EcoreUtil2.getContainerOfType(mdObject.eContainer(), MdObject.class);
        Preconditions.checkState(container != null && !container.eIsProxy());

        Path targetPath = getMdObjectTargetDirectory(container);
        Path containerPath = targetPath.resolveSibling(targetPath.getFileName() + container.getName());
        if (mdObject instanceof com._1c.g5.v8.dt.metadata.mdclass.BasicForm)
        {
            return containerPath;
        }
//        if (mdObject instanceof com._1c.g5.v8.dt.metadata.mdclass.BasicTemplate)
//        {
//            return containerPath.resolve(TEMPLATES);
//        }
//        if (mdObject instanceof com._1c.g5.v8.dt.metadata.mdclass.BasicCommand)
//        {
//            return containerPath.resolve("Commands");
//        }
//        if (mdObject instanceof com._1c.g5.v8.dt.metadata.mdclass.Recalculation)
//        {
//            return containerPath.resolve("Recalculations");
//        }

        ExportDebugTrace debugTrace = ExportDebugTrace.getInstance();
        String message = MessageFormat.format("Trying gets target directory for unknown metadata object with uri: {0}",
            new Object[] { EcoreUtil.getURI(mdObject) });
        debugTrace.trace(IExporter.EXPORTER_TRACE_OPTION, message);
        debugTrace.traceDumpStack(IExporter.EXPORTER_TRACE_OPTION);
        throw new IllegalArgumentException(message);
    }

    private static class ContainsMdObjectPredicate
        implements Predicate<Subsystem>
    {
        private MdObject mdObject;

        ContainsMdObjectPredicate(MdObject mdObject)
        {
            this.mdObject = mdObject;
        }

        @Override
        public boolean apply(Subsystem subsystem)
        {
            return containsBmObject((List<? extends MdObject>)subsystem.getContent(), this.mdObject);
        }
    }

}
