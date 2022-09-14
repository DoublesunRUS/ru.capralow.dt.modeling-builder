/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.internal.yaml.writer;

import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.metadata.mdclass.CommonAttributeContentItem;
import com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.inject.Inject;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.md.yaml.IMetadataYamlElements;
import ru.capralow.dt.modeling.yaml.IQNameProvider;
import ru.capralow.dt.modeling.yaml.writer.ISpecifiedElementWriter;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;

public class CommonAttributeContentItemWriter
    implements ISpecifiedElementWriter
{
    @Inject
    private IQNameProvider nameProvider;

    @Inject
    private MetadataSmartFeatureWriter smartFeatureWriter;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws ExportException
    {
        if (feature != MdClassPackage.Literals.COMMON_ATTRIBUTE__CONTENT)
        {
            throw new IllegalArgumentException(String.format("Invalid feature %s", new Object[] { feature }));
        }
        QName elementQName = this.nameProvider.getElementQName(feature);
        if (eObject == null)
        {
            writer.writeEmptyElement(elementQName);
            return;
        }
        Object value = eObject.eGet(feature);
        if (!(value instanceof List) || ((List)value).isEmpty())
        {
            writer.writeEmptyElement(elementQName);
            return;
        }
        writer.writeStartElement(elementQName);
        for (CommonAttributeContentItem item : (List<CommonAttributeContentItem>)value)
        {
            writer.writeStartElement(IMetadataYamlElements.XR.ITEM);
            this.smartFeatureWriter.write(writer, item, MdClassPackage.Literals.COMMON_ATTRIBUTE_CONTENT_ITEM__METADATA,
                true, version);
            this.smartFeatureWriter.write(writer, item, MdClassPackage.Literals.COMMON_ATTRIBUTE_CONTENT_ITEM__USE,
                true, version);
            this.smartFeatureWriter.write(writer, item,
                MdClassPackage.Literals.COMMON_ATTRIBUTE_CONTENT_ITEM__CONDITIONAL_SEPARATION, true, version);
            writer.writeEndElement();
        }
        writer.writeEndElement();
    }
}
