package pe.puyu.jticketdesing.domain.designer.qr;

import org.jetbrains.annotations.NotNull;
import pe.puyu.jticketdesing.domain.inputs.block.PrinterJustifyAlign;
import pe.puyu.jticketdesing.domain.inputs.block.ScaleType;

public record SweetQrStyle(
    @NotNull PrinterJustifyAlign align,
    @NotNull Integer size,
    @NotNull ScaleType scale
) {
}
