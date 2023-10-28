package pe.puyu.jticketdesing.util.escpos;

import java.awt.image.BufferedImage;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.EscPosConst.Justification;
import com.github.anastaciocintra.escpos.Style.ColorMode;
import com.github.anastaciocintra.escpos.Style.FontSize;
import com.github.anastaciocintra.escpos.barcode.QRCode;
import com.github.anastaciocintra.escpos.barcode.QRCode.QRErrorCorrectionLevel;
import com.github.anastaciocintra.escpos.barcode.QRCode.QRModel;
import com.github.anastaciocintra.escpos.image.Bitonal;
import com.github.anastaciocintra.escpos.image.CoffeeImageImpl;
import com.github.anastaciocintra.escpos.image.EscPosImage;
import com.github.anastaciocintra.escpos.image.RasterBitImageWrapper;

import pe.puyu.jticketdesing.util.StringUtils;

public class EscPosWrapper {
  private EscPos escPos;
  private final StyleWrapper style;

  public EscPosWrapper(EscPos escPos) {
    this(escPos, new Style());
  }

  public EscPosWrapper(EscPos escPos, Style style) {
    this.escPos = escPos;
    this.style = new StyleWrapper(style);
  }

  public void setStyle(Style style) {
    this.style.setStyle(style);
  }

  public void setEscPos(EscPos escPos) {
    this.escPos = escPos;
  }

  public EscPos getEscPos() {
    return escPos;
  }

  public void toCenter(String text, int maxWidth) throws Exception {
    toCenter(text, maxWidth, FontSize._1, true, ' ');
  }

  public void toCenter(String text, int maxWidth, FontSize fontsize) throws Exception {
    toCenter(text, maxWidth, fontsize, true, ' ');
  }

  public void toCenter(String text, int maxWidth, FontSize width, FontSize height) throws Exception {
    toCenter(text, maxWidth, width, height, true, ' ');
  }

  public void toCenter(String text, int maxWidth, char pad) throws Exception {
    toCenter(text, maxWidth, FontSize._1, true, pad);
  }

  public void toCenter(String text, int maxWidth, boolean feed) throws Exception {
    toCenter(text, maxWidth, FontSize._1, feed, ' ');
  }

  public void toCenter(String text, int maxWidth, boolean feed, char pad) throws Exception {
    toCenter(text, maxWidth, FontSize._1, feed, pad);
  }

  public void toCenter(String text, int maxWidth, FontSize fontsize, boolean feed) throws Exception {
    toCenter(text, maxWidth, fontsize, feed, ' ');
  }

  public void toCenter(String text, int maxWidth, FontSize width, FontSize height, boolean feed) throws Exception {
    toCenter(text, maxWidth, width, height, feed, ' ');
  }

  public void toCenter(String text, int maxWidth, FontSize fontsize, char pad) throws Exception {
    toCenter(text, maxWidth, fontsize, true, pad);
  }

  public void toCenter(String text, int maxWidth, FontSize width, FontSize height, char pad) throws Exception {
    toCenter(text, maxWidth, width, height, true, pad);
  }

  public void toCenter(String text, int maxWidth, FontSize fontSize, boolean feed, char pad) throws Exception {
    toCenter(text, maxWidth, fontSize, fontSize, feed, pad);
  }

  public void toCenter(String text, int maxWidth, FontSize width, FontSize height, boolean feed, char pad)
      throws Exception {
    text = StringUtils.cutOverflow(text, maxWidth, StyleWrapper.valueFontSize(width));
    int spacesAvailable = maxWidth - (text.length() * StyleWrapper.valueFontSize(width));
    int startSpaces = spacesAvailable / 2;
    int endSpaces = spacesAvailable - startSpaces;
    escPos.write(style.fontSize(1), StringUtils.repeat(pad, startSpaces));
    escPos.write(style.fontSize(width, height), text);
    escPos.write(style.fontSize(1), StringUtils.repeat(pad, endSpaces));
    if (feed)
      this.escPos.feed(1);
  }

  public void toLeft(String text, int maxWidth) throws Exception {
    toLeft(text, maxWidth, FontSize._1, true, ' ');
  }

  public void toLeft(String text, int maxWidth, FontSize fontsize) throws Exception {
    toLeft(text, maxWidth, fontsize, true, ' ');
  }

  public void toLeft(String text, int maxWidth, FontSize width, FontSize height) throws Exception {
    toLeft(text, maxWidth, width, height, true, ' ');
  }

  public void toLeft(String text, int maxWidth, char pad) throws Exception {
    toLeft(text, maxWidth, FontSize._1, true, pad);
  }

  public void toLeft(String text, int maxWidth, boolean feed) throws Exception {
    toLeft(text, maxWidth, FontSize._1, feed, ' ');
  }

  public void toLeft(String text, int maxWidth, boolean feed, char pad) throws Exception {
    toLeft(text, maxWidth, FontSize._1, feed, pad);
  }

  public void toLeft(String text, int maxWidth, FontSize fontsize, boolean feed) throws Exception {
    toLeft(text, maxWidth, fontsize, feed, ' ');
  }

  public void toLeft(String text, int maxWidth, FontSize width, FontSize height, boolean feed) throws Exception {
    toLeft(text, maxWidth, width, height, feed, ' ');
  }

