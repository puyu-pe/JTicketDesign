package pe.puyu.SweetTicketDesign.domain.table;

import com.github.anastaciocintra.escpos.EscPos;
import com.google.gson.JsonObject;

@Deprecated(since = "0.1.0", forRemoval = true)
public class PrinterPropertiesReader {
	protected JsonObject properties;
	protected final JsonObject ticket;

	public PrinterPropertiesReader(JsonObject ticket) {
		this.ticket = ticket;
		JsonObject printer = new JsonObject();
		if (ticket.has("builder") && ticket.get("builder").isJsonObject()) {
			printer = ticket.getAsJsonObject("builder");
		}
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

	public int width() {
		if (properties.has("span") && !properties.get("span").isJsonNull()) {
			return properties.get("span").getAsInt();
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
