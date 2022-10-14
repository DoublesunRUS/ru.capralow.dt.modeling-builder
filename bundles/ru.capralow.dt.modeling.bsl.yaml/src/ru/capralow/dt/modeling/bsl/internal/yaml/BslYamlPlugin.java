/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.bsl.internal.yaml;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

public class BslYamlPlugin
    extends Plugin
{
    public static final String ID = "ru.capralow.dt.modeling.bsl.yaml"; //$NON-NLS-1$

    private static BslYamlPlugin instance;

    public static IStatus createErrorStatus(String msg, Throwable e)
    {
        return new Status(IStatus.ERROR, ID, 0, msg, e);
    }

    public static IStatus createWarningStatus(String msg)
    {
        return new Status(IStatus.WARNING, ID, 0, msg, null);
    }

    public static BslYamlPlugin getInstance()
    {
        return instance;
    }

    public static void log(IStatus status)
    {
        if (status != null)
        {
            instance.getLog().log(status);
        }
    }

    private Injector injector;

    public synchronized Injector getInjector()
    {
        if (injector == null)
        {
            injector = createInjector();
        }

        return injector;
    }

    @Override
    public void start(BundleContext context) throws Exception
    {
        super.start(context);

        instance = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception
    {
        this.injector = null;
        instance = null;
        super.stop(context);
    }

    private Injector createInjector()
    {
        try
        {
            return Guice.createInjector(new Module[] { new RuntimeModule(), new ExternalDependenciesModule(this) });
        }
        catch (Exception e)
        {
            log(createErrorStatus("Failed to create injector for " + getBundle().getSymbolicName(), e));
            throw new RuntimeException("Failed to create injector for " + getBundle().getSymbolicName(), e);
        }
    }
}
