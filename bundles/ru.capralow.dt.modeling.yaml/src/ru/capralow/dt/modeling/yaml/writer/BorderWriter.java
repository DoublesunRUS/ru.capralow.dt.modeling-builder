/**
 *
 */
package ru.capralow.dt.modeling.yaml.writer;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.core.model.ISymLinkConverter;
import com._1c.g5.v8.dt.core.naming.ISymbolicNameService;
import com._1c.g5.v8.dt.mcore.Border;
import com._1c.g5.v8.dt.mcore.BorderDef;
import com._1c.g5.v8.dt.mcore.BorderRef;
import com._1c.g5.v8.dt.mcore.McorePackage;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.common.base.Strings;
import com.google.inject.Inject;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.yaml.IQNameProvider;
import ru.capralow.dt.modeling.yaml.IXmlElements;

public class BorderWriter
    implements ISpecifiedElementWriter
{
    private static final String REF = "ref"; //$NON-NLS-1$

    @Inject
    private ISymLinkConverter linkConverter;

    @Inject
    private ISymbolicNameService symbolicNameService;

    @Inject
    private IQNameProvider nameManager;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws XMLStreamException, ExportException
    {
        if (feature.isMany() || feature.getEType() != McorePackage.Literals.BORDER)
        {
            throw new IllegalArgumentException(String.format("Invalid feature type in %s", new Object[] { feature }));
        }
        Border border = (Border)eObject.eGet(feature);
        writeBorder(writer, feature, border);
    }

    public void writeBorder(YamlStreamWriter writer, EStructuralFeature feature, Border border)
        throws XMLStreamException, ExportException
    {
        if (border != null)
        {
            boolean isValue = feature.getEType() == McorePackage.Literals.VALUE;
            QName elementQName = this.nameManager.getElementQName(feature);
            if (border instanceof BorderRef)
            {
                Border borderRef = ((BorderRef)border).getBorder();
                if (borderRef != null)
                {
                    String ref = this.linkConverter.convert(border, McorePackage.Literals.BORDER_REF__BORDER,
                        this.symbolicNameService.generateSymbolicName(borderRef, border,
                            McorePackage.Literals.BORDER_REF__BORDER));
                    writer.writeEmptyElement(elementQName);
                    if (isValue)
                    {
                        writer.writeElement(IXmlElements.XSI.TYPE, IXmlElements.V8UI.BORDER);
                    }
                    if (!Strings.isNullOrEmpty(ref))
                    {
                        writer.writeElement(REF, ref);
                    }
                }
            }
            else if (border instanceof BorderDef)
            {
                BorderDef borderDef = (BorderDef)border;
                writer.writeStartElement(elementQName);
                if (isValue)
                {
                    writer.writeElement(IXmlElements.XSI.TYPE, IXmlElements.V8UI.BORDER);
                }
                writer.writeElement("width", String.valueOf(borderDef.getWidth()));
                writer.writeStartElement(IXmlElements.V8UI.STYLE);
                writer.writeElement(IXmlElements.XSI.TYPE, IXmlElements.V8UI.CONTROL_BORDER_TYPE);
                writer.writeCharacters(borderDef.getStyle().getLiteral());
                writer.writeInlineEndElement();
                writer.writeEndElement();
            }
            else
            {
                throw new ExportException(Messages.BorderWriter_has_unknown_border_type);
            }
        }
    }
}
