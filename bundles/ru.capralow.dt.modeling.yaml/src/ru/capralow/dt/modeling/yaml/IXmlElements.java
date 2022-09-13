/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.yaml;

import javax.xml.namespace.QName;

public class IXmlElements
{
    public static final String VERSION_ATTRIBUTE = "version"; //$NON-NLS-1$

    public static class XR
    {
        public static final QName COMMON = new QName("http://v8.1c.ru/8.3/xcf/readable", "Common", "xr");

        public static final QName VALUE = new QName("http://v8.1c.ru/8.3/xcf/readable", "Value", "xr");

        public static final QName VALUE_LIST = new QName("http://v8.1c.ru/8.3/xcf/readable", "ValueList", "xr");

        public static final QName REF = new QName("http://v8.1c.ru/8.3/xcf/readable", "Ref", "xr");

        public static final QName ABS = new QName("http://v8.1c.ru/8.3/xcf/readable", "Abs", "xr");

        public static final QName LOAD_TRANSPARENT =
            new QName("http://v8.1c.ru/8.3/xcf/readable", "LoadTransparent", "xr");

        public static final QName TRANSPARENT_PIXEL =
            new QName("http://v8.1c.ru/8.3/xcf/readable", "TransparentPixel", "xr");

        public static final QName DESIGN_TIME_REF =
            new QName("http://v8.1c.ru/8.3/xcf/readable", "DesignTimeRef", "xr");

        public static final QName ITEM = new QName("http://v8.1c.ru/8.3/xcf/readable", "Item", "xr");

        public static final QName MD_OBJECT_REF = new QName("http://v8.1c.ru/8.3/xcf/readable", "MDObjectRef", "xr");

        public static final QName LINK_ITEM = new QName("http://v8.1c.ru/8.3/xcf/readable", "LinkItem", "xr");

        public static final QName DATA_PATH = new QName("http://v8.1c.ru/8.3/xcf/readable", "DataPath", "xr");

        public static final QName LINK = new QName("http://v8.1c.ru/8.3/xcf/readable", "Link", "xr");

        public static final QName NAME = new QName("http://v8.1c.ru/8.3/xcf/readable", "Name", "xr");

        public static final QName VALUE_CHANGE = new QName("http://v8.1c.ru/8.3/xcf/readable", "ValueChange", "xr");

        public static final QName PRESENTATION = new QName("http://v8.1c.ru/8.3/xcf/readable", "Presentation", "xr");

        public static final QName PROPERTY_STATE = new QName("http://v8.1c.ru/8.3/xcf/readable", "PropertyState", "xr");

        public static final QName PROPERTY = new QName("http://v8.1c.ru/8.3/xcf/readable", "Property", "xr");

        public static final QName STATE = new QName("http://v8.1c.ru/8.3/xcf/readable", "State", "xr");

        public static final QName CHECK_STATE = new QName("http://v8.1c.ru/8.3/xcf/readable", "CheckState", "xr");

        public static final QName EXTENDED_PROPERTY =
            new QName("http://v8.1c.ru/8.3/xcf/readable", "ExtendedProperty", "xr");

        public static final QName CHECK_VALUE = new QName("http://v8.1c.ru/8.3/xcf/readable", "CheckValue", "xr");

        public static final QName EXTEND_VALUE = new QName("http://v8.1c.ru/8.3/xcf/readable", "ExtendValue", "xr");

        public static final QName NOTIFY_VALUE = new QName("http://v8.1c.ru/8.3/xcf/readable", "NotifyValue", "xr");
    }

    public static class V8
    {
        public static final QName LANG = new QName("http://v8.1c.ru/8.1/data/core", "lang", "v8");

        public static final QName ITEM = new QName("http://v8.1c.ru/8.1/data/core", "item", "v8");

        public static final QName CONTENT = new QName("http://v8.1c.ru/8.1/data/core", "content", "v8");

        public static final QName TYPE_DESCRIPTION =
            new QName("http://v8.1c.ru/8.1/data/core", "TypeDescription", "v8");

