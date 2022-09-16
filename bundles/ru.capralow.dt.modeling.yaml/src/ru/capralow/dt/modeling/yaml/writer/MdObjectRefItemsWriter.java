/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.yaml.writer;

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
import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.yaml.IqNameProvider;

@Singleton
public class MdObjectRefItemsWriter
    implements ISpecifiedElementWriter
{
    private final ImmutableList<EStructuralFeature> supportedFeatures =
        new ImmutableList.Builder().add(new EStructuralFeature[] { MdClassPackage.Literals.BASIC_DB_OBJECT__BASED_ON,
            MdClassPackage.Literals.TABLE__BASED_ON, MdClassPackage.Literals.CONFIGURATION__DEFAULT_ROLES,
            MdClassPackage.Literals.CONFIGURATION__STANDALONE_CONFIGURATION_RESTRICTION_ROLES,
            MdClassPackage.Literals.CONFIGURATION__DEFAULT_INTERFACE, MdClassPackage.Literals.CONFIGURATION__CONTENT,
            MdClassPackage.Literals.CONFIGURATION__ADDITIONAL_FULL_TEXT_SEARCH_DICTIONARIES,
            MdClassPackage.Literals.CATALOG__OWNERS, MdClassPackage.Literals.COLUMN__REFERENCES,
            MdClassPackage.Literals.CHART_OF_CALCULATION_TYPES__BASE_CALCULATION_TYPES,
            MdClassPackage.Literals.DOCUMENT__REGISTER_RECORDS,
            MdClassPackage.Literals.DOCUMENT_JOURNAL__REGISTERED_DOCUMENTS,
            MdClassPackage.Literals.FILTER_CRITERION__CONTENT,
            MdClassPackage.Literals.FUNCTIONAL_OPTIONS_PARAMETER__USE, MdClassPackage.Literals.SEQUENCE__DOCUMENTS,
            MdClassPackage.Literals.SEQUENCE__REGISTER_RECORDS, MdClassPackage.Literals.SUBSYSTEM__CONTENT,
            MdClassPackage.Literals.SEQUENCE_DIMENSION__DOCUMENT_MAP,
            MdClassPackage.Literals.SEQUENCE_DIMENSION__REGISTER_RECORDS_MAP,
            MdClassPackage.Literals.RECALCULATION_DIMENSION__LEADING_REGISTER_DATA }).build();

    @Inject
    private IqNameProvider nameProvider;

    @Inject
    private ISymLinkConverter linkConverter;

    @Inject
    private ISymbolicNameService symbolicNameService;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws ExportException
    {
        if (!this.supportedFeatures.contains(feature))
        {
            throw new IllegalArgumentException(String.format("Invalid feature %s", new Object[] { feature }));
        }
        QName elementQName = this.nameProvider.getElementQName(feature);
        if (eObject == null)
        {
            if (writeEmpty)
            {
//                writer.writeEmptyElement(elementQName);
            }
            return;
        }
        Object value = eObject.eGet(feature);
        if (feature.isMany())
        {
            if (!(value instanceof List) || ((List)value).isEmpty())
            {
                if (writeEmpty)
                {
//                    writer.writeEmptyElement(elementQName);
                }
            }
            else
            {
                writer.writeElement(elementQName, "");
                for (EObject item : (List<EObject>)value)
                {
                    String ref = this.linkConverter.convert(eObject, (EReference)feature,
                        this.symbolicNameService.generateSymbolicName(item, eObject, (EReference)feature));
                    if (!Strings.isNullOrEmpty(ref))
                    {
//                        writer.writeStartElement(IYamlElements.XR.ITEM);
//                        writer.writeElement(IYamlElements.XSI.TYPE, IYamlElements.XR.MD_OBJECT_REF);
//                        writer.writeCharacters(ref);
//                        writer.writeInlineEndElement();
                    }
                }
//                writer.writeEndElement();
            }
        }
        else if (value instanceof EObject)
        {
            String ref = this.linkConverter.convert(eObject, (EReference)feature,
                this.symbolicNameService.generateSymbolicName((EObject)value, eObject, (EReference)feature));
            if (!Strings.isNullOrEmpty(ref))
            {
                writer.writeElement(elementQName, "");
//                writer.writeElement(IYamlElements.XSI.TYPE, IYamlElements.XR.MD_OBJECT_REF);
//                writer.writeCharacters(ref);
//                writer.writeEndElement();
            }
        }
        else if (writeEmpty)
        {
//            writer.writeEmptyElement(elementQName);
        }
    }
}
