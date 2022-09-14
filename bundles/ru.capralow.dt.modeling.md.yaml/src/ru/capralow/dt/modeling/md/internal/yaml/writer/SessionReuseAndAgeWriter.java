/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.internal.yaml.writer;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.md.yaml.impl.MetadataFeatureNameProvider;
import ru.capralow.dt.modeling.yaml.writer.ISpecifiedElementWriter;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;

@Singleton
public class SessionReuseAndAgeWriter
    implements ISpecifiedElementWriter
{
    @Inject
    private MetadataFeatureNameProvider featureNameProvider;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws ExportException
    {
        if (!(eObject instanceof com._1c.g5.v8.dt.metadata.mdclass.HTTPService)
            && !(eObject instanceof com._1c.g5.v8.dt.metadata.mdclass.WebService))
            throw new IllegalArgumentException(String.format("Invalid object %s", new Object[] { eObject }));
        if (feature != MdClassPackage.Literals.HTTP_SERVICE__REUSE_SESSIONS
            && feature != MdClassPackage.Literals.WEB_SERVICE__REUSE_SESSIONS
            && feature != MdClassPackage.Literals.HTTP_SERVICE__SESSION_MAX_AGE
            && feature != MdClassPackage.Literals.WEB_SERVICE__SESSION_MAX_AGE)
            throw new IllegalArgumentException(String.format("Invalid feature %s", new Object[] { feature }));
        if (version.compareTo(Version.V8_3_9) >= 0)
            writer.writeTextElement(this.featureNameProvider.getElementQName(feature), eObject.eGet(feature));
    }
}
