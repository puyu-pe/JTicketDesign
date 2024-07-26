package pe.puyu.jticketdesing.domain.designer;

import org.jetbrains.annotations.NotNull;
import pe.puyu.jticketdesing.domain.inputpayload.PrinterCutMode;

public record SweetProperties(
    @NotNull Integer blockWidth,
    @NotNull Boolean normalize,
    @NotNull String charCode,
    @NotNull CutProperty cutProperty
) {

    public record CutProperty(
        @NotNull Integer feed,
        @NotNull PrinterCutMode mode
    ) {

    }
}
