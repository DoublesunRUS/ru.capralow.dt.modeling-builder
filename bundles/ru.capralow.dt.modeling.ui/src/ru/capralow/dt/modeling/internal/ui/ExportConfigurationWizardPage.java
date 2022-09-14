/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.internal.ui;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.eclipse.core.databinding.AggregateValidationStatus;
import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.SelectObservableValue;
import org.eclipse.core.databinding.observable.value.WritableValue;
import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.databinding.viewers.typed.ViewerProperties;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.DialogSettings;
import org.eclipse.jface.dialogs.IPageChangingListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.PageChangingEvent;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;

import com._1c.g5.ides.monitoring.BusinessEvent;
import com._1c.g5.ides.monitoring.IMonitoringEventDispatcher;
import com._1c.g5.v8.dt.common.ui.dialogs.StatusDialog;
import com._1c.g5.v8.dt.common.ui.jface.viewers.DialogSettingsBasedHistory;
import com._1c.g5.v8.dt.core.ICoreConstants;
import com._1c.g5.v8.dt.core.platform.IConfigurationAware;
import com._1c.g5.v8.dt.core.platform.IDtProject;
import com._1c.g5.v8.dt.core.platform.IV8Project;
import com._1c.g5.v8.dt.core.platform.IV8ProjectManager;
import com._1c.g5.v8.dt.metadata.mdclass.Configuration;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.inject.Inject;

import ru.capralow.dt.modeling.core.IExportArtifactBuilderFactory;
import ru.capralow.dt.modeling.core.IExportOperation;
import ru.capralow.dt.modeling.core.IExportOperationFactory;
import ru.capralow.dt.modeling.core.IExportStrategy;
import ru.capralow.dt.modeling.core.ZipArchiveBuilder;
import ru.capralow.dt.modeling.ui.IExportWizardPage;

