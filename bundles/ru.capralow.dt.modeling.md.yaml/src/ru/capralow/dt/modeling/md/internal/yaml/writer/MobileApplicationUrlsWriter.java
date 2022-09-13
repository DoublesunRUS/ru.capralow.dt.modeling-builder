/**
 *
 */
package ru.capralow.dt.modeling.md.internal.yaml.writer;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.metadata.common.AbstractMobileApplicationUrl;
import com._1c.g5.v8.dt.metadata.common.MobileApplicationUrl;
import com._1c.g5.v8.dt.metadata.mdclass.Configuration;
import com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.md.yaml.IMetadataXmlElements;
import ru.capralow.dt.modeling.md.yaml.impl.MetadataFeatureNameProvider;
import ru.capralow.dt.modeling.yaml.IXmlElements;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;
import ru.capralow.dt.modeling.yaml.writer.ISpecifiedElementWriter;

@Singleton
public class MobileApplicationUrlsWriter
    implements ISpecifiedElementWriter
{
    @Inject
    private MetadataFeatureNameProvider nameProvider;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws XMLStreamException, ExportException
    {
        Preconditions.checkArgument(version.isGreaterThan(Version.V8_3_17),
            "8.3.18 format data isn't being written in older versions");
        Preconditions.checkArgument(eObject instanceof Configuration, "Invalid object " + eObject.toString());
        Preconditions.checkArgument(feature == MdClassPackage.Literals.CONFIGURATION__MOBILE_APPLICATION_URLS,
            "Invalid feature " + feature.toString());
        QName qName = this.nameProvider.getElementQName(feature);
        EList<AbstractMobileApplicationUrl> eList = ((Configuration)eObject).getMobileApplicationUrls();
        if (eList.isEmpty())
        {
            writer.writeEmptyElement(qName);
        }
        else
        {
            writer.writeStartElement(qName);
            for (AbstractMobileApplicationUrl abstractMobileApplicationUrl : eList)
            {
                if (abstractMobileApplicationUrl instanceof MobileApplicationUrl)
                {
                    MobileApplicationUrl url = (MobileApplicationUrl)abstractMobileApplicationUrl;
                    writer.writeStartElement(IMetadataXmlElements.V8.VALUE);
                    writer.writeElement(IXmlElements.XSI.TYPE, IXmlElements.APP.MOBILE_APPLICATION_URL_TYPE);
                    writer.writeTextElement(IMetadataXmlElements.APP.BASE_URL, url.getBaseUrl());
                    writer.writeTextElement(IMetadataXmlElements.APP.USE_ANDROID, Boolean.toString(url.isUseAndroid()));
                    writer.writeTextElement(IMetadataXmlElements.APP.USE_IOS, Boolean.toString(url.isUseIOS()));
                    writer.writeTextElement(IMetadataXmlElements.APP.USE_WINDOWS, Boolean.toString(url.isUseWindows()));
                    writer.writeEndElement();
                }
            }
            writer.writeEndElement();
        }
    }
}
