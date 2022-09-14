/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.internal.yaml.writer;

import java.util.List;

import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.core.model.ISymLinkConverter;
import com._1c.g5.v8.dt.core.naming.ISymbolicNameService;
import com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.yaml.IQNameProvider;
import ru.capralow.dt.modeling.yaml.writer.ISpecifiedElementWriter;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;

@Singleton
public class FunctionalOptionContentWriter
    implements ISpecifiedElementWriter
{
    @Inject
    private IQNameProvider nameProvider;

    @Inject
    private ISymLinkConverter linkConverter;

    @Inject
    private ISymbolicNameService symbolicNameService;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws ExportException
    {
        if (MdClassPackage.Literals.FUNCTIONAL_OPTION__CONTENT != feature)
        {
            throw new IllegalArgumentException(String.format("Invalid feature %s", new Object[] { feature }));
        }
        QName elementQName = this.nameProvider.getElementQName(feature);
        List<?> content = (List)eObject.eGet(feature);
        if (content != null && !content.isEmpty())
        {
            writer.writeElement(elementQName, "");
            for (Object object : content)
            {
                String ref = this.linkConverter.convert(eObject, (EReference)feature,
                    this.symbolicNameService.generateSymbolicName((EObject)object, eObject, (EReference)feature));
                if (!Strings.isNullOrEmpty(ref))
                {
                    writer.writeElement("XR.OBJECT", "");
//                    writer.writeCharacters(ref);
//                    writer.writeInlineEndElement();
                }
            }
//            writer.writeEndElement();
        }
        else if (writeEmpty)
        {
//            writer.writeEmptyElement(elementQName);
        }
    }
}
