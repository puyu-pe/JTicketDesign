package pe.puyu.jticketdesing.domain.designer.qr;

import org.jetbrains.annotations.NotNull;
import pe.puyu.jticketdesing.domain.inputs.block.QrCorrectionLevel;
import pe.puyu.jticketdesing.domain.inputs.block.QrType;

public record SweetQrInfo(
    @NotNull String data,
    @NotNull QrType qrType,
    @NotNull QrCorrectionLevel correctionLevel
) {
}
