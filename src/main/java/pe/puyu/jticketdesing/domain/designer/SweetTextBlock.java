package pe.puyu.jticketdesing.domain.designer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pe.puyu.jticketdesing.domain.inputpayload.PrinterDesignCell;
import pe.puyu.jticketdesing.domain.inputpayload.PrinterDesignStyle;

import java.util.List;
import java.util.Map;

public record SweetTextBlock(
    @NotNull Integer gap,
    @NotNull Character separator,
    @NotNull Integer nColumns,
    @NotNull Map<@Nullable String, @Nullable PrinterDesignStyle> styles,
    @NotNull List<@Nullable List<@Nullable PrinterDesignCell>> rows
) {
}
