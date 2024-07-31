package pe.puyu.jticketdesing.application.maker.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pe.puyu.jticketdesing.domain.inputs.block.*;
import pe.puyu.jticketdesing.domain.inputs.PrinterDesignObject;
import pe.puyu.jticketdesing.domain.inputs.drawer.PrinterDesignOpenDrawer;
import pe.puyu.jticketdesing.domain.inputs.drawer.PrinterPinConnector;
import pe.puyu.jticketdesing.domain.inputs.properties.PrinterCutMode;
import pe.puyu.jticketdesing.domain.inputs.properties.PrinterDesignCut;
import pe.puyu.jticketdesing.domain.inputs.properties.PrinterDesignProperties;
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
            PrinterDesignOpenDrawer openDrawer = buildPrinterOpenDrawer();
            return new PrinterDesignObject(properties, blocks, openDrawer);
        } catch (Exception e) {
            throw new DesignObjectMakerException(String.format("GsonDesignObjectMaker throw an exception: %s", e.getMessage()), e);
        }
    }

    private @Nullable PrinterDesignProperties buildPrinterDesignProperties() {
        if (designObject.has("properties") && designObject.get("properties").isJsonObject()) {
            GsonObject properties = new GsonObject(designObject.get("properties").getAsJsonObject());
            return new PrinterDesignProperties(
                properties.getInt("blockWidth"),
                properties.getBoolean("normalize"),
                properties.getString("charCode"),
                buildPrinterCutModeProperty(properties.getElement("cut"))
            );
        }
        return null;
    }

    private @Nullable List<PrinterDesignBlock> buildPrinterDesignBlocks() {
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

    private @Nullable PrinterDesignBlock castBlock(JsonElement element) {
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

    private @Nullable Map<String, PrinterDesignStyle> buildPrinterDesignStyles(@NotNull JsonElement styleElement) {
        if (styleElement.isJsonObject()) {
            Map<String, PrinterDesignStyle> styles = new HashMap<>();
            styleElement.getAsJsonObject().asMap().forEach((key, element) -> styles.put(key, castStyle(element)));
            return styles;
        }
        return null;
    }

    private @Nullable PrinterDesignStyle castStyle(@NotNull JsonElement element) {
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
                style.getInt("span"),
                ScaleType.fromValue(style.getString("scale")),
                style.getInt("width"),
                style.getInt("height")
            );
        }
        return null;
    }

    private @Nullable List<List<PrinterDesignCell>> buildRows(@NotNull JsonElement element) {
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

    private @Nullable PrinterDesignCut buildPrinterCutModeProperty(@NotNull JsonElement element) {
        if (element.isJsonObject()) {
            GsonObject cut = new GsonObject(element.getAsJsonObject());
            return new PrinterDesignCut(
                cut.getInt("feed"),
                PrinterCutMode.fromValue(cut.getString("mode"))
            );
        }
        return null;
    }

    private @Nullable PrinterDesignOpenDrawer buildPrinterOpenDrawer() {
        if (designObject.has("openDrawer") && designObject.get("openDrawer").isJsonObject()) {
            GsonObject openDrawer = new GsonObject(designObject.get("openDrawer").getAsJsonObject());
            return new PrinterDesignOpenDrawer(
                PrinterPinConnector.fromValue(openDrawer.getInt("pin")),
                openDrawer.getInt("t1"),
                openDrawer.getInt("t2")
            );
        }
        return null;
    }


}
