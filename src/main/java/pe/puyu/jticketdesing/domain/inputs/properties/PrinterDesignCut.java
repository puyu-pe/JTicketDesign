package pe.puyu.jticketdesing.domain.inputs.properties;

import org.jetbrains.annotations.Nullable;

public record PrinterDesignCut(
    @Nullable Integer feed,
    @Nullable PrinterCutMode mode
) {
}
