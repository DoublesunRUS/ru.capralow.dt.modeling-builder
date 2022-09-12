/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.internal.ui;

import org.eclipse.core.runtime.Plugin;

import com._1c.g5.ides.monitoring.IMonitoringEventDispatcher;

public class ExternalDependenciesModule
    extends ru.capralow.dt.modeling.core.ExternalDependenciesModule
{

    public ExternalDependenciesModule(Plugin bundle)
    {
        super(bundle);
    }

    @Override
    protected void doConfigure()
    {
        super.doConfigure();
        bind(IMonitoringEventDispatcher.class).toService();
    }

}
