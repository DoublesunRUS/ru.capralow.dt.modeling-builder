/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.internal.yaml;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import com._1c.g5.v8.dt.core.naming.ISymbolicNameService;
import com._1c.g5.v8.dt.core.platform.IResourceLookup;
import com._1c.g5.v8.dt.core.platform.IV8ProjectManager;
import com._1c.g5.v8.dt.core.platform.management.IDtHostResourceManager;
import com._1c.g5.v8.dt.platform.version.IRuntimeVersionSupport;
import com._1c.g5.wiring.AbstractServiceAwareModule;

public class RuntimeModule
    extends AbstractServiceAwareModule
{
    public RuntimeModule(BundleContext context)
    {
        super(context);
    }

    public RuntimeModule(Plugin bundle)
    {
        super(bundle);
    }

    @Override
    protected void doConfigure()
    {
        bind(IRuntimeVersionSupport.class).toService();
        bind(IResourceLookup.class).toService();
        bind(ISymbolicNameService.class).toService();
        bind(IV8ProjectManager.class).toService();
        bind(IDtHostResourceManager.class).toService();
    }
}
