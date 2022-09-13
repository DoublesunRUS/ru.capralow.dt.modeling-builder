/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.internal.yaml;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;

import com._1c.g5.v8.dt.platform.RuntimeCompatibility;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.common.collect.Lists;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.yaml.IExporter;
import ru.capralow.dt.modeling.yaml.IExporterRegistry;

@Singleton
public class ExporterRegistry
    implements IExporterRegistry
{
    private static final String EXTENSION_POINT_ID = "ru.capralow.dt.modeling.yaml.exporter"; //$NON-NLS-1$

    private static final String ATT_CLASS = "class"; //$NON-NLS-1$

    private static final String ATT_RUNTIME = "runtime"; //$NON-NLS-1$

    private Map<Version, List<IExporter>> registry;

    private volatile boolean initialized;

    private Object lock = new Object();

    @Override
    public IExporter getExporter(Version version, EObject eObject) throws ExportException
    {
        return getExporters().getOrDefault(version, Collections.emptyList())
            .stream()
            .filter(e -> e.isAppropriate(version, eObject))
            .findFirst()
            .orElseThrow(() -> createExportNotRegistredException(version, eObject));
    }

    @Override
    public boolean exporterExists(Version version, EObject eObject)
    {
        return (getExporters().getOrDefault(version, Collections.emptyList())).stream()
            .anyMatch(e -> e.isAppropriate(version, eObject));
    }

    private Map<Version, List<IExporter>> loadExporters()
    {
        Map<Version, List<IExporter>> registry1 = new HashMap<>();
        IConfigurationElement[] configurationElements =
            Platform.getExtensionRegistry().getConfigurationElementsFor(EXTENSION_POINT_ID);
        IConfigurationElement[] arrayOfIConfigurationElement1 = configurationElements;
        for (int i = arrayOfIConfigurationElement1.length, b = 0; b < i;)
        {
            IConfigurationElement configurationElement = arrayOfIConfigurationElement1[b];
            List<Version> versions = getVersions(configurationElement);
            if (!versions.isEmpty())
            {
                try
                {
                    IExporter exporter = (IExporter)configurationElement.createExecutableExtension(ATT_CLASS);
                    for (Version exporterVersion : versions)
                    {
                        registry1.computeIfAbsent(exporterVersion, k -> new ArrayList<>()).add(exporter);
                    }
                }
                catch (CoreException e)
                {
                    YamlPlugin.log(e.getStatus());
                }
            }
            b++;
        }
        return registry1;
    }

    private List<Version> getVersions(IConfigurationElement configurationElement)
    {
        List<Version> versions = Lists.newArrayList();
        byte b;
        int i;
        IConfigurationElement[] arrayOfIConfigurationElement = configurationElement.getChildren(ATT_RUNTIME);
        for (i = arrayOfIConfigurationElement.length, b = 0; b < i;)
        {
            IConfigurationElement runtimeElement = arrayOfIConfigurationElement[b];
            try
            {
                versions.addAll(RuntimeCompatibility.computeVersions(runtimeElement));
            }
            catch (CoreException e)
            {
                YamlPlugin.log(e.getStatus());
            }
            b++;
        }
        return versions;
    }

    private Map<Version, List<IExporter>> getExporters()
    {
        if (!this.initialized)
        {
            synchronized (this.lock)
            {
                if (!this.initialized)
                {
                    Map<Version, List<IExporter>> exporters = loadExporters();
                    this.registry = exporters;
                    this.initialized = true;
                    return exporters;
                }
            }
        }
        return this.registry;
    }

    private ExportException createExportNotRegistredException(Version version, EObject eObject)
    {
        return new ExportException(String
            .valueOf(MessageFormat.format(Messages.ExporterRegistry_exporter_not_registered__0__version__1,
                new Object[] { eObject.eClass().getName(), version.toString() }))
            + " all exporters by version: "
            + (getExporters().get(version)).stream()
                .map(item -> item.getClass().getName())
                .collect(Collectors.joining(", "))); //$NON-NLS-1$
    }
}
