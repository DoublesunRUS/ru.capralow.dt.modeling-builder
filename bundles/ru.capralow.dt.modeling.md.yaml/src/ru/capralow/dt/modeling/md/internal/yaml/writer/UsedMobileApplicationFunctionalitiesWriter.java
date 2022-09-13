/**
 *
 */
package ru.capralow.dt.modeling.md.internal.yaml.writer;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.stream.XMLStreamException;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.md.availability.MobileApplicationFunctionalitiesVersionAvailability;
import com._1c.g5.v8.dt.md.availability.RequiredMobileApplicationPermissionMessagesVersionAvailability;
import com._1c.g5.v8.dt.metadata.common.CommonFactory;
import com._1c.g5.v8.dt.metadata.common.CommonPackage;
import com._1c.g5.v8.dt.metadata.common.MobileApplicationFunctionalities;
import com._1c.g5.v8.dt.metadata.common.RequiredMobileApplicationPermissionMessages;
import com._1c.g5.v8.dt.metadata.common.RequiredPermissionMessage;
import com._1c.g5.v8.dt.metadata.common.UsedFunctionality;
import com._1c.g5.v8.dt.metadata.common.UsedFunctionalityFlag;
import com._1c.g5.v8.dt.metadata.mdclass.Configuration;
import com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.md.yaml.IMetadataXmlElements;
import ru.capralow.dt.modeling.yaml.IQNameProvider;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;
import ru.capralow.dt.modeling.yaml.writer.ISpecifiedElementWriter;

