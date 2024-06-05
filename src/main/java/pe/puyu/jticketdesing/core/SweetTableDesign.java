package pe.puyu.jticketdesing.core;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.Style.FontSize;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import pe.puyu.jticketdesing.metadata.PrinterPropertiesReader;
import pe.puyu.jticketdesing.util.JsonUtil;
import pe.puyu.jticketdesing.util.StringUtils;
import pe.puyu.jticketdesing.util.escpos.EscPosWrapper;
import pe.puyu.jticketdesing.util.escpos.StyleWrapper;

import java.io.ByteArrayOutputStream;
import java.util.*;

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
		if (numberOfColumns <= 0) {
			return;
		}
		headers = fillMissingColumns(headers, numberOfColumns, getDefaultHeaderProperties());
		var rowStrings = JsonUtil.map(headers, element -> {
			JsonObject jsonObject = element.getAsJsonObject();
			return new JsonPrimitive(jsonObject.get("text").getAsString());
		});
		List<List<String>> row = wrapRow(rowStrings);
		EscPosWrapper escPosWrapper = new EscPosWrapper(escpos, StyleWrapper.textBold());
		for (int j = 0; j < row.get(0).size(); j++) {
			for (int i = 0; i < row.size(); i++) {
				JsonObject header = headers.get(i).getAsJsonObject();
				String splitText = row.get(i).get(j);

			}
		}
	}

	private void bodyTableLayout(JsonArray body, JsonArray headers, int numberOfColumns) {

	}

	private void footerTableLayout(JsonArray footer, int numberOfColumns) {

	}

	// important!! rowString -> Array JsonPrimitives
	private List<List<String>> wrapRow(JsonArray rowStrings) {
		List<List<String>> rows = new LinkedList<>();
		int maxColumns = -1;
		for (JsonElement element : rowStrings) {
			List<String> wrapCell = StringUtils.wrapText(element.getAsString(), properties.width(), 1);
			rows.add(wrapCell);
			if (wrapCell.size() > maxColumns) {
				maxColumns = wrapCell.size();
			}
		}
		List<List<String>> result = new LinkedList<>();
		for (List<String> row : rows) {
			List<String> normalizeRow = new LinkedList<>(row);
			int missing = 0;
			if (normalizeRow.size() < maxColumns) {
				missing = Math.max(maxColumns - row.size(), 0);
			}
			for (int i = 0; i < missing; ++i) {
				normalizeRow.add("");
			}
			result.add(normalizeRow);
		}
		return result;
	}

	private JsonArray normalizeHeaders(JsonArray headers) {
		headers = JsonUtil.map(headers, element -> JsonUtil.normalizeToJsonObject(element, "text", "", getDefaultHeaderProperties()));
		final int notSetWidthPercentage = JsonUtil.filter(headers,
			element -> {
				JsonObject header = element.getAsJsonObject();
				return !header.has("widthPercentage") || header.get("widthPercentage").isJsonNull();
			}).size();

		final int sizeHeaders = headers.size();
		if (notSetWidthPercentage > 0) {
			headers = JsonUtil.map(headers, element -> {
				JsonObject header = element.getAsJsonObject();
				if (!header.has("widthPercentage") || header.get("widthPercentage").isJsonNull()) {
					header.addProperty("widthPercentage", sizeHeaders / notSetWidthPercentage);
				}
				return header;
			});
		}
		return headers;
	}

	private JsonArray fillMissingColumns(JsonArray array, int maxColumns, JsonElement defaultValue) {
		JsonArray result = new JsonArray();
		array.forEach(result::add);
		int missing = Math.max(maxColumns - result.size(), 0);
		for (int i = 0; i < missing; ++i) {
			result.add(defaultValue);
		}
		return result;
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
			int overWidth = Math.max(properties.width() - accumulatedWidth, 0);
			return JsonUtil.findOneAndApply(
				headers,
				element -> element.getAsJsonObject().get("width").getAsInt() == maxWidth,
				element -> {
					JsonObject jsonObject = element.getAsJsonObject();
					int currentWidth = jsonObject.get("width").getAsInt();
					jsonObject.addProperty("width", Math.max(currentWidth - overWidth, 0));
					return jsonObject;
				});
		}
	}

	private JsonObject getDefaultHeaderProperties() {
		JsonObject defaults = new JsonObject();
		defaults.addProperty("align", "center");
		defaults.addProperty("text", ""); // default value
		defaults.addProperty("bodyAlign", "left");
		return defaults;
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