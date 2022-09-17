/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.yaml;

import java.util.Arrays;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

import com._1c.g5.v8.dt.platform.version.Version;
import com.google.common.base.Preconditions;

public class UnionExporterQualifier
    implements IExporterQualifier
{
    public static IExporterQualifier combine(IExporterQualifier... qualifiers)
    {
        return new UnionExporterQualifier(Arrays.asList(qualifiers));
    }

    private final List<IExporterQualifier> qualifiers;

    public UnionExporterQualifier(List<IExporterQualifier> qualifiers)
    {
        Preconditions.checkArgument(qualifiers != null);
        this.qualifiers = qualifiers;
    }

    @Override
    public boolean qualify(IExporter exporter, Version version, EObject eObject)
    {
        return this.qualifiers.stream().allMatch(qualifier -> qualifier.qualify(exporter, version, eObject));
    }
}
