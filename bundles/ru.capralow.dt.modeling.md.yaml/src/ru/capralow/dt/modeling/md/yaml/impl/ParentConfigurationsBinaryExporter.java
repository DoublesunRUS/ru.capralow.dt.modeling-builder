/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.yaml.impl;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com._1c.g5.v8.bm.integration.IBmModel;
import com._1c.g5.v8.dt.binary.model.BinaryData;
import com._1c.g5.v8.dt.core.platform.IBmModelManager;
import com._1c.g5.v8.dt.md.IExternalPropertyManagerRegistry;
import com._1c.g5.v8.dt.metadata.IExternalPropertyManager;
import com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.core.IExportArtifactBuilder;
import ru.capralow.dt.modeling.md.internal.yaml.MdYamlPlugin;
import ru.capralow.dt.modeling.yaml.BasicExporter;
import ru.capralow.dt.modeling.yaml.IExportContext;

@Singleton
public class ParentConfigurationsBinaryExporter
    extends BasicExporter
{
    @Inject
    private Provider<IExternalPropertyManagerRegistry> externalPropertyManagerRegistryProvider;

    @Inject
    private Provider<IBmModelManager> bmModelManagerProvider;

    @Override
    public boolean isAppropriate(Version version, EObject eObject)
    {
        return isSupported(eObject) && super.isAppropriate(version, eObject);
    }

    @Override
    public IStatus work(EObject eObject, IExportContext exportContext, IExportArtifactBuilder artifactBuilder,
        IProgressMonitor progressMonitor) throws ExportException
    {
        MultiStatus status = new MultiStatus(MdYamlPlugin.ID, 0, "", null); //$NON-NLS-1$

        return status;
    }

    private IBmModel getBmModel(EObject referenceObject)
    {
        if (referenceObject.eIsProxy())
        {
            return bmModelManagerProvider.get().getModel(EcoreUtil.getURI(referenceObject));
        }
        return bmModelManagerProvider.get().getModel(referenceObject);
    }

    private IExternalPropertyManager getExternalPropertyManager(EObject referenceObject)
    {
        IBmModel bmModel = getBmModel(referenceObject);
        return externalPropertyManagerRegistryProvider.get().getExternalPropertyManager(bmModel);
    }

    private boolean isSupported(EObject eObject)
    {
        return eObject instanceof BinaryData && getExternalPropertyManager(eObject)
            .getReference(eObject) == MdClassPackage.Literals.CONFIGURATION__PARENT_CONFIGURATIONS;
    }

}
