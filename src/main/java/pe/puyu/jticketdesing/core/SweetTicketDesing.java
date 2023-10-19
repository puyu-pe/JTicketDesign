package pe.puyu.jticketdesing.core;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPos.CutMode;
import com.github.anastaciocintra.escpos.EscPos.PinConnector;
import com.github.anastaciocintra.escpos.Style.FontSize;
import com.github.anastaciocintra.escpos.image.BitonalOrderedDither;

import pe.puyu.jticketdesing.metadata.TicketPropertiesReader;
import pe.puyu.jticketdesing.util.QRCodeGenerator;
import pe.puyu.jticketdesing.util.StringUtils;
import pe.puyu.jticketdesing.util.escpos.EscPosWrapper;
import pe.puyu.jticketdesing.util.escpos.StyleWrapper;

class Default {
  public final static int QUANTITY_COLUMN_WIDTH = 4;
  public final static int TOTAL_COLUMN_WIDTH = 7;
}

public class SweetTicketDesing {

  private JSONObject data;
  private TicketPropertiesReader properties;
  private EscPos escpos;

  public SweetTicketDesing(JSONObject ticket) throws Exception {
    this.data = ticket.getJSONObject("data");
    this.properties = new TicketPropertiesReader(ticket);
  }

  private void initEscPos(ByteArrayOutputStream buffer) throws Exception {
    this.escpos = new EscPos(buffer);
    this.escpos.setCharacterCodeTable(this.properties.charCodeTable());
  }

  public byte[] getBytes() throws Exception {
    try {
      var byteArrayOutputStream = new ByteArrayOutputStream();
      initEscPos(byteArrayOutputStream);
      for (int i = 0; i < properties.times(); ++i) {
        desingLayout();
      }
      return byteArrayOutputStream.toByteArray();
    } catch (Exception e) {
      throw new Exception(String.format("JTicketDesing Exception: %s", e.getMessage()));
    } finally {
      this.escpos.close();
    }
  }

  private void desingLayout() throws Exception {
    this.header();
    switch (properties.type()) {
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
        throw new Exception(String.format("No se pudo imprimir el diseño tipo de documento %s", properties.type()));
    }
    this.escpos.feed(4);
    this.escpos.cut(CutMode.PART);
  }

  private void header() throws Exception {
    if (properties.type().equalsIgnoreCase("command"))
      return;
    EscPosWrapper escPosWrapper = new EscPosWrapper(escpos, StyleWrapper.textBold());
    JSONObject business = this.data.getJSONObject("business");
    if (business.has("comercialDescription")) {
      var comercialDescription = business.getJSONObject("comercialDescription");
      String type = comercialDescription.getString("type");
      if (type.equalsIgnoreCase("text")) {
        var lines = StringUtils.wrapText(comercialDescription.getString("value"), properties.width(), 2);
        for (var line : lines) {
          escPosWrapper.toCenter(normalize(line), properties.width(), FontSize._2);
        }
      }
      if (type.equalsIgnoreCase("img") && properties.logoPath().isPresent()) {
        escPosWrapper.bitImage(properties.logoPath().get(), 290);
      }
    }
    if (business.has("description")) {
      escPosWrapper.removeStyleBold();
      var lines = StringUtils.wrapText(business.getString("description"), properties.width(), 1);
      for (var line : lines) {
        escPosWrapper.toCenter(normalize(line), properties.width());
      }
    }
  }

  private void businessAdditional() throws Exception {
    var escPosWrapper = new EscPosWrapper(escpos);
    JSONObject business = this.data.getJSONObject("business");
    if (business.has("additional")) {
      JSONArray additional = business.getJSONArray("additional");
      for (Object item : additional) {
        var lines = StringUtils.wrapText(item.toString(), properties.width(), 1);
        for (var line : lines) {
          escPosWrapper.toCenter(normalize(line), properties.width());
        }
      }
    }
    escPosWrapper.printLine(' ', 1);
  }

