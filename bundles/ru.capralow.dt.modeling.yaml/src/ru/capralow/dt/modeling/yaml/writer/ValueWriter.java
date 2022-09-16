/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.yaml.writer;

import java.util.Map;

import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.form.model.AccountTypeValue;
import com._1c.g5.v8.dt.form.model.FormPackage;
import com._1c.g5.v8.dt.mcore.BinaryValue;
import com._1c.g5.v8.dt.mcore.BooleanValue;
import com._1c.g5.v8.dt.mcore.BorderValue;
import com._1c.g5.v8.dt.mcore.ColorValue;
import com._1c.g5.v8.dt.mcore.DateValue;
import com._1c.g5.v8.dt.mcore.EnumValue;
import com._1c.g5.v8.dt.mcore.FixedArrayValue;
import com._1c.g5.v8.dt.mcore.FontValue;
import com._1c.g5.v8.dt.mcore.IrresolvableReferenceValue;
import com._1c.g5.v8.dt.mcore.McorePackage;
import com._1c.g5.v8.dt.mcore.NumberValue;
import com._1c.g5.v8.dt.mcore.ReferenceValue;
import com._1c.g5.v8.dt.mcore.StandardPeriodValue;
import com._1c.g5.v8.dt.mcore.StringValue;
import com._1c.g5.v8.dt.mcore.SysEnumValue;
import com._1c.g5.v8.dt.mcore.TypeDescription;
import com._1c.g5.v8.dt.mcore.TypeDescriptionValue;
import com._1c.g5.v8.dt.mcore.Value;
import com._1c.g5.v8.dt.mcore.ValueList;
import com._1c.g5.v8.dt.metadata.common.ChartLineTypeValue;
import com._1c.g5.v8.dt.metadata.common.CommonPackage;
import com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage;
import com._1c.g5.v8.dt.platform.version.Version;
import com._1c.g5.v8.dt.xml.XdtoTypeMap;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.yaml.IQNameProvider;

