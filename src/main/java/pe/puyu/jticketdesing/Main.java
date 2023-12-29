package pe.puyu.jticketdesing;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPos.CharacterCodeTable;
import com.github.anastaciocintra.escpos.EscPos.CutMode;
import com.github.anastaciocintra.escpos.Style.FontSize;
import com.github.anastaciocintra.output.TcpIpOutputStream;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		try (OutputStream outputStream = new TcpIpOutputStream("192.168.18.39",9100)) {
			var buffer = new ByteArrayOutputStream();
			EscPos escPos = new EscPos(buffer);
			escPos.setCharacterCodeTable(CharacterCodeTable.WPC1252);
			escPos.getStyle().setBold(true).setFontSize(FontSize._2,FontSize._1);
			escPos.writeLF("N° áéíóúÑ");
			escPos.feed(4);
			escPos.cut(CutMode.PART);
			outputStream.write(buffer.toByteArray());
			outputStream.close();
			escPos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