        public static final QName TYPE = new QName("http://v8.1c.ru/8.1/data/core", "Type", "v8");

        public static final QName TYPE_SET = new QName("http://v8.1c.ru/8.1/data/core", "TypeSet", "v8");

        public static final QName NUMBER_QUALIFIERS =
            new QName("http://v8.1c.ru/8.1/data/core", "NumberQualifiers", "v8");

        public static final QName FRACTION_DIGITS = new QName("http://v8.1c.ru/8.1/data/core", "FractionDigits", "v8");

        public static final QName DIGITS = new QName("http://v8.1c.ru/8.1/data/core", "Digits", "v8");

        public static final QName ALLOWED_SIGN = new QName("http://v8.1c.ru/8.1/data/core", "AllowedSign", "v8");

        public static final QName STRING_QUALIFIERS =
            new QName("http://v8.1c.ru/8.1/data/core", "StringQualifiers", "v8");

        public static final QName LENGTH = new QName("http://v8.1c.ru/8.1/data/core", "Length", "v8");

        public static final QName ALLOWED_LENGTH = new QName("http://v8.1c.ru/8.1/data/core", "AllowedLength", "v8");

        public static final QName DATE_QUALIFIERS = new QName("http://v8.1c.ru/8.1/data/core", "DateQualifiers", "v8");

        public static final QName DATE_FRACTIONS = new QName("http://v8.1c.ru/8.1/data/core", "DateFractions", "v8");

        public static final QName BINARY_QUALIFIERS =
            new QName("http://v8.1c.ru/8.1/data/core", "BinaryDataQualifiers", "v8");

        public static final QName FIXED_ARRAY = new QName("http://v8.1c.ru/8.1/data/core", "FixedArray", "v8");

        public static final QName VALUE = new QName("http://v8.1c.ru/8.1/data/core", "Value", "v8");

        public static final QName STANDARD_PERIOD = new QName("http://v8.1c.ru/8.1/data/core", "StandardPeriod", "v8");

        public static final QName STANDARD_PERIOD_VARIANT =
            new QName("http://v8.1c.ru/8.1/data/core", "StandardPeriodVariant", "v8");

        public static final QName VARIANT = new QName("http://v8.1c.ru/8.1/data/core", "variant", "v8");

        public static final QName START_DATE = new QName("http://v8.1c.ru/8.1/data/core", "startDate", "v8");

        public static final QName END_DATE = new QName("http://v8.1c.ru/8.1/data/core", "endDate", "v8");

        public static final QName LOCAL_STRING_TYPE =
            new QName("http://v8.1c.ru/8.1/data/core", "LocalStringType", "v8");
    }

    public static class ENT
    {
        public static final QName ACCOUNT_TYPE = new QName("http://v8.1c.ru/8.1/data/enterprise", "AccountType", "ent");
    }

    public static class XS
    {
        public static final QName BOOLEAN = new QName("http://www.w3.org/2001/XMLSchema", "boolean", "xs");

        public static final QName DECIMAL = new QName("http://www.w3.org/2001/XMLSchema", "decimal", "xs");

        public static final QName DATETIME = new QName("http://www.w3.org/2001/XMLSchema", "dateTime", "xs");

        public static final QName STRING = new QName("http://www.w3.org/2001/XMLSchema", "string", "xs");

        public static final QName BINARY_DATA = new QName("http://www.w3.org/2001/XMLSchema", "base64Binary", "xs");

        public static final QName PAIR = new QName("http://v8.1c.ru/8.1/data/core", "pair", "xs");

        public static final QName KEY = new QName("http://v8.1c.ru/8.1/data/core", "key", "xs");

        public static final QName VALUE = new QName("http://v8.1c.ru/8.1/data/core", "value", "xs");
    }

    public static class XSI
    {
        public static final QName TYPE = new QName("http://www.w3.org/2001/XMLSchema-instance", "type", "xsi");

        public static final QName NIL = new QName("http://www.w3.org/2001/XMLSchema-instance", "nil", "xsi");
    }

