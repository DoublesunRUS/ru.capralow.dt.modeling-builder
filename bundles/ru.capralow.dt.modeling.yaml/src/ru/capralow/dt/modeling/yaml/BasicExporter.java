/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.yaml;

import java.nio.file.Path;
import java.text.MessageFormat;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com._1c.g5.v8.dt.platform.version.Version;
import com.google.inject.Inject;

import ru.capralow.dt.modeling.core.ExportDebugTrace;
import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.core.IExportFileSystemSupport;

public abstract class BasicExporter
    implements IExporter
{
    protected final ExportDebugTrace debugTrace = ExportDebugTrace.getInstance();

    @Inject
    private IExportFileSystemSupport fileProvider;

    @Inject
    private IExporterQualifier exporterQuialifier;

    @Override
    public Path getOutputPath(EObject eObject, EStructuralFeature feature, String fileExtension, Version version)
        throws ExportException
    {
        try
        {
            Path outputFile = this.fileProvider.getFileName(eObject, feature, fileExtension, version);
            if (outputFile == null)
            {
                this.debugTrace.trace(IExporter.EXPORTER_TRACE_OPTION,
                    MessageFormat.format("No output file for object with uri: {0} in {1}",
                        new Object[] { EcoreUtil.getURI(eObject), getClass().getSimpleName() }));
            }

            return outputFile;
        }

        catch (Exception e)
        {
            this.debugTrace.trace(IExporter.EXPORTER_TRACE_OPTION,
                MessageFormat.format("BasicExporter gets output path error for object {0}, feature {1}",
                    new Object[] { EcoreUtil.getURI(eObject), feature }),
                e);
            throw e;
        }
    }

    @Override
    public Path getOutputPath(EObject eObject, EStructuralFeature feature, Version version) throws ExportException
    {
        return getOutputPath(eObject, feature, null, version);
    }

    public Path getOutputPath(EObject eObject, String fileExtension, Version version) throws ExportException
    {
        return getOutputPath(eObject, null, fileExtension, version);
    }

    public Path getOutputPath(EObject eObject, Version version) throws ExportException
    {
        return getOutputPath(eObject, null, null, version);
    }

    @Override
    public boolean isAppropriate(Version version, EObject eObject)
    {
        return this.exporterQuialifier.qualify(this, version, eObject);
    }
}
