package pe.puyu.jticketdesing.domain.printer;

public interface SweetDesignPrinter {
    void printText(String text, SweetDesignStyle style);
    void printImg(String localImgPath);
    void printQrAsImage(String stringQr);
    void printNativeQr(String stringQr);
}
