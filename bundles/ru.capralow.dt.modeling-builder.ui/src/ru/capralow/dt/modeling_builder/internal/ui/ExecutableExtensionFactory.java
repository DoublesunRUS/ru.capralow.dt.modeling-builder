/**
 * Copyright (c) 2020, Alexander Kapralov
 */
package ru.capralow.dt.modeling_builder.internal.ui;

import org.osgi.framework.Bundle;

import com._1c.g5.wiring.AbstractGuiceAwareExecutableExtensionFactory;
import com.google.inject.Injector;

public class ExecutableExtensionFactory
    extends AbstractGuiceAwareExecutableExtensionFactory
{

    @Override
    protected Bundle getBundle()
    {
        return UiPlugin.getInstance().getBundle();
    }

    @Override
    protected Injector getInjector()
    {
        return UiPlugin.getInstance().getInjector();
    }

}
