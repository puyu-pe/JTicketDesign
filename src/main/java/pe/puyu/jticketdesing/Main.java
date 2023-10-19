package pe.puyu.jticketdesing;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPos.CharacterCodeTable;
import com.github.anastaciocintra.escpos.EscPos.CutMode;
import com.github.anastaciocintra.escpos.Style.FontSize;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		File smbFile = new File("\\\\192.168.1.53\\jpuka");
		try (OutputStream outputStream = new FileOutputStream(smbFile)) {
			var buffer = new ByteArrayOutputStream();
			EscPos escPos = new EscPos(buffer);
			escPos.setCharacterCodeTable(CharacterCodeTable.WPC1252);
			escPos.setCharsetName(CharacterCodeTable.WPC1252.charsetName);
			escPos.getStyle().setBold(true).setFontSize(FontSize._2,FontSize._1);
			escPos.writeLF("N° áéíóúñ");
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
