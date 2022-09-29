/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.internal.yaml;

import java.nio.file.Path;

import org.eclipse.emf.ecore.EObject;

import com._1c.g5.v8.dt.platform.version.Version;
import com.google.inject.Inject;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.core.IExportFileSupport;
import ru.capralow.dt.modeling.yaml.IExporter;
import ru.capralow.dt.modeling.yaml.IExporterRegistry;

public class ExportFileSupport
    implements IExportFileSupport
{
    @Inject
    private IExporterRegistry exporterRegistry;

    @Override
    public Path getOutputPath(EObject eObject, Version version)
    {
        try
        {
            IExporter exporter = exporterRegistry.getExporter(version, eObject);
            return exporter.getOutputPath(eObject, null, version);
        }
        catch (ExportException e)
        {
            YamlPlugin.log(YamlPlugin.createErrorStatus(e.getMessage(), e));
            return null;
        }
    }

    @Override
    public boolean isExportable(EObject eObject, Version version)
    {
        return exporterRegistry.exporterExists(version, eObject);
    }
}
