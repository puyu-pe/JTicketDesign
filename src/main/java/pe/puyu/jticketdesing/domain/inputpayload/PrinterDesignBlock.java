package pe.puyu.jticketdesing.domain.inputpayload;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public record PrinterDesignBlock(
    @Nullable Integer gap,
    @Nullable Character separator,
    @Nullable String stringQR,
    @Nullable String imgPath,
    @Nullable Integer nColumns,
    @Nullable Map<String, PrinterDesignStyle> styles,
    @Nullable List<List<PrinterDesignCell>> rows
) {
}
