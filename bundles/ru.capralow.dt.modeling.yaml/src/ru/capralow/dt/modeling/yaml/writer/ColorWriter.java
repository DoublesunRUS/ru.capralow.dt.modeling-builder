/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.yaml.writer;

import java.util.Collection;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.bm.core.IBmObject;
import com._1c.g5.v8.bm.core.IBmTransaction;
import com._1c.g5.v8.bm.integration.AbstractBmTask;
import com._1c.g5.v8.bm.integration.IBmModel;
import com._1c.g5.v8.bm.integration.IBmTask;
import com._1c.g5.v8.dt.core.model.ISymLinkConverter;
import com._1c.g5.v8.dt.core.naming.ISymbolicNameService;
import com._1c.g5.v8.dt.core.platform.IBmModelManager;
import com._1c.g5.v8.dt.core.platform.IV8Project;
import com._1c.g5.v8.dt.core.platform.IV8ProjectManager;
import com._1c.g5.v8.dt.mcore.Color;
import com._1c.g5.v8.dt.mcore.ColorRef;
import com._1c.g5.v8.dt.mcore.McorePackage;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.inject.Inject;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.yaml.IQNameProvider;
import ru.capralow.dt.modeling.yaml.IXmlElements;

public class ColorWriter
    implements ISpecifiedElementWriter
{
    @Inject
    private ISymLinkConverter linkConverter;

    @Inject
    private ISymbolicNameService symbolicNameService;

    @Inject
    private IQNameProvider nameManager;

    @Inject
    private IV8ProjectManager v8ProjectManager;

    @Inject
    private IBmModelManager bmModelManager;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws XMLStreamException, ExportException
    {
        QName elementQName = this.nameManager.getElementQName(feature);
        if (feature.getEType() != McorePackage.Literals.COLOR)
        {
            throw new IllegalArgumentException(String.format("Invalid feature type in %s", new Object[] { feature }));
        }
        if (feature.isMany())
        {
            Collection<?> colors = (Collection)eObject.eGet(feature);
            for (Object color : colors)
            {
                writeColor(writer, feature, (Color)color, writeEmpty, elementQName);
            }
        }
        else
        {
            Color color = (Color)eObject.eGet(feature);
            writeColor(writer, feature, color, writeEmpty, elementQName);
        }
    }

    public void writeColor(YamlStreamWriter writer, EStructuralFeature feature, Color color, boolean writeEmpty,
        QName elementQName) throws XMLStreamException, ExportException
    {
        boolean isValue = feature.getEType() == McorePackage.Literals.VALUE;
        if (color != null)
        {
            String text = getColorRepresentation(color);
            if (text != null)
            {
                writeElement(writer, elementQName, text, isValue);
            }
            else
            {
                throw new ExportException(Messages.ColorWriter_has_unknown_color_type);
            }
        }
        else if (writeEmpty)
        {
            writeElement(writer, elementQName, "auto", isValue);
        }
    }

    public String getColorRepresentation(Color color)
    {
        if (color instanceof ColorRef)
        {
            Color colorRef = ((ColorRef)color).getColor();
            if (colorRef != null)
            {
                if (skipColorRef((ColorRef)color))
                {
                    return "0";
                }
                return this.linkConverter.convert(color, McorePackage.Literals.COLOR_REF__COLOR,
                    this.symbolicNameService.generateSymbolicName(colorRef, color,
                        McorePackage.Literals.COLOR_REF__COLOR));
            }
        }
        if (color instanceof com._1c.g5.v8.dt.mcore.ColorDef)
        {
            return String.format("#%s%s%s",
                new Object[] { intToHex(color.red()), intToHex(color.green()), intToHex(color.blue()) });
        }
        return null;
    }

    private boolean skipColorRef(ColorRef color)
    {
        IV8Project project = this.v8ProjectManager.getProject(color);
        if (project instanceof com._1c.g5.v8.dt.core.platform.IExtensionProject)
        {
            final Color colorRef = color.getColor();
            if (colorRef.eIsProxy())
            {
                return true;
            }
            if (colorRef instanceof com._1c.g5.v8.dt.mcore.StyleColor)
            {
                Boolean result;
                IBmModel bmModel = this.bmModelManager.getModel(project.getDtProject());
                IBmTransaction transaction = bmModel.getEngine().getCurrentTransaction();
                if (transaction != null)
                {
                    result = objectExists(transaction, colorRef);
                }
                else
                {
                    result = (Boolean)bmModel
                        .executeReadonlyTask((IBmTask)new AbstractBmTask<Boolean>("Check whether the top object exists")
                        {
                            @Override
                            public Boolean execute(IBmTransaction transaction, IProgressMonitor progressMonitor)
                            {
                                return ColorWriter.objectExists(transaction, colorRef);
                            }
                        });
                }
                return result.booleanValue();
            }
        }
        return false;
    }

    private static Boolean objectExists(IBmTransaction transaction, Color colorRef)
    {
        IBmObject iBmObject = transaction.getTopObjectByFqn(((IBmObject)colorRef.eContainer()).bmGetFqn());
        return (iBmObject == null) ? Boolean.valueOf(true) : Boolean.valueOf(false);
    }

    private static void writeElement(YamlStreamWriter writer, QName elementQName, String text, boolean isValue)
        throws XMLStreamException, ExportException
    {
        if (isValue)
        {
            writer.writeStartElement(elementQName);
            writer.writeElement(IXmlElements.XSI.TYPE, IXmlElements.V8UI.COLOR);
            writer.writeCharacters(text);
            writer.writeInlineEndElement();
        }
        else
        {
            writer.writeTextElement(elementQName, text);
        }
    }

    private static String intToHex(int color)
    {
        String hex = Integer.toHexString(color).toUpperCase();
        return (hex.length() == 1) ? (String.valueOf('0') + hex) : hex;
    }
}
