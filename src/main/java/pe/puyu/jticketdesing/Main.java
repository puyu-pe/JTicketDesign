package pe.puyu.jticketdesing;

import pe.puyu.jticketdesing.util.StringUtils;

public class Main {
  public static void main(String[] args) throws InterruptedException {
    System.out.println(StringUtils.padBoth("oscar",11,'*'));
    // String[] printServicesNames = PrinterOutputStream.getListPrintServicesNames();
    // for (String printServiceName : printServicesNames) {
    //   System.out.println(printServiceName);
    // }
    // PrintService printService = PrinterOutputStream.getPrintServiceByName("XPRINTER");
    // try (var escpos = new EscPos(new PrinterOutputStream(printService))) {
    //   escpos.getStyle().setBold(true).setJustification(Justification.Center);
    //   escpos.writeLF(StringUtils.padBoth("PADBOTH",48,' '));
    //   escpos.feed(4);
    //   escpos.cut(CutMode.PART);
    //   escpos.close();
    // } catch (IOException e) {
    //   e.printStackTrace();
    // }
  }
}
