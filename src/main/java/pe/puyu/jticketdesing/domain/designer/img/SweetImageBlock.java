package pe.puyu.jticketdesing.domain.designer.img;

import org.jetbrains.annotations.NotNull;

public record SweetImageBlock(
    @NotNull String imgPath,
    @NotNull Integer widthInPx,
    @NotNull SweetImageInfo imageInfo
) {
}
