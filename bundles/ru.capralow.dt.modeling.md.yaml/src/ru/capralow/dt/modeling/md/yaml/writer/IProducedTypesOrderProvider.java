/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.yaml.writer;

import java.util.List;

import org.eclipse.emf.ecore.EReference;

import com._1c.g5.v8.dt.metadata.mdtype.MdTypes;
import com.google.inject.ImplementedBy;

import ru.capralow.dt.modeling.md.internal.yaml.writer.ProducedTypesOrderProvider;

@ImplementedBy(ProducedTypesOrderProvider.class)
public interface IProducedTypesOrderProvider
{
    List<EReference> getOrderedReferences(MdTypes paramMdTypes);
}
