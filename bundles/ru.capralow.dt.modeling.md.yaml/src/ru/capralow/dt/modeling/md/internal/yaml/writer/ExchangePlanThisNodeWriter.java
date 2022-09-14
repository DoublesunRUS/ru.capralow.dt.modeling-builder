/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.internal.yaml.writer;

import java.util.UUID;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.metadata.mdclass.ExchangePlan;
import com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.md.yaml.IMetadataYamlElements;
import ru.capralow.dt.modeling.yaml.writer.ISpecifiedElementWriter;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;

@Singleton
public class ExchangePlanThisNodeWriter
    implements ISpecifiedElementWriter
{
    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws ExportException
    {
        if (!(eObject instanceof ExchangePlan))
        {
            throw new IllegalArgumentException(String.format("Invalid object %s", new Object[] { eObject }));
        }
        if (feature != MdClassPackage.Literals.EXCHANGE_PLAN__THIS_NODE)
        {
            throw new IllegalArgumentException(String.format("Invalid feature %s", new Object[] { feature }));
        }
        UUID uuid = ((ExchangePlan)eObject).getThisNode();
        if (uuid != null)
        {
            writer.writeElement(IMetadataYamlElements.XR.THIS_NODE, uuid.toString());
        }
    }
}
