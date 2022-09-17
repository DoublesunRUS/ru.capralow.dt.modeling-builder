/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.internal.yaml.writer;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.mcore.McorePackage;
import com._1c.g5.v8.dt.mcore.QName;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.common.base.Strings;
import com.google.inject.Inject;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.yaml.IQnameProvider;
import ru.capralow.dt.modeling.yaml.writer.ISpecifiedElementWriter;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;

public class QNameWriter
    implements ISpecifiedElementWriter
{
    @Inject
    private IQnameProvider nameProvider;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version, Map<String, Object> group) throws ExportException
    {
        if (feature.isMany() || feature.getEType() != McorePackage.Literals.QNAME)
        {
            throw new IllegalArgumentException(String.format("Invalid feature type in %s", new Object[] { feature }));
        }
        javax.xml.namespace.QName featureName = nameProvider.getElementQName(feature);
        QName qName = (QName)eObject.eGet(feature);
        if (qName != null)
        {
            String nsUri = qName.getNsUri();
            String name = qName.getName();
            if (!Strings.isNullOrEmpty(name) && !Strings.isNullOrEmpty(nsUri))
            {
//                writer.writeStartElement(featureName.toString());
                writer.writeElement("", ':' + name, group);
//                writer.writeInlineEndElement();
            }
        }
        else if (writeEmpty)
        {
//            writer.writeEmptyElement(featureName.toString());
        }
    }

}
