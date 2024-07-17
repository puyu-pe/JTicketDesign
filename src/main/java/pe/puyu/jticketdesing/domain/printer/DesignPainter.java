package pe.puyu.jticketdesing.domain.printer;

public interface DesignPainter {
    byte[] printText(String text, PainterStyle style);
    byte[] printImg(String localImgPath);
    byte[] printQR(String stringQr);
}
