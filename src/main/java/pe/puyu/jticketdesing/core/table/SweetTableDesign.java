package pe.puyu.jticketdesing.core.table;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.Style.FontSize;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import pe.puyu.jticketdesing.core.DesignerHelper;
import pe.puyu.jticketdesing.metadata.PrinterPropertiesReader;
import pe.puyu.jticketdesing.util.JsonUtil;
import pe.puyu.jticketdesing.util.StringUtils;
import pe.puyu.jticketdesing.util.escpos.*;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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
			designTable();
			return buffer.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException("SweetTableDesign Exception.", e);
		}
	}

	private void designTable() throws Exception {
		JsonArray tables = this.data.get("tables").getAsJsonArray();
		titleAndDetailsLayout();
		for (JsonElement table : tables) {
			JsonObject tableObject = table.getAsJsonObject();
			int numberOfColumns = calcNumberOfColumns(tableObject);

			// ##### calcular los anchos de celda #########
			JsonArray widthPercents = new JsonArray();
			if (tableObject.has("widthPercents") && tableObject.get("widthPercents").isJsonArray()) {
				widthPercents = tableObject.get("widthPercents").getAsJsonArray();
			}
			List<Integer> listWidthPercents = fillMissingWidthPercents(widthPercents, numberOfColumns);
			listWidthPercents = calculateWidthPercent(listWidthPercents);
			List<Integer> cellWidths = calculateCellWidths(listWidthPercents);

			JsonArray headers = Optional.ofNullable(tableObject.get("headers")).orElseGet(JsonArray::new).getAsJsonArray();
			JsonArray footer = Optional.ofNullable(tableObject.get("footer")).orElseGet(JsonArray::new).getAsJsonArray();

			// ##### normalizar encabezados por capas #########
			headers = fillMissingColumns(headers, cellWidths.size(), getDefaultHeaderProperties());
			headers = normalizeHeaders(headers);      // standard objects

			titleTableLayout(tableObject);
			headersTableLayout(headers, cellWidths);
			bodyTableLayout(tableObject, cellWidths);
			footerTableLayout(footer, cellWidths);
		}
		helper.paperCut(escpos);
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
		body = JsonUtil.filter(body, JsonElement::isJsonArray);
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
		printBoldLine();
		escPosWrapper.printText(text, helper.properties().width(), styleText);
		printBoldLine();
	}

	private void headersTableLayout(JsonArray headers, List<Integer> cellWidths) throws Exception {
		// construyendo los table cell
		if (headers.isEmpty())
			return;
		List<TableCell> row = JsonUtil.mapToList(headers, (currentIndex, currentElement) -> {
			JsonObject header = currentElement.getAsJsonObject();
			String text = header.get("text").getAsString();
			int width = cellWidths.get(currentIndex);
			String align = header.get("align").getAsString();
			StyleText styleText = helper.styleNormalizeBuilder().align(align).bold(true).build();
			return new TableCell(text, width, styleText);
		});
		// imprimir la fila de table cells
		printRow(row);
		printLine();
	}

	private void bodyTableLayout(JsonObject tableObject, List<Integer> cellWidths) throws Exception {
		JsonArray body = Optional.ofNullable(tableObject.get("body")).orElseGet(JsonArray::new).getAsJsonArray();
		body = normalizeBody(body); //normalize to list of standard objects or json array
		if (cellWidths.isEmpty()) return;
		EscPosWrapper escPosWrapper = new EscPosWrapper(escpos);
		for (int i = 0; i < body.size(); ++i) {
			JsonElement element = body.get(i);
			if (element.isJsonObject()) {
				JsonObject subtitle = element.getAsJsonObject();
				String text = subtitle.get("text").getAsString();
				String align = subtitle.get("align").getAsString();
				boolean bold = subtitle.get("bold").getAsBoolean();
				StyleText styleText = helper.styleNormalizeBuilder().align(align).bold(bold).build();
				if (text.length() == 1) {
					escPosWrapper.printLine(text.charAt(0), helper.properties().width(), styleText);
					continue;
				}
				escPosWrapper.printText(text, helper.properties().width(), styleText);
			} else if (element.isJsonArray()) {
				JsonArray row = element.getAsJsonArray();
				row = fillMissingColumns(row, cellWidths.size(), new JsonPrimitive(""));
				List<TableCell> cells = JsonUtil.mapToList(row, (currentIndex, currentItem) -> {
					JsonObject defaultItem = new JsonObject();
					defaultItem.addProperty("text", "");
					JsonObject item = JsonUtil.normalizeToJsonObject(currentItem, "text", "", defaultItem);
					String text = item.get("text").getAsString();
					StyleText styleText = getCellBodyStyle(tableObject, currentIndex);
					if (item.has("align") && item.get("align").isJsonPrimitive()) {
						styleText = StyleText.builder(styleText).align(item.get("align").getAsString()).build();
					}
					int width = cellWidths.get(currentIndex);
					return new TableCell(text, width, styleText);
				});
				printRow(cells);
			}
		}
	}

	private void footerTableLayout(JsonArray footer, List<Integer> cellWidths) throws Exception {
		printBoldLine('-');
		EscPosWrapper escPosWrapper = new EscPosWrapper(escpos);
		int startCell = 0;
		List<TableCell> row = new LinkedList<>();
		for (int i = 0; i < footer.size(); ++i) {
			JsonElement element = footer.get(i);
			JsonObject footerItem = JsonUtil.normalizeToJsonObject(element, "text", "", getDefaultFooterProperties());
			int span = footerItem.get("span").getAsInt();
			int endCell = Math.min(startCell + span, cellWidths.size());
			int width = cellWidths.subList(startCell, endCell).stream().reduce(0, Integer::sum);
			String text = footerItem.get("text").getAsString();
			StyleText styleText = helper.styleNormalizeBuilder()
				.bold(footerItem.get("bold").getAsBoolean())
				.align(footerItem.get("align").getAsString())
				.build();
			row.add(new TableCell(text, width, styleText));
			startCell = endCell;
		}
		printRow(row);
		escPosWrapper.printLine(' ', helper.properties().width());
	}

	private JsonArray normalizeHeaders(JsonArray headers) {
		return JsonUtil.map(
			headers,
			element -> JsonUtil.normalizeToJsonObject(element, "text", "", getDefaultHeaderProperties())
		);
	}

	private JsonArray normalizeBody(JsonArray body) {
		JsonObject subtitleDefaultProperties = new JsonObject();
		subtitleDefaultProperties.addProperty("text", "");
		subtitleDefaultProperties.addProperty("align", "center");
		subtitleDefaultProperties.addProperty("bold", false);
		body = JsonUtil.map(body, row -> {
			if (row.isJsonArray()) return row;
			return JsonUtil.normalizeToJsonObject(row, "text", "", subtitleDefaultProperties);
		});
		return body;
	}

	private JsonArray fillMissingColumns(JsonArray array, int maxColumns, JsonElement defaultValue) {
		JsonArray result = new JsonArray();
		array.forEach(result::add);
		int missing = result.isEmpty() ? 0 : Math.max(maxColumns - result.size(), 0); // evitar llenar un array vacio
		for (int i = 0; i < missing; ++i) {
			result.add(defaultValue);
		}
		return result;
	}

	private JsonObject getDefaultFooterProperties() {
		JsonObject defaults = new JsonObject();
		defaults.addProperty("align", "left");
		defaults.addProperty("bold", false);
		defaults.addProperty("span", 1);
		return defaults;
	}

	private JsonObject getDefaultHeaderProperties() {
		JsonObject defaults = new JsonObject();
		defaults.addProperty("align", "center");
		defaults.addProperty("text", ""); // default value
		return defaults;
	}

	private List<Integer> fillMissingWidthPercents(JsonArray cellWidths, int maxNumberOfColumns) {
		List<Integer> widthPercentsArray = new ArrayList<>();
		for (int i = 0; i < maxNumberOfColumns; ++i) {
			int widthPercent = 0;
			if (i < cellWidths.size() && cellWidths.get(i).isJsonPrimitive()) {
				widthPercent = Math.max(0, cellWidths.get(i).getAsInt());
			}
			widthPercentsArray.add(widthPercent);
		}
		return widthPercentsArray;
	}


	private List<Integer> calculateWidthPercent(List<Integer> widthPercentsArray) {
		// asignar widthPercent a las celdas con ancho automatico (0)
		int numberOfCellsWithAutoWidth = (int) widthPercentsArray.stream().filter(widthPercent -> widthPercent <= 0).count();
		int coveredWidthPercent = Math.min(widthPercentsArray.stream().reduce(0, Integer::sum), 100);
		int uncoveredWidthPercent = Math.max(100 - coveredWidthPercent, 0);
		return widthPercentsArray.stream()
			.map(widthPercent -> {
				if (widthPercent == 0) {
					return uncoveredWidthPercent / numberOfCellsWithAutoWidth;
				}
				return widthPercent;
			})
			.toList();
	}

	private List<Integer> calculateCellWidths(List<Integer> widthPercentsArray) {
		int paperWidth = helper.properties().width();
		List<Integer> cellWidths = widthPercentsArray.stream()
			.map(widthPercent -> paperWidth * widthPercent / 100)
			.toList();
		final int totalCoveredWidth = cellWidths.stream().reduce(0, Integer::sum);
		final int maxWidth = cellWidths.stream().max(Integer::compare).orElse(0);
		boolean hasModify = false;
		List<Integer> result = new ArrayList<>();
		for (int width : cellWidths) {
			if (width == maxWidth && !hasModify) {
				result.add(width + (paperWidth - totalCoveredWidth));
				hasModify = true;
			} else {
				result.add(width);
			}
		}
		return result;
	}

	private StyleText getCellBodyStyle(JsonObject tableObject, int index) {
		JsonObject defaultCellBodyStyle = new JsonObject();
		defaultCellBodyStyle.addProperty("align", "left");
		defaultCellBodyStyle.addProperty("bold", false);
		StyleTextBuilder defaultStyle = helper.styleNormalizeBuilder()
			.bold(defaultCellBodyStyle.get("bold").getAsBoolean())
			.align(defaultCellBodyStyle.get("align").getAsString());
		if (!tableObject.has("cellBodyStyles")) {
			return defaultStyle.build();
		}
		JsonObject cellBodyStyles = tableObject.get("cellBodyStyles").getAsJsonObject();
		String key = String.valueOf(Math.max(index, 0));
		if (!cellBodyStyles.has(key)) {
			return defaultStyle.build();
		}
		JsonElement cellBodyStyle = cellBodyStyles.get(key);
		JsonObject styleObject = JsonUtil.normalizeToJsonObject(cellBodyStyle, "align", "left", defaultCellBodyStyle);
		return defaultStyle
			.align(styleObject.get("align").getAsString())
			.bold(styleObject.get("bold").getAsBoolean())
			.build();
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
				if (!isLastRow) {
					String separator = text.isEmpty() ? " " : "|";
					escPosWrapper.printText(separator, 1, helper.noFeedBuilder().build()); // imprimir espacio intermedio
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

	private void printBoldLine() {
		printBoldLine('*');
	}

	private void printBoldLine(char pad) {
		EscPosWrapper wrapper = new EscPosWrapper(escpos);
		try {
			wrapper.printLine(pad, helper.properties().width(), StyleText.builder().bold(true).build());
		} catch (Exception ignored) {
		}
	}
}


