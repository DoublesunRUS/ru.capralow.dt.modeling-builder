/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.bsl.internal.xbsl;

import org.eclipse.xtext.service.AbstractGenericModule;

import com.google.inject.Binder;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.bsl.internal.xbsl.impl.ExportModuleFileSystemSupport;
import ru.capralow.dt.modeling.core.IExportFileSystemSupport;
import ru.capralow.dt.modeling.yaml.ExtensionBasedExporterQualifier;
import ru.capralow.dt.modeling.yaml.IExporterQualifier;

public class RuntimeModule
    extends AbstractGenericModule
{
    public void configureIExporterQualifier(final Binder binder)
    {
        binder.bind(IExporterQualifier.class).to(ExtensionBasedExporterQualifier.class).in(Singleton.class);
    }

    public void configureIProjectFileSystemSupport(final Binder binder)
    {
        binder.bind(IExportFileSystemSupport.class).to(ExportModuleFileSystemSupport.class);
    }
}
