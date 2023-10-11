package pe.puyu.jticketdesing.core;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Path;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPos.CharacterCodeTable;
import com.github.anastaciocintra.escpos.EscPos.CutMode;
import com.github.anastaciocintra.escpos.EscPos.PinConnector;
import com.github.anastaciocintra.escpos.EscPosConst.Justification;
import com.github.anastaciocintra.escpos.Style.ColorMode;
import com.github.anastaciocintra.escpos.Style.FontSize;
import com.github.anastaciocintra.escpos.barcode.QRCode;
import com.github.anastaciocintra.escpos.barcode.QRCode.QRErrorCorrectionLevel;
import com.github.anastaciocintra.escpos.barcode.QRCode.QRModel;
import com.github.anastaciocintra.escpos.image.BitonalOrderedDither;
import com.github.anastaciocintra.escpos.image.BitonalThreshold;
import com.github.anastaciocintra.escpos.image.CoffeeImageImpl;
import com.github.anastaciocintra.escpos.image.EscPosImage;
import com.github.anastaciocintra.escpos.image.RasterBitImageWrapper;

import pe.puyu.jticketdesing.util.QRCodeGenerator;
import pe.puyu.jticketdesing.util.StringUtils;

class Default {
  public final static int TIMES = 1;
  public final static int MAX_TICKET_WIDTH = 42;
  public final static int QUANTITY_COLUMN_WIDTH = 4;
  public final static int MARGIN_RIGHT = 0;
  public final static int TOTAL_COLUMN_WIDTH = 7 + MARGIN_RIGHT;
}

public class SweetTicketDesing {

  private JSONObject ticket;
  private JSONObject metadata; // typeTicket, times, maxTicketWidth, logoPath
  private EscPos escpos;
  private String typeTicket;
  private int times;
  private int maxTicketWidth;

  public SweetTicketDesing(JSONObject ticket, JSONObject metadata) throws Exception {
    this.ticket = ticket;
    this.metadata = metadata;
    this.typeTicket = this.metadata.getString("typeTicket");
    this.times = metadata.has("times") ? metadata.getInt("times") : Default.TIMES;
    this.maxTicketWidth = metadata.has("maxTicketWidth") ? metadata.getInt("maxTicketWidth") : Default.MAX_TICKET_WIDTH;
  }

  private void initEscPos(ByteArrayOutputStream buffer) throws Exception {
    this.escpos = new EscPos(buffer);
    if (metadata.has("charCodeTable") && !metadata.isNull("charCodeTable")) {
      this.escpos.setCharacterCodeTable(CharacterCodeTable.valueOf(metadata.getString("charCodeTable")));
    } else {
      this.escpos.setCharacterCodeTable(CharacterCodeTable.WPC1252);
    }
    if (metadata.has("charSetName") && !metadata.isNull("charSetName")) {
      try {
        this.escpos.setCharsetName(metadata.getString("charSetName"));
      } catch (Exception e) {
      }
    }
  }

  public byte[] getBytes() throws Exception {
    try {
      var byteArrayOutputStream = new ByteArrayOutputStream();
      initEscPos(byteArrayOutputStream);
      for (int i = 0; i < times; ++i) {
        desingLayout();
      }
      return byteArrayOutputStream.toByteArray();
    } catch (Exception e) {
      throw new Exception(String.format("Error al diseñar el ticket: %s", e.getMessage()));
    } finally {
      this.escpos.close();
    }
  }

  private void desingLayout() throws Exception {
    this.header();
    switch (this.typeTicket) {
      case "invoice":
        this.businessAdditional();
        this.documentLegal();
        this.customer();
        this.additional();
        this.items();
        this.amounts();
        this.additionalFooter();
        this.finalMessage();
        this.stringQR();
        this.escpos.pulsePin(PinConnector.Pin_2, 120, 240);
        break;
      case "note":
        this.documentLegal();
        this.customer();
        this.additional();
        break;
      case "command":
        this.productionArea();
        this.textBackgroudInverted();
        this.documentLegal();
        this.additional();
        this.items();
        break;
      case "precount":
        this.documentLegal();
        this.additional();
        this.items();
        this.amounts();
        break;
      case "extra":
        this.titleExtra();
        this.additional();
        this.items();
        this.amounts();
        break;
      default:
        throw new Exception(String.format("No se pudo imprimir el diseño tipo de documento %s", this.typeTicket));
    }
    this.escpos.feed(4);
    this.escpos.cut(CutMode.PART);
  }