public class ValueWriter
    implements ISpecifiedElementWriter
{
    private static final ImmutableList<EReference> AS_DESIGN_TIME_REF = ImmutableList.of(
        MdClassPackage.Literals.STANDARD_ATTRIBUTE__FILL_VALUE, MdClassPackage.Literals.DB_OBJECT_ATTRIBUTE__FILL_VALUE,
        MdClassPackage.Literals.COMMON_ATTRIBUTE__FILL_VALUE, MdClassPackage.Literals.ACCOUNTING_FLAG__FILL_VALUE,
        MdClassPackage.Literals.EXT_DIMENSION_ACCOUNTING_FLAG__FILL_VALUE,
        MdClassPackage.Literals.INFORMATION_REGISTER_RESOURCE__FILL_VALUE,
        MdClassPackage.Literals.INFORMATION_REGISTER_ATTRIBUTE__FILL_VALUE,
        MdClassPackage.Literals.INFORMATION_REGISTER_DIMENSION__FILL_VALUE,
        MdClassPackage.Literals.REPORT_TABULAR_SECTION_ATTRIBUTE__FILL_VALUE,
        MdClassPackage.Literals.DATA_PROCESSOR_TABULAR_SECTION_ATTRIBUTE__FILL_VALUE,
        MdClassPackage.Literals.ADDRESSING_ATTRIBUTE__FILL_VALUE,
        MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__TYPES_FILTER_VALUE,
        new EReference[] { FormPackage.Literals.FORM_CHOICE_LIST_DES_TIME_VALUE__VALUE,
            CommonPackage.Literals.CHOICE_PARAMETER__VALUE, McorePackage.Literals.VALUE_LIST__VALUES,
            McorePackage.Literals.FIXED_ARRAY_VALUE__VALUES });

    @Inject
    protected ReferenceWriter referenceWriter;

    @Inject
    private IQNameProvider nameManager;

    @Inject
    @Named(ISpecifiedElementWriter.SMART_ELEMENT_WRITER)
    private ISpecifiedElementWriter featureWriter;

    @Inject
    private Provider<TypeDescriptionWriter> typeDescriptionWriterProvider;

    @Inject
    private Provider<StandardPeriodWriter> standardPeriodWriterProvider;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws ExportException
    {
        if (feature == MdClassPackage.Literals.WEB_SERVICE__XDTO_PACKAGES)
        {
            throw new IllegalArgumentException(String.format("Invalid object feature %s", new Object[] { feature }));
        }
        QName elementName = nameManager.getElementQName(feature);
        if (eObject != null)
        {
            Object valueObject = eObject.eGet(feature);
            if (valueObject != null && !(valueObject instanceof Value))
            {
                throw new IllegalArgumentException(
                    String.format("Invalid value object %s", new Object[] { valueObject }));
            }
            writeValue(writer, valueObject, elementName, writeEmpty, feature, version);
        }
        else if (writeEmpty)
        {
//            writer.writeEmptyElement(elementName);
        }
    }

    public void writeValue(YamlStreamWriter writer, Object valueObject, QName elementName, boolean writeEmpty,
        EStructuralFeature feature, Version version) throws ExportException
    {
        if (valueObject == null)
        {
            return;
        }
        if (valueObject instanceof com._1c.g5.v8.dt.mcore.UndefinedValue
            || valueObject instanceof com._1c.g5.v8.dt.mcore.NullValue)
        {
//            writer.writeEmptyElement(elementName);
            writer.writeElement("XSI.NIL", "true");
        }
        else if (valueObject instanceof BooleanValue)
        {
            Map<String, Object> valueGroup = writer.addGroup(elementName);

//            writer.writeStartElement(elementName);
            writer.writeElement("XSI.TYPE", "XS.BOOLEAN");
//            writer.writeCharacters(Boolean.toString(((BooleanValue)valueObject).isValue()));
//            writer.writeInlineEndElement();
        }
        else if (valueObject instanceof NumberValue)
        {
//            writer.writeStartElement(elementName);
            writer.writeElement("XSI.TYPE", "XS.DECIMAL");
//            writer.writeCharacters(((NumberValue)valueObject).getValue().toString());
//            writer.writeInlineEndElement();
        }
        else if (valueObject instanceof StringValue)
        {
            String value = ((StringValue)valueObject).getValue();
            if (Strings.isNullOrEmpty(value))
            {
//                writer.writeEmptyElement(elementName);
                writer.writeElement("XSI.TYPE", "XS.STRING");
            }
            else
            {
//                writer.writeStartElement(elementName);
                writer.writeElement("XSI.TYPE", "XS.STRING");
//                writer.writeCharacters(value);
//                writer.writeInlineEndElement();
            }
        }
        else if (valueObject instanceof DateValue)
        {
//            writer.writeStartElement(elementName);
            writer.writeElement("XSI.TYPE", "XS.DATETIME");
//            writer.writeCharacters(((DateValue)valueObject).getValue().toString());
//            writer.writeInlineEndElement();
        }
        else if (valueObject instanceof BinaryValue)
        {
//            writer.writeStartElement(elementName);
            writer.writeElement("XSI.TYPE", "XS.BINARY_DATA");
//            writer.writeCharacters(((BinaryValue)valueObject).getValue());
//            writer.writeInlineEndElement();
        }
        else if (valueObject instanceof ReferenceValue)
        {
            ReferenceValue refValueObject = (ReferenceValue)valueObject;
            QName typeName = getTypeName(refValueObject);
            if (refValueObject.getValue() == null)
            {
                if (writeEmpty)
                {
//                    writer.writeEmptyElement(elementName);
                    writer.writeElement("XSI.TYPE", typeName);
                }
            }
            else
            {
                String ref = referenceWriter.getReferenceRepresentation(refValueObject,
                    McorePackage.Literals.REFERENCE_VALUE__VALUE, refValueObject.getValue());
                if (!Strings.isNullOrEmpty(ref))
                {
//                    writer.writeStartElement(elementName);
                    writer.writeElement("XSI.TYPE", typeName);
//                    writer.writeCharacters(ref);
//                    writer.writeInlineEndElement();
                }
                else if (writeEmpty)
                {
//                    writer.writeEmptyElement(elementName);
                    writer.writeElement("XSI.TYPE", typeName);
                }
            }
        }
        else if (valueObject instanceof IrresolvableReferenceValue)
        {
            IrresolvableReferenceValue referenceValue = (IrresolvableReferenceValue)valueObject;
//            writer.writeStartElement(elementName);
            writer.writeElement("XSI.TYPE", "XR.DESIGN_TIME_REF");
//            writer.writeCharacters(String.format("%s.%s",
//                new Object[] {
//            referenceValue.getRefTypeId().toString(), referenceValue.getInstanceId().toString() }));
//            writer.writeInlineEndElement();
        }
        else if (valueObject instanceof ValueList)
        {
            ValueList valueList = (ValueList)valueObject;
            if (!valueList.getValues().isEmpty())
            {
//                writer.writeStartElement(elementName);
                writer.writeElement("XSI.TYPE", "XR.VALUE_LIST");
                for (Value value : valueList.getValues())
                {
                    writeValue(writer, value, nameManager.getElementQName(McorePackage.Literals.VALUE_LIST__VALUES),
                        writeEmpty, feature, version);
                }
//                writer.writeEndElement();
            }
            else
            {
//                writer.writeEmptyElement(elementName);
                writer.writeElement("XSI.TYPE", "XR.VALUE_LIST");
            }
        }
        else if (valueObject instanceof FixedArrayValue)
        {
//            writer.writeStartElement(elementName);
            writer.writeElement("XSI.TYPE", "V8.FIXED_ARRAY");
            for (Value value : ((FixedArrayValue)valueObject).getValues())
            {
                writeValue(writer, value, nameManager.getElementQName(McorePackage.Literals.FIXED_ARRAY_VALUE__VALUES),
                    writeEmpty, feature, version);
            }
//            writer.writeEndElement();
        }
        else if (valueObject instanceof TypeDescriptionValue)
        {
            TypeDescription value = ((TypeDescriptionValue)valueObject).getValue();
            TypeDescriptionWriter typeDescriptionWriter = typeDescriptionWriterProvider.get();
            if (!typeDescriptionWriter.isEmptyTypeDescription(value))
            {
//                writer.writeStartElement(elementName);
                writer.writeElement("XSI.TYPE", "V8.TYPE_DESCRIPTION");
                typeDescriptionWriter.writeTypeDescription(writer, value);
//                writer.writeEndElement();
            }
            else if (writeEmpty)
            {
//                writer.writeEmptyElement(elementName);
                writer.writeElement("XSI.TYPE", "V8.TYPE_DESCRIPTION");
            }
        }
        else if (valueObject instanceof StandardPeriodValue)
        {
//            writer.writeStartElement(elementName);
            writer.writeElement("XSI.TYPE", "V8.STANDARD_PERIOD");
            standardPeriodWriterProvider.get()
                .writeStandardPeriod(writer, ((StandardPeriodValue)valueObject).getValue());
//            writer.writeEndElement();
        }
        else if (valueObject instanceof com._1c.g5.v8.dt.form.model.FormChoiceListDesTimeValue)
        {
//            writer.writeStartElement(elementName);
            writer.writeElement("XSI.TYPE", "FormChoiceListDesTimeValue");
            featureWriter.write(writer, (EObject)valueObject,
                (EStructuralFeature)FormPackage.Literals.FORM_CHOICE_LIST_DES_TIME_VALUE__PRESENTATION, writeEmpty,
                version);
            featureWriter.write(writer, (EObject)valueObject,
                (EStructuralFeature)FormPackage.Literals.FORM_CHOICE_LIST_DES_TIME_VALUE__VALUE, writeEmpty, version);
//            writer.writeEndElement();
        }
        else if (valueObject instanceof BorderValue)
        {
//            borderWriter.writeBorder(writer, feature, ((BorderValue)valueObject).getValue());
        }
        else if (valueObject instanceof ColorValue)
        {
//            colorWriter.writeColor(writer, feature, ((ColorValue)valueObject).getValue(), writeEmpty, elementName);
        }
        else if (valueObject instanceof FontValue)
        {
//            fontWriter.writeFont(writer, feature, ((FontValue)valueObject).getValue(),
//                nameManager.getElementQName(feature));
        }
        else if (valueObject instanceof AccountTypeValue)
        {
//            writer.writeStartElement(elementName);
            writer.writeElement("XSI.TYPE", "ENT.ACCOUNT_TYPE");
//            writer.writeCharacters(((AccountTypeValue)valueObject).getValue().toString());
//            writer.writeInlineEndElement();
        }
        else if (valueObject instanceof ChartLineTypeValue)
        {
            ChartLineTypeValue value = (ChartLineTypeValue)valueObject;
//            writer.writeStartElement(elementName);
            writer.writeElement("XSI.TYPE", "V8UI.CHART_LINE_TYPE");
//            writer.writeCharacters(value.getValue().toString());
//            writer.writeInlineEndElement();
        }
        else if (valueObject instanceof EnumValue)
        {
            EnumValue value = (EnumValue)valueObject;
            if (value.getValue() != null)
            {
                writeSimpleValue(writer, elementName, value.getValue().getClass().getSimpleName(),
                    value.getValue().getLiteral());
            }
        }
        else if (valueObject instanceof SysEnumValue)
        {
            SysEnumValue value = (SysEnumValue)valueObject;
            if (value.getValue() != null && value.getValue().indexOf('.') != -1)
            {
                String[] segments = value.getValue().split("\\."); //$NON-NLS-1$
                writeSimpleValue(writer, elementName, segments[0], segments[1]);
            }
        }
        else
        {
            throw new RuntimeException(
                String.format("Unexpected value implementation in %s", new Object[] { getClass().getName() }));
        }
    }

    protected QName getTypeName(ReferenceValue refValueObject)
    {
        return AS_DESIGN_TIME_REF.contains(refValueObject.eContainingFeature()) ? new QName("XR.DESIGN_TIME_REF")
            : new QName("XR.MD_OBJECT_REF");
    }

    private void writeSimpleValue(YamlStreamWriter writer, QName elementName, String typeName, String value)
        throws ExportException
    {
//        writer.writeStartElement(elementName);
        QName typeQName = XdtoTypeMap.INSTANCE.typeNameToXmlName(typeName);
        writer.writeElement("XSI.TYPE", ':' + typeQName.getLocalPart());
//        writer.writeCharacters(value);
//        writer.writeInlineEndElement();
    }
}
