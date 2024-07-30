package pe.puyu.jticketdesing.domain.painter;

import org.jetbrains.annotations.NotNull;
import pe.puyu.jticketdesing.domain.inputs.properties.PrinterCutMode;
import pe.puyu.jticketdesing.domain.inputs.drawer.PrinterPinConnector;

import java.awt.image.BufferedImage;

public interface DesignPainter {
    // sin salto de linea
    void print(@NotNull String text, @NotNull PainterStyle style);

    // con salto de linea
    void println(@NotNull String text, @NotNull PainterStyle style);

    void printImg(@NotNull BufferedImage image);

    void printQr(@NotNull String stringQr);

    void cut(@NotNull Integer feed, @NotNull PrinterCutMode mode);

    void openDrawer(@NotNull PrinterPinConnector pin, int t1, int t2);
}
