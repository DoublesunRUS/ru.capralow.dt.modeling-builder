/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.ui;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;

public interface IExportWizardPage
    extends IWizardPage
{
    boolean finish();

    void setSelection(IStructuredSelection var1);
}
