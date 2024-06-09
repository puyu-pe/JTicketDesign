package pe.puyu.jticketdesing.core.table;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.Style.FontSize;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import pe.puyu.jticketdesing.core.DesignerHelper;
import pe.puyu.jticketdesing.metadata.PrinterPropertiesReader;
import pe.puyu.jticketdesing.util.JsonUtil;
import pe.puyu.jticketdesing.util.StringUtils;
import pe.puyu.jticketdesing.util.escpos.EscPosWrapper;
import pe.puyu.jticketdesing.util.escpos.JustifyAlign;
import pe.puyu.jticketdesing.util.escpos.StyleEscPosUtil;
import pe.puyu.jticketdesing.util.escpos.StyleText;

import java.io.ByteArrayOutputStream;
import java.util.*;

public class SweetTableDesign {

	private final JsonObject data;
	private EscPos escpos;
	private final DesignerHelper<PrinterPropertiesReader> helper;

	public SweetTableDesign(JsonObject data) {
		this.data = data;
		this.helper = new DesignerHelper<>(new PrinterPropertiesReader(data));
	}

	public byte[] getBytes() {
		try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
			// Important!! initialize escpos, because it is not initialized in the constructor
			this.escpos = new EscPos(buffer);
			this.escpos.setCharacterCodeTable(helper.properties().charCodeTable());

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
		EscPosWrapper escPosWrapper = new EscPosWrapper(this.escpos);
		StyleText titleStyle = helper.styleNormalizeBuilder().bold(true).align(JustifyAlign.CENTER).build();
		StyleText detailsStyle = helper.styleNormalizeBuilder().align(JustifyAlign.LEFT).build();
		String[] fields = {"title", "details"};
		for (String field : fields) {
			StyleText style = field.equalsIgnoreCase("title") ? titleStyle : detailsStyle;
			if (this.data.has(field) && !this.data.get(field).isJsonNull()) {
				JsonElement value = this.data.get(field);
				JsonArray values = JsonUtil.normalizeToJsonArray(value);
				for (JsonElement element : values) {
					var lines = StringUtils.wrapText(element.getAsString(), helper.properties().width(), 1);
					for (String line : lines) {
						escPosWrapper.printText(line, helper.properties().width(), style);
					}
				}
			}
			escPosWrapper.printLine(' ', 1);
		}
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
		JustifyAlign align = JustifyAlign.fromValue(title.get("align").getAsString()); // on error fromValue return LEFT
		FontSize fontSize = StyleEscPosUtil.toFontSize(title.get("fontSize").getAsInt()); // on error toFontSize return 1
		String text = title.get("text").getAsString();
		EscPosWrapper escPosWrapper = new EscPosWrapper(escpos);
		StyleText styleText = helper.styleNormalizeBuilder()
			.fontSize(fontSize)
			.align(align)
			.bold(true)
			.build();
		escPosWrapper.printText(text, helper.properties().width(), styleText);
	}

	private void headersTableLayout(JsonArray headers, int numberOfColumns) throws Exception {
		if (numberOfColumns <= 0) {
			return;
		}
		headers = fillMissingColumns(headers, numberOfColumns, getDefaultHeaderProperties());
		List<TableCell> row = JsonUtil.mapToList(headers, (element) -> {
			JsonObject header = element.getAsJsonObject();
			String text = header.get("text").getAsString();
			int width = header.get("width").getAsInt();
			String align = header.get("align").getAsString();
			StyleText styleText = helper.styleNormalizeBuilder().align(align).bold(true).build();
			return new TableCell(text, width, styleText);
		});
		printRow(row);
	}

	private void bodyTableLayout(JsonArray body, JsonArray headers, int numberOfColumns) {

	}

	private void footerTableLayout(JsonArray footer, int numberOfColumns) {

	}

