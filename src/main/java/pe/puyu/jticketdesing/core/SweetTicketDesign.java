package pe.puyu.jticketdesing.core;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPos.CutMode;
import com.github.anastaciocintra.escpos.EscPos.PinConnector;
import com.github.anastaciocintra.escpos.Style.FontSize;
import com.github.anastaciocintra.escpos.image.BitonalOrderedDither;
import com.github.anastaciocintra.escpos.image.BitonalThreshold;

import pe.puyu.jticketdesing.metadata.TicketPropertiesReader;
import pe.puyu.jticketdesing.util.ImageUtil;
import pe.puyu.jticketdesing.util.QRCodeGenerator;
import pe.puyu.jticketdesing.util.StringUtils;
import pe.puyu.jticketdesing.util.escpos.*;

class Default {
	public final static int QUANTITY_COLUMN_WIDTH = 4;
	public final static int TOTAL_COLUMN_WIDTH = 7;
	public final static int UNIT_PRICE_WIDTH = 7;
}

public class SweetTicketDesign {

	private final JsonObject data;
	private EscPos escpos;
	private final DesignerHelper<TicketPropertiesReader> helper;

	public SweetTicketDesign(JsonObject ticket) {
		this.data = ticket.getAsJsonObject("data");
		this.helper = new DesignerHelper<>(new TicketPropertiesReader(ticket));
	}

	public SweetTicketDesign(String jsonString) {
		this(JsonParser.parseString(jsonString).getAsJsonObject());
	}

	private void initEscPos(ByteArrayOutputStream buffer) throws Exception {
		this.escpos = new EscPos(buffer);
		this.escpos.setCharacterCodeTable(helper.properties().charCodeTable());
	}

	public byte[] getBytes() throws Exception {
		try {
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			initEscPos(byteArrayOutputStream);
			for (int i = 0; i < helper.properties().times(); ++i) {
				designLayout();
			}
			return byteArrayOutputStream.toByteArray();
		} catch (Exception e) {
			throw new Exception(String.format("JTicketDesign Exception: %s", e.getMessage()));
		} finally {
			this.escpos.close();
		}
	}

	private void designLayout() throws Exception {
		this.header();
		switch (helper.properties().type()) {
			case "proforma":
				this.businessAdditional();
				this.documentLegal();
				this.customer();
				this.additional();
				this.items();
				this.amounts();
				this.additionalFooter();
				break;
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
				this.documentLegal();
				this.textBackgroundInverted();
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
				throw new Exception(String.format("No se pudo imprimir el diseño tipo de documento %s", helper.properties().type()));
		}
		this.escpos.feed(4);
		this.escpos.cut(CutMode.PART);
	}

	private void productionArea() throws Exception {
		if (!this.data.has("productionArea") || !helper.properties().showProductionArea())
			return;
		EscPosWrapper escPosWrapper = new EscPosWrapper(escpos);
		StyleText styleText = helper.styleBuilder()
			.align(JustifyAlign.CENTER)
			.bold(true)
			.fontSize(2)
			.build();
		escPosWrapper.printText(this.data.get("productionArea").getAsString(), helper.properties().width(), styleText);
		escPosWrapper.printLine(' ', 1);
	}

	private void header() throws Exception {
		if (helper.properties().type().equalsIgnoreCase("command"))
			return;
		EscPosWrapper escPosWrapper = new EscPosWrapper(escpos);
		JsonObject business = this.data.getAsJsonObject("business");
		if (business.has("comercialDescription")) {
			JsonObject commercialDescription = business.getAsJsonObject("comercialDescription");
			String type = commercialDescription.get("type").getAsString();
			if (type.equalsIgnoreCase("text")) {
				String commercialDescriptionText = commercialDescription.get("text").getAsString();
				var lines = StringUtils.wrapText(commercialDescriptionText, helper.properties().width(), 2);
				StyleText commercialDescriptionStyle = helper.styleBuilder()
					.align(JustifyAlign.CENTER)
					.fontSize(FontSize._2)
					.bold(true)
					.build();
				for (String line : lines) {
					escPosWrapper.printText(line, helper.properties().width(), commercialDescriptionStyle);
				}
			}
			if (type.equalsIgnoreCase("img") && helper.properties().logoPath().isPresent()) {
				int imgSize = 290;
				BufferedImage image = ImageUtil.toBufferedImage(helper.properties().logoPath().get());
				BufferedImage resizedImage = ImageUtil.resizeImage(image, imgSize);
				BufferedImage centerImage = ImageUtil.justifyImageToCenter(resizedImage, helper.calcWidthPaperInPx(), imgSize);
				escPosWrapper.bitImage(centerImage, new BitonalThreshold());
			}
		}
		if (business.has("description")) {
			String descriptionText = business.get("description").getAsString();
			var lines = StringUtils.wrapText(descriptionText, helper.properties().width(), 1);
			StyleText descriptionStyle = helper.styleBuilder().build();
			for (var line : lines) {
				escPosWrapper.printText(line, helper.properties().width(), descriptionStyle);
			}
		}
	}

