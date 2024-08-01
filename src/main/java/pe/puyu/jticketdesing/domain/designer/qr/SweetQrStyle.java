package pe.puyu.jticketdesing.domain.designer.qr;

import org.jetbrains.annotations.NotNull;
import pe.puyu.jticketdesing.domain.components.block.SweetJustify;
import pe.puyu.jticketdesing.domain.components.block.SweetScale;

public record SweetQrStyle(
    @NotNull SweetJustify align,
    @NotNull Integer size,
    @NotNull SweetScale scale
) {
}
