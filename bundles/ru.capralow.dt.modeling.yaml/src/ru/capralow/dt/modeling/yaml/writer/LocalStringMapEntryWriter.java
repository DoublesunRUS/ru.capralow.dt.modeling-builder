/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.yaml.writer;

import java.util.Map;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.form.model.Decoration;
import com._1c.g5.v8.dt.form.model.FormPackage;
import com._1c.g5.v8.dt.mcore.McorePackage;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.yaml.IqNameProvider;

@Singleton
public class LocalStringMapEntryWriter
    implements ISpecifiedElementWriter
{
    @Inject
    private IqNameProvider nameProvider;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws ExportException
    {
        Preconditions.checkArgument(
            feature != null && feature.isMany() && feature.getEType() == McorePackage.Literals.LOCAL_STRING_MAP_ENTRY,
            Messages.ElementWriter_Invalid_feature_type_0, feature);

        QName name = nameProvider.getElementQName(feature);

        @SuppressWarnings("unchecked")
        EMap<String, String> localStringMap = (EMap<String, String>)eObject.eGet(feature);

        writeLocalString(writer, eObject, feature, localStringMap, name, writeEmpty, version);
    }

    public void writeLocalString(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature,
        EMap<String, String> localString, QName elementQName, boolean writeEmpty, Version version)
        throws ExportException
    {
        boolean isFormattedTitle = isFormattedTitle(eObject, feature);
        if (localString != null && !localString.isEmpty())
        {
//            List<Object> list = writer.addList(elementQName);

//            if (isFormattedTitle)
//            {
//                writer.writeElement("formatted", Boolean.toString(((Decoration)eObject).isFormatted()), mainGroup);
//            }
            for (Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)localString.entrySet())
            {
//                Map<String, Object> entryGroup = writer.addGroup(list);
//
//                String lang = entry.getKey();
//                if (lang != null && !lang.isEmpty())
//                {
//                    writer.writeElement("V8.LANG", lang, entryGroup);
//                }
//                else
//                {
//                    if (lang == null)
//                    {
//                        YamlPlugin.log(
//                YamlPlugin.createErrorStatus("Localized string entry key cannot be null", null));
//                    }
////                    writer.writeEmptyElement(IYamlElements.V8.LANG);
//                }
                String value = entry.getValue();
                if (value != null && !value.isEmpty())
                {
//                    writer.writeElement("V8.CONTENT", value, entryGroup);
                    writer.writeElement(elementQName, value);
                }
                else
                {
//                    writer.writeEmptyElement(IYamlElements.V8.CONTENT);
                }
            }
        }
        else if (isFormattedTitle && ((Decoration)eObject).isFormatted())
        {
//            writer.writeEmptyElement(elementQName.toString());
//            if (isFormattedTitle)
//            {
//                writer.writeElement("formatted", Boolean.toString(((Decoration)eObject).isFormatted()));
//            }
        }
        else if (writeEmpty)
        {
//            writer.writeEmptyElement(elementQName.toString());
        }
    }

    private boolean isFormattedTitle(EObject context, EStructuralFeature feature)
    {
        return context instanceof Decoration && feature == FormPackage.Literals.TITLED__TITLE;
    }
}
