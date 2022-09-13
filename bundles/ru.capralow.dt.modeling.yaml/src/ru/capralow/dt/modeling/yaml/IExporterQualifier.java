/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.yaml;

import org.eclipse.emf.ecore.EObject;

import com._1c.g5.v8.dt.platform.version.Version;
import com.google.inject.ImplementedBy;
import com.google.inject.Singleton;

@ImplementedBy(IExporterQualifier.NullExporterQualifier.class)
public interface IExporterQualifier
{
    boolean qualify(IExporter paramIExporter, Version paramVersion, EObject paramEObject);

    @Singleton
    class NullExporterQualifier
        implements IExporterQualifier
    {
        @Override
        public boolean qualify(IExporter exporter, Version version, EObject eObject)
        {
            return true;
        }
    }
}
