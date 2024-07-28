package pe.puyu.jticketdesing.domain.inputs.drawer;

import org.jetbrains.annotations.Nullable;

public record PrinterDesignOpenDrawer(
    @Nullable PrinterPinConnector pin,
    @Nullable Integer t1,
    @Nullable Integer t2
) {
}
