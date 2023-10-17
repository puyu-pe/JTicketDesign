package pe.puyu.jticketdesing.util.escpos;

import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.Style.ColorMode;
import com.github.anastaciocintra.escpos.Style.FontSize;

public class StyleWrapper {
  private Style style;

  public StyleWrapper(Style style) {
    this.style = style;
  }

  public StyleWrapper() {
    this(new Style());
  }

  public Style fontSize(FontSize fontSize) {
    this.style.setFontSize(fontSize, fontSize);
    return this.style;
  }

  public Style fontSize(FontSize width, FontSize height) {
    this.style.setFontSize(width, height);
    return this.style;
  }

  public Style fontSize(int size) {
    var fontSize = toFontSize(size);
    return this.style.setFontSize(fontSize, fontSize);
  }

  public Style fontSize(int width, int height) {
    var fontWidth = toFontSize(width);
    var fontHeight = toFontSize(height);
    return this.style.setFontSize(fontWidth, fontHeight);
  }

  public Style bold() {
    return this.style.setBold(true);
  }

  public Style noBold() {
    return this.style.setBold(false);
  }

  public Style bgInverted() {
    return this.style.setColorMode(ColorMode.WhiteOnBlack);
  }

  public static Style textInverted() {
    return new Style().setColorMode(ColorMode.WhiteOnBlack);
  }

  public static Style textInvertedBold() {
    return new Style().setColorMode(ColorMode.WhiteOnBlack).setBold(true);
  }

  public static Style textBold() {
    return new Style().setBold(true);
  }

  public Style reset() {
    this.style.reset();
    return this.style;
  }

  public StyleWrapper setStyle(Style style) {
    this.style = style;
    return this;
  }

  public Style getStyle() {
    return style;
  }

  public static int valueFontSize(FontSize fontSize) {
    switch (fontSize) {
      case _2:
        return 2;
      case _3:
        return 3;
      case _4:
        return 4;
      case _5:
        return 5;
      case _6:
        return 6;
      case _7:
        return 7;
      default:
        return 1;
    }
  }

  public static FontSize toFontSize(int size) {
    switch (size) {
      case 2:
        return FontSize._2;
      case 3:
        return FontSize._3;
      case 4:
        return FontSize._4;
      case 5:
        return FontSize._5;
      case 6:
        return FontSize._6;
      case 7:
        return FontSize._7;
      default:
        return FontSize._1;
    }
  }
}
