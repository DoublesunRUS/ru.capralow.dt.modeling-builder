/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.yaml.writer;

import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.core.model.ISymLinkConverter;
import com._1c.g5.v8.dt.core.naming.ISymbolicNameService;
import com._1c.g5.v8.dt.metadata.common.ChoiceParameterLink;
import com._1c.g5.v8.dt.metadata.common.CommonPackage;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.yaml.IQNameProvider;

@Singleton
public class ChoiceParameterLinkWriter
    implements ISpecifiedElementWriter
{
    @Inject
    private IQNameProvider nameManager;

    @Inject
    @Named(ISpecifiedElementWriter.SMART_ELEMENT_WRITER)
    private ISpecifiedElementWriter featureWriter;

    @Inject
    private ISymLinkConverter linkConverter;

    @Inject
    private ISymbolicNameService symbolicNameService;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws ExportException
    {
        if (feature.getEType() != CommonPackage.Literals.CHOICE_PARAMETER_LINK)
        {
            throw new IllegalArgumentException(String.format("Invalid feature type in %s", new Object[] { feature }));
        }
        if (eObject == null)
        {
            return;
        }
        QName elementQName = nameManager.getElementQName(feature);
        Object value = eObject.eGet(feature);
        if (feature.isMany())
        {
            List<ChoiceParameterLink> list = (List<ChoiceParameterLink>)value;
            if (!list.isEmpty())
            {
                writer.writeElement(elementQName.toString(), "");
                for (ChoiceParameterLink choiceParameterLink : list)
                {
                    writeChoiceParameterLink(writer, choiceParameterLink, version);
                }
//                writer.writeEndElement();
            }
            else if (writeEmpty)
            {
//                writer.writeEmptyElement(elementQName.toString());
            }
        }
        else
        {
            writeChoiceParameterLink(writer, (ChoiceParameterLink)value, version);
        }
    }

    private void writeChoiceParameterLink(YamlStreamWriter writer, ChoiceParameterLink choiceParameterLink,
        Version version) throws ExportException
    {
        writer.writeElement("XR.LINK", "");
        this.featureWriter.write(writer, choiceParameterLink,
            CommonPackage.Literals.ABSTRACT_CHOICE_PARAMETER_LINK__NAME, false, version);
        if (choiceParameterLink.getField() != null)
        {
            String ref =
                this.linkConverter.convert(choiceParameterLink, CommonPackage.Literals.CHOICE_PARAMETER_LINK__FIELD,
                    this.symbolicNameService.generateSymbolicName(choiceParameterLink.getField(), choiceParameterLink,
                        CommonPackage.Literals.CHOICE_PARAMETER_LINK__FIELD));
            if (ref != null)
            {
                writer.writeElement("XR.DATA_PATH", "");
                writer.writeElement("XSI.TYPE", "XS.STRING");
//                writer.writeCharacters(ref);
//                writer.writeInlineEndElement();
            }
            else
            {
//                writer.writeEmptyElement(IYamlElements.XR.DATA_PATH);
            }
        }
        this.featureWriter.write(writer, choiceParameterLink,
            CommonPackage.Literals.ABSTRACT_CHOICE_PARAMETER_LINK__CHANGE_MODE, true, version);
//        writer.writeEndElement();
    }
}