    public static class APP
    {
        public static final QName ITEM = new QName("http://v8.1c.ru/8.2/managed-application/core", "item", "app");

        public static final QName VALUE = new QName("http://v8.1c.ru/8.2/managed-application/core", "value", "app");

        public static final QName MOBILE_APPLICATION_URL_TYPE =
            new QName("http://v8.1c.ru/8.2/managed-application/core", "MobileApplicationURL", "app");
    }

    public static class V8UI
    {
        public static final QName STYLE = new QName("http://v8.1c.ru/8.1/data/ui", "style", "v8ui");

        public static final QName CONTROL_BORDER_TYPE =
            new QName("http://v8.1c.ru/8.1/data/ui", "ControlBorderType", "v8ui");

        public static final QName BORDER = new QName("http://v8.1c.ru/8.1/data/ui", "Border", "v8ui");

        public static final QName COLOR = new QName("http://v8.1c.ru/8.1/data/ui", "Color", "v8ui");

        public static final QName FONT = new QName("http://v8.1c.ru/8.1/data/ui", "Font", "v8ui");

        public static final QName CHART_LINE_TYPE = new QName("http://v8.1c.ru/8.1/data/ui", "ChartLineType", "v8ui");
    }

    public static class NS
    {
        public static final String APP = "app";

        public static final String CFG = "cfg";

        public static final String CMI = "cmi";

        public static final String ENT = "ent";

        public static final String LF = "lf";

        public static final String MXL = "mxl";

        public static final String PL = "pl";

        public static final String STYLE = "style";

        public static final String SYS = "sys";

        public static final String V8 = "v8";

        public static final String V8UI = "v8ui";

        public static final String WEB = "web";

        public static final String WIN = "win";

        public static final String XEN = "xen";

        public static final String XPR = "xpr";

        public static final String XR = "xr";

        public static final String XS = "xs";

        public static final String XSI = "xsi";

        public static final String DCSCOR = "dcscor";

        public static final String DCSSCH = "dcssch";

        public static final String DCSSET = "dcsset";

        public static final String APP_URI = "http://v8.1c.ru/8.2/managed-application/core";

        public static final String CFG_URI = "http://v8.1c.ru/8.1/data/enterprise/current-config";

        public static final String CMI_URI = "http://v8.1c.ru/8.2/managed-application/cmi";

        public static final String ENT_URI = "http://v8.1c.ru/8.1/data/enterprise";

        public static final String LF_URI = "http://v8.1c.ru/8.2/managed-application/logform";

        public static final String STYLE_URI = "http://v8.1c.ru/8.1/data/ui/style";

        public static final String SYS_URI = "http://v8.1c.ru/8.1/data/ui/fonts/system";

        public static final String V8_URI = "http://v8.1c.ru/8.1/data/core";

        public static final String V8UI_URI = "http://v8.1c.ru/8.1/data/ui";

        public static final String WEB_URI = "http://v8.1c.ru/8.1/data/ui/colors/web";

        public static final String WIN_URI = "http://v8.1c.ru/8.1/data/ui/colors/windows";

        public static final String XEN_URI = "http://v8.1c.ru/8.3/xcf/enums";

        public static final String XPR_URI = "http://v8.1c.ru/8.3/xcf/predef";

        public static final String XR_URI = "http://v8.1c.ru/8.3/xcf/readable";

        public static final String XS_URI = "http://www.w3.org/2001/XMLSchema";

        public static final String XSI_URI = "http://www.w3.org/2001/XMLSchema-instance";

        public static final String DCSCOR_URI = "http://v8.1c.ru/8.1/data-composition-system/core";

        public static final String DCSSCH_URI = "http://v8.1c.ru/8.1/data-composition-system/schema";

        public static final String DCSSET_URI = "http://v8.1c.ru/8.1/data-composition-system/settings";

        public static final String PL_URI = "http://v8.1c.ru/8.3/data/planner";

        public static final String MXL_URI = "http://v8.1c.ru/8.2/data/spreadsheet";

        public static final String EXT_URI = "http://v8.1c.ru/8.3/xcf/extrnprops";
    }
}
