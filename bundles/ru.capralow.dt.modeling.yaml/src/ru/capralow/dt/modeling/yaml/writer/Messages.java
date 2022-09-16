/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.yaml.writer;

import org.eclipse.osgi.util.NLS;

class Messages
    extends NLS
{
    private static final String BUNDLE_NAME = "ru.capralow.dt.modeling.yaml.writer.messages"; //$NON-NLS-1$

    public static String ExportXmlStreamWriter_Error_of_writing_XML_attribute_with_localname__0__and_value__1;

    public static String BorderWriter_has_unknown_border_type;

    public static String ColorWriter_has_unknown_color_type;

    public static String FontWriter_has_empty_font_ref;

    public static String FontWriter_has_unknown_font_ref;

    public static String FontWriter_has_unknown_font_type;

    public static String PictureWriter_Empty_reference_to_picture_in__0__1;

    public static String PictureWriter_unexpected_usage_picture_ref__0__in__1__2;

    public static String PictureWriter_unexpected_usage_picture__0;

    public static String ElementWriter_Invalid_feature_type_0;

    static
    {
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }
}