  private void documentLegal() throws Exception {
    if (!this.data.has("document"))
      return;
    var documentObj = this.data.get("document");
    String text = "";
    if (!(documentObj instanceof String)) {
      var document = (JSONObject) documentObj;
      if (properties.type().equalsIgnoreCase("precount") || !document.has("identifier")) {
        text = document.getString("description");
      } else {
        text = String.format("%s %s", document.getString("description"), document.get("identifier").toString());
      }
    } else {
      text = String.format("%s", documentObj.toString());
    }
    var escPosWrapper = new EscPosWrapper(escpos, StyleWrapper.textBold());
    FontSize fontWidth = FontSize._1, fontHeight = FontSize._1;
    switch (properties.type()) {
      case "command":
        fontWidth = FontSize._2;
        fontHeight = FontSize._1;
        break;
      case "precount":
        fontWidth = FontSize._2;
        fontHeight = FontSize._2;
        break;
    }
    var lines = StringUtils.wrapText(text, properties.width(), StyleWrapper.valueFontSize(fontWidth));
    for (var line : lines) {
      escPosWrapper.toCenter(normalize(line), properties.width(), fontWidth, fontHeight);
    }
    if (!properties.type().equalsIgnoreCase("command")) {
      escPosWrapper.printLine(' ', 1);
    }
  }

  private void customer() throws Exception {
    var escPosWrapper = new EscPosWrapper(escpos);
    if (this.data.has("customer")) {
      JSONArray customer = this.data.getJSONArray("customer");
      for (var item : customer) {
        var lines = StringUtils.wrapText(item.toString(), properties.width(), 1);
        for (var line : lines) {
          escPosWrapper.toLeft(normalize(line), properties.width());
        }
      }
    } else {
      escPosWrapper.printLine('-', properties.width());
    }
    escPosWrapper.printLine(' ', 1);
  }

  private void additional() throws Exception {
    var escPosWrapper = new EscPosWrapper(escpos);
    if (this.data.has("additional")) {
      JSONArray additional = this.data.getJSONArray("additional");
      var isCommand = properties.type().equalsIgnoreCase("command");
      var fontSize = isCommand ? FontSize._2 : FontSize._1;
      if (isCommand)
        escPosWrapper.addStyleBold();
      for (int i = 0; i < additional.length(); ++i) {
        // NOTE: El siguiente if es para mantener la compatibilidad con puka-csharp, solo en comandas
        // NOTE: Eliminarlo una vez puka-charp este completamente eliminado
        if(i == 0 && isCommand)
          continue;
        var item = additional.get(i);
        var lines = StringUtils.wrapText(item.toString(), properties.width(), StyleWrapper.valueFontSize(fontSize));
        for (int j = 0; j < lines.size(); ++j) {
          if (isCommand) {
            escPosWrapper.toCenter(normalize(lines.get(j)), properties.width(), fontSize, FontSize._1);
          } else {
            escPosWrapper.toLeft(normalize(lines.get(j)), properties.width(), fontSize);
          }
        }
      }
    }
    escPosWrapper.printLine(' ', 1);
  }

