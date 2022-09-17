/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.yaml;

import java.nio.file.Path;

import org.eclipse.emf.ecore.EObject;

import com._1c.g5.v8.dt.xml.LineFeedConverter;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.core.IExportArtifactBuilder;
import ru.capralow.dt.modeling.yaml.IExportContext;
import ru.capralow.dt.modeling.yaml.IExporter;

public interface IYamlExporterExtensionManager
{
    void copyTemplateWithLineFeedConversion(IExporter paramIExporter, EObject paramEObject,
        IExportContext paramIExportContext, IExportArtifactBuilder paramIExportArtifactBuilder, Path paramPath1,
        Path paramPath2, LineFeedConverter.ConvertOption... paramVarArgs) throws ExportException;

    boolean overridesTemplateCopying(EObject paramEObject, IExportContext paramIExportContext);

    void registerExtension(IYamlExporterExtension paramIXmlExporterExtension);
}
