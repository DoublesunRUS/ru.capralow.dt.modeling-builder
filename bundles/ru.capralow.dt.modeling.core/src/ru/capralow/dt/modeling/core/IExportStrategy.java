/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.core;

import org.eclipse.emf.ecore.EObject;

public interface IExportStrategy
{
    IExportStrategy DEFAULT = new IExportStrategy()
    {
        @Override
        public boolean exportSubordinatesObjects(final EObject eObject)
        {
            return true;
        }

        @Override
        public boolean exportUnknown()
        {
            return true;
        }

        @Override
        public boolean exportExternalProperties(final EObject eObject)
        {
            return true;
        }
    };

    boolean exportSubordinatesObjects(EObject p0);

    boolean exportExternalProperties(EObject p0);

    boolean exportUnknown();
}