  private void items() throws Exception {
    if (!this.data.has("items") || this.data.getJSONArray("items").isEmpty())
      return;

    var items = this.data.getJSONArray("items");
    boolean isCommand = properties.type().equalsIgnoreCase("command");
    int quantityWidth = 0;
    var fontSize = isCommand ? properties.fontSizeCommand() : FontSize._1;
    var valueFontSize = StyleWrapper.valueFontSize(fontSize);
    int totalWidth = isCommand ? 0 : Default.TOTAL_COLUMN_WIDTH;
    int descriptionWidth = properties.width();
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
    escPosWrapper.printBoldLine('-', properties.width());
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
            line = normalize(line);
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
            escPosWrapper.toCenter(normalize(lines.get(j)), descriptionWidth, fontSize, FontSize._1);
          } else {
            escPosWrapper.toLeft(normalize(lines.get(j)), descriptionWidth, !item.has("totalPrice"));
          }
          if (j == 0 && item.has("totalPrice")) {
            escPosWrapper.toRight(price.format(item.getBigDecimal("totalPrice")), totalWidth);
          } else if (!isCommand) {
            escPosWrapper.printLine(' ', totalWidth, true);
          }
        }
      }
      if (item.has("commentary") && !item.isNull("commentary")) {
        escPosWrapper.removeStyleBold();
        var lines = StringUtils.wrapText(item.getString("commentary"), descriptionWidth, 1);
        for (var line : lines) {
          line = normalize(line);
          escPosWrapper.printLine(' ', quantityWidth, false);
          if (isCommand)
            escPosWrapper.toCenter(line, descriptionWidth);
          else
            escPosWrapper.toLeft(line, descriptionWidth);
        }
      }
    }
    if (isCommand)
      escPosWrapper.printLine(' ', properties.width());
    else {
      escPosWrapper.printLine('-', properties.width());
    }
  }

  private void amounts() throws Exception {
    var escPosWrapper = new EscPosWrapper(escpos);
    if (this.data.has("amounts")) {
      var amounts = this.data.getJSONObject("amounts").toMap();
      for (var amount : amounts.entrySet()) {
        var text = String.format("%s: %s", amount.getKey(), amount.getValue().toString());
        escPosWrapper.toRight(text, properties.width());
      }
      escPosWrapper.printLine('-', properties.width());
    }
    escPosWrapper.printLine(' ', 1);
  }

  private void additionalFooter() throws Exception {
    var escPosWrapper = new EscPosWrapper(escpos);
    if (this.data.has("additionalFooter")) {
      var additionalFooter = this.data.getJSONArray("additionalFooter");
      for (Object item : additionalFooter) {
        escPosWrapper.toLeft(normalize(item.toString()), properties.width());
      }
      escPosWrapper.printLine('-', properties.width());
    }
    escPosWrapper.printLine(' ', 1);
  }

  private void finalMessage() throws Exception {
    var escPosWrapper = new EscPosWrapper(escpos);
    if (!this.data.has("finalMessage")) {
      escPosWrapper.printLine(' ', 1);
      return;
    }
    Object finalMessageObj = this.data.get("finalMessage");
    if (finalMessageObj instanceof JSONArray) {
      var finalMessage = (JSONArray) finalMessageObj;
      for (var item : finalMessage) {
        var lines = StringUtils.wrapText(item.toString(), properties.width(), 1);
        for (var line : lines) {
          escPosWrapper.toCenter(normalize(line), properties.width());
        }
      }
    } else {
      var lines = StringUtils.wrapText(finalMessageObj.toString(), properties.width(), 1);
      for (var line : lines) {
        escPosWrapper.toCenter(normalize(line), properties.width());
      }
    }
  }

  private void stringQR() throws Exception {
    if (!this.data.has("stringQR") || this.data.isNull("stringQR")) {
      this.escpos.writeLF(StringUtils.repeat('-', properties.width()));
      return;
    }
    var stringQr = this.data.get("stringQR").toString();
    var escPosWrapper = new EscPosWrapper(this.escpos);
    if (!properties.nativeQR()) {
      escPosWrapper.bitImage(QRCodeGenerator.render(stringQr), new BitonalOrderedDither());
    } else {
      escPosWrapper.standardQR(stringQr);
    }
  }

  private void titleExtra() throws Exception {
    var escPosWrapper = new EscPosWrapper(escpos, StyleWrapper.textBold());
    if (!this.data.has("titleExtra")) {
      escPosWrapper.printLine('-', properties.width());
      return;
    }
    var titleExtra = this.data.getJSONObject("titleExtra");
    escPosWrapper.toCenter(normalize(titleExtra.getString("title")), properties.width(), FontSize._2);
    escPosWrapper.removeStyleBold();
    escPosWrapper.toCenter(normalize(titleExtra.getString("subtitle")), properties.width(), FontSize._2);
    escPosWrapper.printLine(' ', 1);
  }

  private void textBackgroudInverted() throws Exception {
    if (this.data.has("textBackgroundInverted") && !this.data.isNull("textBackgroundInverted")) {
      var escPosWrapper = new EscPosWrapper(escpos);
      boolean supportBackgroundInverted = properties.backgroundInverted();
      var text = String.format(" %s ", this.data.getString("textBackgroundInverted"));
      text = normalize(text);
      var pad = '*';
      if (supportBackgroundInverted) {
        pad = ' ';
        escPosWrapper.addStyleInverted();
      } else {
        escPosWrapper.addStyleBold();
      }
      escPosWrapper.toCenter(normalize(text), properties.width(), pad);
      escPosWrapper.removeStyleInverted();
      escPosWrapper.printLine(' ', properties.width());
    }
  }

  private String normalize(String text) {
    if (!properties.textNormalize()) {
      return text;
    }
    String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
    return Pattern.compile("\\p{InCombiningDiacriticalMarks}+").matcher(normalized)
        .replaceAll("")
        .replaceAll("[^\\p{ASCII}]", "");
  }
}