  private void header() throws Exception {
    JSONObject business = this.ticket.getJSONObject("business");
    if (business.has("comercialDescription")) {
      var comercialDescription = business.getJSONObject("comercialDescription");
      String type = comercialDescription.getString("type");
      if (type.equalsIgnoreCase("text")) {
        this.escpos.getStyle()
            .setBold(true)
            .setFontSize(FontSize._2, FontSize._2)
            .setJustification(Justification.Center);
        this.escpos.writeLF(comercialDescription.getString("value"));
        this.escpos.getStyle().reset();
      }
      if (type.equalsIgnoreCase("img") && this.metadata.has("logoPath")) {
        RasterBitImageWrapper imageWrapper = new RasterBitImageWrapper();
        imageWrapper.setJustification(Justification.Center);
        BufferedImage image = ImageIO.read(new File(this.metadata.getString("logoPath")));
        int size = 290;
        Image scaledImage = image.getScaledInstance(size, size, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(size, size, 2);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(scaledImage, 0, 0, null);
        g.dispose();
        EscPosImage escPosImage = new EscPosImage(new CoffeeImageImpl(resizedImage), new BitonalThreshold());
        escpos.write(imageWrapper, escPosImage);
      }
    }
    if (business.has("description")) {
      this.escpos.getStyle().setJustification(Justification.Center);
      this.escpos.writeLF(business.getString("description")).getStyle().reset();
    }
  }

  private void businessAdditional() throws Exception {
    JSONObject business = this.ticket.getJSONObject("business");
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
    switch (this.typeTicket) {
      case "invoice":
      case "note":
      case "command":
        if (this.ticket.has("document")) {
          var document = this.ticket.getJSONObject("document");
          this.escpos.feed(1);
          this.escpos
              .writeLF(
                  String.format("%s %s", document.getString("description"), document.get("identifier").toString()));
        } else {
          var document = this.ticket.getString("document");
          this.escpos.writeLF(String.format("%s %s", document, ticket.getString("documentId")));
        }
        break;
      case "precount":
        var document = this.ticket.getJSONObject("document");
        this.escpos.getStyle().setFontSize(FontSize._2, FontSize._2);
        this.escpos.writeLF(document.getString("description"));
        break;
    }
    this.escpos.feed(1);
    this.escpos.getStyle().reset();
  }

  private void customer() throws Exception {
    if (this.ticket.has("customer")) {
      JSONArray customer = this.ticket.getJSONArray("customer");
      for (var item : customer) {
        this.escpos.writeLF(item.toString());
      }
    } else {
      this.escpos.writeLF(StringUtils.repeat('-', this.maxTicketWidth));
    }
  }

  private void additional() throws Exception {
    if (this.ticket.has("additional")) {
      JSONArray additional = this.ticket.getJSONArray("additional");
      for (var item : additional) {
        this.escpos.writeLF(item.toString());
      }
    }
  }

  private void items() throws Exception {
    if (!this.ticket.has("items"))
      return;
    this.escpos.getStyle().setBold(true);
    var items = this.ticket.getJSONArray("items");
    int descriptionColumnWidth = this.maxTicketWidth - Default.TOTAL_COLUMN_WIDTH;
    var priceFormat = new DecimalFormat("0.00");

    if (items.getJSONObject(0).has("quantity")) {
      descriptionColumnWidth -= Default.QUANTITY_COLUMN_WIDTH;
      this.escpos.write(StringUtils.padRight("CAN", Default.QUANTITY_COLUMN_WIDTH, ' '));
    }
    this.escpos.write(StringUtils.padRight("DESCRIPCIÓN", descriptionColumnWidth, ' '));
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
          if (j == 0 && item.has("totalPrice")) {
            this.escpos.write(StringUtils.padLeft(priceFormat.format(item.getBigDecimal("totalPrice")),
                Default.TOTAL_COLUMN_WIDTH, ' ',
                Default.MARGIN_RIGHT));
          }
          this.escpos.feed(1);
        }
      } else {
        var lines = StringUtils.split(descriptionObj.toString(), descriptionColumnWidth);
        for (int j = 0; j < lines.size(); ++j) {
          if (j == 0) {
            this.escpos
                .write(StringUtils.padLeft(item.get("quantity").toString(), Default.QUANTITY_COLUMN_WIDTH, ' ', 1));
          } else {
            this.escpos.write(StringUtils.padRight("", Default.QUANTITY_COLUMN_WIDTH, ' '));
          }
          this.escpos.write(StringUtils.padRight(lines.get(j), descriptionColumnWidth, ' '));
          if (j == 0 && item.has("totalPrice")) {
            this.escpos.write(StringUtils.padLeft(priceFormat.format(item.getBigDecimal("totalPrice")),
                Default.TOTAL_COLUMN_WIDTH, ' ',
                Default.MARGIN_RIGHT));
          }
          this.escpos.feed(1);
        }
      }
    }
    this.escpos.writeLF(StringUtils.repeat('-', this.maxTicketWidth)).getStyle().reset();
  }

  private void amounts() throws Exception {
    if (!this.ticket.has("amounts")) {
      this.escpos.writeLF(StringUtils.repeat(' ', this.maxTicketWidth));
      return;
    }
    var amounts = this.ticket.getJSONObject("amounts").toMap();
    for (var amount : amounts.entrySet()) {
      this.escpos.writeLF(
          StringUtils.padLeft(String.format("%s: %s", amount.getKey(), amount.getValue().toString()),
              this.maxTicketWidth, ' ', Default.MARGIN_RIGHT));
    }
    this.escpos.writeLF(StringUtils.repeat('-', this.maxTicketWidth));
  }

  private void additionalFooter() throws Exception {
    if (!this.ticket.has("additionalFooter")) {
      this.escpos.writeLF(StringUtils.repeat(' ', this.maxTicketWidth));
      return;
    }
    var additionalFooter = this.ticket.getJSONArray("additionalFooter");
    for (Object item : additionalFooter) {
      this.escpos.writeLF(StringUtils.padRight(item.toString(), this.maxTicketWidth, ' '));
    }
    this.escpos.writeLF(StringUtils.repeat('-', this.maxTicketWidth));
  }

  private void finalMessage() throws Exception {
    if (!this.ticket.has("finalMessage")) {
      this.escpos.writeLF(StringUtils.repeat(' ', this.maxTicketWidth));
      return;
    }
    this.escpos.getStyle().setJustification(Justification.Center);
    Object finalMessageObj = this.ticket.get("finalMessage");
    if (finalMessageObj instanceof JSONArray) {
      var finalMessage = (JSONArray) finalMessageObj;
      for (var item : finalMessage) {
        this.escpos.writeLF(item.toString());
      }
    } else {
      this.escpos.writeLF(finalMessageObj.toString());
    }
    this.escpos.getStyle().reset();
  }

  private void stringQR() throws Exception {
    if (!this.ticket.has("stringQR") || this.ticket.isNull("stringQR")) {
      this.escpos.writeLF(StringUtils.repeat('-', this.maxTicketWidth));
      return;
    }
    boolean nativeQR = false;
    var stringQr = this.ticket.get("stringQR").toString();
    if (metadata.has("nativeQR") && !metadata.isNull("nativeQR")) {
      nativeQR = metadata.getBoolean("nativeQR");
    }
    if (!nativeQR) {
      String tmpPath = Path.of(System.getProperty("java.io.tmpdir"), "qr.png").toString();
      QRCodeGenerator.render(stringQr, tmpPath);
      BufferedImage image = ImageIO.read(new File(tmpPath));
      EscPosImage escposImage = new EscPosImage(new CoffeeImageImpl(image), new BitonalOrderedDither());
      RasterBitImageWrapper imageWrapper = new RasterBitImageWrapper();
      imageWrapper.setJustification(Justification.Center);
      this.escpos.write(imageWrapper, escposImage);
    } else {
      var qrCode = new QRCode();
      qrCode
          .setSize(10)
          .setJustification(Justification.Center)
          .setModel(QRModel._2)
          .setErrorCorrectionLevel(QRErrorCorrectionLevel.QR_ECLEVEL_Q);
      this.escpos.write(qrCode, stringQr);
    }
  }

  private void productionArea() throws Exception {
    if (!this.ticket.has("productionArea")) {
      this.escpos.writeLF(StringUtils.repeat('-', this.maxTicketWidth));
      return;
    }
    this.escpos.getStyle().setJustification(Justification.Center);
    this.escpos.writeLF(this.ticket.getString("productionArea"));
    this.escpos.getStyle().reset();
  }

  private void titleExtra() throws Exception {
    if (!this.ticket.has("titleExtra")) {
      this.escpos.writeLF(StringUtils.repeat('-', this.maxTicketWidth));
      return;
    }
    var titleExtra = this.ticket.getJSONObject("titleExtra");
    this.escpos.getStyle()
        .setJustification(Justification.Center)
        .setBold(true)
        .setFontSize(FontSize._2, FontSize._2);
    this.escpos.writeLF(titleExtra.getString("title"));
    this.escpos.getStyle().setBold(false);
    this.escpos.writeLF(titleExtra.getString("subtitle"));
    this.escpos.getStyle().reset();
  }

  private void textBackgroudInverted() throws Exception {

    if (!this.ticket.has("textBackgroundInverted") || this.ticket.isNull("textBackgroundInverted")) {
      this.escpos.writeLF(StringUtils.repeat('-', this.maxTicketWidth));
      return;
    }
    char pad = '*';
    boolean supportBackgroundInverted = true;
    if (metadata.has("backgroundInverted") && !metadata.isNull("backgroundInverted")) {
      supportBackgroundInverted = metadata.getBoolean("backgroundInverted");
    }
    if (supportBackgroundInverted) {
      pad = ' ';
      this.escpos.getStyle().setColorMode(ColorMode.WhiteOnBlack);
    }
    var textBackgroundInverted = String.format(" %s ", this.ticket.getString("textBackgroundInverted"));
    this.escpos.getStyle().setJustification(Justification.Center).setBold(true);
    this.escpos.writeLF(StringUtils.padBoth(textBackgroundInverted, maxTicketWidth, pad));
    this.escpos.getStyle().reset();
  }
}
