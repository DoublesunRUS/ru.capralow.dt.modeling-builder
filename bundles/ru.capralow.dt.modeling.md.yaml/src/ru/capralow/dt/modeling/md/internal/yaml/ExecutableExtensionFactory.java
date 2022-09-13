/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.internal.yaml;

import org.osgi.framework.Bundle;

import com._1c.g5.wiring.AbstractGuiceAwareExecutableExtensionFactory;
import com.google.inject.Injector;

public class ExecutableExtensionFactory
    extends AbstractGuiceAwareExecutableExtensionFactory
{
    @Override
    protected Bundle getBundle()
    {
        return MdYamlPlugin.getInstance().getBundle();
    }

    @Override
    protected Injector getInjector()
    {
        return MdYamlPlugin.getInstance().getInjector();
    }
}
