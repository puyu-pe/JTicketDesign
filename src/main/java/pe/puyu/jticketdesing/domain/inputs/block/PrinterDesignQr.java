package pe.puyu.jticketdesing.domain.inputs.block;

import org.jetbrains.annotations.Nullable;

public record PrinterDesignQr(
    @Nullable String data,
    @Nullable QrType qrType,
    @Nullable QrCorrectionLevel correctionLevel
) {
}
