/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.core;

import java.nio.file.Path;

import org.eclipse.emf.ecore.EObject;

import com._1c.g5.v8.dt.platform.version.Version;

public interface IExportFileSupport
{
    Path getOutputPath(EObject p0, Version p1);

    boolean isExportable(EObject p0, Version p1);
}
