/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.internal.yaml;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.xtext.naming.IQualifiedNameProvider;

import com._1c.g5.modeling.xtext.scoping.IIndexSlicePredicateService;
import com._1c.g5.v8.dt.bm.index.emf.IBmEmfIndexManager;
import com._1c.g5.v8.dt.bm.index.rights.IBmRightsIndexManager;
import com._1c.g5.v8.dt.core.filesystem.IProjectFileSystemSupportProvider;
import com._1c.g5.v8.dt.core.filesystem.IQualifiedNameFilePathConverter;
import com._1c.g5.v8.dt.core.model.IModelEditingSupport;
import com._1c.g5.v8.dt.core.naming.ISymbolicNameService;
import com._1c.g5.v8.dt.core.naming.ITopObjectFqnGenerator;
import com._1c.g5.v8.dt.core.navigator.order.index.IBmNavigatorOrderIndexManager;
import com._1c.g5.v8.dt.core.platform.IBmModelManager;
import com._1c.g5.v8.dt.core.platform.IConfigurationProjectManager;
import com._1c.g5.v8.dt.core.platform.IConfigurationProvider;
import com._1c.g5.v8.dt.core.platform.IDerivedDataManagerProvider;
import com._1c.g5.v8.dt.core.platform.IDtProjectManager;
import com._1c.g5.v8.dt.core.platform.IExtensionProjectManager;
import com._1c.g5.v8.dt.core.platform.IExternalObjectProjectManager;
import com._1c.g5.v8.dt.core.platform.IMdObjectByTypeProvider;
import com._1c.g5.v8.dt.core.platform.IResourceLookup;
import com._1c.g5.v8.dt.core.platform.IV8ProjectManager;
import com._1c.g5.v8.dt.core.provider.IResourceContentExporterExtensionManager;
import com._1c.g5.v8.dt.core.provider.IResourceContentImporterExtensionManager;
import com._1c.g5.v8.dt.md.ICommonAttributeCacheProvider;
import com._1c.g5.v8.dt.md.IExternalPropertyManagerRegistry;
import com._1c.g5.v8.dt.md.IModelParentSupport;
import com._1c.g5.v8.dt.md.typeinfo.IMdTypeIndex;
import com._1c.g5.v8.dt.platform.IRuntimeRegistry;
import com._1c.g5.v8.dt.platform.core.typeinfo.category.IClassifiersTypeInfoServiceProvider;
import com._1c.g5.v8.dt.platform.core.typeinfo.category.ITypeInfoCategoryServiceProvider;
import com._1c.g5.v8.dt.platform.version.IRuntimeVersionSupport;
import com._1c.g5.v8.dt.validation.marker.IMarkerManager;
import com._1c.g5.wiring.AbstractServiceAwareModule;
import com.e1c.g5.dt.core.api.scoping.IDtProjectGlobalScopeProvider;

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
        bind(IRuntimeVersionSupport.class).toService();
        bind(IRuntimeRegistry.class).toService();
        bind(IConfigurationProvider.class).toService();
        bind(IMdObjectByTypeProvider.class).toService();
        bind(IBmModelManager.class).toService();
        bind(IProjectFileSystemSupportProvider.class).toService();
        bind(IQualifiedNameFilePathConverter.class).toService();
        bind(IModelParentSupport.class).toService();
        bind(ICommonAttributeCacheProvider.class).toService();
        bind(IResourceLookup.class).toService();
        bind(IQualifiedNameProvider.class).toService();
        bind(ISymbolicNameService.class).toService();
        bind(ITopObjectFqnGenerator.class).toService();
        bind(IDerivedDataManagerProvider.class).toService();
        bind(IExtensionProjectManager.class).toService();
        bind(IExternalObjectProjectManager.class).toService();
        bind(IConfigurationProjectManager.class).toService();
        bind(IV8ProjectManager.class).toService();
        bind(IExternalPropertyManagerRegistry.class).toService();
        bind(IModelEditingSupport.class).toService();
        bind(IDtProjectManager.class).toService();
        bind(ITypeInfoCategoryServiceProvider.class).toService();
        bind(IDtProjectGlobalScopeProvider.class).toService();
        bind(IIndexSlicePredicateService.class).toService();
        bind(IClassifiersTypeInfoServiceProvider.class).toService();
        bind(IMarkerManager.class).toService();
        bind(IBmRightsIndexManager.class).toService();
        bind(IBmEmfIndexManager.class).toService();
        bind(IBmNavigatorOrderIndexManager.class).toService();
        bind(IResourceContentImporterExtensionManager.class).toService();
        bind(IResourceContentExporterExtensionManager.class).toService();
        bind(IMdTypeIndex.class).toService();
    }
}
