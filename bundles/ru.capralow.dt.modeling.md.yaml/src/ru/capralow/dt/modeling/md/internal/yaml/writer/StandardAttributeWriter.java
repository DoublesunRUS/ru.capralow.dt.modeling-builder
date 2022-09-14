/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.internal.yaml.writer;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage;
import com._1c.g5.v8.dt.metadata.mdclass.StandardAttribute;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.md.yaml.impl.MetadataFeatureNameProvider;
import ru.capralow.dt.modeling.yaml.writer.ISpecifiedElementWriter;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;

@Singleton
public class StandardAttributeWriter
    implements ISpecifiedElementWriter
{
    private final List<EStructuralFeature> propertyStandardAttributeOrderList = fillStandardAttributeFeatureOrderList();

    @Inject
    private MetadataSmartFeatureWriter smartFeatureWriter;

    @Inject
    private MetadataFeatureNameProvider featureNameProvider;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws ExportException
    {
        if (feature.getEType() != MdClassPackage.Literals.STANDARD_ATTRIBUTE)
        {
            throw new IllegalArgumentException(String.format("Invalid feature type in %s", new Object[] { feature }));
        }

        Object value = eObject.eGet(feature);
        if (!(value instanceof List) && ((List<?>)value).isEmpty())
        {
            return;
        }

//            writer.writeStartElement(featureNameProvider.getElementQName(feature));
        for (StandardAttribute attribute : (List<StandardAttribute>)value)
        {
//                writer.writeStartElement(IMetadataXmlElements.XR.STANDARD_ATTRIBUTE);
            writer.writeElement("NAME_ATTRIBUTE", attribute.getName());
            for (EStructuralFeature structuralFeature : propertyStandardAttributeOrderList)
            {
                smartFeatureWriter.write(writer, attribute, structuralFeature, true, version);
            }
//                writer.writeEndElement();
        }
//            writer.writeEndElement();
    }

    private List<EStructuralFeature> fillStandardAttributeFeatureOrderList()
    {
        ImmutableList.Builder<EStructuralFeature> builder = ImmutableList.builder();
        builder.add(MdClassPackage.Literals.STANDARD_ATTRIBUTE__LINK_BY_TYPE);
        builder.add(MdClassPackage.Literals.STANDARD_ATTRIBUTE__FILL_CHECKING);
        builder.add(MdClassPackage.Literals.STANDARD_ATTRIBUTE__MULTI_LINE);
        builder.add(MdClassPackage.Literals.STANDARD_ATTRIBUTE__FILL_FROM_FILLING_VALUE);
        builder.add(MdClassPackage.Literals.STANDARD_ATTRIBUTE__CREATE_ON_INPUT);
        builder.add(MdClassPackage.Literals.STANDARD_ATTRIBUTE__MAX_VALUE);
        builder.add(MdClassPackage.Literals.STANDARD_ATTRIBUTE__TOOL_TIP);
        builder.add(MdClassPackage.Literals.STANDARD_ATTRIBUTE__EXTENDED_EDIT);
        builder.add(MdClassPackage.Literals.STANDARD_ATTRIBUTE__FORMAT);
        builder.add(MdClassPackage.Literals.STANDARD_ATTRIBUTE__CHOICE_FORM);
        builder.add(MdClassPackage.Literals.STANDARD_ATTRIBUTE__QUICK_CHOICE);
        builder.add(MdClassPackage.Literals.STANDARD_ATTRIBUTE__CHOICE_HISTORY_ON_INPUT);
        builder.add(MdClassPackage.Literals.STANDARD_ATTRIBUTE__EDIT_FORMAT);
        builder.add(MdClassPackage.Literals.STANDARD_ATTRIBUTE__PASSWORD_MODE);
        builder.add(MdClassPackage.Literals.DATA_HISTORY_SUPPORT__DATA_HISTORY);
        builder.add(MdClassPackage.Literals.STANDARD_ATTRIBUTE__MARK_NEGATIVES);
        builder.add(MdClassPackage.Literals.STANDARD_ATTRIBUTE__MIN_VALUE);
        builder.add(MdClassPackage.Literals.STANDARD_ATTRIBUTE__SYNONYM);
        builder.add(MdClassPackage.Literals.STANDARD_ATTRIBUTE__COMMENT);
        builder.add(MdClassPackage.Literals.STANDARD_ATTRIBUTE__FULL_TEXT_SEARCH);
        builder.add(MdClassPackage.Literals.STANDARD_ATTRIBUTE__CHOICE_PARAMETER_LINKS);
        builder.add(MdClassPackage.Literals.STANDARD_ATTRIBUTE__FILL_VALUE);
        builder.add(MdClassPackage.Literals.STANDARD_ATTRIBUTE__MASK);
        builder.add(MdClassPackage.Literals.STANDARD_ATTRIBUTE__CHOICE_PARAMETERS);
        return builder.build();
    }
}
