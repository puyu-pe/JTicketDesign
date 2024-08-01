package pe.puyu.jticketdesing.domain.designer.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import pe.puyu.jticketdesing.domain.components.block.SweetQrCorrectionLevel;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class SweetQrHelper {

    public static BufferedImage generateQr(SweetQrInfo info, int size) throws Exception {
        final String charset = "UTF-8";
        Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, toErrorCorrectionLevel(info.correctionLevel()));
        BitMatrix matrix = new MultiFormatWriter().encode(new String(info.data().getBytes(charset), charset),
            BarcodeFormat.QR_CODE, size, size, hintMap);
        return MatrixToImageWriter.toBufferedImage(matrix);
    }

    private static ErrorCorrectionLevel toErrorCorrectionLevel(SweetQrCorrectionLevel correctionLevel) {
        return switch (correctionLevel) {
            case L -> ErrorCorrectionLevel.L;
            case M -> ErrorCorrectionLevel.M;
            case Q -> ErrorCorrectionLevel.Q;
            case H -> ErrorCorrectionLevel.H;
        };
    }
}
