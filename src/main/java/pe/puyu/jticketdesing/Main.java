package pe.puyu.jticketdesing;

import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.PrintModeStyle;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.output.PrinterOutputStream;
import com.github.anastaciocintra.output.TcpIpOutputStream;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import pe.puyu.jticketdesing.domain.SweetTicketDesign;
import pe.puyu.jticketdesing.domain.designer.SweetDesignHelper;
import pe.puyu.jticketdesing.domain.table.SweetTableDesign;

import javax.print.PrintService;

public class Main {
	public static void main(String[] args) throws InterruptedException {
	}

	private static void testLineSpacing(OutputStream outputStream) throws IOException {
		EscPos escpos = new EscPos(outputStream);
		PrintModeStyle printModeStyle = new PrintModeStyle();
		printModeStyle.setFontSize(true, true);
		Style style = new Style();
		style.setUnderline(Style.Underline.OneDotThick);
		style.setBold(true);
		style.setLineSpacing(1);
		escpos.writeLF(style, "hola");
		escpos.writeLF(style,"camaron con cola");
	}

	private static void testPrint(OutputStream outputStream) throws Exception {
		String pathToFile = "/home/socamaru/Documentos/projects/testPrintJson/print.json";
		FileReader reader = new FileReader(pathToFile);
		JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
		SweetTicketDesign printDesign = new SweetTicketDesign(jsonObject);
		outputStream.write(printDesign.getBytes());
	}

	private static void testTableDesign(OutputStream outputStream) throws IOException {
		String pathToFile = "/home/socamaru/Documentos/projects/testPrintJson/tables.json";
		FileReader reader = new FileReader(pathToFile);
		JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
		SweetTableDesign tableDesign = new SweetTableDesign(jsonObject);
		outputStream.write(tableDesign.getBytes());
	}

	private static TcpIpOutputStream ip(String ip) throws IOException {
		return new TcpIpOutputStream(ip);
	}

	private static OutputStream bixonSrpE300() throws IOException {
		return printerOutput("BIXOLON_SRP-E300");
	}

	private static OutputStream printerOutput(String serviceName) throws IOException {
		PrintService printService = PrinterOutputStream.getPrintServiceByName(serviceName);
		return new PrinterOutputStream(printService);
	}

}
