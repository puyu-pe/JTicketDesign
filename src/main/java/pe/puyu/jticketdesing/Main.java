package pe.puyu.jticketdesing;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.module.InvalidModuleDescriptorException;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.PrintModeStyle;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.output.PrinterOutputStream;
import com.github.anastaciocintra.output.TcpIpOutputStream;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import pe.puyu.jticketdesing.core.table.SweetTableDesign;
import pe.puyu.jticketdesing.metadata.PrinterPropertiesReader;
import pe.puyu.jticketdesing.util.escpos.EscPosWrapper;

import javax.print.PrintService;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		try (OutputStream outputStream = bixonSrpE300()) {
			testTableDesign(outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	private static void testTableDesign(OutputStream outputStream) throws IOException {
		String pathToFile = "/home/socamaru/Documentos/projects/puka-http/src/main/resources/pe/puyu/pukahttp/testPrinter/report.json";
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
