/**
 *
 */
package ru.capralow.dt.modeling.md.internal.yaml.writer;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.EcoreUtil2;

import com._1c.g5.v8.dt.metadata.mdclass.ChartOfAccounts;
import com._1c.g5.v8.dt.metadata.mdclass.ExtDimensionAccountingFlag;
import com._1c.g5.v8.dt.metadata.mdclass.ExtDimensionType;
import com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.inject.Inject;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.md.yaml.IMetadataXmlElements;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;
import ru.capralow.dt.modeling.yaml.writer.ISpecifiedElementWriter;
import ru.capralow.dt.modeling.yaml.writer.ReferenceWriter;

public class ExtDimensionTypeAccountingFlagsWriter
    implements ISpecifiedElementWriter
{
    @Inject
    private ReferenceWriter referenceWriter;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws XMLStreamException, ExportException
    {
        if (feature != MdClassPackage.Literals.EXT_DIMENSION_TYPE__EXT_DIMENSION_ACCOUNTING_FLAGS)
            throw new IllegalArgumentException(String.format("Invalid feature %s, expected %s",
                new Object[] { feature, MdClassPackage.Literals.EXT_DIMENSION_TYPE__EXT_DIMENSION_ACCOUNTING_FLAGS }));
        QName elementQName = IMetadataXmlElements.XPR.ACCOUNTING_FLAGS;
        ExtDimensionType item = (ExtDimensionType)eObject;
        ChartOfAccounts chartOfAccounts = EcoreUtil2.getContainerOfType((EObject)item, ChartOfAccounts.class);
        EList<ExtDimensionAccountingFlag> accountingFlags = chartOfAccounts.getExtDimensionAccountingFlags();
        if (accountingFlags.isEmpty())
        {
            writer.writeEmptyElement(elementQName);
            return;
        }
        writer.writeStartElement(elementQName);
        for (ExtDimensionAccountingFlag accountingFlag : accountingFlags)
        {
            writer.writeStartElement(IMetadataXmlElements.XPR.FLAG);
            writer.writeElement("ref", this.referenceWriter.getReferenceRepresentation(item,
                MdClassPackage.Literals.EXT_DIMENSION_TYPE__EXT_DIMENSION_ACCOUNTING_FLAGS, accountingFlag));
            writer.writeCharacters(String.valueOf(item.getExtDimensionAccountingFlags().contains(accountingFlag)));
            writer.writeInlineEndElement();
        }
        writer.writeEndElement();
    }
}
