/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.internal.yaml.writer;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.mcore.McorePackage;
import com._1c.g5.v8.dt.metadata.common.CommonPackage;
import com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage;
import com._1c.g5.v8.dt.metadata.mdtype.MdTypeFactory;
import com._1c.g5.v8.dt.metadata.mdtype.MdTypePackage;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.yaml.IqNameProvider;
import ru.capralow.dt.modeling.yaml.writer.ChoiceParameterLinkWriter;
import ru.capralow.dt.modeling.yaml.writer.ChoiceParameterWriter;
import ru.capralow.dt.modeling.yaml.writer.ISpecifiedElementWriter;
import ru.capralow.dt.modeling.yaml.writer.LocalStringMapEntryWriter;
import ru.capralow.dt.modeling.yaml.writer.PictureWriter;
import ru.capralow.dt.modeling.yaml.writer.ReferenceWriter;
import ru.capralow.dt.modeling.yaml.writer.TypeDescriptionWriter;
import ru.capralow.dt.modeling.yaml.writer.TypeLinkWriter;
import ru.capralow.dt.modeling.yaml.writer.ValueWriter;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;

@Singleton
public class MetadataSmartFeatureWriter
    implements ISpecifiedElementWriter
{
    @Inject
    private IqNameProvider nameProvider;

    @Inject
    private ReferenceWriter referenceWriter;

    @Inject
    private Injector injector;

    private final ImmutableMap<EStructuralFeature, Class<? extends ISpecifiedElementWriter>> specifiedFeatureWriters =
        fillSpecialFeatureWriters();

    private final ImmutableMap<EClassifier, Class<? extends ISpecifiedElementWriter>> specifiedClassifierWriters =
        fillSpecialClassifierWriters();

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws ExportException
    {
        if (specifiedFeatureWriters.containsKey(feature))
        {
            ISpecifiedElementWriter specifiedWriter = injector.getInstance(specifiedFeatureWriters.get(feature));
            specifiedWriter.write(writer, eObject, feature, writeEmpty, version);
        }
        else if (specifiedClassifierWriters.containsKey(feature.getEType()))
        {
            ISpecifiedElementWriter specifiedWriter =
                injector.getInstance(specifiedClassifierWriters.get(feature.getEType()));
            specifiedWriter.write(writer, eObject, feature, writeEmpty, version);
        }
        else if (feature instanceof org.eclipse.emf.ecore.EAttribute)
        {
            if (!feature.isMany() && eObject != null)
            {
                Object value = eObject.eGet(feature);
                if (value != null || value == null && writeEmpty)
                {
                    writer.writeElement(nameProvider.getElementQName(feature), eObject.eGet(feature));
                }
            }
        }
        else if (feature instanceof org.eclipse.emf.ecore.EReference)
        {
            referenceWriter.write(writer, eObject, feature, writeEmpty, version);
        }
    }

    private ImmutableMap<EStructuralFeature, Class<? extends ISpecifiedElementWriter>> fillSpecialFeatureWriters()
    {
        ImmutableMap.Builder<EStructuralFeature, Class<? extends ISpecifiedElementWriter>> builder =
            ImmutableMap.builder();

        builder.put(MdClassPackage.Literals.MD_OBJECT__COMMENT, ISpecifiedElementWriter.ZeroWriter.class);

        builder.put(MdClassPackage.Literals.CONFIGURATION__ADDITIONAL_FULL_TEXT_SEARCH_DICTIONARIES,
            ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__BRIEF_INFORMATION, ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__CATALOGS, ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__COMMON_PICTURES, ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__COMMON_MODULES, ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__COMPATIBILITY_MODE,
            ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__CONFIGURATION_EXTENSION_COMPATIBILITY_MODE,
            ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__CONFIGURATION_INFORMATION_ADDRESS,
            ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__CONSTANTS, ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__CONTENT, ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__COPYRIGHT, ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__DATA_LOCK_CONTROL_MODE,
            ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__DATA_PROCESSORS, ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__DEFAULT_INTERFACE, ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__DEFAULT_LANGUAGE, ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__DEFAULT_ROLE, ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__DEFAULT_ROLES, ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__DEFAULT_RUN_MODE, ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__DETAILED_INFORMATION,
            ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__ENUMS, ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__INCLUDE_HELP_IN_CONTENTS,
            ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__INFORMATION_REGISTERS,
            ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__INTERFACE_COMPATIBILITY_MODE,
            ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__FUNCTIONAL_OPTIONS,
            ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__LANGUAGES, ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__LOGO, ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__MAIN_CLIENT_APPLICATION_WINDOW_MODE,
            ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__MOBILE_APPLICATION_URLS,
            ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__MODALITY_USE_MODE, ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__NAME_PREFIX, ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__OBJECT_AUTONUMERATION_MODE,
            ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__REPORTS, ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__REQUIRED_MOBILE_APPLICATION_PERMISSIONS,
            ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__REQUIRED_MOBILE_APPLICATION_PERMISSIONS8315,
            ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__ROLES, ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__SCHEDULED_JOBS, ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__SCRIPT_VARIANT, ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__SPLASH, ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__STANDALONE_CONFIGURATION_RESTRICTION_ROLES,
            ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__STYLE_ITEMS, ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__SYNCHRONOUS_PLATFORM_EXTENSION_AND_ADD_IN_CALL_USE_MODE,
            ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__UPDATE_CATALOG_ADDRESS,
            ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__USE_MANAGED_FORM_IN_ORDINARY_APPLICATION,
            ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__USE_ORDINARY_FORM_IN_MANAGED_APPLICATION,
            ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__USE_PURPOSES, ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__USED_MOBILE_APPLICATION_FUNCTIONALITIES,
            ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CONFIGURATION__VENDOR_INFORMATION_ADDRESS,
            ISpecifiedElementWriter.ZeroWriter.class);

        builder.put(MdClassPackage.Literals.CATALOG__PREDEFINED, ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CATALOG_PREDEFINED__ITEMS, ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CATALOG_PREDEFINED_ITEM__CONTENT, ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(MdClassPackage.Literals.CATALOG_PREDEFINED_ITEM__CODE, ISpecifiedElementWriter.ZeroWriter.class);
//        builder.put(MdClassPackage.Literals.CATALOG__OWNERS, MdObjectRefItemsWriter.class);
        builder.put(MdClassPackage.Literals.CATALOG__ATTRIBUTES, MetadataObjectWriter.class);
        builder.put(MdClassPackage.Literals.CATALOG__TABULAR_SECTIONS, MetadataObjectWriter.class);
        builder.put(MdClassPackage.Literals.CATALOG__COMMANDS, ISpecifiedElementWriter.ZeroWriter.class);
//        builder.put(MdClassPackage.Literals.CATALOG__FORMS, ReferenceWriter.class);
        builder.put(MdClassPackage.Literals.CATALOG__TEMPLATES, ISpecifiedElementWriter.ZeroWriter.class);

//        builder.put(MdClassPackage.Literals.ACCOUNTING_REGISTER__RESOURCES, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.ACCOUNTING_REGISTER__ATTRIBUTES, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.ACCOUNTING_REGISTER__DIMENSIONS, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.ACCOUNTING_REGISTER__FORMS, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.ACCOUNTING_REGISTER__TEMPLATES, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.ACCOUNTING_REGISTER__COMMANDS, MetadataObjectWriter.class);
//
//        builder.put(MdClassPackage.Literals.ACCUMULATION_REGISTER__RESOURCES, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.ACCUMULATION_REGISTER__ATTRIBUTES, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.ACCUMULATION_REGISTER__DIMENSIONS, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.ACCUMULATION_REGISTER__FORMS, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.ACCUMULATION_REGISTER__TEMPLATES, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.ACCUMULATION_REGISTER__COMMANDS, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.ACCUMULATION_REGISTER__AGGREGATES,
//            ISpecifiedElementWriter.ZeroWriter.class);
//
//        builder.put(MdClassPackage.Literals.BASIC_DB_OBJECT__BASED_ON, MdObjectRefItemsWriter.class);
//
//        builder.put(MdClassPackage.Literals.BASIC_FORM__FORM, ISpecifiedElementWriter.ZeroWriter.class);
//
//        builder.put(MdClassPackage.Literals.BASIC_TEMPLATE__TEMPLATE, ISpecifiedElementWriter.ZeroWriter.class);
//
//        builder.put(MdClassPackage.Literals.BUSINESS_PROCESS__ATTRIBUTES, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.BUSINESS_PROCESS__TABULAR_SECTIONS, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.BUSINESS_PROCESS__FORMS, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.BUSINESS_PROCESS__TEMPLATES, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.BUSINESS_PROCESS__COMMANDS, MetadataObjectWriter.class);
//
//        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS__PREDEFINED, ISpecifiedElementWriter.ZeroWriter.class);
//        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS__ATTRIBUTES, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS__TABULAR_SECTIONS, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS__FORMS, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS__TEMPLATES, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS__COMMANDS, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS__ACCOUNTING_FLAGS, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS__EXT_DIMENSION_ACCOUNTING_FLAGS,
//            MetadataObjectWriter.class);
//
//        builder.put(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES__PREDEFINED,
//            ISpecifiedElementWriter.ZeroWriter.class);
//        builder.put(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES__ATTRIBUTES, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES__TABULAR_SECTIONS, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES__FORMS, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES__TEMPLATES, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES__COMMANDS, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES__BASE_CALCULATION_TYPES,
//            MdObjectRefItemsWriter.class);
//
//        builder.put(MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES__PREDEFINED,
//            ISpecifiedElementWriter.ZeroWriter.class);
//        builder.put(MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES__ATTRIBUTES, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES__TABULAR_SECTIONS,
//            MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES__FORMS, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES__TEMPLATES, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES__COMMANDS, MetadataObjectWriter.class);
//
//        builder.put(MdClassPackage.Literals.COLUMN__REFERENCES, MdObjectRefItemsWriter.class);
//
//        builder.put(MdClassPackage.Literals.COMMON_ATTRIBUTE__CONTENT, CommonAttributeContentItemWriter.class);
//
//        builder.put(MdClassPackage.Literals.CUBE__DIMENSION_TABLES, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.CUBE__DIMENSIONS, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.CUBE__RESOURCES, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.CUBE__FORMS, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.CUBE__TEMPLATES, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.CUBE__COMMANDS, MetadataObjectWriter.class);
//
//        builder.put(MdClassPackage.Literals.DATA_PROCESSOR__ATTRIBUTES, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.DATA_PROCESSOR__TABULAR_SECTIONS, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.DATA_PROCESSOR__FORMS, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.DATA_PROCESSOR__TEMPLATES, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.DATA_PROCESSOR__COMMANDS, MetadataObjectWriter.class);
//
//        builder.put(MdClassPackage.Literals.DIMENSION_TABLE__FIELDS, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.DIMENSION_TABLE__FORMS, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.DIMENSION_TABLE__TEMPLATES, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.DIMENSION_TABLE__COMMANDS, MetadataObjectWriter.class);
//
//        builder.put(MdClassPackage.Literals.EXTERNAL_DATA_PROCESSOR__ATTRIBUTES, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.EXTERNAL_DATA_PROCESSOR__TABULAR_SECTIONS, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.EXTERNAL_DATA_PROCESSOR__FORMS, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.EXTERNAL_DATA_PROCESSOR__TEMPLATES, ReferenceWriter.class);
//
//        builder.put(MdClassPackage.Literals.DATA_PROCESSOR_TABULAR_SECTION__ATTRIBUTES, MetadataObjectWriter.class);
//
//        builder.put(MdClassPackage.Literals.DB_OBJECT_TABULAR_SECTION__ATTRIBUTES, MetadataObjectWriter.class);
//
//        builder.put(MdClassPackage.Literals.DOCUMENT__ATTRIBUTES, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.DOCUMENT__TABULAR_SECTIONS, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.DOCUMENT__FORMS, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.DOCUMENT__TEMPLATES, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.DOCUMENT__COMMANDS, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.DOCUMENT__REGISTER_RECORDS, MdObjectRefItemsWriter.class);
//        builder.put(MdClassPackage.Literals.DOCUMENT_JOURNAL__REGISTERED_DOCUMENTS, MdObjectRefItemsWriter.class);
//        builder.put(MdClassPackage.Literals.DOCUMENT_JOURNAL__COLUMNS, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.DOCUMENT_JOURNAL__FORMS, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.DOCUMENT_JOURNAL__TEMPLATES, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.DOCUMENT_JOURNAL__COMMANDS, MetadataObjectWriter.class);
//
//        builder.put(MdClassPackage.Literals.ENUM__ENUM_VALUES, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.ENUM__FORMS, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.ENUM__TEMPLATES, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.ENUM__COMMANDS, MetadataObjectWriter.class);
//
//        builder.put(MdClassPackage.Literals.EXCHANGE_PLAN__ATTRIBUTES, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.EXCHANGE_PLAN__TABULAR_SECTIONS, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.EXCHANGE_PLAN__FORMS, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.EXCHANGE_PLAN__TEMPLATES, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.EXCHANGE_PLAN__COMMANDS, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.EXCHANGE_PLAN__CONTENT, ISpecifiedElementWriter.ZeroWriter.class);
//        builder.put(MdClassPackage.Literals.EXCHANGE_PLAN__THIS_NODE, ExchangePlanThisNodeWriter.class);
//
//        builder.put(MdClassPackage.Literals.EXTERNAL_DATA_SOURCE__TABLES, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.EXTERNAL_DATA_SOURCE__CUBES, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.EXTERNAL_DATA_SOURCE__FUNCTIONS, MetadataObjectWriter.class);
//
//        builder.put(MdClassPackage.Literals.FILTER_CRITERION__FORMS, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.FILTER_CRITERION__COMMANDS, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.FILTER_CRITERION__CONTENT, MdObjectRefItemsWriter.class);
//
//        builder.put(MdClassPackage.Literals.FUNCTIONAL_OPTION__CONTENT, FunctionalOptionContentWriter.class);
//        builder.put(MdClassPackage.Literals.FUNCTIONAL_OPTIONS_PARAMETER__USE, MdObjectRefItemsWriter.class);
//
//        builder.put(MdClassPackage.Literals.INFORMATION_REGISTER__RESOURCES, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.INFORMATION_REGISTER__ATTRIBUTES, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.INFORMATION_REGISTER__DIMENSIONS, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.INFORMATION_REGISTER__FORMS, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.INFORMATION_REGISTER__TEMPLATES, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.INFORMATION_REGISTER__COMMANDS, MetadataObjectWriter.class);
//
//        builder.put(MdClassPackage.Literals.INTEGRATION_SERVICE__INTEGRATION_SERVICE_CHANNELS,
//            MetadataObjectWriter.class);
//
//        builder.put(MdClassPackage.Literals.REPORT__ATTRIBUTES, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.REPORT__TABULAR_SECTIONS, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.REPORT__FORMS, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.REPORT__TEMPLATES, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.REPORT__COMMANDS, MetadataObjectWriter.class);
//
//        builder.put(MdClassPackage.Literals.EXTERNAL_REPORT__ATTRIBUTES, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.EXTERNAL_REPORT__TABULAR_SECTIONS, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.EXTERNAL_REPORT__FORMS, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.EXTERNAL_REPORT__TEMPLATES, ReferenceWriter.class);
//
//        builder.put(MdClassPackage.Literals.REPORT_TABULAR_SECTION__ATTRIBUTES, MetadataObjectWriter.class);
//
//        builder.put(MdClassPackage.Literals.SCHEDULED_JOB__SCHEDULE, ISpecifiedElementWriter.ZeroWriter.class);
//
//        builder.put(MdClassPackage.Literals.SETTINGS_STORAGE__FORMS, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.SETTINGS_STORAGE__TEMPLATES, ReferenceWriter.class);
//
//        builder.put(MdClassPackage.Literals.SEQUENCE__DOCUMENTS, MdObjectRefItemsWriter.class);
//        builder.put(MdClassPackage.Literals.SEQUENCE__DIMENSIONS, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.SEQUENCE__REGISTER_RECORDS, MdObjectRefItemsWriter.class);
//        builder.put(MdClassPackage.Literals.SEQUENCE_DIMENSION__DOCUMENT_MAP, MdObjectRefItemsWriter.class);
//        builder.put(MdClassPackage.Literals.SEQUENCE_DIMENSION__REGISTER_RECORDS_MAP, MdObjectRefItemsWriter.class);
//
//        builder.put(MdClassPackage.Literals.SUBSYSTEM__CONTENT, MdObjectRefItemsWriter.class);
//
//        builder.put(MdClassPackage.Literals.TASK__ATTRIBUTES, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.TASK__TABULAR_SECTIONS, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.TASK__FORMS, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.TASK__TEMPLATES, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.TASK__ADDRESSING_ATTRIBUTES, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.TASK__COMMANDS, MetadataObjectWriter.class);
//
//        builder.put(MdClassPackage.Literals.TABLE__TABLE_FIELDS, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.TABLE__FORMS, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.TABLE__TEMPLATES, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.TABLE__COMMANDS, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.TABLE__BASED_ON, MdObjectRefItemsWriter.class);
//
//        builder.put(McorePackage.Literals.FIELD_SOURCE__REF_FIELD_SOURCES, ISpecifiedElementWriter.ZeroWriter.class);
//
//        builder.put(MdClassPackage.Literals.HTTP_SERVICE__URL_TEMPLATES, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.URL_TEMPLATE__METHODS, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.HTTP_SERVICE__REUSE_SESSIONS, SessionReuseAndAgeWriter.class);
//        builder.put(MdClassPackage.Literals.WEB_SERVICE__REUSE_SESSIONS, SessionReuseAndAgeWriter.class);
//        builder.put(MdClassPackage.Literals.HTTP_SERVICE__SESSION_MAX_AGE, SessionReuseAndAgeWriter.class);
//        builder.put(MdClassPackage.Literals.WEB_SERVICE__SESSION_MAX_AGE, SessionReuseAndAgeWriter.class);
//
//        builder.put(MdClassPackage.Literals.CALCULATION_REGISTER__RESOURCES, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.CALCULATION_REGISTER__ATTRIBUTES, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.CALCULATION_REGISTER__DIMENSIONS, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.CALCULATION_REGISTER__FORMS, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.CALCULATION_REGISTER__TEMPLATES, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.CALCULATION_REGISTER__COMMANDS, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.CALCULATION_REGISTER__RECALCULATIONS, ReferenceWriter.class);
//        builder.put(MdClassPackage.Literals.RECALCULATION__DIMENSIONS, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.RECALCULATION_DIMENSION__LEADING_REGISTER_DATA,
//            MdObjectRefItemsWriter.class);
//
//        builder.put(MdClassPackage.Literals.WEB_SERVICE__OPERATIONS, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.OPERATION__PARAMETERS, MetadataObjectWriter.class);
//        builder.put(MdClassPackage.Literals.WEB_SERVICE__XDTO_PACKAGES, WebServiceXDTOPackagesValueWriter.class);
//
//        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED__ITEMS, PredefinedItemWriter.class);
//        builder.put(
//        MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED_ITEM__CHILD_ITEMS, PredefinedItemWriter.class);
//        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED_ITEM__CODE, PredefinedItemCodeWriter.class);
//        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED_ITEM__ACCOUNTING_FLAGS,
//            ChartOfAccountsPredefinedItemAccountingFlagsWriter.class);
//        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED_ITEM__EXT_DIMENSION_TYPES,
//            ExtDimensionTypesWriter.class);
//        builder.put(MdClassPackage.Literals.EXT_DIMENSION_TYPE__EXT_DIMENSION_ACCOUNTING_FLAGS,
//            ExtDimensionTypeAccountingFlagsWriter.class);
//
//        builder.put(MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES_PREDEFINED__ITEMS,
//            PredefinedItemWriter.class);
//        builder.put(MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES_PREDEFINED_ITEM__CONTENT,
//            PredefinedItemWriter.class);
//        builder.put(MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES_PREDEFINED_ITEM__CODE,
//            PredefinedItemCodeWriter.class);
//
//        builder.put(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES_PREDEFINED__ITEMS, PredefinedItemWriter.class);
//        builder.put(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES_PREDEFINED_ITEM__CODE,
//            PredefinedItemCodeWriter.class);
//        builder.put(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES_PREDEFINED_ITEM__DISPLACED,
//            PredefinedItemCalculationTypeWriter.class);
//        builder.put(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES_PREDEFINED_ITEM__BASE,
//            PredefinedItemCalculationTypeWriter.class);
//        builder.put(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES_PREDEFINED_ITEM__LEADING,
//            PredefinedItemCalculationTypeWriter.class);
//
//        builder.put(MdClassPackage.Literals.DATA_HISTORY_SUPPORT__DATA_HISTORY, DataHistoryWriter.class);
//
//        builder.put(MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__TYPES_FILTER_VALUE,
//            CharacteristicsDescriptionValueWriter.class);

        return builder.build();
    }

    private ImmutableMap<EClassifier, Class<? extends ISpecifiedElementWriter>> fillSpecialClassifierWriters()
    {
        ImmutableMap.Builder<EClassifier, Class<? extends ISpecifiedElementWriter>> builder = ImmutableMap.builder();

//        builder.put(MdClassPackage.Literals.CONTAINED_OBJECT, ContainedObjectsWriter.class);
//        builder.put(MdClassPackage.Literals.OBJECT_BELONGING, ObjectBelongingWriter.class);
//        builder.put(MdClassPackage.Literals.STANDARD_ATTRIBUTE, StandardAttributeWriter.class);
        builder.put(MdClassPackage.Literals.STANDARD_TABULAR_SECTION_DESCRIPTION,
            StandardTabularSectionDescriptorWriter.class);
//        builder.put(MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION, CharacteristicsDescriptionWriter.class);
        builder.put(McorePackage.Literals.PICTURE, PictureWriter.class);
        builder.put(McorePackage.Literals.HELP, ISpecifiedElementWriter.ZeroWriter.class);
        builder.put(McorePackage.Literals.LOCAL_STRING_MAP_ENTRY, LocalStringMapEntryWriter.class);
        builder.put(McorePackage.Literals.FIELD, FieldWriter.class);
        builder.put(McorePackage.Literals.TYPE_DESCRIPTION, TypeDescriptionWriter.class);
        builder.put(McorePackage.Literals.VALUE, ValueWriter.class);
//        builder.put(CommonPackage.Literals.APPLICATION_USE_PURPOSE, UsePurposeWriter.class);
        builder.put(CommonPackage.Literals.TYPE_LINK, TypeLinkWriter.class);
        builder.put(CommonPackage.Literals.CHOICE_PARAMETER_LINK, ChoiceParameterLinkWriter.class);
        builder.put(CommonPackage.Literals.CHOICE_PARAMETER, ChoiceParameterWriter.class);
//        builder.put(CommonPackage.Literals.TRANSACTIONS_ISOLATION_LEVEL, TransactionsIsolationLevelWriter.class);
        builder.put(McorePackage.Literals.QNAME, QNameWriter.class);

        fillProducedTypes(builder);

        return builder.build();
    }

    private void fillProducedTypes(ImmutableMap.Builder<EClassifier, Class<? extends ISpecifiedElementWriter>> builder)
    {
        for (EClassifier classifier : MdTypeFactory.eINSTANCE.getEPackage().getEClassifiers())
        {
            if (classifier instanceof EClass)
            {
                EClass eClass = (EClass)classifier;
                if (!eClass.isAbstract() && !eClass.isInterface()
                    && MdTypePackage.Literals.MD_TYPES.isSuperTypeOf(eClass))
                {
                    builder.put(eClass, ProducedTypeWriter.class);
                }
            }
        }
    }
}
