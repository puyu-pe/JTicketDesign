package pe.puyu.jticketdesing.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPos.CutMode;
import com.github.anastaciocintra.escpos.EscPosConst.Justification;
import com.github.anastaciocintra.escpos.Style.FontSize;
import com.github.anastaciocintra.escpos.image.BitonalThreshold;
import com.github.anastaciocintra.escpos.image.CoffeeImageImpl;
import com.github.anastaciocintra.escpos.image.EscPosImage;
import com.github.anastaciocintra.escpos.image.RasterBitImageWrapper;
import com.github.anastaciocintra.output.TcpIpOutputStream;

import pe.puyu.jticketdesing.util.StringUtils;

class Default {
  public final static int TIMES = 1;
  public final static int MAX_TICKET_WIDTH = 42;
  public final static int QUANTITY_COLUMN_WIDTH = 4;
  public final static int MARGIN_RIGHT = 2;
  public final static int TOTAL_COLUMN_WIDTH = 7 + MARGIN_RIGHT;
}

public class SweetTicketPrinter {

  private JSONObject printerInfo;
  private String typeDocument;
  private JSONObject bodyTicket;
  private int times;
  private int maxTicketWidth;
  private EscPos escpos;

  public SweetTicketPrinter(JSONObject data) throws Exception {
    // WARNING: Falta testear EncodingLatin2 para caracteres con diacritics
    printerInfo = data.getJSONObject("printer");
    typeDocument = data.getString("type");
    bodyTicket = data.getJSONObject("data");
    times = data.has("times") ? data.getInt("times") : Default.TIMES;
    maxTicketWidth = printerInfo.has("width") ? printerInfo.getInt("width") : Default.MAX_TICKET_WIDTH;
  }

  private void initEscPos() throws Exception {
    this.escpos = new EscPos(getOutputStreamByPrinterType());
  }

  private OutputStream getOutputStreamByPrinterType() throws Exception {
    var printerInfo = this.printerInfo;
    switch (printerInfo.getString("type")) {
      case "ethernet":
        return new TcpIpOutputStream(printerInfo.getString("name_system"), printerInfo.getInt("port"));
      default:
        throw new Exception(String.format("Tipo de impresora: %s no soportado", printerInfo.getString("type")));
    }
  }

  public void printTicket() throws Exception {
    try {
      initEscPos();
      for (int i = 0; i < times; ++i) {
        printLayout();
      }
      this.escpos.close();
    } catch (Exception e) {
      String name_system = this.printerInfo.getString("name_system");
      int port = this.printerInfo.getInt("port");
      String type = this.printerInfo.getString("type");
      var error = String.format("Error al imprimir ticket en name_system: %s, port: %d , type: %s. Mensaje error: %s",
          name_system,
          port, type, e.getMessage());
      throw new Exception(error);
    }
  }

  private void printLayout() throws Exception {
    this.header();
    switch (this.typeDocument) {
      case "invoice":
        this.businessAdditional();
        this.documentLegal();
        break;
      case "note":
        break;
      case "command":
        break;
      case "precount":
        break;
      case "extra":
        break;
      default:
        throw new Exception(String.format("No se pudo imprimir el diseño tipo de documento %s", this.typeDocument));
    }
    this.escpos.feed(4);
    this.escpos.cut(CutMode.PART);
  }

  private void header() throws Exception {
    JSONObject business = this.bodyTicket.getJSONObject("business");
    if (business.has("comercialDescription")) {
      var comercialDescription = business.getJSONObject("comercialDescription");
      String type = comercialDescription.getString("type");
      if (type == "text") {
        this.escpos.getStyle()
            .setBold(true)
            .setFontSize(FontSize._2, FontSize._2)
            .setJustification(Justification.Center);
        this.escpos.writeLF(comercialDescription.getString("value"));
        this.escpos.getStyle().reset();
      }
      if (type == "img") {
        RasterBitImageWrapper imageWrapper = new RasterBitImageWrapper();
        imageWrapper.setJustification(Justification.Center);
        // FIX: obtener la url absoluta de la imagen
        BufferedImage image = ImageIO.read(new File(""));
        EscPosImage escPosImage = new EscPosImage(new CoffeeImageImpl(image), new BitonalThreshold());
        escpos.write(imageWrapper, escPosImage);
      }
    }
    if (business.has("description")) {
      this.escpos.getStyle().setJustification(Justification.Center);
      this.escpos.writeLF(business.getString("description")).getStyle().reset();
    }
  }

  private void businessAdditional() throws Exception {
    JSONObject business = this.bodyTicket.getJSONObject("business");
    if (!business.has("additional")) {
      this.escpos.feed(1);
      return;
    }
    JSONArray additional = business.getJSONArray("additional");
    this.escpos.getStyle().setJustification(Justification.Center);
    for (Object item : additional) {
      this.escpos.writeLF(item.toString());
    }
    this.escpos.feed(1).getStyle().reset();
  }

