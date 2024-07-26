package pe.puyu.jticketdesing.domain.inputpayload;

import org.jetbrains.annotations.Nullable;

public record PrinterDesignProperties(
    @Nullable Integer blockWidth,
    @Nullable Boolean normalize,
    @Nullable String charCode,
    @Nullable PrinterDesignCut cutMode
) {

}
