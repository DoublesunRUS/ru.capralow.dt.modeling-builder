/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.internal.core;

import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.osgi.service.debug.DebugOptionsListener;
import org.osgi.framework.BundleContext;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import ru.capralow.dt.modeling.core.ExportDebugTrace;
import ru.capralow.dt.modeling.core.ExportRuntimeModule;
import ru.capralow.dt.modeling.core.ExternalDependenciesModule;

public class CorePlugin
    extends Plugin
{
    public static final String ID = "ru.capralow.dt.modeling.core"; //$NON-NLS-1$

    private static CorePlugin instance;

    private Injector injector;

    public static IStatus createErrorStatus(String message)
    {
        return new Status(IStatus.ERROR, ID, 0, message, (Throwable)null);
    }

    public static IStatus createErrorStatus(String message, int code)
    {
        return new Status(IStatus.ERROR, ID, code, message, (Throwable)null);
    }

    public static IStatus createErrorStatus(String message, int code, Throwable throwable)
    {
        return new Status(IStatus.ERROR, ID, code, message, throwable);
    }

    public static IStatus createErrorStatus(String message, Throwable throwable)
    {
        return new Status(IStatus.ERROR, ID, 0, message, throwable);
    }

    public static CorePlugin getInstance()
    {
        return instance;
    }

    public static void log(IStatus status)
    {
        getInstance().getLog().log(status);
    }

    @Override
    public void start(BundleContext context) throws Exception
    {
        super.start(context);

        instance = this;

        Dictionary<String, String> props = new Hashtable<>(4);
        props.put("listener.symbolic.name", "ru.capralow.dt.modeling"); //$NON-NLS-1$ //$NON-NLS-2$
        context.registerService(DebugOptionsListener.class.getName(), ExportDebugTrace.getInstance(), props);
    }

    @Override
    public void stop(BundleContext context) throws Exception
    {
        injector = null;

        instance = null;

        super.stop(context);
    }

    public Injector getInjector()
    {
        if (injector == null)
        {
            injector = createInjector();
        }
        return injector;
    }

    private Injector createInjector()
    {
        try
        {
            return Guice
                .createInjector(new Module[] { new ExportRuntimeModule(), new ExternalDependenciesModule(this) });
        }
        catch (Exception e)
        {
            log(createErrorStatus("Failed to create injector for " + this.getBundle().getSymbolicName(), e));
            throw new RuntimeException("Failed to create injector for " + this.getBundle().getSymbolicName(), e);
        }
    }

}
