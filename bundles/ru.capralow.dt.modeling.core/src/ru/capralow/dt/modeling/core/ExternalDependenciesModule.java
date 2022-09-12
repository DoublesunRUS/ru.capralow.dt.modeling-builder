/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.core;

import org.eclipse.core.runtime.Plugin;

import com._1c.g5.v8.activitytracking.core.ISystemIdleService;
import com._1c.g5.v8.dt.core.platform.IBmModelManager;
import com._1c.g5.v8.dt.core.platform.IConfigurationProvider;
import com._1c.g5.v8.dt.core.platform.IDerivedDataManagerProvider;
import com._1c.g5.v8.dt.core.platform.IResourceLookup;
import com._1c.g5.v8.dt.core.platform.IV8ProjectManager;
import com._1c.g5.v8.dt.platform.version.IRuntimeVersionSupport;
import com._1c.g5.wiring.AbstractServiceAwareModule;

public class ExternalDependenciesModule
    extends AbstractServiceAwareModule
{
    public ExternalDependenciesModule(final Plugin bundle)
    {
        super(bundle);
    }

    @Override
    protected void doConfigure()
    {
        this.bind(IRuntimeVersionSupport.class).toService();
        this.bind(IBmModelManager.class).toService();
        this.bind(IConfigurationProvider.class).toService();
        this.bind(IExportFileSupport.class).toService();
        this.bind(IResourceLookup.class).toService();
        this.bind(ISystemIdleService.class).toService();
        this.bind(IDerivedDataManagerProvider.class).toService();
        this.bind(IV8ProjectManager.class).toService();
    }
}
