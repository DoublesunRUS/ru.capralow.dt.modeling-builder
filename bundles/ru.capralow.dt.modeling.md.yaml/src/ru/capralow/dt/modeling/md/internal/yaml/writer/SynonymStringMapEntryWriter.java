/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.internal.yaml.writer;

import java.util.Map;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.mcore.McorePackage;
import com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.md.yaml.IMetadataYamlElements;
import ru.capralow.dt.modeling.yaml.IQnameProvider;
import ru.capralow.dt.modeling.yaml.writer.LocalStringMapEntryWriter;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;

@Singleton
public class SynonymStringMapEntryWriter
    extends LocalStringMapEntryWriter
{
    @Inject
    private IQnameProvider nameProvider;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version, Map<String, Object> group) throws ExportException
    {
        Preconditions.checkArgument(
            feature != null && feature.isMany() && feature.getEType() == McorePackage.Literals.LOCAL_STRING_MAP_ENTRY,
            Messages.ElementWriter_Invalid_feature_type_0, feature);

        QName featureName = nameProvider.getElementQName(feature);
        if (eObject instanceof com._1c.g5.v8.dt.metadata.mdclass.Configuration
            && feature == MdClassPackage.Literals.MD_OBJECT__SYNONYM)
        {
            featureName = IMetadataYamlElements.Application.SYNONYM;
        }

        @SuppressWarnings("unchecked")
        EMap<String, String> localStringMap = (EMap<String, String>)eObject.eGet(feature);

        writeLocalString(writer, eObject, feature, localStringMap, featureName, writeEmpty, version, group);
    }

}
