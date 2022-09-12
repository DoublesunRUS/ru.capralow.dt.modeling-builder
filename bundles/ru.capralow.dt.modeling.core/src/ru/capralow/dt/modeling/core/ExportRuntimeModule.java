/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.core;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.internal.core.ExportOperationFactory;

public class ExportRuntimeModule
    extends AbstractModule
{
    @Override
    protected void configure()
    {
        this.bind(IExportOperationFactory.class).to(ExportOperationFactory.class).in(Singleton.class);
    }
}
