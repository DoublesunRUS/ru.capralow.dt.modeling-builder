/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.internal.ui;

import java.util.Collection;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.emf.ecore.EObject;

import com._1c.g5.v8.dt.core.platform.IConfigurationAware;
import com._1c.g5.v8.dt.core.platform.IDtProject;
import com._1c.g5.v8.dt.core.platform.IV8Project;
import com._1c.g5.v8.dt.core.platform.IV8ProjectManager;
import com._1c.g5.v8.dt.metadata.mdclass.Configuration;
import com._1c.g5.v8.dt.metadata.mdclass.MdObject;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;

public class ExportConfigurationWizardPageData
{
    private final Map<IProject, ProjectData> projects;
    private IProject project;
    private String targetPath;

    @Inject
    public ExportConfigurationWizardPageData(final IV8ProjectManager projectManager)
    {
        this.projects = this.initProjectsData(projectManager);
    }

    public String getLabel(final IProject project)
    {
        final ProjectData projectData = this.projects.get(project);
        final MdObject rootObject = projectData.getRootObject();
        final Version version = projectData.getVersion();
        return String.format("%s - %s (v. %s)", project.getName(), (rootObject != null) ? rootObject.getName() : "", //$NON-NLS-1$ //$NON-NLS-2$
            version);
    }

    public IProject getProject()
    {
        return this.project;
    }

    public Collection<IProject> getProjects()
    {
        return this.projects.keySet();
    }

    public EObject getRootObject()
    {
        if (this.project != null)
        {
            return this.projects.get(this.project).getRootObject();
        }
        return null;
    }

    public String getTargetPath()
    {
        return this.targetPath;
    }

    public Version getVersion()
    {
        return this.projects.get(this.project).getVersion();
    }

    public void setProject(final IProject project)
    {
        this.project = this.projects.containsKey(project) ? project : null;
    }

    public void setTargetPath(final String path)
    {
        this.targetPath = (path != null) ? path.trim() : null;
    }

    private Map<IProject, ProjectData> initProjectsData(final IV8ProjectManager projectManager)
    {
        final ImmutableMap.Builder<IProject, ProjectData> builder = ImmutableMap.builder();
        for (final IV8Project project1 : projectManager.getProjects())
        {
            if (project1 instanceof IConfigurationAware
                && project1.getDtProject().getType() == IDtProject.WORKSPACE_PROJECT_TYPE)
            {
                final Configuration configuration = ((IConfigurationAware)project1).getConfiguration();
                builder.put(project1.getProject(), new ProjectData(project1.getVersion(), configuration));
            }
        }
        return builder.build();
    }

    private static class ProjectData
    {
        private final Version version;
        private final MdObject rootObject;

        ProjectData(final Version version, final MdObject rootObject)
        {
            this.version = version;
            this.rootObject = rootObject;
        }

        public MdObject getRootObject()
        {
            return this.rootObject;
        }

        public Version getVersion()
        {
            return this.version;
        }
    }
}
