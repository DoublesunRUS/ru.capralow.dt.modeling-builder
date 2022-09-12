/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.internal.ui;

import java.text.MessageFormat;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import ru.capralow.dt.modeling.core.ExportRuntimeModule;
import ru.capralow.dt.modeling.ui.UiModule;

public class UiPlugin
    extends AbstractUIPlugin
{
    public static final String ID = "ru.capralow.dt.modeling.ui"; //$NON-NLS-1$
    public static final String T_WIZBAN = "/wizban/"; //$NON-NLS-1$
    public static final String IMG_EXPORT_WIZ = "ru.capralow.dt.modeling.ui/wizban/export_wiz.png"; //$NON-NLS-1$

    private static UiPlugin instance;

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

    public static UiPlugin getInstance()
    {
        return instance;
    }

    public static void log(IStatus status)
    {
        getInstance().getLog().log(status);
    }

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
        instance = null;

        super.stop(context);
    }

    private Injector createInjector()
    {
        try
        {
            return Guice.createInjector(
                new Module[] { new ExportRuntimeModule(), new UiModule(), new ExternalDependenciesModule(this) });

        }
        catch (Exception e)
        {
            String msg =
                MessageFormat.format(Messages.Plugin_Failed_to_create_injector_for_0, getBundle().getSymbolicName());
            log(createErrorStatus(msg, e));
            return null;

        }
    }

    public static Image getImage(String symbolicName)
    {
        return getInstance().getImageRegistry().get(symbolicName);
    }

    public static ImageDescriptor getImageDescriptor(String symbolicName)
    {
        return getInstance().getImageRegistry().getDescriptor(symbolicName);
    }

    @Override
    protected void initializeImageRegistry(ImageRegistry reg)
    {
        reg.put(IMG_EXPORT_WIZ, imageDescriptorFromSymbolicName(IMG_EXPORT_WIZ));
    }

    private static ImageDescriptor imageDescriptorFromSymbolicName(String symbolicName)
    {
        String path = "icons" + symbolicName.substring(ID.length()); //$NON-NLS-1$
        return imageDescriptorFromPlugin(ID, path);
    }

}
