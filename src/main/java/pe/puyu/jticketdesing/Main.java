package pe.puyu.jticketdesing;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.output.TcpIpOutputStream;

public class Main {
  public static void main(String[] args) {
    try (TcpIpOutputStream outputStream = new TcpIpOutputStream("192.168.1.100",
        9100)) {
      EscPos escpos = new EscPos(outputStream);
      escpos.writeLF("Hello world");
      escpos.feed(5).cut(EscPos.CutMode.FULL);
      escpos.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
