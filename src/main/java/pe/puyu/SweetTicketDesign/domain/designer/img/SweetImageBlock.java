package pe.puyu.SweetTicketDesign.domain.designer.img;

import org.jetbrains.annotations.NotNull;

public record SweetImageBlock(
    @NotNull String imgPath,
    @NotNull Integer widthInPx,
    @NotNull SweetImageInfo imageInfo
) {
}
