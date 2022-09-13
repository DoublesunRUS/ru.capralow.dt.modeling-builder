/**
 *
 */
package ru.capralow.dt.modeling.md.internal.yaml.writer;

import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.core.model.ISymLinkConverter;
import com._1c.g5.v8.dt.core.naming.ISymbolicNameService;
import com._1c.g5.v8.dt.metadata.mdclass.ChartOfCalculationTypesPredefinedItem;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.md.yaml.IMetadataXmlElements;
import ru.capralow.dt.modeling.yaml.IQNameProvider;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;
import ru.capralow.dt.modeling.yaml.writer.ISpecifiedElementWriter;

@Singleton
public class PredefinedItemCalculationTypeWriter
    implements ISpecifiedElementWriter
{
    @Inject
    public IQNameProvider nameProvider;

    @Inject
    private ISymLinkConverter linkConverter;

    @Inject
    private ISymbolicNameService symbolicNameService;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws XMLStreamException, ExportException
    {
        if (!(feature instanceof EReference) || !feature.isMany())
            throw new IllegalArgumentException(String.format("Invalid feature %s", new Object[] { feature }));
        if (eObject != null)
        {
            List<? extends ChartOfCalculationTypesPredefinedItem> calculationTypes =
                (List<? extends ChartOfCalculationTypesPredefinedItem>)eObject.eGet(feature);
            if (!calculationTypes.isEmpty())
            {
                QName featureQName = this.nameProvider.getElementQName(feature);
                writer.writeStartElement(featureQName);
                for (ChartOfCalculationTypesPredefinedItem value : calculationTypes)
                    writePredefinedCalculationType(writer, eObject, (EReference)feature, (EObject)value, writeEmpty);
                writer.writeEndElement();
            }
        }
    }

    private void writePredefinedCalculationType(YamlStreamWriter writer, EObject eObject, EReference feature,
        EObject value, boolean writeEmpty) throws XMLStreamException
    {
        if (value != null)
        {
            String ref = this.symbolicNameService.generateSymbolicName(value, eObject, feature);
            if (!Strings.isNullOrEmpty(ref))
            {
                writer.writeTextElement(IMetadataXmlElements.XPR.CALCULATION_TYPE,
                    this.linkConverter.convert(eObject, feature, ref));
            }
            else if (writeEmpty)
            {
                writer.writeEmptyElement(IMetadataXmlElements.XPR.CALCULATION_TYPE);
            }
        }
        else if (writeEmpty)
        {
            writer.writeEmptyElement(IMetadataXmlElements.XPR.CALCULATION_TYPE);
        }
    }
}
