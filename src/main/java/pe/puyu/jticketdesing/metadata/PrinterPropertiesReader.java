package pe.puyu.jticketdesing.metadata;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.Style;
import com.google.gson.JsonObject;
import pe.puyu.jticketdesing.util.escpos.StyleWrapper;

public class PrinterPropertiesReader {
	protected JsonObject properties;
	protected final JsonObject ticket;

	public PrinterPropertiesReader(JsonObject ticket){
		this.ticket = ticket;
		JsonObject printer = ticket.getAsJsonObject("printer");
		properties = new JsonObject();
		if (printer.has("properties") && !printer.get("properties").isJsonNull()) {
			properties = printer.getAsJsonObject("properties");
		}
	}

	public EscPos.CharacterCodeTable charCodeTable() {
		try {
			if (properties.has("charCodeTable") && !properties.get("charCodeTable").isJsonNull())
				return EscPos.CharacterCodeTable.valueOf(properties.get("charCodeTable").getAsString());
			else
				return EscPos.CharacterCodeTable.WPC1252;
		} catch (Exception e) {
			return EscPos.CharacterCodeTable.WPC1252;
		}
	}

	public Style.FontSize fontSizeCommand() {
		if (properties.has("fontSizeCommand") && !properties.get("fontSizeCommand").isJsonNull()) {
			return StyleWrapper.toFontSize(properties.get("fontSizeCommand").getAsInt());
		} else {
			return Style.FontSize._2;
		}
	}

	public int width() {
		if (properties.has("width") && !properties.get("width").isJsonNull()) {
			return properties.get("width").getAsInt();
		}
		return 42;
	}

	public boolean backgroundInverted() {
		if (properties.has("backgroundInverted") && !properties.get("backgroundInverted").isJsonNull()) {
			return properties.get("backgroundInverted").getAsBoolean();
		}
		return true;
	}

	public boolean textNormalize() {
		if (properties.has("textNormalize") && !properties.get("textNormalize").isJsonNull()) {
			return properties.get("textNormalize").getAsBoolean();
		}
		return false;
	}
}
