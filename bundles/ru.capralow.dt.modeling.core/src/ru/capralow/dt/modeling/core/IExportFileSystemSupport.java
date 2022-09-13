/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.core;

import java.nio.file.Path;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.platform.version.Version;

public interface IExportFileSystemSupport
{
    Path getFileName(EObject eObject, EStructuralFeature eStructuralFeature, Version version);

    default Path getFileName(EObject eObject, EStructuralFeature feature, String fileExtension, Version version)
        throws ExportException
    {
        return getFileName(eObject, feature, version);
    }
}
