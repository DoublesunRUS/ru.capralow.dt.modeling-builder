/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.yaml.writer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import ru.capralow.dt.modeling.core.ExportException;

public class YamlStreamWriter
    implements AutoCloseable
{
    protected Yaml yaml;

    protected OutputStreamWriter writer;

    protected Map<String, Object> data;

    public YamlStreamWriter(OutputStream outputStream) throws ExportException
    {
        DumperOptions options = new DumperOptions();
//        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setIndentWithIndicator(true);
        options.setIndent(4);
        this.yaml = new Yaml(options);

        try
        {
            this.writer = new OutputStreamWriter(outputStream, "UTF-8"); //$NON-NLS-1$
        }
        catch (UnsupportedEncodingException e)
        {
            throw new ExportException(e.getMessage(), e);
        }

        data = new HashMap<>();
    }

    @Override
    public void close() throws IOException
    {
        if (!(data.isEmpty()))
        {
            yaml.dump(data, writer);
        }
        writer.close();
        yaml = null;
    }

    public void writeElement(String localName, String value) throws ExportException
    {
        if (localName == null || value == null)
        {
            throw new ExportException(MessageFormat.format(
                Messages.ExportXmlStreamWriter_Error_of_writing_XML_attribute_with_localname__0__and_value__1,
                new Object[] { localName, value }));
        }

        data.put(localName, value);
    }

    public void writeElement(String localName, String value, Map<String, Object> group) throws ExportException
    {
        if (localName == null || value == null)
        {
            throw new ExportException(MessageFormat.format(
                Messages.ExportXmlStreamWriter_Error_of_writing_XML_attribute_with_localname__0__and_value__1,
                new Object[] { localName, value }));
        }

        group.put(localName, value);
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

        writeElement(qName.getLocalPart(), String.valueOf(value));
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

    public Map<String, Object> addGroup(List<Object> list) throws ExportException
    {
        if (list == null)
        {
            throw new ExportException(MessageFormat.format(
                Messages.ExportXmlStreamWriter_Error_of_writing_XML_attribute_with_localname__0__and_value__1,
                new Object[] { list }));
        }

        Map<String, Object> groupData = new HashMap<>();
        list.add(groupData);

        return groupData;
    }

    public Map<String, Object> addGroup(String localName) throws ExportException
    {
        return addGroup(localName, data);
    }

    public Map<String, Object> addGroup(QName qName) throws ExportException
    {
        if (qName == null)
        {
            throw new ExportException(MessageFormat.format(
                Messages.ExportXmlStreamWriter_Error_of_writing_XML_attribute_with_localname__0__and_value__1,
                new Object[] { qName }));
        }

        return addGroup(qName.getLocalPart(), data);
    }

    public Map<String, Object> addGroup(String localName, Map<String, Object> group) throws ExportException
    {
        if (localName == null)
        {
            throw new ExportException(MessageFormat.format(
                Messages.ExportXmlStreamWriter_Error_of_writing_XML_attribute_with_localname__0__and_value__1,
                new Object[] { localName }));
        }

        Map<String, Object> groupData = new HashMap<>();
        group.put(localName, groupData);

        return groupData;
    }

    public List<Object> addList(String localName) throws ExportException
    {
        return addList(localName, data);
    }

    public List<Object> addList(QName qName) throws ExportException
    {
        if (qName == null)
        {
            throw new ExportException(MessageFormat.format(
                Messages.ExportXmlStreamWriter_Error_of_writing_XML_attribute_with_localname__0__and_value__1,
                new Object[] { qName }));
        }

        return addList(qName.getLocalPart(), data);
    }

    public List<Object> addList(String localName, Map<String, Object> group) throws ExportException
    {
        if (localName == null)
        {
            throw new ExportException(MessageFormat.format(
                Messages.ExportXmlStreamWriter_Error_of_writing_XML_attribute_with_localname__0__and_value__1,
                new Object[] { localName }));
        }

        List<Object> groupData = new ArrayList<>();
        group.put(localName, groupData);

        return groupData;
    }

}
