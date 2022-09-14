/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.internal.yaml.writer;

import java.text.MessageFormat;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.mcore.TypeItem;
import com._1c.g5.v8.dt.mcore.util.McoreUtil;
import com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage;
import com._1c.g5.v8.dt.metadata.mdtype.AbstractMdType;
import com._1c.g5.v8.dt.metadata.mdtype.MdType;
import com._1c.g5.v8.dt.metadata.mdtype.MdTypePackage;
import com._1c.g5.v8.dt.metadata.mdtype.MdTypeSet;
import com._1c.g5.v8.dt.metadata.mdtype.MdTypes;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.md.yaml.writer.IProducedTypesOrderProvider;
import ru.capralow.dt.modeling.yaml.writer.ISpecifiedElementWriter;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;

@Singleton
public class ProducedTypeWriter
    implements ISpecifiedElementWriter
{
    @Inject
    private IProducedTypesOrderProvider orderProvider;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws ExportException
    {
        Preconditions.checkArgument(eObject instanceof com._1c.g5.v8.dt.metadata.mdclass.MdObject,
            String.format("Invalid object %s", new Object[] { eObject }));
        Object value = eObject.eGet(feature);
        Preconditions.checkArgument(value instanceof MdTypes,
            String.format("Invalid value %s", new Object[] { eObject }));
        MdTypes mdTypes = (MdTypes)value;
        for (EReference reference : this.orderProvider.getOrderedReferences(mdTypes))
        {
            if (isTypeSupportedByVersion(reference, version))
            {
                AbstractMdType abstractMdType = (AbstractMdType)mdTypes.eGet(reference);
                TypeItem type = getType(abstractMdType);
                String category = getCategoryName(abstractMdType);
                if (type == null || category == null)
                {
                    throw new ExportException(
                        MessageFormat.format(Messages.ProducedTypeWriter_error_exporting__0__produced_type,
                            new Object[] { reference.getName() }));
                }
                String producedTypeName = McoreUtil.getTypeName(type);
                if (eObject.eClass() == MdClassPackage.Literals.EXTERNAL_DATA_PROCESSOR
                    && producedTypeName.startsWith("ExternalDataProcessor."))
                {
                    producedTypeName =
                        producedTypeName.replace("ExternalDataProcessor.", "ExternalDataProcessorObject.");
                }
                else if (eObject.eClass() == MdClassPackage.Literals.EXTERNAL_REPORT
                    && producedTypeName.startsWith("ExternalReport."))
                {
                    producedTypeName = producedTypeName.replace("ExternalReport.", "ExternalReportObject.");
                }
                writeProducedType(writer, producedTypeName, category, abstractMdType.getTypeId().toString(),
                    abstractMdType.getValueTypeId().toString());
            }
        }
    }

    private boolean isTypeSupportedByVersion(EReference reference, Version version)
    {
        if (version.isLessThan(Version.V8_3_13) && reference == MdTypePackage.Literals.CONSTANT_TYPES__VALUE_KEY_TYPE)
        {
            return false;
        }
        if (version.isLessThan(Version.V8_3_14) && reference == MdTypePackage.Literals.TABLE_TYPES__RECORD_TYPE)
        {
            return false;
        }
        if (reference == MdTypePackage.Literals.CUBE_TYPES__RECORD_TYPE)
        {
            return false;
        }
        return true;
    }

    private void writeProducedType(YamlStreamWriter writer, String name, String category, String typeId,
        String valueTypeId) throws ExportException
    {
//        writer.writeElement(IMetadataYamlElements.NAME_ATTRIBUTE.getLocalPart(), name);
//        writer.writeElement(IMetadataYamlElements.CATEGORY_ATTRIBUTE.getLocalPart(), category);
//        writer.writeElement(IMetadataYamlElements.XR.TYPE_ID, typeId);
//        writer.writeElement(IMetadataYamlElements.XR.VALUE_ID, valueTypeId);
    }

    private TypeItem getType(AbstractMdType abstractMdType)
    {
        if (abstractMdType instanceof MdType)
        {
            return ((MdType)abstractMdType).getType();
        }
        return ((MdTypeSet)abstractMdType).getTypeSet();
    }

    String getCategoryName(AbstractMdType mdType)
    {
        if (mdType.eClass() == MdTypePackage.Literals.MD_CONTAINER_TYPE)
        {
            return "Characteristic";
        }
        if (mdType.eClass() == MdTypePackage.Literals.MD_LIST_TYPE)
        {
            return "List";
        }
        if (mdType.eClass() == MdTypePackage.Literals.MD_MANAGER_TYPE)
        {
            if (mdType.eContainingFeature() == MdTypePackage.Literals.EXTERNAL_DATA_SOURCE_TYPES__TABLES_MANAGER_TYPE)
            {
                return "TablesManager";
            }
            if (mdType.eContainingFeature() == MdTypePackage.Literals.EXTERNAL_DATA_SOURCE_TYPES__CUBES_MANAGER_TYPE)
            {
                return "CubesManager";
            }
            return "Manager";
        }
        if (mdType.eClass() == MdTypePackage.Literals.MD_OBJECT_TYPE)
        {
            return (mdType.eContainer() instanceof com._1c.g5.v8.dt.metadata.mdtype.TabularSectionTypes)
                ? "TabularSection" : "Object";
        }
        if (mdType.eClass() == MdTypePackage.Literals.MD_RECORD_KEY_TYPE)
        {
            return "RecordKey";
        }
        if (mdType.eClass() == MdTypePackage.Literals.MD_RECORD_MANAGER_TYPE)
        {
            return "RecordManager";
        }
        if (mdType.eClass() == MdTypePackage.Literals.MD_RECORD_SET_TYPE)
        {
            return "RecordSet";
        }
        if (mdType.eClass() == MdTypePackage.Literals.MD_RECORD_TYPE)
        {
            return "Record";
        }
        if (mdType.eClass() == MdTypePackage.Literals.MD_REF_TYPE)
        {
            return "Ref";
        }
        if (mdType.eClass() == MdTypePackage.Literals.MD_ROW_TYPE)
        {
            return "TabularSectionRow";
        }
        if (mdType.eClass() == MdTypePackage.Literals.MD_SELECTION_TYPE)
        {
            return "Selection";
        }
        if (mdType.eClass() == MdTypePackage.Literals.MD_USER_DEFINED_TYPE)
        {
            return "DefinedType";
        }
        if (mdType.eClass() == MdTypePackage.Literals.MD_VALUE_MANAGER_TYPE)
        {
            return "ValueManager";
        }
        if (mdType.eClass() == MdTypePackage.Literals.MD_VALUE_KEY_TYPE)
        {
            return "ValueKey";
        }
        if (mdType.eClass() == MdTypePackage.Literals.ROUTE_POINT_REF_TYPE)
        {
            return "RoutePointRef";
        }
        if (mdType.eClass() == MdTypePackage.Literals.MD_EXT_DIMENSIONS_TYPE)
        {
            return "ExtDimensions";
        }
        if (mdType.eClass() == MdTypePackage.Literals.MD_EXT_DIMENSION_TYPES)
        {
            return "ExtDimensionTypes";
        }
        if (mdType.eClass() == MdTypePackage.Literals.MD_EXT_DIMENSION_TYPES_ROW)
        {
            return "ExtDimensionTypesRow";
        }
        if (mdType.eClass() == MdTypePackage.Literals.MD_DISPLACING_CALCULATION_TYPES_TYPE)
        {
            return "DisplacingCalculationTypes";
        }
        if (mdType.eClass() == MdTypePackage.Literals.MD_DISPLACING_CALCULATION_TYPES_ROW_TYPE)
        {
            return "DisplacingCalculationTypesRow";
        }
        if (mdType.eClass() == MdTypePackage.Literals.MD_BASE_CALCULATION_TYPES_TYPE)
        {
            return "BaseCalculationTypes";
        }
        if (mdType.eClass() == MdTypePackage.Literals.MD_BASE_CALCULATION_TYPES_ROW_TYPE)
        {
            return "BaseCalculationTypesRow";
        }
        if (mdType.eClass() == MdTypePackage.Literals.MD_LEADING_CALCULATION_TYPES_TYPE)
        {
            return "LeadingCalculationTypes";
        }
        if (mdType.eClass() == MdTypePackage.Literals.MD_LEADING_CALCULATION_TYPES_ROW_TYPE)
        {
            return "LeadingCalculationTypesRow";
        }
        if (mdType.eClass() == MdTypePackage.Literals.MD_RECALCS_TYPE)
        {
            return "Recalcs";
        }
        if (mdType.eClass() == MdTypePackage.Literals.MD_RECALCS_TYPE)
        {
            return "Recalcs";
        }
        if (mdType.eClass() == MdTypePackage.Literals.MD_DIMENSION_TABLES_MANAGER_TYPE)
        {
            return "DimensionTables";
        }
        return null;
    }
}
