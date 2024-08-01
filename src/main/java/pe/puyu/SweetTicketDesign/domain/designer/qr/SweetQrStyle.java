package pe.puyu.SweetTicketDesign.domain.designer.qr;

import org.jetbrains.annotations.NotNull;
import pe.puyu.SweetTicketDesign.domain.components.block.SweetJustify;
import pe.puyu.SweetTicketDesign.domain.components.block.SweetScale;

public record SweetQrStyle(
    @NotNull SweetJustify align,
    @NotNull Integer size,
    @NotNull SweetScale scale
) {
}
