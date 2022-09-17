/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.yaml.writer;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.platform.version.Version;
import com.google.inject.ImplementedBy;

import ru.capralow.dt.modeling.md.internal.yaml.writer.MetadataObjectFeatureOrderProvider;

@ImplementedBy(MetadataObjectFeatureOrderProvider.class)
public interface IMetadataObjectFeatureOrderProvider
{
    List<EStructuralFeature> getChildren(EClass paramEClass, Version paramVersion);

    List<EStructuralFeature> getInnerInfo(EClass paramEClass, Version paramVersion);

    List<EStructuralFeature> getProperties(EClass paramEClass, Version paramVersion);
}
