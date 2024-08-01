package pe.puyu.SweetTicketDesign.application.printer.escpos;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPos.CharacterCodeTable;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.Style.FontSize;
import com.github.anastaciocintra.escpos.barcode.QRCode;
import pe.puyu.SweetTicketDesign.domain.components.block.SweetJustify;
import pe.puyu.SweetTicketDesign.domain.components.block.SweetQrCorrectionLevel;
import pe.puyu.SweetTicketDesign.domain.components.properties.SweetCutMode;
import pe.puyu.SweetTicketDesign.domain.components.drawer.SweetPinConnector;

public class EscPosUtil {

    public static CharacterCodeTable valueCharCodeTable(String charCode) {
        try {
            return EscPos.CharacterCodeTable.valueOf(charCode);
        } catch (Exception ignored) {
            return EscPos.CharacterCodeTable.WPC1252;
        }
    }

    public static EscPos.CutMode toEscPosCutMode(SweetCutMode cutMode) {
        return switch (cutMode) {
            case PART -> EscPos.CutMode.PART;
            case FULL -> EscPos.CutMode.FULL;
        };
    }

    public static EscPos.PinConnector toPinConnector(SweetPinConnector pinConnector){
        return switch (pinConnector){
            case Pin_2 -> EscPos.PinConnector.Pin_2;
            case Pin_5 -> EscPos.PinConnector.Pin_5;
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

    public static EscPosConst.Justification toEscPosJustification(SweetJustify align){
        return switch (align){
            case CENTER -> EscPosConst.Justification.Center;
            case RIGHT -> EscPosConst.Justification.Right;
            default -> EscPosConst.Justification.Left_Default;
        };
    }

    public static QRCode.QRErrorCorrectionLevel toQRErrorCorrectionLevel(SweetQrCorrectionLevel correctionLevel){
        return switch (correctionLevel){
            case Q -> QRCode.QRErrorCorrectionLevel.QR_ECLEVEL_Q;
            case L -> QRCode.QRErrorCorrectionLevel.QR_ECLEVEL_L;
            case H -> QRCode.QRErrorCorrectionLevel.QR_ECLEVEL_H;
            default -> QRCode.QRErrorCorrectionLevel.QR_ECLEVEL_M_Default;
        };
    }
}
