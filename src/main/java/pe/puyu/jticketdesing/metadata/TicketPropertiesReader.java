package pe.puyu.jticketdesing.metadata;

import java.util.Optional;

import org.json.JSONObject;

import com.github.anastaciocintra.escpos.EscPos.CharacterCodeTable;
import com.github.anastaciocintra.escpos.Style.FontSize;

import pe.puyu.jticketdesing.util.escpos.StyleWrapper;

public class TicketPropertiesReader {
	private JSONObject properties;
	private JSONObject ticket;

	public TicketPropertiesReader(JSONObject ticket) {
		this.ticket = ticket;
		var printer = ticket.getJSONObject("printer");
		properties = new JSONObject();
		if (printer.has("properties") && !printer.isNull("properties")) {
			properties = printer.getJSONObject("properties");
		}
	}

	public CharacterCodeTable charCodeTable() {
		try {
			if (properties.has("charCodeTable") && !properties.isNull("charCodeTable"))
				return CharacterCodeTable.valueOf(properties.getString("charCodeTable"));
			else
				return CharacterCodeTable.WPC1252;
		} catch (Exception e) {
			return CharacterCodeTable.WPC1252;
		}
	}

	public String charSetName() {
		try {
			if (properties.has("charSetName") && !properties.isNull("charSetName"))
				return properties.getString("charSetName");
			else
				return CharacterCodeTable.CP437_USA_Standard_Europe.charsetName;
		} catch (Exception e) {
			return CharacterCodeTable.CP437_USA_Standard_Europe.charsetName;
		}
	}

	public FontSize fontSizeCommand() {
		if (properties.has("fontSizeCommand") && !properties.isNull("fontSizeCommand")) {
			return StyleWrapper.toFontSize(properties.getInt("fontSizeCommand"));
		} else {
			return FontSize._2;
		}
	}

	public Optional<String> logoPath() {
		if (!ticket.has("metadata")) {
			return Optional.empty();
		}
		var metadata = ticket.getJSONObject("metadata");
		if (metadata.has("logoPath") && !metadata.isNull("logoPath"))
			return Optional.ofNullable(metadata.getString("logoPath"));
		return Optional.empty();
	}

	public String type() {
		if (ticket.has("type") && !ticket.isNull("type"))
			return ticket.getString("type");
		return "tipo de documento desconocido";
	}

	public int times() {
		if (ticket.has("times") && !ticket.isNull("times")) {
			return ticket.getInt("times");
		}
		return 1;
	}

	public int width() {
		if (properties.has("width") && !properties.isNull("width")) {
			return properties.getInt("width");
		}
		return 42;
	}

	public boolean backgroundInverted() {
		if (properties.has("backgroundInverted") && !properties.isNull("backgroundInverted")) {
			return properties.getBoolean("backgroundInverted");
		}
		return true;
	}

	public boolean nativeQR() {
		if (properties.has("nativeQR") && !properties.isNull("nativeQR"))
			return properties.getBoolean("nativeQR");
		return false;
	}

	public boolean textNormalize() {
		if (properties.has("textNormalize") && !properties.isNull("textNormalize")) {
			return properties.getBoolean("textNormalize");
		}
		return false;
	}

}
