/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.internal.core;

import org.eclipse.osgi.util.NLS;

final class Messages
    extends NLS
{
    private static final String BUNDLE_NAME = "ru.capralow.dt.modeling.internal.core.messages"; //$NON-NLS-1$

    public static String ExportOperation_exporting;
    public static String ExportOperation_export_operation_success;
    public static String ExportOperation_waiting_for_build_of_configuration;
    public static String ExportOperation_export_operation_canceled_by_user;
    public static String ExportOperation_export_operation_has_several_errors;
    public static String ExportOperation_export_operation_has_error__0;

    public static String ExportServiceRegistry_unexpected_error;
    public static String ExportServiceRegistry_exportServiceVersion0NotRegistered;

    static
    {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }
}
