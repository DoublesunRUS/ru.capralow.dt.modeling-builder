/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.yaml;

import javax.xml.namespace.QName;

import ru.capralow.dt.modeling.yaml.IXmlElements;

public class IMetadataXmlElements
    extends IXmlElements
{
    public static final QName META_DATA_OBJECT = new QName("http://v8.1c.ru/8.3/MDClasses", "MetaDataObject");

    public static final QName INTERNAL_INFO = new QName("http://v8.1c.ru/8.3/MDClasses", "InternalInfo");

    public static final QName PROPERTIES = new QName("http://v8.1c.ru/8.3/MDClasses", "Properties");

    public static final QName CHILD_OBJECTS = new QName("http://v8.1c.ru/8.3/MDClasses", "ChildObjects");

    public static final QName OWNERS = new QName("http://v8.1c.ru/8.3/MDClasses", "Owners");

    public static final QName ATTRIBUTE = new QName("http://v8.1c.ru/8.3/MDClasses", "Attribute");

    public static final QName TABULAR_SECTION = new QName("http://v8.1c.ru/8.3/MDClasses", "TabularSection");

    public static final QName FORM = new QName("http://v8.1c.ru/8.3/MDClasses", "Form");

    public static final QName TEMPLATE = new QName("http://v8.1c.ru/8.3/MDClasses", "Template");

    public static final QName COMMAND = new QName("http://v8.1c.ru/8.3/MDClasses", "Command");

    public static final QName RESOURCE = new QName("http://v8.1c.ru/8.3/MDClasses", "Resource");

    public static final QName DIMENSION = new QName("http://v8.1c.ru/8.3/MDClasses", "Dimension");

    public static final QName COLUMN = new QName("http://v8.1c.ru/8.3/MDClasses", "Column");

    public static final QName ADDRESSING_ATTRIBUTE = new QName("http://v8.1c.ru/8.3/MDClasses", "AddressingAttribute");

    public static final QName STANDARD_ATTRIBUTES = new QName("http://v8.1c.ru/8.3/MDClasses", "StandardAttributes");

    public static final QName STANDARD_TABULAR_SECTIONS =
        new QName("http://v8.1c.ru/8.3/MDClasses", "StandardTabularSections");

    public static final QName CHARACTERISTICS = new QName("http://v8.1c.ru/8.3/MDClasses", "Characteristics");

    public static final QName ENUM_VALUE = new QName("http://v8.1c.ru/8.3/MDClasses", "EnumValue");

    public static final QName CUBE = new QName("http://v8.1c.ru/8.3/MDClasses", "Cube");

    public static final QName TABLE = new QName("http://v8.1c.ru/8.3/MDClasses", "Table");

    public static final QName FUNCTION = new QName("http://v8.1c.ru/8.3/MDClasses", "Function");

    public static final QName DIMENSION_TABLE = new QName("http://v8.1c.ru/8.3/MDClasses", "DimensionTable");

    public static final QName FIELD = new QName("http://v8.1c.ru/8.3/MDClasses", "Field");

    public static final QName ACCOUNTING_FLAG = new QName("http://v8.1c.ru/8.3/MDClasses", "AccountingFlag");

    public static final QName EXT_DIMENSION_ACCOUNTING_FLAG =
        new QName("http://v8.1c.ru/8.3/MDClasses", "ExtDimensionAccountingFlag");

    public static final QName UUID_ATTRIBUTE = new QName("http://v8.1c.ru/8.3/MDClasses", "uuid");

    public static final QName NAME_ATTRIBUTE = new QName("http://v8.1c.ru/8.3/MDClasses", "name");

    public static final QName CATEGORY_ATTRIBUTE = new QName("http://v8.1c.ru/8.3/MDClasses", "category");

    public static final QName FROM_ATTRIBUTE = new QName("http://v8.1c.ru/8.3/MDClasses", "from");

    public static final QName LANGUAGE = new QName("http://v8.1c.ru/8.3/MDClasses", "Language");

    public static final QName SUBSYSTEM = new QName("http://v8.1c.ru/8.3/MDClasses", "Subsystem");

    public static final QName STYLE_ITEM = new QName("http://v8.1c.ru/8.3/MDClasses", "StyleItem");

    public static final QName STYLE = new QName("http://v8.1c.ru/8.3/MDClasses", "Style");

    public static final QName COMMON_PICTURE = new QName("http://v8.1c.ru/8.3/MDClasses", "CommonPicture");

    public static final QName INTERFACE = new QName("http://v8.1c.ru/8.3/MDClasses", "Interface");

    public static final QName SESSION_PARAMETER = new QName("http://v8.1c.ru/8.3/MDClasses", "SessionParameter");

    public static final QName ROLE = new QName("http://v8.1c.ru/8.3/MDClasses", "Role");

    public static final QName COMMON_TEMPLATE = new QName("http://v8.1c.ru/8.3/MDClasses", "CommonTemplate");

    public static final QName FILTER_CRITERION = new QName("http://v8.1c.ru/8.3/MDClasses", "FilterCriterion");

    public static final QName COMMON_MODULE = new QName("http://v8.1c.ru/8.3/MDClasses", "CommonModule");

    public static final QName COMMON_ATTRIBUTE = new QName("http://v8.1c.ru/8.3/MDClasses", "CommonAttribute");

    public static final QName EXCHANGE_PLAN = new QName("http://v8.1c.ru/8.3/MDClasses", "ExchangePlan");

    public static final QName XDTO_PACKAGE = new QName("http://v8.1c.ru/8.3/MDClasses", "XDTOPackage");

    public static final QName WEB_SERVICE = new QName("http://v8.1c.ru/8.3/MDClasses", "WebService");

    public static final QName HTTP_SERVICE = new QName("http://v8.1c.ru/8.3/MDClasses", "HTTPService");

    public static final QName WS_REFERENCE = new QName("http://v8.1c.ru/8.3/MDClasses", "WSReference");

    public static final QName EVENT_SUBSCRIPTION = new QName("http://v8.1c.ru/8.3/MDClasses", "EventSubscription");

    public static final QName SCHEDULED_JOB = new QName("http://v8.1c.ru/8.3/MDClasses", "ScheduledJob");

    public static final QName SETTINGS_STORAGE = new QName("http://v8.1c.ru/8.3/MDClasses", "SettingsStorage");

    public static final QName FUNCTIONAL_OPTION = new QName("http://v8.1c.ru/8.3/MDClasses", "FunctionalOption");

    public static final QName FUNCTIONAL_OPTIONS_PARAMETER =
        new QName("http://v8.1c.ru/8.3/MDClasses", "FunctionalOptionsParameter");

    public static final QName DEFINED_TYPE = new QName("http://v8.1c.ru/8.3/MDClasses", "DefinedType");

    public static final QName COMMON_COMMAND = new QName("http://v8.1c.ru/8.3/MDClasses", "CommonCommand");

    public static final QName COMMAND_GROUP = new QName("http://v8.1c.ru/8.3/MDClasses", "CommandGroup");

    public static final QName CONSTANT = new QName("http://v8.1c.ru/8.3/MDClasses", "Constant");

    public static final QName COMMON_FORM = new QName("http://v8.1c.ru/8.3/MDClasses", "CommonForm");

    public static final QName CATALOG = new QName("http://v8.1c.ru/8.3/MDClasses", "Catalog");

    public static final QName DOCUMENT = new QName("http://v8.1c.ru/8.3/MDClasses", "Document");

    public static final QName DOCUMENT_NUMERATOR = new QName("http://v8.1c.ru/8.3/MDClasses", "DocumentNumerator");

    public static final QName SEQUENCE = new QName("http://v8.1c.ru/8.3/MDClasses", "Sequence");

    public static final QName DOCUMENT_JOURNAL = new QName("http://v8.1c.ru/8.3/MDClasses", "DocumentJournal");

    public static final QName ENUM = new QName("http://v8.1c.ru/8.3/MDClasses", "Enum");

    public static final QName REPORT = new QName("http://v8.1c.ru/8.3/MDClasses", "Report");

    public static final QName DATA_PROCESSOR = new QName("http://v8.1c.ru/8.3/MDClasses", "DataProcessor");

    public static final QName INFORMATION_REGISTER = new QName("http://v8.1c.ru/8.3/MDClasses", "InformationRegister");

    public static final QName INTEGRATION_SERVICE = new QName("http://v8.1c.ru/8.3/MDClasses", "IntegrationService");

    public static final QName INTEGRATION_SERVICE_CHANNEL =
        new QName("http://v8.1c.ru/8.3/MDClasses", "IntegrationServiceChannel");

    public static final QName ACCUMULATION_REGISTER =
        new QName("http://v8.1c.ru/8.3/MDClasses", "AccumulationRegister");

    public static final QName CHART_OF_CHARACTERISTIC_TYPES =
        new QName("http://v8.1c.ru/8.3/MDClasses", "ChartOfCharacteristicTypes");

    public static final QName CHART_OF_ACCOUNTS = new QName("http://v8.1c.ru/8.3/MDClasses", "ChartOfAccounts");

    public static final QName ACCOUNTING_REGISTER = new QName("http://v8.1c.ru/8.3/MDClasses", "AccountingRegister");

    public static final QName CHART_OF_CALCULATION_TYPES =
        new QName("http://v8.1c.ru/8.3/MDClasses", "ChartOfCalculationTypes");

    public static final QName CALCULATION_REGISTER = new QName("http://v8.1c.ru/8.3/MDClasses", "CalculationRegister");

    public static final QName BUSINESS_PROCESS = new QName("http://v8.1c.ru/8.3/MDClasses", "BusinessProcess");

    public static final QName TASK = new QName("http://v8.1c.ru/8.3/MDClasses", "Task");

    public static final QName EXTERNAL_DATA_SOURCE = new QName("http://v8.1c.ru/8.3/MDClasses", "ExternalDataSource");

    public static final QName URL_TEMPLATE = new QName("http://v8.1c.ru/8.3/MDClasses", "URLTemplate");

    public static final QName URL_TEMPLATE_METHOD = new QName("http://v8.1c.ru/8.3/MDClasses", "Method");

    public static final QName HTTP_METHOD = new QName("http://v8.1c.ru/8.3/MDClasses", "HTTPMethod");

    public static final QName RECALCULATION = new QName("http://v8.1c.ru/8.3/MDClasses", "Recalculation");

    public static final QName OPERATIONS = new QName("http://v8.1c.ru/8.3/MDClasses", "Operation");

    public static final QName PARAMETERS = new QName("http://v8.1c.ru/8.3/MDClasses", "Parameter");

    public static final QName XDTO_RETURNING_VALUE_TYPE =
        new QName("http://v8.1c.ru/8.3/MDClasses", "XDTOReturningValueType");

    public static final QName XDTO_VALUE_TYPE = new QName("http://v8.1c.ru/8.3/MDClasses", "XDTOValueType");

    public static final QName XDTO_PACKAGES = new QName("http://v8.1c.ru/8.3/MDClasses", "XDTOPackages");

    public static final QName BOT = new QName("http://v8.1c.ru/8.3/MDClasses", "Bot");

    public static final QName MOBILE_APPLICATION_URLS =
        new QName("http://v8.1c.ru/8.3/MDClasses", "MobileApplicationURLs");

    public static final QName SYNCHRONOUS_PLATFORM_EXTENSION_AND_ADD_IN_CALL_USE_MODE =
        new QName("http://v8.1c.ru/8.3/MDClasses", "SynchronousPlatformExtensionAndAddInCallUseMode");

    public static class APP
    {
        public static final QName APPLICATION_USE_PURPOSE =
            new QName("http://v8.1c.ru/8.2/managed-application/core", "ApplicationUsePurpose", "app");

        public static final QName REQUIRED_MOBILE_APPLICATION_PERMISSIONS =
            new QName("http://v8.1c.ru/8.2/managed-application/core", "RequiredMobileApplicationPermissions", "app");

        public static final QName VALUE = new QName("http://v8.1c.ru/8.2/managed-application/core", "value", "app");

        public static final QName PERMISSION =
            new QName("http://v8.1c.ru/8.2/managed-application/core", "permission", "app");

        public static final QName USE = new QName("http://v8.1c.ru/8.2/managed-application/core", "use", "app");

        public static final QName DESCRIPTION =
            new QName("http://v8.1c.ru/8.2/managed-application/core", "description", "app");

        public static final QName FUNCTIONALITY =
            new QName("http://v8.1c.ru/8.2/managed-application/core", "functionality", "app");

        public static final QName PERMISSION_MESSAGE =
            new QName("http://v8.1c.ru/8.2/managed-application/core", "permissionMessage", "app");

        public static final QName BASE_URL =
            new QName("http://v8.1c.ru/8.2/managed-application/core", "baseUrl", "app");

        public static final QName USE_ANDROID =
            new QName("http://v8.1c.ru/8.2/managed-application/core", "useAndroid", "app");

        public static final QName USE_IOS = new QName("http://v8.1c.ru/8.2/managed-application/core", "useIOS", "app");

        public static final QName USE_WINDOWS =
            new QName("http://v8.1c.ru/8.2/managed-application/core", "useWindows", "app");
    }

    public static class V8
    {
        public static final QName PAIR = new QName("http://v8.1c.ru/8.1/data/core", "pair", "v8");

        public static final QName KEY = new QName("http://v8.1c.ru/8.1/data/core", "Key", "v8");

        public static final QName VALUE = new QName("http://v8.1c.ru/8.1/data/core", "Value", "v8");

        public static final QName FIXED_ARRAY = new QName("http://v8.1c.ru/8.1/data/core", "FixedArray", "v8");
    }

    public static class XR
        extends IXmlElements.XR
    {
        public static final QName THIS_NODE = new QName("http://v8.1c.ru/8.3/xcf/readable", "ThisNode", "xr");

        public static final QName CONTAINED_OBJECT =
            new QName("http://v8.1c.ru/8.3/xcf/readable", "ContainedObject", "xr");

        public static final QName CLASS_ID = new QName("http://v8.1c.ru/8.3/xcf/readable", "ClassId", "xr");

        public static final QName OBJECT_ID = new QName("http://v8.1c.ru/8.3/xcf/readable", "ObjectId", "xr");

        public static final QName OBJECT = new QName("http://v8.1c.ru/8.3/xcf/readable", "Object", "xr");

        public static final QName GENERATED_TYPE = new QName("http://v8.1c.ru/8.3/xcf/readable", "GeneratedType", "xr");

        public static final QName TYPE_ID = new QName("http://v8.1c.ru/8.3/xcf/readable", "TypeId", "xr");

        public static final QName VALUE_ID = new QName("http://v8.1c.ru/8.3/xcf/readable", "ValueId", "xr");

        public static final QName ITEM = new QName("http://v8.1c.ru/8.3/xcf/readable", "Item", "xr");

        public static final QName FIELD = new QName("http://v8.1c.ru/8.3/xcf/readable", "Field", "xr");

        public static final QName STANDARD_ATTRIBUTE =
            new QName("http://v8.1c.ru/8.3/xcf/readable", "StandardAttribute", "xr");

        public static final QName STANDARD_ATTRIBUTES =
            new QName("http://v8.1c.ru/8.3/xcf/readable", "StandardAttributes", "xr");

        public static final QName STANDARD_TABULAR_SECTION =
            new QName("http://v8.1c.ru/8.3/xcf/readable", "StandardTabularSection", "xr");

        public static final QName SYNONYM = new QName("http://v8.1c.ru/8.3/xcf/readable", "Synonym", "xr");

        public static final QName COMMENT = new QName("http://v8.1c.ru/8.3/xcf/readable", "Comment", "xr");

        public static final QName TOOLTIP = new QName("http://v8.1c.ru/8.3/xcf/readable", "ToolTip", "xr");

        public static final QName QUICK_CHOICE = new QName("http://v8.1c.ru/8.3/xcf/readable", "QuickChoice", "xr");

        public static final QName FILL_FROM_FILLING_VALUE =
            new QName("http://v8.1c.ru/8.3/xcf/readable", "FillFromFillingValue", "xr");

        public static final QName FILL_CHECKING = new QName("http://v8.1c.ru/8.3/xcf/readable", "FillChecking", "xr");

        public static final QName CHOICE_PARAMETER_LINKS =
            new QName("http://v8.1c.ru/8.3/xcf/readable", "ChoiceParameterLinks", "xr");

        public static final QName CHOICE_PARAMETERS =
            new QName("http://v8.1c.ru/8.3/xcf/readable", "ChoiceParameters", "xr");

        public static final QName LINK_BY_TYPE = new QName("http://v8.1c.ru/8.3/xcf/readable", "LinkByType", "xr");

        public static final QName FULL_TEXT_SEARCH =
            new QName("http://v8.1c.ru/8.3/xcf/readable", "FullTextSearch", "xr");

        public static final QName PASSWORD_MODE = new QName("http://v8.1c.ru/8.3/xcf/readable", "PasswordMode", "xr");

        public static final QName FORMAT = new QName("http://v8.1c.ru/8.3/xcf/readable", "Format", "xr");

        public static final QName EDIT_FORMAT = new QName("http://v8.1c.ru/8.3/xcf/readable", "EditFormat", "xr");

        public static final QName MASK = new QName("http://v8.1c.ru/8.3/xcf/readable", "Mask", "xr");

        public static final QName MULTI_LINE = new QName("http://v8.1c.ru/8.3/xcf/readable", "MultiLine", "xr");

        public static final QName EXTENDED_EDIT = new QName("http://v8.1c.ru/8.3/xcf/readable", "ExtendedEdit", "xr");

        public static final QName MIN_VALUE = new QName("http://v8.1c.ru/8.3/xcf/readable", "MinValue", "xr");

        public static final QName MAX_VALUE = new QName("http://v8.1c.ru/8.3/xcf/readable", "MaxValue", "xr");

        public static final QName MARK_NEGATIVES = new QName("http://v8.1c.ru/8.3/xcf/readable", "MarkNegatives", "xr");

        public static final QName CREATE_ON_INPUT =
            new QName("http://v8.1c.ru/8.3/xcf/readable", "CreateOnInput", "xr");

        public static final QName CHOICE_FORM = new QName("http://v8.1c.ru/8.3/xcf/readable", "ChoiceForm", "xr");

        public static final QName CHOICE_HISTORY_ON_INPUT =
            new QName("http://v8.1c.ru/8.3/xcf/readable", "ChoiceHistoryOnInput", "xr");

        public static final QName FILL_VALUE = new QName("http://v8.1c.ru/8.3/xcf/readable", "FillValue", "xr");

        public static final QName DATA_HISTORY = new QName("http://v8.1c.ru/8.3/xcf/readable", "DataHistory", "xr");

        public static final QName CHARACTERISTIC =
            new QName("http://v8.1c.ru/8.3/xcf/readable", "Characteristic", "xr");

        public static final QName CHARACTERISTIC_TYPES =
            new QName("http://v8.1c.ru/8.3/xcf/readable", "CharacteristicTypes", "xr");

        public static final QName KEY_FIELD = new QName("http://v8.1c.ru/8.3/xcf/readable", "KeyField", "xr");

        public static final QName TYPES_FILTER_FIELD =
            new QName("http://v8.1c.ru/8.3/xcf/readable", "TypesFilterField", "xr");

        public static final QName TYPES_FILTER_VALUE =
            new QName("http://v8.1c.ru/8.3/xcf/readable", "TypesFilterValue", "xr");

        public static final QName DATA_PATH_FIELD =
            new QName("http://v8.1c.ru/8.3/xcf/readable", "DataPathField", "xr");

        public static final QName USE_MULTIPLE_VALUES_FIELD =
            new QName("http://v8.1c.ru/8.3/xcf/readable", "MultipleValuesUseField", "xr");

        public static final QName CHARACTERISTIC_VALUES =
            new QName("http://v8.1c.ru/8.3/xcf/readable", "CharacteristicValues", "xr");

        public static final QName OBJECT_FIELD = new QName("http://v8.1c.ru/8.3/xcf/readable", "ObjectField", "xr");

        public static final QName TYPE_FIELD = new QName("http://v8.1c.ru/8.3/xcf/readable", "TypeField", "xr");

        public static final QName VALUE_FIELD = new QName("http://v8.1c.ru/8.3/xcf/readable", "ValueField", "xr");

        public static final QName MULTIPLE_VALUES_KEY_FIELD =
            new QName("http://v8.1c.ru/8.3/xcf/readable", "MultipleValuesKeyField", "xr");

        public static final QName MULTIPLE_VALUES_ORDER_FIELD =
            new QName("http://v8.1c.ru/8.3/xcf/readable", "MultipleValuesOrderField", "xr");

        public static final QName NAME = new QName("http://v8.1c.ru/8.3/xcf/readable", "Name", "xr");

        public static final QName VALUE_CHANGE = new QName("http://v8.1c.ru/8.3/xcf/readable", "ValueChange", "xr");

        public static final QName METADATA = new QName("http://v8.1c.ru/8.3/xcf/readable", "Metadata", "xr");

        public static final QName USE = new QName("http://v8.1c.ru/8.3/xcf/readable", "Use", "xr");

        public static final QName CONDITIONAL_SEPARATION =
            new QName("http://v8.1c.ru/8.3/xcf/readable", "ConditionalSeparation", "xr");
    }

    public static class XPR
    {
        public static final QName PREDEFINED_DATA = new QName("http://v8.1c.ru/8.3/xcf/predef", "PredefinedData");

        public static final QName NAME = new QName("http://v8.1c.ru/8.3/xcf/predef", "Name");

        public static final QName DESCRIPTION = new QName("http://v8.1c.ru/8.3/xcf/predef", "Description");

        public static final QName ITEM = new QName("http://v8.1c.ru/8.3/xcf/predef", "Item");

        public static final QName CODE = new QName("http://v8.1c.ru/8.3/xcf/predef", "Code");

        public static final QName IS_FOLDER = new QName("http://v8.1c.ru/8.3/xcf/predef", "IsFolder");

        public static final QName CHILD_ITEMS = new QName("http://v8.1c.ru/8.3/xcf/predef", "ChildItems");

        public static final QName ACCOUNTING_FLAGS = new QName("http://v8.1c.ru/8.3/xcf/predef", "AccountingFlags");

        public static final QName FLAG = new QName("http://v8.1c.ru/8.3/xcf/predef", "Flag");

        public static final QName ACCOUNT_TYPE = new QName("http://v8.1c.ru/8.3/xcf/predef", "AccountType");

        public static final QName OFF_BALANCE = new QName("http://v8.1c.ru/8.3/xcf/predef", "OffBalance");

        public static final QName ORDER = new QName("http://v8.1c.ru/8.3/xcf/predef", "Order");

        public static final QName TYPE = new QName("http://v8.1c.ru/8.3/xcf/predef", "Type");

        public static final QName ACTION_PERIOD_IS_BASE =
            new QName("http://v8.1c.ru/8.3/xcf/predef", "ActionPeriodIsBase");

        public static final QName BASE = new QName("http://v8.1c.ru/8.3/xcf/predef", "Base");

        public static final QName LEADING = new QName("http://v8.1c.ru/8.3/xcf/predef", "Leading");

        public static final QName DISPLACED = new QName("http://v8.1c.ru/8.3/xcf/predef", "Displaced");

        public static final QName EXT_DIMENSION_TYPES =
            new QName("http://v8.1c.ru/8.3/xcf/predef", "ExtDimensionTypes");

        public static final QName EXT_DIMENSION_TYPE = new QName("http://v8.1c.ru/8.3/xcf/predef", "ExtDimensionType");

        public static final QName TURNOVER = new QName("http://v8.1c.ru/8.3/xcf/predef", "Turnover");

        public static final QName CALCULATION_TYPE = new QName("http://v8.1c.ru/8.3/xcf/predef", "CalculationType");

        public static final QName EXTENSION_STATE = new QName("http://v8.1c.ru/8.3/xcf/predef", "ExtensionState");

        public static final QName VERSION_ATTRIBUTE = new QName("http://v8.1c.ru/8.3/xcf/predef", "version");

        public static final String NAME_ATTRIBUTE = "name";

        public static final String ID_ATTRIBUTE = "id";

        public static final String REF_ATTRIBUTE = "ref";
    }

    public static class EXT
    {
        public static final QName EXT_PICTURE = new QName("http://v8.1c.ru/8.3/xcf/extrnprops", "ExtPicture");

        public static final QName PICTURE = new QName("http://v8.1c.ru/8.3/xcf/extrnprops", "Picture");

        public static final QName EXCHANGE_PLAN_CONTENT =
            new QName("http://v8.1c.ru/8.3/xcf/extrnprops", "ExchangePlanContent");

        public static final QName ITEM = new QName("http://v8.1c.ru/8.3/xcf/extrnprops", "Item");

        public static final QName METADATA = new QName("http://v8.1c.ru/8.3/xcf/extrnprops", "Metadata");

        public static final QName AUTO_RECORD = new QName("http://v8.1c.ru/8.3/xcf/extrnprops", "AutoRecord");

        public static final QName HELP = new QName("http://v8.1c.ru/8.3/xcf/extrnprops", "Help");

        public static final QName PAGE = new QName("http://v8.1c.ru/8.3/xcf/extrnprops", "Page");
    }

    public static class ENT
    {
        public static final QName DETAILED_DAILY_SCHEDULES =
            new QName("http://v8.1c.ru/8.1/data/enterprise", "DetailedDailySchedules");

        public static final QName WEEK_DAYS = new QName("http://v8.1c.ru/8.1/data/enterprise", "WeekDays");

        public static final QName MONTHS = new QName("http://v8.1c.ru/8.1/data/enterprise", "Months");
    }

    public static class NS
        extends IXmlElements.NS
    {
        public static final String DEFAULT_URI = "http://v8.1c.ru/8.3/MDClasses";
    }
}
