/**
 * Copyright (c) 2022, Aleksandr Kapralov
 */
package ru.capralow.dt.modeling.yaml.writer;

import java.text.MessageFormat;

import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.util.Strings;

import com._1c.g5.v8.bm.core.IBmObject;
import com._1c.g5.v8.dt.core.naming.ISymbolicNameService;
import com._1c.g5.v8.dt.mcore.McorePackage;
import com._1c.g5.v8.dt.mcore.Picture;
import com._1c.g5.v8.dt.mcore.PictureDef;
import com._1c.g5.v8.dt.mcore.PictureRef;
import com._1c.g5.v8.dt.mcore.Point;
import com._1c.g5.v8.dt.platform.version.Version;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;

import ru.capralow.dt.modeling.core.ExportException;
import ru.capralow.dt.modeling.internal.yaml.YamlPlugin;
import ru.capralow.dt.modeling.yaml.IqNameProvider;

public class PictureWriter
    implements ISpecifiedElementWriter
{
    private static final String PICTURE_EXTENSION_KEY = "picture.extension"; //$NON-NLS-1$

    @Inject
    private IqNameProvider nameProvider;

    @Inject
    private ISymbolicNameService symbolicNameService;

    @Override
    public void write(YamlStreamWriter writer, EObject eObject, EStructuralFeature feature, boolean writeEmpty,
        Version version) throws ExportException
    {
        Preconditions.checkArgument(feature.getEType() == McorePackage.Literals.PICTURE, "Invalid feature type in %s",
            feature);
        QName featureName = this.nameProvider.getElementQName(feature);
        Picture picture = (Picture)eObject.eGet(feature);
        if (checkPicture(picture))
        {
            writer.writeElement(featureName.toString(), "");
            writePicture(writer, picture, eObject, feature);
//            writer.writeEndElement();
        }
        else if (writeEmpty)
        {
//            writer.writeEmptyElement(featureName.toString());
        }
    }

    protected final void writePictureRefContent(YamlStreamWriter writer, String reference, boolean loadTransparent)
        throws ExportException

    {
        writer.writeElement("XR.REF", reference);
        writer.writeElement("XR.LOAD_TRANSPARENT", Boolean.valueOf(loadTransparent));
    }

    protected final void writePictureDefContent(YamlStreamWriter writer, String pictureName, Point transparentPixel)
        throws ExportException
    {
        writer.writeElement("XR.ABS", pictureName);
        writer.writeElement("XR.LOAD_TRANSPARENT", Boolean.valueOf(transparentPixel != null));
        if (transparentPixel != null && transparentPixel.getX() != -1 && transparentPixel.getY() != -1)
        {
//            writer.writeEmptyElement(IYamlElements.XR.TRANSPARENT_PIXEL);
            writer.writeElement("x", Integer.toString(transparentPixel.getX()));
            writer.writeElement("y", Integer.toString(transparentPixel.getY()));
        }
    }

    private boolean checkPicture(Picture picture)
    {
        if (picture != null)
        {
            if (picture instanceof PictureDef)
            {
                if (!(picture instanceof IBmObject))
                {
                    YamlPlugin.logError(MessageFormat.format(Messages.PictureWriter_unexpected_usage_picture__0,
                        new Object[] { picture.getClass().getName() }), null);
                    return false;
                }
                return true;
            }
            if (picture instanceof PictureRef)
            {
                Picture referencePicture = ((PictureRef)picture).getPicture();
                if (referencePicture == null)
                {
                    YamlPlugin.logError(
                        MessageFormat.format(Messages.PictureWriter_Empty_reference_to_picture_in__0__1, new Object[] {
                            picture.eContainer().eClass().getName(), picture.eContainingFeature().getName() }),
                        null);
                    return false;
                }
                if (!(referencePicture instanceof com._1c.g5.v8.dt.metadata.mdclass.CommonPicture)
                    && !(referencePicture instanceof com._1c.g5.v8.dt.platform.model.PlatformPicture)
                    && !referencePicture.eIsProxy())
                {
                    YamlPlugin.logError(
                        MessageFormat.format(Messages.PictureWriter_unexpected_usage_picture_ref__0__in__1__2,
                            new Object[] { referencePicture.getClass().getName(),
                                picture.eContainer().eClass().getName(), picture.eContainingFeature().getName() }),
                        null);
                    return false;
                }
                return true;
            }
            YamlPlugin.logError(MessageFormat.format(Messages.PictureWriter_unexpected_usage_picture__0,
                new Object[] { picture.eClass().getName() }), null);
        }
        return false;
    }

    private void writePicture(YamlStreamWriter writer, Picture picture, EObject eObject, EStructuralFeature feature)
        throws ExportException
    {
        if (picture instanceof PictureDef)
        {
            writePictureDef(writer, (PictureDef)picture, eObject, feature);
        }
        if (picture instanceof PictureRef)
        {
            writePictureRef(writer, (PictureRef)picture, eObject, feature);
        }
    }

    private void writePictureRef(YamlStreamWriter writer, PictureRef picture, EObject contextObject,
        EStructuralFeature feature) throws ExportException
    {
        Picture referencePicture = picture.getPicture();
        String reference = this.symbolicNameService.generateSymbolicName(referencePicture, picture,
            McorePackage.Literals.PICTURE_REF__PICTURE);
        boolean loadTransparent = !(!(referencePicture instanceof com._1c.g5.v8.dt.platform.model.PlatformPicture)
            && (!(referencePicture instanceof com._1c.g5.v8.dt.metadata.mdclass.CommonPicture)
                || ((PictureDef)referencePicture).getTransparentPixel() == null)
            && (!referencePicture.eIsProxy() || !reference.startsWith("StdPicture.")));
        writePictureRefContent(writer, reference, loadTransparent);
    }

    private void writePictureDef(YamlStreamWriter writer, PictureDef picture, EObject contextObject,
        EStructuralFeature feature) throws ExportException
    {
        String pictureExtension = ((IBmObject)picture).bmGetProperty(PICTURE_EXTENSION_KEY);
        pictureExtension =
            ("picture".equals(pictureExtension) || pictureExtension == null) ? "" : ("." + pictureExtension);
        String pictureName = String.valueOf(Strings.toFirstUpper(feature.getName())) + pictureExtension;
        Point transparentPixel = picture.getTransparentPixel();
        writePictureDefContent(writer, pictureName, transparentPixel);
    }
}
