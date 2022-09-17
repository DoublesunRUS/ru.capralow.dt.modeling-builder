/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.yaml.impl;

import java.nio.file.Path;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.emf.ecore.EObject;

import com._1c.g5.v8.dt.xml.LineFeedConverter;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.core.IExportArtifactBuilder;
import ru.capralow.dt.modeling.md.internal.yaml.MdYamlPlugin;
import ru.capralow.dt.modeling.md.yaml.IYamlExporterExtension;
import ru.capralow.dt.modeling.md.yaml.IYamlExporterExtensionManager;
import ru.capralow.dt.modeling.yaml.IExportContext;
import ru.capralow.dt.modeling.yaml.IExporter;

@Singleton
public class YamlExporterExtensionManager
    implements IYamlExporterExtensionManager
{
    private final Collection<IYamlExporterExtension> extensions = new CopyOnWriteArrayList<>();

    @Override
    public void copyTemplateWithLineFeedConversion(IExporter exporter, EObject eObject, IExportContext exportContext,
        IExportArtifactBuilder artifactBuilder, Path source, Path target, LineFeedConverter.ConvertOption... options)
        throws ExportException
    {
        for (IYamlExporterExtension extension : this.extensions)
        {
            if (extension.overridesTemplateCopying(eObject, exportContext))
            {
                extension.copyTemplateWithLineFeedConversion(exporter, eObject, exportContext, artifactBuilder, source,
                    target, options);
            }
        }
    }

    @Override
    public boolean overridesTemplateCopying(EObject eObject, IExportContext exportContext)
    {
        for (IYamlExporterExtension extension : this.extensions)
        {
            if (extension.overridesTemplateCopying(eObject, exportContext))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void registerExtension(IYamlExporterExtension extension)
    {
        this.extensions.add(extension);
        MdYamlPlugin
            .log(MdYamlPlugin.createWarningStatus("The external IYamlExporterExtension is registered: " + extension));
    }
}
