/**
 *
 */
package ru.capralow.dt.modeling.yaml.writer;

import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.form.model.Decoration;
import com._1c.g5.v8.dt.form.model.FormPackage;
import com._1c.g5.v8.dt.mcore.McorePackage;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.internal.yaml.YamlPlugin;
import ru.capralow.dt.modeling.yaml.IQNameProvider;
import ru.capralow.dt.modeling.yaml.IXmlElements;

@Singleton
public class LocalStringMapEntryWriter
    implements ISpecifiedElementWriter
{
    @Inject
    private IQNameProvider nameProvider;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws XMLStreamException, ExportException
    {
        Preconditions.checkArgument(
            (feature != null && feature.isMany() && feature.getEType() == McorePackage.Literals.LOCAL_STRING_MAP_ENTRY),
            "Invalid feature type %s", feature);
        QName name = this.nameProvider.getElementQName(feature);
        EMap<String, String> localStringMap = (EMap<String, String>)eObject.eGet(feature);
        writeLocalString(writer, eObject, feature, localStringMap, name, writeEmpty, version);
    }

    public void writeLocalString(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature,
        EMap<String, String> localString, QName elementQName, boolean writeEmpty, Version version)
        throws XMLStreamException, ExportException
    {
        boolean isFormattedTitle = isFormattedTitle(eObject, feature);
        if (localString != null && !localString.isEmpty())
        {
            writer.writeStartElement(elementQName.toString());
            if (isFormattedTitle)
                writer.writeElement("formatted", Boolean.toString(((Decoration)eObject).isFormatted()));
            for (Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)localString.entrySet())
            {
                writer.writeStartElement(IXmlElements.V8.ITEM);
                String lang = entry.getKey();
                if (lang != null && !lang.isEmpty())
                {
                    writer.writeStartElement(IXmlElements.V8.LANG);
                    writer.writeCharacters(lang);
                    writer.writeInlineEndElement();
                }
                else
                {
                    if (lang == null)
                        YamlPlugin.log(YamlPlugin.createErrorStatus("Localized string entry key cannot be null", null));
                    writer.writeEmptyElement(IXmlElements.V8.LANG);
                }
                String value = entry.getValue();
                if (value != null && !value.isEmpty())
                {
                    writer.writeStartElement(IXmlElements.V8.CONTENT);
                    writer.writeCharacters(value);
                    writer.writeInlineEndElement();
                }
                else
                {
                    writer.writeEmptyElement(IXmlElements.V8.CONTENT);
                }
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
        else if (isFormattedTitle && ((Decoration)eObject).isFormatted())
        {
            writer.writeEmptyElement(elementQName.toString());
            if (isFormattedTitle)
                writer.writeElement("formatted", Boolean.toString(((Decoration)eObject).isFormatted()));
        }
        else if (writeEmpty)
        {
            writer.writeEmptyElement(elementQName.toString());
        }
    }

    private boolean isFormattedTitle(EObject context, EStructuralFeature feature)
    {
        return (context instanceof Decoration && feature == FormPackage.Literals.TITLED__TITLE);
    }
}
