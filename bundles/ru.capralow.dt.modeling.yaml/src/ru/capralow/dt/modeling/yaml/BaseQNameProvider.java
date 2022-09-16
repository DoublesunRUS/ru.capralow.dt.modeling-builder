/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.yaml;

import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.util.Strings;

import com.google.common.collect.ImmutableMap;

public class BaseQNameProvider
    implements IqNameProvider
{
    protected final ImmutableMap<EStructuralFeature, QName> specifiedFeatureNames;

    protected final ImmutableMap<EClass, QName> specifiedClassNames;

    public BaseQNameProvider()
    {
        ImmutableMap.Builder<EStructuralFeature, QName> featuresBuilder = ImmutableMap.builder();
        fillSpecifiedFeatureNames(featuresBuilder);
        this.specifiedFeatureNames = featuresBuilder.build();

        ImmutableMap.Builder<EClass, QName> classesBuilder = ImmutableMap.builder();
        fillSpecifiedClassNames(classesBuilder);
        this.specifiedClassNames = classesBuilder.build();
    }

    @Override
    public QName getClassQName(EClass eClass)
    {
        return specifiedClassNames.containsKey(eClass) ? (QName)specifiedClassNames.get(eClass)
            : uncapitalizeFirstLetter(eClass);
    }

    @Override
    public QName getElementQName(EStructuralFeature feature)
    {
        return specifiedFeatureNames.containsKey(feature) ? (QName)specifiedFeatureNames.get(feature)
            : capitalizeFirstLetter(feature);
    }

    protected void fillSpecifiedClassNames(ImmutableMap.Builder<EClass, QName> builder)
    {
        // Nothing to do
    }

    protected void fillSpecifiedFeatureNames(ImmutableMap.Builder<EStructuralFeature, QName> builder)
    {
        // Nothing to do
    }

    protected boolean needToCapitalizeFirstLetterOfFeatureName()
    {
        return true;
    }

    private QName capitalizeFirstLetter(EStructuralFeature feature)
    {
        String name =
            needToCapitalizeFirstLetterOfFeatureName() ? Strings.toFirstUpper(feature.getName()) : feature.getName();
        return new QName(name);
    }

    private QName uncapitalizeFirstLetter(EClass eClass)
    {
        String name =
            needToCapitalizeFirstLetterOfFeatureName() ? eClass.getName() : Strings.toFirstLower(eClass.getName());
        return new QName(name);
    }
}
