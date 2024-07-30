package pe.puyu.jticketdesing.domain.designer.img;

import org.jetbrains.annotations.NotNull;
import pe.puyu.jticketdesing.domain.inputs.block.ScaleType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class SweetImageHelper {

    public static BufferedImage toBufferedImage(String pathToImage) throws Exception {
        return ImageIO.read(new File(pathToImage));
    }

    public static BufferedImage justify(
        @NotNull BufferedImage image,
        @NotNull Integer containerWidth,
        @NotNull SweetImageInfo imageInfo
    ) {
        BufferedImage centerImage = new BufferedImage(containerWidth, imageInfo.height(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = centerImage.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, containerWidth, imageInfo.width());
        switch (imageInfo.align()){
            case CENTER:
                g.drawImage(image, (containerWidth - imageInfo.width()) / 2, 0, null);
                break;
            case RIGHT:
                g.drawImage(image, containerWidth - imageInfo.width() - 2, 0, null);
                break;
            default:
                g.drawImage(image, 0, 0, null);
        }
        g.dispose();
        return centerImage;
    }

    public static BufferedImage resize(BufferedImage image, SweetImageInfo imageInfo) {
        Image scaledImage = image.getScaledInstance(imageInfo.width(), imageInfo.height(), toImageScaleType(imageInfo.scaleType()));
        BufferedImage resizedImage = new BufferedImage(imageInfo.width(), imageInfo.height(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(scaledImage, 0, 0, null);
        g.dispose();
        return resizedImage;
    }

    public static int toImageScaleType(@NotNull ScaleType scaleType) {
        return switch (scaleType) {
            case DEFAULT -> Image.SCALE_DEFAULT;
            case FAST -> Image.SCALE_FAST;
            case SMOOTH -> Image.SCALE_SMOOTH;
            case REPLICATE -> Image.SCALE_REPLICATE;
            case AREA_AVERAGING -> Image.SCALE_AREA_AVERAGING;
        };
    }


}
