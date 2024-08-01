package pe.puyu.jticketdesing.domain.printer;

import org.jetbrains.annotations.NotNull;
import pe.puyu.jticketdesing.domain.components.properties.SweetCutMode;
import pe.puyu.jticketdesing.domain.components.drawer.SweetPinConnector;

import java.awt.image.BufferedImage;

public interface SweetPrinter {
    // sin salto de linea
    void print(@NotNull String text, @NotNull SweetPrinterStyle style);

    // con salto de linea
    void println(@NotNull String text, @NotNull SweetPrinterStyle style);

    void printImg(@NotNull BufferedImage image);

    void printQr(@NotNull String data, @NotNull SweetPrinterQrHints hints);

    void cut(@NotNull Integer feed, @NotNull SweetCutMode mode);

    void openDrawer(@NotNull SweetPinConnector pin, int t1, int t2);
}
