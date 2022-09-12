/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.internal.ui;

import org.eclipse.osgi.util.NLS;

final class Messages
    extends NLS
{
    private static final String BUNDLE_NAME = "ru.capralow.dt.modeling.internal.ui.messages"; //$NON-NLS-1$

    public static String Plugin_Failed_to_create_injector_for_0;

    public static String ExportConfigurationWizard_title;

    public static String ExportConfigurationWizardPage_title;
    public static String ExportConfigurationWizardPage_description;
    public static String ExportConfigurationWizardPage_projectHasError;
    public static String ExportConfigurationWizardPage_projectHasWarning;
    public static String ExportConfigurationWizardPage_projectNotValid;
    public static String ExportConfigurationWizardPage_messageTargetDirNotExist;
    public static String ExportConfigurationWizardPage_messageTargetDirNotEmpty;
    public static String ExportConfigurationWizardPage_Message_target_zip_file_exist_replace;
    public static String ExportConfigurationWizardPage_An_IO_exception_occurred_with_specified_target_path__0;
    public static String ExportConfigurationWizardPage_labelV8Project;
    public static String ExportConfigurationWizardPage_To_directory;
    public static String ExportConfigurationWizardPage_labelBrowse;
    public static String ExportConfigurationWizardPage_To_archive;
    public static String ExportConfigurationWizardPage_browseDialogMessage;
    public static String ExportConfigurationWizardPage_exportProblem;
    public static String ExportConfigurationWizardPage_question;
    public static String ExportConfigurationWizardPage_executionErrorMessage;
    public static String ExportConfigurationWizardPage_Select_target_directory_message;
    public static String ExportConfigurationWizardPage_targetAlredyExistAsFile;
    public static String ExportConfigurationWizardPage_Select_target_archive_message;
    public static String ExportConfigurationWizardPage_info;
    public static String ExportConfigurationWizardPage_noConfigurationInProject;

    static
    {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }
}
