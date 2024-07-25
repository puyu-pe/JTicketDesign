package pe.puyu.jticketdesing.application.maker.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;
import pe.puyu.jticketdesing.domain.inputpayload.*;
import pe.puyu.jticketdesing.domain.maker.DesignObjectMaker;
import pe.puyu.jticketdesing.domain.maker.DesignObjectMakerException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GsonDesignObjectMaker implements DesignObjectMaker {

    private final JsonObject designObject;

    public GsonDesignObjectMaker(@NotNull String jsonString) {
        this(JsonParser.parseString(jsonString).getAsJsonObject());
    }

    public GsonDesignObjectMaker(@NotNull JsonObject jsonObject) {
        this.designObject = jsonObject;
    }

    @Override
    public @NotNull PrinterDesignObject build() {
        try {
            PrinterDesignProperties properties = buildPrinterDesignProperties();
            List<PrinterDesignBlock> blocks = buildPrinterDesignBlocks();
            return new PrinterDesignObject(properties, blocks);
        } catch (Exception e) {
            throw new DesignObjectMakerException(String.format("GsonDesignObjectMaker throw an exception: %s", e.getMessage()), e);
        }
    }

    private PrinterDesignProperties buildPrinterDesignProperties() {
        if (designObject.has("properties") && designObject.get("properties").isJsonObject()) {
            GsonObject properties = new GsonObject(designObject.get("properties").getAsJsonObject());
            return new PrinterDesignProperties(
                properties.getInt("blockWidth"),
                properties.getBoolean("normalize"),
                properties.getString("charCode")
            );
        }
        return null;
    }

    private List<PrinterDesignBlock> buildPrinterDesignBlocks() {
        if (designObject.has("data")) {
            List<PrinterDesignBlock> blocks = new LinkedList<>();
            JsonElement element = designObject.get("data");
            if (element.isJsonObject()) {
                blocks.add(castBlock(element));
            } else if (element.isJsonArray()) {
                element.getAsJsonArray().forEach(block -> blocks.add(castBlock(block)));
            }
            return blocks;
        }
        return null;
    }

    private PrinterDesignBlock castBlock(JsonElement element) {
        if (element.isJsonPrimitive()) {
            PrinterDesignCell cell = new PrinterDesignCell(null, element.getAsString());
            List<PrinterDesignCell> row = new LinkedList<>();
            row.add(cell);
            List<List<PrinterDesignCell>> rows = new LinkedList<>();
            rows.add(row);
            return new PrinterDesignBlock(
                null,
                null,
                null,
                null,
                null,
                null,
                rows
            );
        } else if (element.isJsonObject()) {
            GsonObject block = new GsonObject(element.getAsJsonObject());
            return new PrinterDesignBlock(
                block.getInt("gap"),
                block.getCharacter("separator"),
                block.getString("stringQr"),
                block.getString("imgPath"),
                block.getInt("nColumns"),
                buildPrinterDesignStyles(block.getElement("styles")),
                buildRows(block.getElement("rows"))
            );
        }
        return null;
    }

    private Map<String, PrinterDesignStyle> buildPrinterDesignStyles(@NotNull JsonElement styleElement) {
        if (styleElement.isJsonObject()) {
            Map<String, PrinterDesignStyle> styles = new HashMap<>();
            styleElement.getAsJsonObject().asMap().forEach((key, element) -> styles.put(key, castStyle(element)));
            return styles;
        }
        return null;
    }

    private PrinterDesignStyle castStyle(@NotNull JsonElement element) {
        if (element.isJsonObject()) {
            GsonObject style = new GsonObject(element.getAsJsonObject());
            return new PrinterDesignStyle(
                style.getInt("fontWidth"),
                style.getInt("fontHeight"),
                style.getBoolean("bold"),
                style.getBoolean("normalize"),
                style.getBoolean("bgInverted"),
                style.getCharacter("pad"),
                PrinterJustifyAlign.fromValue(style.getString("align")),
                style.getInt("span")
            );
        }
        return null;
    }

    private List<List<PrinterDesignCell>> buildRows(@NotNull JsonElement element) {
        if (element.isJsonArray()) {
            List<List<PrinterDesignCell>> rows = new LinkedList<>();
            element.getAsJsonArray().forEach(row -> rows.add(castRow(row)));
            return rows;
        }
        return null;
    }

    private List<PrinterDesignCell> castRow(@NotNull JsonElement element) {
        List<PrinterDesignCell> row = new LinkedList<>();
        if (element.isJsonPrimitive()) {
            PrinterDesignCell cell = new PrinterDesignCell(null, element.getAsString());
            row.add(cell);
            return row;
        } else if (element.isJsonObject()) {
            GsonObject cell = new GsonObject(element.getAsJsonObject());
            PrinterDesignCell printerDesignCell = new PrinterDesignCell(cell.getString("className"), cell.getString("text"));
            row.add(printerDesignCell);
            return row;
        } else if (element.isJsonArray()) {
            element.getAsJsonArray().forEach(item -> row.addAll(castRow(item)));
        }
        return row;
    }

}