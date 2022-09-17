/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.internal.yaml;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

import com._1c.g5.wiring.InjectorAwareServiceRegistrator;
import com._1c.g5.wiring.ServiceInitialization;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import ru.capralow.dt.modeling.core.IExportFileSupport;
import ru.capralow.dt.modeling.core.IExportService;
import ru.capralow.dt.modeling.yaml.IExporterRegistry;

public class YamlPlugin
    extends Plugin
{
    public static final String ID = "ru.capralow.dt.modeling.yaml"; //$NON-NLS-1$

    private static YamlPlugin instance;

    public static IStatus createErrorStatus(String msg, Throwable e)
    {
        return new Status(IStatus.ERROR, ID, 0, msg, e);
    }

    public static IStatus createWarningStatus(String msg)
    {
        return new Status(IStatus.WARNING, ID, 0, msg, null);
    }

    public static YamlPlugin getInstance()
    {
        return instance;
    }

    public static void log(IStatus status)
    {
        instance.getLog().log(status);
    }

    public static void logError(String message, Throwable throwable)
    {
        log(createErrorStatus(message, throwable));
    }

    private InjectorAwareServiceRegistrator registrator;

    private volatile Injector injector;

    public Injector getInjector()
    {
        Injector localInstance = this.injector;
        if (localInstance == null)
        {
            synchronized (YamlPlugin.class)
            {
                localInstance = this.injector;
                if (localInstance == null)
                {
                    localInstance = createInjector();
                    this.injector = localInstance;
                }
            }
        }
        return localInstance;
    }

    @Override
    public void start(BundleContext context) throws Exception
    {
        super.start(context);
        instance = this;
        this.registrator = new InjectorAwareServiceRegistrator(context, this::getInjector);
        ServiceInitialization.schedule(() -> {
            this.registrator.service(IExportFileSupport.class).registerInjected();
            this.registrator.service(IExporterRegistry.class).registerInjected();
            this.registrator.managedService(IExportService.class).activateBeforeRegistration().registerInjected();
        });
    }

    @Override
    public void stop(BundleContext context) throws Exception
    {
        registrator.unregisterServices();

        injector = null;

        instance = null;

        super.stop(context);
    }

    private Injector createInjector()
    {
        try
        {
            return Guice.createInjector(new Module[] { new RuntimeModule(this), new ServicesModule() });
        }
        catch (Exception e)
        {
            log(createErrorStatus("Failed to create injector for " + getBundle().getSymbolicName(), e));
            throw new RuntimeException("Failed to create injector for " + getBundle().getSymbolicName(), e);
        }
    }
}
