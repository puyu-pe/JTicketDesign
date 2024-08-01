package pe.puyu.jticketdesing.domain.printer;

import org.jetbrains.annotations.NotNull;
import pe.puyu.jticketdesing.domain.components.block.SweetJustify;
import pe.puyu.jticketdesing.domain.components.block.SweetQrCorrectionLevel;

public record SweetPrinterQrHints(
    @NotNull Integer size,
    @NotNull SweetJustify align,
    @NotNull SweetQrCorrectionLevel correctionLevel
    ) {
}
