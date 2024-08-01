package pe.puyu.SweetTicketDesign.domain.table.util.escpos;

import java.awt.image.BufferedImage;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.EscPosConst.Justification;
import com.github.anastaciocintra.escpos.barcode.QRCode;
import com.github.anastaciocintra.escpos.barcode.QRCode.QRErrorCorrectionLevel;
import com.github.anastaciocintra.escpos.barcode.QRCode.QRModel;
import com.github.anastaciocintra.escpos.image.Bitonal;
import com.github.anastaciocintra.escpos.image.CoffeeImageImpl;
import com.github.anastaciocintra.escpos.image.EscPosImage;
import com.github.anastaciocintra.escpos.image.RasterBitImageWrapper;

import pe.puyu.SweetTicketDesign.domain.table.util.StringUtils;

@Deprecated(since = "0.1.0", forRemoval = true)
public class EscPosWrapper {
  private final EscPos escPos;

  public EscPosWrapper(EscPos escPos) {
    this.escPos = escPos;
  }

  public void printText(String text,  int maxWidth, StyleText styleText) throws Exception {
    text = StringUtils.cutOverflow(text, maxWidth, StyleEscPosUtil.valueFontSize(styleText.getFontWidth()));
    int spacesAvailable = maxWidth - (text.length() * StyleEscPosUtil.valueFontSize(styleText.getFontWidth()));
    int startSpaces = spacesAvailable / 2;
    int endSpaces = spacesAvailable - startSpaces;

    if(styleText.isNormalize()){
      text = StringUtils.normalize(text);
    }

    Style spacesStyle = StyleText.builder(styleText) // copy from styleText
      .fontSize(1)
      .build()
      .toEscPosStyle();

    Style escPosTextStyle = styleText.toEscPosStyle();

    switch (styleText.getAlign()){
      case CENTER:
        escPos.write(spacesStyle, StringUtils.repeat(styleText.getPad(), startSpaces));
        escPos.write(escPosTextStyle, text);
        escPos.write(spacesStyle, StringUtils.repeat(styleText.getPad(), endSpaces));
        break;
      case RIGHT:
        escPos.write(spacesStyle, StringUtils.repeat(styleText.getPad(), spacesAvailable));
        escPos.write(escPosTextStyle, text);
        break;
      default: // LEFT
        escPos.write(escPosTextStyle, text);
        escPos.write(spacesStyle, StringUtils.repeat(styleText.getPad(), spacesAvailable));
    }
    if (styleText.isFeed())
      this.escPos.feed(1);
  }

  public void printText(String text, int maxWidth) throws Exception {
    printText(text, maxWidth, StyleText.builder().build());
  }

  public void printLine(char pad, int maxWidth) throws Exception {
    StyleText styleText = StyleText.builder()
      .fontSize(1)
      .feed(true)
      .build();
    this.printLine(pad, maxWidth, styleText);
  }

  public void printLine(char pad, int maxWidth, StyleText styleText) throws Exception {
    Style escPosTextStyle = StyleEscPosUtil.toEscPosStyle(styleText);
    this.escPos.write(escPosTextStyle, StringUtils.repeat(pad, maxWidth));
    if (styleText.isFeed())
      this.escPos.feed(1);
  }

  public void printBoldLine(char pad, int maxWidth) throws Exception {
    Style styleText = StyleText.builder()
      .fontSize(1)
      .bold(true)
      .build()
      .toEscPosStyle();
    this.escPos.writeLF(styleText, StringUtils.repeat(pad, maxWidth));
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
