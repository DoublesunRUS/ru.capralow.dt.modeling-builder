/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.yaml;

import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

public interface IqNameProvider
{
    QName getElementQName(EStructuralFeature paramEStructuralFeature);

    QName getClassQName(EClass paramEClass);
}
