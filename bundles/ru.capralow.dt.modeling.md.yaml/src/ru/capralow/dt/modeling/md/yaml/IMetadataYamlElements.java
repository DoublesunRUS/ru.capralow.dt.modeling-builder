/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.yaml;

import javax.xml.namespace.QName;

import ru.capralow.dt.modeling.yaml.IYamlElements;

public class IMetadataYamlElements
    extends IYamlElements
{
    public static final QName NAME = new QName("Имя"); //$NON-NLS-1$

    public static final QName SYNONYM = new QName("Представление"); //$NON-NLS-1$

    public static class Application
    {
        public static final QName SYNONYM = new QName("Название"); //$NON-NLS-1$
        public static final QName VERSION = new QName("Версия"); //$NON-NLS-1$
        public static final QName VENDOR = new QName("Разработчик"); //$NON-NLS-1$
    }

}
