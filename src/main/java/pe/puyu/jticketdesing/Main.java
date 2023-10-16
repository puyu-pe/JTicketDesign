package pe.puyu.jticketdesing;

import java.util.LinkedList;
import java.util.List;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.Style.FontSize;
import com.github.anastaciocintra.output.TcpIpOutputStream;

import pe.puyu.jticketdesing.util.StringUtils;
import pe.puyu.jticketdesing.util.escpos.EscPosWrapper;

public class Main {
  public static void main(String[] args) throws InterruptedException {
    System.out.println(StringUtils.wrapText("FETUCCINI AL ALFREDO fjsalfjsdlfjslkdf sfjsdlfjssdf fasdfsdfasdwee", 31, 1));
    // try (var escpos = new EscPos(new TcpIpOutputStream("192.168.18.100", 9100)))
    // {
    // var escposUtil = new EscPosWrapper(escpos);
    //
    // escposUtil.toRight("1", 7, FontSize._2, false);
    // escposUtil.toCenter("ARROZ CON LANGOSTINOS", 21, FontSize._2, false);
    // escposUtil.toLeft("17.00", 14, FontSize._2, true);
    // escpos.getStyle().reset();
    //
    // escpos.feed(4);
    // escpos.cut(EscPos.CutMode.PART);
    // escpos.close();
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
  }
}
