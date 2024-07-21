package pe.puyu.jticketdesing.domain.designer;

import org.jetbrains.annotations.NotNull;

public record SweetProperties (
    @NotNull Integer blockWidth,
    @NotNull Boolean normalize,
    @NotNull String charCode
) {
}
