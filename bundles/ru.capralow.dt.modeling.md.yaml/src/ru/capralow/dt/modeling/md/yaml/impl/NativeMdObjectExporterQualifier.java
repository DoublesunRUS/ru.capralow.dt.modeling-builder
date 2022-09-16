/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.yaml.impl;

import org.eclipse.emf.ecore.EObject;

import com._1c.g5.v8.dt.metadata.mdclass.MdObject;
import com._1c.g5.v8.dt.metadata.mdclass.ObjectBelonging;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.yaml.IExporter;
import ru.capralow.dt.modeling.yaml.IExporterQualifier;

@Singleton
public class NativeMdObjectExporterQualifier
    implements IExporterQualifier
{
    @Override
    public boolean qualify(IExporter exporter, Version version, EObject eObject)
    {
        EObject eObject1 = eObject;

        if (eObject1 instanceof com._1c.g5.v8.dt.metadata.mdclass.Predefined)
        {
            eObject1 = eObject.eContainer();
        }
        return !(eObject1 instanceof MdObject && ((MdObject)eObject1).getObjectBelonging() != ObjectBelonging.NATIVE);
    }
}