  private void documentLegal() throws Exception {
    this.escpos.getStyle().setJustification(Justification.Center).setBold(true);
    switch (this.typeDocument) {
      case "invoice":
      case "note":
      case "command":
        if (this.bodyTicket.has("document")) {
          var document = this.bodyTicket.getJSONObject("document");
          this.escpos
              .writeLF(String.format("%s %s", document.getString("description"), document.getString("identifier")));
        } else {
          var document = this.bodyTicket.getString("document");
          this.escpos.writeLF(String.format("%s %s", document, bodyTicket.getString("documentId")));
        }
        break;
      case "precount":
        var document = this.bodyTicket.getJSONObject("document");
        this.escpos.getStyle().setFontSize(FontSize._2, FontSize._2);
        this.escpos.writeLF(document.getString("description"));
        break;
    }
    this.escpos.feed(1);
    this.escpos.getStyle().reset();
  }

  private void customer() throws Exception {
    if (this.bodyTicket.has("customer")) {
      JSONArray customer = this.bodyTicket.getJSONArray("customer");
      for (var item : customer) {
        this.escpos.writeLF(item.toString());
      }
    } else {
      this.escpos.writeLF(StringUtils.repeat('-', this.maxTicketWidth));
    }
  }

  private void additional() throws Exception {
    if (this.bodyTicket.has("additional")) {
      JSONArray additional = this.bodyTicket.getJSONArray("additional");
      for (var item : additional) {
        this.escpos.writeLF(item.toString());
      }
    }
  }

  private void items() throws Exception {
    if (!this.bodyTicket.has("items"))
      return;
    this.escpos.getStyle().setBold(true);
    var items = this.bodyTicket.getJSONArray("items");
    int descriptionColumnWidth = this.maxTicketWidth - Default.TOTAL_COLUMN_WIDTH;

    if (items.getJSONObject(0).has("quantity")) {
      descriptionColumnWidth -= Default.QUANTITY_COLUMN_WIDTH;
      this.escpos.write(StringUtils.padRight("CAN", Default.QUANTITY_COLUMN_WIDTH, ' '));
    }
    this.escpos.write(StringUtils.padRight("DESCRIPTION", descriptionColumnWidth, ' '));
    this.escpos.write(StringUtils.padLeft("TOTAL", Default.TOTAL_COLUMN_WIDTH, ' ', Default.MARGIN_RIGHT));
    this.escpos.feed(1);
    this.escpos.writeLF(StringUtils.repeat('-', this.maxTicketWidth));
    this.escpos.getStyle().setBold(false);

    for (int i = 0; i < items.length(); ++i) {
      var item = items.getJSONObject(i);
      Object descriptionObj = item.get("description");
      if (descriptionObj instanceof JSONArray) {
        var description = (JSONArray) descriptionObj;
        for (int j = 0; j < description.length(); ++j) {
          var itemDescription = description.getString(j);
          this.escpos.write(StringUtils.padRight(itemDescription, descriptionColumnWidth, ' '));
          if (j == 0) {
            this.escpos.write(StringUtils.padLeft(item.getString("totalPrice"), Default.TOTAL_COLUMN_WIDTH, ' ',
                Default.MARGIN_RIGHT));
          }
          this.escpos.feed(1);
        }
      } else {
        var lines = StringUtils.split(descriptionObj.toString(), descriptionColumnWidth);
        for (int j = 0; j < lines.size(); ++j) {
          if (j == 0) {
            this.escpos
                .write(StringUtils.padLeft(item.getString("quantity") + " ", Default.QUANTITY_COLUMN_WIDTH, ' ',
                    Default.MARGIN_RIGHT));
          } else {
            this.escpos.write(StringUtils.padRight("", Default.QUANTITY_COLUMN_WIDTH, ' '));
          }
          this.escpos.write(StringUtils.padRight(lines.get(j), descriptionColumnWidth, ' '));
          if (j == 0) {
            this.escpos.write(StringUtils.padLeft(item.getString("totalPrice"), Default.TOTAL_COLUMN_WIDTH, ' ',
                Default.MARGIN_RIGHT));
          }
          this.escpos.feed(1);
        }
      }
    }
    this.escpos.writeLF(StringUtils.repeat('-', this.maxTicketWidth)).getStyle().reset();
  }

  private void amounts() throws Exception {
    if (!this.bodyTicket.has("amounts")) {
      this.escpos.writeLF(StringUtils.repeat(' ', this.maxTicketWidth));
      return;
    }
    var amounts = this.bodyTicket.getJSONObject("amounts").toMap();
    for (var amount : amounts.entrySet()) {
      this.escpos.writeLF(
          StringUtils.padLeft(String.format("%s: %s", amount.getKey(), amount.getValue().toString()),
              this.maxTicketWidth, ' ', Default.MARGIN_RIGHT));
    }
    this.escpos.writeLF(StringUtils.repeat('-', this.maxTicketWidth));
  }

  private void additionalFooter(){
    //NOTE: aqui me quede
  }
}
