/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.core;

public class ExportException
    extends Exception
{
    public ExportException(final String message)
    {
        super(message);
    }

    public ExportException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    public ExportException(final Throwable cause)
    {
        super(cause);
    }
}
