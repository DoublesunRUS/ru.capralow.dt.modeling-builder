/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.internal.yaml.writer;

import javax.inject.Singleton;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.md.DataHistorySupportProvider;
import com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.md.yaml.impl.MetadataFeatureNameProvider;
import ru.capralow.dt.modeling.yaml.writer.ISpecifiedElementWriter;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;

@Singleton
public class DataHistoryWriter
    implements ISpecifiedElementWriter
{
    @Inject
    private MetadataFeatureNameProvider nameProvider;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws ExportException
    {
        Preconditions.checkArgument(feature == MdClassPackage.Literals.DATA_HISTORY_SUPPORT__DATA_HISTORY,
            "Invalid feature %s", feature);
        Object value = eObject.eGet(feature);
        if (value != null)
        {
            if (eObject instanceof com._1c.g5.v8.dt.metadata.mdclass.StandardAttribute
                && version.isGreaterThan(Version.V8_3_10))
            {
                writer.writeElement("XR.DATA_HISTORY", value);
            }
            else if (DataHistorySupportProvider.isDataHistorySupported(eObject, version))
            {
                writer.writeElement(nameProvider.getElementQName(feature), value);
            }
        }
    }
}
