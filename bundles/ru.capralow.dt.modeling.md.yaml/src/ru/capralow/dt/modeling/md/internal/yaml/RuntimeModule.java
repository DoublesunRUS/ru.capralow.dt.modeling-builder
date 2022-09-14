/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.internal.yaml;

import com._1c.g5.v8.dt.core.model.ISymLinkConverter;
import com._1c.g5.v8.dt.md.MdRuntimeModule;
import com.google.inject.Binder;
import com.google.inject.name.Names;

import ru.capralow.dt.modeling.core.IExportFileSystemSupport;
import ru.capralow.dt.modeling.md.internal.yaml.writer.MetadataSmartFeatureWriter;
import ru.capralow.dt.modeling.md.internal.yaml.writer.ProducedTypesOrderProvider;
import ru.capralow.dt.modeling.md.yaml.IYamlExporterExtensionManager;
import ru.capralow.dt.modeling.md.yaml.impl.ExportMdFilesSupport;
import ru.capralow.dt.modeling.md.yaml.impl.ExportMdSymLinkConverter;
import ru.capralow.dt.modeling.md.yaml.impl.MetadataFeatureNameProvider;
import ru.capralow.dt.modeling.md.yaml.impl.NativeMdObjectExporterQualifier;
import ru.capralow.dt.modeling.md.yaml.impl.YamlExporterExtensionManager;
import ru.capralow.dt.modeling.md.yaml.writer.IProducedTypesOrderProvider;
import ru.capralow.dt.modeling.yaml.ExtensionBasedExporterQualifier;
import ru.capralow.dt.modeling.yaml.IExporterQualifier;
import ru.capralow.dt.modeling.yaml.IQNameProvider;
import ru.capralow.dt.modeling.yaml.UnionExporterQualifier;
import ru.capralow.dt.modeling.yaml.writer.ISpecifiedElementWriter;

public class RuntimeModule
    extends MdRuntimeModule
{
    public Class<? extends IQNameProvider> bindIqNameProvider()
    {
        return MetadataFeatureNameProvider.class;
    }

    public Class<? extends ISymLinkConverter> bindISymLinkConverter()
    {
        return ExportMdSymLinkConverter.class;
    }

    public Class<? extends IProducedTypesOrderProvider> bindIProducedTypesOrderProvider()
    {
        return ProducedTypesOrderProvider.class;
    }

    public Class<? extends IYamlExporterExtensionManager> bindIXmlExporterExtensionManager()
    {
        return YamlExporterExtensionManager.class;
    }

    public void configureIProjectFileSystemSupport(Binder binder)
    {
        binder.bind(IExportFileSystemSupport.class).to(ExportMdFilesSupport.class);
        binder.bind(ISpecifiedElementWriter.class)
            .annotatedWith(Names.named(ISpecifiedElementWriter.SMART_ELEMENT_WRITER))
            .to(MetadataSmartFeatureWriter.class);
    }

    public void configureIExporterQualifier(Binder binder)
    {
        binder.bind(IExporterQualifier.class)
            .toInstance(UnionExporterQualifier.combine(new IExporterQualifier[] { new NativeMdObjectExporterQualifier(),
                new ExtensionBasedExporterQualifier() }));
    }
}
