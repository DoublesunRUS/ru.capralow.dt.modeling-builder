/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.internal.yaml.writer;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.metadata.mdclass.CatalogAttribute;
import com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.md.yaml.IMetadataYamlElements;
import ru.capralow.dt.modeling.md.yaml.impl.MetadataFeatureNameProvider;
import ru.capralow.dt.modeling.yaml.writer.ISpecifiedElementWriter;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;

@Singleton
public class CatalogAttributeWriter
    implements ISpecifiedElementWriter
{
    private final List<EStructuralFeature> propertyCatalogAttributeOrderList = fillCatalogAttributeFeatureOrderList();

    @Inject
    private MetadataSmartFeatureWriter smartFeatureWriter;

    @Inject
    private MetadataFeatureNameProvider featureNameProvider;

    @Inject
    private MetadataObjectFeatureOrderProvider featureOrderProvider;

    @SuppressWarnings("unchecked")
    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version, Map<String, Object> group) throws ExportException
    {
        if (feature.getEType() != MdClassPackage.Literals.CATALOG_ATTRIBUTE)
        {
            throw new IllegalArgumentException(
                String.format(Messages.ElementWriter_Invalid_feature_type_0, new Object[] { feature }));
        }

        Object value = eObject.eGet(feature);
        if (!(value instanceof List) && ((List<?>)value).isEmpty())
        {
            return;
        }

        List<Object> featureList = writer.addList(featureNameProvider.getElementQName(feature), group);
        for (CatalogAttribute attribute : (List<CatalogAttribute>)value)
        {
            Map<String, Object> attributeGroup = writer.addGroup(featureList);

            writer.writeElement(IMetadataYamlElements.NAME, attribute.getName(), attributeGroup);
            writer.writeElement(IMetadataYamlElements.ID, attribute.getUuid(), attributeGroup);

            List<EStructuralFeature> propertiesList = getPropertiesFeatureList(attribute, version);
            if (!propertiesList.isEmpty())
            {
                for (EStructuralFeature property : propertiesList)
                {
                    smartFeatureWriter.write(writer, attribute, property, false, version, attributeGroup);
                }
            }

            for (EStructuralFeature structuralFeature : propertyCatalogAttributeOrderList)
            {
                smartFeatureWriter.write(writer, attribute, structuralFeature, false, version, attributeGroup);
            }
        }
    }

    private List<EStructuralFeature> fillCatalogAttributeFeatureOrderList()
    {
        ImmutableList.Builder<EStructuralFeature> builder = ImmutableList.builder();
        builder.add(MdClassPackage.Literals.CATALOG_ATTRIBUTE__CHOICE_HISTORY_ON_INPUT);
        return builder.build();
    }

    protected List<EStructuralFeature> getPropertiesFeatureList(MdObject mdObject, Version version)
    {
        return featureOrderProvider.getProperties(mdObject.eClass(), version);
    }
}
