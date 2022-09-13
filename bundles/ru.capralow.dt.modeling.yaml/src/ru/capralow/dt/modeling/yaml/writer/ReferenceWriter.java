/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.yaml.writer;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.core.model.ISymLinkConverter;
import com._1c.g5.v8.dt.core.naming.ISymbolicNameService;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.yaml.IQNameProvider;

@Singleton
public class ReferenceWriter
    implements ISpecifiedElementWriter
{
    @Inject
    public IQNameProvider nameProvider;

    @Inject
    private ISymLinkConverter linkConverter;

    @Inject
    private ISymbolicNameService symbolicNameService;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws ExportException
    {
        if (!(feature instanceof EReference))
        {
            throw new IllegalArgumentException(String.format("Invalid feature %s", new Object[] { feature }));
        }
        if (eObject != null)
        {
            if (feature.isMany())
            {
                Iterator<EObject> eObjectList = ((List<EObject>)eObject.eGet(feature)).iterator();
                while (eObjectList.hasNext())
                {
                    EObject value = eObjectList.next();

                    writeReference(writer, eObject, (EReference)feature, value, writeEmpty);
                }
            }
            else
            {
                writeReference(writer, eObject, (EReference)feature, (EObject)eObject.eGet(feature), writeEmpty);
            }
        }
        else if (writeEmpty)
        {
//            writer.writeEmptyElement(this.nameProvider.getElementQName(feature));
        }
    }

    public String getReferenceRepresentation(EObject context, EReference feature, EObject value)
    {
        return this.symbolicNameService.generateSymbolicName(value, context, feature);
    }

    private void writeReference(YamlStreamWriter writer, EObject eObject, EReference feature, EObject value,
        boolean writeEmpty) throws ExportException
    {
        if (value != null)
        {
            String ref = getReferenceRepresentation(eObject, feature, value);
            if (!Strings.isNullOrEmpty(ref))
            {
                writer.writeElement(this.nameProvider.getElementQName(feature),
                    this.linkConverter.convert(eObject, feature, ref));
            }
            else if (writeEmpty)
            {
//                writer.writeEmptyElement(this.nameProvider.getElementQName(feature));
            }
        }
        else if (writeEmpty)
        {
//            writer.writeEmptyElement(this.nameProvider.getElementQName(feature));
        }
    }
}
