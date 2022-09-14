/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.internal.yaml.writer;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.mcore.NumberValue;
import com._1c.g5.v8.dt.mcore.StringValue;
import com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.common.base.Strings;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.md.yaml.IMetadataYamlElements;
import ru.capralow.dt.modeling.yaml.IYamlElements;
import ru.capralow.dt.modeling.yaml.writer.ISpecifiedElementWriter;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;

public class PredefinedItemCodeWriter
    implements ISpecifiedElementWriter
{
    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws ExportException
    {
        if (feature != MdClassPackage.Literals.CATALOG_PREDEFINED_ITEM__CODE
            && feature != MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED_ITEM__CODE
            && feature != MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES_PREDEFINED_ITEM__CODE
            && feature != MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES_PREDEFINED_ITEM__CODE)
            throw new IllegalArgumentException(String.format("Invalid feature %s", new Object[] { feature }));
        Object objectValue = eObject.eGet(feature);
        String value = getValueString(objectValue);
        if (!Strings.isNullOrEmpty(value))
        {
            if (objectValue instanceof NumberValue)
            {
                writer.writeStartElement(IMetadataYamlElements.XPR.CODE);
                writer.writeElement(IYamlElements.XSI.TYPE, IYamlElements.XS.DECIMAL);
                writer.writeCharacters(value);
                writer.writeInlineEndElement();
            }
            else
            {
                writer.writeTextElement(IMetadataYamlElements.XPR.CODE, value);
            }
        }
        else if (writeEmpty)
        {
            writer.writeEmptyElement(IMetadataYamlElements.XPR.CODE);
        }
    }

    private String getValueString(Object objectValue) throws ExportException
    {
        if (objectValue instanceof String)
            return (String)objectValue;
        if (objectValue instanceof StringValue)
            return ((StringValue)objectValue).getValue();
        if (objectValue instanceof NumberValue)
            return ((NumberValue)objectValue).getValue().toString();
        if (objectValue == null || objectValue instanceof com._1c.g5.v8.dt.mcore.UndefinedValue)
            return null;
        throw new ExportException(Messages.PredefinedItemCodeWriter_unknown_type_of_predefined_item_code);
    }
}
