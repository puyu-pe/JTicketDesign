package pe.puyu.jticketdesing.util.escpos;

import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.Style.FontSize;

public class StyleText {
	private FontSize fontWidth = FontSize._1;
	private FontSize fontHeight = FontSize._1;
	private boolean feed = true;
	private boolean bold = false;
	private boolean normalize = false;
	private boolean bgInverted = false;
	private char pad = ' ';
	private JustifyAlign align = JustifyAlign.LEFT;

	private StyleText() {}

	public static StyleTextBuilder builder(){
		return new StyleTextBuilder(new StyleText());
	}

	public static StyleTextBuilder builder(StyleText otherStyle){
		StyleText newStyle = new StyleText();
		newStyle.fontWidth = otherStyle.getFontWidth();
		newStyle.fontHeight = otherStyle.getFontHeight();
		newStyle.feed = otherStyle.feed;
		newStyle.bold = otherStyle.bold;
		newStyle.normalize = otherStyle.normalize;
		newStyle.bgInverted = otherStyle.bgInverted;
		newStyle.pad = otherStyle.pad;
		newStyle.align = otherStyle.align;
		return new StyleTextBuilder(newStyle);
	}

	public Style toEscPosStyle(){
		return StyleEscPosUtil.toEscPosStyle(this);
	}

	public FontSize getFontWidth() {
		return fontWidth;
	}

	public void setFontWidth(FontSize fontWidth) {
		this.fontWidth = fontWidth;
	}

	public FontSize getFontHeight() {
		return fontHeight;
	}

	public void setFontHeight(FontSize fontHeight) {
		this.fontHeight = fontHeight;
	}

	public boolean isFeed() {
		return feed;
	}

	public void setFeed(boolean feed) {
		this.feed = feed;
	}

	public char getPad() {
		return pad;
	}

	public void setPad(char pad) {
		this.pad = pad;
	}

	public JustifyAlign getAlign() {
		return align;
	}

	public void setAlign(JustifyAlign align) {
		this.align = align;
	}

	public boolean isBold() {
		return bold;
	}

	public void setBold(boolean bold) {
		this.bold = bold;
	}

	public boolean isNormalize() {
		return normalize;
	}

	public void setNormalize(boolean normalize) {
		this.normalize = normalize;
	}

	public boolean isBgInverted() {
		return bgInverted;
	}

	public void setBgInverted(boolean bgInverted) {
		this.bgInverted = bgInverted;
	}
}

