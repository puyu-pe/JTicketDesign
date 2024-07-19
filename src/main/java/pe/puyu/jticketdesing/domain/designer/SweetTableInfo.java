package pe.puyu.jticketdesing.domain.designer;

import org.jetbrains.annotations.NotNull;

public record SweetTableInfo(
    @NotNull Integer gap,
    @NotNull String separator,
    @NotNull Integer maxNumberOfColumns
) {
}
