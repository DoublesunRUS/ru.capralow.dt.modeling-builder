/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.core;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;

import com._1c.g5.wiring.IManagedService;

public interface IExportService
    extends IManagedService
{
    String EXPORT_SERVICE_TRACE_OPTION = "/operation/service"; //$NON-NLS-1$

    IStatus work(EObject p0, IExportArtifactBuilder p1, boolean p2, boolean p3, IProgressMonitor p4);
}
