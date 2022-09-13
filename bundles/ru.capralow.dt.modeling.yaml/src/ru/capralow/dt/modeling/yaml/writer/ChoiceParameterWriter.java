/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.yaml.writer;

import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.metadata.common.ChoiceParameter;
import com._1c.g5.v8.dt.metadata.common.CommonPackage;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.yaml.IQNameProvider;
import ru.capralow.dt.modeling.yaml.IXmlElements;

@Singleton
public class ChoiceParameterWriter
    implements ISpecifiedElementWriter
{
    @Inject
    private IQNameProvider nameManager;

    @Inject
    @Named("SmartSpecifiedElementWriter")
    private ISpecifiedElementWriter featureWriter;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws XMLStreamException, ExportException
    {
        if (feature.getEType() != CommonPackage.Literals.CHOICE_PARAMETER)
        {
            throw new IllegalArgumentException(String.format("Invalid feature type in %s", new Object[] { feature }));
        }
        if (eObject == null)
        {
            return;
        }
        QName elementQName = nameManager.getElementQName(feature);
        Object value = eObject.eGet(feature);
        if (feature.isMany())
        {
            List<ChoiceParameter> list = (List<ChoiceParameter>)value;
            if (!list.isEmpty())
            {
                writer.writeStartElement(elementQName.toString());
                for (ChoiceParameter choiceParameter : list)
                {
                    writeChoiceParameter(writer, choiceParameter, version);
                }
                writer.writeEndElement();
            }
            else if (writeEmpty)
            {
                writer.writeEmptyElement(elementQName.toString());
            }
        }
        else
        {
            writeChoiceParameter(writer, (ChoiceParameter)value, version);
        }
    }

    private void writeChoiceParameter(YamlStreamWriter writer, ChoiceParameter choiceParameter, Version version)
        throws XMLStreamException, ExportException
    {
        writer.writeStartElement(IXmlElements.APP.ITEM);
        writer.writeElement("name", Strings.nullToEmpty(choiceParameter.getName()));
        this.featureWriter.write(writer, choiceParameter, CommonPackage.Literals.CHOICE_PARAMETER__VALUE, true,
            version);
        writer.writeEndElement();
    }
}
