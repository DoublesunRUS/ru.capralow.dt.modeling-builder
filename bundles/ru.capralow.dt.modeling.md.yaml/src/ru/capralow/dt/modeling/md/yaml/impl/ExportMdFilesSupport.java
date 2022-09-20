/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.yaml.impl;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.EcoreUtil2;

import com._1c.g5.v8.dt.binary.model.BinaryData;
import com._1c.g5.v8.dt.common.StringUtils;
import com._1c.g5.v8.dt.mcore.Help;
import com._1c.g5.v8.dt.mcore.HelpPage;
import com._1c.g5.v8.dt.metadata.mdclass.AbstractAggregates;
import com._1c.g5.v8.dt.metadata.mdclass.AbstractFlowchart;
import com._1c.g5.v8.dt.metadata.mdclass.BasicTemplate;
import com._1c.g5.v8.dt.metadata.mdclass.BusinessProcess;
import com._1c.g5.v8.dt.metadata.mdclass.CommonPicture;
import com._1c.g5.v8.dt.metadata.mdclass.ExchangePlan;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;
import com._1c.g5.v8.dt.metadata.mdclass.MdPicture;
import com._1c.g5.v8.dt.metadata.mdclass.Predefined;
import com._1c.g5.v8.dt.metadata.mdclass.WSReference;
import com._1c.g5.v8.dt.platform.version.Version;
import com._1c.g5.v8.dt.ws.wsdefinitions.model.WSDefinitions;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.core.IExportFileSystemSupport;
import ru.capralow.dt.modeling.yaml.BaseYamlExportFileSystemSupport;

