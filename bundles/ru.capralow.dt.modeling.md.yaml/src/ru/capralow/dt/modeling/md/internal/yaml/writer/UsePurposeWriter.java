/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.internal.yaml.writer;

import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.metadata.common.ApplicationUsePurpose;
import com._1c.g5.v8.dt.metadata.common.CommonPackage;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.md.yaml.impl.MetadataFeatureNameProvider;
import ru.capralow.dt.modeling.yaml.writer.ISpecifiedElementWriter;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;

@Singleton
public class UsePurposeWriter
    implements ISpecifiedElementWriter
{
    @Inject
    private MetadataFeatureNameProvider nameProvider;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws ExportException
    {
        if (feature.isMany() && feature.getEType() != CommonPackage.Literals.APPLICATION_USE_PURPOSE)
        {
            throw new IllegalArgumentException(String.format("Invalid feature type in %s", new Object[] { feature }));
        }
        QName elementQName = this.nameProvider.getElementQName(feature);
        if (eObject == null)
        {
//            writer.writeEmptyElement(elementQName);
            return;
        }
        Object value = eObject.eGet(feature);
        if (!(value instanceof List) || ((List)value).isEmpty())
        {
//            writer.writeEmptyElement(elementQName);
            return;
        }
        writer.writeElement(elementQName, "");
        for (Object object : (List)value)
        {
            if (object instanceof ApplicationUsePurpose)
            {
//                writer.writeStartElement(IMetadataYamlElements.V8.VALUE);
//                writer.writeElement(IYamlElements.XSI.TYPE, IMetadataYamlElements.APP.APPLICATION_USE_PURPOSE);
//                writer.writeCharacters(getLiteral((ApplicationUsePurpose)object, version));
//                writer.writeInlineEndElement();
            }
        }
//        writer.writeEndElement();
    }

    private String getLiteral(ApplicationUsePurpose usePurpose, Version version)
    {
        boolean oldLiterals = version.isLessThan(Version.V8_3_12);
        switch (usePurpose)
        {
        case PERSONAL_COMPUTER:
            return oldLiterals ? usePurpose.getLiteral() : "PlatformApplication";
        case MOBILE_DEVICE:
            return oldLiterals ? usePurpose.getLiteral() : "MobilePlatformApplication";
        default:
            throw new IllegalArgumentException("Unexpected ApplicationUsePurpose enum value");
        }
    }
}
