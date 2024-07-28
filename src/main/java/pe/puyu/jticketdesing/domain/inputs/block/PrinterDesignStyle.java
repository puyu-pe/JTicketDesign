package pe.puyu.jticketdesing.domain.inputs.block;

import org.jetbrains.annotations.Nullable;

public record PrinterDesignStyle(
    @Nullable Integer fontWidth,
    @Nullable Integer fontHeight,
    @Nullable Boolean bold,
    @Nullable Boolean normalize,
    @Nullable Boolean bgInverted,
    @Nullable Character pad,
    @Nullable PrinterJustifyAlign align,
    @Nullable Integer span
) {

}
