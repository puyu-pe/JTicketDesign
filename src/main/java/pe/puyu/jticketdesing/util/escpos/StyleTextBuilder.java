package pe.puyu.jticketdesing.util.escpos;

import com.github.anastaciocintra.escpos.Style.FontSize;

public class StyleTextBuilder {
	private final StyleText styleText;

	public StyleTextBuilder(StyleText styleText){
		this.styleText = styleText;
	}

	public StyleTextBuilder fontWidth(int width){
		fontWidth(StyleEscPosUtil.toFontSize(width));
		return this;
	}

	public StyleTextBuilder fontWidth(FontSize width){
		styleText.setFontWidth(width);
		return this;
	}

	public StyleTextBuilder fontHeight(int height){
		fontHeight(StyleEscPosUtil.toFontSize(height));
		return this;
	}

	public StyleTextBuilder fontHeight(FontSize height){
		styleText.setFontHeight(height);
		return this;
	}

	public StyleTextBuilder fontSize(FontSize fontSize){
		styleText.setFontWidth(fontSize);
		styleText.setFontHeight(fontSize);
		return this;
	}

	public StyleTextBuilder fontSize(int fontSize){
		FontSize size = StyleEscPosUtil.toFontSize(fontSize);
		styleText.setFontWidth(size);
		styleText.setFontHeight(size);
		return this;
	}

	public StyleTextBuilder feed(boolean value){
		styleText.setFeed(value);
		return this;
	}

	public StyleTextBuilder pad(char value){
		styleText.setPad(value);
		return this;
	}

	public StyleTextBuilder align(JustifyAlign value){
		styleText.setAlign(value);
		return this;
	}

	public StyleTextBuilder align(String value){
		styleText.setAlign(JustifyAlign.fromValue(value));
		return this;
	}

	public StyleTextBuilder bold(boolean value){
		styleText.setBold(value);
		return this;
	}

	public StyleTextBuilder normalize(boolean value){
		styleText.setNormalize(value);
		return this;
	}

	public StyleTextBuilder bgInverted(boolean value){
		styleText.setBgInverted(value);
		return this;
	}

	public StyleText build(){
		return this.styleText;
	}
}
