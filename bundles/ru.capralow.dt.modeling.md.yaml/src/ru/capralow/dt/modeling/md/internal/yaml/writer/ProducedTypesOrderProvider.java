/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.internal.yaml.writer;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EReference;

import com._1c.g5.v8.dt.metadata.mdtype.MdTypePackage;
import com._1c.g5.v8.dt.metadata.mdtype.MdTypes;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.md.yaml.writer.IProducedTypesOrderProvider;

@Singleton
public class ProducedTypesOrderProvider
    implements IProducedTypesOrderProvider
{
    private static final Map<EClass, List<EReference>> ORDER_MAP = new ImmutableMap.Builder<EClass, List<EReference>>()
        .put(MdTypePackage.Literals.ACCOUNTING_REGISTER_TYPES,
            Lists.newArrayList(new EReference[] { MdTypePackage.Literals.ACCOUNTING_REGISTER_TYPES__RECORD_TYPE,
                MdTypePackage.Literals.ACCOUNTING_REGISTER_TYPES__EXT_DIMENSIONS_TYPE,
                MdTypePackage.Literals.BASIC_REGISTER_TYPES__RECORD_SET_TYPE,
                MdTypePackage.Literals.BASIC_REGISTER_TYPES__RECORD_KEY_TYPE,
                MdTypePackage.Literals.BASIC_REGISTER_TYPES__SELECTION_TYPE,
                MdTypePackage.Literals.BASIC_REGISTER_TYPES__LIST_TYPE,
                MdTypePackage.Literals.BASIC_REGISTER_TYPES__MANAGER_TYPE }))
        .put(MdTypePackage.Literals.ACCUMULATION_REGISTER_TYPES,
            Lists.newArrayList(new EReference[] { MdTypePackage.Literals.ACCUMULATION_REGISTER_TYPES__RECORD_TYPE,
                MdTypePackage.Literals.BASIC_REGISTER_TYPES__MANAGER_TYPE,
                MdTypePackage.Literals.BASIC_REGISTER_TYPES__SELECTION_TYPE,
                MdTypePackage.Literals.BASIC_REGISTER_TYPES__LIST_TYPE,
                MdTypePackage.Literals.BASIC_REGISTER_TYPES__RECORD_SET_TYPE,
                MdTypePackage.Literals.BASIC_REGISTER_TYPES__RECORD_KEY_TYPE }))
        .put(MdTypePackage.Literals.CATALOG_TYPES,
            Lists.newArrayList(new EReference[] { MdTypePackage.Literals.BASIC_DB_OBJECT_TYPES__OBJECT_TYPE,
                MdTypePackage.Literals.BASIC_DB_OBJECT_TYPES__REF_TYPE,
                MdTypePackage.Literals.BASIC_DB_OBJECT_TYPES__SELECTION_TYPE,
                MdTypePackage.Literals.BASIC_DB_OBJECT_TYPES__LIST_TYPE,
                MdTypePackage.Literals.BASIC_DB_OBJECT_TYPES__MANAGER_TYPE }))
        .put(MdTypePackage.Literals.CHART_OF_ACCOUNTS_TYPES,
            Lists.newArrayList(new EReference[] { MdTypePackage.Literals.BASIC_DB_OBJECT_TYPES__OBJECT_TYPE,
                MdTypePackage.Literals.BASIC_DB_OBJECT_TYPES__REF_TYPE,
                MdTypePackage.Literals.BASIC_DB_OBJECT_TYPES__SELECTION_TYPE,
                MdTypePackage.Literals.BASIC_DB_OBJECT_TYPES__LIST_TYPE,
                MdTypePackage.Literals.BASIC_DB_OBJECT_TYPES__MANAGER_TYPE,
                MdTypePackage.Literals.CHART_OF_ACCOUNTS_TYPES__EXT_DIMENSION_TYPES,
                MdTypePackage.Literals.CHART_OF_ACCOUNTS_TYPES__EXT_DIMENSION_TYPES_ROW }))
        .put(MdTypePackage.Literals.DOCUMENT_TYPES,
            Lists.newArrayList(new EReference[] { MdTypePackage.Literals.BASIC_DB_OBJECT_TYPES__OBJECT_TYPE,
                MdTypePackage.Literals.BASIC_DB_OBJECT_TYPES__REF_TYPE,
                MdTypePackage.Literals.BASIC_DB_OBJECT_TYPES__SELECTION_TYPE,
                MdTypePackage.Literals.BASIC_DB_OBJECT_TYPES__LIST_TYPE,
                MdTypePackage.Literals.BASIC_DB_OBJECT_TYPES__MANAGER_TYPE }))
        .put(MdTypePackage.Literals.ENUM_TYPES,
            Lists.newArrayList(new EReference[] { MdTypePackage.Literals.ENUM_TYPES__REF_TYPE,
                MdTypePackage.Literals.ENUM_TYPES__MANAGER_TYPE, MdTypePackage.Literals.ENUM_TYPES__LIST_TYPE }))
        .put(MdTypePackage.Literals.FILTER_CRITERION_TYPES,
            Lists.newArrayList(new EReference[] { MdTypePackage.Literals.FILTER_CRITERION_TYPES__MANAGER_TYPE,
                MdTypePackage.Literals.FILTER_CRITERION_TYPES__LIST_TYPE }))
        .put(MdTypePackage.Literals.INFORMATION_REGISTER_TYPES,
            Lists.newArrayList(new EReference[] { MdTypePackage.Literals.INFORMATION_REGISTER_TYPES__RECORD_TYPE,
                MdTypePackage.Literals.BASIC_REGISTER_TYPES__MANAGER_TYPE,
                MdTypePackage.Literals.BASIC_REGISTER_TYPES__SELECTION_TYPE,
                MdTypePackage.Literals.BASIC_REGISTER_TYPES__LIST_TYPE,
                MdTypePackage.Literals.BASIC_REGISTER_TYPES__RECORD_SET_TYPE,
                MdTypePackage.Literals.BASIC_REGISTER_TYPES__RECORD_KEY_TYPE,
                MdTypePackage.Literals.INFORMATION_REGISTER_TYPES__RECORD_MANAGER_TYPE }))
        .put(MdTypePackage.Literals.CHART_OF_CALCULATION_TYPES_TYPES,
            Lists.newArrayList(new EReference[] { MdTypePackage.Literals.BASIC_DB_OBJECT_TYPES__OBJECT_TYPE,
                MdTypePackage.Literals.BASIC_DB_OBJECT_TYPES__REF_TYPE,
                MdTypePackage.Literals.BASIC_DB_OBJECT_TYPES__SELECTION_TYPE,
                MdTypePackage.Literals.BASIC_DB_OBJECT_TYPES__LIST_TYPE,
                MdTypePackage.Literals.BASIC_DB_OBJECT_TYPES__MANAGER_TYPE,
                MdTypePackage.Literals.CHART_OF_CALCULATION_TYPES_TYPES__DISPLACING_CALCULATION_TYPES_TYPE,
                MdTypePackage.Literals.CHART_OF_CALCULATION_TYPES_TYPES__DISPLACING_CALCULATION_TYPES_ROW_TYPE,
                MdTypePackage.Literals.CHART_OF_CALCULATION_TYPES_TYPES__BASE_CALCULATION_TYPES_TYPE,
                MdTypePackage.Literals.CHART_OF_CALCULATION_TYPES_TYPES__BASE_CALCULATION_TYPES_ROW_TYPE,
                MdTypePackage.Literals.CHART_OF_CALCULATION_TYPES_TYPES__LEADING_CALCULATION_TYPES_TYPE,
                MdTypePackage.Literals.CHART_OF_CALCULATION_TYPES_TYPES__LEADING_CALCULATION_TYPES_ROW_TYPE }))
        .put(MdTypePackage.Literals.CHART_OF_CHARACTERISTIC_TYPES_TYPES,
            Lists.newArrayList(new EReference[] { MdTypePackage.Literals.BASIC_DB_OBJECT_TYPES__OBJECT_TYPE,
                MdTypePackage.Literals.BASIC_DB_OBJECT_TYPES__REF_TYPE,
                MdTypePackage.Literals.BASIC_DB_OBJECT_TYPES__SELECTION_TYPE,
                MdTypePackage.Literals.BASIC_DB_OBJECT_TYPES__LIST_TYPE,
                MdTypePackage.Literals.CHART_OF_CHARACTERISTIC_TYPES_TYPES__CONTAINER_TYPE,
                MdTypePackage.Literals.BASIC_DB_OBJECT_TYPES__MANAGER_TYPE }))
        .put(MdTypePackage.Literals.CALCULATION_REGISTER_TYPES,
            Lists.newArrayList(new EReference[] { MdTypePackage.Literals.CALCULATION_REGISTER_TYPES__RECORD_TYPE,
                MdTypePackage.Literals.BASIC_REGISTER_TYPES__MANAGER_TYPE,
                MdTypePackage.Literals.BASIC_REGISTER_TYPES__SELECTION_TYPE,
                MdTypePackage.Literals.BASIC_REGISTER_TYPES__LIST_TYPE,
                MdTypePackage.Literals.BASIC_REGISTER_TYPES__RECORD_SET_TYPE,
                MdTypePackage.Literals.BASIC_REGISTER_TYPES__RECORD_KEY_TYPE,
                MdTypePackage.Literals.CALCULATION_REGISTER_TYPES__RECALCS_TYPE }))
        .put(MdTypePackage.Literals.RECALCULATION_TYPES,
            Lists.newArrayList(new EReference[] { MdTypePackage.Literals.RECALCULATION_TYPES__RECORD_TYPE,
                MdTypePackage.Literals.RECALCULATION_TYPES__MANAGER_TYPE,
                MdTypePackage.Literals.RECALCULATION_TYPES__RECORD_SET_TYPE }))
        .put(MdTypePackage.Literals.TABLE_TYPES,
            Lists.newArrayList(new EReference[] { MdTypePackage.Literals.TABLE_TYPES__MANAGER_TYPE,
                MdTypePackage.Literals.TABLE_TYPES__OBJECT_TYPE, MdTypePackage.Literals.TABLE_TYPES__REF_TYPE,
                MdTypePackage.Literals.TABLE_TYPES__LIST_TYPE, MdTypePackage.Literals.TABLE_TYPES__RECORD_TYPE,
                MdTypePackage.Literals.TABLE_TYPES__RECORD_SET_TYPE,
                MdTypePackage.Literals.TABLE_TYPES__RECORD_KEY_TYPE,
                MdTypePackage.Literals.TABLE_TYPES__RECORD_MANAGER_TYPE }))
        .put(MdTypePackage.Literals.CUBE_TYPES,
            Lists.newArrayList(new EReference[] { MdTypePackage.Literals.CUBE_TYPES__MANAGER_TYPE,
                MdTypePackage.Literals.CUBE_TYPES__LIST_TYPE, MdTypePackage.Literals.CUBE_TYPES__RECORD_SET_TYPE,
                MdTypePackage.Literals.CUBE_TYPES__RECORD_KEY_TYPE,
                MdTypePackage.Literals.CUBE_TYPES__RECORD_MANAGER_TYPE,
                MdTypePackage.Literals.CUBE_TYPES__DIMENSION_TABLES_MANAGER_TYPE }))
        .put(MdTypePackage.Literals.CUBE_DIMENSION_TABLE_TYPES,
            Lists.newArrayList(new EReference[] { MdTypePackage.Literals.CUBE_DIMENSION_TABLE_TYPES__MANAGER_TYPE,
                MdTypePackage.Literals.CUBE_DIMENSION_TABLE_TYPES__OBJECT_TYPE,
                MdTypePackage.Literals.CUBE_DIMENSION_TABLE_TYPES__REF_TYPE,
                MdTypePackage.Literals.CUBE_DIMENSION_TABLE_TYPES__LIST_TYPE }))
        .build();

    @Override
    public List<EReference> getOrderedReferences(MdTypes mdTypes)
    {
        List<EReference> list = ORDER_MAP.get(mdTypes.eClass());
        return (list != null) ? list : (List<EReference>)mdTypes.eClass().getEAllReferences();
    }
}
