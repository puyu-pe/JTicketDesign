package pe.puyu.jticketdesing.domain.designer.img;


import org.jetbrains.annotations.NotNull;
import pe.puyu.jticketdesing.domain.inputs.block.PrinterJustifyAlign;
import pe.puyu.jticketdesing.domain.inputs.block.ScaleType;

public record SweetImageInfo(
    @NotNull ScaleType scaleType,
    @NotNull Integer width,
    @NotNull Integer height,
    @NotNull PrinterJustifyAlign align
) {
}