@Singleton
public class ExportMdFilesSupport
    extends BaseYamlExportFileSystemSupport
    implements IExportFileSystemSupport
{
    private static final String HELP = "Help";

    private static final String PREDEFINED = "Predefined";

    private static final String TEMPLATE = "Template";

    private static final String PICTURE = "Picture";

    private static final String CONTENT = "Content";

    private static final String WS_DEFINITION = "WSDefinition";

    private static final String AGGREGATES_FILE_NAME = "Aggregates.xml";

    private static final String PARENT_CONFIGURATIONS_FILE_NAME = "ParentConfigurations.bin";

    private static final String YAML_EXT = "yaml"; //$NON-NLS-1$

    private static final String DOT_HTML_EXT = ".html";

    private static final String BINARY_EXT = "bin"; //$NON-NLS-1$

    private static final String TEXT_EXT = "txt"; //$NON-NLS-1$

    private static final String WS_DEFINITION_XML = "WSDefinition.xml";

    private static final String CONTENT_XML = "Content.xml";

    private static final String HELP_XML = "Help.xml";

    private static final String PREDEFINED_XML = "Predefined.xml";

    private static final String PICTURE_XML = "Picture.xml";

    private static final String FLOWCHART_SCHEME = "Flowchart.xml";

    @Override
    public Path getFileName(EObject eObject, EStructuralFeature feature, Version version)
    {
        if (feature == null)
        {
            if (eObject instanceof com._1c.g5.v8.dt.metadata.mdclass.Configuration)
            {
                return Paths.get("Проект.yaml", new String[0]); //$NON-NLS-1$
            }
            if (eObject instanceof MdObject)
            {
                Path targetPath = getMdObjectTargetDirectory((MdObject)eObject);
                Path prefix = targetPath.getFileName();
                if (String.valueOf(prefix.getFileName()).equals("-")) //$NON-NLS-1$
                {
                    prefix = prefix.resolveSibling(""); //$NON-NLS-1$
                }
                String fileName = String.valueOf(((MdObject)eObject).getName()) + "." + YAML_EXT; //$NON-NLS-1$
                return targetPath.resolveSibling(prefix + fileName);
            }
//            if (eObject instanceof Help)
//            {
//                return getHelpPath((Help)eObject);
//            }
//            if (eObject instanceof HelpPage)
//            {
//                return getHelpPagePath((HelpPage)eObject);
//            }
//            if (eObject instanceof Predefined)
//            {
//                return getPredefinedPath((Predefined)eObject);
//            }
//            if (eObject instanceof AbstractFlowchart)
//            {
//                return getBusinessProcessFlowchartPath((AbstractFlowchart)eObject);
//            }
//            if (eObject instanceof MdPicture)
//            {
//                return getMdPicturePath((MdPicture)eObject);
//            }
//            if (eObject instanceof AbstractAggregates)
//            {
//                return getAbstractAggregatesPath((AbstractAggregates)eObject);
//            }
//            if (eObject instanceof WSDefinitions)
//            {
//                return getWsDefinitionsPath((WSDefinitions)eObject);
//            }
//            if (eObject instanceof BinaryData)
//            {
//                EReference reference = getExternalPropertyManager(eObject).getReference(eObject);
//                if (reference == MdClassPackage.Literals.CONFIGURATION__PARENT_CONFIGURATIONS)
//                {
//                    return getParentConfigurationsPath((BinaryData)eObject);
//                }
//            }
        }
        else
        {
//            if (eObject instanceof CommonPicture && feature == McorePackage.Literals.PICTURE_REF__PICTURE)
//            {
//                return getCommonPicturePicturePath((CommonPicture)eObject);
//            }
//            if (eObject instanceof ExchangePlan && feature == MdClassPackage.Literals.EXCHANGE_PLAN__CONTENT)
//            {
//                return getExchangePlanContentPath((ExchangePlan)eObject);
//            }
//            if (eObject instanceof WSReference && feature == MdClassPackage.Literals.WS_REFERENCE__WS_DEFINITIONS)
//            {
//                return getWsReferenceDefinitionsPath((WSReference)eObject);
//            }
//            if (eObject instanceof BasicTemplate && feature == MdClassPackage.Literals.BASIC_TEMPLATE__TEMPLATE)
//            {
//                return getBasicTemplateTemplatePath((BasicTemplate)eObject);
//            }
        }

        return null;
    }

    private Path getAbstractAggregatesPath(AbstractAggregates aggregates)
    {
        MdObject aggregatesContainer = EcoreUtil2.getContainerOfType(aggregates.eContainer(), MdObject.class);
        return getMdObjectTargetDirectory(aggregatesContainer)
            .resolve(Paths.get(aggregatesContainer.getName(), new String[] { "Ext", "Aggregates.xml" }));
    }

    private String getBasicTemplateExtension(BasicTemplate template)
    {
        switch (template.getTemplateType())
        {
        case BINARY_DATA:
        case ACTIVE_DOCUMENT:
        case ADD_IN:
            return BINARY_EXT;
        case SPREADSHEET_DOCUMENT:
        case HTML_DOCUMENT:
        case GEOGRAPHICAL_SCHEMA:
        case GRAPHICAL_SCHEMA:
        case DATA_COMPOSITION_SCHEMA:
        case DATA_COMPOSITION_APPEARANCE_TEMPLATE:
            return YAML_EXT;
        case TEXT_DOCUMENT:
            return TEXT_EXT;
        default:
            return null;
        }
    }

    private Path getBasicTemplateTemplatePath(BasicTemplate template)
    {
        Path templatePath =
            Paths.get(template.getName(), new String[] { "Ext", "Template." + getBasicTemplateExtension(template) });
        return getMdObjectTargetDirectory(template).resolve(templatePath);
    }

    private Path getBusinessProcessFlowchartPath(AbstractFlowchart flowChart)
    {
        BusinessProcess owner =
            getExternalPropertyManager(flowChart).getOwner((EObject)flowChart, BusinessProcess.class);
        return getMdObjectTargetDirectory(owner)
            .resolve(Paths.get(owner.getName(), new String[] { "Ext", "Flowchart.xml" }));
    }

    private Path getCommonPicturePicturePath(CommonPicture eObject)
    {
        return getMdObjectTargetDirectory(eObject)
            .resolve(Paths.get(eObject.getName(), new String[] { "Ext", "Picture.xml" }));
    }

    private Path getExchangePlanContentPath(ExchangePlan eObject)
    {
        return getMdObjectTargetDirectory(eObject)
            .resolve(Paths.get(eObject.getName(), new String[] { "Ext", "Content.xml" }));
    }

    private Path getHelpPagePath(HelpPage helpPage)
    {
        MdObject helpContainer = EcoreUtil2.getContainerOfType((EObject)helpPage, MdObject.class);
        Path helpPagePath = (helpContainer instanceof com._1c.g5.v8.dt.metadata.mdclass.Configuration)
            ? Paths.get("Ext", new String[] { "Help", String.valueOf(helpPage.getLang()) + ".html" }) : Paths.get(
                helpContainer.getName(), new String[] { "Ext", "Help", String.valueOf(helpPage.getLang()) + ".html" });
        return getMdObjectTargetDirectory(helpContainer).resolve(helpPagePath);
    }

    private Path getHelpPath(Help help)
    {
        MdObject helpContainer = EcoreUtil2.getContainerOfType((EObject)help, MdObject.class);
        Path helpPath = (helpContainer instanceof com._1c.g5.v8.dt.metadata.mdclass.Configuration)
            ? Paths.get("Ext", new String[] { "Help.xml" })
            : Paths.get(helpContainer.getName(), new String[] { "Ext", "Help.xml" });
        return getMdObjectTargetDirectory(helpContainer).resolve(helpPath);
    }

    private Path getMdPicturePath(MdPicture mdPicture)
    {
        String pictureName = String.valueOf(StringUtils.capitalize(mdPicture.eContainingFeature().getName())) + ".xml";
        MdObject pictureContainer = EcoreUtil2.getContainerOfType((EObject)mdPicture, MdObject.class);
        Path picturePath = (pictureContainer instanceof com._1c.g5.v8.dt.metadata.mdclass.Configuration)
            ? Paths.get("Ext", new String[] { pictureName })
            : Paths.get(pictureContainer.getName(), new String[] { "Ext", pictureName });
        return getMdObjectTargetDirectory(pictureContainer).resolve(picturePath);
    }

    private Path getParentConfigurationsPath(BinaryData eObject)
    {
        return Path.of("Ext", new String[] { "ParentConfigurations.bin" });
    }

    private Path getPredefinedPath(Predefined predefined)
    {
        MdObject predefinedContainer = EcoreUtil2.getContainerOfType((EObject)predefined, MdObject.class);
        return getMdObjectTargetDirectory(predefinedContainer)
            .resolve(Paths.get(predefinedContainer.getName(), new String[] { "Ext", "Predefined.xml" }));
    }

    private Path getWsDefinitionsPath(WSDefinitions eObject)
    {
        WSReference wsReference = getExternalPropertyManager(eObject).getOwner((EObject)eObject, WSReference.class);
        return getWsReferenceDefinitionsPath(wsReference);
    }

    private Path getWsReferenceDefinitionsPath(WSReference wsReference)
    {
        return getMdObjectTargetDirectory(wsReference)
            .resolve(Paths.get(wsReference.getName(), new String[] { "Ext", "WSDefinition.xml" }));
    }
}
