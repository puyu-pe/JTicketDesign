package pe.puyu.jticketdesing.domain.inputpayload;

import org.jetbrains.annotations.Nullable;

public record PrinterDesignCell(
    @Nullable String className,
    @Nullable String text
) {
}
