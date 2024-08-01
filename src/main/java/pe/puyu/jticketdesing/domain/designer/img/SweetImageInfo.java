package pe.puyu.jticketdesing.domain.designer.img;


import org.jetbrains.annotations.NotNull;
import pe.puyu.jticketdesing.domain.components.block.SweetJustify;
import pe.puyu.jticketdesing.domain.components.block.SweetScale;

public record SweetImageInfo(
    @NotNull SweetScale scale,
    @NotNull Integer width,
    @NotNull Integer height,
    @NotNull SweetJustify align
) {
}
