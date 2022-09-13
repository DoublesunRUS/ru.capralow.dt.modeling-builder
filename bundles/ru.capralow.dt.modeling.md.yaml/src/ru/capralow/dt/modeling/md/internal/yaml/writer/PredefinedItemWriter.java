/**
 *
 */
package ru.capralow.dt.modeling.md.internal.yaml.writer;

import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage;
import com._1c.g5.v8.dt.metadata.mdclass.PredefinedItem;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.md.yaml.IMetadataXmlElements;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;
import ru.capralow.dt.modeling.yaml.writer.ISpecifiedElementWriter;

public class PredefinedItemWriter
    implements ISpecifiedElementWriter
{
    private static final ImmutableList<EStructuralFeature> CATALOG_PREDEFINED_ITEM_FEATURE_LIST = ImmutableList.of(
        MdClassPackage.Literals.PREDEFINED_ITEM__NAME, MdClassPackage.Literals.CATALOG_PREDEFINED_ITEM__CODE,
        MdClassPackage.Literals.PREDEFINED_ITEM__DESCRIPTION,
        MdClassPackage.Literals.CATALOG_PREDEFINED_ITEM__IS_FOLDER, MdClassPackage.Literals.PREDEFINED_ITEM__EXTENSION,
        MdClassPackage.Literals.CATALOG_PREDEFINED_ITEM__CONTENT);

    private static final ImmutableList<EStructuralFeature> CHART_OF_ACCOUNTS_PREDEFINED_ITEM_FEATURE_LIST =
        ImmutableList.of(MdClassPackage.Literals.PREDEFINED_ITEM__NAME,
            MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED_ITEM__CODE,
            MdClassPackage.Literals.PREDEFINED_ITEM__DESCRIPTION,
            MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED_ITEM__ACCOUNT_TYPE,
            MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED_ITEM__OFF_BALANCE,
            MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED_ITEM__ORDER,
            MdClassPackage.Literals.PREDEFINED_ITEM__EXTENSION,
            MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED_ITEM__ACCOUNTING_FLAGS,
            MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED_ITEM__EXT_DIMENSION_TYPES,
            MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED_ITEM__CHILD_ITEMS);

    private static final ImmutableList<EStructuralFeature> CHART_OF_CHARACTERISTIC_TYPES_PREDEFINED_ITEM_FEATURE_LIST =
        ImmutableList.of(MdClassPackage.Literals.PREDEFINED_ITEM__NAME,
            MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES_PREDEFINED_ITEM__CODE,
            MdClassPackage.Literals.PREDEFINED_ITEM__DESCRIPTION,
            MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES_PREDEFINED_ITEM__TYPE,
            MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES_PREDEFINED_ITEM__IS_FOLDER,
            MdClassPackage.Literals.PREDEFINED_ITEM__EXTENSION,
            MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES_PREDEFINED_ITEM__CONTENT);

    private static final ImmutableList<EStructuralFeature> CHART_OF_CALCULATION_TYPES_PREDEFINED_ITEM_FEATURE_LIST =
        ImmutableList.of(MdClassPackage.Literals.PREDEFINED_ITEM__NAME,
            MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES_PREDEFINED_ITEM__CODE,
            MdClassPackage.Literals.PREDEFINED_ITEM__DESCRIPTION,
            MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES_PREDEFINED_ITEM__ACTION_PERIOD_IS_BASE,
            MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES_PREDEFINED_ITEM__BASE,
            MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES_PREDEFINED_ITEM__LEADING,
            MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES_PREDEFINED_ITEM__DISPLACED,
            MdClassPackage.Literals.PREDEFINED_ITEM__EXTENSION);

    @Inject
    private MetadataSmartFeatureWriter featureWriter;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws XMLStreamException, ExportException
    {
        if (feature != MdClassPackage.Literals.CATALOG_PREDEFINED__ITEMS
            && feature != MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED__ITEMS
            && feature != MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES_PREDEFINED__ITEMS
            && feature != MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES_PREDEFINED__ITEMS
            && feature != MdClassPackage.Literals.CATALOG_PREDEFINED_ITEM__CONTENT
            && feature != MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED_ITEM__CHILD_ITEMS
            && feature != MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES_PREDEFINED_ITEM__CONTENT)
            throw new IllegalArgumentException(String.format("Invalid feature %s", new Object[] { feature }));
        List<PredefinedItem> predefinedItems = (List<PredefinedItem>)eObject.eGet(feature);
        boolean isContent = !(feature != MdClassPackage.Literals.CATALOG_PREDEFINED_ITEM__CONTENT
            && feature != MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES_PREDEFINED_ITEM__CONTENT
            && feature != MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED_ITEM__CHILD_ITEMS);
        if (isContent && !predefinedItems.isEmpty())
            writer.writeStartElement(IMetadataXmlElements.XPR.CHILD_ITEMS);
        for (PredefinedItem predefinedItem : predefinedItems)
        {
            writer.writeStartElement(IMetadataXmlElements.XPR.ITEM);
            writer.writeElement("id", predefinedItem.getId().toString());
            for (EStructuralFeature structuralFeature : getFeaturesList(eObject.eClass()))
                writePredefinedItemProperty(writer, predefinedItem, structuralFeature, true, version);
            writer.writeEndElement();
        }
        if (isContent && !predefinedItems.isEmpty())
            writer.writeEndElement();
    }

    protected void writePredefinedItemProperty(YamlStreamWriter writer, PredefinedItem predefinedItem,
        EStructuralFeature feature, boolean writeEmpty, Version version) throws XMLStreamException, ExportException
    {
        if (!isFeatureOnlyForExtension(feature, version) && isFeatureSupportedByVersion(feature, version))
            this.featureWriter.write(writer, predefinedItem, feature, writeEmpty, version);
    }

    protected boolean isFeatureSupportedByVersion(EStructuralFeature feature, Version version)
    {
        if (version.isLessThan(Version.V8_3_21))
            if (feature == MdClassPackage.Literals.PREDEFINED_ITEM__EXTENSION)
                return false;
        return true;
    }

    private boolean isFeatureOnlyForExtension(EStructuralFeature feature, Version version)
    {
        return (feature == MdClassPackage.Literals.PREDEFINED_ITEM__EXTENSION);
    }

    private List<EStructuralFeature> getFeaturesList(EClass eClass)
    {
        if (eClass == MdClassPackage.Literals.CATALOG_PREDEFINED
            || eClass == MdClassPackage.Literals.CATALOG_PREDEFINED_ITEM)
            return CATALOG_PREDEFINED_ITEM_FEATURE_LIST;
        if (eClass == MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED
            || eClass == MdClassPackage.Literals.CHART_OF_ACCOUNTS_PREDEFINED_ITEM)
            return CHART_OF_ACCOUNTS_PREDEFINED_ITEM_FEATURE_LIST;
        if (eClass == MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES_PREDEFINED
            || eClass == MdClassPackage.Literals.CHART_OF_CHARACTERISTIC_TYPES_PREDEFINED_ITEM)
            return CHART_OF_CHARACTERISTIC_TYPES_PREDEFINED_ITEM_FEATURE_LIST;
        if (eClass == MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES_PREDEFINED
            || eClass == MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES_PREDEFINED_ITEM)
            return CHART_OF_CALCULATION_TYPES_PREDEFINED_ITEM_FEATURE_LIST;
        throw new AssertionError("Unknown predefined container");
    }
}
