package pe.puyu.jticketdesing.domain.painter;

import org.jetbrains.annotations.NotNull;

public interface DesignPainter {
    // sin salto de linea
    void print(@NotNull String text, @NotNull PainterStyle style);

    // con salto de linea
    void println(@NotNull String text, @NotNull PainterStyle style);

    void img(@NotNull String localImgPath);

    void qr(@NotNull String stringQr);
}
