package pe.puyu.SweetTicketDesign.domain.components.block;

import org.jetbrains.annotations.Nullable;

public record SweetStyleComponent(
    @Nullable Integer fontWidth,
    @Nullable Integer fontHeight,
    @Nullable Boolean bold,
    @Nullable Boolean normalize,
    @Nullable Boolean bgInverted,
    @Nullable Character pad,
    @Nullable SweetJustify align,
    @Nullable Integer span,
    @Nullable SweetScale scale,
    @Nullable Integer width,
    @Nullable Integer height
) {

}
