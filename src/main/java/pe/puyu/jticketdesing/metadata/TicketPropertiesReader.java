package pe.puyu.jticketdesing.metadata;

import java.util.Optional;

import com.github.anastaciocintra.escpos.Style;
import com.google.gson.JsonObject;
import pe.puyu.jticketdesing.util.escpos.StyleEscPosUtil;

public class TicketPropertiesReader extends PrinterPropertiesReader {

	public TicketPropertiesReader(JsonObject ticket) {
		super(ticket);
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

	public Style.FontSize fontSizeCommand() {
		if (properties.has("fontSizeCommand") && !properties.get("fontSizeCommand").isJsonNull()) {
			return StyleEscPosUtil.toFontSize(properties.get("fontSizeCommand").getAsInt());
		} else {
			return Style.FontSize._2;
		}
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

	public boolean nativeQR() {
		if (properties.has("nativeQR") && !properties.get("nativeQR").isJsonNull())
			return properties.get("nativeQR").getAsBoolean();
		return false;
	}

	public int blankLinesAfterItems() {
		if (properties.has("blankLinesAfterItems") && !properties.get("blankLinesAfterItems").isJsonNull()) {
			return properties.get("blankLinesAfterItems").getAsInt();
		}
		return 0;
	}

	public boolean showUnitPrice() {
		if (properties.has("showUnitPrice") && !properties.get("showUnitPrice").isJsonNull()) {
			return properties.get("showUnitPrice").getAsBoolean();
		}
		return false;
	}

	public boolean showProductionArea() {
		if (properties.has("showProductionArea") && !properties.get("showProductionArea").isJsonNull()) {
			return properties.get("showProductionArea").getAsBoolean();
		}
		return false;
	}
}
