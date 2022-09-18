/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.yaml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com._1c.g5.v8.dt.core.platform.IConfigurationAware;
import com._1c.g5.v8.dt.core.platform.IV8Project;
import com._1c.g5.v8.dt.core.platform.IV8ProjectManager;
import com._1c.g5.v8.dt.form.model.FormPackage;
import com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SubObjectsManager
{
    private static final IVersionAwareReferecePredicate ANY_REFERECE = (v, r) -> true;

    @Inject
    private IV8ProjectManager v8ProjectManager;

    private final VersionAwareRefereceProvider versionAwareRefereceProvider = new VersionAwareRefereceProvider();

    private final ImmutableMap<EClass, ImmutableList<EReference>> subordinateObjectsMap;

    private final ImmutableMap<EClass, ImmutableList<EReference>> externalPropertiesMap;

    private final ImmutableMap<EClass, ImmutableList<EReference>> moduleReferenceMap;

    public SubObjectsManager()
    {
        SubObjectsReferenceComputer computer = new SubObjectsReferenceComputer();
        this.subordinateObjectsMap = computer.getSubordinateObjectsMap();
        this.externalPropertiesMap = computer.getExternalPropertiesMap();
        this.moduleReferenceMap = computer.getModuleReferenceMap();
    }

    public ImmutableList<EReference> getSubordinateObjectReferences(EClass owner)
    {
        ImmutableList.Builder<EReference> builder = ImmutableList.builder();
        if (this.subordinateObjectsMap.containsKey(owner))
        {
            builder.addAll(this.subordinateObjectsMap.get(owner));
        }
        if (this.moduleReferenceMap.containsKey(owner))
        {
            builder.addAll(this.moduleReferenceMap.get(owner));
        }
        if (this.externalPropertiesMap.containsKey(owner))
        {
            builder.addAll(this.externalPropertiesMap.get(owner));
        }
        return builder.build();
    }

    public List<Supplier<EObject>> getSubordinateObjects(EObject paramEObject, boolean subordinateObjects,
        boolean externalProperties, Version version)
    {
        List<EObject> subObjects = Lists.newArrayList();
        EClass eClass = paramEObject.eClass();
        IVersionAwareReferecePredicate refereceProvider =
            this.versionAwareRefereceProvider.getVersionAwareRefereceProvider(eClass);
        if (subordinateObjects)
        {
            subObjects.addAll(getSubordinateObjects(paramEObject, this.subordinateObjectsMap.get(eClass), version,
                refereceProvider, true));
        }
        if (externalProperties)
        {
            subObjects.addAll(getSubordinateObjects(paramEObject, this.externalPropertiesMap.get(eClass), version,
                refereceProvider, eClass != FormPackage.Literals.FORM));
            subObjects.addAll(getSubordinateObjects(paramEObject, this.moduleReferenceMap.get(eClass), version,
                refereceProvider, false));
        }
        return subObjects.stream().map(subObject -> wrap(subObject, paramEObject)).collect(Collectors.toList());
    }

    private List<EObject> getSubordinateObjects(EObject eObject, List<EReference> references, Version version,
        IVersionAwareReferecePredicate refereceProvider, boolean resolve)
    {
        List<EObject> list = new ArrayList<>();
        if (references != null)
        {
            for (EReference reference : references)
            {
                if (!(refereceProvider.test(version, reference)))
                {
                    continue;
                }

                Object value = eObject.eGet(reference, resolve);

                if (reference.isMany())
                {
                    list.addAll((Collection<? extends EObject>)value);
                }
                else if (value != null)
                {
                    list.add((EObject)value);
                }
            }
        }
        return list;
    }

    private Supplier<EObject> wrap(EObject eObject, EObject v8Project)
    {
        if (eObject instanceof com._1c.g5.v8.dt.form.model.Form && eObject.eIsProxy())
        {
            IV8Project project = this.v8ProjectManager.getProject(v8Project);
            if (project instanceof IConfigurationAware && ((IConfigurationAware)project).getConfiguration() != null)
            {
                return () -> EcoreUtil.resolve(eObject, ((IConfigurationAware)v8Project).getConfiguration());
            }
        }
        return () -> eObject;
    }

    private interface IVersionAwareReferecePredicate
        extends BiPredicate<Version, EReference>
    {
        @Override
        boolean test(Version param1Version, EReference param1EReference);
    }

    private static class SubObjectsReferenceComputer
    {
        private ImmutableMap.Builder<EClass, ImmutableList<EReference>> subordinateObjectsBuilder =
            ImmutableMap.builder();

        private ImmutableMap.Builder<EClass, ImmutableList<EReference>> externalPropertiesBuilder =
            ImmutableMap.builder();

        private ImmutableMap.Builder<EClass, ImmutableList<EReference>> moduleReferenceBuilder = ImmutableMap.builder();

        SubObjectsReferenceComputer()
        {
            basicFormsRefs();
            basicCommandsRefs();
            accountingRegisterRefs();
            accumulationRegisterRefs();
            businessProcessRefs();
            botRefs();
            calculationRegisterRefs();
            сatalogRefs();
            chartOfAccountsRefs();
            chartOfCalculationTypesRefs();
            chartOfCharacteristicTypesRefs();
            commonCommandRefs();
            commonModuleRefs();
            сonfigurationRefs();
            constantRefs();
            cubeRefs();
            dataProcessorRefs();
            externalDataProcessorRefs();
            externalDataSourceRefs();
            dimensionTableRefs();
            documentRefs();
            documentJournalRefs();
            enumRefs();
            exchangePlanRefs();
            filterCriterionRefs();
            httpServiceRefs();
            informationRegisterRefs();
            integrationServiceRefs();
            recalculationRefs();
            reportRefs();
            externalReportRefs();
            roleRefs();
            scheduledJobRefs();
            settingsStorageRefs();
            sequenceRefs();
            subsystemRefs();
            tableRefs();
            taskRefs();
            webServiceRefs();
            xdtoPackageRefs();
            styleRefs();
            formRefs();
        }

        public ImmutableMap<EClass, ImmutableList<EReference>> getExternalPropertiesMap()
        {
            return this.externalPropertiesBuilder.build();
        }

        public ImmutableMap<EClass, ImmutableList<EReference>> getModuleReferenceMap()
        {
            return this.moduleReferenceBuilder.build();
        }

        public ImmutableMap<EClass, ImmutableList<EReference>> getSubordinateObjectsMap()
        {
            return this.subordinateObjectsBuilder.build();
        }

        private void сatalogRefs()
        {
            addToBuilder(this.subordinateObjectsBuilder, MdClassPackage.Literals.CATALOG,
                new EReference[] { MdClassPackage.Literals.CATALOG__FORMS, MdClassPackage.Literals.CATALOG__COMMANDS,
                    MdClassPackage.Literals.CATALOG__TEMPLATES });
            addToBuilder(this.externalPropertiesBuilder, MdClassPackage.Literals.CATALOG, new EReference[] {
                MdClassPackage.Literals.CATALOG__PREDEFINED, MdClassPackage.Literals.BASIC_DB_OBJECT__HELP });
            addToBuilder(this.moduleReferenceBuilder, MdClassPackage.Literals.CATALOG,
                new EReference[] { MdClassPackage.Literals.BASIC_DB_OBJECT__OBJECT_MODULE,
                    MdClassPackage.Literals.BASIC_DB_OBJECT__MANAGER_MODULE });
        }

        private void сonfigurationRefs()
        {
            addToBuilder(this.subordinateObjectsBuilder, MdClassPackage.Literals.CONFIGURATION, new EReference[] {
                MdClassPackage.Literals.CONFIGURATION__LANGUAGES, MdClassPackage.Literals.CONFIGURATION__SUBSYSTEMS,
                MdClassPackage.Literals.CONFIGURATION__STYLE_ITEMS, MdClassPackage.Literals.CONFIGURATION__STYLES,
                MdClassPackage.Literals.CONFIGURATION__COMMON_PICTURES,
                MdClassPackage.Literals.CONFIGURATION__INTERFACES,
                MdClassPackage.Literals.CONFIGURATION__SESSION_PARAMETERS, MdClassPackage.Literals.CONFIGURATION__ROLES,
                MdClassPackage.Literals.CONFIGURATION__COMMON_TEMPLATES,
                MdClassPackage.Literals.CONFIGURATION__FILTER_CRITERIA,
                MdClassPackage.Literals.CONFIGURATION__COMMON_MODULES,
                MdClassPackage.Literals.CONFIGURATION__COMMON_ATTRIBUTES,
                MdClassPackage.Literals.CONFIGURATION__EXCHANGE_PLANS,
                MdClassPackage.Literals.CONFIGURATION__XDTO_PACKAGES,
                MdClassPackage.Literals.CONFIGURATION__WEB_SERVICES,
                MdClassPackage.Literals.CONFIGURATION__HTTP_SERVICES,
                MdClassPackage.Literals.CONFIGURATION__WS_REFERENCES,
                MdClassPackage.Literals.CONFIGURATION__EVENT_SUBSCRIPTIONS,
                MdClassPackage.Literals.CONFIGURATION__SCHEDULED_JOBS,
                MdClassPackage.Literals.CONFIGURATION__SETTINGS_STORAGES,
                MdClassPackage.Literals.CONFIGURATION__FUNCTIONAL_OPTIONS,
                MdClassPackage.Literals.CONFIGURATION__FUNCTIONAL_OPTIONS_PARAMETERS,
                MdClassPackage.Literals.CONFIGURATION__DEFINED_TYPES,
                MdClassPackage.Literals.CONFIGURATION__COMMON_COMMANDS,
                MdClassPackage.Literals.CONFIGURATION__COMMAND_GROUPS, MdClassPackage.Literals.CONFIGURATION__CONSTANTS,
                MdClassPackage.Literals.CONFIGURATION__COMMON_FORMS, MdClassPackage.Literals.CONFIGURATION__CATALOGS,
                MdClassPackage.Literals.CONFIGURATION__DOCUMENTS,
                MdClassPackage.Literals.CONFIGURATION__DOCUMENT_NUMERATORS,
                MdClassPackage.Literals.CONFIGURATION__SEQUENCES,
                MdClassPackage.Literals.CONFIGURATION__DOCUMENT_JOURNALS, MdClassPackage.Literals.CONFIGURATION__ENUMS,
                MdClassPackage.Literals.CONFIGURATION__REPORTS, MdClassPackage.Literals.CONFIGURATION__DATA_PROCESSORS,
                MdClassPackage.Literals.CONFIGURATION__INFORMATION_REGISTERS,
                MdClassPackage.Literals.CONFIGURATION__ACCUMULATION_REGISTERS,
                MdClassPackage.Literals.CONFIGURATION__CHARTS_OF_CHARACTERISTIC_TYPES,
                MdClassPackage.Literals.CONFIGURATION__CHARTS_OF_ACCOUNTS,
                MdClassPackage.Literals.CONFIGURATION__ACCOUNTING_REGISTERS,
                MdClassPackage.Literals.CONFIGURATION__CHARTS_OF_CALCULATION_TYPES,
                MdClassPackage.Literals.CONFIGURATION__CALCULATION_REGISTERS,
                MdClassPackage.Literals.CONFIGURATION__BUSINESS_PROCESSES, MdClassPackage.Literals.CONFIGURATION__TASKS,
                MdClassPackage.Literals.CONFIGURATION__EXTERNAL_DATA_SOURCES,
                MdClassPackage.Literals.CONFIGURATION__INTEGRATION_SERVICES,
                MdClassPackage.Literals.CONFIGURATION__BOTS });
            addToBuilder(this.externalPropertiesBuilder, MdClassPackage.Literals.CONFIGURATION,
                new EReference[] { MdClassPackage.Literals.CONFIGURATION__PARENT_CONFIGURATIONS,
                    MdClassPackage.Literals.CONFIGURATION__COMMAND_INTERFACE,
                    MdClassPackage.Literals.CONFIGURATION__START_PAGE_WORKING_AREA,
                    MdClassPackage.Literals.CONFIGURATION__HOME_PAGE_WORK_AREA,
                    MdClassPackage.Literals.CONFIGURATION__MAIN_SECTION_COMMAND_INTERFACE,
                    MdClassPackage.Literals.CONFIGURATION__HELP,
                    MdClassPackage.Literals.CONFIGURATION__MAIN_SECTION_PICTURE,
                    MdClassPackage.Literals.CONFIGURATION__LOGO, MdClassPackage.Literals.CONFIGURATION__SPLASH,
                    MdClassPackage.Literals.CONFIGURATION__CLIENT_APPLICATION_INTERFACE,
                    MdClassPackage.Literals.CONFIGURATION__MOBILE_APPLICATION_CONTENT });
            addToBuilder(this.moduleReferenceBuilder, MdClassPackage.Literals.CONFIGURATION,
                new EReference[] { MdClassPackage.Literals.CONFIGURATION__MANAGED_APPLICATION_MODULE,
                    MdClassPackage.Literals.CONFIGURATION__SESSION_MODULE,
                    MdClassPackage.Literals.CONFIGURATION__EXTERNAL_CONNECTION_MODULE,
                    MdClassPackage.Literals.CONFIGURATION__ORDINARY_APPLICATION_MODULE });
        }

        private void accountingRegisterRefs()
        {
            addToBuilder(this.subordinateObjectsBuilder, MdClassPackage.Literals.ACCOUNTING_REGISTER,
                new EReference[] { MdClassPackage.Literals.ACCOUNTING_REGISTER__FORMS,
                    MdClassPackage.Literals.ACCOUNTING_REGISTER__COMMANDS,
                    MdClassPackage.Literals.ACCOUNTING_REGISTER__TEMPLATES });
            addToBuilder(this.externalPropertiesBuilder, MdClassPackage.Literals.ACCOUNTING_REGISTER,
                new EReference[] { MdClassPackage.Literals.ACCOUNTING_REGISTER__HELP });
            addToBuilder(this.moduleReferenceBuilder, MdClassPackage.Literals.ACCOUNTING_REGISTER,
                new EReference[] { MdClassPackage.Literals.ACCOUNTING_REGISTER__RECORD_SET_MODULE,
                    MdClassPackage.Literals.ACCOUNTING_REGISTER__MANAGER_MODULE });
        }

        private void accumulationRegisterRefs()
        {
            addToBuilder(this.subordinateObjectsBuilder, MdClassPackage.Literals.ACCUMULATION_REGISTER,
                new EReference[] { MdClassPackage.Literals.ACCUMULATION_REGISTER__FORMS,
                    MdClassPackage.Literals.ACCUMULATION_REGISTER__COMMANDS,
                    MdClassPackage.Literals.ACCUMULATION_REGISTER__TEMPLATES });
            addToBuilder(this.externalPropertiesBuilder, MdClassPackage.Literals.ACCUMULATION_REGISTER,
                new EReference[] { MdClassPackage.Literals.ACCUMULATION_REGISTER__HELP,
                    MdClassPackage.Literals.ACCUMULATION_REGISTER__AGGREGATES });
            addToBuilder(this.moduleReferenceBuilder, MdClassPackage.Literals.ACCUMULATION_REGISTER,
                new EReference[] { MdClassPackage.Literals.ACCUMULATION_REGISTER__RECORD_SET_MODULE,
                    MdClassPackage.Literals.ACCUMULATION_REGISTER__MANAGER_MODULE });
        }

        private void addToBuilder(ImmutableMap.Builder<EClass, ImmutableList<EReference>> builder, EClass eClass,
            EReference... eReferences)
        {
            builder.put(eClass, ImmutableList.copyOf(eReferences));
        }

        private void addToBuilder(ImmutableMap.Builder<EClass, ImmutableList<EReference>> builder,
            ImmutableList<EReference> references, EClass... eClasses)
        {
            byte b;
            int i;
            EClass[] arrayOfEClass = eClasses;
            for (i = arrayOfEClass.length, b = 0; b < i;)
            {
                EClass eClass = arrayOfEClass[b];
                builder.put(eClass, references);
                b++;
            }
        }

        private void basicCommandsRefs()
        {
            addToBuilder(this.moduleReferenceBuilder,
                ImmutableList.of(MdClassPackage.Literals.BASIC_COMMAND__COMMAND_MODULE),
                new EClass[] { MdClassPackage.Literals.ACCOUNTING_REGISTER_COMMAND,
                    MdClassPackage.Literals.ACCUMULATION_REGISTER_COMMAND,
                    MdClassPackage.Literals.BUSINESS_PROCESS_COMMAND,
                    MdClassPackage.Literals.CALCULATION_REGISTER_COMMAND, MdClassPackage.Literals.CATALOG_COMMAND,
                    MdClassPackage.Literals.CHART_OF_ACCOUNTS_COMMAND,
                    MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES_COMMAND,
                    MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES_COMMAND,
                    MdClassPackage.Literals.DATA_PROCESSOR_COMMAND, MdClassPackage.Literals.DOCUMENT_COMMAND,
                    MdClassPackage.Literals.DOCUMENT_JOURNAL_COMMAND, MdClassPackage.Literals.ENUM_COMMAND,
                    MdClassPackage.Literals.EXCHANGE_PLAN_COMMAND, MdClassPackage.Literals.FILTER_CRITERION_COMMAND,
                    MdClassPackage.Literals.INFORMATION_REGISTER_COMMAND, MdClassPackage.Literals.REPORT_COMMAND,
                    MdClassPackage.Literals.TASK_COMMAND, MdClassPackage.Literals.TABLE_COMMAND,
                    MdClassPackage.Literals.DIMENSION_TABLE_COMMAND, MdClassPackage.Literals.CUBE_COMMAND });
        }

        private void basicFormsRefs()
        {
            addToBuilder(this.externalPropertiesBuilder,
                ImmutableList.of(MdClassPackage.Literals.BASIC_FORM__FORM, MdClassPackage.Literals.BASIC_FORM__HELP),
                new EClass[] { MdClassPackage.Literals.ACCOUNTING_REGISTER_FORM,
                    MdClassPackage.Literals.ACCUMULATION_REGISTER_FORM, MdClassPackage.Literals.BUSINESS_PROCESS_FORM,
                    MdClassPackage.Literals.CALCULATION_REGISTER_FORM, MdClassPackage.Literals.CATALOG_FORM,
                    MdClassPackage.Literals.CHART_OF_ACCOUNTS_FORM,
                    MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES_FORM,
                    MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES_FORM, MdClassPackage.Literals.COMMON_FORM,
                    MdClassPackage.Literals.DATA_PROCESSOR_FORM, MdClassPackage.Literals.DOCUMENT_FORM,
                    MdClassPackage.Literals.DOCUMENT_JOURNAL_FORM, MdClassPackage.Literals.ENUM_FORM,
                    MdClassPackage.Literals.EXCHANGE_PLAN_FORM, MdClassPackage.Literals.FILTER_CRITERION_FORM,
                    MdClassPackage.Literals.INFORMATION_REGISTER_FORM, MdClassPackage.Literals.REPORT_FORM,
                    MdClassPackage.Literals.SETTINGS_STORAGE_FORM, MdClassPackage.Literals.TASK_FORM,
                    MdClassPackage.Literals.TABLE_FORM, MdClassPackage.Literals.DIMENSION_TABLE_FORM,
                    MdClassPackage.Literals.CUBE_FORM });
        }

        private void botRefs()
        {
            addToBuilder(this.moduleReferenceBuilder, MdClassPackage.Literals.BOT,
                new EReference[] { MdClassPackage.Literals.BOT__MODULE });
        }

        private void businessProcessRefs()
        {
            addToBuilder(this.subordinateObjectsBuilder, MdClassPackage.Literals.BUSINESS_PROCESS,
                new EReference[] { MdClassPackage.Literals.BUSINESS_PROCESS__FORMS,
                    MdClassPackage.Literals.BUSINESS_PROCESS__COMMANDS,
                    MdClassPackage.Literals.BUSINESS_PROCESS__TEMPLATES });
            addToBuilder(this.externalPropertiesBuilder, MdClassPackage.Literals.BUSINESS_PROCESS, new EReference[] {
                MdClassPackage.Literals.BUSINESS_PROCESS__FLOWCHART, MdClassPackage.Literals.BASIC_DB_OBJECT__HELP });
            addToBuilder(this.moduleReferenceBuilder, MdClassPackage.Literals.BUSINESS_PROCESS,
                new EReference[] { MdClassPackage.Literals.BASIC_DB_OBJECT__MANAGER_MODULE,
                    MdClassPackage.Literals.BASIC_DB_OBJECT__OBJECT_MODULE });
        }

        private void calculationRegisterRefs()
        {
            addToBuilder(this.subordinateObjectsBuilder, MdClassPackage.Literals.CALCULATION_REGISTER,
                new EReference[] { MdClassPackage.Literals.CALCULATION_REGISTER__FORMS,
                    MdClassPackage.Literals.CALCULATION_REGISTER__COMMANDS,
                    MdClassPackage.Literals.CALCULATION_REGISTER__TEMPLATES,
                    MdClassPackage.Literals.CALCULATION_REGISTER__RECALCULATIONS });
            addToBuilder(this.externalPropertiesBuilder, MdClassPackage.Literals.CALCULATION_REGISTER,
                new EReference[] { MdClassPackage.Literals.CALCULATION_REGISTER__HELP });
            addToBuilder(this.moduleReferenceBuilder, MdClassPackage.Literals.CALCULATION_REGISTER,
                new EReference[] { MdClassPackage.Literals.CALCULATION_REGISTER__RECORD_SET_MODULE,
                    MdClassPackage.Literals.CALCULATION_REGISTER__MANAGER_MODULE });
        }

        private void chartOfAccountsRefs()
        {
            addToBuilder(this.subordinateObjectsBuilder, MdClassPackage.Literals.CHART_OF_ACCOUNTS,
                new EReference[] { MdClassPackage.Literals.CHART_OF_ACCOUNTS__FORMS,
                    MdClassPackage.Literals.CHART_OF_ACCOUNTS__COMMANDS,
                    MdClassPackage.Literals.CHART_OF_ACCOUNTS__TEMPLATES });
            addToBuilder(this.externalPropertiesBuilder, MdClassPackage.Literals.CHART_OF_ACCOUNTS, new EReference[] {
                MdClassPackage.Literals.BASIC_DB_OBJECT__HELP, MdClassPackage.Literals.CHART_OF_ACCOUNTS__PREDEFINED });
            addToBuilder(this.moduleReferenceBuilder, MdClassPackage.Literals.CHART_OF_ACCOUNTS,
                new EReference[] { MdClassPackage.Literals.BASIC_DB_OBJECT__OBJECT_MODULE,
                    MdClassPackage.Literals.BASIC_DB_OBJECT__MANAGER_MODULE });
        }

        private void chartOfCalculationTypesRefs()
        {
            addToBuilder(this.subordinateObjectsBuilder, MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES,
                new EReference[] { MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES__FORMS,
                    MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES__COMMANDS,
                    MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES__TEMPLATES });
            addToBuilder(this.externalPropertiesBuilder, MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES,
                new EReference[] { MdClassPackage.Literals.BASIC_DB_OBJECT__HELP,
                    MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES__PREDEFINED });
            addToBuilder(this.moduleReferenceBuilder, MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES,
                new EReference[] { MdClassPackage.Literals.BASIC_DB_OBJECT__OBJECT_MODULE,
                    MdClassPackage.Literals.BASIC_DB_OBJECT__MANAGER_MODULE });
        }

        private void chartOfCharacteristicTypesRefs()
        {
            addToBuilder(this.subordinateObjectsBuilder, MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES,
                new EReference[] { MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES__FORMS,
                    MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES__COMMANDS,
                    MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES__TEMPLATES });
            addToBuilder(this.externalPropertiesBuilder, MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES,
                new EReference[] { MdClassPackage.Literals.BASIC_DB_OBJECT__HELP,
                    MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES__PREDEFINED });
            addToBuilder(this.moduleReferenceBuilder, MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES,
                new EReference[] { MdClassPackage.Literals.BASIC_DB_OBJECT__OBJECT_MODULE,
                    MdClassPackage.Literals.BASIC_DB_OBJECT__MANAGER_MODULE });
        }

        private void commonCommandRefs()
        {
            addToBuilder(this.moduleReferenceBuilder, MdClassPackage.Literals.COMMON_COMMAND,
                new EReference[] { MdClassPackage.Literals.BASIC_COMMAND__COMMAND_MODULE });
            addToBuilder(this.externalPropertiesBuilder, MdClassPackage.Literals.COMMON_COMMAND,
                new EReference[] { MdClassPackage.Literals.COMMON_COMMAND__HELP });
        }

        private void commonModuleRefs()
        {
            addToBuilder(this.moduleReferenceBuilder, MdClassPackage.Literals.COMMON_MODULE,
                new EReference[] { MdClassPackage.Literals.COMMON_MODULE__MODULE });
        }

        private void constantRefs()
        {
            addToBuilder(this.moduleReferenceBuilder, MdClassPackage.Literals.CONSTANT,
                new EReference[] { MdClassPackage.Literals.CONSTANT__VALUE_MANAGER_MODULE,
                    MdClassPackage.Literals.CONSTANT__MANAGER_MODULE });
        }

        private void cubeRefs()
        {
            addToBuilder(this.subordinateObjectsBuilder, MdClassPackage.Literals.CUBE,
                new EReference[] { MdClassPackage.Literals.CUBE__DIMENSION_TABLES, MdClassPackage.Literals.CUBE__FORMS,
                    MdClassPackage.Literals.CUBE__COMMANDS, MdClassPackage.Literals.CUBE__TEMPLATES });
            addToBuilder(this.externalPropertiesBuilder, MdClassPackage.Literals.CUBE,
                new EReference[] { MdClassPackage.Literals.CUBE__HELP });
            addToBuilder(this.moduleReferenceBuilder, MdClassPackage.Literals.CUBE, new EReference[] {
                MdClassPackage.Literals.CUBE__RECORD_SET_MODULE, MdClassPackage.Literals.CUBE__MANAGER_MODULE });
        }

        private void dataProcessorRefs()
        {
            addToBuilder(this.subordinateObjectsBuilder, MdClassPackage.Literals.DATA_PROCESSOR,
                new EReference[] { MdClassPackage.Literals.DATA_PROCESSOR__FORMS,
                    MdClassPackage.Literals.DATA_PROCESSOR__COMMANDS,
                    MdClassPackage.Literals.DATA_PROCESSOR__TEMPLATES });
            addToBuilder(this.externalPropertiesBuilder, MdClassPackage.Literals.DATA_PROCESSOR,
                new EReference[] { MdClassPackage.Literals.DATA_PROCESSOR__HELP });
            addToBuilder(this.moduleReferenceBuilder, MdClassPackage.Literals.DATA_PROCESSOR,
                new EReference[] { MdClassPackage.Literals.DATA_PROCESSOR__OBJECT_MODULE,
                    MdClassPackage.Literals.DATA_PROCESSOR__MANAGER_MODULE });
        }

        private void dimensionTableRefs()
        {
            addToBuilder(this.subordinateObjectsBuilder, MdClassPackage.Literals.DIMENSION_TABLE,
                new EReference[] { MdClassPackage.Literals.DIMENSION_TABLE__FORMS,
                    MdClassPackage.Literals.DIMENSION_TABLE__COMMANDS,
                    MdClassPackage.Literals.DIMENSION_TABLE__TEMPLATES });
            addToBuilder(this.externalPropertiesBuilder, MdClassPackage.Literals.DIMENSION_TABLE,
                new EReference[] { MdClassPackage.Literals.DIMENSION_TABLE__HELP });
            addToBuilder(this.moduleReferenceBuilder, MdClassPackage.Literals.DIMENSION_TABLE,
                new EReference[] { MdClassPackage.Literals.DIMENSION_TABLE__OBJECT_MODULE,
                    MdClassPackage.Literals.DIMENSION_TABLE__MANAGER_MODULE });
        }

        private void documentJournalRefs()
        {
            addToBuilder(this.subordinateObjectsBuilder, MdClassPackage.Literals.DOCUMENT_JOURNAL,
                new EReference[] { MdClassPackage.Literals.DOCUMENT_JOURNAL__FORMS,
                    MdClassPackage.Literals.DOCUMENT_JOURNAL__COMMANDS,
                    MdClassPackage.Literals.DOCUMENT_JOURNAL__TEMPLATES });
            addToBuilder(this.externalPropertiesBuilder, MdClassPackage.Literals.DOCUMENT_JOURNAL,
                new EReference[] { MdClassPackage.Literals.DOCUMENT_JOURNAL__HELP });
            addToBuilder(this.moduleReferenceBuilder, MdClassPackage.Literals.DOCUMENT_JOURNAL,
                new EReference[] { MdClassPackage.Literals.DOCUMENT_JOURNAL__MANAGER_MODULE });
        }

        private void documentRefs()
        {
            addToBuilder(this.subordinateObjectsBuilder, MdClassPackage.Literals.DOCUMENT,
                new EReference[] { MdClassPackage.Literals.DOCUMENT__FORMS, MdClassPackage.Literals.DOCUMENT__COMMANDS,
                    MdClassPackage.Literals.DOCUMENT__TEMPLATES });
            addToBuilder(this.externalPropertiesBuilder, MdClassPackage.Literals.DOCUMENT,
                new EReference[] { MdClassPackage.Literals.BASIC_DB_OBJECT__HELP });
            addToBuilder(this.moduleReferenceBuilder, MdClassPackage.Literals.DOCUMENT,
                new EReference[] { MdClassPackage.Literals.BASIC_DB_OBJECT__OBJECT_MODULE,
                    MdClassPackage.Literals.BASIC_DB_OBJECT__MANAGER_MODULE });
        }

        private void enumRefs()
        {
            addToBuilder(this.subordinateObjectsBuilder, MdClassPackage.Literals.ENUM,
                new EReference[] { MdClassPackage.Literals.ENUM__FORMS, MdClassPackage.Literals.ENUM__COMMANDS,
                    MdClassPackage.Literals.ENUM__TEMPLATES });
            addToBuilder(this.moduleReferenceBuilder, MdClassPackage.Literals.ENUM,
                new EReference[] { MdClassPackage.Literals.ENUM__MANAGER_MODULE });
        }

        private void exchangePlanRefs()
        {
            addToBuilder(this.subordinateObjectsBuilder, MdClassPackage.Literals.EXCHANGE_PLAN,
                new EReference[] { MdClassPackage.Literals.EXCHANGE_PLAN__FORMS,
                    MdClassPackage.Literals.EXCHANGE_PLAN__COMMANDS,
                    MdClassPackage.Literals.EXCHANGE_PLAN__TEMPLATES });
            addToBuilder(this.externalPropertiesBuilder, MdClassPackage.Literals.EXCHANGE_PLAN,
                new EReference[] { MdClassPackage.Literals.BASIC_DB_OBJECT__HELP });
            addToBuilder(this.moduleReferenceBuilder, MdClassPackage.Literals.EXCHANGE_PLAN,
                new EReference[] { MdClassPackage.Literals.BASIC_DB_OBJECT__OBJECT_MODULE,
                    MdClassPackage.Literals.BASIC_DB_OBJECT__MANAGER_MODULE });
        }

        private void externalDataProcessorRefs()
        {
            addToBuilder(this.subordinateObjectsBuilder, MdClassPackage.Literals.EXTERNAL_DATA_PROCESSOR,
                new EReference[] { MdClassPackage.Literals.EXTERNAL_DATA_PROCESSOR__FORMS,
                    MdClassPackage.Literals.EXTERNAL_DATA_PROCESSOR__TEMPLATES });
            addToBuilder(this.externalPropertiesBuilder, MdClassPackage.Literals.EXTERNAL_DATA_PROCESSOR,
                new EReference[] { MdClassPackage.Literals.EXTERNAL_DATA_PROCESSOR__HELP });
            addToBuilder(this.moduleReferenceBuilder, MdClassPackage.Literals.EXTERNAL_DATA_PROCESSOR,
                new EReference[] { MdClassPackage.Literals.EXTERNAL_DATA_PROCESSOR__OBJECT_MODULE });
        }

        private void externalDataSourceRefs()
        {
            addToBuilder(this.subordinateObjectsBuilder, MdClassPackage.Literals.EXTERNAL_DATA_SOURCE,
                new EReference[] { MdClassPackage.Literals.EXTERNAL_DATA_SOURCE__TABLES,
                    MdClassPackage.Literals.EXTERNAL_DATA_SOURCE__CUBES });
        }

        private void externalReportRefs()
        {
            addToBuilder(this.subordinateObjectsBuilder, MdClassPackage.Literals.EXTERNAL_REPORT, new EReference[] {
                MdClassPackage.Literals.EXTERNAL_REPORT__FORMS, MdClassPackage.Literals.EXTERNAL_REPORT__TEMPLATES });
            addToBuilder(this.externalPropertiesBuilder, MdClassPackage.Literals.EXTERNAL_REPORT,
                new EReference[] { MdClassPackage.Literals.EXTERNAL_REPORT__HELP });
            addToBuilder(this.moduleReferenceBuilder, MdClassPackage.Literals.EXTERNAL_REPORT,
                new EReference[] { MdClassPackage.Literals.EXTERNAL_REPORT__OBJECT_MODULE });
        }

        private void filterCriterionRefs()
        {
            addToBuilder(this.subordinateObjectsBuilder, MdClassPackage.Literals.FILTER_CRITERION, new EReference[] {
                MdClassPackage.Literals.FILTER_CRITERION__FORMS, MdClassPackage.Literals.FILTER_CRITERION__COMMANDS });
            addToBuilder(this.moduleReferenceBuilder, MdClassPackage.Literals.FILTER_CRITERION,
                new EReference[] { MdClassPackage.Literals.FILTER_CRITERION__MANAGER_MODULE });
        }

        private void formRefs()
        {
            addToBuilder(this.moduleReferenceBuilder, FormPackage.Literals.FORM,
                new EReference[] { MdClassPackage.Literals.ABSTRACT_FORM__MODULE });
        }

        private void httpServiceRefs()
        {
            addToBuilder(this.moduleReferenceBuilder, MdClassPackage.Literals.HTTP_SERVICE,
                new EReference[] { MdClassPackage.Literals.HTTP_SERVICE__MODULE });
        }

        private void informationRegisterRefs()
        {
            addToBuilder(this.subordinateObjectsBuilder, MdClassPackage.Literals.INFORMATION_REGISTER,
                new EReference[] { MdClassPackage.Literals.INFORMATION_REGISTER__FORMS,
                    MdClassPackage.Literals.INFORMATION_REGISTER__COMMANDS,
                    MdClassPackage.Literals.INFORMATION_REGISTER__TEMPLATES });
            addToBuilder(this.externalPropertiesBuilder, MdClassPackage.Literals.INFORMATION_REGISTER,
                new EReference[] { MdClassPackage.Literals.INFORMATION_REGISTER__HELP });
            addToBuilder(this.moduleReferenceBuilder, MdClassPackage.Literals.INFORMATION_REGISTER,
                new EReference[] { MdClassPackage.Literals.INFORMATION_REGISTER__RECORD_SET_MODULE,
                    MdClassPackage.Literals.INFORMATION_REGISTER__MANAGER_MODULE });
        }

        private void integrationServiceRefs()
        {
            addToBuilder(this.moduleReferenceBuilder, MdClassPackage.Literals.INTEGRATION_SERVICE,
                new EReference[] { MdClassPackage.Literals.INTEGRATION_SERVICE__MODULE });
        }

        private void recalculationRefs()
        {
            addToBuilder(this.moduleReferenceBuilder, MdClassPackage.Literals.RECALCULATION,
                new EReference[] { MdClassPackage.Literals.RECALCULATION__RECORD_SET_MODULE });
        }

        private void reportRefs()
        {
            addToBuilder(this.subordinateObjectsBuilder, MdClassPackage.Literals.REPORT,
                new EReference[] { MdClassPackage.Literals.REPORT__FORMS, MdClassPackage.Literals.REPORT__COMMANDS,
                    MdClassPackage.Literals.REPORT__TEMPLATES });
            addToBuilder(this.externalPropertiesBuilder, MdClassPackage.Literals.REPORT,
                new EReference[] { MdClassPackage.Literals.REPORT__HELP });
            addToBuilder(this.moduleReferenceBuilder, MdClassPackage.Literals.REPORT, new EReference[] {
                MdClassPackage.Literals.REPORT__OBJECT_MODULE, MdClassPackage.Literals.REPORT__MANAGER_MODULE });
        }

        private void roleRefs()
        {
            addToBuilder(this.externalPropertiesBuilder, MdClassPackage.Literals.ROLE,
                new EReference[] { MdClassPackage.Literals.ROLE__RIGHTS });
        }

        private void scheduledJobRefs()
        {
            addToBuilder(this.externalPropertiesBuilder, MdClassPackage.Literals.SCHEDULED_JOB,
                new EReference[] { MdClassPackage.Literals.SCHEDULED_JOB__SCHEDULE });
        }

        private void sequenceRefs()
        {
            addToBuilder(this.moduleReferenceBuilder, MdClassPackage.Literals.SEQUENCE,
                new EReference[] { MdClassPackage.Literals.SEQUENCE__RECORD_SET_MODULE });
        }

        private void settingsStorageRefs()
        {
            addToBuilder(this.subordinateObjectsBuilder, MdClassPackage.Literals.SETTINGS_STORAGE, new EReference[] {
                MdClassPackage.Literals.SETTINGS_STORAGE__FORMS, MdClassPackage.Literals.SETTINGS_STORAGE__TEMPLATES });
            addToBuilder(this.moduleReferenceBuilder, MdClassPackage.Literals.SETTINGS_STORAGE,
                new EReference[] { MdClassPackage.Literals.SETTINGS_STORAGE__MANAGER_MODULE });
        }

        private void styleRefs()
        {
            addToBuilder(this.externalPropertiesBuilder, MdClassPackage.Literals.STYLE,
                new EReference[] { MdClassPackage.Literals.STYLE__STYLE });
        }

        private void subsystemRefs()
        {
            addToBuilder(this.subordinateObjectsBuilder, MdClassPackage.Literals.SUBSYSTEM,
                new EReference[] { MdClassPackage.Literals.SUBSYSTEM__SUBSYSTEMS });
            addToBuilder(this.externalPropertiesBuilder, MdClassPackage.Literals.SUBSYSTEM, new EReference[] {
                MdClassPackage.Literals.SUBSYSTEM__HELP, MdClassPackage.Literals.SUBSYSTEM__COMMAND_INTERFACE });
        }

        private void tableRefs()
        {
            addToBuilder(this.subordinateObjectsBuilder, MdClassPackage.Literals.TABLE,
                new EReference[] { MdClassPackage.Literals.TABLE__FORMS, MdClassPackage.Literals.TABLE__COMMANDS,
                    MdClassPackage.Literals.TABLE__TEMPLATES });
            addToBuilder(this.externalPropertiesBuilder, MdClassPackage.Literals.TABLE,
                new EReference[] { MdClassPackage.Literals.TABLE__HELP });
            addToBuilder(this.moduleReferenceBuilder, MdClassPackage.Literals.TABLE,
                new EReference[] { MdClassPackage.Literals.TABLE__RECORD_SET_MODULE,
                    MdClassPackage.Literals.TABLE__OBJECT_MODULE, MdClassPackage.Literals.TABLE__MANAGER_MODULE });
        }

        private void taskRefs()
        {
            addToBuilder(this.subordinateObjectsBuilder, MdClassPackage.Literals.TASK,
                new EReference[] { MdClassPackage.Literals.TASK__FORMS, MdClassPackage.Literals.TASK__COMMANDS,
                    MdClassPackage.Literals.TASK__TEMPLATES });
            addToBuilder(this.externalPropertiesBuilder, MdClassPackage.Literals.TASK,
                new EReference[] { MdClassPackage.Literals.BASIC_DB_OBJECT__HELP });
            addToBuilder(this.moduleReferenceBuilder, MdClassPackage.Literals.TASK,
                new EReference[] { MdClassPackage.Literals.BASIC_DB_OBJECT__OBJECT_MODULE,
                    MdClassPackage.Literals.BASIC_DB_OBJECT__MANAGER_MODULE });
        }

        private void webServiceRefs()
        {
            addToBuilder(this.moduleReferenceBuilder, MdClassPackage.Literals.WEB_SERVICE,
                new EReference[] { MdClassPackage.Literals.WEB_SERVICE__MODULE });
        }

        private void xdtoPackageRefs()
        {
            addToBuilder(this.externalPropertiesBuilder, MdClassPackage.Literals.XDTO_PACKAGE,
                new EReference[] { MdClassPackage.Literals.XDTO_PACKAGE__PACKAGE });
        }
    }

    private static class VersionAwareRefereceProvider
    {
        private final ImmutableMap<EClass, IVersionAwareReferecePredicate> versionAwareReferecePredicates =
            fillVersionAwareRefereceProviders();

        public SubObjectsManager.IVersionAwareReferecePredicate getVersionAwareRefereceProvider(EClass eClass)
        {
            SubObjectsManager.IVersionAwareReferecePredicate provider = this.versionAwareReferecePredicates.get(eClass);
            return (provider == null) ? SubObjectsManager.ANY_REFERECE : provider;
        }

        private ImmutableMap<EClass, IVersionAwareReferecePredicate> fillVersionAwareRefereceProviders()
        {
            ImmutableMap.Builder<EClass, SubObjectsManager.IVersionAwareReferecePredicate> builder =
                ImmutableMap.builder();
            builder.put(MdClassPackage.Literals.CONFIGURATION,
                (version, reference) -> (reference == MdClassPackage.Literals.CONFIGURATION__MOBILE_APPLICATION_CONTENT)
                    ? version.isGreaterThan(Version.V8_3_15) : (

                    !(reference == MdClassPackage.Literals.CONFIGURATION__HOME_PAGE_WORK_AREA
                        && version.compareTo(Version.V8_3_9) < 0
                        || reference == MdClassPackage.Literals.CONFIGURATION__START_PAGE_WORKING_AREA
                            && version.compareTo(Version.V8_3_9) >= 0)));
            return builder.build();
        }
    }
}
