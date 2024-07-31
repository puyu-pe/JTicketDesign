package pe.puyu.jticketdesing.domain.painter;

import org.jetbrains.annotations.NotNull;
import pe.puyu.jticketdesing.domain.inputs.block.PrinterJustifyAlign;
import pe.puyu.jticketdesing.domain.inputs.block.QrCorrectionLevel;

public record QrHints(
    @NotNull Integer size,
    @NotNull PrinterJustifyAlign align,
    @NotNull QrCorrectionLevel correctionLevel
    ) {
}
