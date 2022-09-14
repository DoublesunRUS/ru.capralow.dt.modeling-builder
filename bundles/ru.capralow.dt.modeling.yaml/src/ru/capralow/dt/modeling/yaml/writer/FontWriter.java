/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.yaml.writer;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import com._1c.g5.v8.bm.core.BmUriUtil;
import com._1c.g5.v8.dt.core.model.ISymLinkConverter;
import com._1c.g5.v8.dt.core.naming.ISymbolicNameService;
import com._1c.g5.v8.dt.mcore.AutoFont;
import com._1c.g5.v8.dt.mcore.Font;
import com._1c.g5.v8.dt.mcore.FontDef;
import com._1c.g5.v8.dt.mcore.FontRef;
import com._1c.g5.v8.dt.mcore.McorePackage;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.common.base.Strings;
import com.google.inject.Inject;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.yaml.IQNameProvider;
import ru.capralow.dt.modeling.yaml.IYamlElements;

public class FontWriter
    implements ISpecifiedElementWriter
{
    private static final String REF = "ref";

    private static final String FACE_NAME = "faceName";

    private static final String HEIGHT = "height";

    private static final String BOLD = "bold";

    private static final String ITALIC = "italic";

    private static final String UNDERLINE = "underline";

    private static final String STRIKEOUT = "strikeout";

    private static final String KIND = "kind";

    private static final String SCALE = "scale";

    private static final String ABSOLUTE_KIND = "Absolute";

    private static final String WINDOWS_FONT_KIND = "WindowsFont";

    private static final String STYLE_ITEM_KIND = "StyleItem";

    private static final String AUTO_FONT_KIND = "AutoFont";

    private static final String SYSTEM_OBJECT_NAME_PREFIX = "System";

    private static final String STYLE_OBJECT_NAME_PREFIX = "Style";

    private static final Predicate<String> IS_VALID_FONT_REF = Pattern.compile(
        "(?i)^(:?\\d+(:[a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})?|Style[\\.:](:?\\d+|[a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8}))?$")
        .asPredicate()
        .negate();

    @Inject
    private ISymbolicNameService symbolicNameService;

    @Inject
    private ISymLinkConverter linkConverter;

    @Inject
    private IQNameProvider nameManager;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws ExportException
    {
        QName elementQName = this.nameManager.getElementQName(feature);
        if (feature.isMany() || feature.getEType() != McorePackage.Literals.FONT)
        {
            throw new IllegalArgumentException(String.format("Invalid feature type in %s", new Object[] { feature }));
        }
        Font font = (Font)eObject.eGet(feature);
        writeFont(writer, feature, font, elementQName);
    }

    public void writeFont(YamlStreamWriter writer, EStructuralFeature feature, Font font, QName elementQName)
        throws ExportException
    {
        boolean isValue = feature.getEType() == McorePackage.Literals.VALUE;
        if (font instanceof FontRef)
        {
            FontRef fontRef = (FontRef)font;
            Font ref = fontRef.getFont();
            if (ref != null)
            {
//                writer.writeEmptyElement(elementQName);
                if (isValue)
                {
                    writer.writeElement(IYamlElements.XSI.TYPE, IYamlElements.V8UI.FONT);
                }
                String refText = this.linkConverter.convert(font, McorePackage.Literals.FONT_REF__FONT,
                    this.symbolicNameService.generateSymbolicName(ref, font, McorePackage.Literals.FONT_REF__FONT));
                if (!Strings.isNullOrEmpty(refText))
                {
//                    writeNamespace(writer, ref);
                    writer.writeElement("ref", refText);
                }
                else
                {
                    throw new ExportException(Messages.FontWriter_has_unknown_font_ref);
                }
                if (fontRef.isSetFaceName())
                {
                    writer.writeElement("faceName", fontRef.faceName());
                }
                if (fontRef.isSetHeight())
                {
                    writer.writeElement("height", getFontHeightRepresentation(fontRef));
                }
                if (fontRef.isSetBold())
                {
                    writer.writeElement("bold", String.valueOf(fontRef.isBold()));
                }
                if (fontRef.isSetItalic())
                {
                    writer.writeElement("italic", String.valueOf(fontRef.isItalic()));
                }
                if (fontRef.isSetUnderline())
                {
                    writer.writeElement("underline", String.valueOf(fontRef.isUnderline()));
                }
                if (fontRef.isSetStrikeout())
                {
                    writer.writeElement("strikeout", String.valueOf(fontRef.isStrikeout()));
                }
                if (ref.eIsProxy())
                {
                    String kind = null;
                    URI uri = ((InternalEObject)ref).eProxyURI();
                    String fontName = uri.fragment();
                    if (BmUriUtil.isBmUri(uri))
                    {
                        fontName = this.symbolicNameService.convertUriToCommonSymbolicName(uri);
                    }
                    else if ("unresolved".equals(uri.scheme()))
                    {
                        fontName = uri.segment(0);
                    }
                    if (fontName != null)
                    {
                        if (fontName.startsWith("System"))
                        {
                            kind = "WindowsFont";
                        }
                        else if (fontName.startsWith("Style") && IS_VALID_FONT_REF.test(fontName))
                        {
                            kind = "StyleItem";
                        }
                        else if (!IS_VALID_FONT_REF.test(fontName))
                        {
                            kind = "AutoFont";
                        }
                    }
                    if (kind != null)
                    {
                        writer.writeElement("kind", kind);
                    }
                    else
                    {
                        throw new ExportException(Messages.FontWriter_has_unknown_font_ref);
                    }
                }
                else if (ref instanceof com._1c.g5.v8.dt.platform.model.SystemFont)
                {
                    writer.writeElement("kind", "WindowsFont");
                }
                else if (ref instanceof com._1c.g5.v8.dt.platform.model.PlatformFontRef
                    || ref instanceof com._1c.g5.v8.dt.mcore.StyleFont)
                {
                    writer.writeElement("kind", "StyleItem");
                }
                else
                {
                    throw new ExportException(Messages.FontWriter_has_unknown_font_ref);
                }
                if (fontRef.isSetScale())
                {
                    writer.writeElement("scale", String.valueOf(fontRef.scale()));
                }
            }
            else
            {
                throw new ExportException(Messages.FontWriter_has_empty_font_ref);
            }
        }
        else if (font instanceof FontDef)
        {
            FontDef fontDef = (FontDef)font;
//            writer.writeEmptyElement(elementQName);
            if (isValue)
            {
                writer.writeElement(IYamlElements.XSI.TYPE, IYamlElements.V8UI.FONT);
            }
            if (fontDef.getFaceName() != null)
            {
                writer.writeElement("faceName", fontDef.faceName());
            }
            writer.writeElement("height", getFontHeightRepresentation(fontDef));
            writer.writeElement("bold", String.valueOf(fontDef.isBold()));
            writer.writeElement("italic", String.valueOf(fontDef.isItalic()));
            writer.writeElement("underline", String.valueOf(fontDef.isUnderline()));
            writer.writeElement("strikeout", String.valueOf(fontDef.isStrikeout()));
            writer.writeElement("kind", "Absolute");
            writer.writeElement("scale", String.valueOf(fontDef.scale()));
        }
        else if (font instanceof AutoFont)
        {
            AutoFont autoFont = (AutoFont)font;
//            writer.writeEmptyElement(elementQName);
            if (isValue)
            {
                writer.writeElement(IYamlElements.XSI.TYPE, IYamlElements.V8UI.FONT);
            }
            if (autoFont.isSetFaceName())
            {
                writer.writeElement("faceName", autoFont.faceName());
            }
            if (autoFont.isSetHeight())
            {
                writer.writeElement("height", getFontHeightRepresentation(autoFont));
            }
            if (autoFont.isSetBold())
            {
                writer.writeElement("bold", String.valueOf(autoFont.isBold()));
            }
            if (autoFont.isSetItalic())
            {
                writer.writeElement("italic", String.valueOf(autoFont.isItalic()));
            }
            if (autoFont.isSetUnderline())
            {
                writer.writeElement("underline", String.valueOf(autoFont.isUnderline()));
            }
            if (autoFont.isSetStrikeout())
            {
                writer.writeElement("strikeout", String.valueOf(autoFont.isStrikeout()));
            }
            writer.writeElement("kind", "AutoFont");
            if (autoFont.isSetStrikeout())
            {
                writer.writeElement("scale", String.valueOf(autoFont.scale()));
            }
        }
        else if (font != null)
        {
            throw new ExportException(Messages.FontWriter_has_unknown_font_type);
        }
    }

    private String getFontHeightRepresentation(Font font)
    {
        double h = Double.valueOf(font.heightF()).doubleValue();
        String value = (h == Math.ceil(h)) ? String.valueOf(font.height()) : String.valueOf(font.heightF());
        return value;
    }

}
