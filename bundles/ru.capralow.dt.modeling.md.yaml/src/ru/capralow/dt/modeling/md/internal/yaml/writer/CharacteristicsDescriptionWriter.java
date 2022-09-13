/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.internal.yaml.writer;

import java.util.List;
import java.util.Optional;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.core.model.ISymLinkConverter;
import com._1c.g5.v8.dt.core.naming.ISymbolicNameService;
import com._1c.g5.v8.dt.metadata.mdclass.CharacteristicsDescription;
import com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.md.yaml.IMetadataXmlElements;
import ru.capralow.dt.modeling.yaml.IQNameProvider;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;
import ru.capralow.dt.modeling.yaml.writer.ISpecifiedElementWriter;

@Singleton
public class CharacteristicsDescriptionWriter
    implements ISpecifiedElementWriter
{
    @Inject
    private MetadataSmartFeatureWriter smartFeatureWriter;

    @Inject
    private IQNameProvider nameProvider;

    @Inject
    private ISymLinkConverter linkConverter;

    @Inject
    private ISymbolicNameService symbolicNameService;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws XMLStreamException, ExportException
    {
        Preconditions.checkArgument((feature.getEType() == MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION),
            "Invalid feature type");
        QName elementQName = this.nameProvider.getElementQName(feature);
        Optional<List<CharacteristicsDescription>> characteristics = getCharacteristicsDescription(eObject, feature);
        if (characteristics.isPresent())
        {
            writer.writeStartElement(elementQName);
            for (CharacteristicsDescription characteristic : characteristics.get())
            {
                writer.writeStartElement(IMetadataXmlElements.XR.CHARACTERISTIC);
                writer.writeStartElement(IMetadataXmlElements.XR.CHARACTERISTIC_TYPES);
                MdObject types = characteristic.getCharacteristicTypes();
                writer
                    .writeElement(IMetadataXmlElements.FROM_ATTRIBUTE.getLocalPart(),
                        this.linkConverter
                            .convert(characteristic,
                                MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__CHARACTERISTIC_TYPES,
                                (types != null)
                                    ? this.symbolicNameService.generateSymbolicName(types, characteristic,
                                        MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__CHARACTERISTIC_TYPES)
                                    : ""));
                this.smartFeatureWriter.write(writer, characteristic,
                    MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__KEY_FIELD, true, version);
                this.smartFeatureWriter.write(writer, characteristic,
                    MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__TYPES_FILTER_FIELD, true, version);
                this.smartFeatureWriter.write(writer, characteristic,
                    MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__TYPES_FILTER_VALUE, true, version);
                if (version.isGreaterThan(Version.V8_3_18))
                    this.smartFeatureWriter.write(writer, characteristic,
                        MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__DATA_PATH_FIELD, true, version);
                if (version.isGreaterThan(Version.V8_3_20))
                    this.smartFeatureWriter.write(writer, characteristic,
                        MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__MULTIPLE_VALUES_USE_FIELD, true, version);
                writer.writeEndElement();
                MdObject values = characteristic.getCharacteristicValues();
                writer.writeStartElement(IMetadataXmlElements.XR.CHARACTERISTIC_VALUES);
                writer
                    .writeElement(IMetadataXmlElements.FROM_ATTRIBUTE.getLocalPart(),
                        this.linkConverter
                            .convert(characteristic,
                                MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__CHARACTERISTIC_VALUES,
                                (values != null)
                                    ? this.symbolicNameService.generateSymbolicName(values, characteristic,
                                        MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__CHARACTERISTIC_VALUES)
                                    : ""));
                this.smartFeatureWriter.write(writer, characteristic,
                    MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__OBJECT_FIELD, true, version);
                this.smartFeatureWriter.write(writer, characteristic,
                    MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__TYPE_FIELD, true, version);
                this.smartFeatureWriter.write(writer, characteristic,
                    MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__VALUE_FIELD, true, version);
                if (version.isGreaterThan(Version.V8_3_20))
                {
                    this.smartFeatureWriter.write(writer, characteristic,
                        MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__MULTIPLE_VALUES_KEY_FIELD, true, version);
                    this.smartFeatureWriter.write(writer, characteristic,
                        MdClassPackage.Literals.CHARACTERISTICS_DESCRIPTION__MULTIPLE_VALUES_ORDER_FIELD, true,
                        version);
                }
                writer.writeEndElement();
                writer.writeEndElement();
            }
            writer.writeEndElement();
        }
        else
        {
            writer.writeEmptyElement(elementQName);
        }
    }

    private Optional<List<CharacteristicsDescription>> getCharacteristicsDescription(EObject eObject,
        EStructuralFeature feature)
    {
        if (eObject != null)
        {
            Object values = eObject.eGet(feature);
            if (values instanceof List && !((List)values).isEmpty())
                return Optional.of((List<CharacteristicsDescription>)values);
        }
        return Optional.empty();
    }
}
