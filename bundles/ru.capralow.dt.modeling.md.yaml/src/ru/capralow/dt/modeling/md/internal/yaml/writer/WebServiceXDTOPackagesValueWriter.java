/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.internal.yaml.writer;

import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.mcore.Value;
import com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.yaml.IQNameProvider;
import ru.capralow.dt.modeling.yaml.IYamlElements;
import ru.capralow.dt.modeling.yaml.writer.ISpecifiedElementWriter;
import ru.capralow.dt.modeling.yaml.writer.ValueWriter;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;

@Singleton
public class WebServiceXDTOPackagesValueWriter
    implements ISpecifiedElementWriter
{
    @Inject
    private IQNameProvider nameManager;

    @Inject
    private ValueWriter featureWriter;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws ExportException
    {
        if (!feature.isMany() || feature != MdClassPackage.Literals.WEB_SERVICE__XDTO_PACKAGES)
        {
            throw new IllegalArgumentException(String.format("Invalid feature type in %s", new Object[] { feature }));
        }
        List<Value> list = (List<Value>)eObject.eGet(feature);
        QName featureName = this.nameManager.getElementQName(feature);
        if (!list.isEmpty())
        {
            writer.writeStartElement(featureName);
            for (Value value : list)
            {
                writer.writeStartElement(IYamlElements.XR.ITEM);
                writer.writeEmptyElement(IYamlElements.XR.PRESENTATION);
                writer.writeTextElement(IYamlElements.XR.CHECK_STATE, Integer.valueOf(0));
                this.featureWriter.writeValue(writer, value, IYamlElements.XR.VALUE, writeEmpty, null, version);
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
        else if (writeEmpty)
        {
            writer.writeEmptyElement(featureName);
        }
    }
}