public class ExportConfigurationWizardPage
    extends WizardPage
    implements IPageChangingListener, IExportWizardPage
{
    public static final String PAGE_NAME = "1C:Enterprise Development Tools Configuration Export Page";
    private static final String[] ARCHIVE_EXTENSIONS;
    private static final String DIR_TARGET_PATH_HISTORY_SECTION = "dirTargetPaths"; //$NON-NLS-1$
    private static final String ZIP_TARGET_PATH_HISTORY_SECTION = "zipTargetPaths"; //$NON-NLS-1$
    private static final int TARGET_PATH_HISTORY_LENGTH = 5;
    private static final Point DEFAULT_PAGE_SIZE;
    private String previouslyBrowsedDirectory;
    private String previouslyBrowsedArchive;
    private Point initialPageSize;
    private SelectObservableValue<Boolean> isDirectoryTargetSelected;
    private IObservableValue<String> targetDirPathValue;
    private IObservableValue<String> targetZipPathValue;
    private IObservableValue<IV8Project> projectValue;
    private List<IV8Project> projects;
    private DataBindingContext dataBindingContext;
    private Map<String, DialogSettingsBasedHistory> history;
    @Inject
    private IExportOperationFactory exportOperationFactory;
    @Inject
    private IV8ProjectManager projectManager;
    @Inject
    private IMonitoringEventDispatcher monitoringEventDispatcher;

    static
    {
        ARCHIVE_EXTENSIONS = new String[] { "*.zip" }; //$NON-NLS-1$
        DEFAULT_PAGE_SIZE = new Point(720, 300);
    }

    public ExportConfigurationWizardPage()
    {
        super(PAGE_NAME);
        previouslyBrowsedDirectory = ""; //$NON-NLS-1$
        previouslyBrowsedArchive = ""; //$NON-NLS-1$
        initialPageSize = ExportConfigurationWizardPage.DEFAULT_PAGE_SIZE;
        targetDirPathValue = new WritableValue<>();
        targetZipPathValue = new WritableValue<>();
        projectValue = new WritableValue<>();
        history = new HashMap<>();
        setTitle(Messages.ExportConfigurationWizardPage_title);
        setDescription(Messages.ExportConfigurationWizardPage_description);
        setPageComplete(false);
    }

    @Override
    public void setSelection(final IStructuredSelection selection)
    {
        if (selection != null)
        {
            final Object resource = selection.getFirstElement();
            IV8Project v8Project = null;
            if (resource instanceof IProject)
            {
                v8Project = projectManager.getProject((IProject)resource);
            }
            else if (resource instanceof EObject)
            {
                v8Project = projectManager.getProject((EObject)resource);
            }
            if (isAvailableProject(v8Project))
            {
                projectValue.setValue(v8Project);
            }
        }
    }

    @Override
    public void createControl(final Composite parent)
    {
        initializeDialogUnits(parent);
        final Composite pageArea = new Composite(parent, 0);
        setControl(pageArea);
        GridLayoutFactory.fillDefaults().applyTo(pageArea);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(pageArea);
        projects = initAvaliableProjectsList();
        createPageArea(pageArea);
        createWizardValidationManager();
        initWizardDialogPageChangingListener();
        Dialog.applyDialogFont(parent);
    }

    @Override
    public boolean finish()
    {
        PlatformUI.getWorkbench().saveAllEditors(true);
        if (!prepareProject() || !prepareTargetPath())
        {
            return false;
        }
        history.get(getTargetPathHistorySection()).addHistoryEntry(getTargetPathValue().getValue().trim());
        return executeExport();
    }

    @Override
    public void dispose()
    {
        if (getWizard().getContainer() instanceof WizardDialog)
        {
            ((WizardDialog)getWizard().getContainer()).removePageChangingListener(this);
        }
        super.dispose();
    }

    @Override
    public void handlePageChanging(final PageChangingEvent event)
    {
        getShell()
            .setSize(equals(event.getTargetPage()) ? ExportConfigurationWizardPage.DEFAULT_PAGE_SIZE : initialPageSize);
    }

    private boolean prepareProject()
    {
        try
        {
            final int severity = projectValue.getValue()
                .getProject()
                .findMaxProblemSeverity(IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
            if (severity == IMarker.SEVERITY_ERROR)
            {
                return queryYesNoQuestion(Messages.ExportConfigurationWizardPage_projectHasError);
            }
            if (severity == IMarker.SEVERITY_WARNING)
            {
                return queryYesNoQuestion(Messages.ExportConfigurationWizardPage_projectHasWarning);
            }
        }
        catch (CoreException e)
        {
            displayErrorDialog(Messages.ExportConfigurationWizardPage_projectNotValid);
            return false;
        }
        return true;
    }

    private boolean prepareTargetPath()
    {
        Path target = Paths.get(getTargetPathValue().getValue(), new String[0]);
        Boolean isDirectoryTarget = isDirectoryTargetSelected.getValue();
        Path targetDirectory = isDirectoryTarget ? target : target.getParent();

        try
        {
            if (Files.notExists(targetDirectory, new LinkOption[0]))
            {
                if (!queryYesNoQuestion(Messages.ExportConfigurationWizardPage_messageTargetDirNotExist))
                {
                    return false;
                }
                Files.createDirectories(targetDirectory, new FileAttribute[0]);
            }
            if (isDirectoryTarget)
            {
                if (Files.list(target).findAny().isPresent())
                {
                    if (!queryYesNoQuestion(Messages.ExportConfigurationWizardPage_messageTargetDirNotEmpty))
                    {
                        return false;
                    }

                    Files.walk(targetDirectory)
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .filter(item -> !item.getPath().equals(targetDirectory.toAbsolutePath().toString()))
                        .forEach(File::delete);
                }
            }
            else if (Files.isRegularFile(target, new LinkOption[0]))
            {
                if (!this
                    .queryYesNoQuestion(Messages.ExportConfigurationWizardPage_Message_target_zip_file_exist_replace))
                {
                    return false;
                }
                Files.delete(target);
            }
            return true;
        }
        catch (IOException e)
        {
            displayErrorDialog(MessageFormat.format(
                Messages.ExportConfigurationWizardPage_An_IO_exception_occurred_with_specified_target_path__0,
                targetDirectory));
            return false;
        }
    }

    private boolean executeExport()
    {
        final IExportOperation exportOperation = createExportOperation();
        final IStatus[] exportStatus = { null };
        try
        {
            getContainer().run(true, true, new IRunnableWithProgress()
            {
                @Override
                public void run(final IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
                {
                    exportStatus[0] = exportOperation.run(monitor);
                }
            });
        }
        catch (InterruptedException ex)
        {
            // Nothing to do
        }
        catch (InvocationTargetException e)
        {
            UiPlugin.log(UiPlugin.createErrorStatus(Messages.ExportConfigurationWizardPage_executionErrorMessage, e));
            displayErrorDialog(Messages.ExportConfigurationWizardPage_executionErrorMessage);
            return true;
        }
        if (!exportStatus[0].isOK())
        {
            StatusDialog.open(getShell(), getTitle(), exportStatus[0]);
        }
        else
        {
            displayOkStatusDialog(exportStatus[0].getMessage());
        }
        dispatchProjectExportedEvent(projectValue.getValue(), exportStatus[0]);
        return true;
    }

    private IExportOperation createExportOperation()
    {
        final Version version = projectValue.getValue().getVersion();
        final EObject object = ((IConfigurationAware)projectValue.getValue()).getConfiguration();
        if (isDirectoryTargetSelected.getValue())
        {
            final Path targetPath = Path.of(targetDirPathValue.getValue(), new String[0]);
            return exportOperationFactory.createExportOperation(targetPath, version, new EObject[] { object });
        }
        final Path targetPath = Path.of(targetZipPathValue.getValue(), new String[0]);
        final IExportArtifactBuilderFactory zipArtifactBuilderFactory =
            () -> ZipArchiveBuilder.create(targetPath, 1, 67108864L);
        return exportOperationFactory.createExportOperation(version, IExportStrategy.DEFAULT, zipArtifactBuilderFactory,
            new EObject[] { object });
    }

    private void createPageArea(final Composite parent)
    {
        dataBindingContext = new DataBindingContext();
        final Composite areaComposite = new Composite(parent, 0);
        GridLayoutFactory.swtDefaults().numColumns(3).applyTo(areaComposite);
        GridDataFactory.swtDefaults().align(4, 4).grab(true, true).applyTo(areaComposite);
        new Label(areaComposite, 0).setText(Messages.ExportConfigurationWizardPage_labelV8Project);
        final ComboViewer projectsCombo = new ComboViewer(areaComposite, 2060);
        GridDataFactory.swtDefaults().align(4, 16777216).grab(true, false).applyTo(projectsCombo.getCombo());
        projectsCombo.setContentProvider(new ArrayContentProvider());
        projectsCombo.setLabelProvider(LabelProvider.createTextProvider(this::getProjectLabel));
        final List<Object> input = new ArrayList<>();
        input.add(""); //$NON-NLS-1$
        input.addAll(projects);
        projectsCombo.setInput(input);
        new Label(areaComposite, 0);
        final UpdateValueStrategy<Object, IV8Project> projectNameValidationStrategy = new UpdateValueStrategy<>();
        projectNameValidationStrategy.setAfterGetValidator(new ProjectValidation());
        dataBindingContext.bindValue(ViewerProperties.singleSelection().observe(projectsCombo), projectValue,
            projectNameValidationStrategy, new UpdateValueStrategy<>());
        final Button directoryTargetRadioButton = new Button(areaComposite, 16);
        directoryTargetRadioButton.setLayoutData(new GridData(1, 16777216, false, false));
        directoryTargetRadioButton.setText(Messages.ExportConfigurationWizardPage_To_directory);
        directoryTargetRadioButton.setSelection(true);
        final Combo directoryTargetPathCombo = new Combo(areaComposite, 4);
        GridDataFactory.swtDefaults().align(4, 4).grab(true, false).applyTo(directoryTargetPathCombo);
        List<String> historyList = getHistory(DIR_TARGET_PATH_HISTORY_SECTION).loadHistory();
        directoryTargetPathCombo.setItems(historyList.toArray(new String[historyList.size()]));
        final IObservableValue<String> directoryTargetPathComboValue =
            WidgetProperties.text().observe(directoryTargetPathCombo);
        final Button directoryTargetBrowseButton = new Button(areaComposite, 8);
        directoryTargetBrowseButton.setText(Messages.ExportConfigurationWizardPage_labelBrowse);
        directoryTargetBrowseButton.addSelectionListener(SelectionListener
            .widgetSelectedAdapter(e -> openTargetDirectorySelectionDialog(directoryTargetPathComboValue)));
        final Button archiveTargetRadioButton = new Button(areaComposite, 16);
        archiveTargetRadioButton.setLayoutData(new GridData(1, 16777216, false, false));
        archiveTargetRadioButton.setText(Messages.ExportConfigurationWizardPage_To_archive);
        final Combo archiveTargetPathCombo = new Combo(areaComposite, 4);
        GridDataFactory.swtDefaults().align(4, 4).grab(true, false).applyTo(archiveTargetPathCombo);
        historyList = getHistory(ZIP_TARGET_PATH_HISTORY_SECTION).loadHistory();
        archiveTargetPathCombo.setItems(historyList.toArray(new String[historyList.size()]));
        final IObservableValue<String> archiveTargetPathComboValue =
            WidgetProperties.text().observe(archiveTargetPathCombo);
        final Button archiveTargetBrowseButton = new Button(areaComposite, 8);
        archiveTargetBrowseButton.setText(Messages.ExportConfigurationWizardPage_labelBrowse);
        archiveTargetBrowseButton.addSelectionListener(SelectionListener
            .widgetSelectedAdapter(e -> openTargetArchiveSelectionDialog(archiveTargetPathComboValue)));
        isDirectoryTargetSelected = new SelectObservableValue<>();
        isDirectoryTargetSelected.addOption(Boolean.TRUE,
            WidgetProperties.buttonSelection().observe(directoryTargetRadioButton));
        isDirectoryTargetSelected.addOption(Boolean.FALSE,
            WidgetProperties.buttonSelection().observe(archiveTargetRadioButton));
        final UpdateValueStrategy<String, String> targetDirValidationStrategy = new UpdateValueStrategy<>();
        targetDirValidationStrategy.setAfterGetValidator(new TargetPathValidator(true));
        final Binding sourceDirBinding = dataBindingContext.bindValue(directoryTargetPathComboValue, targetDirPathValue,
            targetDirValidationStrategy, new UpdateValueStrategy<>());
        final UpdateValueStrategy<String, String> targetZipValidationStrategy = new UpdateValueStrategy<>();
        targetZipValidationStrategy.setAfterGetValidator(new TargetPathValidator(false));
        final Binding sourceZipBinding = dataBindingContext.bindValue(archiveTargetPathComboValue, targetZipPathValue,
            targetZipValidationStrategy, new UpdateValueStrategy<>());
        isDirectoryTargetSelected.addValueChangeListener(e -> {
            sourceDirBinding.updateTargetToModel();
            sourceZipBinding.updateTargetToModel();
        });
        dataBindingContext.bindValue(WidgetProperties.buttonSelection().observe(directoryTargetRadioButton),
            WidgetProperties.enabled().observe(directoryTargetPathCombo), new UpdateValueStrategy<>(),
            UpdateValueStrategy.never());
        dataBindingContext.bindValue(WidgetProperties.buttonSelection().observe(directoryTargetRadioButton),
            WidgetProperties.enabled().observe(directoryTargetBrowseButton), new UpdateValueStrategy<>(),
            UpdateValueStrategy.never());
        dataBindingContext.bindValue(WidgetProperties.buttonSelection().observe(archiveTargetRadioButton),
            WidgetProperties.enabled().observe(archiveTargetPathCombo), new UpdateValueStrategy<>(),
            UpdateValueStrategy.never());
        dataBindingContext.bindValue(WidgetProperties.buttonSelection().observe(archiveTargetRadioButton),
            WidgetProperties.enabled().observe(archiveTargetBrowseButton), new UpdateValueStrategy<>(),
            UpdateValueStrategy.never());
    }

    private void createWizardValidationManager()
    {
        final AggregateValidationStatus validationStatus = new AggregateValidationStatus(dataBindingContext, 2);
        validationStatus.addValueChangeListener(e -> updateValidationStatus(e.getObservableValue().getValue()));
    }

    private void updateValidationStatus(final IStatus status)
    {
        switch (status.getSeverity())
        {
        case IStatus.INFO:
        {
            setErrorMessage((String)null);
            setMessage(status.getMessage(), 1);
            setPageComplete(true);
            break;
        }
        case IStatus.WARNING:
        {
            setErrorMessage((String)null);
            setMessage(status.getMessage(), 2);
            setPageComplete(true);
            break;
        }
        case IStatus.ERROR:
        case IStatus.CANCEL:
        {
            setErrorMessage(status.getMessage());
            setPageComplete(false);
            break;
        }
        default:
        {
            setErrorMessage((String)null);
            setMessage((String)null);
            setPageComplete(true);
            break;
        }
        }
    }

    private void openTargetDirectorySelectionDialog(final IObservableValue<String> pathValue)
    {
        final DirectoryDialog dialog = new DirectoryDialog(getShell(), 268435456);
        dialog.setMessage(Messages.ExportConfigurationWizardPage_browseDialogMessage);
        String directory = Strings.nullToEmpty(pathValue.getValue()).trim();
        if (directory.isEmpty())
        {
            directory = previouslyBrowsedDirectory;
        }
        if (directory.isEmpty())
        {
            dialog.setFilterPath(ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString());
        }
        else
        {
            final Path path = Path.of(directory, new String[0]);
            if (Files.exists(path, new LinkOption[0]))
            {
                dialog.setFilterPath(directory);
            }
        }
        final String selectedDirectory = dialog.open();
        if (selectedDirectory != null)
        {
            previouslyBrowsedDirectory = selectedDirectory;
            pathValue.setValue(selectedDirectory);
        }
    }

    private void openTargetArchiveSelectionDialog(final IObservableValue<String> pathValue)
    {
        final FileDialog dialog = new FileDialog(getShell(), 268443648);
        String archive = Strings.nullToEmpty(pathValue.getValue()).trim();
        if (archive.isEmpty())
        {
            archive = previouslyBrowsedArchive;
        }
        if (archive.isEmpty())
        {
            dialog.setFilterPath(ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString());
        }
        else if (Files.exists(Paths.get(archive, new String[0]), new LinkOption[0]))
        {
            dialog.setFilterPath(archive);
        }
        dialog.setFilterExtensions(ExportConfigurationWizardPage.ARCHIVE_EXTENSIONS);
        final String selectedArchive = dialog.open();
        if (selectedArchive != null)
        {
            previouslyBrowsedArchive = selectedArchive;
            pathValue.setValue(selectedArchive);
        }
    }

    private void displayErrorDialog(final String message)
    {
        MessageDialog.open(1, getContainer().getShell(), Messages.ExportConfigurationWizardPage_exportProblem, message,
            268435456);
    }

    private void displayOkStatusDialog(final String message)
    {
        MessageDialog.open(2, getContainer().getShell(), Messages.ExportConfigurationWizardPage_title, message,
            268435456);
    }

    private boolean queryYesNoQuestion(final String message)
    {
        return MessageDialog.openQuestion(getContainer().getShell(), Messages.ExportConfigurationWizardPage_question,
            message);
    }

    private void initWizardDialogPageChangingListener()
    {
        if (getWizard().getContainer() instanceof WizardDialog)
        {
            initialPageSize = getShell().getSize();
            getShell().setSize(ExportConfigurationWizardPage.DEFAULT_PAGE_SIZE);
            ((WizardDialog)getWizard().getContainer()).addPageChangingListener(this);
        }
    }

    private void dispatchProjectExportedEvent(final IV8Project project, final IStatus exportStatus)
    {
        String projectType = null;
        try
        {
            projectType = project.getProject().hasNature(ICoreConstants.V8_CONFIGURATION_NATURE) ? "configuration" //$NON-NLS-1$
                : project.getProject().hasNature(ICoreConstants.V8_EXTENSION_NATURE) ? "extension" : null; //$NON-NLS-1$
        }
        catch (CoreException e)
        {
            UiPlugin.log(e.getStatus());
        }
        if (projectType != null)
        {
            final String result = exportStatus.isOK() ? "success" : "failure"; //$NON-NLS-1$ //$NON-NLS-2$
            final String eventName =
                "ru.capralow.dt.modeling.ui/event/projectExportedToXml." + projectType + "." + result; //$NON-NLS-1$ //$NON-NLS-2$
            monitoringEventDispatcher.dispatchEvent(new BusinessEvent(eventName));
        }
    }

    private List<IV8Project> initAvaliableProjectsList()
    {
        return projectManager.getProjects(this::isAvailableProject).stream().collect(Collectors.toList());
    }

    private boolean isAvailableProject(final IV8Project project)
    {
        return project instanceof IConfigurationAware
            && project.getDtProject().getType() == IDtProject.WORKSPACE_PROJECT_TYPE;
    }

    private String getProjectLabel(final Object object)
    {
        if (object instanceof IV8Project)
        {
            final IV8Project project = (IV8Project)object;
            final Configuration configuration = ((IConfigurationAware)project).getConfiguration();
            final String rootObjectName = (configuration != null) ? (" - " + configuration.getName()) : ""; //$NON-NLS-1$ //$NON-NLS-2$
            final Version version = project.getVersion();
            return String.format("%s%s (v. %s)", project.getProject().getName(), rootObjectName, version); //$NON-NLS-1$
        }
        return ""; //$NON-NLS-1$
    }

    private IObservableValue<String> getTargetPathValue()
    {
        return (isDirectoryTargetSelected.getValue() == Boolean.TRUE) ? targetDirPathValue : targetZipPathValue;
    }

    private String getTargetPathHistorySection()
    {
        return (isDirectoryTargetSelected.getValue() == Boolean.TRUE) ? DIR_TARGET_PATH_HISTORY_SECTION
            : ZIP_TARGET_PATH_HISTORY_SECTION;
    }

    private DialogSettingsBasedHistory getHistory(final String historySection)
    {
        return history.computeIfAbsent(historySection, this::loadHistory);
    }

    private DialogSettingsBasedHistory loadHistory(final String historySection)
    {
        final DialogSettingsBasedHistory sectionHistory =
            new DialogSettingsBasedHistory(DialogSettings.getOrCreateSection(getDialogSettings(), historySection));
        sectionHistory.setHistorySize(TARGET_PATH_HISTORY_LENGTH);
        return sectionHistory;
    }

    private class TargetPathValidator
        implements IValidator<String>
    {
        private boolean isDirectoryTargetValidator;

        TargetPathValidator(final boolean directoryTargetValidator)
        {
            isDirectoryTargetValidator = directoryTargetValidator;
        }

        @Override
        public IStatus validate(String targetText)
        {
            Preconditions.checkState(isDirectoryTargetSelected.getValue() != null);
            String trimmedTargetText = Strings.nullToEmpty(targetText).trim();
            return isDirectoryTargetValidator ? validateTargetDirectory(trimmedTargetText)
                : validateTargetArchive(trimmedTargetText);
        }

        private IStatus validateTargetDirectory(final String targetText)
        {
            if (isDirectoryTargetSelected.getValue() == Boolean.TRUE)
            {
                if (targetText.isBlank())
                {
                    return UiPlugin.createErrorStatus(
                        Messages.ExportConfigurationWizardPage_Select_target_directory_message, (Throwable)null);
                }
                final Path targetPath = Paths.get(targetText, new String[0]);
                if (Files.exists(targetPath, new LinkOption[0]) && Files.isRegularFile(targetPath, new LinkOption[0]))
                {
                    return UiPlugin.createErrorStatus(Messages.ExportConfigurationWizardPage_targetAlredyExistAsFile,
                        (Throwable)null);
                }
            }
            return Status.OK_STATUS;
        }

        private IStatus validateTargetArchive(final String targetText)
        {
            if (isDirectoryTargetSelected.getValue() == Boolean.FALSE && targetText.isBlank())
            {
                return UiPlugin.createErrorStatus(Messages.ExportConfigurationWizardPage_Select_target_archive_message,
                    (Throwable)null);
            }
            return Status.OK_STATUS;
        }
    }

    private class ProjectValidation
        implements IValidator<Object>
    {
        @Override
        public IStatus validate(final Object value)
        {
            if (!(value instanceof IConfigurationAware))
            {
                return UiPlugin.createErrorStatus(Messages.ExportConfigurationWizardPage_info, (Throwable)null);
            }
            if (((IConfigurationAware)value).getConfiguration() != null)
            {
                return Status.OK_STATUS;
            }
            return UiPlugin.createErrorStatus(Messages.ExportConfigurationWizardPage_noConfigurationInProject,
                (Throwable)null);
        }
    }
}
