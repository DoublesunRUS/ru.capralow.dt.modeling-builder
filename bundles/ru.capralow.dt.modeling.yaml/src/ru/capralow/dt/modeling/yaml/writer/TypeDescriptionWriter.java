/**
 *
 */
package ru.capralow.dt.modeling.yaml.writer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.form.model.FormPackage;
import com._1c.g5.v8.dt.mcore.BinaryQualifiers;
import com._1c.g5.v8.dt.mcore.DateQualifiers;
import com._1c.g5.v8.dt.mcore.McorePackage;
import com._1c.g5.v8.dt.mcore.NumberQualifiers;
import com._1c.g5.v8.dt.mcore.StringQualifiers;
import com._1c.g5.v8.dt.mcore.TypeDescription;
import com._1c.g5.v8.dt.mcore.TypeItem;
import com._1c.g5.v8.dt.mcore.util.McoreUtil;
import com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage;
import com._1c.g5.v8.dt.platform.version.Version;
import com._1c.g5.v8.dt.xml.XdtoTypeMap;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.yaml.IQNameProvider;
import ru.capralow.dt.modeling.yaml.IXmlElements;

@Singleton
public class TypeDescriptionWriter
    implements ISpecifiedElementWriter
{
    private static final Set<String> NOT_TYPE_SET_NAMES = ImmutableSet.of("AccountingRegisterManager",
        "AccumulationRegisterManager", "BusinessProcessManager", "CalculationRegisterManager", "CatalogManager",
        "ChartOfAccountsManager",
        new String[] { "ChartOfCalculationTypesManager", "ChartOfCharacteristicTypesManager", "DataProcessorManager",
            "DocumentManager", "DocumentJournalManager", "EnumManager", "ExchangePlanManager", "FilterCriterionManager",
            "InformationRegisterManager", "ReportManager", "SettingsStorageManager", "TaskManager" });

    private static final Set<EReference> EXT_ATTRIBUTES_CONTAINER_FEATURES = ImmutableSet.of(
        FormPackage.Literals.FORM__ATTRIBUTES, MdClassPackage.Literals.EXTERNAL_DATA_PROCESSOR__ATTRIBUTES,
        MdClassPackage.Literals.EXTERNAL_REPORT__ATTRIBUTES);

    @Inject
    private IQNameProvider nameProvider;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws XMLStreamException, ExportException
    {
        if (feature.isMany() || feature.getEType() != McorePackage.Literals.TYPE_DESCRIPTION)
        {
            throw new IllegalArgumentException(String.format("Invalid feature type in %s", new Object[] { feature }));
        }
        if (eObject == null)
        {
            return;
        }
        QName featureQName = this.nameProvider.getElementQName(feature);
        Object value = eObject.eGet(feature);
        if (!isEmptyTypeDescription((TypeDescription)value))
        {
            if (eObject instanceof com._1c.g5.v8.dt.form.model.ValueListExtInfo)
            {
                writeTypeDescription(writer, (TypeDescription)value);
            }
            else
            {
                writer.writeStartElement(featureQName);
                writeTypeDescription(writer, (TypeDescription)value);
                writer.writeEndElement();
            }
        }
        else if (writeEmpty)
        {
            writer.writeEmptyElement(featureQName);
        }
    }

    public void writeTypeDescription(YamlStreamWriter writer, TypeDescription value)
        throws XMLStreamException, ExportException
    {
        writeTypes(writer, value);
        NumberQualifiers numberQualifiers = value.getNumberQualifiers();
        if (numberQualifiers != null)
        {
            writer.writeStartElement(IXmlElements.V8.NUMBER_QUALIFIERS);
            writer.writeStartElement(IXmlElements.V8.DIGITS);
            writer.writeCharacters(String.valueOf(numberQualifiers.getPrecision()));
            writer.writeInlineEndElement();
            writer.writeStartElement(IXmlElements.V8.FRACTION_DIGITS);
            writer.writeCharacters(String.valueOf(numberQualifiers.getScale()));
            writer.writeInlineEndElement();
            writer.writeStartElement(IXmlElements.V8.ALLOWED_SIGN);
            writer.writeCharacters(numberQualifiers.isNonNegative() ? "Nonnegative" : "Any");
            writer.writeInlineEndElement();
            writer.writeEndElement();
        }
        StringQualifiers stringQualifiers = value.getStringQualifiers();
        if (stringQualifiers != null)
        {
            writer.writeStartElement(IXmlElements.V8.STRING_QUALIFIERS);
            writer.writeStartElement(IXmlElements.V8.LENGTH);
            writer.writeCharacters(String.valueOf(stringQualifiers.getLength()));
            writer.writeInlineEndElement();
            writer.writeStartElement(IXmlElements.V8.ALLOWED_LENGTH);
            writer.writeCharacters(stringQualifiers.isFixed() ? "Fixed" : "Variable");
            writer.writeInlineEndElement();
            writer.writeEndElement();
        }
        DateQualifiers dateQualifiers = value.getDateQualifiers();
        if (dateQualifiers != null)
        {
            writer.writeStartElement(IXmlElements.V8.DATE_QUALIFIERS);
            writer.writeStartElement(IXmlElements.V8.DATE_FRACTIONS);
            writer.writeCharacters(dateQualifiers.getDateFractions().toString());
            writer.writeInlineEndElement();
            writer.writeEndElement();
        }
        BinaryQualifiers binaryQualifiers = value.getBinaryQualifiers();
        if (binaryQualifiers != null)
        {
            writer.writeStartElement(IXmlElements.V8.BINARY_QUALIFIERS);
            writer.writeStartElement(IXmlElements.V8.LENGTH);
            writer.writeCharacters(String.valueOf(binaryQualifiers.getLength()));
            writer.writeInlineEndElement();
            writer.writeStartElement(IXmlElements.V8.ALLOWED_LENGTH);
            writer.writeCharacters(binaryQualifiers.isFixed() ? "Fixed" : "Variable");
            writer.writeInlineEndElement();
            writer.writeEndElement();
        }
    }

    public boolean isEmptyTypeDescription(TypeDescription value)
    {
        if (value != null)
        {
            EList<TypeItem> types = value.getTypes();
            boolean isNoTypes =
                !(!types.isEmpty() && (types.size() != 1 || !"Arbitrary".equals(McoreUtil.getTypeName(types.get(0)))));
            return isNoTypes && value.getStringQualifiers() == null && value.getNumberQualifiers() == null
                && value.getDateQualifiers() == null && value.getBinaryQualifiers() == null;
        }
        return true;
    }

    private void writeTypes(YamlStreamWriter writer, TypeDescription typeDescription)
        throws XMLStreamException, ExportException
    {
        List<TypeItem> typeSets = new ArrayList<>();
        for (TypeItem item : typeDescription.getTypes())
        {
            if (item instanceof com._1c.g5.v8.dt.mcore.Type || NOT_TYPE_SET_NAMES.contains(McoreUtil.getTypeName(item)))
            {
                writeTypeItem(writer, item, typeDescription, IXmlElements.V8.TYPE);
                continue;
            }
            typeSets.add(item);
        }
        for (TypeItem typeSet : typeSets)
        {
            writeTypeItem(writer, typeSet, typeDescription, IXmlElements.V8.TYPE_SET);
        }
    }

    private void writeTypeItem(YamlStreamWriter writer, TypeItem typeItem, TypeDescription typeDescription,
        QName elementName) throws XMLStreamException, ExportException
    {
        if (!"Arbitrary".equals(McoreUtil.getTypeName(typeItem)))
        {
            writer.writeStartElement(elementName);
            QName typeName = XdtoTypeMap.INSTANCE.typeToXmlName(typeItem);
            NamespaceContext namespaceContext = writer.getYamlWriter().getNamespaceContext();
            String prefix = namespaceContext.getPrefix(typeName.getNamespaceURI());
            if (prefix == null)
            {
                prefix = writer.getNewPrefix(typeName.getNamespaceURI());
                writer.writeElement("xmlns:" + prefix, typeName.getNamespaceURI());
            }
            String localPartName = getLocalPartName(typeItem, typeName, typeDescription);
            writer.writeCharacters(String.valueOf(prefix) + ':' + localPartName);
            writer.writeInlineEndElement();
        }
    }

    private static String getLocalPartName(TypeItem typeItem, QName typeName, TypeDescription typeDescription)
    {
        String localPart = typeName.getLocalPart();
        if (typeDescription.eContainer() != null
            && EXT_ATTRIBUTES_CONTAINER_FEATURES.contains(typeDescription.eContainer().eContainingFeature()))
        {
            if (McoreUtil.getTypeName(typeItem).startsWith("ExternalReport."))
            {
                localPart = localPart.replace("ExternalReport.", "ExternalReportObject.");
            }
            else if (McoreUtil.getTypeName(typeItem).startsWith("ExternalDataProcessor."))
            {
                localPart = localPart.replace("ExternalDataProcessor.", "ExternalDataProcessorObject.");
            }
        }
        return localPart;
    }
}
