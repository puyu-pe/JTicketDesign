package pe.puyu.jticketdesing.domain.components.block;

import org.jetbrains.annotations.Nullable;

public record SweetQrComponent(
    @Nullable String data,
    @Nullable SweetQrType qrType,
    @Nullable SweetQrCorrectionLevel correctionLevel
) {
}
