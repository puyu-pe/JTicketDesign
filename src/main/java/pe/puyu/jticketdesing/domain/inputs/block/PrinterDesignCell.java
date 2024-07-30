package pe.puyu.jticketdesing.domain.inputs.block;

import org.jetbrains.annotations.Nullable;

public record PrinterDesignCell(
    @Nullable String className,
    @Nullable String text
) {
}
