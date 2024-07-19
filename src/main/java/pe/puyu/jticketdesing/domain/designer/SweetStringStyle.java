package pe.puyu.jticketdesing.domain.designer;

import org.jetbrains.annotations.NotNull;
import pe.puyu.jticketdesing.domain.inputpayload.PrinterJustifyAlign;

public record SweetStringStyle(
    @NotNull Integer span,
    @NotNull String pad,
    @NotNull PrinterJustifyAlign align,
    @NotNull Boolean normalize
) {
}
