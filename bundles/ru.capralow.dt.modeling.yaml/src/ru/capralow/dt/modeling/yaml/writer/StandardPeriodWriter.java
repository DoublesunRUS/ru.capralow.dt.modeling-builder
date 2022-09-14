/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.yaml.writer;

import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.mcore.McorePackage;
import com._1c.g5.v8.dt.mcore.StandardPeriod;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.inject.Inject;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.yaml.IQNameProvider;
import ru.capralow.dt.modeling.yaml.IYamlElements;

public class StandardPeriodWriter
    implements ISpecifiedElementWriter
{
    @Inject
    private IQNameProvider nameManager;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws ExportException
    {
        if (feature.isMany() || feature.getEType() != McorePackage.Literals.STANDARD_PERIOD)
        {
            throw new IllegalArgumentException(String.format("Invalid feature type in %s", new Object[] { feature }));
        }
        if (eObject == null)
        {
            return;
        }
        QName elementName = this.nameManager.getElementQName(feature);
        StandardPeriod standardPeriod = (StandardPeriod)eObject.eGet(feature);
        if (standardPeriod != null)
        {
//            writer.writeStartElement(elementName);
            writeStandardPeriod(writer, standardPeriod);
//            writer.writeEndElement();
        }
        else if (writeEmpty)
        {
//            writer.writeEmptyElement(elementName);
        }
    }

    public void writeStandardPeriod(YamlStreamWriter writer, StandardPeriod standardPeriod) throws ExportException
    {
        if (standardPeriod.getVariant() != null)
        {
//            writer.writeStartElement(IXmlElements.V8.VARIANT);
            writer.writeElement(IYamlElements.XSI.TYPE, IYamlElements.V8.STANDARD_PERIOD_VARIANT);
//            writer.writeCharacters(standardPeriod.getVariant().toString());
//            writer.writeInlineEndElement();
        }
        if (standardPeriod.getStartDate() != null)
        {
//            writer.writeStartElement(IXmlElements.V8.START_DATE);
//            writer.writeCharacters(standardPeriod.getStartDate().toString());
//            writer.writeInlineEndElement();
        }
        if (standardPeriod.getEndDate() != null)
        {
//            writer.writeStartElement(IXmlElements.V8.END_DATE);
//            writer.writeCharacters(standardPeriod.getEndDate().toString());
//            writer.writeInlineEndElement();
        }
    }
}
