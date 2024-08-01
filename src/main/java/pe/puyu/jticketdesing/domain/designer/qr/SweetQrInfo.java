package pe.puyu.jticketdesing.domain.designer.qr;

import org.jetbrains.annotations.NotNull;
import pe.puyu.jticketdesing.domain.components.block.SweetQrCorrectionLevel;
import pe.puyu.jticketdesing.domain.components.block.SweetQrType;

public record SweetQrInfo(
    @NotNull String data,
    @NotNull SweetQrType qrType,
    @NotNull SweetQrCorrectionLevel correctionLevel
) {
}
