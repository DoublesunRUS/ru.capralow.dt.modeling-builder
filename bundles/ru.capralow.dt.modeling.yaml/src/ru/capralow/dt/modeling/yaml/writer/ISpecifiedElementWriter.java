/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.yaml.writer;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.platform.version.Version;

import ru.capralow.dt.modeling.core.ExportException;

public interface ISpecifiedElementWriter
{
    String SMART_ELEMENT_WRITER = "SmartSpecifiedElementWriter"; //$NON-NLS-1$

    void write(YamlStreamWriter exportXmlStreamWriter, EObject eObject, EStructuralFeature eStructuralFeature,
        boolean writeEmpty, Version version, Map<String, Object> group) throws ExportException;

    class ZeroWriter
        implements ISpecifiedElementWriter
    {
        @Override
        public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
            Version version, Map<String, Object> group) throws ExportException
        {
            // Skip object
        }
    }
}
