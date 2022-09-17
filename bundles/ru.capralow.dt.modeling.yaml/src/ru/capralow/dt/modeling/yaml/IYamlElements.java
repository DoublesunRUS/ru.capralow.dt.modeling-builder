/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.yaml;

import javax.xml.namespace.QName;

public class IYamlElements
{
    public static final QName ELEMENT_KIND = new QName("ВидЭлемента"); //$NON-NLS-1$

    protected IYamlElements()
    {

    }

    public static class VisibilityScope
    {
        public static final QName NAME = new QName("ОбластьВидимости"); //$NON-NLS-1$

        public static final QName GLOBAL = new QName("Глобально"); //$NON-NLS-1$

        public static final QName PROJECT = new QName("Проект"); //$NON-NLS-1$

        public static final QName SUBSYSTEM = new QName("Подсистема"); //$NON-NLS-1$
    }
}
