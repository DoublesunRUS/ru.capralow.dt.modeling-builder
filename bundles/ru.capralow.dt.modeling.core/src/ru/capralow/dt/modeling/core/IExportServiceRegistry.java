/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.core;

import com._1c.g5.v8.dt.platform.version.Version;
import com.google.inject.ImplementedBy;

import ru.capralow.dt.modeling.internal.core.ExportServiceRegistry;

@ImplementedBy(ExportServiceRegistry.class)
public interface IExportServiceRegistry
{
    IExportFileSupport getExportFileSupport(Version p0) throws ExportException;

    IExportService getExportService(Version p0) throws ExportException;
}
