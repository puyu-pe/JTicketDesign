package pe.puyu.jticketdesing.domain.inputpayload;

import org.jetbrains.annotations.Nullable;

public record PrinterDesignCut(
    @Nullable Integer feed,
    @Nullable PrinterCutMode mode
) {
}
