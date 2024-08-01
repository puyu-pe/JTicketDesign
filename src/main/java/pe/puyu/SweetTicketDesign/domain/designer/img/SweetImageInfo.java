package pe.puyu.SweetTicketDesign.domain.designer.img;


import org.jetbrains.annotations.NotNull;
import pe.puyu.SweetTicketDesign.domain.components.block.SweetJustify;
import pe.puyu.SweetTicketDesign.domain.components.block.SweetScale;

public record SweetImageInfo(
    @NotNull SweetScale scale,
    @NotNull Integer width,
    @NotNull Integer height,
    @NotNull SweetJustify align
) {
}
