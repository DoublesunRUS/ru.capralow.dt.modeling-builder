/**
 *
 */
package ru.capralow.dt.modeling.md.yaml.impl;

import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.mcore.McorePackage;
import com._1c.g5.v8.dt.metadata.common.CommonPackage;
import com._1c.g5.v8.dt.metadata.mdclass.MdClassFactory;
import com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.md.yaml.IMetadataXmlElements;
import ru.capralow.dt.modeling.yaml.BaseQNameProvider;

@Singleton
public class MetadataFeatureNameProvider
    extends BaseQNameProvider
{
    @Override
    protected void fillSpecifiedClassNames(ImmutableMap.Builder<EClass, QName> builder)
    {
        for (EClassifier classifier : MdClassFactory.eINSTANCE.getEPackage().getEClassifiers())
        {
            if (classifier instanceof EClass)
            {
                EClass eClass = (EClass)classifier;
                if (eClass != MdClassPackage.Literals.COMMON_FORM && !eClass.isAbstract() && !eClass.isInterface()
                    && MdClassPackage.Literals.BASIC_FORM.isSuperTypeOf(eClass))
                {
                    builder.put(eClass, IMetadataXmlElements.FORM);
                }
                if (eClass != MdClassPackage.Literals.COMMON_TEMPLATE && !eClass.isAbstract() && !eClass.isInterface()
                    && MdClassPackage.Literals.BASIC_TEMPLATE.isSuperTypeOf(eClass))
                {
                    builder.put(eClass, IMetadataXmlElements.TEMPLATE);
                }
            }
        }
    }

    @Override
    protected void fillSpecifiedFeatureNames(ImmutableMap.Builder<EStructuralFeature, QName> builder)
    {
        builder.put(MdClassPackage.Literals.DB_OBJECT_TABULAR_SECTION__ATTRIBUTES, IMetadataXmlElements.ATTRIBUTE);
        builder.put(MdClassPackage.Literals.REPORT_TABULAR_SECTION__ATTRIBUTES, IMetadataXmlElements.ATTRIBUTE);
        builder.put(MdClassPackage.Literals.DATA_PROCESSOR_TABULAR_SECTION__ATTRIBUTES, IMetadataXmlElements.ATTRIBUTE);
        builder.put(MdClassPackage.Literals.CONFIGURATION__LANGUAGES, IMetadataXmlElements.LANGUAGE);
        builder.put(MdClassPackage.Literals.CONFIGURATION__SUBSYSTEMS, IMetadataXmlElements.SUBSYSTEM);
        builder.put(MdClassPackage.Literals.CONFIGURATION__STYLE_ITEMS, IMetadataXmlElements.STYLE_ITEM);
        builder.put(MdClassPackage.Literals.CONFIGURATION__STYLES, IMetadataXmlElements.STYLE);
        builder.put(MdClassPackage.Literals.CONFIGURATION__COMMON_PICTURES, IMetadataXmlElements.COMMON_PICTURE);
        builder.put(MdClassPackage.Literals.CONFIGURATION__INTERFACES, IMetadataXmlElements.INTERFACE);
        builder.put(MdClassPackage.Literals.CONFIGURATION__SESSION_PARAMETERS, IMetadataXmlElements.SESSION_PARAMETER);
        builder.put(MdClassPackage.Literals.CONFIGURATION__ROLES, IMetadataXmlElements.ROLE);
        builder.put(MdClassPackage.Literals.CONFIGURATION__COMMON_TEMPLATES, IMetadataXmlElements.COMMON_TEMPLATE);
        builder.put(MdClassPackage.Literals.CONFIGURATION__FILTER_CRITERIA, IMetadataXmlElements.FILTER_CRITERION);
        builder.put(MdClassPackage.Literals.CONFIGURATION__COMMON_MODULES, IMetadataXmlElements.COMMON_MODULE);
        builder.put(MdClassPackage.Literals.CONFIGURATION__COMMON_ATTRIBUTES, IMetadataXmlElements.COMMON_ATTRIBUTE);
        builder.put(MdClassPackage.Literals.CONFIGURATION__EXCHANGE_PLANS, IMetadataXmlElements.EXCHANGE_PLAN);
        builder.put(MdClassPackage.Literals.CONFIGURATION__XDTO_PACKAGES, IMetadataXmlElements.XDTO_PACKAGE);
        builder.put(MdClassPackage.Literals.CONFIGURATION__WEB_SERVICES, IMetadataXmlElements.WEB_SERVICE);
        builder.put(MdClassPackage.Literals.CONFIGURATION__HTTP_SERVICES, IMetadataXmlElements.HTTP_SERVICE);
        builder.put(MdClassPackage.Literals.CONFIGURATION__WS_REFERENCES, IMetadataXmlElements.WS_REFERENCE);
        builder.put(MdClassPackage.Literals.CONFIGURATION__EVENT_SUBSCRIPTIONS,
            IMetadataXmlElements.EVENT_SUBSCRIPTION);
        builder.put(MdClassPackage.Literals.CONFIGURATION__SCHEDULED_JOBS, IMetadataXmlElements.SCHEDULED_JOB);
        builder.put(MdClassPackage.Literals.CONFIGURATION__SETTINGS_STORAGES, IMetadataXmlElements.SETTINGS_STORAGE);
        builder.put(MdClassPackage.Literals.CONFIGURATION__FUNCTIONAL_OPTIONS, IMetadataXmlElements.FUNCTIONAL_OPTION);
        builder.put(MdClassPackage.Literals.CONFIGURATION__FUNCTIONAL_OPTIONS_PARAMETERS,
            IMetadataXmlElements.FUNCTIONAL_OPTIONS_PARAMETER);
        builder.put(MdClassPackage.Literals.CONFIGURATION__DEFINED_TYPES, IMetadataXmlElements.DEFINED_TYPE);
        builder.put(MdClassPackage.Literals.CONFIGURATION__COMMON_COMMANDS, IMetadataXmlElements.COMMON_COMMAND);
        builder.put(MdClassPackage.Literals.CONFIGURATION__COMMAND_GROUPS, IMetadataXmlElements.COMMAND_GROUP);
        builder.put(MdClassPackage.Literals.CONFIGURATION__CONSTANTS, IMetadataXmlElements.CONSTANT);
        builder.put(MdClassPackage.Literals.CONFIGURATION__COMMON_FORMS, IMetadataXmlElements.COMMON_FORM);
        builder.put(MdClassPackage.Literals.CONFIGURATION__CATALOGS, IMetadataXmlElements.CATALOG);
        builder.put(MdClassPackage.Literals.CONFIGURATION__DOCUMENTS, IMetadataXmlElements.DOCUMENT);
        builder.put(MdClassPackage.Literals.CONFIGURATION__DOCUMENT_NUMERATORS,
            IMetadataXmlElements.DOCUMENT_NUMERATOR);
        builder.put(MdClassPackage.Literals.CONFIGURATION__SEQUENCES, IMetadataXmlElements.SEQUENCE);
        builder.put(MdClassPackage.Literals.CONFIGURATION__DOCUMENT_JOURNALS, IMetadataXmlElements.DOCUMENT_JOURNAL);
        builder.put(MdClassPackage.Literals.CONFIGURATION__ENUMS, IMetadataXmlElements.ENUM);
        builder.put(MdClassPackage.Literals.CONFIGURATION__REPORTS, IMetadataXmlElements.REPORT);
        builder.put(MdClassPackage.Literals.CONFIGURATION__DATA_PROCESSORS, IMetadataXmlElements.DATA_PROCESSOR);
        builder.put(MdClassPackage.Literals.CONFIGURATION__INFORMATION_REGISTERS,
            IMetadataXmlElements.INFORMATION_REGISTER);
        builder.put(MdClassPackage.Literals.CONFIGURATION__ACCUMULATION_REGISTERS,
            IMetadataXmlElements.ACCUMULATION_REGISTER);
        builder.put(MdClassPackage.Literals.CONFIGURATION__CHARTS_OF_CHARACTERISTIC_TYPES,
            IMetadataXmlElements.CHART_OF_CHARACTERISTIC_TYPES);
        builder.put(MdClassPackage.Literals.CONFIGURATION__CHARTS_OF_ACCOUNTS, IMetadataXmlElements.CHART_OF_ACCOUNTS);
        builder.put(MdClassPackage.Literals.CONFIGURATION__ACCOUNTING_REGISTERS,
            IMetadataXmlElements.ACCOUNTING_REGISTER);
        builder.put(MdClassPackage.Literals.CONFIGURATION__CHARTS_OF_CALCULATION_TYPES,
            IMetadataXmlElements.CHART_OF_CALCULATION_TYPES);
        builder.put(MdClassPackage.Literals.CONFIGURATION__CALCULATION_REGISTERS,
            IMetadataXmlElements.CALCULATION_REGISTER);
        builder.put(MdClassPackage.Literals.CONFIGURATION__BUSINESS_PROCESSES, IMetadataXmlElements.BUSINESS_PROCESS);
        builder.put(MdClassPackage.Literals.CONFIGURATION__TASKS, IMetadataXmlElements.TASK);
        builder.put(MdClassPackage.Literals.CONFIGURATION__EXTERNAL_DATA_SOURCES,
            IMetadataXmlElements.EXTERNAL_DATA_SOURCE);
        builder.put(MdClassPackage.Literals.CONFIGURATION__INTEGRATION_SERVICES,
            IMetadataXmlElements.INTEGRATION_SERVICE);
        builder.put(MdClassPackage.Literals.CONFIGURATION__BOTS, IMetadataXmlElements.BOT);
        builder.put(MdClassPackage.Literals.CONFIGURATION__MOBILE_APPLICATION_URLS,
            IMetadataXmlElements.MOBILE_APPLICATION_URLS);
        builder.put(MdClassPackage.Literals.SUBSYSTEM__SUBSYSTEMS, IMetadataXmlElements.SUBSYSTEM);
        builder.put(MdClassPackage.Literals.COMMON_ATTRIBUTE_CONTENT_ITEM__METADATA, IMetadataXmlElements.XR.METADATA);
        builder.put(MdClassPackage.Literals.COMMON_ATTRIBUTE_CONTENT_ITEM__USE, IMetadataXmlElements.XR.USE);
        builder.put(MdClassPackage.Literals.COMMON_ATTRIBUTE_CONTENT_ITEM__CONDITIONAL_SEPARATION,
            IMetadataXmlElements.XR.CONDITIONAL_SEPARATION);
        builder.put(MdClassPackage.Literals.CATALOG__ATTRIBUTES, IMetadataXmlElements.ATTRIBUTE);
        builder.put(MdClassPackage.Literals.CATALOG__TABULAR_SECTIONS, IMetadataXmlElements.TABULAR_SECTION);
        builder.put(MdClassPackage.Literals.CATALOG__FORMS, IMetadataXmlElements.FORM);
        builder.put(MdClassPackage.Literals.CATALOG__TEMPLATES, IMetadataXmlElements.TEMPLATE);
        builder.put(MdClassPackage.Literals.CATALOG__COMMANDS, IMetadataXmlElements.COMMAND);
        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS__ATTRIBUTES, IMetadataXmlElements.ATTRIBUTE);
        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS__TABULAR_SECTIONS, IMetadataXmlElements.TABULAR_SECTION);
        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS__FORMS, IMetadataXmlElements.FORM);
        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS__TEMPLATES, IMetadataXmlElements.TEMPLATE);
        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS__COMMANDS, IMetadataXmlElements.COMMAND);
        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS__ACCOUNTING_FLAGS, IMetadataXmlElements.ACCOUNTING_FLAG);
        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS__EXT_DIMENSION_ACCOUNTING_FLAGS,
            IMetadataXmlElements.EXT_DIMENSION_ACCOUNTING_FLAG);
        builder.put(MdClassPackage.Literals.DOCUMENT__ATTRIBUTES, IMetadataXmlElements.ATTRIBUTE);
        builder.put(MdClassPackage.Literals.DOCUMENT__TABULAR_SECTIONS, IMetadataXmlElements.TABULAR_SECTION);
        builder.put(MdClassPackage.Literals.DOCUMENT__FORMS, IMetadataXmlElements.FORM);
        builder.put(MdClassPackage.Literals.DOCUMENT__TEMPLATES, IMetadataXmlElements.TEMPLATE);
        builder.put(MdClassPackage.Literals.DOCUMENT__COMMANDS, IMetadataXmlElements.COMMAND);
        builder.put(MdClassPackage.Literals.ENUM__ENUM_VALUES, IMetadataXmlElements.ENUM_VALUE);
        builder.put(MdClassPackage.Literals.ENUM__FORMS, IMetadataXmlElements.FORM);
        builder.put(MdClassPackage.Literals.ENUM__TEMPLATES, IMetadataXmlElements.TEMPLATE);
        builder.put(MdClassPackage.Literals.ENUM__COMMANDS, IMetadataXmlElements.COMMAND);
        builder.put(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES__ATTRIBUTES, IMetadataXmlElements.ATTRIBUTE);
        builder.put(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES__TABULAR_SECTIONS,
            IMetadataXmlElements.TABULAR_SECTION);
        builder.put(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES__FORMS, IMetadataXmlElements.FORM);
        builder.put(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES__TEMPLATES, IMetadataXmlElements.TEMPLATE);
        builder.put(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES__COMMANDS, IMetadataXmlElements.COMMAND);
        builder.put(MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES__ATTRIBUTES, IMetadataXmlElements.ATTRIBUTE);
        builder.put(MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES__TABULAR_SECTIONS,
            IMetadataXmlElements.TABULAR_SECTION);
        builder.put(MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES__FORMS, IMetadataXmlElements.FORM);
        builder.put(MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES__TEMPLATES, IMetadataXmlElements.TEMPLATE);
        builder.put(MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES__COMMANDS, IMetadataXmlElements.COMMAND);
        builder.put(MdClassPackage.Literals.INFORMATION_REGISTER__RESOURCES, IMetadataXmlElements.RESOURCE);
        builder.put(MdClassPackage.Literals.INFORMATION_REGISTER__ATTRIBUTES, IMetadataXmlElements.ATTRIBUTE);
        builder.put(MdClassPackage.Literals.INFORMATION_REGISTER__DIMENSIONS, IMetadataXmlElements.DIMENSION);
        builder.put(MdClassPackage.Literals.INFORMATION_REGISTER__FORMS, IMetadataXmlElements.FORM);
        builder.put(MdClassPackage.Literals.INFORMATION_REGISTER__TEMPLATES, IMetadataXmlElements.TEMPLATE);
        builder.put(MdClassPackage.Literals.INFORMATION_REGISTER__COMMANDS, IMetadataXmlElements.COMMAND);
        builder.put(MdClassPackage.Literals.EXCHANGE_PLAN__ATTRIBUTES, IMetadataXmlElements.ATTRIBUTE);
        builder.put(MdClassPackage.Literals.EXCHANGE_PLAN__TABULAR_SECTIONS, IMetadataXmlElements.TABULAR_SECTION);
        builder.put(MdClassPackage.Literals.EXCHANGE_PLAN__FORMS, IMetadataXmlElements.FORM);
        builder.put(MdClassPackage.Literals.EXCHANGE_PLAN__TEMPLATES, IMetadataXmlElements.TEMPLATE);
        builder.put(MdClassPackage.Literals.EXCHANGE_PLAN__COMMANDS, IMetadataXmlElements.COMMAND);
        builder.put(MdClassPackage.Literals.EXCHANGE_PLAN_CONTENT_ITEM__MD_OBJECT, IMetadataXmlElements.EXT.METADATA);
        builder.put(MdClassPackage.Literals.EXCHANGE_PLAN_CONTENT_ITEM__AUTO_RECORD,
            IMetadataXmlElements.EXT.AUTO_RECORD);
        builder.put(MdClassPackage.Literals.SEQUENCE__DIMENSIONS, IMetadataXmlElements.DIMENSION);
        builder.put(MdClassPackage.Literals.DOCUMENT_JOURNAL__COLUMNS, IMetadataXmlElements.COLUMN);
        builder.put(MdClassPackage.Literals.DOCUMENT_JOURNAL__FORMS, IMetadataXmlElements.FORM);
        builder.put(MdClassPackage.Literals.DOCUMENT_JOURNAL__TEMPLATES, IMetadataXmlElements.TEMPLATE);
        builder.put(MdClassPackage.Literals.DOCUMENT_JOURNAL__COMMANDS, IMetadataXmlElements.COMMAND);
        builder.put(MdClassPackage.Literals.REPORT__ATTRIBUTES, IMetadataXmlElements.ATTRIBUTE);
        builder.put(MdClassPackage.Literals.REPORT__TABULAR_SECTIONS, IMetadataXmlElements.TABULAR_SECTION);
        builder.put(MdClassPackage.Literals.REPORT__FORMS, IMetadataXmlElements.FORM);
        builder.put(MdClassPackage.Literals.REPORT__TEMPLATES, IMetadataXmlElements.TEMPLATE);
        builder.put(MdClassPackage.Literals.REPORT__COMMANDS, IMetadataXmlElements.COMMAND);
        builder.put(MdClassPackage.Literals.EXTERNAL_REPORT__ATTRIBUTES, IMetadataXmlElements.ATTRIBUTE);
        builder.put(MdClassPackage.Literals.EXTERNAL_REPORT__TABULAR_SECTIONS, IMetadataXmlElements.TABULAR_SECTION);
        builder.put(MdClassPackage.Literals.EXTERNAL_REPORT__FORMS, IMetadataXmlElements.FORM);
        builder.put(MdClassPackage.Literals.EXTERNAL_REPORT__TEMPLATES, IMetadataXmlElements.TEMPLATE);
        builder.put(MdClassPackage.Literals.DATA_PROCESSOR__ATTRIBUTES, IMetadataXmlElements.ATTRIBUTE);
        builder.put(MdClassPackage.Literals.DATA_PROCESSOR__TABULAR_SECTIONS, IMetadataXmlElements.TABULAR_SECTION);
        builder.put(MdClassPackage.Literals.DATA_PROCESSOR__FORMS, IMetadataXmlElements.FORM);
        builder.put(MdClassPackage.Literals.DATA_PROCESSOR__TEMPLATES, IMetadataXmlElements.TEMPLATE);
        builder.put(MdClassPackage.Literals.DATA_PROCESSOR__COMMANDS, IMetadataXmlElements.COMMAND);
        builder.put(MdClassPackage.Literals.EXTERNAL_DATA_PROCESSOR__ATTRIBUTES, IMetadataXmlElements.ATTRIBUTE);
        builder.put(MdClassPackage.Literals.EXTERNAL_DATA_PROCESSOR__TABULAR_SECTIONS,
            IMetadataXmlElements.TABULAR_SECTION);
        builder.put(MdClassPackage.Literals.EXTERNAL_DATA_PROCESSOR__FORMS, IMetadataXmlElements.FORM);
        builder.put(MdClassPackage.Literals.EXTERNAL_DATA_PROCESSOR__TEMPLATES, IMetadataXmlElements.TEMPLATE);
        builder.put(MdClassPackage.Literals.EXTERNAL_DATA_SOURCE__CUBES, IMetadataXmlElements.CUBE);
        builder.put(MdClassPackage.Literals.EXTERNAL_DATA_SOURCE__TABLES, IMetadataXmlElements.TABLE);
        builder.put(MdClassPackage.Literals.EXTERNAL_DATA_SOURCE__FUNCTIONS, IMetadataXmlElements.FUNCTION);
        builder.put(MdClassPackage.Literals.ACCOUNTING_REGISTER__RESOURCES, IMetadataXmlElements.RESOURCE);
        builder.put(MdClassPackage.Literals.ACCOUNTING_REGISTER__ATTRIBUTES, IMetadataXmlElements.ATTRIBUTE);
        builder.put(MdClassPackage.Literals.ACCOUNTING_REGISTER__DIMENSIONS, IMetadataXmlElements.DIMENSION);
        builder.put(MdClassPackage.Literals.ACCOUNTING_REGISTER__FORMS, IMetadataXmlElements.FORM);
        builder.put(MdClassPackage.Literals.ACCOUNTING_REGISTER__TEMPLATES, IMetadataXmlElements.TEMPLATE);
        builder.put(MdClassPackage.Literals.ACCOUNTING_REGISTER__COMMANDS, IMetadataXmlElements.COMMAND);
        builder.put(MdClassPackage.Literals.ACCUMULATION_REGISTER__RESOURCES, IMetadataXmlElements.RESOURCE);
        builder.put(MdClassPackage.Literals.ACCUMULATION_REGISTER__ATTRIBUTES, IMetadataXmlElements.ATTRIBUTE);
        builder.put(MdClassPackage.Literals.ACCUMULATION_REGISTER__DIMENSIONS, IMetadataXmlElements.DIMENSION);
        builder.put(MdClassPackage.Literals.ACCUMULATION_REGISTER__FORMS, IMetadataXmlElements.FORM);
        builder.put(MdClassPackage.Literals.ACCUMULATION_REGISTER__TEMPLATES, IMetadataXmlElements.TEMPLATE);
        builder.put(MdClassPackage.Literals.ACCUMULATION_REGISTER__COMMANDS, IMetadataXmlElements.COMMAND);
        builder.put(MdClassPackage.Literals.BUSINESS_PROCESS__ATTRIBUTES, IMetadataXmlElements.ATTRIBUTE);
        builder.put(MdClassPackage.Literals.BUSINESS_PROCESS__TABULAR_SECTIONS, IMetadataXmlElements.TABULAR_SECTION);
        builder.put(MdClassPackage.Literals.BUSINESS_PROCESS__FORMS, IMetadataXmlElements.FORM);
        builder.put(MdClassPackage.Literals.BUSINESS_PROCESS__TEMPLATES, IMetadataXmlElements.TEMPLATE);
        builder.put(MdClassPackage.Literals.BUSINESS_PROCESS__COMMANDS, IMetadataXmlElements.COMMAND);
        builder.put(MdClassPackage.Literals.TASK__ATTRIBUTES, IMetadataXmlElements.ATTRIBUTE);
        builder.put(MdClassPackage.Literals.TASK__TABULAR_SECTIONS, IMetadataXmlElements.TABULAR_SECTION);
        builder.put(MdClassPackage.Literals.TASK__FORMS, IMetadataXmlElements.FORM);
        builder.put(MdClassPackage.Literals.TASK__TEMPLATES, IMetadataXmlElements.TEMPLATE);
        builder.put(MdClassPackage.Literals.TASK__ADDRESSING_ATTRIBUTES, IMetadataXmlElements.ADDRESSING_ATTRIBUTE);
        builder.put(MdClassPackage.Literals.TASK__COMMANDS, IMetadataXmlElements.COMMAND);
        builder.put(MdClassPackage.Literals.CUBE__DIMENSION_TABLES, IMetadataXmlElements.DIMENSION_TABLE);
        builder.put(MdClassPackage.Literals.CUBE__DIMENSIONS, IMetadataXmlElements.DIMENSION);
        builder.put(MdClassPackage.Literals.CUBE__RESOURCES, IMetadataXmlElements.RESOURCE);
        builder.put(MdClassPackage.Literals.CUBE__FORMS, IMetadataXmlElements.FORM);
        builder.put(MdClassPackage.Literals.CUBE__TEMPLATES, IMetadataXmlElements.TEMPLATE);
        builder.put(MdClassPackage.Literals.CUBE__COMMANDS, IMetadataXmlElements.COMMAND);
        builder.put(MdClassPackage.Literals.TABLE__TABLE_FIELDS, IMetadataXmlElements.FIELD);
        builder.put(MdClassPackage.Literals.TABLE__FORMS, IMetadataXmlElements.FORM);
        builder.put(MdClassPackage.Literals.TABLE__TEMPLATES, IMetadataXmlElements.TEMPLATE);
        builder.put(MdClassPackage.Literals.TABLE__COMMANDS, IMetadataXmlElements.COMMAND);
        builder.put(MdClassPackage.Literals.DIMENSION_TABLE__FIELDS, IMetadataXmlElements.FIELD);
        builder.put(MdClassPackage.Literals.DIMENSION_TABLE__FORMS, IMetadataXmlElements.FORM);
        builder.put(MdClassPackage.Literals.DIMENSION_TABLE__TEMPLATES, IMetadataXmlElements.TEMPLATE);
        builder.put(MdClassPackage.Literals.DIMENSION_TABLE__COMMANDS, IMetadataXmlElements.COMMAND);
        builder.put(MdClassPackage.Literals.FILTER_CRITERION__FORMS, IMetadataXmlElements.FORM);
        builder.put(MdClassPackage.Literals.FILTER_CRITERION__COMMANDS, IMetadataXmlElements.COMMAND);
        builder.put(MdClassPackage.Literals.SETTINGS_STORAGE__FORMS, IMetadataXmlElements.FORM);
        builder.put(MdClassPackage.Literals.SETTINGS_STORAGE__TEMPLATES, IMetadataXmlElements.TEMPLATE);
        builder.put(MdClassPackage.Literals.STANDARD_TABULAR_SECTION_DESCRIPTION__SYNONYM,
            IMetadataXmlElements.XR.SYNONYM);
        builder.put(MdClassPackage.Literals.STANDARD_TABULAR_SECTION_DESCRIPTION__COMMENT,
            IMetadataXmlElements.XR.COMMENT);
        builder.put(MdClassPackage.Literals.STANDARD_TABULAR_SECTION_DESCRIPTION__TOOL_TIP,
            IMetadataXmlElements.XR.TOOLTIP);
        builder.put(MdClassPackage.Literals.STANDARD_TABULAR_SECTION_DESCRIPTION__FILL_CHECKING,
            IMetadataXmlElements.XR.FILL_CHECKING);
        builder.put(MdClassPackage.Literals.STANDARD_TABULAR_SECTION_DESCRIPTION__STANDARD_ATTRIBUTES,
            IMetadataXmlElements.XR.STANDARD_ATTRIBUTES);
        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__SYNONYM, IMetadataXmlElements.XR.SYNONYM);
        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__COMMENT, IMetadataXmlElements.XR.COMMENT);
        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__TOOL_TIP, IMetadataXmlElements.XR.TOOLTIP);
        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__QUICK_CHOICE, IMetadataXmlElements.XR.QUICK_CHOICE);
        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__FILL_FROM_FILLING_VALUE,
            IMetadataXmlElements.XR.FILL_FROM_FILLING_VALUE);
        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__FILL_CHECKING, IMetadataXmlElements.XR.FILL_CHECKING);
        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__CHOICE_PARAMETER_LINKS,
            IMetadataXmlElements.XR.CHOICE_PARAMETER_LINKS);
        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__CHOICE_PARAMETERS,
            IMetadataXmlElements.XR.CHOICE_PARAMETERS);
        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__LINK_BY_TYPE, IMetadataXmlElements.XR.LINK_BY_TYPE);
        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__FULL_TEXT_SEARCH,
            IMetadataXmlElements.XR.FULL_TEXT_SEARCH);
        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__PASSWORD_MODE, IMetadataXmlElements.XR.PASSWORD_MODE);
        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__FORMAT, IMetadataXmlElements.XR.FORMAT);
        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__EDIT_FORMAT, IMetadataXmlElements.XR.EDIT_FORMAT);
        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__MASK, IMetadataXmlElements.XR.MASK);
        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__MULTI_LINE, IMetadataXmlElements.XR.MULTI_LINE);
        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__EXTENDED_EDIT, IMetadataXmlElements.XR.EXTENDED_EDIT);
        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__MIN_VALUE, IMetadataXmlElements.XR.MIN_VALUE);
        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__MAX_VALUE, IMetadataXmlElements.XR.MAX_VALUE);
        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__MARK_NEGATIVES, IMetadataXmlElements.XR.MARK_NEGATIVES);
        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__CREATE_ON_INPUT,
            IMetadataXmlElements.XR.CREATE_ON_INPUT);
        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__CHOICE_FORM, IMetadataXmlElements.XR.CHOICE_FORM);
        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__CHOICE_HISTORY_ON_INPUT,
            IMetadataXmlElements.XR.CHOICE_HISTORY_ON_INPUT);
        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__FILL_VALUE, IMetadataXmlElements.XR.FILL_VALUE);
        builder.put(MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__KEY_FIELD, IMetadataXmlElements.XR.KEY_FIELD);
        builder.put(MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__TYPES_FILTER_FIELD,
            IMetadataXmlElements.XR.TYPES_FILTER_FIELD);
        builder.put(MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__TYPES_FILTER_VALUE,
            IMetadataXmlElements.XR.TYPES_FILTER_VALUE);
        builder.put(MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__DATA_PATH_FIELD,
            IMetadataXmlElements.XR.DATA_PATH_FIELD);
        builder.put(MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__MULTIPLE_VALUES_USE_FIELD,
            IMetadataXmlElements.XR.USE_MULTIPLE_VALUES_FIELD);
        builder.put(MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__OBJECT_FIELD,
            IMetadataXmlElements.XR.OBJECT_FIELD);
        builder.put(MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__TYPE_FIELD,
            IMetadataXmlElements.XR.TYPE_FIELD);
        builder.put(MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__VALUE_FIELD,
            IMetadataXmlElements.XR.VALUE_FIELD);
        builder.put(MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__MULTIPLE_VALUES_KEY_FIELD,
            IMetadataXmlElements.XR.MULTIPLE_VALUES_KEY_FIELD);
        builder.put(MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__MULTIPLE_VALUES_ORDER_FIELD,
            IMetadataXmlElements.XR.MULTIPLE_VALUES_ORDER_FIELD);
        builder.put(McorePackage.Literals.VALUE_LIST__VALUES, IMetadataXmlElements.V8.VALUE);
        builder.put(McorePackage.Literals.FIXED_ARRAY_VALUE__VALUES, IMetadataXmlElements.V8.VALUE);
        builder.put(CommonPackage.Literals.ABSTRACT_CHOICE_PARAMETER_LINK__NAME, IMetadataXmlElements.XR.NAME);
        builder.put(CommonPackage.Literals.ABSTRACT_CHOICE_PARAMETER_LINK__CHANGE_MODE,
            IMetadataXmlElements.XR.VALUE_CHANGE);
        builder.put(CommonPackage.Literals.CHOICE_PARAMETER__VALUE, IMetadataXmlElements.APP.VALUE);
        builder.put(CommonPackage.Literals.REQUIRED_PERMISSION__DESCRIPTION, IMetadataXmlElements.APP.DESCRIPTION);
        builder.put(CommonPackage.Literals.REQUIRED_PERMISSION_MESSAGE__DESCRIPTION,
            IMetadataXmlElements.APP.DESCRIPTION);
        builder.put(MdClassPackage.Literals.HTTP_SERVICE__URL_TEMPLATES, IMetadataXmlElements.URL_TEMPLATE);
        builder.put(MdClassPackage.Literals.URL_TEMPLATE__METHODS, IMetadataXmlElements.URL_TEMPLATE_METHOD);
        builder.put(MdClassPackage.Literals.METHOD__HTTP_METHOD, IMetadataXmlElements.HTTP_METHOD);
        builder.put(MdClassPackage.Literals.CALCULATION_REGISTER__RESOURCES, IMetadataXmlElements.RESOURCE);
        builder.put(MdClassPackage.Literals.CALCULATION_REGISTER__ATTRIBUTES, IMetadataXmlElements.ATTRIBUTE);
        builder.put(MdClassPackage.Literals.CALCULATION_REGISTER__DIMENSIONS, IMetadataXmlElements.DIMENSION);
        builder.put(MdClassPackage.Literals.CALCULATION_REGISTER__FORMS, IMetadataXmlElements.FORM);
        builder.put(MdClassPackage.Literals.CALCULATION_REGISTER__TEMPLATES, IMetadataXmlElements.TEMPLATE);
        builder.put(MdClassPackage.Literals.CALCULATION_REGISTER__COMMANDS, IMetadataXmlElements.COMMAND);
        builder.put(MdClassPackage.Literals.CALCULATION_REGISTER__RECALCULATIONS, IMetadataXmlElements.RECALCULATION);
        builder.put(MdClassPackage.Literals.RECALCULATION__DIMENSIONS, IMetadataXmlElements.DIMENSION);
        builder.put(MdClassPackage.Literals.WEB_SERVICE__OPERATIONS, IMetadataXmlElements.OPERATIONS);
        builder.put(MdClassPackage.Literals.WEB_SERVICE__XDTO_PACKAGES, IMetadataXmlElements.XDTO_PACKAGES);
        builder.put(MdClassPackage.Literals.OPERATION__PARAMETERS, IMetadataXmlElements.PARAMETERS);
        builder.put(MdClassPackage.Literals.OPERATION__XDTO_RETURNING_VALUE_TYPE,
            IMetadataXmlElements.XDTO_RETURNING_VALUE_TYPE);
        builder.put(MdClassPackage.Literals.PARAMETER__XDTO_VALUE_TYPE, IMetadataXmlElements.XDTO_VALUE_TYPE);
        builder.put(MdClassPackage.Literals.PREDEFINED_ITEM__NAME, IMetadataXmlElements.XPR.NAME);
        builder.put(MdClassPackage.Literals.PREDEFINED_ITEM__DESCRIPTION, IMetadataXmlElements.XPR.DESCRIPTION);
        builder.put(MdClassPackage.Literals.CATALOG_PREDEFINED_ITEM__CODE, IMetadataXmlElements.XPR.CODE);
        builder.put(MdClassPackage.Literals.CATALOG_PREDEFINED_ITEM__CONTENT, IMetadataXmlElements.XPR.CHILD_ITEMS);
        builder.put(MdClassPackage.Literals.CATALOG_PREDEFINED_ITEM__IS_FOLDER, IMetadataXmlElements.XPR.IS_FOLDER);
        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED_ITEM__CODE, IMetadataXmlElements.XPR.CODE);
        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED_ITEM__ACCOUNT_TYPE,
            IMetadataXmlElements.XPR.ACCOUNT_TYPE);
        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED_ITEM__ACCOUNTING_FLAGS,
            IMetadataXmlElements.XPR.ACCOUNTING_FLAGS);
        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED_ITEM__CHILD_ITEMS,
            IMetadataXmlElements.XPR.CHILD_ITEMS);
        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED_ITEM__EXT_DIMENSION_TYPES,
            IMetadataXmlElements.XPR.EXT_DIMENSION_TYPES);
        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED_ITEM__OFF_BALANCE,
            IMetadataXmlElements.XPR.OFF_BALANCE);
        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED_ITEM__ORDER, IMetadataXmlElements.XPR.ORDER);
        builder.put(MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES_PREDEFINED_ITEM__CODE,
            IMetadataXmlElements.XPR.CODE);
        builder.put(MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES_PREDEFINED_ITEM__TYPE,
            IMetadataXmlElements.XPR.TYPE);
        builder.put(MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES_PREDEFINED_ITEM__IS_FOLDER,
            IMetadataXmlElements.XPR.IS_FOLDER);
        builder.put(MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES_PREDEFINED_ITEM__CONTENT,
            IMetadataXmlElements.XPR.CHILD_ITEMS);
        builder.put(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES_PREDEFINED_ITEM__CODE,
            IMetadataXmlElements.XPR.CODE);
        builder.put(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES_PREDEFINED_ITEM__ACTION_PERIOD_IS_BASE,
            IMetadataXmlElements.XPR.ACTION_PERIOD_IS_BASE);
        builder.put(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES_PREDEFINED_ITEM__BASE,
            IMetadataXmlElements.XPR.BASE);
        builder.put(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES_PREDEFINED_ITEM__LEADING,
            IMetadataXmlElements.XPR.LEADING);
        builder.put(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES_PREDEFINED_ITEM__DISPLACED,
            IMetadataXmlElements.XPR.DISPLACED);
        builder.put(MdClassPackage.Literals.EXT_DIMENSION_TYPE__TURNOVER, IMetadataXmlElements.XPR.TURNOVER);
        builder.put(MdClassPackage.Literals.EXT_DIMENSION_TYPE__EXT_DIMENSION_ACCOUNTING_FLAGS,
            IMetadataXmlElements.XPR.ACCOUNTING_FLAGS);
        builder.put(MdClassPackage.Literals.INTEGRATION_SERVICE__INTEGRATION_SERVICE_CHANNELS,
            IMetadataXmlElements.INTEGRATION_SERVICE_CHANNEL);
    }

    @Override
    protected void fillSpecifiedPackageNsUri(ImmutableMap.Builder<EPackage, String> builder)
    {
        builder.put(MdClassPackage.eINSTANCE, "http://v8.1c.ru/8.3/MDClasses");
    }
}
