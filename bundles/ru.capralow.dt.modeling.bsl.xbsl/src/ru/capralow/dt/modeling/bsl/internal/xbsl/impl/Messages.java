/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.bsl.internal.xbsl.impl;

import org.eclipse.osgi.util.NLS;

class Messages
    extends NLS
{
    private static final String BUNDLE_NAME = "ru.capralow.dt.modeling.bsl.internal.xbsl.impl"; //$NON-NLS-1$

    public static String ExportModuleFileSystemSupport_Error_getting_parent_object_for_module__0;

    public static String ModuleExporter_Cant_get_file_name;

    public static String ModuleExporter_Export_file__0;

    public static String ExportModuleFileSystemSupport_failed_to_get_module_path_for_0_basic_form_1;

    public static String ExportModuleFileSystemSupport_failed_to_get_module_path_for_0_metadata_object_1;

    static
    {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }
}
