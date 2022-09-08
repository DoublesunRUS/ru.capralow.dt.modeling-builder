/**
 * Copyright (c) 2020, Alexander Kapralov
 */
package ru.capralow.dt.modeling_builder.core;

import org.eclipse.osgi.util.NLS;

final class Messages
    extends NLS
{
    private static final String BUNDLE_NAME = "ru.capralow.dt.modeling_builder.core.messages"; //$NON-NLS-1$

    public static String Internal_class;

    static
    {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }
}
