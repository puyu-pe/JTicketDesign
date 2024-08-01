package pe.puyu.SweetTicketDesign.domain.components.block;

import org.jetbrains.annotations.Nullable;

public record SweetCellComponent(
    @Nullable String className,
    @Nullable String text
) {
}
