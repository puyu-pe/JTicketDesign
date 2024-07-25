package pe.puyu.jticketdesing.application.painter.escpos;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPos.CharacterCodeTable;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.Style.FontSize;

public class StyleEscPosUtil {

    public static CharacterCodeTable valueCharCodeTable(String charCode) {
        try {
            return EscPos.CharacterCodeTable.valueOf(charCode);
        } catch (Exception ignored) {
            return EscPos.CharacterCodeTable.WPC1252;
        }
    }

    public static int valueFontSize(FontSize fontSize) {
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

    public static FontSize toFontSize(int size) {
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
