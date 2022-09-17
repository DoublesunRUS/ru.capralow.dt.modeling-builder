/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.internal.ui;

import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.ide.IDE;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import ru.capralow.dt.modeling.ui.IExportWizardPage;
import ru.capralow.dt.modeling.ui.UiModule;

public class ExportConfigurationWizard
    extends Wizard
    implements IExportWizard
{
    private static final String EXPORT_WIZARD = "V8ExportConfigurationWizard"; //$NON-NLS-1$

    private IStructuredSelection selection;

    @Inject
    @Named(UiModule.EXPORT_WIZARD_PAGE_QUALIFIER)
    private IExportWizardPage mainPage;

    public ExportConfigurationWizard()
    {
        final IDialogSettings workbenchSettings = UiPlugin.getInstance().getDialogSettings();
        IDialogSettings wizardSettings = workbenchSettings.getSection(EXPORT_WIZARD);
        if (wizardSettings == null)
        {
            wizardSettings = workbenchSettings.addNewSection(EXPORT_WIZARD);
        }
        this.setDialogSettings(wizardSettings);
    }

    @Override
    public void addPages()
    {
        this.addPage(this.mainPage);
    }

    @Override
    public void init(final IWorkbench workbench, final IStructuredSelection currentSelection)
    {
        this.initSelection(workbench, currentSelection);
        this.setWindowTitle(Messages.ExportConfigurationWizard_title);
        this.setDefaultPageImageDescriptor(UiPlugin.getImageDescriptor(UiPlugin.IMG_EXPORT_WIZ));
        this.setNeedsProgressMonitor(true);
    }

    @Override
    public boolean performFinish()
    {
        return this.mainPage.finish();
    }

    private void initSelection(final IWorkbench workbench, final IStructuredSelection currentSelection)
    {
        this.selection = currentSelection;
        final List<IResource> selectedResources = IDE.computeSelectedResources(currentSelection);
        if (!selectedResources.isEmpty())
        {
            this.selection = new StructuredSelection(selectedResources);
        }
        if (this.selection.isEmpty() && workbench.getActiveWorkbenchWindow() != null)
        {
            final IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
            if (page != null)
            {
                final IEditorPart currentEditor = page.getActiveEditor();
                if (currentEditor != null)
                {
                    final Object selectedResource = currentEditor.getEditorInput().getAdapter(IResource.class);
                    if (selectedResource != null)
                    {
                        this.selection = new StructuredSelection(selectedResource);
                    }
                }
            }
        }
        this.mainPage.setSelection(this.selection);
    }
}
