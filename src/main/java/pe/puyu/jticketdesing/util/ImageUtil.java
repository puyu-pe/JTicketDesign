package pe.puyu.jticketdesing.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ImageUtil {
  public static BufferedImage toBufferedImage(String pathToImage) throws Exception {
    return ImageIO.read(new File(pathToImage));
  }

  public static BufferedImage justifyImageToCenter(BufferedImage image, int containerWidth, int sizeImg) {
    BufferedImage centerImage = new BufferedImage(containerWidth, sizeImg, 2);
    Graphics2D g = centerImage.createGraphics();
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, containerWidth, sizeImg);
    g.drawImage(image, (containerWidth - sizeImg) / 2, 0, null);
    g.dispose();
    return centerImage;
  }

  public static BufferedImage resizeImage(BufferedImage image, int newSize) {
    Image scaledImage = image.getScaledInstance(newSize, newSize, Image.SCALE_SMOOTH);
    BufferedImage resizedImage = new BufferedImage(newSize, newSize, 2);
    Graphics2D g = resizedImage.createGraphics();
    g.drawImage(scaledImage, 0, 0, null);
    g.dispose();
    return resizedImage;
  }

}
