/**
 *
 */
package ru.capralow.dt.modeling.md.internal.yaml.writer;

import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.core.model.ISymLinkConverter;
import com._1c.g5.v8.dt.core.naming.ISymbolicNameService;
import com._1c.g5.v8.dt.mcore.Field;
import com._1c.g5.v8.dt.mcore.McorePackage;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.md.yaml.IMetadataXmlElements;
import ru.capralow.dt.modeling.md.yaml.impl.MetadataFeatureNameProvider;
import ru.capralow.dt.modeling.yaml.writer.ISpecifiedElementWriter;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;

@Singleton
public class FieldWriter
    implements ISpecifiedElementWriter
{
    @Inject
    private MetadataFeatureNameProvider nameProvider;

    @Inject
    private ISymLinkConverter linkConverter;

    @Inject
    private ISymbolicNameService symbolicNameService;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws ExportException
    {
        if (feature.getEType() != McorePackage.Literals.FIELD)
            throw new IllegalArgumentException(String.format("Invalid feature type in %s", new Object[] { feature }));
        QName elementQName = this.nameProvider.getElementQName(feature);
        if (eObject == null)
        {
            if (writeEmpty)
                writer.writeEmptyElement(elementQName);
            return;
        }
        Object value = eObject.eGet(feature);
        if (value instanceof List && !((List)value).isEmpty())
        {
            writer.writeStartElement(elementQName);
            for (Field field : (List<Field>)value)
            {
                writer.writeStartElement(IMetadataXmlElements.XR.FIELD);
                writer.writeCharacters(this.linkConverter.convert(eObject, (EReference)feature,
                    this.symbolicNameService.generateSymbolicName((EObject)field, eObject, (EReference)feature)));
                writer.writeInlineEndElement();
            }
            writer.writeEndElement();
        }
        else if (value instanceof Field)
        {
            writer.writeStartElement(elementQName);
            writer.writeCharacters(this.linkConverter.convert(eObject, (EReference)feature,
                this.symbolicNameService.generateSymbolicName((EObject)value, eObject, (EReference)feature)));
            writer.writeInlineEndElement();
        }
        else if (writeEmpty)
        {
            writer.writeEmptyElement(elementQName);
        }
    }
}
