/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.yaml;

import java.nio.file.Path;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.platform.version.Version;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.core.IExportArtifactBuilder;

public interface IExporter
{
    String EXPORTER_TRACE_OPTION = "/operation/exporter"; //$NON-NLS-1$

    Path getOutputPath(EObject eObject, EStructuralFeature eStructuralFeature, String fileExtension, Version version)
        throws ExportException;

    default Path getOutputPath(EObject eObject, EStructuralFeature feature, Version version) throws ExportException
    {
        return getOutputPath(eObject, feature, null, version);
    }

    boolean isAppropriate(Version version, EObject eObject);

    IStatus work(EObject eObject, IExportContext iExportContext, IExportArtifactBuilder iExportArtifactBuilder,
        IProgressMonitor iProgressMonitor) throws ExportException;
}
