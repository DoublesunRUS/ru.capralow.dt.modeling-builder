/**
 *
 */
package ru.capralow.dt.modeling.md.yaml.impl;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import com.google.inject.Inject;

import ru.capralow.dt.modeling.yaml.BaseExportSymLinkConverter;
import ru.capralow.dt.modeling.yaml.SubObjectsManager;

public class ExportMdSymLinkConverter
    extends BaseExportSymLinkConverter
{
    @Inject
    private SubObjectsManager subObjectsManager;

    public String convert(EObject context, EReference reference, String symLink)
    {
        if (context instanceof com._1c.g5.v8.dt.metadata.mdclass.Configuration
            && reference.getEAnnotation("http://www.1c.ru/v8/dt/metadata/MdClass") != null)
        {
            int dotIndex = symLink.indexOf('.');
            if (dotIndex != -1)
                return symLink.substring(dotIndex + 1);
        }
        if (this.subObjectsManager.getSubordinateObjectReferences(context.eClass()).contains(reference))
        {
            int dotIndex = symLink.indexOf('.');
            if (dotIndex != -1)
                return symLink.substring(symLink.lastIndexOf('.') + 1);
        }
        return super.convert(context, reference, symLink);
    }
}
