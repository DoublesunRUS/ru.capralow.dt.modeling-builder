/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.internal.yaml.writer;

import java.util.List;
import java.util.stream.Collectors;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.metadata.common.CommonFactory;
import com._1c.g5.v8.dt.metadata.common.CommonPackage;
import com._1c.g5.v8.dt.metadata.common.RequiredMobileApplicationPermissions;
import com._1c.g5.v8.dt.metadata.common.RequiredPermission;
import com._1c.g5.v8.dt.metadata.mdclass.Configuration;
import com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.md.yaml.IMetadataYamlElements;
import ru.capralow.dt.modeling.yaml.IQNameProvider;
import ru.capralow.dt.modeling.yaml.writer.ISpecifiedElementWriter;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;

@Singleton
public class RequiredMobileApplicationPermissions8315Writer
    implements ISpecifiedElementWriter
{
    @Inject
    @Named(ISpecifiedElementWriter.SMART_ELEMENT_WRITER)
    private ISpecifiedElementWriter smartFeatureWriter;

    @Inject
    private IQNameProvider nameProvider;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws ExportException
    {
        Preconditions.checkArgument(version.isGreaterThan(Version.V8_3_14),
            "8.3.15 format data isn't being written in older versions");
        Preconditions.checkArgument(eObject instanceof Configuration, "Invalid object " + eObject.toString());
        Preconditions.checkArgument(
            (feature == MdClassPackage.Literals.CONFIGURATION__REQUIRED_MOBILE_APPLICATION_PERMISSIONS8315),
            "Invalid feature " + feature.toString());
        QName qName = this.nameProvider
            .getElementQName(MdClassPackage.Literals.CONFIGURATION__REQUIRED_MOBILE_APPLICATION_PERMISSIONS);
        if (version.isGreaterThan(Version.V8_3_17))
        {
            writer.writeEmptyElement(qName);
        }
        else
        {
            writer.writeStartElement(qName);
            EList eList = ((Configuration)eObject).getRequiredMobileApplicationPermissions8315();
            for (RequiredMobileApplicationPermissions permissions : getPermissionsAvailableByVersion(version))
            {
                RequiredPermission permission = getCorresondingPermissionDefinition(permissions, eList);
                if (!isEmptyGroupPermission(permission) && isSuppertedVersion(permissions, version))
                    writeRequiredPermission(writer, version, permissions, permission);
            }
            writer.writeEndElement();
        }
    }

    private boolean isSuppertedVersion(RequiredMobileApplicationPermissions permissions, Version version)
    {
        return !(version.isLessThan(Version.V8_3_16)
            && (permissions == RequiredMobileApplicationPermissions.ALLOW_OS_BACKUP
                || permissions == RequiredMobileApplicationPermissions.INSTALL_PACKAGES));
    }

    private void writeRequiredPermission(YamlStreamWriter writer, Version version,
        RequiredMobileApplicationPermissions permissions, RequiredPermission permission)
        throws XMLStreamException, ExportException
    {
        writer.writeStartElement(IMetadataYamlElements.APP.PERMISSION);
        writer.writeTextElement(IMetadataYamlElements.APP.PERMISSION, permissions);
        writer.writeTextElement(IMetadataYamlElements.APP.USE, Boolean.toString(permission.isUse()));
        this.smartFeatureWriter.write(writer, permission, CommonPackage.Literals.REQUIRED_PERMISSION__DESCRIPTION,
            false, version);
        writer.writeEndElement();
    }

    private List<RequiredMobileApplicationPermissions> getPermissionsAvailableByVersion(Version version)
    {
        return RequiredMobileApplicationPermissions.VALUES.stream()
            .filter(v -> v != RequiredMobileApplicationPermissions.MULTIMEDIA
                && v != RequiredMobileApplicationPermissions.TELEPHONY)

            .collect(Collectors.toList());
    }

    private boolean isEmptyGroupPermission(RequiredPermission permission)
    {
        return permission.getDescription().isEmpty() && !permission.isUse()
            && (permission.getPermission() == RequiredMobileApplicationPermissions.PERMISSION_GROUP_CALL_LOG
                || permission.getPermission() == RequiredMobileApplicationPermissions.PERMISSION_GROUP_PHONE
                || permission.getPermission() == RequiredMobileApplicationPermissions.PERMISSION_GROUP_SMS);
    }

    private RequiredPermission getCorresondingPermissionDefinition(
        RequiredMobileApplicationPermissions paramRequiredMobileApplicationPermissions,
        List<RequiredPermission> availablePermissions)
    {
        return availablePermissions.stream()
            .filter(permissionDefinition -> permissionDefinition
                .getPermission() == paramRequiredMobileApplicationPermissions)
            .findAny()
            .orElseGet(() -> createRequiredPermission(paramRequiredMobileApplicationPermissions));
    }

    private RequiredPermission createRequiredPermission(RequiredMobileApplicationPermissions permissions)
    {
        RequiredPermission requiredPermission = CommonFactory.eINSTANCE.createRequiredPermission();
        requiredPermission.setPermission(permissions);
        return requiredPermission;
    }
}
