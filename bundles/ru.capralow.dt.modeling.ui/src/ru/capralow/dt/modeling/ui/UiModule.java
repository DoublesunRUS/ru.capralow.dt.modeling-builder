/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.ui;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import ru.capralow.dt.modeling.internal.ui.ExportConfigurationWizardPage;

public class UiModule
    extends AbstractModule
{
    public static final String EXPORT_WIZARD_PAGE_QUALIFIER =
        "1C:Enterprise Development Tools Configuration Export Page"; //$NON-NLS-1$
    public static final String EXPORT_OBJECT_WIZARD_PAGE_QUALIFIER =
        "ExportExternalObjectWizardPage.STORE_DESTINATION_NAMES_ID"; //$NON-NLS-1$

    @Override
    protected void configure()
    {
        bind(IExportWizardPage.class).annotatedWith(Names.named(EXPORT_WIZARD_PAGE_QUALIFIER))
            .to(ExportConfigurationWizardPage.class);
    }
}
