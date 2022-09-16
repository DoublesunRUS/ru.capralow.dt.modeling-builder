/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.yaml;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;

import com._1c.g5.v8.dt.core.model.ISymLinkConverter;
import com._1c.g5.v8.dt.mcore.McorePackage;
import com.google.inject.Singleton;

@Singleton
public class BaseExportSymLinkConverter
    implements ISymLinkConverter
{
    @Override
    public String convert(EObject context, EReference reference, String symLink)
    {
        if (McorePackage.eINSTANCE.getFont().isSuperTypeOf(reference.getEReferenceType())
            || McorePackage.eINSTANCE.getColor().isSuperTypeOf(reference.getEReferenceType())
            || McorePackage.eINSTANCE.getBorder().isSuperTypeOf(reference.getEReferenceType()))
        {
            return getUiSchemeLink(symLink);
        }
        if (reference.getEType() == McorePackage.Literals.TYPE_ITEM)
        {
            throw new AssertionError("Unsupport feature type to link convert (TypeItem)");
        }
        return symLink;
    }

    private String getUiSchemeLink(String symLink)
    {
        int dotIndex = symLink.indexOf('.');
        if (dotIndex == -1)
        {
            return symLink;
        }

        String str = symLink.substring(0, dotIndex);
        switch (str.hashCode())
        {
        case -1803461041:
            if (!str.equals("System"))
            {
                return symLink;
            }
            return "sys:" + symLink.substring(dotIndex + 1);
        case -1280820637:
            if (!str.equals("Windows"))
            {
                return symLink;
            }
            return "win:" + symLink.substring(dotIndex + 1);
        case 86836:
            if (!str.equals("Web"))
            {
                return symLink;
            }
            return "web:" + symLink.substring(dotIndex + 1);
        case 80227729:
            if (!str.equals("Style"))
            {
                return symLink;
            }
            return "style:" + symLink.substring(dotIndex + 1);
        default:
            return symLink;
        }
    }
}
