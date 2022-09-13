/**
 *
 */
package ru.capralow.dt.modeling.md.internal.yaml.writer;

import javax.xml.stream.XMLStreamException;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage;
import com._1c.g5.v8.dt.metadata.mdclass.ObjectBelonging;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.md.yaml.impl.MetadataFeatureNameProvider;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;
import ru.capralow.dt.modeling.yaml.writer.ISpecifiedElementWriter;

@Singleton
public class ObjectBelongingWriter
    implements ISpecifiedElementWriter
{
    @Inject
    private MetadataFeatureNameProvider nameProvider;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws XMLStreamException, ExportException
    {
        if (feature.getEType() != MdClassPackage.Literals.OBJECT_BELONGING)
            throw new IllegalArgumentException(String.format("Invalid feature type in %s", new Object[] { feature }));
        Object value = eObject.eGet(feature);
        if (value != null && value != ObjectBelonging.NATIVE)
            writer.writeTextElement(this.nameProvider.getElementQName(feature), eObject.eGet(feature));
    }
}
