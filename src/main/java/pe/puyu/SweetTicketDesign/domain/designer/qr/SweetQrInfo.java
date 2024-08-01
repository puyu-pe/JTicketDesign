package pe.puyu.SweetTicketDesign.domain.designer.qr;

import org.jetbrains.annotations.NotNull;
import pe.puyu.SweetTicketDesign.domain.components.block.SweetQrCorrectionLevel;
import pe.puyu.SweetTicketDesign.domain.components.block.SweetQrType;

public record SweetQrInfo(
    @NotNull String data,
    @NotNull SweetQrType qrType,
    @NotNull SweetQrCorrectionLevel correctionLevel
) {
}
