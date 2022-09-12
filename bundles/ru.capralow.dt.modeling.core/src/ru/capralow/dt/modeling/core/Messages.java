/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.core;

import org.eclipse.osgi.util.NLS;

final class Messages
    extends NLS
{
    private static final String BUNDLE_NAME = "ru.capralow.dt.modeling.core.messages"; //$NON-NLS-1$

    public static String Internal_class;

    static
    {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }
}
