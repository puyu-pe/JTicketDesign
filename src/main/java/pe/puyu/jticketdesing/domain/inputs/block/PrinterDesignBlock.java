package pe.puyu.jticketdesing.domain.inputs.block;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public record PrinterDesignBlock(
    @Nullable Integer gap,
    @Nullable Character separator,
    @Nullable PrinterDesignQr qr,
    @Nullable String imgPath,
    @Nullable Integer nColumns,
    @Nullable Map<@NotNull String, @Nullable PrinterDesignStyle> styles,
    @Nullable List<@Nullable List<@Nullable PrinterDesignCell>> rows
) {
}