	private void businessAdditional() throws Exception {
		var escPosWrapper = new EscPosWrapper(escpos);
		JsonObject business = this.data.getAsJsonObject("business");
		StyleText styleText = helper.styleBuilder().align(JustifyAlign.CENTER).build();
		if (business.has("additional")) {
			JsonArray additional = business.get("additional").getAsJsonArray();
			for (JsonElement item : additional) {
				List<String> lines = StringUtils.wrapText(item.getAsString(), helper.properties().width(), 1);
				for (String line : lines) {
					escPosWrapper.printText(line, helper.properties().width(), styleText);
				}
			}
		}
		escPosWrapper.printLine(' ', 1);
	}

	private void documentLegal() throws Exception {
		if (!this.data.has("document"))
			return;
		JsonElement documentObj = this.data.get("document");
		String text;
		if (!(documentObj.isJsonPrimitive())) {
			JsonObject document = documentObj.getAsJsonObject();
			if (helper.properties().type().equalsIgnoreCase("precount") || !document.has("identifier")) {
				text = document.get("description").getAsString();
			} else {
				text = String.format("%s %s", document.get("description").getAsString(), document.get("identifier").getAsString());
			}
		} else {
			text = String.format("%s", documentObj);
		}
		FontSize fontWidth = FontSize._1, fontHeight = FontSize._1;
		switch (helper.properties().type()) {
			case "command":
				fontWidth = FontSize._2;
				break;
			case "precount":
				fontWidth = FontSize._2;
				fontHeight = FontSize._2;
				break;
		}
		var lines = StringUtils.wrapText(text, helper.properties().width(), StyleWrapper.valueFontSize(fontWidth));
		StyleText styleText = helper.styleBuilder()
			.align(JustifyAlign.CENTER)
			.bold(true)
			.fontWidth(fontWidth)
			.fontHeight(fontHeight)
			.build();
		var escPosWrapper = new EscPosWrapper(escpos);
		for (String line : lines) {
			escPosWrapper.printText(line, helper.properties().width(), styleText);
		}
		if (!helper.properties().type().equalsIgnoreCase("command")) {
			escPosWrapper.printLine(' ', 1);
		}
	}

	private void customer() throws Exception {
		var escPosWrapper = new EscPosWrapper(escpos);
		StyleText styleText = helper.styleBuilder()
			.align(JustifyAlign.LEFT)
			.build();
		if (this.data.has("customer")) {
			JsonArray customer = this.data.get("customer").getAsJsonArray();
			for (JsonElement item : customer) {
				List<String> lines = StringUtils.wrapText(item.getAsString(), helper.properties().width(), 1);
				for (String line : lines) {
					escPosWrapper.printText(line, helper.properties().width(), styleText);
				}
			}
		} else {
			escPosWrapper.printLine('-', helper.properties().width());
		}
		escPosWrapper.printLine(' ', 1);
	}

	private void additional() throws Exception {
		EscPosWrapper escPosWrapper = new EscPosWrapper(escpos);
		if (this.data.has("additional")) {
			JsonArray additional = this.data.get("additional").getAsJsonArray();
			boolean isCommand = helper.properties().type().equalsIgnoreCase("command");
			FontSize fontSize = isCommand ? FontSize._2 : FontSize._1;
			StyleTextBuilder styleBuilder = helper.styleBuilder().bold(isCommand);
			for (int i = 0; i < additional.size(); ++i) {
				JsonElement item = additional.get(i);
				List<String> lines = StringUtils.wrapText(item.getAsString(), helper.properties().width(), StyleWrapper.valueFontSize(fontSize));
				for (String line : lines) {
					if (isCommand) {
						var style = styleBuilder
							.fontHeight(1)
							.fontWidth(fontSize)
							.align(JustifyAlign.CENTER)
							.build();
						escPosWrapper.printText(line, helper.properties().width(), style);
					} else {
						var style = styleBuilder.fontSize(fontSize);
						escPosWrapper.toLeft(normalize(line), properties.width(), fontSize);
					}
				}
			}
		}
		escPosWrapper.printLine(' ', 1);
	}

