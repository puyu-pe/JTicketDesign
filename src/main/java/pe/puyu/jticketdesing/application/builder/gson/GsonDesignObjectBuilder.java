package pe.puyu.jticketdesing.application.builder.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pe.puyu.jticketdesing.domain.components.block.*;
import pe.puyu.jticketdesing.domain.components.SweetPrinterObjectComponent;
import pe.puyu.jticketdesing.domain.components.drawer.SweetOpenDrawerComponent;
import pe.puyu.jticketdesing.domain.components.drawer.SweetPinConnector;
import pe.puyu.jticketdesing.domain.components.properties.SweetCutMode;
import pe.puyu.jticketdesing.domain.components.properties.SweetCutComponent;
import pe.puyu.jticketdesing.domain.components.properties.SweetPropertiesComponent;
import pe.puyu.jticketdesing.domain.builder.SweetDesignObjectBuilder;
import pe.puyu.jticketdesing.domain.builder.DesignObjectBuilderException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GsonDesignObjectBuilder implements SweetDesignObjectBuilder {

    private final JsonObject designObject;

    public GsonDesignObjectBuilder(@NotNull String jsonString) {
        this(JsonParser.parseString(jsonString).getAsJsonObject());
    }

    public GsonDesignObjectBuilder(@NotNull JsonObject jsonObject) {
        this.designObject = jsonObject;
    }

    @Override
    public @NotNull SweetPrinterObjectComponent build() {
        try {
            SweetPropertiesComponent properties = buildPrinterDesignProperties();
            List<SweetBlockComponent> blocks = buildPrinterDesignBlocks();
            SweetOpenDrawerComponent openDrawer = buildPrinterOpenDrawer();
            return new SweetPrinterObjectComponent(properties, blocks, openDrawer);
        } catch (Exception e) {
            throw new DesignObjectBuilderException(String.format("GsonDesignObjectBuilder throw an exception: %s", e.getMessage()), e);
        }
    }

    private @Nullable SweetPropertiesComponent buildPrinterDesignProperties() {
        if (designObject.has("properties") && designObject.get("properties").isJsonObject()) {
            GsonObject properties = new GsonObject(designObject.get("properties").getAsJsonObject());
            return new SweetPropertiesComponent(
                properties.getInt("blockWidth"),
                properties.getBoolean("normalize"),
                properties.getString("charCode"),
                buildPrinterCutModeProperty(properties.getElement("cut"))
            );
        }
        return null;
    }

    private @Nullable List<SweetBlockComponent> buildPrinterDesignBlocks() {
        if (designObject.has("data")) {
            List<SweetBlockComponent> blocks = new LinkedList<>();
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

    private @Nullable SweetBlockComponent castBlock(JsonElement element) {
        if (element.isJsonPrimitive()) {
            SweetCellComponent cell = new SweetCellComponent(null, element.getAsString());
            List<SweetCellComponent> row = new LinkedList<>();
            row.add(cell);
            List<List<SweetCellComponent>> rows = new LinkedList<>();
            rows.add(row);
            return new SweetBlockComponent(
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
            return new SweetBlockComponent(
                block.getInt("gap"),
                block.getCharacter("separator"),
                buildQrProperty(block.getElement("qr")),
                block.getString("imgPath"),
                block.getInt("nColumns"),
                buildPrinterDesignStyles(block.getElement("styles")),
                buildRows(block.getElement("rows"))
            );
        }
        return null;
    }

    private @Nullable Map<String, SweetStyleComponent> buildPrinterDesignStyles(@Nullable JsonElement styleElement) {
        if (styleElement != null && styleElement.isJsonObject()) {
            Map<String, SweetStyleComponent> styles = new HashMap<>();
            styleElement.getAsJsonObject().asMap().forEach((key, element) -> styles.put(key, castStyle(element)));
            return styles;
        }
        return null;
    }

    private @Nullable SweetStyleComponent castStyle(@NotNull JsonElement element) {
        if (element.isJsonObject()) {
            GsonObject style = new GsonObject(element.getAsJsonObject());
            return new SweetStyleComponent(
                style.getInt("fontWidth"),
                style.getInt("fontHeight"),
                style.getBoolean("bold"),
                style.getBoolean("normalize"),
                style.getBoolean("bgInverted"),
                style.getCharacter("pad"),
                SweetJustify.fromValue(style.getString("align")),
                style.getInt("span"),
                SweetScale.fromValue(style.getString("scale")),
                style.getInt("width"),
                style.getInt("height")
            );
        }
        return null;
    }

    private @Nullable List<List<SweetCellComponent>> buildRows(@Nullable JsonElement element) {
        if (element != null && element.isJsonArray()) {
            List<List<SweetCellComponent>> rows = new LinkedList<>();
            element.getAsJsonArray().forEach(row -> rows.add(castRow(row)));
            return rows;
        }
        return null;
    }

    private List<SweetCellComponent> castRow(@NotNull JsonElement element) {
        List<SweetCellComponent> row = new LinkedList<>();
        if (element.isJsonPrimitive()) {
            SweetCellComponent cell = new SweetCellComponent(null, element.getAsString());
            row.add(cell);
            return row;
        } else if (element.isJsonObject()) {
            GsonObject cell = new GsonObject(element.getAsJsonObject());
            SweetCellComponent sweetCellComponent = new SweetCellComponent(cell.getString("className"), cell.getString("text"));
            row.add(sweetCellComponent);
            return row;
        } else if (element.isJsonArray()) {
            element.getAsJsonArray().forEach(item -> row.addAll(castRow(item)));
        }
        return row;
    }

    private @Nullable SweetCutComponent buildPrinterCutModeProperty(@Nullable JsonElement element) {
        if (element != null && element.isJsonObject()) {
            GsonObject cut = new GsonObject(element.getAsJsonObject());
            return new SweetCutComponent(
                cut.getInt("feed"),
                SweetCutMode.fromValue(cut.getString("mode"))
            );
        }
        return null;
    }

    private @Nullable SweetOpenDrawerComponent buildPrinterOpenDrawer() {
        if (designObject.has("openDrawer") && designObject.get("openDrawer").isJsonObject()) {
            GsonObject openDrawer = new GsonObject(designObject.get("openDrawer").getAsJsonObject());
            return new SweetOpenDrawerComponent(
                SweetPinConnector.fromValue(openDrawer.getInt("pin")),
                openDrawer.getInt("t1"),
                openDrawer.getInt("t2")
            );
        }
        return null;
    }

    private @Nullable SweetQrComponent buildQrProperty(@Nullable JsonElement element) {
        if (element == null) {
            return null;
        }
        if (element.isJsonPrimitive()) {
            String data = element.getAsString();
            return new SweetQrComponent(data, null, null);
        } else if (element.isJsonObject()) {
            GsonObject qr = new GsonObject(element.getAsJsonObject());
            return new SweetQrComponent(
                qr.getString("data"),
                SweetQrType.fromValue(qr.getString("type")),
                SweetQrCorrectionLevel.fromValue(qr.getString("correctionLevel"))
            );
        }
        return null;
    }

}
