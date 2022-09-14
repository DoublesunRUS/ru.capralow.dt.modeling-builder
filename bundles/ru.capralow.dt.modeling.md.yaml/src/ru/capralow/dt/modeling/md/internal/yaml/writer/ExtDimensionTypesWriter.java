/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.internal.yaml.writer;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.core.naming.ISymbolicNameService;
import com._1c.g5.v8.dt.metadata.mdclass.ExtDimensionType;
import com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.common.base.Strings;
import com.google.inject.Inject;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.md.yaml.IMetadataYamlElements;
import ru.capralow.dt.modeling.yaml.writer.ISpecifiedElementWriter;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;

public class ExtDimensionTypesWriter
    implements ISpecifiedElementWriter
{
    @Inject
    private MetadataSmartFeatureWriter featureWriter;

    @Inject
    private ISymbolicNameService symbolicNameService;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws ExportException
    {
        if (feature != MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED_ITEM__EXT_DIMENSION_TYPES)
            throw new IllegalArgumentException(String.format("Invalid feature %s, expected %s", new Object[] { feature,
                MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED_ITEM__EXT_DIMENSION_TYPES }));
        QName elementQName = IMetadataYamlElements.XPR.EXT_DIMENSION_TYPES;
        List<ExtDimensionType> extDimensionTypeList = getExtDimensionTypeList(eObject.eGet(feature));
        if (extDimensionTypeList.isEmpty())
        {
            writer.writeEmptyElement(elementQName);
        }
        else
        {
            writer.writeStartElement(elementQName);
            for (ExtDimensionType extDimensionType : extDimensionTypeList)
            {
                writer.writeStartElement(IMetadataYamlElements.XPR.EXT_DIMENSION_TYPE);
                String ref = this.symbolicNameService.generateSymbolicName(extDimensionType.getCharacteristicType(),
                    extDimensionType, MdClassPackage.Literals.EXT_DIMENSION_TYPE__CHARACTERISTIC_TYPE);
                if (!Strings.isNullOrEmpty(ref))
                    writer.writeElement("name", ref);
                this.featureWriter.write(writer, extDimensionType, MdClassPackage.Literals.EXT_DIMENSION_TYPE__TURNOVER,
                    writeEmpty, version);
                this.featureWriter.write(writer, extDimensionType,
                    MdClassPackage.Literals.EXT_DIMENSION_TYPE__EXT_DIMENSION_ACCOUNTING_FLAGS, writeEmpty, version);
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
    }

    private List<ExtDimensionType> getExtDimensionTypeList(Object value)
    {
        if (value instanceof List)
            return (List<ExtDimensionType>)((List)value).stream()
                .filter(ExtDimensionType.class::isInstance)
                .map(ExtDimensionType.class::cast)
                .filter(entry -> (((ExtDimensionType)entry).getCharacteristicType() != null))
                .collect(Collectors.toList());
        return Collections.emptyList();
    }
}
