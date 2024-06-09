package pe.puyu.jticketdesing;

import java.io.FileReader;
import java.io.OutputStream;

import com.github.anastaciocintra.output.TcpIpOutputStream;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import pe.puyu.jticketdesing.core.table.SweetTableDesign;

public class Main {
	public static void main(String[] args) throws InterruptedException {
		testTableDesign();
	}


	private static void testTableDesign() {
		try (OutputStream outputStream = new TcpIpOutputStream("192.168.18.39", 9100)) {
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
