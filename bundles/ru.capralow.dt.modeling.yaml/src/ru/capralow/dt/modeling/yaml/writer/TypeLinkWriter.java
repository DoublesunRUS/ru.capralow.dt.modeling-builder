/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.yaml.writer;

import java.util.Map;

import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.core.model.ISymLinkConverter;
import com._1c.g5.v8.dt.core.naming.ISymbolicNameService;
import com._1c.g5.v8.dt.mcore.Field;
import com._1c.g5.v8.dt.metadata.common.CommonPackage;
import com._1c.g5.v8.dt.metadata.common.TypeLink;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.yaml.IQnameProvider;

@Singleton
public class TypeLinkWriter
    implements ISpecifiedElementWriter
{
    @Inject
    public IQnameProvider nameProvider;

    @Inject
    public ISymLinkConverter linkConverter;

    @Inject
    public ISymbolicNameService symbolicNameService;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version, Map<String, Object> group) throws ExportException
    {
        if (feature.getEType() != CommonPackage.Literals.TYPE_LINK)
        {
            throw new IllegalArgumentException(String.format("Invalid feature type in %s", new Object[] { feature }));
        }
        QName elementQName = this.nameProvider.getElementQName(feature);
        if (eObject == null || eObject.eGet(feature) == null)
        {
            if (writeEmpty)
            {
//                writer.writeEmptyElement(elementQName.toString());
            }
        }
        else
        {
            TypeLink typeLink = (TypeLink)eObject.eGet(feature);
            if (typeLink.getField() != null || typeLink.getLinkItem() != 0)
            {
                writer.writeElement(elementQName.toString(), "", group);
                Field field = typeLink.getField();
                if (field != null)
                {
//                    writer.writeStartElement(IYamlElements.XR.DATA_PATH);
//                    writer.writeCharacters(
//                    this.linkConverter.convert(typeLink, CommonPackage.Literals.TYPE_LINK__FIELD,
//                        this.symbolicNameService.generateSymbolicName(field, typeLink,
//                            CommonPackage.Literals.TYPE_LINK__FIELD)));
//                    writer.writeInlineEndElement();
                }
                else
                {
//                    writer.writeEmptyElement(IYamlElements.XR.DATA_PATH);
                }
//                writer.writeStartElement(IYamlElements.XR.LINK_ITEM);
//                writer.writeCharacters(String.valueOf(typeLink.getLinkItem()));
//                writer.writeInlineEndElement();
//                writer.writeEndElement();
            }
            else if (writeEmpty)
            {
//                writer.writeEmptyElement(elementQName.toString());
            }
        }
    }

}
