package pe.puyu.jticketdesing;

import java.io.IOException;

import javax.print.PrintService;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPos.CutMode;
import com.github.anastaciocintra.escpos.EscPosConst.Justification;
import com.github.anastaciocintra.output.PrinterOutputStream;

public class Main {
  public static void main(String[] args) throws InterruptedException {
    String[] printServicesNames = PrinterOutputStream.getListPrintServicesNames();
    for (String printServiceName : printServicesNames) {
      System.out.println(printServiceName);
    }
    PrintService printService = PrinterOutputStream.getPrintServiceByName("XPRINTER");
    try (var escpos = new EscPos(new PrinterOutputStream(printService))) {
      escpos.getStyle().setBold(true).setJustification(Justification.Center);
      escpos.writeLF("DESCRIPTIÓN Ñ PERÚ");
      escpos.feed(4);
      escpos.cut(CutMode.PART);
      escpos.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
