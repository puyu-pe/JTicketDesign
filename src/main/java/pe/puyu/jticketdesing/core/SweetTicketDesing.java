package pe.puyu.jticketdesing.core;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPos.CharacterCodeTable;
import com.github.anastaciocintra.escpos.EscPos.CutMode;
import com.github.anastaciocintra.escpos.EscPos.PinConnector;
import com.github.anastaciocintra.escpos.Style.FontSize;
import com.github.anastaciocintra.escpos.image.BitonalOrderedDither;

import pe.puyu.jticketdesing.util.QRCodeGenerator;
import pe.puyu.jticketdesing.util.StringUtils;
import pe.puyu.jticketdesing.util.escpos.EscPosWrapper;
import pe.puyu.jticketdesing.util.escpos.StyleWrapper;

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
        this.documentLegal();
        this.textBackgroudInverted();
        this.additional();
        this.items();
        this.finalMessage();
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
    if (typeTicket.equalsIgnoreCase("command"))
      return;
    EscPosWrapper escPosWrapper = new EscPosWrapper(escpos, StyleWrapper.textBold());
    JSONObject business = this.ticket.getJSONObject("business");
    if (business.has("comercialDescription")) {
      var comercialDescription = business.getJSONObject("comercialDescription");
      String type = comercialDescription.getString("type");
      if (type.equalsIgnoreCase("text")) {
        var lines = StringUtils.wrapText(comercialDescription.getString("value"), maxTicketWidth, 2);
        for (var line : lines) {
          escPosWrapper.toCenter(line, maxTicketWidth, FontSize._2);
        }
      }
      if (type.equalsIgnoreCase("img") && this.metadata.has("logoPath")) {
        escPosWrapper.bitImage(this.metadata.getString("logoPath"), 290);
      }
    }
    if (business.has("description")) {
      escPosWrapper.removeStyleBold();
      var lines = StringUtils.wrapText(business.getString("description"), maxTicketWidth, 1);
      for (var line : lines) {
        escPosWrapper.toCenter(line, maxTicketWidth);
      }
    }
  }

  private void businessAdditional() throws Exception {
    var escPosWrapper = new EscPosWrapper(escpos);
    JSONObject business = this.ticket.getJSONObject("business");
    if (business.has("additional")) {
      JSONArray additional = business.getJSONArray("additional");
      for (Object item : additional) {
        var lines = StringUtils.wrapText(item.toString(), maxTicketWidth, 1);
        for (var line : lines) {
          escPosWrapper.toCenter(line, maxTicketWidth);
        }
      }
    }
    escPosWrapper.printLine(' ', 1);
  }

  private void documentLegal() throws Exception {
    if (!this.ticket.has("document"))
      return;
    var documentObj = this.ticket.get("document");
    String text = "";
    if (!(documentObj instanceof String)) {
      var document = (JSONObject) documentObj;
      if (this.typeTicket.equalsIgnoreCase("precount") || !document.has("identifier")) {
        text = document.getString("description");
      } else {
        text = String.format("%s %s", document.getString("description"), document.get("identifier").toString());
      }
    } else {
      text = String.format("%s", documentObj.toString());
    }
    var escPosWrapper = new EscPosWrapper(escpos, StyleWrapper.textBold());
    FontSize fontWidth = FontSize._1, fontHeight = FontSize._1;
    switch (this.typeTicket) {
      case "command":
        fontWidth = FontSize._2;
        fontHeight = FontSize._1;
        break;
      case "precount":
        fontWidth = FontSize._2;
        fontHeight = FontSize._2;
        break;
    }
    var lines = StringUtils.wrapText(text, maxTicketWidth, StyleWrapper.valueFontSize(fontWidth));
    for (var line : lines) {
      escPosWrapper.toCenter(line, this.maxTicketWidth, fontWidth, fontHeight);
    }
    escPosWrapper.printLine(' ', 1);
  }

  private void customer() throws Exception {
    var escPosWrapper = new EscPosWrapper(escpos);
    if (this.ticket.has("customer")) {
      JSONArray customer = this.ticket.getJSONArray("customer");
      for (var item : customer) {
        var lines = StringUtils.wrapText(item.toString(), maxTicketWidth, 1);
        for (var line : lines) {
          escPosWrapper.toLeft(line, maxTicketWidth);
        }
      }
    } else {
      escPosWrapper.printLine('-', maxTicketWidth);
    }
    escPosWrapper.printLine(' ', 1);
  }

  private void additional() throws Exception {
    var escPosWrapper = new EscPosWrapper(escpos);
    if (this.ticket.has("additional")) {
      JSONArray additional = this.ticket.getJSONArray("additional");
      var isCommand = this.typeTicket.equalsIgnoreCase("command");
      var fontSize = isCommand ? FontSize._2 : FontSize._1;
      if (isCommand)
        escPosWrapper.addStyleBold();
      for (var item : additional) {
        var lines = StringUtils.wrapText(item.toString(), maxTicketWidth,
            StyleWrapper.valueFontSize(fontSize));
        for (int i = 0; i < lines.size(); ++i) {
          if (isCommand) {
            escPosWrapper.toCenter(lines.get(i), this.maxTicketWidth, fontSize, FontSize._1);
          } else {
            escPosWrapper.toLeft(lines.get(i), this.maxTicketWidth, fontSize);
          }
        }
      }
    }
    escPosWrapper.printLine(' ', 1);
  }

  private void items() throws Exception {
    if (!this.ticket.has("items") || this.ticket.getJSONArray("items").isEmpty())
      return;

    var items = this.ticket.getJSONArray("items");
    boolean isCommand = this.typeTicket.equalsIgnoreCase("command");
    int quantityWidth = 0;
    var fontSize = isCommand ? FontSize._2 : FontSize._1;
    var valueFontSize = StyleWrapper.valueFontSize(fontSize);
    int totalWidth = isCommand ? 0 : Default.TOTAL_COLUMN_WIDTH;
    int descriptionWidth = this.maxTicketWidth;
    var price = new DecimalFormat("0.00");

    if (items.getJSONObject(0).has("quantity")) {
      quantityWidth = Default.QUANTITY_COLUMN_WIDTH * StyleWrapper.valueFontSize(fontSize);
      descriptionWidth -= (quantityWidth + totalWidth);
    } else {
      descriptionWidth -= totalWidth;
    }

    var escPosWrapper = new EscPosWrapper(escpos);
    escPosWrapper.toLeft("CAN", quantityWidth, false);
    escPosWrapper.toCenter("DESCRIPCION", descriptionWidth, FontSize._1, false);
    escPosWrapper.toRight("TOTAL", totalWidth, true);
    escPosWrapper.printBoldLine('-', this.maxTicketWidth);
    for (int i = 0; i < items.length(); ++i) {
      if (isCommand)
        escPosWrapper.addStyleBold();
      var item = items.getJSONObject(i);
      var descriptionObj = item.get("description");
      if (descriptionObj instanceof JSONArray) {
        var description = (JSONArray) descriptionObj;
        for (int j = 0; j < description.length(); ++j) {
          escPosWrapper.printLine(' ', quantityWidth, false);
          var lines = StringUtils.wrapText(description.getString(j), descriptionWidth, valueFontSize);
          for (var line : lines) {
            if (isCommand)
              escPosWrapper.toCenter(line, descriptionWidth, fontSize, FontSize._1);
            else
              escPosWrapper.toLeft(line, descriptionWidth, fontSize, j != 0);
          }
          if (j == 0) {
            escPosWrapper.toRight(price.format(item.getBigDecimal("totalPrice")), totalWidth, true);
          }
        }
      } else {
        var lines = StringUtils.wrapText(descriptionObj.toString(), descriptionWidth,
            StyleWrapper.valueFontSize(fontSize));
        for (int j = 0; j < lines.size(); ++j) {
          if (j == 0) {
            escPosWrapper.toLeft(item.get("quantity").toString(), quantityWidth, fontSize, FontSize._1, false);
          } else {
            escPosWrapper.printLine(' ', quantityWidth, false);
          }
          if (isCommand) {
            escPosWrapper.toCenter(lines.get(j), descriptionWidth, fontSize, FontSize._1);
          } else {
            escPosWrapper.toLeft(lines.get(j), descriptionWidth, !item.has("totalPrice"));
          }
          if (j == 0 && item.has("totalPrice")) {
            escPosWrapper.toRight(price.format(item.getBigDecimal("totalPrice")), totalWidth);
          } else if (!isCommand) {
            escPosWrapper.printLine(' ', totalWidth, true);
          }
        }
      }
      if (item.has("commentary")) {
        escPosWrapper.removeStyleBold();
        var lines = StringUtils.wrapText(item.getString("commentary"), descriptionWidth, 1);
        for (var line : lines) {
          escPosWrapper.printLine(' ', quantityWidth, false);
          if (isCommand)
            escPosWrapper.toCenter(line, descriptionWidth);
          else
            escPosWrapper.toLeft(line, descriptionWidth);
        }
      }
      escPosWrapper.printLine(' ', maxTicketWidth);
    }
    if (isCommand)
      escPosWrapper.printLine(' ', maxTicketWidth);
    else {
      escPosWrapper.printLine('-', maxTicketWidth);
    }
  }

  private void amounts() throws Exception {
    var escPosWrapper = new EscPosWrapper(escpos);
    if (this.ticket.has("amounts")) {
      var amounts = this.ticket.getJSONObject("amounts").toMap();
      for (var amount : amounts.entrySet()) {
        var text = String.format("%s: %s", amount.getKey(), amount.getValue().toString());
        escPosWrapper.toRight(text, maxTicketWidth);
      }
      escPosWrapper.printLine('-', maxTicketWidth);
    }
    escPosWrapper.printLine(' ', 1);
  }

  private void additionalFooter() throws Exception {
    var escPosWrapper = new EscPosWrapper(escpos);
    if (this.ticket.has("additionalFooter")) {
      var additionalFooter = this.ticket.getJSONArray("additionalFooter");
      for (Object item : additionalFooter) {
        escPosWrapper.toLeft(item.toString(), maxTicketWidth);
      }
      escPosWrapper.printLine('-', this.maxTicketWidth);
    }
    escPosWrapper.printLine(' ', 1);
  }

  private void finalMessage() throws Exception {
    var escPosWrapper = new EscPosWrapper(escpos);
    if (!this.ticket.has("finalMessage")) {
      escPosWrapper.printLine(' ', 1);
      return;
    }
    Object finalMessageObj = this.ticket.get("finalMessage");
    if (finalMessageObj instanceof JSONArray) {
      var finalMessage = (JSONArray) finalMessageObj;
      for (var item : finalMessage) {
        var lines = StringUtils.wrapText(item.toString(), maxTicketWidth, 1);
        for (var line : lines) {
          escPosWrapper.toCenter(line, maxTicketWidth);
        }
      }
    } else {
      var lines = StringUtils.wrapText(finalMessageObj.toString(), maxTicketWidth, 1);
      for (var line : lines) {
        escPosWrapper.toCenter(line, maxTicketWidth);
      }
    }
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
    var escPosWrapper = new EscPosWrapper(this.escpos);
    if (!nativeQR) {
      escPosWrapper.bitImage(QRCodeGenerator.render(stringQr), new BitonalOrderedDither());
    } else {
      escPosWrapper.standardQR(stringQr);
    }
  }

  private void titleExtra() throws Exception {
    var escPosWrapper = new EscPosWrapper(escpos, StyleWrapper.textBold());
    if (!this.ticket.has("titleExtra")) {
      escPosWrapper.printLine('-', maxTicketWidth);
      return;
    }
    var titleExtra = this.ticket.getJSONObject("titleExtra");
    escPosWrapper.toCenter(titleExtra.getString("title"), maxTicketWidth, FontSize._2);
    escPosWrapper.removeStyleBold();
    escPosWrapper.toCenter(titleExtra.getString("subtitle"), maxTicketWidth, FontSize._2);
    escPosWrapper.printLine(' ', 1);
  }

  private void textBackgroudInverted() throws Exception {
    if (this.ticket.has("textBackgroundInverted") && !this.ticket.isNull("textBackgroundInverted")) {
      var escPosWrapper = new EscPosWrapper(escpos);
      boolean supportBackgroundInverted = true;
      if (metadata.has("backgroundInverted") && !metadata.isNull("backgroundInverted")) {
        supportBackgroundInverted = metadata.getBoolean("backgroundInverted");
      }
      var text = String.format(" %s ", this.ticket.getString("textBackgroundInverted"));
      var pad = '*';
      if (supportBackgroundInverted) {
        pad = ' ';
        escPosWrapper.addStyleInverted();
      } else {
        escPosWrapper.addStyleBold();
      }
      escPosWrapper.toCenter(text, this.maxTicketWidth, pad);
      escPosWrapper.removeStyleInverted();
      escPosWrapper.printLine(' ', this.maxTicketWidth);
    }
  }
}
