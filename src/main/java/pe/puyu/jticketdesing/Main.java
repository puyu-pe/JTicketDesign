package pe.puyu.jticketdesing;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

import com.github.anastaciocintra.output.PrinterOutputStream;
import com.github.anastaciocintra.output.TcpIpOutputStream;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import pe.puyu.jticketdesing.core.table.SweetTableDesign;
import pe.puyu.jticketdesing.metadata.PrinterPropertiesReader;

import javax.print.PrintService;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		testTableDesign();
	}

	private static void testTableDesign() {
		PrintService printService = PrinterOutputStream.getPrintServiceByName("BIXOLON_SRP-E300");
		try (PrinterOutputStream outputStream = new PrinterOutputStream(printService)) {
			String pathToFile = "/home/socamaru/Documentos/projects/puka-http/src/main/resources/pe/puyu/pukahttp/testPrinter/report.json";
			FileReader reader = new FileReader(pathToFile);
			JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
			SweetTableDesign tableDesign = new SweetTableDesign(jsonObject);
			outputStream.write(tableDesign.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
