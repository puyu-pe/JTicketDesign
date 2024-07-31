package pe.puyu.jticketdesing.application.painter.escpos;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.barcode.QRCode;
import com.github.anastaciocintra.escpos.image.*;
import org.jetbrains.annotations.NotNull;
import pe.puyu.jticketdesing.domain.inputs.properties.PrinterCutMode;
import pe.puyu.jticketdesing.domain.inputs.drawer.PrinterPinConnector;
import pe.puyu.jticketdesing.domain.printer.SweetPrinter;
import pe.puyu.jticketdesing.domain.printer.SweetPrinterStyle;
import pe.puyu.jticketdesing.domain.printer.SweetPrinterQrHints;

import java.awt.image.BufferedImage;
import java.io.OutputStream;

public class EscPosPrinter implements SweetPrinter {
    private final EscPos escpos;

    public EscPosPrinter(@NotNull OutputStream buffer) {
        this.escpos = new EscPos(buffer);
    }

    @Override
    public void print(@NotNull String text, @NotNull SweetPrinterStyle style) {
        write(text, style, false);
    }

    @Override
    public void println(@NotNull String text, @NotNull SweetPrinterStyle style) {
        write(text, style, true);
    }

    @Override
    public void printImg(@NotNull BufferedImage image) {
        writBitImage(image, new BitonalThreshold());
    }

    @Override
    public void printQr(@NotNull String data, @NotNull SweetPrinterQrHints hints) {
        int size = Math.max(1, Math.min(hints.size(), 16)); // else throw illegalArgumentException
        try {
            var qrCode = new QRCode();
            qrCode.setSize(size)
                .setJustification(EscPosUtil.toEscPosJustification(hints.align()))
                .setModel(QRCode.QRModel._2)
                .setErrorCorrectionLevel(EscPosUtil.toQRErrorCorrectionLevel(hints.correctionLevel()));
            this.escpos.write(qrCode, data);
        } catch (Exception ignored) {

        }
    }

    @Override
    public void cut(@NotNull Integer feed, @NotNull PrinterCutMode mode) {
        try {
            this.escpos.feed(feed);
            this.escpos.cut(EscPosUtil.toEscPosCutMode(mode));
        } catch (Exception ignored) {

        }
    }

    @Override
    public void openDrawer(@NotNull PrinterPinConnector pin, int t1, int t2) {
        try {
            this.escpos.pulsePin(EscPosUtil.toPinConnector(pin), t1, t2);
        } catch (Exception ignored) {

        }
    }

    private void write(@NotNull String text, @NotNull SweetPrinterStyle style, boolean feed) {
        try {
            Style escposStyle = new Style();
            escposStyle.setFontSize(EscPosUtil.toFontSize(style.fontWidth()), EscPosUtil.toFontSize(style.fontHeight()));
            if (style.bgInverted()) {
                escposStyle.setColorMode(Style.ColorMode.WhiteOnBlack);
            }
            escposStyle.setBold(style.bold());
            this.escpos.setCharacterCodeTable(EscPosUtil.valueCharCodeTable(style.charCode()));
            this.escpos.write(escposStyle, text);
            if (feed) {
                this.escpos.feed(1);
            }
        } catch (Exception ignored) {
        }
    }

    public void writBitImage(BufferedImage image, Bitonal algorithm) {
        try {
            EscPosImage escPosImage = new EscPosImage(new CoffeeImageImpl(image), algorithm);
            RasterBitImageWrapper imageWrapper = new RasterBitImageWrapper();
            this.escpos.write(imageWrapper, escPosImage);
        } catch (Exception ignored) {
        }
    }

}