  public void toLeft(String text, int maxWidth, FontSize fontsize, char pad) throws Exception {
    toLeft(text, maxWidth, fontsize, true, pad);
  }

  public void toLeft(String text, int maxWidth, FontSize width, FontSize height, char pad) throws Exception {
    toLeft(text, maxWidth, width, height, true, pad);
  }

  public void toLeft(String text, int maxWidth, FontSize fontSize, boolean feed, char pad) throws Exception {
    toLeft(text, maxWidth, fontSize, fontSize, feed, pad);
  }

  public void toLeft(String text, int maxWidth, FontSize width, FontSize height, boolean feed, char pad)
      throws Exception {
    text = StringUtils.cutOverflow(text, maxWidth, StyleWrapper.valueFontSize(width));
    int spacesAvailable = maxWidth - (text.length() * StyleWrapper.valueFontSize(width));
    escPos.write(style.fontSize(width, height), text);
    escPos.write(style.fontSize(1), StringUtils.repeat(pad, spacesAvailable));
    if (feed)
      this.escPos.feed(1);
  }

  public void toRight(String text, int maxWidth, FontSize width, FontSize height, boolean feed, char pad)
      throws Exception {
    text = StringUtils.cutOverflow(text, maxWidth, StyleWrapper.valueFontSize(width));
    int spacesAvailable = maxWidth - (text.length() * StyleWrapper.valueFontSize(width));
    escPos.write(style.fontSize(1), StringUtils.repeat(pad, spacesAvailable));
    escPos.write(style.fontSize(width, height), text);
    if (feed)
      this.escPos.feed(1);
  }

  public void toRight(String text, int maxWidth, FontSize fontSize, boolean feed, char pad)
      throws Exception {
    toRight(text, maxWidth, fontSize, fontSize, feed, pad);
  }

  public void toRight(String text, int maxWidth) throws Exception {
    toRight(text, maxWidth, FontSize._1, true, ' ');
  }

  public void toRight(String text, int maxWidth, FontSize fontsize) throws Exception {
    toRight(text, maxWidth, fontsize, true, ' ');
  }

  public void toRight(String text, int maxWidth, FontSize width, FontSize height) throws Exception {
    toRight(text, maxWidth, width, height, true, ' ');
  }

  public void toRight(String text, int maxWidth, char pad) throws Exception {
    toRight(text, maxWidth, FontSize._1, true, pad);
  }

  public void toRight(String text, int maxWidth, boolean feed) throws Exception {
    toRight(text, maxWidth, FontSize._1, feed, ' ');
  }

  public void toRight(String text, int maxWidth, boolean feed, char pad) throws Exception {
    toRight(text, maxWidth, FontSize._1, feed, pad);
  }

  public void toRight(String text, int maxWidth, FontSize fontsize, boolean feed) throws Exception {
    toRight(text, maxWidth, fontsize, feed, ' ');
  }

  public void toRight(String text, int maxWidth, FontSize width, FontSize height, boolean feed) throws Exception {
    toRight(text, maxWidth, width, height, feed, ' ');
  }

  public void toRight(String text, int maxWidth, FontSize fontsize, char pad) throws Exception {
    toRight(text, maxWidth, fontsize, true, pad);
  }

  public void toRight(String text, int maxWidth, FontSize width, FontSize height, char pad) throws Exception {
    toRight(text, maxWidth, width, height, true, pad);
  }

  public void printLine(char pad, int maxWidth) throws Exception {
    this.printLine(pad, maxWidth, true);
  }

  public void printLine(char pad, int maxWidth, boolean feed) throws Exception {
    this.escPos.write(style.fontSize(1), StringUtils.repeat(pad, maxWidth));
    if (feed)
      this.escPos.feed(1);
  }

  public void printBoldLine(char pad, int maxWidth) throws Exception {
    this.escPos.writeLF(style.fontSize(1).setBold(true), StringUtils.repeat(pad, maxWidth));
    style.noBold();
  }

  public EscPosWrapper addStyleBold() {
    this.style.getStyle().setBold(true);
    return this;
  }

  public EscPosWrapper removeStyleBold() {
    this.style.getStyle().setBold(false);
    return this;
  }

  public EscPosWrapper addStyleInverted() {
    this.style.getStyle().setColorMode(ColorMode.WhiteOnBlack);
    return this;
  }

  public EscPosWrapper removeStyleInverted() {
    this.style.getStyle().setColorMode(ColorMode.BlackOnWhite_Default);
    return this;
  }

  public void bitImage(BufferedImage image, Bitonal algorithm) throws Exception {
    EscPosImage escPosImage = new EscPosImage(new CoffeeImageImpl(image), algorithm);
    RasterBitImageWrapper imageWrapper = new RasterBitImageWrapper();
    this.escPos.write(imageWrapper, escPosImage);
  }

  public void standardQR(String stringQr) throws Exception {
    var qrCode = new QRCode();
    qrCode
        .setSize(4)
        .setJustification(Justification.Center)
        .setModel(QRModel._2)
        .setErrorCorrectionLevel(QRErrorCorrectionLevel.QR_ECLEVEL_Q);
    this.escPos.write(qrCode, stringQr);
  }
}
