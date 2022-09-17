/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.internal.yaml.writer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.xml.namespace.QName;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.core.platform.IResourceLookup;
import com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage.Literals;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.md.yaml.IMetadataYamlElements;
import ru.capralow.dt.modeling.yaml.IQnameProvider;
import ru.capralow.dt.modeling.yaml.IYamlElements;
import ru.capralow.dt.modeling.yaml.writer.ISpecifiedElementWriter;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;

@Singleton
public class MetadataObjectWriter
    implements ISpecifiedElementWriter
{
    public static boolean isMdObjectSupported(MdObject mdObject)
    {
        return !(mdObject instanceof com._1c.g5.v8.dt.metadata.mdclass.FunctionalOption
            || mdObject instanceof com._1c.g5.v8.dt.metadata.mdclass.Language
            || mdObject instanceof com._1c.g5.v8.dt.metadata.mdclass.Role
            || mdObject instanceof com._1c.g5.v8.dt.metadata.mdclass.ScheduledJob
            || mdObject instanceof com._1c.g5.v8.dt.metadata.mdclass.StyleItem
            || mdObject instanceof com._1c.g5.v8.dt.metadata.mdclass.Constant
            || mdObject instanceof com._1c.g5.v8.dt.metadata.mdclass.Report
            || mdObject instanceof com._1c.g5.v8.dt.metadata.mdclass.DataProcessor
            || mdObject instanceof com._1c.g5.v8.dt.metadata.mdclass.DataProcessorForm);
    }

    @Inject
    @Named(ISpecifiedElementWriter.SMART_ELEMENT_WRITER)
    private ISpecifiedElementWriter smartFeatureWriter;

    @Inject
    private IQnameProvider nameProvider;

    @Inject
    private MetadataObjectFeatureOrderProvider featureOrderProvider;

    @Inject
    private IResourceLookup resourceLookup;

    @Override
    public void write(YamlStreamWriter writer, EObject parent, EStructuralFeature feature, boolean writeEmpty,
        Version version, Map<String, Object> group) throws ExportException
    {
        if (feature == null)
        {
            writeMdObject(writer, nameProvider.getClassQName(parent.eClass()), parent, version, group);
        }
        else if (feature.isMany())
        {
            for (Object object : (List<?>)parent.eGet(feature))
            {
                writeMdObject(writer, nameProvider.getElementQName(feature), object, version, group);
            }
        }
        else
        {
            writeMdObject(writer, nameProvider.getElementQName(feature), parent.eGet(feature), version, group);
        }
    }

    private Path getConfigurationUnsupportedPart(IProject project)
    {
        Path projectPath = Paths.get(project.getLocationURI());
        return projectPath.resolve("unknown").resolve("Configuration.part"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    private boolean isChildObjectsListEmpty(MdObject mdObject, List<EStructuralFeature> childObjectFeatureList,
        Version version) throws ExportException
    {
        for (EStructuralFeature feature : childObjectFeatureList)
        {
            Object value = mdObject.eGet(feature);
            if (value instanceof Collection && !((Collection<?>)value).isEmpty())
            {
                return false;
            }
        }

        if (mdObject instanceof com._1c.g5.v8.dt.metadata.mdclass.Configuration)
        {
            final Path path = getConfigurationUnsupportedPart(resourceLookup.getProject(mdObject));
            if (Files.isRegularFile(path, new LinkOption[0]))
            {
                try (Stream<String> lines = Files.lines(path);)
                {
                    return !lines.findFirst().isPresent();
                }
                catch (IOException e)
                {
                    throw new ExportException(e.getMessage(), e);
                }
            }
        }

        return true;
    }

    private boolean isFeatureOnlyForExtension(EStructuralFeature feature, Version version)
    {
        return !(feature != Literals.MD_OBJECT__EXTENDED_CONFIGURATION_OBJECT
            && feature != Literals.CONFIGURATION__CONFIGURATION_EXTENSION_PURPOSE
            && feature != Literals.CONFIGURATION__KEEP_MAPPING_TO_EXTENDED_CONFIGURATION_OBJECTS_BY_IDS);
    }

    private boolean isWriteEmpty(MdObject mdObject, EStructuralFeature feature, Version version)
    {
        return !(feature == Literals.MD_OBJECT__EXTENDED_CONFIGURATION_OBJECT);
    }

    protected List<EStructuralFeature> getChildrenFeatureList(MdObject mdObject, Version version)
    {
        return featureOrderProvider.getChildren(mdObject.eClass(), version);
    }

    protected List<EStructuralFeature> getInnerInfoFeatureList(MdObject mdObject, Version version)
    {
        return featureOrderProvider.getInnerInfo(mdObject.eClass(), version);
    }

    protected List<EStructuralFeature> getPropertiesFeatureList(MdObject mdObject, Version version)
    {
        return featureOrderProvider.getProperties(mdObject.eClass(), version);
    }

    protected boolean isFeatureSupportedByVersion(EStructuralFeature feature, Version version)
    {
        if (version.isLessThan(Version.V8_3_8))
        {
            if (feature == Literals.CONFIGURATION__SYNCHRONOUS_PLATFORM_EXTENSION_AND_ADD_IN_CALL_USE_MODE)
            {
                return false;
            }
        }
        else if (feature == Literals.CONFIGURATION__SYNCHRONOUS_EXTENSION_AND_ADD_IN_CALL_USE_MODE)
        {
            return false;
        }

        if (version.isLessThan(Version.V8_3_9))
        {
            if (feature == Literals.ACCOUNTING_REGISTER__PERIOD_ADJUSTMENT_LENGTH)
            {
                return false;
            }
        }

        if (version.isLessThan(Version.V8_3_10)
            && (feature == Literals.CONFIGURATION__MAIN_CLIENT_APPLICATION_WINDOW_MODE
                || feature == Literals.COMMON_ATTRIBUTE__CONFIGURATION_EXTENSIONS_SEPARATION))
        {
            return false;
        }

        if (version.isLessThan(Version.V8_3_12) && feature == Literals.EXCHANGE_PLAN__INCLUDE_CONFIGURATION_EXTENSIONS)
        {
            return false;
        }

        if (version.isLessThan(Version.V8_3_13)
            && IAvailableFeaturesByVersionProvider.SINCE_8_3_13_FEATURES.contains(feature))
        {
            return false;
        }

        if (version.isLessThan(Version.V8_3_15))
        {
            if (IAvailableFeaturesByVersionProvider.SINCE_8_3_15_FEATURES.contains(feature))
            {
                return false;
            }
        }
        else if (feature == Literals.CONFIGURATION__REQUIRED_MOBILE_APPLICATION_PERMISSIONS)
        {
            return false;
        }

        if (version.isLessThan(Version.V8_3_16))
        {
            if (IAvailableFeaturesByVersionProvider.SINCE_8_3_16_FEATURES.contains(feature))
            {
                return false;
            }
        }

        if (version.isLessThan(Version.V8_3_18))
        {
            if (IAvailableFeaturesByVersionProvider.SINCE_8_3_18_FEATURES.contains(feature))
            {
                return false;
            }
        }

        if (version.isLessThan(Version.V8_3_19))
        {
            if (IAvailableFeaturesByVersionProvider.SINCE_8_3_19_FEATURES.contains(feature))
            {
                return false;
            }
        }

        if (version.isLessThan(Version.V8_3_21))
        {
            if (IAvailableFeaturesByVersionProvider.SINCE_8_3_21_FEATURES.contains(feature))
            {
                return false;
            }
        }

        return true;
    }

    protected void writeMdObject(YamlStreamWriter writer, QName name, Object object, Version version,
        Map<String, Object> group) throws ExportException
    {
        if (!(object instanceof MdObject))
        {
            return;
        }

        MdObject mdObject = (MdObject)object;

        if (!(mdObject instanceof com._1c.g5.v8.dt.metadata.mdclass.Configuration))
        {
            if (mdObject instanceof com._1c.g5.v8.dt.metadata.mdclass.CommonModule)
            {
                writer.writeElement(IYamlElements.ELEMENT_KIND, IMetadataYamlElements.COMMON_MODULE, group);
            }
            else if (mdObject instanceof com._1c.g5.v8.dt.metadata.mdclass.HTTPService)
            {
                writer.writeElement(IYamlElements.ELEMENT_KIND, IMetadataYamlElements.HTTP_SERVICE, group);
            }
            else if (mdObject instanceof com._1c.g5.v8.dt.metadata.mdclass.Catalog)
            {
                writer.writeElement(IYamlElements.ELEMENT_KIND, IMetadataYamlElements.CATALOG, group);
            }
            else if (mdObject instanceof com._1c.g5.v8.dt.metadata.mdclass.Enum)
            {
                writer.writeElement(IYamlElements.ELEMENT_KIND, IMetadataYamlElements.ENUM, group);
            }
            else if (mdObject instanceof com._1c.g5.v8.dt.metadata.mdclass.InformationRegister)
            {
                writer.writeElement(IYamlElements.ELEMENT_KIND, IMetadataYamlElements.INFORMATION_REGISTER, group);
            }
            else
            {
                writer.writeElement(IYamlElements.ELEMENT_KIND, "НеизвестныйТип_" + mdObject.getClass(), group);
            }

            writer.writeElement(IYamlElements.VisibilityScope.NAME, IYamlElements.VisibilityScope.PROJECT, group);
        }

        writer.writeElement(IMetadataYamlElements.ID, mdObject.getUuid(), group);

        List<EStructuralFeature> innerInfoList = getInnerInfoFeatureList(mdObject, version);
        if (!innerInfoList.isEmpty())
        {
            Map<String, Object> internalInfoGroup = writer.addGroup("INTERNAL_INFO", group);
            for (EStructuralFeature feature : innerInfoList)
            {
                writeMdObjectInternalInfo(writer, version, mdObject, feature, internalInfoGroup);
            }
            writer.removeEmptyGroup("INTERNAL_INFO", group);
        }

        List<EStructuralFeature> propertiesList = getPropertiesFeatureList(mdObject, version);
        if (!propertiesList.isEmpty())
        {
            for (EStructuralFeature feature : propertiesList)
            {
                writeMdObjectProperty(writer, version, mdObject, feature, group);
            }
        }

        List<EStructuralFeature> childFeaturesList = getChildrenFeatureList(mdObject, version);
        if (!childFeaturesList.isEmpty())
        {
            if (!isChildObjectsListEmpty(mdObject, childFeaturesList, version))
            {
                for (EStructuralFeature feature : childFeaturesList)
                {
                    writeMdObjectChildObject(writer, version, mdObject, feature, group);
                }
                if (mdObject instanceof com._1c.g5.v8.dt.metadata.mdclass.Configuration)
                {
                    writeUnsupportedObjectsRefs(writer, version, mdObject, group);
                }
            }
            else
            {
//                writer.writeEmptyElement(IMetadataXmlElements.CHILD_OBJECTS);
            }
        }

    }

    protected void writeMdObjectChildObject(YamlStreamWriter writer, Version version, MdObject mdObject,
        EStructuralFeature feature, Map<String, Object> group) throws ExportException
    {
        writeMdObjectSmartFeature(writer, version, mdObject, feature, group);
    }

    protected void writeMdObjectInternalInfo(YamlStreamWriter writer, Version version, MdObject mdObject,
        EStructuralFeature feature, Map<String, Object> group) throws ExportException
    {
        writeMdObjectSmartFeature(writer, version, mdObject, feature, group);
    }

    protected void writeMdObjectProperty(YamlStreamWriter writer, Version version, MdObject mdObject,
        EStructuralFeature feature, Map<String, Object> group) throws ExportException
    {
        if (!isFeatureOnlyForExtension(feature, version) && isFeatureSupportedByVersion(feature, version))
        {
            writeMdObjectSmartFeature(writer, version, mdObject, feature, group);
        }
    }

    protected void writeMdObjectSmartFeature(YamlStreamWriter writer, Version version, MdObject mdObject,
        EStructuralFeature feature, Map<String, Object> group) throws ExportException
    {
        smartFeatureWriter.write(writer, mdObject, feature, isWriteEmpty(mdObject, feature, version), version, group);
    }

    protected void writeUnsupportedObjectsRefs(YamlStreamWriter writer, Version version, MdObject mdObject,
        Map<String, Object> group) throws ExportException
    {
        Path path = getConfigurationUnsupportedPart(resourceLookup.getProject(mdObject));
        if (Files.exists(path, new java.nio.file.LinkOption[0]))
        {
            try
            {
                for (String line : Files.readAllLines(path, StandardCharsets.UTF_8))
                {
                    int dotIndex = line.indexOf('.');
                    if (dotIndex != -1)
                    {
                        writer.writeElement(new QName(line.substring(0, dotIndex)), line.substring(dotIndex + 1),
                            group);
                    }
                }
            }
            catch (IOException e)
            {
                throw new ExportException(e.getMessage(), e);
            }
        }
    }

    private static class IAvailableFeaturesByVersionProvider
    {
        static List<EStructuralFeature> SINCE_8_3_21_FEATURES = List.of(Literals.SUBSYSTEM__USE_ONE_COMMAND);

        static List<EStructuralFeature> SINCE_8_3_19_FEATURES =
            List.of(Literals.INTEGRATION_SERVICE_CHANNEL__TRANSACTIONED);

        static List<EStructuralFeature> SINCE_8_3_18_FEATURES =
            List.of(Literals.CONFIGURATION__USED_MOBILE_APPLICATION_FUNCTIONALITIES,
                Literals.CONFIGURATION__MOBILE_APPLICATION_URLS);

        static List<EStructuralFeature> SINCE_8_3_16_FEATURES =
            List.of(Literals.CONFIGURATION__STANDALONE_CONFIGURATION_RESTRICTION_ROLES,
                Literals.COMMON_PICTURE__AVAILABILITY_FOR_CHOICE, Literals.COMMON_PICTURE__AVAILABILITY_FOR_APPEARANCE,
                Literals.BASIC_COMMAND__ON_MAIN_SERVER_UNAVALABLE_BEHAVIOR);

        static List<EStructuralFeature> SINCE_8_3_15_FEATURES =
            List.of(new EStructuralFeature[] { Literals.CONFIGURATION__DEFAULT_COLLABORATION_SYSTEM_USERS_CHOICE_FORM,
                Literals.CONFIGURATION__REQUIRED_MOBILE_APPLICATION_PERMISSIONS8315,
                Literals.CONFIGURATION__KEEP_MAPPING_TO_EXTENDED_CONFIGURATION_OBJECTS_BY_IDS,
                Literals.MD_OBJECT__EXTENDED_CONFIGURATION_OBJECT,
                Literals.CATALOG__UPDATE_DATA_HISTORY_IMMEDIATELY_AFTER_WRITE,
                Literals.CATALOG__EXECUTE_AFTER_WRITE_DATA_HISTORY_VERSION_PROCESSING,
                Literals.DOCUMENT__UPDATE_DATA_HISTORY_IMMEDIATELY_AFTER_WRITE,
                Literals.DOCUMENT__EXECUTE_AFTER_WRITE_DATA_HISTORY_VERSION_PROCESSING,
                Literals.CHART_OF_ACCOUNTS__UPDATE_DATA_HISTORY_IMMEDIATELY_AFTER_WRITE,
                Literals.CHART_OF_ACCOUNTS__EXECUTE_AFTER_WRITE_DATA_HISTORY_VERSION_PROCESSING,
                Literals.CHART_OF_CALCULATION_TYPES__UPDATE_DATA_HISTORY_IMMEDIATELY_AFTER_WRITE,
                Literals.CHART_OF_CALCULATION_TYPES__EXECUTE_AFTER_WRITE_DATA_HISTORY_VERSION_PROCESSING,
                Literals.CHART_OF_CHARACTERISTIC_TYPES__UPDATE_DATA_HISTORY_IMMEDIATELY_AFTER_WRITE,
                Literals.CHART_OF_CHARACTERISTIC_TYPES__EXECUTE_AFTER_WRITE_DATA_HISTORY_VERSION_PROCESSING,
                Literals.INFORMATION_REGISTER__UPDATE_DATA_HISTORY_IMMEDIATELY_AFTER_WRITE,
                Literals.INFORMATION_REGISTER__EXECUTE_AFTER_WRITE_DATA_HISTORY_VERSION_PROCESSING,
                Literals.BUSINESS_PROCESS__UPDATE_DATA_HISTORY_IMMEDIATELY_AFTER_WRITE,
                Literals.BUSINESS_PROCESS__EXECUTE_AFTER_WRITE_DATA_HISTORY_VERSION_PROCESSING,
                Literals.EXCHANGE_PLAN__UPDATE_DATA_HISTORY_IMMEDIATELY_AFTER_WRITE,
                Literals.EXCHANGE_PLAN__EXECUTE_AFTER_WRITE_DATA_HISTORY_VERSION_PROCESSING,
                Literals.CONSTANT__UPDATE_DATA_HISTORY_IMMEDIATELY_AFTER_WRITE,
                Literals.CONSTANT__EXECUTE_AFTER_WRITE_DATA_HISTORY_VERSION_PROCESSING,
                Literals.TASK__UPDATE_DATA_HISTORY_IMMEDIATELY_AFTER_WRITE,
                Literals.TASK__EXECUTE_AFTER_WRITE_DATA_HISTORY_VERSION_PROCESSING });

        static List<EStructuralFeature> SINCE_8_3_13_FEATURES =
            List.of(Literals.CONFIGURATION__DEFAULT_DATA_HISTORY_CHANGE_HISTORY_FORM,
                Literals.CONFIGURATION__DEFAULT_DATA_HISTORY_VERSION_DATA_FORM,
                Literals.CONFIGURATION__DEFAULT_DATA_HISTORY_VERSION_DIFFERENCES_FORM);
    }
}
