/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling_builder.internal.ui;

import java.text.MessageFormat;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class UiPlugin
    extends AbstractUIPlugin
{
    public static final String ID = "ru.capralow.dt.modeling-builder.launcher.ui"; //$NON-NLS-1$

    private static UiPlugin instance;

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

    public static UiPlugin getInstance()
    {
        return instance;
    }

    public static void log(IStatus status)
    {
        getInstance().getLog().log(status);
    }

    private Injector injector;

    public synchronized Injector getInjector()
    {
        if (injector == null)
            injector = createInjector();

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
        instance = null;

        super.stop(context);
    }

    private Injector createInjector()
    {
        try
        {
            return Guice.createInjector(new ExternalDependenciesModule(this));

        }
        catch (Exception e)
        {
            String msg = MessageFormat.format(Messages.UnitLauncherPlugin_Failed_to_create_injector_for_0,
                getBundle().getSymbolicName());
            log(createErrorStatus(msg, e));
            return null;

        }
    }

}
