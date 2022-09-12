/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.core;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;

public interface IExportOperation
{
    String EXPORT_OPERATION_TRACE_OPTION = "/operation"; //$NON-NLS-1$

    IStatus run(IProgressMonitor paramIProgressMonitor);
}
