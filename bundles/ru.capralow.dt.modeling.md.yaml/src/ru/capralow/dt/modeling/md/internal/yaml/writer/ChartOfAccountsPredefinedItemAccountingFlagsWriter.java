/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.internal.yaml.writer;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.EcoreUtil2;

import com._1c.g5.v8.dt.metadata.mdclass.AccountingFlag;
import com._1c.g5.v8.dt.metadata.mdclass.ChartOfAccounts;
import com._1c.g5.v8.dt.metadata.mdclass.ChartOfAccountsPredefinedItem;
import com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.md.yaml.IMetadataYamlElements;
import ru.capralow.dt.modeling.yaml.writer.ISpecifiedElementWriter;
import ru.capralow.dt.modeling.yaml.writer.ReferenceWriter;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;

@Singleton
public class ChartOfAccountsPredefinedItemAccountingFlagsWriter
    implements ISpecifiedElementWriter
{
    @Inject
    private ReferenceWriter referenceWriter;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws ExportException
    {
        if (feature != MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED_ITEM__ACCOUNTING_FLAGS)
            throw new IllegalArgumentException(String.format("Invalid feature %s, expected %s",
                new Object[] { feature, MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED_ITEM__ACCOUNTING_FLAGS }));
        QName elementQName = IMetadataYamlElements.XPR.ACCOUNTING_FLAGS;
        ChartOfAccountsPredefinedItem item = (ChartOfAccountsPredefinedItem)eObject;
        ChartOfAccounts chartOfAccounts = EcoreUtil2.getContainerOfType((EObject)item, ChartOfAccounts.class);
        EList<AccountingFlag> accountingFlags = chartOfAccounts.getAccountingFlags();
        if (accountingFlags.isEmpty())
        {
            writer.writeEmptyElement(elementQName);
            return;
        }
        writer.writeStartElement(elementQName);
        for (AccountingFlag accountingFlag : accountingFlags)
        {
            writer.writeStartElement(IMetadataYamlElements.XPR.FLAG);
            writer.writeElement("ref", this.referenceWriter.getReferenceRepresentation(item,
                MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED_ITEM__ACCOUNTING_FLAGS, accountingFlag));
            writer.writeCharacters(String.valueOf(item.getAccountingFlags().contains(accountingFlag)));
            writer.writeInlineEndElement();
        }
        writer.writeEndElement();
    }
}
