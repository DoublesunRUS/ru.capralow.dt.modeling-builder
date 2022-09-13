/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.yaml.writer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import ru.capralow.dt.modeling.core.ExportException;

public class YamlStreamWriter
    implements AutoCloseable
{
    protected final OutputStreamWriter writer;
    protected final Yaml yaml;

    public YamlStreamWriter(OutputStream outputStream) throws ExportException
    {
        DumperOptions options = new DumperOptions();
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        this.yaml = new Yaml(options);
        try
        {
            this.writer = new OutputStreamWriter(outputStream, "UTF-8"); //$NON-NLS-1$
        }
        catch (UnsupportedEncodingException e)
        {
            throw new ExportException(e.getMessage(), e);
        }
    }

    @Override
    public void close() throws IOException
    {
        writer.close();
    }

    public void writeElement(String localName, Object value) throws ExportException
    {
        if (localName == null || value == null)
        {
            throw new ExportException(MessageFormat.format(
                Messages.ExportXmlStreamWriter_Error_of_writing_XML_attribute_with_localname__0__and_value__1,
                new Object[] { localName, value }));
        }

        writeElement(localName, String.valueOf(value));
    }

    public void writeElement(String localName, String value) throws ExportException
    {
        if (localName == null || value == null)
        {
            throw new ExportException(MessageFormat.format(
                Messages.ExportXmlStreamWriter_Error_of_writing_XML_attribute_with_localname__0__and_value__1,
                new Object[] { localName, value }));
        }

        Map<String, Object> data = new HashMap<>();
        data.put(localName, value);

        yaml.dump(data, writer);
    }

    public void writeElement(QName qName, Object value) throws ExportException
    {
        if (qName == null || value == null)
        {
            throw new ExportException(MessageFormat.format(
                Messages.ExportXmlStreamWriter_Error_of_writing_XML_attribute_with_localname__0__and_value__1,
                new Object[] { qName, value }));
        }

        writeElement(qName, String.valueOf(value));
    }

    public void writeElement(QName qName, String value) throws ExportException
    {
        if (qName == null || value == null)
        {
            throw new ExportException(MessageFormat.format(
                Messages.ExportXmlStreamWriter_Error_of_writing_XML_attribute_with_localname__0__and_value__1,
                new Object[] { qName, value }));
        }

        Map<String, Object> data = new HashMap<>();
        data.put(qName.getLocalPart(), value);

        yaml.dump(data, writer);
    }

    public void writeElement(QName qName, QName qValue) throws ExportException
    {
        if (qName == null || qValue == null)
        {
            throw new ExportException(MessageFormat.format(
                Messages.ExportXmlStreamWriter_Error_of_writing_XML_attribute_with_localname__0__and_value__1,
                new Object[] { qName, qValue }));
        }

        StringBuilder value = new StringBuilder();
        if (!qValue.getPrefix().isEmpty())
        {
            value.append(qValue.getPrefix()).append(':');
        }

        value.append(qValue.getLocalPart());
        writeElement(qName, value.toString());
    }

}
