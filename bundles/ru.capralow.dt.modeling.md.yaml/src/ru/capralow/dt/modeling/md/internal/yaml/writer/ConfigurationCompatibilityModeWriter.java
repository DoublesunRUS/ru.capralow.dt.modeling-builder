/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.internal.yaml.writer;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.metadata.mdclass.CompatibilityMode;
import com._1c.g5.v8.dt.metadata.mdclass.MdClassPackage;
import com._1c.g5.v8.dt.metadata.mdclass.util.MdClassUtil;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.yaml.IQNameProvider;
import ru.capralow.dt.modeling.yaml.writer.ISpecifiedElementWriter;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;

@Singleton
public class ConfigurationCompatibilityModeWriter
    implements ISpecifiedElementWriter
{
    @Inject
    private IQNameProvider nameManager;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws ExportException
    {
        if (!(eObject instanceof com._1c.g5.v8.dt.metadata.mdclass.Configuration))
        {
            throw new IllegalArgumentException(String.format("Invalid object %s", new Object[] { eObject }));
        }
        if (feature != MdClassPackage.Literals.CONFIGURATION__COMPATIBILITY_MODE
            && feature != MdClassPackage.Literals.CONFIGURATION__CONFIGURATION_EXTENSION_COMPATIBILITY_MODE)
        {
            throw new IllegalArgumentException(String.format("Invalid feature %s", new Object[] { feature }));
        }
        CompatibilityMode mode = (CompatibilityMode)eObject.eGet(feature);
        if (version.compareTo(Version.V8_3_9) < 0)
        {
            CompatibilityMode currentMode =
                MdClassUtil.getCompatibilityMode(version.getMajor(), version.getMinor(), version.getMicro());
            if (currentMode == mode)
            {
                writer.writeElement(this.nameManager.getElementQName(feature), "DontUse");
                return;
            }
        }
        writer.writeElement(this.nameManager.getElementQName(feature), mode.getName());
    }
}
