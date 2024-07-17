package pe.puyu.jticketdesing.application.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import pe.puyu.jticketdesing.domain.inputpayload.*;
import pe.puyu.jticketdesing.domain.maker.DesignObjectMaker;
import pe.puyu.jticketdesing.domain.inputpayload.DesignDefaultValuesProvider;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GsonDesignObjectMaker implements DesignObjectMaker {

    private final JsonObject designObject;

    public GsonDesignObjectMaker(String jsonString) {
        this(JsonParser.parseString(jsonString).getAsJsonObject());
    }

    public GsonDesignObjectMaker(JsonObject jsonObject) {
        this.designObject = jsonObject;
    }

    @Override
    public PrinterDesignObject build(DesignDefaultValuesProvider defaultProvider) {
        PrinterDesignObject printerDesignObject = new PrinterDesignObject(defaultProvider.getDefaultDesignObject());
        PrinterDesignProperties printerDesignProperties = buildPrinterDesignProperties(defaultProvider.getDefaultDesignProperties());
        List<PrinterDesignBlock> printerDesignBlocks = buildPrinterDesignBlocks(
            defaultProvider.getDefaultBlockValues(),
            defaultProvider.getDefaultCellValues(),
            defaultProvider.getDefaultDesignStyle()
        );
        printerDesignObject.setProperties(printerDesignProperties);
        printerDesignObject.addData(printerDesignBlocks);
        return null;
    }

    private PrinterDesignProperties buildPrinterDesignProperties(PrinterDesignProperties defaultDesignProperties) {
        PrinterDesignProperties printerDesignProperties = new PrinterDesignProperties(defaultDesignProperties);
        if (designObject.has("properties")) {
            JsonElement propertiesElement = designObject.get("properties");
            if (propertiesElement.isJsonObject()) {
                GsonObject properties = new GsonObject(propertiesElement.getAsJsonObject());
                printerDesignProperties.setBlockWidth(properties.getInt("blockWidth", printerDesignProperties.getBlockWidth()));
                printerDesignProperties.setNormalize(properties.getBoolean("normalize", printerDesignProperties.isNormalize()));
                printerDesignProperties.setCharCodeTable(properties.getString("charCodeTable", printerDesignProperties.getCharCodeTable()));
            }
        }
        return printerDesignProperties;
    }

    private List<PrinterDesignBlock> buildPrinterDesignBlocks(
        PrinterDesignBlock defaultDesignBlock,
        PrinterDesignCell defaultCell,
        PrinterDesignStyle defaultStyle
    ) {
        List<PrinterDesignBlock> printerDesignBlocks = new LinkedList<>();
        if (designObject.has("data")) {
            JsonElement element = designObject.get("data");
            if (element.isJsonObject()) {
                printerDesignBlocks.add(castBlock(element, defaultDesignBlock, defaultCell, defaultStyle));
            } else if (element.isJsonArray()) {
                element.getAsJsonArray().forEach(block -> printerDesignBlocks.add(castBlock(block, defaultDesignBlock, defaultCell, defaultStyle)));
            }
        }
        return printerDesignBlocks;
    }

    private PrinterDesignBlock castBlock(
        JsonElement element,
        PrinterDesignBlock defaultDesignBlock,
        PrinterDesignCell defaultCell,
        PrinterDesignStyle defaultStyle
    ) {
        PrinterDesignBlock printerDesignBlock = new PrinterDesignBlock(defaultDesignBlock);
        if (element.isJsonPrimitive()) {
            PrinterDesignCell cell = new PrinterDesignCell(defaultCell);
            cell.setText(element.getAsString());
            List<PrinterDesignCell> row = new LinkedList<>();
            row.add(cell);
            printerDesignBlock.addRow(row);
        } else if (element.isJsonObject()) {
            GsonObject block = new GsonObject(element.getAsJsonObject());
            printerDesignBlock.setGap(block.getInt("gap", printerDesignBlock.getGap()));
            printerDesignBlock.setSeparator(block.getChar("separator", printerDesignBlock.getSeparator()));
            printerDesignBlock.setStringQR(block.getString("stringQR", printerDesignBlock.getStringQR()));
            printerDesignBlock.setImgPath(block.getString("imgPath", printerDesignBlock.getImgPath()));
            printerDesignBlock.addStyles(buildPrinterDesignStyles(block.getElement("styles"), defaultStyle));
            printerDesignBlock.setNColumns(block.getInt("nColumns", printerDesignBlock.getNColumns()));
            printerDesignBlock.addRows(buildRows(block.getElement("rows"), defaultCell));
        }
        return printerDesignBlock;
    }

    private Map<String, PrinterDesignStyle> buildPrinterDesignStyles(JsonElement styleElement, PrinterDesignStyle defaultStyle) {
        Map<String, PrinterDesignStyle> styles = new HashMap<>();
        if (styleElement.isJsonObject()) {
            styleElement.getAsJsonObject().asMap().forEach((key, element) -> styles.put(key, castStyle(element, defaultStyle)));
        }
        return styles;
    }

    private PrinterDesignStyle castStyle(JsonElement element, PrinterDesignStyle defaultStyle) {
        PrinterDesignStyle printerDesignStyle = new PrinterDesignStyle(defaultStyle);
        if (element.isJsonObject()) {
            GsonObject style = new GsonObject(element.getAsJsonObject());
            printerDesignStyle.setFontWidth(style.getInt("fontWidth", printerDesignStyle.getFontWidth()));
            printerDesignStyle.setFontHeight(style.getInt("fontHeight", printerDesignStyle.getFontHeight()));
            printerDesignStyle.setBold(style.getBoolean("bold", printerDesignStyle.isBold()));
            printerDesignStyle.setNormalize(style.getBoolean("normalize", printerDesignStyle.isNormalize()));
            printerDesignStyle.setBgInverted(style.getBoolean("bgInverted", printerDesignStyle.isBgInverted()));
            printerDesignStyle.setPad(style.getChar("pad", printerDesignStyle.getPad()));
            printerDesignStyle.setAlign(PrinterJustifyAlign.fromValue(style.getString("align", printerDesignStyle.getAlign().getValue())));
            printerDesignStyle.setSpan(style.getInt("span", printerDesignStyle.getSpan()));
        }
        return printerDesignStyle;
    }

    private List<List<PrinterDesignCell>> buildRows(JsonElement element, PrinterDesignCell defaultCell) {
        List<List<PrinterDesignCell>> rows = new LinkedList<>();
        if (element.isJsonArray()) {
            element.getAsJsonArray().forEach(row -> rows.add(castRow(row, defaultCell)));
        }
        return rows;
    }

    private List<PrinterDesignCell> castRow(JsonElement element, PrinterDesignCell defaultCell) {
        List<PrinterDesignCell> row = new LinkedList<>();
        if (element.isJsonPrimitive()) {
            PrinterDesignCell printerDesignCell = new PrinterDesignCell(defaultCell);
            printerDesignCell.setText(element.getAsString());
            row.add(printerDesignCell);
        } else if (element.isJsonObject()) {
            PrinterDesignCell printerDesignCell = new PrinterDesignCell(defaultCell);
            GsonObject cell = new GsonObject(element.getAsJsonObject());
            printerDesignCell.setText(cell.getString("text", printerDesignCell.getText()));
            printerDesignCell.setClassName(cell.getString("className", printerDesignCell.getClassName()));
            row.add(printerDesignCell);
        } else if (element.isJsonArray()) {
            element.getAsJsonArray().forEach(item -> row.addAll(castRow(item, defaultCell)));
        }
        return row;
    }

}
