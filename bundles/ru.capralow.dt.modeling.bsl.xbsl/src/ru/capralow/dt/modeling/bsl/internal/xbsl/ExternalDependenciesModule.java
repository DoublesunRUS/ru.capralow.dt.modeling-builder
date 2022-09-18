/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.bsl.internal.xbsl;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.resource.IResourceServiceProvider;

import com._1c.g5.v8.dt.bsl.model.resource.owner.IBslOwnerComputerRegistry;
import com._1c.g5.v8.dt.bsl.model.resource.owner.IBslOwnerComputerService;
import com._1c.g5.v8.dt.bsl.resource.DynamicFeatureAccessComputer;
import com._1c.g5.v8.dt.bsl.resource.TypesComputer;
import com._1c.g5.v8.dt.bsl.resource.extension.IBslResourceExtensionManager;
import com._1c.g5.v8.dt.core.filesystem.IProjectFileSystemSupportProvider;
import com._1c.g5.v8.dt.core.filesystem.IQualifiedNameFilePathConverter;
import com._1c.g5.v8.dt.core.platform.IBmModelManager;
import com._1c.g5.v8.dt.core.platform.IResourceLookup;
import com._1c.g5.v8.dt.core.platform.IV8ProjectManager;
import com._1c.g5.v8.dt.md.IExternalPropertyManagerRegistry;
import com._1c.g5.wiring.AbstractServiceAwareModule;
import com._1c.g5.wiring.ServiceProperties;

public class ExternalDependenciesModule
    extends AbstractServiceAwareModule
{
    public ExternalDependenciesModule(Plugin bundle)
    {
        super(bundle);
    }

    @Override
    protected void doConfigure()
    {
        bind(IBmModelManager.class).toService();
        bind(IBslOwnerComputerRegistry.class).toService();
        bind(IBslOwnerComputerService.class).toService();
        bind(IBslResourceExtensionManager.class).toService();
        bind(IExternalPropertyManagerRegistry.class).toService();
        bind(IProjectFileSystemSupportProvider.class).toService();
        bind(IQualifiedNameFilePathConverter.class).toService();
        bind(IQualifiedNameProvider.class).toService(ServiceProperties.contributed("com._1c.g5.v8.dt.bsl")); //$NON-NLS-1$
        bind(IResourceLookup.class).toService();
        bind(IV8ProjectManager.class).toService();

        URI uri = URI.createURI("*.bsl"); //$NON-NLS-1$
        final IResourceServiceProvider rsp = IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(uri);

        bind(TypesComputer.class).toProvider(() -> rsp.get(TypesComputer.class));
        bind(DynamicFeatureAccessComputer.class).toProvider(() -> rsp.get(DynamicFeatureAccessComputer.class));
    }
}
