/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.internal.yaml.writer;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage;
import com._1c.g5.v8.dt.metadata.mdclass.StandardTabularSectionDescription;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.yaml.writer.ISpecifiedElementWriter;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;

@Singleton
public class StandardTabularSectionDescriptorWriter
    implements ISpecifiedElementWriter
{
    private final List<EStructuralFeature> propertyOrderList = fillFeatureOrderList();

    @Inject
    private MetadataSmartFeatureWriter smartFeatureWriter;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws ExportException
    {
        if (feature.getEType() != MdClassPackage.Literals.STANDARD_TABULAR_SECTION_DESCRIPTION)
        {
            throw new IllegalArgumentException(String.format("Invalid feature type in %s", new Object[] { feature }));
        }

        Object value = eObject.eGet(feature);
        if (!(value instanceof List && !((List)value).isEmpty()))
        {
            return;
        }
//            writer.writeStartElement("STANDARD_TABULAR_SECTIONS");
        for (StandardTabularSectionDescription standardTabularSection : (List<StandardTabularSectionDescription>)value)
        {
//                writer.writeStartElement("XR.STANDARD_TABULAR_SECTION");
            writer.writeElement("NAME_ATTRIBUTE", standardTabularSection.getName());
            for (EStructuralFeature structuralFeature : this.propertyOrderList)
            {
                this.smartFeatureWriter.write(writer, standardTabularSection, structuralFeature, true, version);
            }
//                writer.writeEndElement();
        }
//            writer.writeEndElement();

    }

    private List<EStructuralFeature> fillFeatureOrderList()
    {
        ImmutableList.Builder<EStructuralFeature> builder = ImmutableList.builder();
        builder.add(MdClassPackage.Literals.STANDARD_TABULAR_SECTION_DESCRIPTION__SYNONYM);
        builder.add(MdClassPackage.Literals.STANDARD_TABULAR_SECTION_DESCRIPTION__COMMENT);
        builder.add(MdClassPackage.Literals.STANDARD_TABULAR_SECTION_DESCRIPTION__TOOL_TIP);
        builder.add(MdClassPackage.Literals.STANDARD_TABULAR_SECTION_DESCRIPTION__FILL_CHECKING);
        builder.add(MdClassPackage.Literals.STANDARD_TABULAR_SECTION_DESCRIPTION__STANDARD_ATTRIBUTES);
        return builder.build();
    }
}
