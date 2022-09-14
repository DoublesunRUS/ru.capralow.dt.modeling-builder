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

import javax.inject.Inject;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com._1c.g5.v8.bm.integration.IBmModel;
import com._1c.g5.v8.dt.core.platform.IBmModelManager;
import com._1c.g5.v8.dt.md.IExternalPropertyManagerRegistry;
import com._1c.g5.v8.dt.metadata.IExternalPropertyManager;
import com._1c.g5.v8.dt.metadata.mdclass.Configuration;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;
import com._1c.g5.v8.dt.metadata.mdclass.Subsystem;
import com._1c.g5.v8.dt.metadata.mdclass.util.MdClassUtil;
import com.google.common.base.Predicate;
import com.google.inject.Provider;

import ru.capralow.dt.modeling.core.ExportDebugTrace;
import ru.capralow.dt.modeling.core.IExportFileSystemSupport;

public abstract class BaseYamlExportFileSystemSupport
    implements IExportFileSystemSupport
{
    protected static final String EXT = "Ext"; //$NON-NLS-1$

    private static final String ROOT = ""; //$NON-NLS-1$

    private static final String FORMS = "Forms"; //$NON-NLS-1$

    private static final String TEMPLATES = "Templates"; //$NON-NLS-1$

    private static final String COMMANDS = "Commands"; //$NON-NLS-1$

    private static final String RECALCULATIONS = "Recalculations"; //$NON-NLS-1$

    @Inject
    private Provider<IExternalPropertyManagerRegistry> externalPropertyManagerRegistryProvider;

    @Inject
    private Provider<IBmModelManager> bmModelManagerProvider;

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

        if (!mdObjectsubsystems.isEmpty())
        {
            Subsystem subsystem = mdObjectsubsystems.stream().findFirst().get();

            Path path = Paths.get(subsystem.getName(), new String[0]);
            while (subsystem.getParentSubsystem() != null)
            {
                subsystem = subsystem.getParentSubsystem();
                path = Paths.get(subsystem.getName(), new String[] { subsystem.getName() }).resolve(path);
            }

            return path;

        }
        else
        {
            return Paths.get("Основной", new String[0]); //$NON-NLS-1$
        }

//        if (subsystem != null)
//        {
//            if (mdObject instanceof Subsystem)
//            {
//                Path path = Paths.get(subsystem.getName(), new String[0]);
//                while (subsystem.getParentSubsystem() != null)
//                {
//                    subsystem = subsystem.getParentSubsystem();
//                    path = Paths.get(subsystem.getName(), new String[] { subsystem.getName() }).resolve(path);
//                }
//                return path;
//            }

//            if (mdObject instanceof Table)
//            {
//                Table table = (Table)mdObject;
//                String parentFolder = TOP_OBJECT_FOLDER_NAMES.get(MdClassPackage.Literals.EXTERNAL_DATA_SOURCE);
//                return Paths.get(parentFolder, new String[] { table.getParentDataSource().getName(), target });
//            }
//
//            if (mdObject instanceof Cube)
//            {
//                Cube cube = (Cube)mdObject;
//                String parentFolder = TOP_OBJECT_FOLDER_NAMES.get(MdClassPackage.Literals.EXTERNAL_DATA_SOURCE);
//                return Paths.get(parentFolder, new String[] { cube.getParentDataSource().getName(), target });
//            }
//
//            if (mdObject instanceof DimensionTable)
//            {
//                DimensionTable dimensionTable = (DimensionTable)mdObject;
//                String parentFolder = TOP_OBJECT_FOLDER_NAMES.get(MdClassPackage.Literals.EXTERNAL_DATA_SOURCE);
//                String parentName = dimensionTable.getParentCube().getParentDataSource().getName();
//                String cubeFolder = TOP_OBJECT_FOLDER_NAMES.get(MdClassPackage.Literals.CUBE);
//                String cubeName = dimensionTable.getParentCube().getName();
//                return Paths.get(parentFolder, new String[] { parentName, cubeFolder, cubeName, target });
//            }

//            return Paths.get(subsystem.getName(), new String[0]);
//        }

//        MdObject container = EcoreUtil2.getContainerOfType(mdObject.eContainer(), MdObject.class);
//        Preconditions.checkState(container != null && !container.eIsProxy());
//        Path containerPath = getMdObjectTargetDirectory(container).resolve(container.getName());
//        if (mdObject instanceof com._1c.g5.v8.dt.metadata.mdclass.BasicForm)
//        {
//            return containerPath.resolve(FORMS);
//        }
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

//        ExportDebugTrace debugTrace = ExportDebugTrace.getInstance();
//        String message = MessageFormat.format("Trying gets target directory for unknown metadata object with uri: {0}",
//            new Object[] { EcoreUtil.getURI(mdObject) });
//        debugTrace.trace(IExporter.EXPORTER_TRACE_OPTION, message);
//        debugTrace.traceDumpStack(IExporter.EXPORTER_TRACE_OPTION);
//        throw new IllegalArgumentException(message);
    }

    protected IExternalPropertyManager getExternalPropertyManager(EObject referenceObject)
    {
        IBmModel bmModel = getBmModel(referenceObject);
        return this.externalPropertyManagerRegistryProvider.get().getExternalPropertyManager(bmModel);
    }

    private IBmModel getBmModel(EObject referenceObject)
    {
        if (referenceObject.eIsProxy())
        {
            return this.bmModelManagerProvider.get().getModel(EcoreUtil.getURI(referenceObject));
        }
        return this.bmModelManagerProvider.get().getModel(referenceObject);
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

    public static <T extends EObject> boolean containsBmObject(List<? extends T> list, T object)
    {
        return list.contains(object);
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
