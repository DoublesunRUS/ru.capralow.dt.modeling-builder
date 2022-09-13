/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.yaml;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.EObject;

import com._1c.g5.v8.dt.platform.version.Version;

import ru.capralow.dt.modeling.internal.yaml.YamlPlugin;

public class ExtensionBasedExporterQualifier
    implements IExporterQualifier
{
    private static final String EXTENSION_POINT_ID = "ru.capralow.dt.modeling.yaml.exporterQualifier"; //$NON-NLS-1$

    private static final String ATTRIBUTE_CLASS = "class"; //$NON-NLS-1$

    private static volatile List<IExporterQualifier> qualifiers;

    @Override
    public boolean qualify(IExporter paramIExporter, Version paramVersion, EObject paramEObject)
    {
        return getQualifiers().stream()
            .allMatch(qualifier -> qualifier.qualify(paramIExporter, paramVersion, paramEObject));
    }

    private List<IExporterQualifier> getQualifiers()
    {
        if (qualifiers == null)
        {
            synchronized (this)
            {
                if (qualifiers == null)
                {
                    qualifiers = loadQualifiers();
                }
            }
        }
        return qualifiers;
    }

    private List<IExporterQualifier> loadQualifiers()
    {
        List<IExporterQualifier> result = new ArrayList<>();
        IConfigurationElement[] configurationElements =
            Platform.getExtensionRegistry().getConfigurationElementsFor(EXTENSION_POINT_ID);
        byte b;
        int i;
        IConfigurationElement[] arrayOfIConfigurationElement1 = configurationElements;
        for (i = arrayOfIConfigurationElement1.length, b = 0; b < i;)
        {
            IConfigurationElement configurationElement = arrayOfIConfigurationElement1[b];
            try
            {
                IExporterQualifier qualifier =
                    (IExporterQualifier)configurationElement.createExecutableExtension(ATTRIBUTE_CLASS);
                result.add(qualifier);
            }
            catch (CoreException e)
            {
                YamlPlugin.log(e.getStatus());
            }
            b++;
        }
        return result;
    }
}
