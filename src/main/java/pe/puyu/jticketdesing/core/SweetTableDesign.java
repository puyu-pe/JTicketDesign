package pe.puyu.jticketdesing.core;

import com.github.anastaciocintra.escpos.EscPos;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import pe.puyu.jticketdesing.metadata.PrinterPropertiesReader;
import pe.puyu.jticketdesing.util.JsonUtil;
import pe.puyu.jticketdesing.util.escpos.EscPosWrapper;
import pe.puyu.jticketdesing.util.escpos.StyleWrapper;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class SweetTableDesign {

	private final JsonObject data;
	private final PrinterPropertiesReader properties;
	private EscPos escpos;

	public SweetTableDesign(JsonObject data) {
		this.data = data;
		this.properties = new PrinterPropertiesReader(data);
	}

	public byte[] getBytes() {
		try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
			// Important!! initialize escpos, because it is not initialized in the constructor
			this.escpos = new EscPos(buffer);
			this.escpos.setCharacterCodeTable(this.properties.charCodeTable());

			designWith(escpos);

			return buffer.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException("SweetTableDesign Exception.", e);
		}
	}

	private void designWith(EscPos escpos) throws Exception {
		JsonArray tables = this.data.get("tables").getAsJsonArray();

		titleAndDetailsLayout();

		for (JsonElement table : tables) {
			JsonObject tableObject = table.getAsJsonObject();
			int numberOfColumns = calcNumberOfColumns(tableObject);

			JsonArray headers = tableObject.get("headers").getAsJsonArray();
			JsonArray body = tableObject.get("body").getAsJsonArray();
			JsonArray footer = tableObject.get("footer").getAsJsonArray();

			titleTableLayout(tableObject);
			headersTableLayout(headers, numberOfColumns);
			printLine();
			bodyTableLayout(body, getColumnBodyAlignFrom(headers), numberOfColumns);
			printLine();
			footerTableLayout(footer, numberOfColumns);
		}
	}

	private void titleAndDetailsLayout() throws Exception {
		EscPosWrapper escPosWrapper = new EscPosWrapper(this.escpos, StyleWrapper.textBold());
		String[] keys = {"title", "details"};
		for(String key : keys){
			if(key.equalsIgnoreCase("details")){
				escPosWrapper.removeStyleBold();
			}
			if (this.data.has(key) && !this.data.get(key).isJsonNull()) {
				JsonElement value = this.data.get(key);
				JsonArray values = JsonUtil.normalizeToJsonArray(value);
				for (JsonElement element : values) {
					escPosWrapper.toCenter(element.getAsString(), properties.width());
				}
			}
		}
	}

	private int calcNumberOfColumns(JsonObject table) {
		JsonArray headers = table.get("headers").getAsJsonArray();
		JsonArray body = table.get("body").getAsJsonArray();
		int max = headers.size();
		for(JsonElement element : body){
			JsonArray row = element.getAsJsonArray();
			if(row.size() > max)
				max = row.size();
		}
		return max;
	}

	private void titleTableLayout(JsonObject table) {

	}

	private void headersTableLayout(JsonArray headers, int numberOfColumns) {

	}

	private Map<Integer, BodyAlign> getColumnBodyAlignFrom(JsonArray headers) {
		return new HashMap<>();
	}

	private void bodyTableLayout(JsonArray body, Map<Integer, BodyAlign> bodyAlign, int numberOfColumns) {

	}

	private void footerTableLayout(JsonArray footer, int numberOfColumns) {

	}

	private void printLine() {
		EscPosWrapper wrapper = new EscPosWrapper(escpos);
		try {
			wrapper.printLine('-', properties.width());
		} catch (Exception ignored) {
		}
	}
}


enum BodyAlign {
	CENTER("CENTER"),
	LEFT("LEFT"),
	RIGHT("RIGHT");

	private final String value;

	BodyAlign(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public static BodyAlign fromValue(String value) {
		for (BodyAlign type : BodyAlign.values()) {
			if (type.value.equals(value)) {
				return type;
			}
		}
		throw new IllegalArgumentException(String.format("BodyAlign %s, not supported", value));
	}
}