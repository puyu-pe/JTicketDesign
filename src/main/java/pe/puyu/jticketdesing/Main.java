package pe.puyu.jticketdesing;

import java.io.*;

import com.github.anastaciocintra.output.PrinterOutputStream;
import com.github.anastaciocintra.output.TcpIpOutputStream;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import pe.puyu.jticketdesing.application.components.DefaultComponentsProvider;
import pe.puyu.jticketdesing.application.builder.gson.GsonDesignObjectBuilder;
import pe.puyu.jticketdesing.application.printer.escpos.EscPosPrinter;
import pe.puyu.jticketdesing.domain.SweetTicketDesign;
import pe.puyu.jticketdesing.domain.designer.SweetDesigner;
import pe.puyu.jticketdesing.domain.printer.SweetPrinter;
import pe.puyu.jticketdesing.domain.table.SweetTableDesign;

import javax.print.PrintService;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        try (OutputStream outputStream = ip("192.168.1.38")) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            testSweetDesigner(byteArrayOutputStream);
            outputStream.write(byteArrayOutputStream.toByteArray());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private static void testSweetDesigner(OutputStream outputStream) throws FileNotFoundException {
        String pathToFile = "/home/socamaru/Documentos/projects/testPrintJson/designer.json";
        FileReader reader = new FileReader(pathToFile);
        JsonObject jsonObject = JsonParser.parseReader(reader).getAsJsonObject();
        GsonDesignObjectBuilder builder = new GsonDesignObjectBuilder(jsonObject);
        SweetPrinter printer = new EscPosPrinter(outputStream);
        SweetDesigner designer = new SweetDesigner(builder, printer, new DefaultComponentsProvider());
        designer.paintDesign();
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
