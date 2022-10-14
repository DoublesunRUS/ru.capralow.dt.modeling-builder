/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.bsl.internal.yaml.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.naming.IQualifiedNameProvider;

import com._1c.g5.v8.dt.bsl.model.Module;
import com._1c.g5.v8.dt.bsl.model.ModuleType;
import com._1c.g5.v8.dt.bsl.model.resource.owner.IBslOwnerComputerService;
import com._1c.g5.v8.dt.bsl.util.BslUtil;
import com._1c.g5.v8.dt.metadata.mdclass.AbstractForm;
import com._1c.g5.v8.dt.metadata.mdclass.BasicForm;
import com._1c.g5.v8.dt.metadata.mdclass.CommonForm;
import com._1c.g5.v8.dt.metadata.mdclass.Configuration;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.inject.Inject;

import ru.capralow.dt.modeling.bsl.internal.yaml.BslYamlPlugin;
import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.yaml.BaseYamlExportFileSystemSupport;

public class ExportModuleFileSystemSupport
    extends BaseYamlExportFileSystemSupport
{
    private static final String FORM = "Form"; //$NON-NLS-1$

    @Inject
    private IQualifiedNameProvider qualifiedNameProvider;

    @Inject
    private IBslOwnerComputerService bslOwnerComputerService;

    @Override
    public Path getFileName(final EObject eObject, final EStructuralFeature feature, final String fileExtension,
        final Version version) throws ExportException
    {
        if (eObject instanceof Module && feature == null)
        {
            return getModulePath((Module)eObject, feature, fileExtension);
        }
        return null;
    }

    @Override
    public Path getFileName(final EObject eObject, final EStructuralFeature feature, final Version version)
    {
        throw new UnsupportedOperationException();
    }

    private String getModuleName(final ModuleType moduleType)
    {
        switch (moduleType)
        {
        case COMMON_MODULE:
        case FORM_MODULE:
        case WEB_SERVICE_MODULE:
        case HTTP_SERVICE_MODULE:
        case INTEGRATION_SERVICE_MODULE:
        case BOT_MODULE: {
            return "";
        }
        case OBJECT_MODULE: {
            return ".Объект";
        }
        case MANAGER_MODULE: {
            return "";
        }
        case RECORDSET_MODULE: {
            return ".НаборЗаписей";
        }
        case COMMAND_MODULE: {
            return ".Команда";
        }
        case MANAGED_APP_MODULE: {
            return ".ManagedApplicationModule";
        }
        case ORDINARY_APP_MODULE: {
            return ".OrdinaryApplicationModule";
        }
        case EXTERNAL_CONN_MODULE: {
            return ".ExternalConnectionModule";
        }
        case SESSION_MODULE: {
            return ".SessionModule";
        }
        case VALUE_MANAGER_MODULE: {
            return ".ValueManagerModule";
        }
        default: {
            throw new AssertionError("unknown module type" + moduleType);
        }
        }
    }

    private Path getModulePath(final Module module, final EStructuralFeature feature, String fileExtension)
        throws ExportException
    {
        BslUtil.setModuleType(module, qualifiedNameProvider);
        BslUtil.setModuleOwner(module, bslOwnerComputerService);

        String moduleFileExtension = fileExtension;

        if (moduleFileExtension == null)
        {
            moduleFileExtension = ".bsl.yaml"; //$NON-NLS-1$
        }

        final String moduleFileName = String.valueOf(getModuleName(module.getModuleType())) + moduleFileExtension;
        final EObject owner = module.getOwner();
        if (owner == null || owner.eIsProxy())
        {
            throw new ExportException(
                MessageFormat.format(Messages.ExportModuleFileSystemSupport_Error_getting_parent_object_for_module__0,
                    EcoreUtil.getURI(module).path()));
        }

        if (owner instanceof Configuration)
        {
            return Paths.get("Проект" + "." + moduleFileName); //$NON-NLS-1$ //$NON-NLS-2$
        }

        if (owner instanceof AbstractForm)
        {
            final BasicForm basicForm = ((AbstractForm)owner).getMdForm();

            if (basicForm == null || basicForm.eIsProxy())
            {
                BslYamlPlugin.log(BslYamlPlugin.createErrorStatus(
                    MessageFormat.format(
                        Messages.ExportModuleFileSystemSupport_failed_to_get_module_path_for_0_basic_form_1,
                        EcoreUtil.getURI(module),
                        (basicForm == null) ? "null" : ("proxy with uri:" + EcoreUtil.getURI(basicForm))),
                    (Throwable)null));
            }

            if (!(basicForm instanceof CommonForm))
            {
                final MdObject container = EcoreUtil2.getContainerOfType(basicForm.eContainer(), MdObject.class);
                if (container == null || container.eIsProxy())
                {
                    BslYamlPlugin.log(BslYamlPlugin.createErrorStatus(
                        MessageFormat.format(
                            Messages.ExportModuleFileSystemSupport_failed_to_get_module_path_for_0_metadata_object_1,
                            EcoreUtil.getURI(module),
                            (container == null) ? "null" : ("proxy with uri:" + EcoreUtil.getURI(container))),
                        (Throwable)null));
                }
            }

            Path targetPath = getMdObjectTargetDirectory(basicForm);
            Path prefix = targetPath.getFileName();
            if (String.valueOf(prefix.getFileName()).equals("-")) //$NON-NLS-1$
            {
                prefix = prefix.resolveSibling(""); //$NON-NLS-1$
            }
            return targetPath
                .resolveSibling(prefix + String.valueOf(Paths.get(basicForm.getName() + FORM + moduleFileName)));
        }

        if (owner instanceof MdObject)
        {
            final MdObject mdObject = (MdObject)owner;
            Path targetPath = getMdObjectTargetDirectory(mdObject);
            Path prefix = targetPath.getFileName();
            if (String.valueOf(prefix.getFileName()).equals("-")) //$NON-NLS-1$
            {
                prefix = prefix.resolveSibling(""); //$NON-NLS-1$
            }
            return targetPath.resolveSibling(prefix + String.valueOf(Paths.get(mdObject.getName() + moduleFileName)));
        }

        return null;
    }
}
