package pe.puyu.jticketdesing.domain.printer;

public interface DesignPainter {
    void printText(String text, PainterStyle style);
    void printImg(String localImgPath);
    void printQR(String stringQr);
}
