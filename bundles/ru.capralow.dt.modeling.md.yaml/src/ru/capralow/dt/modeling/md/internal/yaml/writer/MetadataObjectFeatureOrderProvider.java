/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.internal.yaml.writer;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.md.yaml.writer.IMetadataObjectFeatureOrderProvider;

@Singleton
public class MetadataObjectFeatureOrderProvider
    implements IMetadataObjectFeatureOrderProvider
{
    private static final String PRODUCED_TYPES_FEATURE_NAME = "producedTypes"; //$NON-NLS-1$

    private final Map<EClass, Function<Version, List<EStructuralFeature>>> propertiesOrderMap;

    public MetadataObjectFeatureOrderProvider()
    {
        ImmutableMap.Builder<EClass, Function<Version, List<EStructuralFeature>>> builder = new ImmutableMap.Builder();
        builder.put(MdClassPackage.Literals.ACCOUNTING_FLAG, this::getAccountingFlag);
        builder.put(MdClassPackage.Literals.ACCOUNTING_REGISTER_ATTRIBUTE, this::getAccountingRegisterAttribute);
        builder.put(MdClassPackage.Literals.ACCOUNTING_REGISTER_DIMENSION, this::getAccountingRegisterDimension);
        builder.put(MdClassPackage.Literals.ACCOUNTING_REGISTER_RESOURCE, this::getAccountingRegisterResource);
        builder.put(MdClassPackage.Literals.ACCUMULATION_REGISTER_ATTRIBUTE, this::getAccumulationRegisterAttribute);
        builder.put(MdClassPackage.Literals.ACCUMULATION_REGISTER_DIMENSION, this::getAccumulationRegisterDimension);
        builder.put(MdClassPackage.Literals.ACCUMULATION_REGISTER_RESOURCE, this::getAccumulationRegisterResource);
        builder.put(MdClassPackage.Literals.ADDRESSING_ATTRIBUTE, this::getAddressingAttribute);
        builder.put(MdClassPackage.Literals.BUSINESS_PROCESS, this::getBusinessProcess);
        builder.put(MdClassPackage.Literals.BUSINESS_PROCESS_ATTRIBUTE, this::getBusinessProcessAttribute);
        builder.put(MdClassPackage.Literals.CALCULATION_REGISTER_ATTRIBUTE, this::getCalculationRegisterAttribute);
        builder.put(MdClassPackage.Literals.CALCULATION_REGISTER_DIMENSION, this::getCalculationRegisterDimension);
        builder.put(MdClassPackage.Literals.CALCULATION_REGISTER_RESOURCE, this::getCalculationRegisterResource);
        builder.put(MdClassPackage.Literals.CATALOG, this::getCatalog);
        builder.put(MdClassPackage.Literals.CATALOG_ATTRIBUTE, this::getCatalogAttribute);
        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS, this::getChartOfAccounts);
        builder.put(MdClassPackage.Literals.CHART_OF_ACCOUNTS_ATTRIBUTE, this::getChartOfAccountsAttribute);
        builder.put(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES, this::getChartOfCalculationTypes);
        builder.put(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES_ATTRIBUTE,
            this::getChartOfCalculationTypesAttribute);
        builder.put(MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES, this::getChartOfCharacteristicTypes);
        builder.put(MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES_ATTRIBUTE,
            this::getChartOfCharacteristicTypesAttribute);
        builder.put(MdClassPackage.Literals.COMMON_ATTRIBUTE, this::getCommonAttribute);
        builder.put(MdClassPackage.Literals.COMMON_COMMAND, this::getCommonCommand);
        builder.put(MdClassPackage.Literals.CONFIGURATION, this::getConfiguration);
        builder.put(MdClassPackage.Literals.CONSTANT, this::getConstant);
        builder.put(MdClassPackage.Literals.DATA_PROCESSOR_TABULAR_SECTION_ATTRIBUTE,
            this::getDataProcessorTabularSectionAttribute);
        builder.put(MdClassPackage.Literals.DOCUMENT, this::getDocument);
        builder.put(MdClassPackage.Literals.DOCUMENT_ATTRIBUTE, this::getDocumentAttribute);
        builder.put(MdClassPackage.Literals.EXCHANGE_PLAN, this::getExchangePlan);
        builder.put(MdClassPackage.Literals.EXCHANGE_PLAN_ATTRIBUTE, this::getExchangePlanAttribute);
        builder.put(MdClassPackage.Literals.EXT_DIMENSION_ACCOUNTING_FLAG, this::getExtDimensionAccountingFlag);
        builder.put(MdClassPackage.Literals.INFORMATION_REGISTER, this::getInformationRegister);
        builder.put(MdClassPackage.Literals.INFORMATION_REGISTER_ATTRIBUTE, this::getInformationRegisterAttribute);
        builder.put(MdClassPackage.Literals.INFORMATION_REGISTER_DIMENSION, this::getInformationRegisterDimension);
        builder.put(MdClassPackage.Literals.INFORMATION_REGISTER_RESOURCE, this::getInformationRegisterResource);
        builder.put(MdClassPackage.Literals.REPORT_TABULAR_SECTION_ATTRIBUTE, this::getReportTabularSectionAttribute);
        builder.put(MdClassPackage.Literals.TABULAR_SECTION_ATTRIBUTE, this::getTabularSectionAttribute);
        builder.put(MdClassPackage.Literals.TASK, this::getTask);
        builder.put(MdClassPackage.Literals.TASK_ATTRIBUTE, this::getTaskAttribute);
        this.propertiesOrderMap = builder.build();
    }

    @Override
    public List<EStructuralFeature> getInnerInfo(EClass eClass, Version version)
    {
        if (eClass == MdClassPackage.Literals.CONFIGURATION)
        {
            return List.of(MdClassPackage.Literals.CONFIGURATION__CONTAINED_OBJECTS);
        }
        if (eClass == MdClassPackage.Literals.EXCHANGE_PLAN)
        {
            return List.of(MdClassPackage.Literals.EXCHANGE_PLAN__THIS_NODE,
                MdClassPackage.Literals.EXCHANGE_PLAN__PRODUCED_TYPES);
        }
        if (eClass == MdClassPackage.Literals.EXTERNAL_REPORT)
        {
            return List.of(MdClassPackage.Literals.EXTERNAL_REPORT__CONTAINED_OBJECTS,
                MdClassPackage.Literals.EXTERNAL_REPORT__PRODUCED_TYPES);
        }
        if (eClass == MdClassPackage.Literals.EXTERNAL_DATA_PROCESSOR)
        {
            return List.of(MdClassPackage.Literals.EXTERNAL_DATA_PROCESSOR__CONTAINED_OBJECTS,
                MdClassPackage.Literals.EXTERNAL_DATA_PROCESSOR__PRODUCED_TYPES);
        }

        EStructuralFeature producedTypes = eClass.getEStructuralFeature(PRODUCED_TYPES_FEATURE_NAME);
        return (producedTypes != null) ? List.of(producedTypes) : Collections.emptyList();
    }

    @Override
    public List<EStructuralFeature> getProperties(EClass paramEClass, Version version)
    {
        return this.propertiesOrderMap
            .getOrDefault(paramEClass, v -> (new ListBuilder(paramEClass, this::defaultPropertyFilter)).build())
            .apply(version);
    }

    @Override
    public List<EStructuralFeature> getChildren(EClass eClass, Version version)
    {
        return eClass.getEAllStructuralFeatures()
            .stream()
            .filter(f -> f.getEAnnotation("http://www.1c.ru/v8/dt/metadata/MdClass") != null)
            .collect(Collectors.toList());
    }

    Map<EClass, Function<Version, List<EStructuralFeature>>> getPropertiesOrderMap()
    {
        return this.propertiesOrderMap;
    }

    private List<EStructuralFeature> getAccountingFlag(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.ACCOUNTING_FLAG, this::defaultPropertyFilter))
            .cursor(MdClassPackage.Literals.BASIC_FEATURE__MAX_VALUE)
            .next(MdClassPackage.Literals.ACCOUNTING_FLAG__FILL_FROM_FILLING_VALUE)
            .next(MdClassPackage.Literals.ACCOUNTING_FLAG__FILL_VALUE)
            .cursor(MdClassPackage.Literals.BASIC_FEATURE__LINK_BY_TYPE)
            .next(MdClassPackage.Literals.ACCOUNTING_FLAG__CHOICE_HISTORY_ON_INPUT)
            .build();
    }

    private List<EStructuralFeature> getAccountingRegisterDimension(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.ACCOUNTING_REGISTER_DIMENSION, this::defaultPropertyFilter))
            .cursor(MdClassPackage.Literals.ACCOUNTING_REGISTER_DIMENSION__ACCOUNTING_FLAG)
            .next(MdClassPackage.Literals.REGISTER_DIMENSION__DENY_INCOMPLETE_VALUES)
            .next(MdClassPackage.Literals.REGISTER_DIMENSION__INDEXING)
            .next(MdClassPackage.Literals.REGISTER_DIMENSION__FULL_TEXT_SEARCH)
            .build();
    }

    private List<EStructuralFeature> getAccountingRegisterResource(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.ACCOUNTING_REGISTER_RESOURCE, this::defaultPropertyFilter))
            .cursor(MdClassPackage.Literals.ACCOUNTING_REGISTER_RESOURCE__EXT_DIMENSION_ACCOUNTING_FLAG)
            .next(MdClassPackage.Literals.REGISTER_RESOURCE__FULL_TEXT_SEARCH)
            .build();
    }

    private List<EStructuralFeature> getAccountingRegisterAttribute(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.ACCOUNTING_REGISTER_ATTRIBUTE, this::defaultPropertyFilter))
            .cursor(MdClassPackage.Literals.BASIC_FEATURE__LINK_BY_TYPE)
            .next(MdClassPackage.Literals.ACCOUNTING_REGISTER_ATTRIBUTE__CHOICE_HISTORY_ON_INPUT)
            .build();
    }

    private List<EStructuralFeature> getAccumulationRegisterAttribute(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.ACCUMULATION_REGISTER_ATTRIBUTE, this::defaultPropertyFilter))
            .cursor(MdClassPackage.Literals.BASIC_FEATURE__LINK_BY_TYPE)
            .next(MdClassPackage.Literals.ACCUMULATION_REGISTER_ATTRIBUTE__CHOICE_HISTORY_ON_INPUT)
            .build();
    }

    private List<EStructuralFeature> getAccumulationRegisterDimension(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.ACCUMULATION_REGISTER_DIMENSION, this::defaultPropertyFilter))
            .cursor(MdClassPackage.Literals.BASIC_FEATURE__LINK_BY_TYPE)
            .next(MdClassPackage.Literals.ACCUMULATION_REGISTER_DIMENSION__CHOICE_HISTORY_ON_INPUT)
            .build();
    }

    private List<EStructuralFeature> getAccumulationRegisterResource(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.ACCUMULATION_REGISTER_RESOURCE, this::defaultPropertyFilter))
            .cursor(MdClassPackage.Literals.BASIC_FEATURE__LINK_BY_TYPE)
            .next(MdClassPackage.Literals.ACCUMULATION_REGISTER_RESOURCE__CHOICE_HISTORY_ON_INPUT)
            .build();
    }

    private List<EStructuralFeature> getAddressingAttribute(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.ADDRESSING_ATTRIBUTE, this::defaultPropertyFilter))
            .cursor(MdClassPackage.Literals.BASIC_FEATURE__MAX_VALUE)
            .next(MdClassPackage.Literals.ADDRESSING_ATTRIBUTE__FILL_FROM_FILLING_VALUE)
            .next(MdClassPackage.Literals.ADDRESSING_ATTRIBUTE__FILL_VALUE)
            .cursor(MdClassPackage.Literals.ADDRESSING_ATTRIBUTE__FULL_TEXT_SEARCH)
            .next(MdClassPackage.Literals.DATA_HISTORY_SUPPORT__DATA_HISTORY)
            .build();
    }

    private List<EStructuralFeature> getBusinessProcess(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.BUSINESS_PROCESS, this::defaultPropertyFilter))
            .cursor(MdClassPackage.Literals.BASIC_DB_OBJECT__USE_STANDARD_COMMANDS)
            .next(MdClassPackage.Literals.BUSINESS_PROCESS__EDIT_TYPE)
            .cursor(MdClassPackage.Literals.BASIC_DB_OBJECT__INPUT_BY_STRING)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__CREATE_ON_INPUT)
            .cursor(MdClassPackage.Literals.BASIC_DB_OBJECT__SEARCH_STRING_MODE_ON_INPUT_BY_STRING)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__CHOICE_DATA_GET_MODE_ON_INPUT_BY_STRING)
            .cursor(MdClassPackage.Literals.BASIC_DB_OBJECT__FULL_TEXT_SEARCH_ON_INPUT_BY_STRING)
            .next(MdClassPackage.Literals.BUSINESS_PROCESS__DEFAULT_OBJECT_FORM)
            .next(MdClassPackage.Literals.BUSINESS_PROCESS__DEFAULT_LIST_FORM)
            .next(MdClassPackage.Literals.BUSINESS_PROCESS__DEFAULT_CHOICE_FORM)
            .next(MdClassPackage.Literals.BUSINESS_PROCESS__AUXILIARY_OBJECT_FORM)
            .next(MdClassPackage.Literals.BUSINESS_PROCESS__AUXILIARY_LIST_FORM)
            .next(MdClassPackage.Literals.BUSINESS_PROCESS__AUXILIARY_CHOICE_FORM)
            .next(MdClassPackage.Literals.BUSINESS_PROCESS__CHOICE_HISTORY_ON_INPUT)
            .next(MdClassPackage.Literals.BUSINESS_PROCESS__NUMBER_TYPE)
            .next(MdClassPackage.Literals.BUSINESS_PROCESS__NUMBER_LENGTH)
            .next(MdClassPackage.Literals.BUSINESS_PROCESS__NUMBER_ALLOWED_LENGTH)
            .next(MdClassPackage.Literals.BUSINESS_PROCESS__CHECK_UNIQUE)
            .cursor(MdClassPackage.Literals.BASIC_DB_OBJECT__CHARACTERISTICS)
            .next(MdClassPackage.Literals.BUSINESS_PROCESS__AUTONUMBERING)
            .cursor(MdClassPackage.Literals.BASIC_DB_OBJECT__BASED_ON)
            .next(MdClassPackage.Literals.BUSINESS_PROCESS__NUMBER_PERIODICITY)
            .next(MdClassPackage.Literals.BUSINESS_PROCESS__TASK)
            .next(MdClassPackage.Literals.BUSINESS_PROCESS__CREATE_TASK_IN_PRIVILEGED_MODE)
            .cursor(MdClassPackage.Literals.BASIC_DB_OBJECT__DATA_LOCK_CONTROL_MODE)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__INCLUDE_HELP_IN_CONTENTS)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__HELP)
            .cursor(MdClassPackage.Literals.BASIC_DB_OBJECT__EXPLANATION)
            .next(MdClassPackage.Literals.DATA_HISTORY_SUPPORT__DATA_HISTORY)
            .build();
    }

    private List<EStructuralFeature> getBusinessProcessAttribute(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.BUSINESS_PROCESS_ATTRIBUTE, this::defaultPropertyFilter))
            .cursor(MdClassPackage.Literals.BASIC_FEATURE__MAX_VALUE)
            .next(MdClassPackage.Literals.DB_OBJECT_ATTRIBUTE__FILL_FROM_FILLING_VALUE)
            .next(MdClassPackage.Literals.DB_OBJECT_ATTRIBUTE__FILL_VALUE)
            .cursor(MdClassPackage.Literals.BASIC_FEATURE__LINK_BY_TYPE)
            .next(MdClassPackage.Literals.BUSINESS_PROCESS_ATTRIBUTE__CHOICE_HISTORY_ON_INPUT)
            .cursor(MdClassPackage.Literals.DB_OBJECT_ATTRIBUTE__FULL_TEXT_SEARCH)
            .next(MdClassPackage.Literals.DATA_HISTORY_SUPPORT__DATA_HISTORY)
            .build();
    }

    private List<EStructuralFeature> getCalculationRegisterAttribute(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.CALCULATION_REGISTER_ATTRIBUTE, this::defaultPropertyFilter))
            .cursor(MdClassPackage.Literals.CALCULATION_REGISTER_ATTRIBUTE__SCHEDULE_LINK)
            .next(MdClassPackage.Literals.REGISTER_ATTRIBUTE__INDEXING)
            .next(MdClassPackage.Literals.REGISTER_ATTRIBUTE__FULL_TEXT_SEARCH)
            .build();
    }

    private List<EStructuralFeature> getCalculationRegisterDimension(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.CALCULATION_REGISTER_DIMENSION, this::defaultPropertyFilter))
            .cursor(MdClassPackage.Literals.BASIC_FEATURE__LINK_BY_TYPE)
            .next(MdClassPackage.Literals.CALCULATION_REGISTER_DIMENSION__CHOICE_HISTORY_ON_INPUT)
            .cursor(MdClassPackage.Literals.REGISTER_DIMENSION__DENY_INCOMPLETE_VALUES)
            .next(MdClassPackage.Literals.CALCULATION_REGISTER_DIMENSION__BASE_DIMENSION)
            .next(MdClassPackage.Literals.CALCULATION_REGISTER_DIMENSION__SCHEDULE_LINK)
            .build();
    }

    private List<EStructuralFeature> getCalculationRegisterResource(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.CALCULATION_REGISTER_RESOURCE, this::defaultPropertyFilter))
            .cursor(MdClassPackage.Literals.BASIC_FEATURE__LINK_BY_TYPE)
            .next(MdClassPackage.Literals.CALCULATION_REGISTER_RESOURCE__CHOICE_HISTORY_ON_INPUT)
            .build();
    }

    private List<EStructuralFeature> getCatalog(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.CATALOG, this::defaultPropertyFilter))
            .cursor(MdClassPackage.Literals.CATALOG__FOLDERS_ON_TOP)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__USE_STANDARD_COMMANDS)
            .cursor(MdClassPackage.Literals.CATALOG__CHOICE_MODE)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__INPUT_BY_STRING)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__SEARCH_STRING_MODE_ON_INPUT_BY_STRING)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__FULL_TEXT_SEARCH_ON_INPUT_BY_STRING)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__CHOICE_DATA_GET_MODE_ON_INPUT_BY_STRING)
            .cursor(MdClassPackage.Literals.CATALOG__DEFAULT_PRESENTATION)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__STANDARD_ATTRIBUTES)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__CHARACTERISTICS)
            .cursor(MdClassPackage.Literals.CATALOG__AUXILIARY_FOLDER_CHOICE_FORM)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__INCLUDE_HELP_IN_CONTENTS)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__HELP)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__BASED_ON)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__DATA_LOCK_FIELDS)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__DATA_LOCK_CONTROL_MODE)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__FULL_TEXT_SEARCH)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__OBJECT_PRESENTATION)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__EXTENDED_OBJECT_PRESENTATION)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__LIST_PRESENTATION)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__EXTENDED_LIST_PRESENTATION)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__EXPLANATION)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__CREATE_ON_INPUT)
            .cursor(MdClassPackage.Literals.CATALOG__CHOICE_HISTORY_ON_INPUT)
            .next(MdClassPackage.Literals.DATA_HISTORY_SUPPORT__DATA_HISTORY)
            .build();
    }

    private List<EStructuralFeature> getCatalogAttribute(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.CATALOG_ATTRIBUTE, this::defaultPropertyFilter))
            .cursor(MdClassPackage.Literals.BASIC_FEATURE__MAX_VALUE)
            .next(MdClassPackage.Literals.DB_OBJECT_ATTRIBUTE__FILL_FROM_FILLING_VALUE)
            .next(MdClassPackage.Literals.DB_OBJECT_ATTRIBUTE__FILL_VALUE)
            .cursor(MdClassPackage.Literals.CATALOG_ATTRIBUTE__CHOICE_HISTORY_ON_INPUT)
            .next(MdClassPackage.Literals.HIERARCHICAL_DB_OBJECT_ATTRIBUTE__USE)
            .next(MdClassPackage.Literals.DB_OBJECT_ATTRIBUTE__INDEXING)
            .next(MdClassPackage.Literals.DB_OBJECT_ATTRIBUTE__FULL_TEXT_SEARCH)
            .next(MdClassPackage.Literals.DATA_HISTORY_SUPPORT__DATA_HISTORY)
            .build();
    }

    private List<EStructuralFeature> getChartOfAccounts(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.CHART_OF_ACCOUNTS, this::defaultPropertyFilter))
            .cursor(MdClassPackage.Literals.BASIC_DB_OBJECT__USE_STANDARD_COMMANDS)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__INCLUDE_HELP_IN_CONTENTS)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__HELP)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__BASED_ON)
            .cursor(MdClassPackage.Literals.CHART_OF_ACCOUNTS__CHOICE_MODE)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__INPUT_BY_STRING)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__SEARCH_STRING_MODE_ON_INPUT_BY_STRING)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__FULL_TEXT_SEARCH_ON_INPUT_BY_STRING)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__CHOICE_DATA_GET_MODE_ON_INPUT_BY_STRING)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__CREATE_ON_INPUT)
            .cursor(MdClassPackage.Literals.CHART_OF_ACCOUNTS__DEFAULT_PRESENTATION)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__STANDARD_ATTRIBUTES)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__CHARACTERISTICS)
            .cursor(MdClassPackage.Literals.CHART_OF_ACCOUNTS__ORDER_LENGTH)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__DATA_LOCK_FIELDS)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__DATA_LOCK_CONTROL_MODE)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__FULL_TEXT_SEARCH)
            .next(MdClassPackage.Literals.DATA_HISTORY_SUPPORT__DATA_HISTORY)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__OBJECT_PRESENTATION)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__EXTENDED_OBJECT_PRESENTATION)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__LIST_PRESENTATION)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__EXTENDED_LIST_PRESENTATION)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__EXPLANATION)
            .build();
    }

    private List<EStructuralFeature> getChartOfAccountsAttribute(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.CHART_OF_ACCOUNTS_ATTRIBUTE, this::defaultPropertyFilter))
            .cursor(MdClassPackage.Literals.BASIC_FEATURE__MAX_VALUE)
            .next(MdClassPackage.Literals.DB_OBJECT_ATTRIBUTE__FILL_FROM_FILLING_VALUE)
            .next(MdClassPackage.Literals.DB_OBJECT_ATTRIBUTE__FILL_VALUE)
            .cursor(MdClassPackage.Literals.BASIC_FEATURE__LINK_BY_TYPE)
            .next(MdClassPackage.Literals.CHART_OF_ACCOUNTS_ATTRIBUTE__CHOICE_HISTORY_ON_INPUT)
            .build();
    }

    private List<EStructuralFeature> getChartOfCalculationTypes(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES, this::defaultPropertyFilter))
            .cursor(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES__CHOICE_MODE)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__INPUT_BY_STRING)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__SEARCH_STRING_MODE_ON_INPUT_BY_STRING)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__FULL_TEXT_SEARCH_ON_INPUT_BY_STRING)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__CHOICE_DATA_GET_MODE_ON_INPUT_BY_STRING)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__CREATE_ON_INPUT)
            .cursor(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES__AUXILIARY_CHOICE_FORM)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__BASED_ON)
            .cursor(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES__ACTION_PERIOD_USE)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__STANDARD_ATTRIBUTES)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__CHARACTERISTICS)
            .cursor(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES__PREDEFINED_DATA_UPDATE)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__INCLUDE_HELP_IN_CONTENTS)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__HELP)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__DATA_LOCK_FIELDS)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__DATA_LOCK_CONTROL_MODE)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__FULL_TEXT_SEARCH)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__OBJECT_PRESENTATION)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__EXTENDED_OBJECT_PRESENTATION)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__LIST_PRESENTATION)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__EXTENDED_LIST_PRESENTATION)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__EXPLANATION)
            .next(MdClassPackage.Literals.DATA_HISTORY_SUPPORT__DATA_HISTORY)
            .build();
    }

    private List<EStructuralFeature> getChartOfCalculationTypesAttribute(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES_ATTRIBUTE,
            this::defaultPropertyFilter)).cursor(MdClassPackage.Literals.BASIC_FEATURE__MAX_VALUE)
                .next(MdClassPackage.Literals.DB_OBJECT_ATTRIBUTE__FILL_FROM_FILLING_VALUE)
                .next(MdClassPackage.Literals.DB_OBJECT_ATTRIBUTE__FILL_VALUE)
                .cursor(MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES_ATTRIBUTE__CHOICE_HISTORY_ON_INPUT)
                .next(MdClassPackage.Literals.DB_OBJECT_ATTRIBUTE__INDEXING)
                .next(MdClassPackage.Literals.DB_OBJECT_ATTRIBUTE__FULL_TEXT_SEARCH)
                .next(MdClassPackage.Literals.DATA_HISTORY_SUPPORT__DATA_HISTORY)
                .build();
    }

    private List<EStructuralFeature> getChartOfCharacteristicTypes(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES, this::defaultPropertyFilter))
            .cursor(MdClassPackage.Literals.BASIC_DB_OBJECT__USE_STANDARD_COMMANDS)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__INCLUDE_HELP_IN_CONTENTS)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__HELP)
            .cursor(MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES__DEFAULT_PRESENTATION)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__STANDARD_ATTRIBUTES)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__CHARACTERISTICS)
            .cursor(MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES__CHOICE_MODE)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__INPUT_BY_STRING)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__CREATE_ON_INPUT)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__SEARCH_STRING_MODE_ON_INPUT_BY_STRING)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__CHOICE_DATA_GET_MODE_ON_INPUT_BY_STRING)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__FULL_TEXT_SEARCH_ON_INPUT_BY_STRING)
            .cursor(MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES__AUXILIARY_FOLDER_CHOICE_FORM)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__BASED_ON)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__DATA_LOCK_FIELDS)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__DATA_LOCK_CONTROL_MODE)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__FULL_TEXT_SEARCH)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__OBJECT_PRESENTATION)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__EXTENDED_OBJECT_PRESENTATION)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__LIST_PRESENTATION)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__EXTENDED_LIST_PRESENTATION)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__EXPLANATION)
            .next(MdClassPackage.Literals.DATA_HISTORY_SUPPORT__DATA_HISTORY)
            .build();
    }

    private List<EStructuralFeature> getChartOfCharacteristicTypesAttribute(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES_ATTRIBUTE,
            this::defaultPropertyFilter)).cursor(MdClassPackage.Literals.BASIC_FEATURE__MAX_VALUE)
                .next(MdClassPackage.Literals.DB_OBJECT_ATTRIBUTE__FILL_FROM_FILLING_VALUE)
                .next(MdClassPackage.Literals.DB_OBJECT_ATTRIBUTE__FILL_VALUE)
                .cursor(MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES_ATTRIBUTE__CHOICE_HISTORY_ON_INPUT)
                .next(MdClassPackage.Literals.DB_OBJECT_ATTRIBUTE__INDEXING)
                .next(MdClassPackage.Literals.HIERARCHICAL_DB_OBJECT_ATTRIBUTE__USE)
                .next(MdClassPackage.Literals.DB_OBJECT_ATTRIBUTE__FULL_TEXT_SEARCH)
                .next(MdClassPackage.Literals.DATA_HISTORY_SUPPORT__DATA_HISTORY)
                .build();
    }

    private List<EStructuralFeature> getCommonAttribute(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.COMMON_ATTRIBUTE, this::defaultPropertyFilter))
            .cursor(MdClassPackage.Literals.BASIC_FEATURE__MAX_VALUE)
            .next(MdClassPackage.Literals.COMMON_ATTRIBUTE__FILL_FROM_FILLING_VALUE)
            .next(MdClassPackage.Literals.COMMON_ATTRIBUTE__FILL_VALUE)
            .cursor(MdClassPackage.Literals.COMMON_ATTRIBUTE__FULL_TEXT_SEARCH)
            .next(MdClassPackage.Literals.DATA_HISTORY_SUPPORT__DATA_HISTORY)
            .build();
    }

    private List<EStructuralFeature> getCommonCommand(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.COMMON_COMMAND, this::defaultPropertyFilter))
            .cursor(MdClassPackage.Literals.COMMON_COMMAND__HELP)
            .next(MdClassPackage.Literals.BASIC_COMMAND__COMMAND_PARAMETER_TYPE)
            .next(MdClassPackage.Literals.BASIC_COMMAND__PARAMETER_USE_MODE)
            .next(MdClassPackage.Literals.BASIC_COMMAND__MODIFIES_DATA)
            .next(MdClassPackage.Literals.BASIC_COMMAND__ON_MAIN_SERVER_UNAVALABLE_BEHAVIOR)
            .build();
    }

    private List<EStructuralFeature> getConfiguration(Version version)
    {
        Predicate<EStructuralFeature> filter = feature -> !(!defaultPropertyFilter(feature)
            && feature != MdClassPackage.Literals.CONFIGURATION__DEFAULT_INTERFACE);
        return (new ListBuilder(MdClassPackage.Literals.CONFIGURATION, filter))
            .cursor(MdClassPackage.Literals.MD_OBJECT__COMMENT)
            .next(MdClassPackage.Literals.CONFIGURATION__CONFIGURATION_EXTENSION_PURPOSE)
            .cursor(version.isGreaterThan(Version.V8_3_14)
                ? (EStructuralFeature)MdClassPackage.Literals.CONFIGURATION__REQUIRED_MOBILE_APPLICATION_PERMISSIONS8315
                : (EStructuralFeature)MdClassPackage.Literals.CONFIGURATION__REQUIRED_MOBILE_APPLICATION_PERMISSIONS)
            .next(MdClassPackage.Literals.CONFIGURATION__USED_MOBILE_APPLICATION_FUNCTIONALITIES)
            .next(MdClassPackage.Literals.CONFIGURATION__STANDALONE_CONFIGURATION_RESTRICTION_ROLES)
            .next(MdClassPackage.Literals.CONFIGURATION__MOBILE_APPLICATION_URLS)
            .next(MdClassPackage.Literals.CONFIGURATION__MAIN_CLIENT_APPLICATION_WINDOW_MODE)
            .build();
    }

    private List<EStructuralFeature> getConstant(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.CONSTANT, this::defaultPropertyFilter))
            .cursor(MdClassPackage.Literals.CONSTANT__DATA_LOCK_CONTROL_MODE)
            .next(MdClassPackage.Literals.DATA_HISTORY_SUPPORT__DATA_HISTORY)
            .build();
    }

    private List<EStructuralFeature> getDataProcessorTabularSectionAttribute(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.DATA_PROCESSOR_TABULAR_SECTION_ATTRIBUTE,
            this::defaultPropertyFilter)).cursor(MdClassPackage.Literals.BASIC_FEATURE__MAX_VALUE)
                .next(MdClassPackage.Literals.DATA_PROCESSOR_TABULAR_SECTION_ATTRIBUTE__FILL_FROM_FILLING_VALUE)
                .next(MdClassPackage.Literals.DATA_PROCESSOR_TABULAR_SECTION_ATTRIBUTE__FILL_VALUE)
                .build();
    }

    private List<EStructuralFeature> getDocument(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.DOCUMENT, this::defaultPropertyFilter))
            .cursor(MdClassPackage.Literals.DOCUMENT__AUTONUMBERING)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__STANDARD_ATTRIBUTES)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__CHARACTERISTICS)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__BASED_ON)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__INPUT_BY_STRING)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__CREATE_ON_INPUT)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__SEARCH_STRING_MODE_ON_INPUT_BY_STRING)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__FULL_TEXT_SEARCH_ON_INPUT_BY_STRING)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__CHOICE_DATA_GET_MODE_ON_INPUT_BY_STRING)
            .cursor(MdClassPackage.Literals.DOCUMENT__UNPOST_IN_PRIVILEGED_MODE)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__INCLUDE_HELP_IN_CONTENTS)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__HELP)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__DATA_LOCK_FIELDS)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__DATA_LOCK_CONTROL_MODE)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__FULL_TEXT_SEARCH)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__OBJECT_PRESENTATION)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__EXTENDED_OBJECT_PRESENTATION)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__LIST_PRESENTATION)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__EXTENDED_LIST_PRESENTATION)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__EXPLANATION)
            .cursor(MdClassPackage.Literals.DOCUMENT__CHOICE_HISTORY_ON_INPUT)
            .next(MdClassPackage.Literals.DATA_HISTORY_SUPPORT__DATA_HISTORY)
            .build();
    }

    private List<EStructuralFeature> getDocumentAttribute(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.DOCUMENT_ATTRIBUTE, this::defaultPropertyFilter))
            .cursor(MdClassPackage.Literals.BASIC_FEATURE__MAX_VALUE)
            .next(MdClassPackage.Literals.DB_OBJECT_ATTRIBUTE__FILL_FROM_FILLING_VALUE)
            .next(MdClassPackage.Literals.DB_OBJECT_ATTRIBUTE__FILL_VALUE)
            .cursor(MdClassPackage.Literals.DOCUMENT_ATTRIBUTE__CHOICE_HISTORY_ON_INPUT)
            .next(MdClassPackage.Literals.DB_OBJECT_ATTRIBUTE__INDEXING)
            .next(MdClassPackage.Literals.DB_OBJECT_ATTRIBUTE__FULL_TEXT_SEARCH)
            .next(MdClassPackage.Literals.DATA_HISTORY_SUPPORT__DATA_HISTORY)
            .build();
    }

    private List<EStructuralFeature> getExchangePlan(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.EXCHANGE_PLAN, this::defaultPropertyFilter))
            .cursor(MdClassPackage.Literals.EXCHANGE_PLAN__CHOICE_MODE)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__INPUT_BY_STRING)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__SEARCH_STRING_MODE_ON_INPUT_BY_STRING)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__FULL_TEXT_SEARCH_ON_INPUT_BY_STRING)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__CHOICE_DATA_GET_MODE_ON_INPUT_BY_STRING)
            .cursor(MdClassPackage.Literals.EXCHANGE_PLAN__AUXILIARY_CHOICE_FORM)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__STANDARD_ATTRIBUTES)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__CHARACTERISTICS)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__BASED_ON)
            .cursor(MdClassPackage.Literals.EXCHANGE_PLAN__INCLUDE_CONFIGURATION_EXTENSIONS)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__CREATE_ON_INPUT)
            .cursor(MdClassPackage.Literals.EXCHANGE_PLAN__CHOICE_HISTORY_ON_INPUT)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__INCLUDE_HELP_IN_CONTENTS)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__HELP)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__DATA_LOCK_FIELDS)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__DATA_LOCK_CONTROL_MODE)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__FULL_TEXT_SEARCH)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__OBJECT_PRESENTATION)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__EXTENDED_OBJECT_PRESENTATION)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__LIST_PRESENTATION)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__EXTENDED_LIST_PRESENTATION)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__EXPLANATION)
            .next(MdClassPackage.Literals.DATA_HISTORY_SUPPORT__DATA_HISTORY)
            .build();
    }

    private List<EStructuralFeature> getExchangePlanAttribute(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.EXCHANGE_PLAN_ATTRIBUTE, this::defaultPropertyFilter))
            .cursor(MdClassPackage.Literals.BASIC_FEATURE__MAX_VALUE)
            .next(MdClassPackage.Literals.DB_OBJECT_ATTRIBUTE__FILL_FROM_FILLING_VALUE)
            .next(MdClassPackage.Literals.DB_OBJECT_ATTRIBUTE__FILL_VALUE)
            .cursor(MdClassPackage.Literals.EXCHANGE_PLAN_ATTRIBUTE__CHOICE_HISTORY_ON_INPUT)
            .next(MdClassPackage.Literals.DB_OBJECT_ATTRIBUTE__INDEXING)
            .next(MdClassPackage.Literals.DB_OBJECT_ATTRIBUTE__FULL_TEXT_SEARCH)
            .next(MdClassPackage.Literals.DATA_HISTORY_SUPPORT__DATA_HISTORY)
            .build();
    }

    private List<EStructuralFeature> getExtDimensionAccountingFlag(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.EXT_DIMENSION_ACCOUNTING_FLAG, this::defaultPropertyFilter))
            .cursor(MdClassPackage.Literals.BASIC_FEATURE__MAX_VALUE)
            .next(MdClassPackage.Literals.EXT_DIMENSION_ACCOUNTING_FLAG__FILL_FROM_FILLING_VALUE)
            .next(MdClassPackage.Literals.EXT_DIMENSION_ACCOUNTING_FLAG__FILL_VALUE)
            .cursor(MdClassPackage.Literals.EXT_DIMENSION_ACCOUNTING_FLAG__CHOICE_HISTORY_ON_INPUT)
            .next(MdClassPackage.Literals.DATA_HISTORY_SUPPORT__DATA_HISTORY)
            .build();
    }

    private List<EStructuralFeature> getInformationRegister(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.INFORMATION_REGISTER, this::defaultPropertyFilter))
            .cursor(MdClassPackage.Literals.INFORMATION_REGISTER__EXPLANATION)
            .next(MdClassPackage.Literals.DATA_HISTORY_SUPPORT__DATA_HISTORY)
            .build();
    }

    private List<EStructuralFeature> getInformationRegisterAttribute(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.INFORMATION_REGISTER_ATTRIBUTE, this::defaultPropertyFilter))
            .cursor(MdClassPackage.Literals.BASIC_FEATURE__MAX_VALUE)
            .next(MdClassPackage.Literals.INFORMATION_REGISTER_ATTRIBUTE__FILL_FROM_FILLING_VALUE)
            .next(MdClassPackage.Literals.INFORMATION_REGISTER_ATTRIBUTE__FILL_VALUE)
            .cursor(MdClassPackage.Literals.INFORMATION_REGISTER_ATTRIBUTE__CHOICE_HISTORY_ON_INPUT)
            .next(MdClassPackage.Literals.REGISTER_ATTRIBUTE__INDEXING)
            .next(MdClassPackage.Literals.REGISTER_ATTRIBUTE__FULL_TEXT_SEARCH)
            .next(MdClassPackage.Literals.DATA_HISTORY_SUPPORT__DATA_HISTORY)
            .build();
    }

    private List<EStructuralFeature> getInformationRegisterDimension(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.INFORMATION_REGISTER_DIMENSION, this::defaultPropertyFilter))
            .cursor(MdClassPackage.Literals.BASIC_FEATURE__MAX_VALUE)
            .next(MdClassPackage.Literals.INFORMATION_REGISTER_DIMENSION__FILL_FROM_FILLING_VALUE)
            .next(MdClassPackage.Literals.INFORMATION_REGISTER_DIMENSION__FILL_VALUE)
            .cursor(MdClassPackage.Literals.INFORMATION_REGISTER_DIMENSION__MAIN_FILTER)
            .next(MdClassPackage.Literals.REGISTER_DIMENSION__DENY_INCOMPLETE_VALUES)
            .next(MdClassPackage.Literals.REGISTER_DIMENSION__INDEXING)
            .next(MdClassPackage.Literals.REGISTER_DIMENSION__FULL_TEXT_SEARCH)
            .next(MdClassPackage.Literals.DATA_HISTORY_SUPPORT__DATA_HISTORY)
            .build();
    }

    private List<EStructuralFeature> getInformationRegisterResource(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.INFORMATION_REGISTER_RESOURCE, this::defaultPropertyFilter))
            .cursor(MdClassPackage.Literals.BASIC_FEATURE__MAX_VALUE)
            .next(MdClassPackage.Literals.INFORMATION_REGISTER_RESOURCE__FILL_FROM_FILLING_VALUE)
            .next(MdClassPackage.Literals.INFORMATION_REGISTER_RESOURCE__FILL_VALUE)
            .cursor(MdClassPackage.Literals.INFORMATION_REGISTER_RESOURCE__INDEXING)
            .next(MdClassPackage.Literals.REGISTER_RESOURCE__FULL_TEXT_SEARCH)
            .next(MdClassPackage.Literals.DATA_HISTORY_SUPPORT__DATA_HISTORY)
            .build();
    }

    private List<EStructuralFeature> getReportTabularSectionAttribute(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.REPORT_TABULAR_SECTION_ATTRIBUTE, this::defaultPropertyFilter))
            .cursor(MdClassPackage.Literals.BASIC_FEATURE__MAX_VALUE)
            .next(MdClassPackage.Literals.REPORT_TABULAR_SECTION_ATTRIBUTE__FILL_FROM_FILLING_VALUE)
            .next(MdClassPackage.Literals.REPORT_TABULAR_SECTION_ATTRIBUTE__FILL_VALUE)
            .build();
    }

    private List<EStructuralFeature> getTabularSectionAttribute(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.TABULAR_SECTION_ATTRIBUTE, this::defaultPropertyFilter))
            .cursor(MdClassPackage.Literals.TABULAR_SECTION_ATTRIBUTE__FULL_TEXT_SEARCH)
            .next(MdClassPackage.Literals.DATA_HISTORY_SUPPORT__DATA_HISTORY)
            .build();
    }

    private List<EStructuralFeature> getTask(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.TASK, this::defaultPropertyFilter))
            .cursor(MdClassPackage.Literals.BASIC_DB_OBJECT__USE_STANDARD_COMMANDS)
            .next(MdClassPackage.Literals.TASK__NUMBER_TYPE)
            .next(MdClassPackage.Literals.TASK__NUMBER_LENGTH)
            .next(MdClassPackage.Literals.TASK__NUMBER_ALLOWED_LENGTH)
            .next(MdClassPackage.Literals.TASK__CHECK_UNIQUE)
            .next(MdClassPackage.Literals.TASK__AUTONUMBERING)
            .next(MdClassPackage.Literals.TASK__TASK_NUMBER_AUTO_PREFIX)
            .next(MdClassPackage.Literals.TASK__DESCRIPTION_LENGTH)
            .next(MdClassPackage.Literals.TASK__ADDRESSING)
            .next(MdClassPackage.Literals.TASK__MAIN_ADDRESSING_ATTRIBUTE)
            .next(MdClassPackage.Literals.TASK__CURRENT_PERFORMER)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__BASED_ON)
            .cursor(MdClassPackage.Literals.BASIC_DB_OBJECT__CHARACTERISTICS)
            .next(MdClassPackage.Literals.TASK__DEFAULT_PRESENTATION)
            .next(MdClassPackage.Literals.TASK__EDIT_TYPE)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__INPUT_BY_STRING)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__SEARCH_STRING_MODE_ON_INPUT_BY_STRING)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__FULL_TEXT_SEARCH_ON_INPUT_BY_STRING)
            .next(MdClassPackage.Literals.BASIC_DB_OBJECT__CHOICE_DATA_GET_MODE_ON_INPUT_BY_STRING)
            .cursor(MdClassPackage.Literals.BASIC_DB_OBJECT__CREATE_ON_INPUT)
            .next(MdClassPackage.Literals.TASK__DEFAULT_OBJECT_FORM)
            .next(MdClassPackage.Literals.TASK__DEFAULT_LIST_FORM)
            .next(MdClassPackage.Literals.TASK__DEFAULT_CHOICE_FORM)
            .next(MdClassPackage.Literals.TASK__AUXILIARY_OBJECT_FORM)
            .next(MdClassPackage.Literals.TASK__AUXILIARY_LIST_FORM)
            .next(MdClassPackage.Literals.TASK__AUXILIARY_CHOICE_FORM)
            .next(MdClassPackage.Literals.TASK__CHOICE_HISTORY_ON_INPUT)
            .build();
    }

    private List<EStructuralFeature> getTaskAttribute(Version version)
    {
        return (new ListBuilder(MdClassPackage.Literals.TASK_ATTRIBUTE, this::defaultPropertyFilter))
            .cursor(MdClassPackage.Literals.BASIC_FEATURE__MAX_VALUE)
            .next(MdClassPackage.Literals.DB_OBJECT_ATTRIBUTE__FILL_FROM_FILLING_VALUE)
            .next(MdClassPackage.Literals.DB_OBJECT_ATTRIBUTE__FILL_VALUE)
            .cursor(MdClassPackage.Literals.TASK_ATTRIBUTE__CHOICE_HISTORY_ON_INPUT)
            .next(MdClassPackage.Literals.DB_OBJECT_ATTRIBUTE__INDEXING)
            .next(MdClassPackage.Literals.DB_OBJECT_ATTRIBUTE__FULL_TEXT_SEARCH)
            .next(MdClassPackage.Literals.DATA_HISTORY_SUPPORT__DATA_HISTORY)
            .build();
    }

    private boolean isPropertyFeature(EStructuralFeature feature)
    {
        return feature.getEAnnotation("http://www.1c.ru/v8/dt/metadata/MdProperty") != null;
    }

    private boolean isNotTransientDerivedFeature(EStructuralFeature feature)
    {
        return !feature.isDerived() && !feature.isTransient();
    }

    private boolean defaultPropertyFilter(EStructuralFeature feature)
    {
        return isPropertyFeature(feature) && isNotTransientDerivedFeature(feature);
    }

    private static class ListBuilder
    {
        private List<EStructuralFeature> orderedFeaturesList;

        private int cursor = 0;

        ListBuilder(EClass eClass, Predicate<EStructuralFeature> filter)
        {
            this.orderedFeaturesList =
                eClass.getEAllStructuralFeatures().stream().filter(filter).collect(Collectors.toList());
        }

        public ListBuilder cursor(EStructuralFeature feature)
        {
            this.cursor = this.orderedFeaturesList.indexOf(feature);
            if (this.cursor == -1)
            {
                throw new IllegalArgumentException();
            }
            return this;
        }

        public ListBuilder next(EStructuralFeature feature)
        {
            this.cursor++;
            int oldIndex = this.orderedFeaturesList.indexOf(feature);
            if (oldIndex >= 0 && oldIndex < this.cursor)
            {
                this.cursor--;
            }
            this.orderedFeaturesList.remove(feature);
            if (this.cursor > this.orderedFeaturesList.size())
            {
                this.orderedFeaturesList.add(feature);
            }
            else
            {
                this.orderedFeaturesList.add(this.cursor, feature);
            }
            return this;
        }

        public List<EStructuralFeature> build()
        {
            return this.orderedFeaturesList;
        }
    }
}
