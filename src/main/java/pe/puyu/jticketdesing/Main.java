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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import pe.puyu.jticketdesing.core.SweetTableDesign;

public class Main {
	public static void main(String[] args) throws InterruptedException {
//		testTableDesign();
	}


	private static void testTableDesign() {
		try (OutputStream outputStream = new TcpIpOutputStream("192.168.18.39", 9100)) {

			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("title", "GASTOS VARIOS SUPER SUPER SUPER SUPER SUPER SUPER");
			jsonObject.addProperty("details", "detalles varios varios detalles varios detalles varios detalles varios");

			JsonObject printer = new JsonObject();
			JsonObject printerProperties = new JsonObject();
			printerProperties.addProperty("width", 42);
			printer.add("properties", printerProperties);
			jsonObject.add("printer", printer);

			JsonObject table1 = new JsonObject();
			JsonObject titleTable = new JsonObject();
			titleTable.addProperty("text", "tabla 1");
			titleTable.addProperty("align", "left");
			titleTable.addProperty("fontSize", 1);
			JsonArray headersTable = new JsonArray();
			JsonArray bodyTable = new JsonArray();
			JsonArray footersTable = new JsonArray();
			table1.add("title", titleTable);

			JsonArray tables = new JsonArray();
			tables.add(table1);
			jsonObject.add("tables", tables);

			SweetTableDesign tableDesign = new SweetTableDesign(jsonObject);
			outputStream.write(tableDesign.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
