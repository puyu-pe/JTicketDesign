package pe.puyu.jticketdesing.application.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class GsonObject {
    private final JsonObject jsonObject;

    public GsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
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

    public int getInt(String property, int defaultValue) {
        if (!hasInt(property)) {
            return defaultValue;
        }
        return jsonObject.get(property).getAsInt();
    }

    public boolean hasBoolean(String property) {
        if (!isPrimitive(property)) {
            return false;
        }
        String value = jsonObject.get(property).getAsString();
        return value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false");
    }

    public boolean getBoolean(String property, boolean defaultValue) {
        if (!hasBoolean(property)) {
            return defaultValue;
        }
        return jsonObject.get(property).getAsBoolean();
    }

    public String getString(String property, String defaultValue) {
        if (!isBlank(property)) {
            return jsonObject.get(property).getAsString();
        }
        return defaultValue;
    }

    public char getChar(String property, char defaultValue) {
        return getString(property, String.valueOf(defaultValue)).charAt(0);
    }

    public JsonElement getElement(String property) {
        if(jsonObject.has(property)){
            return jsonObject.get(property);
        }
        return new JsonObject();
    }

    public boolean isPrimitive(String property) {
        return jsonObject.has(property) && jsonObject.get(property).isJsonPrimitive();
    }

    public boolean isBlank(String property) {
        return !isPrimitive(property) || jsonObject.get(property).getAsString().isBlank();
    }

    public JsonObject get() {
        return this.jsonObject;
    }

}
