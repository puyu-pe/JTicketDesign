package pe.puyu.jticketdesing.domain.components.block;

import org.jetbrains.annotations.Nullable;

public record SweetCellComponent(
    @Nullable String className,
    @Nullable String text
) {
}
