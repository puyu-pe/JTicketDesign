package pe.puyu.jticketdesing.core;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.Style.FontSize;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import pe.puyu.jticketdesing.metadata.PrinterPropertiesReader;
import pe.puyu.jticketdesing.util.JsonUtil;
import pe.puyu.jticketdesing.util.StringUtils;
import pe.puyu.jticketdesing.util.escpos.EscPosWrapper;
import pe.puyu.jticketdesing.util.escpos.StyleWrapper;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

			JsonArray headers = Optional.ofNullable(tableObject.get("headers")).orElseGet(JsonArray::new).getAsJsonArray();
			JsonArray body = Optional.ofNullable(tableObject.get("body")).orElseGet(JsonArray::new).getAsJsonArray();
			JsonArray footer = Optional.ofNullable(tableObject.get("footer")).orElseGet(JsonArray::new).getAsJsonArray();

			// layers
			headers = normalizeHeaders(headers);      // standard objects
			headers = calcWidthHeaders(headers);      // characters per line
			headers = normalizeWidthHeaders(headers); // ensure cover all width

			titleTableLayout(tableObject);
			headersTableLayout(headers, numberOfColumns);
			printLine();
			bodyTableLayout(body, headers, numberOfColumns);
			printLine();
			footerTableLayout(footer, numberOfColumns);
		}
	}

	private void titleAndDetailsLayout() throws Exception {
		EscPosWrapper escPosWrapper = new EscPosWrapper(this.escpos, StyleWrapper.textBold());
		if (this.data.has("title") && !this.data.get("title").isJsonNull()) {
			JsonElement value = this.data.get("title");
			JsonArray values = JsonUtil.normalizeToJsonArray(value);
			for (JsonElement element : values) {
				var lines = StringUtils.wrapText(element.getAsString(), properties.width(), 1);
				for (String line : lines) {
					escPosWrapper.toCenter(line, properties.width());
				}
			}
		}
		escPosWrapper.removeStyleBold();
		escPosWrapper.printLine(' ', 1);
		if (this.data.has("details") && !this.data.get("details").isJsonNull()) {
			JsonElement value = this.data.get("details");
			JsonArray values = JsonUtil.normalizeToJsonArray(value);
			for (JsonElement element : values) {
				var lines = StringUtils.wrapText(element.getAsString(), properties.width(), 1);
				for (String line : lines) {
					escPosWrapper.toLeft(line, properties.width());
				}
			}
		}
		escPosWrapper.printLine(' ', 1);
	}

	private int calcNumberOfColumns(JsonObject table) {
		JsonArray headers = Optional.ofNullable(table.get("headers")).orElseGet(JsonArray::new).getAsJsonArray();
		JsonArray body = Optional.ofNullable(table.get("body")).orElseGet(JsonArray::new).getAsJsonArray();
		int max = headers.getAsJsonArray().size();
		for (JsonElement element : body) {
			JsonArray row = element.getAsJsonArray();
			if (row.size() > max)
				max = row.size();
		}
		return max;
	}

	private void titleTableLayout(JsonObject table) throws Exception {
		if (!table.has("title")) {
			return;
		}
		JsonObject defaults = new JsonObject();
		defaults.addProperty("align", "center");
		defaults.addProperty("text", ""); // default value
		defaults.addProperty("fontSize", 1);
		JsonObject title = JsonUtil.normalizeToJsonObject(table.get("title"), "text", "", defaults);

		CellAlign align = CellAlign.fromValue(title.get("align").getAsString()); // on error fromValue return LEFT
		FontSize fontSize = StyleWrapper.toFontSize(title.get("fontSize").getAsInt()); // on error toFontSize return 1
		String text = title.get("text").getAsString();

		EscPosWrapper escPosWrapper = new EscPosWrapper(escpos, StyleWrapper.textBold());

		switch (align) {
			case RIGHT:
				escPosWrapper.toRight(text, properties.width(), fontSize);
				break;
			case CENTER:
				escPosWrapper.toCenter(text, properties.width(), fontSize);
				break;
			default: // LEFT or others
				escPosWrapper.toLeft(text, properties.width(), fontSize);
		}
	}

	private void headersTableLayout(JsonArray headers, int numberOfColumns) {
		EscPosWrapper escPosWrapper = new EscPosWrapper(escpos, StyleWrapper.textBold());


		for (JsonElement element : headers) {
			JsonObject header = element.getAsJsonObject();
		}
	}

	private JsonArray normalizeHeaders(JsonArray headers) {
		JsonObject defaults = new JsonObject();
		defaults.addProperty("align", "center");
		defaults.addProperty("text", ""); // default value
		defaults.addProperty("bodyAlign", "left");
		headers = JsonUtil.map(headers, element -> JsonUtil.normalizeToJsonObject(element, "text", "", defaults));
		final int notSetWidth = JsonUtil.filter(headers,
			element -> {
				JsonObject header = element.getAsJsonObject();
				return !header.has("widthPercentage") || header.get("widthPercentage").isJsonNull();
			}).size();

		final int sizeHeaders = headers.size();
		if (notSetWidth > 0) {
			headers = JsonUtil.map(headers, element -> {
				JsonObject header = element.getAsJsonObject();
				if (!header.has("widthPercentage") || header.get("widthPercentage").isJsonNull()) {
					header.addProperty("widthPercentage", sizeHeaders / notSetWidth);
				}
				return header;
			});
		}
		return headers;
	}

	// calculate characters per line (width) for each header
	private JsonArray calcWidthHeaders(JsonArray headers) {
		return JsonUtil.map(headers, element -> {
			JsonObject jsonObject = element.getAsJsonObject();
			int widthPercentage = jsonObject.get("widthPercentage").getAsInt();
			jsonObject.addProperty("width", properties.width() * widthPercentage / 100);
			return jsonObject;
		});
	}

	private JsonArray normalizeWidthHeaders(JsonArray headers) {
		int accumulatedWidth = JsonUtil.reduce(
			headers,
			(currentValue, currentElement) -> currentValue + currentElement.getAsJsonObject().get("width").getAsInt(),
			0
		);
		final int maxWidth = JsonUtil.reduce(
			headers,
			(currenValue, currentElement) -> {
				JsonObject jsonObject = currentElement.getAsJsonObject();
				if (jsonObject.get("width").getAsInt() > currenValue)
					return jsonObject.get("width").getAsInt();
				return currenValue;
			}, -1);

		if (accumulatedWidth > properties.width()) {
			int freeWidth = accumulatedWidth - properties.width();
			return JsonUtil.findOneAndApply(
				headers,
				element -> element.getAsJsonObject().get("width").getAsInt() == maxWidth,
				element -> {
					JsonObject jsonObject = element.getAsJsonObject();
					int currentWidth = jsonObject.get("width").getAsInt();
					jsonObject.addProperty("width", currentWidth + freeWidth);
					return jsonObject;
				});
		} else {
			int overWidth = properties.width() - accumulatedWidth;
			return JsonUtil.findOneAndApply(
				headers,
				element -> element.getAsJsonObject().get("width").getAsInt() == maxWidth,
				element -> {
					JsonObject jsonObject = element.getAsJsonObject();
					int currentWidth = jsonObject.get("width").getAsInt();
					jsonObject.addProperty("width", currentWidth - overWidth);
					return jsonObject;
				});
		}
	}

	private void bodyTableLayout(JsonArray body, JsonArray headers, int numberOfColumns) {

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


enum CellAlign {
	CENTER("CENTER"),
	LEFT("LEFT"),
	RIGHT("RIGHT");

	private final String value;

	CellAlign(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public static CellAlign fromValue(String value) {
		for (CellAlign type : CellAlign.values()) {
			if (type.value.equalsIgnoreCase(value)) {
				return type;
			}
		}
		return LEFT;
	}
}