	private void items() throws Exception {
		if (!this.data.has("items") || this.data.get("items").getAsJsonArray().isEmpty())
			return;

		JsonArray items = this.data.get("items").getAsJsonArray();
		boolean isCommand = properties.type().equalsIgnoreCase("command");
		int quantityWidth = 0;
		FontSize fontSize = isCommand ? properties.fontSizeCommand() : FontSize._1;
		int valueFontSize = StyleWrapper.valueFontSize(fontSize);
		int totalWidth = isCommand ? 0 : Default.TOTAL_COLUMN_WIDTH;
		int unitPriceWidth = properties.showUnitPrice() ? Default.UNIT_PRICE_WIDTH : 0;
		int descriptionWidth = properties.width();
		DecimalFormat price = new DecimalFormat("0.00");

		if (items.get(0).getAsJsonObject().has("quantity")) {
			quantityWidth = Default.QUANTITY_COLUMN_WIDTH * StyleWrapper.valueFontSize(fontSize);
			descriptionWidth -= (quantityWidth + totalWidth);
		} else {
			descriptionWidth -= totalWidth;
		}

		if (properties.showUnitPrice()) {
			descriptionWidth -= unitPriceWidth;
		}

		EscPosWrapper escPosWrapper = new EscPosWrapper(escpos);
		escPosWrapper.toLeft("CAN", quantityWidth, false);
		escPosWrapper.toCenter("DESCRIPCION", descriptionWidth, FontSize._1, false);
		escPosWrapper.toRight("P/U ", unitPriceWidth, FontSize._1, false);
		escPosWrapper.toRight("TOTAL", totalWidth, true);
		escPosWrapper.printBoldLine('-', properties.width());
		for (int i = 0; i < items.size(); ++i) {
			if (isCommand)
				escPosWrapper.addStyleBold();
			JsonObject item = items.get(i).getAsJsonObject();
			JsonElement descriptionObj = item.get("description");
			if (descriptionObj.isJsonArray()) {
				JsonArray description = descriptionObj.getAsJsonArray();
				for (int j = 0; j < description.size(); ++j) {
					escPosWrapper.printLine(' ', quantityWidth, false);
					List<String> lines = StringUtils.wrapText(description.get(j).getAsString(), descriptionWidth, valueFontSize);
					for (int k = 0; k < lines.size(); ++k) {
						String line = normalize(lines.get(k));
						if (isCommand)
							escPosWrapper.toCenter(line, descriptionWidth, fontSize, FontSize._1);
						else
							escPosWrapper.toLeft(line, descriptionWidth, fontSize, k != 0 || j != 0);
						if (j == 0 && k == 0) {
							if (properties.showUnitPrice()) {
								if (item.has("unitPrice") && !item.get("unitPrice").isJsonNull()) {
									escPosWrapper.toRight(price.format(item.get("unitPrice").getAsBigDecimal()).concat(" "), unitPriceWidth, false);
								} else {
									escPosWrapper.toRight("null", unitPriceWidth, false);
								}
							}
							escPosWrapper.toRight(price.format(item.get("totalPrice").getAsBigDecimal()), totalWidth, true);
						}
					}
				}
			} else {
				List<String> lines = StringUtils.wrapText(descriptionObj.getAsString(), descriptionWidth,
					StyleWrapper.valueFontSize(fontSize));
				for (int j = 0; j < lines.size(); ++j) {
					if (j == 0) {
						escPosWrapper.toLeft(item.get("quantity").getAsString(), quantityWidth, fontSize, FontSize._1, false);
					} else {
						escPosWrapper.printLine(' ', quantityWidth, false);
					}
					if (isCommand) {
						escPosWrapper.toCenter(normalize(lines.get(j)), descriptionWidth, fontSize, FontSize._1);
						continue;
					} else {
						escPosWrapper.toLeft(normalize(lines.get(j)), descriptionWidth, !item.has("totalPrice"));
					}

					if (j == 0 && properties.showUnitPrice()) {
						if (item.has("unitPrice") && !item.get("unitPrice").isJsonNull()) {
							escPosWrapper.toRight(price.format(item.get("unitPrice").getAsBigDecimal()).concat(" "), unitPriceWidth, false);
						} else {
							escPosWrapper.toRight("null", unitPriceWidth, false);
						}
					}
					if (j == 0 && item.has("totalPrice")) {
						escPosWrapper.toRight(price.format(item.get("totalPrice").getAsBigDecimal()), totalWidth);
					} else {
						escPosWrapper.printLine(' ', totalWidth, true);
					}
				}
			}
			if (item.has("commentary") && !item.get("commentary").isJsonNull()) {
				escPosWrapper.removeStyleBold();
				List<String> lines = StringUtils.wrapText(item.get("commentary").getAsString(), descriptionWidth, 1);
				for (String line : lines) {
					line = normalize(line);
					escPosWrapper.printLine(' ', quantityWidth, false);
					if (isCommand)
						escPosWrapper.toCenter(line, descriptionWidth);
					else
						escPosWrapper.toLeft(line, descriptionWidth);
				}
			} else if (isCommand) {
				escPosWrapper.printLine(' ', 1);
			}
		}
		for (int i = 0; i < properties.blankLinesAfterItems(); ++i) {
			escPosWrapper.printLine(' ', 1);
		}
		if (isCommand)
			escPosWrapper.printLine(' ', 1);
		else {
			escPosWrapper.printLine('-', properties.width());
		}
	}

