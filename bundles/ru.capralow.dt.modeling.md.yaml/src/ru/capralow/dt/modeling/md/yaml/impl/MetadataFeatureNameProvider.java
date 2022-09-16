/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.yaml.impl;

import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.metadata.mdclass.MdClassFactory;
import com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.md.yaml.IMetadataYamlElements;
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
            if (!(classifier instanceof EClass))
            {
                continue;
            }

            EClass eClass = (EClass)classifier;

            if (eClass != MdClassPackage.Literals.COMMON_FORM && !eClass.isAbstract() && !eClass.isInterface()
                && MdClassPackage.Literals.BASIC_FORM.isSuperTypeOf(eClass))
            {
//                    builder.put(eClass, IMetadataYamlElements.FORM);
            }
            if (eClass != MdClassPackage.Literals.COMMON_TEMPLATE && !eClass.isAbstract() && !eClass.isInterface()
                && MdClassPackage.Literals.BASIC_TEMPLATE.isSuperTypeOf(eClass))
            {
//                    builder.put(eClass, IMetadataYamlElements.TEMPLATE);
            }

        }
    }

    @Override
    protected void fillSpecifiedFeatureNames(ImmutableMap.Builder<EStructuralFeature, QName> builder)
    {
        builder.put(MdClassPackage.Literals.MD_OBJECT__NAME, IMetadataYamlElements.NAME);
        builder.put(MdClassPackage.Literals.MD_OBJECT__SYNONYM, IMetadataYamlElements.SYNONYM);

        builder.put(MdClassPackage.Literals.CONFIGURATION__VERSION, IMetadataYamlElements.Application.VERSION);
        builder.put(MdClassPackage.Literals.CONFIGURATION__VENDOR, IMetadataYamlElements.Application.VENDOR);

        //        builder.put(MdClassPackage.Literals.CONFIGURATION__LANGUAGES, IMetadataYamlElements.LANGUAGE);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__SUBSYSTEMS, IMetadataYamlElements.SUBSYSTEM);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__STYLE_ITEMS, IMetadataYamlElements.STYLE_ITEM);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__STYLES, IMetadataYamlElements.STYLE);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__COMMON_PICTURES, IMetadataYamlElements.COMMON_PICTURE);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__INTERFACES, IMetadataYamlElements.INTERFACE);
//        builder.put(
//        MdClassPackage.Literals.CONFIGURATION__SESSION_PARAMETERS, IMetadataYamlElements.SESSION_PARAMETER);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__ROLES, IMetadataYamlElements.ROLE);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__COMMON_TEMPLATES, IMetadataYamlElements.COMMON_TEMPLATE);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__FILTER_CRITERIA, IMetadataYamlElements.FILTER_CRITERION);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__COMMON_MODULES, IMetadataYamlElements.COMMON_MODULE);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__COMMON_ATTRIBUTES, IMetadataYamlElements.COMMON_ATTRIBUTE);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__EXCHANGE_PLANS, IMetadataYamlElements.EXCHANGE_PLAN);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__XDTO_PACKAGES, IMetadataYamlElements.XDTO_PACKAGE);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__WEB_SERVICES, IMetadataYamlElements.WEB_SERVICE);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__HTTP_SERVICES, IMetadataYamlElements.HTTP_SERVICE);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__WS_REFERENCES, IMetadataYamlElements.WS_REFERENCE);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__EVENT_SUBSCRIPTIONS,
//            IMetadataYamlElements.EVENT_SUBSCRIPTION);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__SCHEDULED_JOBS, IMetadataYamlElements.SCHEDULED_JOB);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__SETTINGS_STORAGES, IMetadataYamlElements.SETTINGS_STORAGE);
//        builder.put(
//        MdClassPackage.Literals.CONFIGURATION__FUNCTIONAL_OPTIONS, IMetadataYamlElements.FUNCTIONAL_OPTION);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__FUNCTIONAL_OPTIONS_PARAMETERS,
//            IMetadataYamlElements.FUNCTIONAL_OPTIONS_PARAMETER);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__DEFINED_TYPES, IMetadataYamlElements.DEFINED_TYPE);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__COMMON_COMMANDS, IMetadataYamlElements.COMMON_COMMAND);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__COMMAND_GROUPS, IMetadataYamlElements.COMMAND_GROUP);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__CONSTANTS, IMetadataYamlElements.CONSTANT);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__COMMON_FORMS, IMetadataYamlElements.COMMON_FORM);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__CATALOGS, IMetadataYamlElements.CATALOG);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__DOCUMENTS, IMetadataYamlElements.DOCUMENT);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__DOCUMENT_NUMERATORS,
//            IMetadataYamlElements.DOCUMENT_NUMERATOR);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__SEQUENCES, IMetadataYamlElements.SEQUENCE);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__DOCUMENT_JOURNALS, IMetadataYamlElements.DOCUMENT_JOURNAL);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__ENUMS, IMetadataYamlElements.ENUM);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__REPORTS, IMetadataYamlElements.REPORT);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__DATA_PROCESSORS, IMetadataYamlElements.DATA_PROCESSOR);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__INFORMATION_REGISTERS,
//            IMetadataYamlElements.INFORMATION_REGISTER);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__ACCUMULATION_REGISTERS,
//            IMetadataYamlElements.ACCUMULATION_REGISTER);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__CHARTS_OF_CHARACTERISTIC_TYPES,
//            IMetadataYamlElements.CHART_OF_CHARACTERISTIC_TYPES);
//        builder.put(
//        MdClassPackage.Literals.CONFIGURATION__CHARTS_OF_ACCOUNTS, IMetadataYamlElements.CHART_OF_ACCOUNTS);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__ACCOUNTING_REGISTERS,
//            IMetadataYamlElements.ACCOUNTING_REGISTER);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__CHARTS_OF_CALCULATION_TYPES,
//            IMetadataYamlElements.CHART_OF_CALCULATION_TYPES);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__CALCULATION_REGISTERS,
//            IMetadataYamlElements.CALCULATION_REGISTER);
//        builder.put(
//        MdClassPackage.Literals.CONFIGURATION__BUSINESS_PROCESSES, IMetadataYamlElements.BUSINESS_PROCESS);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__TASKS, IMetadataYamlElements.TASK);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__EXTERNAL_DATA_SOURCES,
//            IMetadataYamlElements.EXTERNAL_DATA_SOURCE);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__INTEGRATION_SERVICES,
//            IMetadataYamlElements.INTEGRATION_SERVICE);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__BOTS, IMetadataYamlElements.BOT);
//        builder.put(MdClassPackage.Literals.CONFIGURATION__MOBILE_APPLICATION_URLS,
//            IMetadataYamlElements.MOBILE_APPLICATION_URLS);
//
//        builder.put(MdClassPackage.Literals.DB_OBJECT_TABULAR_SECTION__ATTRIBUTES, IMetadataYamlElements.ATTRIBUTE);
//
//        builder.put(MdClassPackage.Literals.REPORT_TABULAR_SECTION__ATTRIBUTES, IMetadataYamlElements.ATTRIBUTE);
//
//        builder.put(MdClassPackage.Literals.DATA_PROCESSOR_TABULAR_SECTION__ATTRIBUTES,
//            IMetadataYamlElements.ATTRIBUTE);
//
//        builder.put(MdClassPackage.Literals.SUBSYSTEM__SUBSYSTEMS, IMetadataYamlElements.SUBSYSTEM);
//
//        builder.put(
//        MdClassPackage.Literals.COMMON_ATTRIBUTE_CONTENT_ITEM__METADATA, IMetadataYamlElements.XR.METADATA);
//        builder.put(MdClassPackage.Literals.COMMON_ATTRIBUTE_CONTENT_ITEM__USE, IMetadataYamlElements.XR.USE);
//        builder.put(MdClassPackage.Literals.COMMON_ATTRIBUTE_CONTENT_ITEM__CONDITIONAL_SEPARATION,
//            IMetadataYamlElements.XR.CONDITIONAL_SEPARATION);
//
//        builder.put(MdClassPackage.Literals.CATALOG__ATTRIBUTES, IMetadataYamlElements.ATTRIBUTE);
//        builder.put(MdClassPackage.Literals.CATALOG__TABULAR_SECTIONS, IMetadataYamlElements.TABULAR_SECTION);
//        builder.put(MdClassPackage.Literals.CATALOG__FORMS, IMetadataYamlElements.FORM);
//        builder.put(MdClassPackage.Literals.CATALOG__TEMPLATES, IMetadataYamlElements.TEMPLATE);
//        builder.put(MdClassPackage.Literals.CATALOG__COMMANDS, IMetadataYamlElements.COMMAND);
//
//        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS__ATTRIBUTES, IMetadataYamlElements.ATTRIBUTE);
//        builder.put(
//        MdClassPackage.Literals.CHART_OF_ACCOUNTS__TABULAR_SECTIONS, IMetadataYamlElements.TABULAR_SECTION);
//        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS__FORMS, IMetadataYamlElements.FORM);
//        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS__TEMPLATES, IMetadataYamlElements.TEMPLATE);
//        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS__COMMANDS, IMetadataYamlElements.COMMAND);
//        builder.put(
//        MdClassPackage.Literals.CHART_OF_ACCOUNTS__ACCOUNTING_FLAGS, IMetadataYamlElements.ACCOUNTING_FLAG);
//        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS__EXT_DIMENSION_ACCOUNTING_FLAGS,
//            IMetadataYamlElements.EXT_DIMENSION_ACCOUNTING_FLAG);
//
//        builder.put(MdClassPackage.Literals.DOCUMENT__ATTRIBUTES, IMetadataYamlElements.ATTRIBUTE);
//        builder.put(MdClassPackage.Literals.DOCUMENT__TABULAR_SECTIONS, IMetadataYamlElements.TABULAR_SECTION);
//        builder.put(MdClassPackage.Literals.DOCUMENT__FORMS, IMetadataYamlElements.FORM);
//        builder.put(MdClassPackage.Literals.DOCUMENT__TEMPLATES, IMetadataYamlElements.TEMPLATE);
//        builder.put(MdClassPackage.Literals.DOCUMENT__COMMANDS, IMetadataYamlElements.COMMAND);
//
//        builder.put(MdClassPackage.Literals.ENUM__ENUM_VALUES, IMetadataYamlElements.ENUM_VALUE);
//        builder.put(MdClassPackage.Literals.ENUM__FORMS, IMetadataYamlElements.FORM);
//        builder.put(MdClassPackage.Literals.ENUM__TEMPLATES, IMetadataYamlElements.TEMPLATE);
//        builder.put(MdClassPackage.Literals.ENUM__COMMANDS, IMetadataYamlElements.COMMAND);
//
//        builder.put(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES__ATTRIBUTES, IMetadataYamlElements.ATTRIBUTE);
//        builder.put(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES__TABULAR_SECTIONS,
//            IMetadataYamlElements.TABULAR_SECTION);
//        builder.put(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES__FORMS, IMetadataYamlElements.FORM);
//        builder.put(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES__TEMPLATES, IMetadataYamlElements.TEMPLATE);
//        builder.put(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES__COMMANDS, IMetadataYamlElements.COMMAND);
//
//        builder.put(
//        MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES__ATTRIBUTES, IMetadataYamlElements.ATTRIBUTE);
//        builder.put(MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES__TABULAR_SECTIONS,
//            IMetadataYamlElements.TABULAR_SECTION);
//        builder.put(MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES__FORMS, IMetadataYamlElements.FORM);
//        builder.put(MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES__TEMPLATES, IMetadataYamlElements.TEMPLATE);
//        builder.put(MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES__COMMANDS, IMetadataYamlElements.COMMAND);
//
//        builder.put(MdClassPackage.Literals.INFORMATION_REGISTER__RESOURCES, IMetadataYamlElements.RESOURCE);
//        builder.put(MdClassPackage.Literals.INFORMATION_REGISTER__ATTRIBUTES, IMetadataYamlElements.ATTRIBUTE);
//        builder.put(MdClassPackage.Literals.INFORMATION_REGISTER__DIMENSIONS, IMetadataYamlElements.DIMENSION);
//        builder.put(MdClassPackage.Literals.INFORMATION_REGISTER__FORMS, IMetadataYamlElements.FORM);
//        builder.put(MdClassPackage.Literals.INFORMATION_REGISTER__TEMPLATES, IMetadataYamlElements.TEMPLATE);
//        builder.put(MdClassPackage.Literals.INFORMATION_REGISTER__COMMANDS, IMetadataYamlElements.COMMAND);
//
//        builder.put(MdClassPackage.Literals.EXCHANGE_PLAN__ATTRIBUTES, IMetadataYamlElements.ATTRIBUTE);
//        builder.put(MdClassPackage.Literals.EXCHANGE_PLAN__TABULAR_SECTIONS, IMetadataYamlElements.TABULAR_SECTION);
//        builder.put(MdClassPackage.Literals.EXCHANGE_PLAN__FORMS, IMetadataYamlElements.FORM);
//        builder.put(MdClassPackage.Literals.EXCHANGE_PLAN__TEMPLATES, IMetadataYamlElements.TEMPLATE);
//        builder.put(MdClassPackage.Literals.EXCHANGE_PLAN__COMMANDS, IMetadataYamlElements.COMMAND);
//        builder.put(
//        MdClassPackage.Literals.EXCHANGE_PLAN_CONTENT_ITEM__MD_OBJECT, IMetadataYamlElements.EXT.METADATA);
//        builder.put(MdClassPackage.Literals.EXCHANGE_PLAN_CONTENT_ITEM__AUTO_RECORD,
//            IMetadataYamlElements.EXT.AUTO_RECORD);
//
//        builder.put(MdClassPackage.Literals.SEQUENCE__DIMENSIONS, IMetadataYamlElements.DIMENSION);
//
//        builder.put(MdClassPackage.Literals.DOCUMENT_JOURNAL__COLUMNS, IMetadataYamlElements.COLUMN);
//        builder.put(MdClassPackage.Literals.DOCUMENT_JOURNAL__FORMS, IMetadataYamlElements.FORM);
//        builder.put(MdClassPackage.Literals.DOCUMENT_JOURNAL__TEMPLATES, IMetadataYamlElements.TEMPLATE);
//        builder.put(MdClassPackage.Literals.DOCUMENT_JOURNAL__COMMANDS, IMetadataYamlElements.COMMAND);
//
//        builder.put(MdClassPackage.Literals.REPORT__ATTRIBUTES, IMetadataYamlElements.ATTRIBUTE);
//        builder.put(MdClassPackage.Literals.REPORT__TABULAR_SECTIONS, IMetadataYamlElements.TABULAR_SECTION);
//        builder.put(MdClassPackage.Literals.REPORT__FORMS, IMetadataYamlElements.FORM);
//        builder.put(MdClassPackage.Literals.REPORT__TEMPLATES, IMetadataYamlElements.TEMPLATE);
//        builder.put(MdClassPackage.Literals.REPORT__COMMANDS, IMetadataYamlElements.COMMAND);
//
//        builder.put(MdClassPackage.Literals.EXTERNAL_REPORT__ATTRIBUTES, IMetadataYamlElements.ATTRIBUTE);
//        builder.put(MdClassPackage.Literals.EXTERNAL_REPORT__TABULAR_SECTIONS, IMetadataYamlElements.TABULAR_SECTION);
//        builder.put(MdClassPackage.Literals.EXTERNAL_REPORT__FORMS, IMetadataYamlElements.FORM);
//        builder.put(MdClassPackage.Literals.EXTERNAL_REPORT__TEMPLATES, IMetadataYamlElements.TEMPLATE);
//
//        builder.put(MdClassPackage.Literals.DATA_PROCESSOR__ATTRIBUTES, IMetadataYamlElements.ATTRIBUTE);
//        builder.put(MdClassPackage.Literals.DATA_PROCESSOR__TABULAR_SECTIONS, IMetadataYamlElements.TABULAR_SECTION);
//        builder.put(MdClassPackage.Literals.DATA_PROCESSOR__FORMS, IMetadataYamlElements.FORM);
//        builder.put(MdClassPackage.Literals.DATA_PROCESSOR__TEMPLATES, IMetadataYamlElements.TEMPLATE);
//        builder.put(MdClassPackage.Literals.DATA_PROCESSOR__COMMANDS, IMetadataYamlElements.COMMAND);
//
//        builder.put(MdClassPackage.Literals.EXTERNAL_DATA_PROCESSOR__ATTRIBUTES, IMetadataYamlElements.ATTRIBUTE);
//        builder.put(MdClassPackage.Literals.EXTERNAL_DATA_PROCESSOR__TABULAR_SECTIONS,
//            IMetadataYamlElements.TABULAR_SECTION);
//        builder.put(MdClassPackage.Literals.EXTERNAL_DATA_PROCESSOR__FORMS, IMetadataYamlElements.FORM);
//        builder.put(MdClassPackage.Literals.EXTERNAL_DATA_PROCESSOR__TEMPLATES, IMetadataYamlElements.TEMPLATE);
//
//        builder.put(MdClassPackage.Literals.EXTERNAL_DATA_SOURCE__CUBES, IMetadataYamlElements.CUBE);
//        builder.put(MdClassPackage.Literals.EXTERNAL_DATA_SOURCE__TABLES, IMetadataYamlElements.TABLE);
//        builder.put(MdClassPackage.Literals.EXTERNAL_DATA_SOURCE__FUNCTIONS, IMetadataYamlElements.FUNCTION);
//
//        builder.put(MdClassPackage.Literals.ACCOUNTING_REGISTER__RESOURCES, IMetadataYamlElements.RESOURCE);
//        builder.put(MdClassPackage.Literals.ACCOUNTING_REGISTER__ATTRIBUTES, IMetadataYamlElements.ATTRIBUTE);
//        builder.put(MdClassPackage.Literals.ACCOUNTING_REGISTER__DIMENSIONS, IMetadataYamlElements.DIMENSION);
//        builder.put(MdClassPackage.Literals.ACCOUNTING_REGISTER__FORMS, IMetadataYamlElements.FORM);
//        builder.put(MdClassPackage.Literals.ACCOUNTING_REGISTER__TEMPLATES, IMetadataYamlElements.TEMPLATE);
//        builder.put(MdClassPackage.Literals.ACCOUNTING_REGISTER__COMMANDS, IMetadataYamlElements.COMMAND);
//
//        builder.put(MdClassPackage.Literals.ACCUMULATION_REGISTER__RESOURCES, IMetadataYamlElements.RESOURCE);
//        builder.put(MdClassPackage.Literals.ACCUMULATION_REGISTER__ATTRIBUTES, IMetadataYamlElements.ATTRIBUTE);
//        builder.put(MdClassPackage.Literals.ACCUMULATION_REGISTER__DIMENSIONS, IMetadataYamlElements.DIMENSION);
//        builder.put(MdClassPackage.Literals.ACCUMULATION_REGISTER__FORMS, IMetadataYamlElements.FORM);
//        builder.put(MdClassPackage.Literals.ACCUMULATION_REGISTER__TEMPLATES, IMetadataYamlElements.TEMPLATE);
//        builder.put(MdClassPackage.Literals.ACCUMULATION_REGISTER__COMMANDS, IMetadataYamlElements.COMMAND);
//
//        builder.put(MdClassPackage.Literals.BUSINESS_PROCESS__ATTRIBUTES, IMetadataYamlElements.ATTRIBUTE);
//        builder.put(
//        MdClassPackage.Literals.BUSINESS_PROCESS__TABULAR_SECTIONS, IMetadataYamlElements.TABULAR_SECTION);
//        builder.put(MdClassPackage.Literals.BUSINESS_PROCESS__FORMS, IMetadataYamlElements.FORM);
//        builder.put(MdClassPackage.Literals.BUSINESS_PROCESS__TEMPLATES, IMetadataYamlElements.TEMPLATE);
//        builder.put(MdClassPackage.Literals.BUSINESS_PROCESS__COMMANDS, IMetadataYamlElements.COMMAND);
//
//        builder.put(MdClassPackage.Literals.TASK__COMMANDS, IMetadataYamlElements.COMMAND);
//
//        builder.put(MdClassPackage.Literals.CUBE__DIMENSION_TABLES, IMetadataYamlElements.DIMENSION_TABLE);
//        builder.put(MdClassPackage.Literals.CUBE__DIMENSIONS, IMetadataYamlElements.DIMENSION);
//        builder.put(MdClassPackage.Literals.CUBE__RESOURCES, IMetadataYamlElements.RESOURCE);
//        builder.put(MdClassPackage.Literals.CUBE__FORMS, IMetadataYamlElements.FORM);
//        builder.put(MdClassPackage.Literals.CUBE__TEMPLATES, IMetadataYamlElements.TEMPLATE);
//        builder.put(MdClassPackage.Literals.CUBE__COMMANDS, IMetadataYamlElements.COMMAND);
//
//        builder.put(MdClassPackage.Literals.TABLE__TABLE_FIELDS, IMetadataYamlElements.FIELD);
//        builder.put(MdClassPackage.Literals.TABLE__FORMS, IMetadataYamlElements.FORM);
//        builder.put(MdClassPackage.Literals.TABLE__TEMPLATES, IMetadataYamlElements.TEMPLATE);
//        builder.put(MdClassPackage.Literals.TABLE__COMMANDS, IMetadataYamlElements.COMMAND);
//
//        builder.put(MdClassPackage.Literals.DIMENSION_TABLE__FIELDS, IMetadataYamlElements.FIELD);
//        builder.put(MdClassPackage.Literals.DIMENSION_TABLE__FORMS, IMetadataYamlElements.FORM);
//        builder.put(MdClassPackage.Literals.DIMENSION_TABLE__TEMPLATES, IMetadataYamlElements.TEMPLATE);
//        builder.put(MdClassPackage.Literals.DIMENSION_TABLE__COMMANDS, IMetadataYamlElements.COMMAND);
//
//        builder.put(MdClassPackage.Literals.FILTER_CRITERION__FORMS, IMetadataYamlElements.FORM);
//        builder.put(MdClassPackage.Literals.FILTER_CRITERION__COMMANDS, IMetadataYamlElements.COMMAND);
//
//        builder.put(MdClassPackage.Literals.SETTINGS_STORAGE__FORMS, IMetadataYamlElements.FORM);
//        builder.put(MdClassPackage.Literals.SETTINGS_STORAGE__TEMPLATES, IMetadataYamlElements.TEMPLATE);
//
//        builder.put(MdClassPackage.Literals.STANDARD_TABULAR_SECTION_DESCRIPTION__SYNONYM,
//            IMetadataYamlElements.XR.SYNONYM);
//        builder.put(MdClassPackage.Literals.STANDARD_TABULAR_SECTION_DESCRIPTION__COMMENT,
//            IMetadataYamlElements.XR.COMMENT);
//        builder.put(MdClassPackage.Literals.STANDARD_TABULAR_SECTION_DESCRIPTION__TOOL_TIP,
//            IMetadataYamlElements.XR.TOOLTIP);
//        builder.put(MdClassPackage.Literals.STANDARD_TABULAR_SECTION_DESCRIPTION__FILL_CHECKING,
//            IMetadataYamlElements.XR.FILL_CHECKING);
//        builder.put(MdClassPackage.Literals.STANDARD_TABULAR_SECTION_DESCRIPTION__STANDARD_ATTRIBUTES,
//            IMetadataYamlElements.XR.STANDARD_ATTRIBUTES);
//
//        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__SYNONYM, IMetadataYamlElements.XR.SYNONYM);
//        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__COMMENT, IMetadataYamlElements.XR.COMMENT);
//        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__TOOL_TIP, IMetadataYamlElements.XR.TOOLTIP);
//        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__QUICK_CHOICE, IMetadataYamlElements.XR.QUICK_CHOICE);
//        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__FILL_FROM_FILLING_VALUE,
//            IMetadataYamlElements.XR.FILL_FROM_FILLING_VALUE);
//        builder.put(
//        MdClassPackage.Literals.STANDARD_ATTRIBUTE__FILL_CHECKING, IMetadataYamlElements.XR.FILL_CHECKING);
//        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__CHOICE_PARAMETER_LINKS,
//            IMetadataYamlElements.XR.CHOICE_PARAMETER_LINKS);
//        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__CHOICE_PARAMETERS,
//            IMetadataYamlElements.XR.CHOICE_PARAMETERS);
//        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__LINK_BY_TYPE, IMetadataYamlElements.XR.LINK_BY_TYPE);
//        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__FULL_TEXT_SEARCH,
//            IMetadataYamlElements.XR.FULL_TEXT_SEARCH);
//        builder.put(
//        MdClassPackage.Literals.STANDARD_ATTRIBUTE__PASSWORD_MODE, IMetadataYamlElements.XR.PASSWORD_MODE);
//        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__FORMAT, IMetadataYamlElements.XR.FORMAT);
//        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__EDIT_FORMAT, IMetadataYamlElements.XR.EDIT_FORMAT);
//        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__MASK, IMetadataYamlElements.XR.MASK);
//        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__MULTI_LINE, IMetadataYamlElements.XR.MULTI_LINE);
//        builder.put(
//        MdClassPackage.Literals.STANDARD_ATTRIBUTE__EXTENDED_EDIT, IMetadataYamlElements.XR.EXTENDED_EDIT);
//        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__MIN_VALUE, IMetadataYamlElements.XR.MIN_VALUE);
//        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__MAX_VALUE, IMetadataYamlElements.XR.MAX_VALUE);
//        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__MARK_NEGATIVES,
//            IMetadataYamlElements.XR.MARK_NEGATIVES);
//        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__CREATE_ON_INPUT,
//            IMetadataYamlElements.XR.CREATE_ON_INPUT);
//        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__CHOICE_FORM, IMetadataYamlElements.XR.CHOICE_FORM);
//        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__CHOICE_HISTORY_ON_INPUT,
//            IMetadataYamlElements.XR.CHOICE_HISTORY_ON_INPUT);
//        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE__FILL_VALUE, IMetadataYamlElements.XR.FILL_VALUE);
//
//        builder.put(
//        MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__KEY_FIELD, IMetadataYamlElements.XR.KEY_FIELD);
//        builder.put(MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__TYPES_FILTER_FIELD,
//            IMetadataYamlElements.XR.TYPES_FILTER_FIELD);
//        builder.put(MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__TYPES_FILTER_VALUE,
//            IMetadataYamlElements.XR.TYPES_FILTER_VALUE);
//        builder.put(MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__DATA_PATH_FIELD,
//            IMetadataYamlElements.XR.DATA_PATH_FIELD);
//        builder.put(MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__MULTIPLE_VALUES_USE_FIELD,
//            IMetadataYamlElements.XR.USE_MULTIPLE_VALUES_FIELD);
//        builder.put(MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__OBJECT_FIELD,
//            IMetadataYamlElements.XR.OBJECT_FIELD);
//        builder.put(MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__TYPE_FIELD,
//            IMetadataYamlElements.XR.TYPE_FIELD);
//        builder.put(MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__VALUE_FIELD,
//            IMetadataYamlElements.XR.VALUE_FIELD);
//        builder.put(MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__MULTIPLE_VALUES_KEY_FIELD,
//            IMetadataYamlElements.XR.MULTIPLE_VALUES_KEY_FIELD);
//        builder.put(MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__MULTIPLE_VALUES_ORDER_FIELD,
//            IMetadataYamlElements.XR.MULTIPLE_VALUES_ORDER_FIELD);
//
//        builder.put(McorePackage.Literals.VALUE_LIST__VALUES, IMetadataYamlElements.V8.VALUE);
//
//        builder.put(McorePackage.Literals.FIXED_ARRAY_VALUE__VALUES, IMetadataYamlElements.V8.VALUE);
//
//        builder.put(CommonPackage.Literals.ABSTRACT_CHOICE_PARAMETER_LINK__NAME, IMetadataYamlElements.XR.NAME);
//        builder.put(CommonPackage.Literals.ABSTRACT_CHOICE_PARAMETER_LINK__CHANGE_MODE,
//            IMetadataYamlElements.XR.VALUE_CHANGE);
//
//        builder.put(CommonPackage.Literals.CHOICE_PARAMETER__VALUE, IMetadataYamlElements.APP.VALUE);
//
//        builder.put(CommonPackage.Literals.REQUIRED_PERMISSION__DESCRIPTION, IMetadataYamlElements.APP.DESCRIPTION);
//        builder.put(CommonPackage.Literals.REQUIRED_PERMISSION_MESSAGE__DESCRIPTION,
//            IMetadataYamlElements.APP.DESCRIPTION);
//
//        builder.put(MdClassPackage.Literals.HTTP_SERVICE__URL_TEMPLATES, IMetadataYamlElements.URL_TEMPLATE);
//        builder.put(MdClassPackage.Literals.URL_TEMPLATE__METHODS, IMetadataYamlElements.URL_TEMPLATE_METHOD);
//        builder.put(MdClassPackage.Literals.METHOD__HTTP_METHOD, IMetadataYamlElements.HTTP_METHOD);
//
//        builder.put(MdClassPackage.Literals.CALCULATION_REGISTER__RESOURCES, IMetadataYamlElements.RESOURCE);
//        builder.put(MdClassPackage.Literals.CALCULATION_REGISTER__ATTRIBUTES, IMetadataYamlElements.ATTRIBUTE);
//        builder.put(MdClassPackage.Literals.CALCULATION_REGISTER__DIMENSIONS, IMetadataYamlElements.DIMENSION);
//        builder.put(MdClassPackage.Literals.CALCULATION_REGISTER__FORMS, IMetadataYamlElements.FORM);
//        builder.put(MdClassPackage.Literals.CALCULATION_REGISTER__TEMPLATES, IMetadataYamlElements.TEMPLATE);
//        builder.put(MdClassPackage.Literals.CALCULATION_REGISTER__COMMANDS, IMetadataYamlElements.COMMAND);
//        builder.put(
//        MdClassPackage.Literals.CALCULATION_REGISTER__RECALCULATIONS, IMetadataYamlElements.RECALCULATION);
//        builder.put(MdClassPackage.Literals.RECALCULATION__DIMENSIONS, IMetadataYamlElements.DIMENSION);
//
//        builder.put(MdClassPackage.Literals.WEB_SERVICE__OPERATIONS, IMetadataYamlElements.OPERATIONS);
//        builder.put(MdClassPackage.Literals.WEB_SERVICE__XDTO_PACKAGES, IMetadataYamlElements.XDTO_PACKAGES);
//        builder.put(MdClassPackage.Literals.OPERATION__PARAMETERS, IMetadataYamlElements.PARAMETERS);
//        builder.put(MdClassPackage.Literals.OPERATION__XDTO_RETURNING_VALUE_TYPE,
//            IMetadataYamlElements.XDTO_RETURNING_VALUE_TYPE);
//        builder.put(MdClassPackage.Literals.PARAMETER__XDTO_VALUE_TYPE, IMetadataYamlElements.XDTO_VALUE_TYPE);
//
//        builder.put(MdClassPackage.Literals.PREDEFINED_ITEM__NAME, IMetadataYamlElements.XPR.NAME);
//        builder.put(MdClassPackage.Literals.PREDEFINED_ITEM__DESCRIPTION, IMetadataYamlElements.XPR.DESCRIPTION);
//
//        builder.put(MdClassPackage.Literals.CATALOG_PREDEFINED_ITEM__CODE, IMetadataYamlElements.XPR.CODE);
//        builder.put(MdClassPackage.Literals.CATALOG_PREDEFINED_ITEM__CONTENT, IMetadataYamlElements.XPR.CHILD_ITEMS);
//        builder.put(MdClassPackage.Literals.CATALOG_PREDEFINED_ITEM__IS_FOLDER, IMetadataYamlElements.XPR.IS_FOLDER);
//
//        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED_ITEM__CODE, IMetadataYamlElements.XPR.CODE);
//        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED_ITEM__ACCOUNT_TYPE,
//            IMetadataYamlElements.XPR.ACCOUNT_TYPE);
//        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED_ITEM__ACCOUNTING_FLAGS,
//            IMetadataYamlElements.XPR.ACCOUNTING_FLAGS);
//        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED_ITEM__CHILD_ITEMS,
//            IMetadataYamlElements.XPR.CHILD_ITEMS);
//        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED_ITEM__EXT_DIMENSION_TYPES,
//            IMetadataYamlElements.XPR.EXT_DIMENSION_TYPES);
//        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED_ITEM__OFF_BALANCE,
//            IMetadataYamlElements.XPR.OFF_BALANCE);
//        builder.put(
//        MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED_ITEM__ORDER, IMetadataYamlElements.XPR.ORDER);
//
//        builder.put(MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES_PREDEFINED_ITEM__CODE,
//            IMetadataYamlElements.XPR.CODE);
//        builder.put(MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES_PREDEFINED_ITEM__TYPE,
//            IMetadataYamlElements.XPR.TYPE);
//        builder.put(MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES_PREDEFINED_ITEM__IS_FOLDER,
//            IMetadataYamlElements.XPR.IS_FOLDER);
//        builder.put(MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES_PREDEFINED_ITEM__CONTENT,
//            IMetadataYamlElements.XPR.CHILD_ITEMS);
//
//        builder.put(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES_PREDEFINED_ITEM__CODE,
//            IMetadataYamlElements.XPR.CODE);
//        builder.put(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES_PREDEFINED_ITEM__ACTION_PERIOD_IS_BASE,
//            IMetadataYamlElements.XPR.ACTION_PERIOD_IS_BASE);
//        builder.put(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES_PREDEFINED_ITEM__BASE,
//            IMetadataYamlElements.XPR.BASE);
//        builder.put(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES_PREDEFINED_ITEM__LEADING,
//            IMetadataYamlElements.XPR.LEADING);
//        builder.put(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES_PREDEFINED_ITEM__DISPLACED,
//            IMetadataYamlElements.XPR.DISPLACED);
//
//        builder.put(MdClassPackage.Literals.EXT_DIMENSION_TYPE__TURNOVER, IMetadataYamlElements.XPR.TURNOVER);
//        builder.put(MdClassPackage.Literals.EXT_DIMENSION_TYPE__EXT_DIMENSION_ACCOUNTING_FLAGS,
//            IMetadataYamlElements.XPR.ACCOUNTING_FLAGS);
//
//        builder.put(MdClassPackage.Literals.INTEGRATION_SERVICE__INTEGRATION_SERVICE_CHANNELS,
//            IMetadataYamlElements.INTEGRATION_SERVICE_CHANNEL);
    }

}
