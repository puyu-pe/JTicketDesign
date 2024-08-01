package pe.puyu.SweetTicketDesign.domain.printer;

import org.jetbrains.annotations.NotNull;
import pe.puyu.SweetTicketDesign.domain.components.block.SweetJustify;
import pe.puyu.SweetTicketDesign.domain.components.block.SweetQrCorrectionLevel;

public record SweetPrinterQrHints(
    @NotNull Integer size,
    @NotNull SweetJustify align,
    @NotNull SweetQrCorrectionLevel correctionLevel
    ) {
}
