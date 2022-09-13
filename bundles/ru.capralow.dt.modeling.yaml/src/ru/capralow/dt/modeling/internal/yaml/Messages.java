/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.internal.yaml;

import org.eclipse.osgi.util.NLS;

class Messages
    extends NLS
{
    private static final String BUNDLE_NAME = "ru.capralow.dt.modeling.internal.yaml.messages"; //$NON-NLS-1$

    public static String ExportService_export_operation_canceled_by_user;

    public static String ExportService_export_operation_has_error;

    public static String ExportService_export_operation_has_several_errors;

    public static String ExportService_export_operation_success;

    public static String ExportService_export_operation_completed_with_warning;

    public static String ExporterRegistry_exporter_not_registered__0__version__1;

    static
    {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }
}
