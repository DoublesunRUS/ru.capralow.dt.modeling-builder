/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.core;

import java.nio.file.Path;

import org.eclipse.emf.ecore.EObject;

import com._1c.g5.v8.dt.platform.version.Version;

public interface IExportOperationFactory
{
    IExportOperation createExportOperation(Path p0, EObject... p1);

    IExportOperation createExportOperation(Path p0, Version p1, EObject... p2);

    IExportOperation createExportOperation(Path p0, Version p1, IExportStrategy p2, EObject... p3);

    IExportOperation createExportOperation(Version p0, IExportStrategy p1, IExportArtifactBuilderFactory p2,
        EObject... p3);
}
