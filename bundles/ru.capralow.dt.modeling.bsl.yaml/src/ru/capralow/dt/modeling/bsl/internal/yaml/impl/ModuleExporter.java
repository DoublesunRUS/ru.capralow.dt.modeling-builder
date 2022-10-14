/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.bsl.internal.yaml.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.text.MessageFormat;

import javax.inject.Inject;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;

import com._1c.g5.v8.bm.core.BmUriUtil;
import com._1c.g5.v8.dt.bsl.model.Module;
import com._1c.g5.v8.dt.core.filesystem.IProjectFileSystemSupport;
import com._1c.g5.v8.dt.core.filesystem.IProjectFileSystemSupportProvider;
import com._1c.g5.v8.dt.core.platform.IResourceLookup;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.bsl.internal.yaml.BslYamlPlugin;
import ru.capralow.dt.modeling.bsl.internal.yaml.writer.ModuleWriter;
import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.core.IExportArtifactBuilder;
import ru.capralow.dt.modeling.yaml.BasicExporter;
import ru.capralow.dt.modeling.yaml.IExportContext;
import ru.capralow.dt.modeling.yaml.IExporter;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;

@Singleton
public class ModuleExporter
    extends BasicExporter
{
    @Inject
    private IResourceLookup resourceLookup;

    @Inject
    private IProjectFileSystemSupportProvider fileSystemSupportProvider;

    @Override
    public boolean isAppropriate(Version version, EObject eObject)
    {
        return eObject instanceof Module && super.isAppropriate(version, eObject);
    }

    @Override
    public IStatus work(EObject eObject, IExportContext exportContext, IExportArtifactBuilder artifactBuilder,
        IProgressMonitor progressMonitor) throws ExportException
    {
        IProject project = resourceLookup.getProject(eObject);

        IProjectFileSystemSupport fileSystemSupport = fileSystemSupportProvider.getProjectFileSystemSupport(project);

        IFile bslFile = fileSystemSupport.getFile(eObject);

        debugTrace.trace(IExporter.EXPORTER_TRACE_OPTION, MessageFormat.format("Exporting module: {0}", bslFile));

        if (bslFile == null || !bslFile.exists())
        {
            return Status.OK_STATUS;
        }

        Path outputPath = getOutputPath(eObject, exportContext.getProjectVersion());
        if (outputPath == null)
        {
            URI objectUri = eObject.eResource().getURI();
            if (BmUriUtil.isBmUri(objectUri))
            {
                return BslYamlPlugin
                    .createErrorStatus(MessageFormat.format("MetadataXmlExporter_Error_reading_data_for_export_of__0",
                        new Object[] { BmUriUtil.extractTopObjectFqn(eObject.eResource().getURI()) }), null);
            }
            return BslYamlPlugin
                .createErrorStatus(MessageFormat.format("MetadataXmlExporter_md_object_output_file_error__0",
                    new Object[] { eObject.eClass().getName() }), null);
        }

        progressMonitor.setTaskName(
            MessageFormat.format(Messages.ModuleExporter_Export_file__0, new Object[] { outputPath.toString() }));

        return export(eObject, outputPath, exportContext, artifactBuilder);

    }

    protected IStatus export(EObject eObject, Path path, IExportContext exportContext,
        IExportArtifactBuilder artifactBuilder)
    {
        try (OutputStream outputStream = artifactBuilder.newOutputStream(path);
            YamlStreamWriter writer = new YamlStreamWriter(outputStream);)
        {
            write(eObject, exportContext, writer);
        }

        catch (FactoryConfigurationError | IOException | XMLStreamException | ExportException e)
        {
            debugTrace.trace(IExporter.EXPORTER_TRACE_OPTION,
                MessageFormat.format("Export write to file {0} error in {1}", path, getClass().getSimpleName()), e);
            return BslYamlPlugin.createErrorStatus(MessageFormat.format("XmlExporter_error_exporting__0", path), e);
        }

        return Status.OK_STATUS;
    }

    protected void write(EObject eObject, IExportContext exportContext, YamlStreamWriter writer)
        throws XMLStreamException, ExportException
    {

        ModuleWriter moduleWriter = BslYamlPlugin.getInstance().getInjector().getInstance(ModuleWriter.class);

        moduleWriter.write(writer, eObject);
    }

}
