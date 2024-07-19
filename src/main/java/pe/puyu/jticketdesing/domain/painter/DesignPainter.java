package pe.puyu.jticketdesing.domain.painter;

import org.jetbrains.annotations.NotNull;

public interface DesignPainter {
    void printText(@NotNull String text, @NotNull PainterStyle style);

    void printImg(@NotNull String localImgPath);

    void printQR(@NotNull String stringQr);
}
