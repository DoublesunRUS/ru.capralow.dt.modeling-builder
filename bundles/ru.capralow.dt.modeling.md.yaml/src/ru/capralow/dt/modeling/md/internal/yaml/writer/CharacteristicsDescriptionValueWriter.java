/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.internal.yaml.writer;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.mcore.ReferenceValue;
import com._1c.g5.v8.dt.platform.version.Version;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.yaml.IXmlElements;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;
import ru.capralow.dt.modeling.yaml.writer.ValueWriter;

public class CharacteristicsDescriptionValueWriter
    extends ValueWriter
{
    @Override
    public void writeValue(YamlStreamWriter writer, Object valueObject, QName elementName, boolean writeEmpty,
        EStructuralFeature feature, Version version) throws XMLStreamException, ExportException
    {
        if (valueObject == null || valueObject instanceof com._1c.g5.v8.dt.mcore.UndefinedValue
            || valueObject instanceof com._1c.g5.v8.dt.mcore.NullValue
            || (valueObject instanceof ReferenceValue && ((ReferenceValue)valueObject).getValue() == null))
        {
            writer.writeEmptyElement(elementName);
            writer.writeElement(IXmlElements.XSI.NIL, "true");
        }
        else
        {
            super.writeValue(writer, valueObject, elementName, writeEmpty, feature, version);
        }
    }
}
