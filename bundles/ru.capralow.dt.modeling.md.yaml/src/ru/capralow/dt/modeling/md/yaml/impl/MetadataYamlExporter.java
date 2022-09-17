/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.yaml.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.text.MessageFormat;

import javax.inject.Inject;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com._1c.g5.v8.bm.core.BmUriUtil;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.core.IExportArtifactBuilder;
import ru.capralow.dt.modeling.md.internal.yaml.MdYamlPlugin;
import ru.capralow.dt.modeling.md.internal.yaml.writer.MetadataObjectWriter;
import ru.capralow.dt.modeling.yaml.BasicExporter;
import ru.capralow.dt.modeling.yaml.IExportContext;
import ru.capralow.dt.modeling.yaml.IExporter;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;

@Singleton
public class MetadataYamlExporter
    extends BasicExporter
{
    @Inject
    private MetadataObjectWriter metadataYamlWriter;

    @Override
    public boolean isAppropriate(Version version, EObject eObject)
    {
        boolean res = eObject instanceof MdObject;

        res = res && (!(eObject instanceof com._1c.g5.v8.dt.metadata.mdclass.BasicFeature)
            || eObject instanceof com._1c.g5.v8.dt.metadata.mdclass.CommonAttribute);

        res = res && (!(eObject instanceof com._1c.g5.v8.dt.metadata.mdclass.BasicCommand)
            || eObject instanceof com._1c.g5.v8.dt.metadata.mdclass.CommonCommand);

        res = res && !(eObject instanceof com._1c.g5.v8.dt.metadata.mdclass.BasicTabularSection)
            && !(eObject instanceof com._1c.g5.v8.dt.metadata.mdclass.BasicTemplate)
            && !(eObject instanceof com._1c.g5.v8.dt.metadata.mdclass.ExchangePlan)
            && !(eObject instanceof com._1c.g5.v8.dt.metadata.mdclass.WSReference)
            && !(eObject instanceof com._1c.g5.v8.dt.metadata.mdclass.CommonPicture)
            && !(eObject instanceof com._1c.g5.v8.dt.metadata.mdclass.Column)
            && !(eObject instanceof com._1c.g5.v8.dt.metadata.mdclass.EnumValue)
            && !(eObject instanceof com._1c.g5.v8.dt.metadata.mdclass.RecalculationDimension)
            && !(eObject instanceof com._1c.g5.v8.dt.metadata.mdclass.XDTOPackage)
            && !(eObject instanceof com._1c.g5.v8.dt.metadata.mdclass.URLTemplate)
            && !(eObject instanceof com._1c.g5.v8.dt.metadata.mdclass.Method)
            && !(eObject instanceof com._1c.g5.v8.dt.metadata.mdclass.Operation)
            && !(eObject instanceof com._1c.g5.v8.dt.metadata.mdclass.Parameter)
            && !(eObject instanceof com._1c.g5.v8.dt.metadata.mdclass.Field)
            && !(eObject instanceof com._1c.g5.v8.dt.metadata.mdclass.Resource)
            && !(eObject instanceof com._1c.g5.v8.dt.metadata.mdclass.Dimension)
            && !(eObject instanceof com._1c.g5.v8.dt.metadata.mdclass.Function)
            && !(eObject instanceof com._1c.g5.v8.dt.metadata.mdclass.IntegrationServiceChannel);

        return res && super.isAppropriate(version, eObject);
    }

    @Override
    public IStatus work(EObject eObject, IExportContext exportContext, IExportArtifactBuilder artifactBuilder,
        IProgressMonitor progressMonitor) throws ExportException
    {
        if (eObject.eIsProxy())
        {
            debugTrace.trace(IExporter.EXPORTER_TRACE_OPTION, MessageFormat.format("Proxy object with uri: {0} in {1}",
                new Object[] { EcoreUtil.getURI(eObject), getClass().getSimpleName() }));
            return Status.OK_STATUS;
        }

        Path outputPath = getOutputPath(eObject, exportContext.getProjectVersion());
        if (outputPath == null)
        {
            URI objectUri = eObject.eResource().getURI();
            if (BmUriUtil.isBmUri(objectUri))
            {
                return MdYamlPlugin.createErrorStatus(
                    MessageFormat.format(Messages.MetadataXmlExporter_Error_reading_data_for_export_of__0,
                        new Object[] { BmUriUtil.extractTopObjectFqn(eObject.eResource().getURI()) }),
                    null);
            }
            return MdYamlPlugin
                .createErrorStatus(MessageFormat.format(Messages.MetadataXmlExporter_md_object_output_file_error__0,
                    new Object[] { eObject.eClass().getName() }), null);
        }

        progressMonitor.setTaskName(
            MessageFormat.format(Messages.XmlExporter_export_file__0, new Object[] { outputPath.toString() }));

        MdObject mdObject = (MdObject)eObject;
        if (mdObject.getName() == null)
        {
            debugTrace.trace(IExporter.EXPORTER_TRACE_OPTION,
                MessageFormat.format("Cannot export metadata object {0} without name {1}",
                    new Object[] { EcoreUtil.getURI(eObject), getClass().getSimpleName() }));
            return MdYamlPlugin.createErrorStatus(Messages.MetadataXmlExporter_md_object_without_name_error, null);
        }

        return export((MdObject)eObject, outputPath, exportContext, artifactBuilder);
    }

    protected IStatus export(MdObject mdObject, Path path, IExportContext exportContext,
        IExportArtifactBuilder artifactBuilder)
    {
        if (!(MetadataObjectWriter.isMdObjectSupported(mdObject)))
        {
            return Status.OK_STATUS;
        }

        try (OutputStream outputStream = artifactBuilder.newOutputStream(path);
            YamlStreamWriter writer = new YamlStreamWriter(outputStream);)
        {
            write(mdObject, exportContext, writer);
        }

        catch (FactoryConfigurationError | IOException | XMLStreamException | ExportException e)
        {
            debugTrace.trace(IExporter.EXPORTER_TRACE_OPTION,
                MessageFormat.format("Export write to file {0} error in {1}", path, getClass().getSimpleName()), e);
            return MdYamlPlugin.createErrorStatus(MessageFormat.format(Messages.XmlExporter_error_exporting__0, path),
                e);
        }

        return Status.OK_STATUS;
    }

    protected void write(MdObject mdObject, IExportContext exportContext, YamlStreamWriter writer)
        throws XMLStreamException, ExportException
    {
        metadataYamlWriter.write(writer, mdObject, null, true, exportContext.getProjectVersion(), null);
    }

}
