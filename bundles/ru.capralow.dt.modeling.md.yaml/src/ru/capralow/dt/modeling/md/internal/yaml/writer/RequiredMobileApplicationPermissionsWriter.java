/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.internal.yaml.writer;

import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.metadata.common.RequiredMobileApplicationPermissions;
import com._1c.g5.v8.dt.metadata.mdclass.Configuration;
import com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.md.yaml.IMetadataYamlElements;
import ru.capralow.dt.modeling.md.yaml.impl.MetadataFeatureNameProvider;
import ru.capralow.dt.modeling.yaml.IYamlElements;
import ru.capralow.dt.modeling.yaml.writer.ISpecifiedElementWriter;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;

@Singleton
public class RequiredMobileApplicationPermissionsWriter
    implements ISpecifiedElementWriter
{
    @Inject
    private MetadataFeatureNameProvider nameProvider;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws ExportException
    {
        Preconditions.checkArgument(version.isLessThan(Version.V8_3_15),
            "Pre-8.3.15 format data isn't being written since 8.3.15");
        Preconditions.checkArgument(eObject instanceof Configuration, "Invalid object " + eObject.toString());
        Preconditions.checkArgument(
            (feature == MdClassPackage.Literals.CONFIGURATION__REQUIRED_MOBILE_APPLICATION_PERMISSIONS),
            "Invalid feature " + feature.toString());
        writer.writeStartElement(this.nameProvider.getElementQName(feature));
        EList<RequiredMobileApplicationPermissions> availablePermissions =
            ((Configuration)eObject).getRequiredMobileApplicationPermissions();
        for (RequiredMobileApplicationPermissions permissions : getPermissionsAvailableByVersion(version))
        {
            writer.writeStartElement(IMetadataYamlElements.V8.PAIR);
            writer.writeStartElement(IMetadataYamlElements.V8.KEY);
            writer.writeElement(IYamlElements.XSI.TYPE,
                IMetadataYamlElements.APP.REQUIRED_MOBILE_APPLICATION_PERMISSIONS);
            writer.writeCharacters(permissions.toString());
            writer.writeInlineEndElement();
            writer.writeStartElement(IMetadataYamlElements.V8.VALUE);
            writer.writeElement(IYamlElements.XSI.TYPE, IYamlElements.XS.BOOLEAN);
            writer.writeCharacters(String.valueOf(availablePermissions.contains(permissions)));
            writer.writeInlineEndElement();
            writer.writeEndElement();
        }
        writer.writeEndElement();
    }

    private List<RequiredMobileApplicationPermissions> getPermissionsAvailableByVersion(Version version)
    {
        ImmutableList.Builder<RequiredMobileApplicationPermissions> builder = ImmutableList.builder();
        builder.add(new RequiredMobileApplicationPermissions[] { RequiredMobileApplicationPermissions.MULTIMEDIA,
            RequiredMobileApplicationPermissions.LOCATION, RequiredMobileApplicationPermissions.CONTACTS,
            RequiredMobileApplicationPermissions.CALENDARS, RequiredMobileApplicationPermissions.TELEPHONY,
            RequiredMobileApplicationPermissions.PUSH_NOTIFICATION,
            RequiredMobileApplicationPermissions.LOCAL_NOTIFICATION, RequiredMobileApplicationPermissions.PRINT,
            RequiredMobileApplicationPermissions.IN_APP_PURCHASES });
        if (version.isGreaterThan(Version.V8_3_9))
        {
            builder.add(RequiredMobileApplicationPermissions.ADS);
        }
        if (version.isGreaterThan(Version.V8_3_10))
        {
            builder.add(
                new RequiredMobileApplicationPermissions[] { RequiredMobileApplicationPermissions.BACKGROUND_LOCATION,
                    RequiredMobileApplicationPermissions.BACKGROUND_AUDIO_PLAYBACK,
                    RequiredMobileApplicationPermissions.FILE_EXCHANGE_WITH_PERSONAL_COMPUTER });
        }
        return builder.build();
    }
}
