package pe.puyu.jticketdesing.domain.painter;

import org.jetbrains.annotations.NotNull;

public record PainterStyle(
    @NotNull Integer fontWidth,
    @NotNull Integer fontHeight,
    @NotNull Boolean bold,
    @NotNull Boolean bgInverted,
    @NotNull Boolean charCode
) {
}
