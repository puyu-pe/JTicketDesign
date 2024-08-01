package pe.puyu.SweetTicketDesign.domain.table.util.escpos;

import com.github.anastaciocintra.escpos.Style;

@Deprecated(since = "0.1.0", forRemoval = true)
public class StyleEscPosUtil {

	public static Style toEscPosStyle(StyleText styleText){
		Style style = new Style();
		style.setFontSize(styleText.getFontWidth(), styleText.getFontHeight());
		style.setBold(styleText.isBold());
		if(styleText.isBgInverted()){
			style.setColorMode(Style.ColorMode.WhiteOnBlack);
		}
		return style;
	}


	public static int valueFontSize(Style.FontSize fontSize) {
		return switch (fontSize) {
			case _2 -> 2;
			case _3 -> 3;
			case _4 -> 4;
			case _5 -> 5;
			case _6 -> 6;
			case _7 -> 7;
			default -> 1;
		};
	}

	public static Style.FontSize toFontSize(int size) {
		return switch (size) {
			case 2 -> Style.FontSize._2;
			case 3 -> Style.FontSize._3;
			case 4 -> Style.FontSize._4;
			case 5 -> Style.FontSize._5;
			case 6 -> Style.FontSize._6;
			case 7 -> Style.FontSize._7;
			default -> Style.FontSize._1;
		};
	}
}
