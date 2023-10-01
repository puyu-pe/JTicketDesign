package pe.puyu.jticketdesing;

import java.io.ByteArrayOutputStream;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPos.CharacterCodeTable;
import com.github.anastaciocintra.escpos.EscPosConst.Justification;
import com.github.anastaciocintra.escpos.Style.FontSize;
import com.github.anastaciocintra.output.TcpIpOutputStream;

public class Main {
  public static void main(String[] args) {
    try (TcpIpOutputStream outputStream = new TcpIpOutputStream("192.168.18.100",
        9100)) {
      var baos = new ByteArrayOutputStream();
      EscPos escpos = new EscPos(baos);
      escpos.setCharacterCodeTable(CharacterCodeTable.WPC1252);
      escpos.getStyle().setBold(true).setJustification(Justification.Center).setFontSize(FontSize._2, FontSize._2);
      escpos.writeLF("oscar alcides");
      escpos.feed(5).cut(EscPos.CutMode.FULL);
      escpos.close();
      outputStream.write(baos.toByteArray());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
