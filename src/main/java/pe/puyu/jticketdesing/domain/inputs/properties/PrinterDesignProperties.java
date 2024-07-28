package pe.puyu.jticketdesing.domain.inputs.properties;

import org.jetbrains.annotations.Nullable;

public record PrinterDesignProperties(
    @Nullable Integer blockWidth,
    @Nullable Boolean normalize,
    @Nullable String charCode,
    @Nullable PrinterDesignCut cutMode
) {

}
