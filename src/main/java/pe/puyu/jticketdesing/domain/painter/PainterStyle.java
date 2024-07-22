package pe.puyu.jticketdesing.domain.painter;

import org.jetbrains.annotations.NotNull;

public record PainterStyle(
    @NotNull Integer fontWidth,
    @NotNull Integer fontHeight,
    @NotNull Boolean bold,
    @NotNull Boolean bgInverted,
    @NotNull String charCode
) {

    public PainterStyle(PainterStyle otherPainterStyle){
        this(
            otherPainterStyle.fontWidth,
            otherPainterStyle.fontHeight,
            otherPainterStyle.bold,
            otherPainterStyle.bgInverted,
            otherPainterStyle.charCode
        );
    }
}
