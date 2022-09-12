/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.internal.core;

import java.nio.file.Path;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com._1c.g5.v8.activitytracking.core.ISystemIdleService;
import com._1c.g5.v8.dt.core.platform.IDerivedDataManagerProvider;
import com._1c.g5.v8.dt.core.platform.IResourceLookup;
import com._1c.g5.v8.dt.platform.version.IRuntimeVersionSupport;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import ru.capralow.dt.modeling.core.IExportArtifactBuilderFactory;
import ru.capralow.dt.modeling.core.IExportOperation;
import ru.capralow.dt.modeling.core.IExportOperationFactory;
import ru.capralow.dt.modeling.core.IExportServiceRegistry;
import ru.capralow.dt.modeling.core.IExportStrategy;
import ru.capralow.dt.modeling.core.OrdinaryDirectoryBuilder;

public class ExportOperationFactory
    implements IExportOperationFactory
{
    @Inject
    private IExportServiceRegistry exportServiceRegistry;
    @Inject
    private IRuntimeVersionSupport runtimeVersionSupport;
    @Inject
    private IResourceLookup resourceLookup;
    @Inject
    private ISystemIdleService systemIdleService;
    @Inject
    private IDerivedDataManagerProvider derivedDataManagerProvider;

    @Override
    public IExportOperation createExportOperation(final Path targetPath, final EObject... eObjects)
    {
        Preconditions.checkArgument(eObjects.length > 0, "No objects to export");
        final Version version = runtimeVersionSupport.getRuntimeVersion(getProject(eObjects));
        return createExportOperation(targetPath, version, eObjects);
    }

    @Override
    public IExportOperation createExportOperation(final Path targetPath, final Version runtimeVersion,
        final EObject... eObjects)
    {
        return createExportOperation(targetPath, runtimeVersion, IExportStrategy.DEFAULT, eObjects);
    }

    @Override
    public IExportOperation createExportOperation(final Path path, final Version runtimeVersion,
        final IExportStrategy strategy, final EObject... eObjects)
    {
        return doCreateExportOperation(path, runtimeVersion, strategy, () -> OrdinaryDirectoryBuilder.create(path),
            eObjects);
    }

    @Override
    public IExportOperation createExportOperation(final Version runtimeVersion, final IExportStrategy strategy,
        final IExportArtifactBuilderFactory artifactBuilderFactory, final EObject... eObjects)
    {
        return doCreateExportOperation(null, runtimeVersion, strategy, artifactBuilderFactory, eObjects);
    }

    private IExportOperation doCreateExportOperation(final Path path, final Version runtimeVersion,
        final IExportStrategy strategy, final IExportArtifactBuilderFactory artifactBuilderFactory,
        final EObject... eObjects)
    {
        Preconditions.checkArgument(eObjects.length > 0, "No objects to export");
        return new ExportOperation(runtimeVersion, path, artifactBuilderFactory, exportServiceRegistry, resourceLookup,
            systemIdleService, derivedDataManagerProvider, strategy, eObjects);
    }

    private IProject getProject(final EObject... eObjects)
    {
        final IProject result = resourceLookup.getProject(EcoreUtil.getURI(eObjects[0]));
        for (final EObject eObject : eObjects)
        {
            final IProject candidateProject = resourceLookup.getProject(EcoreUtil.getURI(eObject));
            if (!result.equals(candidateProject))
            {
                throw new IllegalArgumentException(
                    String.format("Requested objects belongs to at least 2 different projects: %s and %s",
                        candidateProject.getName(), result.getName()));
            }
        }
        return result;
    }
}
