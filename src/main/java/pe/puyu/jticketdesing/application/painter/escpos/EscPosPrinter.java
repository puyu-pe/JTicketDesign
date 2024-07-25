package pe.puyu.jticketdesing.application.painter.escpos;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.Style;
import org.jetbrains.annotations.NotNull;
import pe.puyu.jticketdesing.domain.painter.DesignPainter;
import pe.puyu.jticketdesing.domain.painter.PainterStyle;

import java.io.OutputStream;

public class EscPosPrinter implements DesignPainter {
    private final EscPos escpos;

    public EscPosPrinter(@NotNull OutputStream buffer) {
        this.escpos = new EscPos(buffer);
    }

    @Override
    public void print(@NotNull String text, @NotNull PainterStyle style) {
        write(text, style, false);
    }

    @Override
    public void println(@NotNull String text, @NotNull PainterStyle style) {
        write(text, style, true);
    }

    @Override
    public void img(@NotNull String localImgPath) {

    }

    @Override
    public void qr(@NotNull String stringQr) {

    }

    private void write(@NotNull String text, @NotNull PainterStyle style, boolean feed){
        try{
            Style escposStyle = new Style();
            escposStyle.setFontSize(StyleEscPosUtil.toFontSize(style.fontWidth()), StyleEscPosUtil.toFontSize(style.fontHeight()));
            if(style.bgInverted()){
                escposStyle.setColorMode(Style.ColorMode.WhiteOnBlack);
            }
            escposStyle.setBold(style.bold());
            this.escpos.setCharacterCodeTable(StyleEscPosUtil.valueCharCodeTable(style.charCode()));
            this.escpos.write(escposStyle, text);
            if(feed){
                this.escpos.feed(1);
            }
        }catch (Exception ignored){}
    }

}