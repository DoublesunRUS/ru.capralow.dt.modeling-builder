/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.internal.yaml.writer;

import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.metadata.mdclass.ContainedObject;
import com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.md.yaml.IMetadataXmlElements;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;
import ru.capralow.dt.modeling.yaml.writer.ISpecifiedElementWriter;

@Singleton
public class ContainedObjectsWriter
    implements ISpecifiedElementWriter
{
    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws XMLStreamException, ExportException
    {
        if (!checkEObjectClass(eObject.eClass()))
            throw new IllegalArgumentException(String.format("Cannot write contained objects of object: %s",
                new Object[] { eObject.eClass().getName() }));
        if (!feature.isMany() || feature.getEType() != MdClassPackage.Literals.CONTAINED_OBJECT)
            throw new IllegalArgumentException(
                String.format("Cannot write contained objects from feature: %s", new Object[] { feature.getName() }));
        for (ContainedObject containedObject : (List<ContainedObject>)eObject.eGet(feature))
        {
            writer.writeStartElement(IMetadataXmlElements.XR.CONTAINED_OBJECT);
            writer.writeStartElement(IMetadataXmlElements.XR.CLASS_ID);
            writer.writeCharacters(containedObject.getClassId().toString());
            writer.writeInlineEndElement();
            writer.writeStartElement(IMetadataXmlElements.XR.OBJECT_ID);
            writer.writeCharacters(containedObject.getObjectId().toString());
            writer.writeInlineEndElement();
            writer.writeEndElement();
        }
    }

    private boolean checkEObjectClass(EClass eClass)
    {
        return !(eClass != MdClassPackage.Literals.CONFIGURATION && eClass != MdClassPackage.Literals.EXTERNAL_REPORT
            && eClass != MdClassPackage.Literals.EXTERNAL_DATA_PROCESSOR);
    }
}
