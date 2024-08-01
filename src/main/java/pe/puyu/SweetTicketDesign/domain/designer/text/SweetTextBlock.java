package pe.puyu.SweetTicketDesign.domain.designer.text;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pe.puyu.SweetTicketDesign.domain.components.block.SweetCellComponent;
import pe.puyu.SweetTicketDesign.domain.components.block.SweetStyleComponent;

import java.util.List;
import java.util.Map;

public record SweetTextBlock(
    @NotNull Integer gap,
    @NotNull Character separator,
    @NotNull Integer nColumns,
    @NotNull Map<@Nullable String, @Nullable SweetStyleComponent> styles,
    @NotNull List<@Nullable List<@Nullable SweetCellComponent>> rows
) {
}
