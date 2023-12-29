package pe.puyu.jticketdesing.metadata;

import java.util.Optional;

import com.google.gson.JsonObject;

import com.github.anastaciocintra.escpos.EscPos.CharacterCodeTable;
import com.github.anastaciocintra.escpos.Style.FontSize;

import pe.puyu.jticketdesing.util.escpos.StyleWrapper;

public class TicketPropertiesReader {
	private JsonObject properties;
	private final JsonObject ticket;

	public TicketPropertiesReader(JsonObject ticket) {
		this.ticket = ticket;
		JsonObject printer = ticket.getAsJsonObject("printer");
		properties = new JsonObject();
		if (printer.has("properties") && !printer.get("properties").isJsonNull()) {
			properties = printer.getAsJsonObject("properties");
		}
	}

	public CharacterCodeTable charCodeTable() {
		try {
			if (properties.has("charCodeTable") && !properties.get("charCodeTable").isJsonNull())
				return CharacterCodeTable.valueOf(properties.get("charCodeTable").getAsString());
			else
				return CharacterCodeTable.WPC1252;
		} catch (Exception e) {
			return CharacterCodeTable.WPC1252;
		}
	}

	public FontSize fontSizeCommand() {
		if (properties.has("fontSizeCommand") && !properties.get("fontSizeCommand").isJsonNull()) {
			return StyleWrapper.toFontSize(properties.get("fontSizeCommand").getAsInt());
		} else {
			return FontSize._2;
		}
	}

	public Optional<String> logoPath() {
		if (!ticket.has("metadata")) {
			return Optional.empty();
		}
		JsonObject metadata = ticket.getAsJsonObject("metadata");
		if (metadata.has("logoPath") && !metadata.get("logoPath").isJsonNull())
			return Optional.ofNullable(metadata.get("logoPath").getAsString());
		return Optional.empty();
	}

	public String type() {
		if (ticket.has("type") && !ticket.get("type").isJsonNull())
			return ticket.get("type").getAsString();
		return "tipo de documento desconocido";
	}

	public int times() {
		if (ticket.has("times") && !ticket.get("times").isJsonNull()) {
			return ticket.get("times").getAsInt();
		}
		return 1;
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

	public boolean nativeQR() {
		if (properties.has("nativeQR") && !properties.get("nativeQR").isJsonNull())
			return properties.get("nativeQR").getAsBoolean();
		return false;
	}

	public boolean textNormalize() {
		if (properties.has("textNormalize") && !properties.get("textNormalize").isJsonNull()) {
			return properties.get("textNormalize").getAsBoolean();
		}
		return false;
	}

	public int blankLinesAfterItems() {
		if (properties.has("blankLinesAfterItems") && !properties.get("blankLinesAfterItems").isJsonNull()) {
			return properties.get("blankLinesAfterItems").getAsInt();
		}
		return 0;
	}
}