	private void amounts() throws Exception {
		EscPosWrapper escPosWrapper = new EscPosWrapper(escpos);
		if (this.data.has("amounts")) {
			Map<String, JsonElement> amounts = this.data.getAsJsonObject("amounts").asMap();
			for (Map.Entry<String, JsonElement> amount : amounts.entrySet()) {
				String text = String.format("%s: %s", amount.getKey(), amount.getValue().getAsString());
				escPosWrapper.toRight(text, properties.width());
			}
			escPosWrapper.printLine('-', properties.width());
		}
		escPosWrapper.printLine(' ', 1);
	}

	private void additionalFooter() throws Exception {
		EscPosWrapper escPosWrapper = new EscPosWrapper(escpos);
		if (this.data.has("additionalFooter")) {
			JsonArray additionalFooter = this.data.get("additionalFooter").getAsJsonArray();
			for (JsonElement item : additionalFooter) {
				List<String> lines = StringUtils.wrapText(item.getAsString(), properties.width(), 1);
				for (String line : lines) {
					escPosWrapper.toLeft(normalize(line), properties.width());
				}
			}
			if (!properties.type().equals("proforma")) {
				escPosWrapper.printLine('-', properties.width());
			}
		}
		escPosWrapper.printLine(' ', 1);
	}

	private void finalMessage() throws Exception {
		EscPosWrapper escPosWrapper = new EscPosWrapper(escpos);
		if (!this.data.has("finalMessage")) {
			escPosWrapper.printLine(' ', 1);
			return;
		}
		JsonElement finalMessageObj = this.data.get("finalMessage");
		if (finalMessageObj.isJsonArray()) {
			JsonArray finalMessage = finalMessageObj.getAsJsonArray();
			for (JsonElement item : finalMessage) {
				List<String> lines = StringUtils.wrapText(item.getAsString(), properties.width(), 1);
				for (String line : lines) {
					escPosWrapper.toCenter(normalize(line), properties.width());
				}
			}
		} else {
			List<String> lines = StringUtils.wrapText(finalMessageObj.getAsString(), properties.width(), 1);
			for (String line : lines) {
				escPosWrapper.toCenter(normalize(line), properties.width());
			}
		}
	}

	private void stringQR() throws Exception {
		if (!this.data.has("stringQR") || this.data.get("stringQR").isJsonNull()) {
			this.escpos.writeLF(StringUtils.repeat('-', properties.width()));
			return;
		}
		String stringQr = this.data.get("stringQR").toString();
		EscPosWrapper escPosWrapper = new EscPosWrapper(this.escpos);
		if (!properties.nativeQR()) {
			int sizeQR = 200;
			BufferedImage image = QRCodeGenerator.render(stringQr, sizeQR);
			BufferedImage centerImage = ImageUtil.justifyImageToCenter(image, calcWidthPaperInPx(), sizeQR);
			escPosWrapper.bitImage(centerImage, new BitonalOrderedDither());
		} else {
			escPosWrapper.printLine(' ', 1);
			escPosWrapper.standardQR(stringQr);
		}
	}

	private void titleExtra() throws Exception {
		EscPosWrapper escPosWrapper = new EscPosWrapper(escpos, StyleWrapper.textBold());
		if (!this.data.has("titleExtra")) {
			escPosWrapper.printLine('-', properties.width());
			return;
		}
		JsonObject titleExtra = this.data.getAsJsonObject("titleExtra");
		escPosWrapper.toCenter(normalize(titleExtra.get("title").getAsString()), properties.width(), FontSize._2);
		escPosWrapper.removeStyleBold();
		escPosWrapper.toCenter(normalize(titleExtra.get("subtitle").getAsString()), properties.width(), FontSize._2);
		escPosWrapper.printLine(' ', 1);
	}

	private void textBackgroundInverted() throws Exception {
		if (this.data.has("textBackgroundInverted") && !this.data.get("textBackgroundInverted").isJsonNull()) {
			EscPosWrapper escPosWrapper = new EscPosWrapper(escpos);
			boolean supportBackgroundInverted = properties.backgroundInverted();
			String text = String.format(" %s ", this.data.get("textBackgroundInverted").getAsString());
			text = normalize(text);
			char pad = '*';
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
}
