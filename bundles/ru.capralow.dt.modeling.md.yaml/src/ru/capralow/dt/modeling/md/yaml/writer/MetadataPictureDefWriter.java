/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.md.yaml.writer;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import com._1c.g5.v8.dt.mcore.PictureDef;
import com._1c.g5.v8.dt.platform.version.Version;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.yaml.writer.PictureWriter;
import ru.capralow.dt.modeling.yaml.writer.YamlStreamWriter;

public final class MetadataPictureDefWriter
    extends PictureWriter
{
    private static final String PICTURE_STATIC_NAME = "Picture"; //$NON-NLS-1$

    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws ExportException
    {
        throw new IllegalStateException(
            "Cannot invoke com._1c.g5.v8.dt.md.export.xml.writer.MetadataPictureDefWriter#write method"
                + " use #writeMdPicture");
    }

    public void writeMdPicture(YamlStreamWriter writer, PictureDef picture, String pictureFileExtension,
        Map<String, Object> group) throws ExportException
    {
        String fullPictureName =
            PICTURE_STATIC_NAME + ((pictureFileExtension != null) ? (String.valueOf('.') + pictureFileExtension) : ""); //$NON-NLS-1$
        writePictureDefContent(writer, fullPictureName, picture.getTransparentPixel(), group);
    }
}
