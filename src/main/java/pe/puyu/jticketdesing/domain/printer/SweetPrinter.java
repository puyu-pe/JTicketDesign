package pe.puyu.jticketdesing.domain.printer;

import org.jetbrains.annotations.NotNull;
import pe.puyu.jticketdesing.domain.inputs.properties.PrinterCutMode;
import pe.puyu.jticketdesing.domain.inputs.drawer.PrinterPinConnector;

import java.awt.image.BufferedImage;

public interface SweetPrinter {
    // sin salto de linea
    void print(@NotNull String text, @NotNull SweetPrinterStyle style);

    // con salto de linea
    void println(@NotNull String text, @NotNull SweetPrinterStyle style);

    void printImg(@NotNull BufferedImage image);

    void printQr(@NotNull String data, @NotNull SweetPrinterQrHints hints);

    void cut(@NotNull Integer feed, @NotNull PrinterCutMode mode);

    void openDrawer(@NotNull PrinterPinConnector pin, int t1, int t2);
}
