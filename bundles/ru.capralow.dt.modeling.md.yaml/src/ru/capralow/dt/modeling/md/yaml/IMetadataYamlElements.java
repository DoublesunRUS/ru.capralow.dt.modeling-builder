/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.yaml;

import javax.xml.namespace.QName;

import ru.capralow.dt.modeling.yaml.IYamlElements;

public class IMetadataYamlElements
    extends IYamlElements
{
    public static final QName ID = new QName("Ид"); //$NON-NLS-1$

    public static final QName NAME = new QName("Имя"); //$NON-NLS-1$

    public static final QName SYNONYM = new QName("Представление"); //$NON-NLS-1$

    public static final QName COMMON_MODULE = new QName("ОбщийМодуль"); //$NON-NLS-1$

    public static final QName HTTP_SERVICE = new QName("HTTPСервис"); //$NON-NLS-1$

    public static final QName CATALOG = new QName("Справочник"); //$NON-NLS-1$

    public static final QName ENUM = new QName("Перечисление"); //$NON-NLS-1$

    public static final QName INFORMATION_REGISTER = new QName("РегистрСведений"); //$NON-NLS-1$

    public static final QName VALUES = new QName("Элементы"); //$NON-NLS-1$;

    public static final QName ATTRIBUTES = new QName("Реквизиты"); //$NON-NLS-1$;

    public static final QName DIMENSIONS = new QName("Измерения"); //$NON-NLS-1$;

    public static final QName RESOURCES = new QName("Ресурсы"); //$NON-NLS-1$;

    public static class Application
    {
        public static final QName SYNONYM = new QName("Название"); //$NON-NLS-1$
        public static final QName VERSION = new QName("Версия"); //$NON-NLS-1$
        public static final QName VENDOR = new QName("Разработчик"); //$NON-NLS-1$
    }

    public static class CommonModule
    {
        public static final QName ENVIRONMENT = new QName("МестоИсполнения"); //$NON-NLS-1$
        public static final QName CLIENT = new QName("Клиент"); //$NON-NLS-1$
        public static final QName CLIENT_SERVER = new QName("КлиентИСервер"); //$NON-NLS-1$
        public static final QName SERVER_CALL = new QName("ДоступенСКлиента"); //$NON-NLS-1$
    }
}