@Singleton
public class UsedMobileApplicationFunctionalitiesWriter
    implements ISpecifiedElementWriter
{
    @Inject
    @Named("SmartSpecifiedElementWriter")
    private ISpecifiedElementWriter smartFeatureWriter;

    @Inject
    private IQNameProvider nameProvider;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws XMLStreamException, ExportException
    {
        Preconditions.checkArgument(version.isGreaterThan(Version.V8_3_17),
            "8.3.18 format data isn't being written in older versions");
        Preconditions.checkArgument(eObject instanceof Configuration, "Invalid object " + eObject.toString());
        Preconditions.checkArgument(
            feature == MdClassPackage.Literals.CONFIGURATION__USED_MOBILE_APPLICATION_FUNCTIONALITIES,
            "Invalid feature " + feature.toString());
        writer.writeStartElement(this.nameProvider
            .getElementQName(MdClassPackage.Literals.CONFIGURATION__USED_MOBILE_APPLICATION_FUNCTIONALITIES));
        UsedFunctionality usedFunctionality = ((Configuration)eObject).getUsedMobileApplicationFunctionalities();
        List<UsedFunctionalityFlag> usedFunctionalityFlags = (usedFunctionality != null)
            ? (List<UsedFunctionalityFlag>)usedFunctionality.getFunctionality() : Collections.emptyList();
        for (MobileApplicationFunctionalities functionality : getMobileApplicationFunctionalitiesAvailableByVersion(
            version))
        {
            UsedFunctionalityFlag functionalityFlag =
                getCorresondingUsedFunctionalityFlag(functionality, usedFunctionalityFlags);
            writeUsedFunctionalityFlag(writer, version, functionalityFlag);
        }
        List<RequiredPermissionMessage> permissionMessages = (usedFunctionality != null)
            ? (List<RequiredPermissionMessage>)usedFunctionality.getPermissionMessage() : Collections.emptyList();
        for (RequiredMobileApplicationPermissionMessages permission : getRequiredMobileApplicationPermissionMessagesAvailableByVersion(
            version))
        {
            RequiredPermissionMessage permissionMessage =
                getCorresondingRequiredPermissionMessage(permission, permissionMessages);
            writeRequiredPermissionMessage(writer, version, permissionMessage);
        }
        writer.writeEndElement();
    }

    private void writeRequiredPermissionMessage(YamlStreamWriter writer, Version version,
        RequiredPermissionMessage permissionMessage) throws XMLStreamException, ExportException
    {
        if (isNotEmptyDescription(permissionMessage.getDescription()))
        {
            writer.writeStartElement(IMetadataXmlElements.APP.PERMISSION_MESSAGE);
            writer.writeTextElement(IMetadataXmlElements.APP.PERMISSION, permissionMessage.getPermission());
            this.smartFeatureWriter.write(writer, permissionMessage,
                CommonPackage.Literals.REQUIRED_PERMISSION_MESSAGE__DESCRIPTION, false, version);
            writer.writeEndElement();
        }
    }

    private boolean isNotEmptyDescription(EMap<String, String> description)
    {
        return description != null && !description.isEmpty();
    }

    private void writeUsedFunctionalityFlag(YamlStreamWriter writer, Version version,
        UsedFunctionalityFlag functionalityFlag) throws XMLStreamException, ExportException
    {
        writer.writeStartElement(IMetadataXmlElements.APP.FUNCTIONALITY);
        writer.writeTextElement(IMetadataXmlElements.APP.FUNCTIONALITY, functionalityFlag.getFunctionality());
        writer.writeTextElement(IMetadataXmlElements.APP.USE, Boolean.toString(functionalityFlag.isUse()));
        writer.writeEndElement();
    }

    private List<RequiredMobileApplicationPermissionMessages> getRequiredMobileApplicationPermissionMessagesAvailableByVersion(
        Version paramVersion)
    {
        return RequiredMobileApplicationPermissionMessages.VALUES.stream()
            .filter(v -> RequiredMobileApplicationPermissionMessagesVersionAvailability.isAvailable(v, paramVersion))
            .collect(Collectors.toList());
    }

    private List<MobileApplicationFunctionalities> getMobileApplicationFunctionalitiesAvailableByVersion(
        Version paramVersion)
    {
        return MobileApplicationFunctionalities.VALUES.stream()
            .filter(v -> MobileApplicationFunctionalitiesVersionAvailability.isAvailable(v, paramVersion))
            .collect(Collectors.toList());
    }

    private UsedFunctionalityFlag getCorresondingUsedFunctionalityFlag(
        MobileApplicationFunctionalities paramMobileApplicationFunctionalities,
        List<UsedFunctionalityFlag> usedFunctionalityFlags)
    {
        return usedFunctionalityFlags.stream()
            .filter(usedFunctionalityFlag -> usedFunctionalityFlag
                .getFunctionality() == paramMobileApplicationFunctionalities)
            .findAny()
            .orElseGet(() -> createUsedFunctionalityFlag(paramMobileApplicationFunctionalities));
    }

    private RequiredPermissionMessage getCorresondingRequiredPermissionMessage(
        RequiredMobileApplicationPermissionMessages paramRequiredMobileApplicationPermissionMessages,
        List<RequiredPermissionMessage> permissionMessages)
    {
        return permissionMessages.stream()
            .filter(permissionMessage -> permissionMessage
                .getPermission() == paramRequiredMobileApplicationPermissionMessages)
            .findAny()
            .orElseGet(() -> createRequiredPermissionMessage(paramRequiredMobileApplicationPermissionMessages));
    }

    private UsedFunctionalityFlag createUsedFunctionalityFlag(MobileApplicationFunctionalities functionality)
    {
        UsedFunctionalityFlag usedFunctionalityFlag = CommonFactory.eINSTANCE.createUsedFunctionalityFlag();
        usedFunctionalityFlag.setFunctionality(functionality);
        return usedFunctionalityFlag;
    }

    private RequiredPermissionMessage createRequiredPermissionMessage(
        RequiredMobileApplicationPermissionMessages permission)
    {
        RequiredPermissionMessage permissionMessage = CommonFactory.eINSTANCE.createRequiredPermissionMessage();
        permissionMessage.setPermission(permission);
        return permissionMessage;
    }
}
