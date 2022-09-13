/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.internal.yaml;

import org.eclipse.core.resources.IProject;

import com._1c.g5.v8.dt.platform.version.Version;

import ru.capralow.dt.modeling.yaml.IExportContext;

public class ExportContext
    implements IExportContext
{
    private final IProject project;

    private final Version version;

    public ExportContext(IProject project, Version version)
    {
        this.project = project;
        this.version = version;
    }

    @Override
    public IProject getExportProject()
    {
        return this.project;
    }

    @Override
    public Version getProjectVersion()
    {
        return this.version;
    }
}
