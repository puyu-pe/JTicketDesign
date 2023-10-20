package pe.puyu.jticketdesing.util;

import java.util.HashMap;
import java.util.Map;

import java.awt.image.BufferedImage;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRCodeGenerator {

	public static BufferedImage render(String qrCodeData, int sizeQR) throws Exception {
		final String charset = "UTF-8";
		final int qrCodeheight = sizeQR;
		final int qrCodewidth = sizeQR;
		Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);
		BitMatrix matrix = new MultiFormatWriter().encode(new String(qrCodeData.getBytes(charset), charset),
				BarcodeFormat.QR_CODE, qrCodewidth, qrCodeheight, hintMap);
		BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);
		return image;
	}
}
