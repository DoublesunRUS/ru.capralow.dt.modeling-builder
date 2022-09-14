/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.internal.yaml.writer;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.metadata.common.CommonPackage;
import com._1c.g5.v8.dt.metadata.common.TransactionsIsolationLevel;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.yaml.IQNameProvider;
import ru.capralow.dt.modeling.yaml.writer.ISpecifiedElementWriter;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;

public class TransactionsIsolationLevelWriter
    implements ISpecifiedElementWriter
{
    @Inject
    private IQNameProvider nameProvider;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws ExportException
    {
        Preconditions.checkArgument((feature.getEType() == CommonPackage.Literals.TRANSACTIONS_ISOLATION_LEVEL),
            "Invalid feature type");
        TransactionsIsolationLevel transactionsIsolationLevel = (TransactionsIsolationLevel)eObject.eGet(feature);
        writer.writeTextElement(this.nameProvider.getElementQName(feature), getStringValue(transactionsIsolationLevel));
    }

    private String getStringValue(TransactionsIsolationLevel value)
    {
        return (value == TransactionsIsolationLevel.AUTOMATIC) ? "Auto" : value.getLiteral();
    }
}