	private JsonArray normalizeHeaders(JsonArray headers) {
		headers = JsonUtil.map(headers, element -> JsonUtil.normalizeToJsonObject(element, "text", "", getDefaultHeaderProperties()));
		final int notSetWidthPercentage = JsonUtil.filter(headers,
			element -> {
				JsonObject header = element.getAsJsonObject();
				return !header.has("widthPercentage") || header.get("widthPercentage").isJsonNull();
			}).size();

		final int percentageWidthCovered = Math.min(JsonUtil.reduce(
			headers,
			(currentValue, currentElement) -> {
				JsonObject header = currentElement.getAsJsonObject();
				var widthPercentage = Optional.ofNullable(header.get("widthPercentage"));
				return widthPercentage.map(jsonElement -> jsonElement.getAsInt() + currentValue).orElse(currentValue);
			},
			0), 100);

		final int percentageWidthNotCovered = 100 - percentageWidthCovered;

		if (notSetWidthPercentage > 0) {
			headers = JsonUtil.map(headers, element -> {
				JsonObject header = element.getAsJsonObject();
				if (!header.has("widthPercentage") || header.get("widthPercentage").isJsonNull()) {
					header.addProperty("widthPercentage", percentageWidthNotCovered / notSetWidthPercentage);
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
			jsonObject.addProperty("width", helper.properties().width() * widthPercentage / 100);
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

		if (accumulatedWidth > helper.properties().width()) {
			int freeWidth = accumulatedWidth - helper.properties().width();
			return JsonUtil.findOneAndApply(
				headers,
				element -> element.getAsJsonObject().get("width").getAsInt() == maxWidth,
				element -> {
					JsonObject jsonObject = element.getAsJsonObject();
					int currentWidth = jsonObject.get("width").getAsInt();
					jsonObject.addProperty("width", currentWidth - freeWidth);
					return jsonObject;
				});
		} else {
			int overWidth = Math.max(helper.properties().width() - accumulatedWidth, 0);
			return JsonUtil.findOneAndApply(
				headers,
				element -> element.getAsJsonObject().get("width").getAsInt() == maxWidth,
				element -> {
					JsonObject jsonObject = element.getAsJsonObject();
					int currentWidth = jsonObject.get("width").getAsInt();
					jsonObject.addProperty("width", Math.max(currentWidth + overWidth, 0));
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

	private void printRow(List<TableCell> row) throws Exception {
		List<List<TableCell>> matrix = new LinkedList<>();
		int maxNumberOfCuts = -1;
		for (int i = 0; i < row.size(); ++i) {
			boolean isLastRow = i + 1 == row.size();
			TableCell cell = row.get(i);
			List<TableCell> splitCells = new LinkedList<>();
			int width = !isLastRow ? cell.width() - 1 : cell.width(); // ancho reducido para los espacios intermedios
			List<String> splits = StringUtils.wrapText(cell.text(), width, StyleEscPosUtil.valueFontSize(cell.style().getFontWidth()));
			maxNumberOfCuts = Math.max(splits.size(), maxNumberOfCuts);
			for (String split : splits) {
				splitCells.add(new TableCell(split, cell.width(), cell.style().copy()));
			}
			matrix.add(splitCells);
		}
		// print matrix
		EscPosWrapper escPosWrapper = new EscPosWrapper(escpos);
		for (int j = 0; j < maxNumberOfCuts; j++) {
			for (int i = 0; i < matrix.size(); ++i) {
				boolean isLastRow = i + 1 == row.size(); // matrix.size() == row.size()
				List<TableCell> splits = matrix.get(i);
				boolean existsElement = j < splits.size();
				TableCell cell = existsElement ? splits.get(j) : splits.get(0);
				int width = !isLastRow ? cell.width() - 1 : cell.width(); // ancho reducido para los espacios intermedios
				String text = existsElement ? cell.text() : "";
				StyleText styleText = StyleText.builder(cell.style())
					.feed(isLastRow)
					.build();
				escPosWrapper.printText(text, width, styleText);
				if(!isLastRow){
					escPosWrapper.printText(" ", 1, helper.noFeedBuilder().build()); // imprimir espacio intermedio
				}
			}
		}

	}

	private void printLine() {
		EscPosWrapper wrapper = new EscPosWrapper(escpos);
		try {
			wrapper.printLine('-', helper.properties().width());
		} catch (Exception ignored) {
		}
	}
}


