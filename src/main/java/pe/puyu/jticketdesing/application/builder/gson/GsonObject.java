package pe.puyu.jticketdesing.application.builder.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GsonObject {
    private final JsonObject jsonObject;

    public GsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public @Nullable Integer getInt(String property) {
        if (hasInt(property)) {
            return jsonObject.get(property).getAsInt();
        }
        return null;
    }

    public @Nullable Boolean getBoolean(String property) {
        if (hasBoolean(property)) {
            return jsonObject.get(property).getAsBoolean();
        }
        return null;
    }

    public @Nullable String getString(String property) {
        if (!isBlank(property)) {
            return jsonObject.get(property).getAsString();
        }
        return null;
    }

    public @Nullable JsonElement getElement(String property) {
        if (jsonObject.has(property)) {
            return jsonObject.get(property);
        }
        return null;
    }

    public @Nullable Character getCharacter(String property) {
        if (jsonObject.has(property)) {
            return jsonObject.get(property).getAsString().charAt(0);
        }
        return null;
    }

    public boolean hasInt(String property) {
        if (!isPrimitive(property)) {
            return false;
        }
        try {
            Integer.parseInt(jsonObject.get(property).getAsString());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public boolean hasBoolean(String property) {
        if (isPrimitive(property)) {
            String value = jsonObject.get(property).getAsString();
            return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");
        }
        return false;
    }

    public boolean isPrimitive(String property) {
        return jsonObject.has(property) && jsonObject.get(property).isJsonPrimitive();
    }

    public boolean isBlank(String property) {
        return !isPrimitive(property) || jsonObject.get(property).getAsString().isBlank();
    }

    public @NotNull JsonObject get() {
        return this.jsonObject;
    }

}
