/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.yaml;

import org.eclipse.core.resources.IProject;

import com._1c.g5.v8.dt.platform.version.Version;

public interface IExportContext
{
    IProject getExportProject();

    Version getProjectVersion();
}
