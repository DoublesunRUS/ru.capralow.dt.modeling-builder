/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.internal.yaml.writer;

import org.eclipse.osgi.util.NLS;

class Messages
    extends NLS
{
    private static final String BUNDLE_NAME = "ru.capralow.dt.modeling.md.internal.yaml.writer.messages"; //$NON-NLS-1$

    public static String ProducedTypeWriter_error_exporting__0__produced_type;

    public static String PredefinedItemCodeWriter_unknown_type_of_predefined_item_code;

    public static String ElementWriter_Invalid_feature_type_0;

    static
    {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }
}
