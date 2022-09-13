/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.internal.yaml;

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.core.IExportFileSupport;
import ru.capralow.dt.modeling.core.IExportService;
import ru.capralow.dt.modeling.yaml.IExporterRegistry;

public class ServicesModule
    extends AbstractModule
    implements Module
{
    @Override
    protected void configure()
    {
        bind(IExportFileSupport.class).to(ExportFileSupport.class).in(Singleton.class);
        bind(IExporterRegistry.class).to(ExporterRegistry.class).in(Singleton.class);
        bind(IExportService.class).to(ExportService.class).in(Singleton.class);
    }
}
