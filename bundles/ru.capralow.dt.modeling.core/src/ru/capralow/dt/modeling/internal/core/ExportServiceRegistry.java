/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.internal.core;

import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;

import com._1c.g5.v8.dt.platform.RuntimeCompatibility;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.core.IExportFileSupport;
import ru.capralow.dt.modeling.core.IExportService;
import ru.capralow.dt.modeling.core.IExportServiceRegistry;

@Singleton
public class ExportServiceRegistry
    implements IExportServiceRegistry
{
    private static final String EXTENSION_POINT_ID = "ru.capralow.dt.modeling.core.exportService"; //$NON-NLS-1$
    private static final String ATT_CLASS = "class"; //$NON-NLS-1$
    private static final String ATT_EXPORT_FiLE_SUPPORT_CLASS = "exportFileSupportClass"; //$NON-NLS-1$
    private static final String ATT_RUNTIME = "runtime"; //$NON-NLS-1$
    private Map<Version, IConfigurationElement> exportServiceRegistry;
    private Map<Version, IExportFileSupport> exportFileSupportRegistry;
    private volatile boolean isInitialized;

    @Override
    public IExportService getExportService(final Version version) throws ExportException
    {
        final IConfigurationElement element = this.getExportServiceRegistry().get(version);
        if (element != null)
        {
            try
            {
                return (IExportService)element.createExecutableExtension(ATT_CLASS);
            }
            catch (CoreException ex)
            {
                throw new ExportException(Messages.ExportServiceRegistry_unexpected_error, ex);
            }
        }
        throw new ExportException(
            Messages.bind(Messages.ExportServiceRegistry_exportServiceVersion0NotRegistered, version.toString()));
    }

    @Override
    public IExportFileSupport getExportFileSupport(final Version version) throws ExportException
    {
        final IExportFileSupport support = this.getExportFileSupportRegistry().get(version);
        if (support != null)
        {
            return support;
        }
        throw new ExportException(
            Messages.bind(Messages.ExportServiceRegistry_exportServiceVersion0NotRegistered, version.toString()));
    }

    private synchronized Map<Version, IConfigurationElement> getExportServiceRegistry()
    {
        if (!this.isInitialized)
        {
            this.initialize();
            this.isInitialized = true;
        }
        return this.exportServiceRegistry;
    }

    private synchronized Map<Version, IExportFileSupport> getExportFileSupportRegistry()
    {
        if (!this.isInitialized)
        {
            this.initialize();
            this.isInitialized = true;
        }
        return this.exportFileSupportRegistry;
    }

    private void initialize()
    {
        final IConfigurationElement[] configurationElements =
            Platform.getExtensionRegistry().getConfigurationElementsFor(EXTENSION_POINT_ID);
        final ImmutableMap.Builder<Version, IConfigurationElement> exportServiceBuilder = ImmutableMap.builder();
        final ImmutableMap.Builder<Version, IExportFileSupport> exportFileSupportBuilder = ImmutableMap.builder();
        IConfigurationElement[] array = configurationElements;
        for (int length = array.length, i = 0; i < length; ++i)
        {
            final IConfigurationElement configurationElement = array[i];
            final List<Version> versions = this.getVersions(configurationElement);
            if (!versions.isEmpty())
            {
                try
                {
                    final IExportFileSupport exportFileSupport = (IExportFileSupport)configurationElement
                        .createExecutableExtension(ATT_EXPORT_FiLE_SUPPORT_CLASS);
                    for (final Version version : versions)
                    {
                        exportServiceBuilder.put(version, configurationElement);
                        exportFileSupportBuilder.put(version, exportFileSupport);
                    }
                }
                catch (CoreException e)
                {
                    CorePlugin.log(e.getStatus());
                }
            }
        }
        this.exportServiceRegistry = exportServiceBuilder.build();
        this.exportFileSupportRegistry = exportFileSupportBuilder.build();
    }

    private List<Version> getVersions(final IConfigurationElement configurationElement)
    {
        final List<Version> versions = Lists.newArrayList();
        IConfigurationElement[] children = configurationElement.getChildren(ATT_RUNTIME);
        for (int length = children.length, i = 0; i < length; ++i)
        {
            final IConfigurationElement runtimeElement = children[i];
            try
            {
                versions.addAll(RuntimeCompatibility.computeVersions(runtimeElement));
            }
            catch (CoreException e)
            {
                CorePlugin.log(e.getStatus());
            }
        }
        return versions;
    }
}
