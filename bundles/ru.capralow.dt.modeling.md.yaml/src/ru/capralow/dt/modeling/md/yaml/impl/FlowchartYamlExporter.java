/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.yaml.impl;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.emf.ecore.EObject;

import com._1c.g5.v8.dt.platform.version.Version;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.core.IExportArtifactBuilder;
import ru.capralow.dt.modeling.md.internal.yaml.MdYamlPlugin;
import ru.capralow.dt.modeling.yaml.BasicExporter;
import ru.capralow.dt.modeling.yaml.IExportContext;

@Singleton
public class FlowchartYamlExporter
    extends BasicExporter
{
    @Override
    public boolean isAppropriate(Version version, EObject eObject)
    {
        return eObject instanceof com._1c.g5.v8.dt.metadata.mdclass.AbstractFlowchart
            && super.isAppropriate(version, eObject);
    }

    @Override
    public IStatus work(EObject eObject, IExportContext exportContext, IExportArtifactBuilder artifactBuilder,
        IProgressMonitor progressMonitor) throws ExportException
    {
        MultiStatus status = new MultiStatus(MdYamlPlugin.ID, 0, "", null); //$NON-NLS-1$

        return status;
    }

}
