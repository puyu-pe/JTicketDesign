package pe.puyu.jticketdesing.domain.designer;

import org.jetbrains.annotations.NotNull;
import pe.puyu.jticketdesing.domain.inputpayload.PrinterJustifyAlign;

public record SweetStringStyle(
    @NotNull Integer span,
    @NotNull Character pad,
    @NotNull PrinterJustifyAlign align,
    @NotNull Boolean normalize
) {
    public SweetStringStyle(SweetStringStyle otherStringStyle) {
        this(
            otherStringStyle.span,
            otherStringStyle.pad,
            otherStringStyle.align,
            otherStringStyle.normalize
        );
    }
}
