/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.core;

import org.eclipse.osgi.service.debug.DebugOptions;
import org.eclipse.osgi.service.debug.DebugOptionsListener;
import org.eclipse.osgi.service.debug.DebugTrace;

import ru.capralow.dt.modeling.internal.core.CorePlugin;

public final class ExportDebugTrace
    implements DebugOptionsListener, DebugTrace
{
    private static ExportDebugTrace instance;
    private static DebugTrace debugTrace;

    static
    {
        ExportDebugTrace.debugTrace = null;
    }

    public static ExportDebugTrace getInstance()
    {
        if (ExportDebugTrace.instance == null)
        {
            ExportDebugTrace.instance = new ExportDebugTrace();
        }
        return ExportDebugTrace.instance;
    }

    private ExportDebugTrace()
    {
    }

    @Override
    public void optionsChanged(final DebugOptions options)
    {
        if (ExportDebugTrace.debugTrace == null)
        {
            ExportDebugTrace.debugTrace = options.newDebugTrace(CorePlugin.ID);
        }
    }

    @Override
    public void trace(final String option, final String message)
    {
        if (ExportDebugTrace.debugTrace != null)
        {
            ExportDebugTrace.debugTrace.trace(option, message);
        }
    }

    @Override
    public void trace(final String option, final String message, final Throwable error)
    {
        if (ExportDebugTrace.debugTrace != null)
        {
            ExportDebugTrace.debugTrace.trace(option, message, error);
        }
    }

    @Override
    public void traceDumpStack(final String option)
    {
        if (ExportDebugTrace.debugTrace != null)
        {
            ExportDebugTrace.debugTrace.traceDumpStack(option);
        }
    }

    @Override
    public void traceEntry(final String option)
    {
        if (ExportDebugTrace.debugTrace != null)
        {
            ExportDebugTrace.debugTrace.traceEntry(option);
        }
    }

    @Override
    public void traceEntry(final String option, final Object methodArgument)
    {
        if (ExportDebugTrace.debugTrace != null)
        {
            ExportDebugTrace.debugTrace.traceEntry(option, methodArgument);
        }
    }

    @Override
    public void traceExit(final String option)
    {
        if (ExportDebugTrace.debugTrace != null)
        {
            ExportDebugTrace.debugTrace.traceExit(option);
        }
    }

    @Override
    public void traceEntry(final String option, final Object[] methodArguments)
    {
        if (ExportDebugTrace.debugTrace != null)
        {
            ExportDebugTrace.debugTrace.traceEntry(option, methodArguments);
        }
    }

    @Override
    public void traceExit(final String option, final Object result)
    {
        if (ExportDebugTrace.debugTrace != null)
        {
            ExportDebugTrace.debugTrace.traceExit(option, result);
        }
    }
}
