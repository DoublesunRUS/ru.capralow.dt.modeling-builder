/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.yaml;

import org.eclipse.emf.ecore.EObject;

import com._1c.g5.v8.dt.platform.version.Version;
import com.google.inject.ImplementedBy;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.internal.yaml.ExporterRegistry;

@ImplementedBy(ExporterRegistry.class)
public interface IExporterRegistry
{
    IExporter getExporter(Version paramVersion, EObject paramEObject) throws ExportException;

    boolean exporterExists(Version paramVersion, EObject paramEObject);
}
