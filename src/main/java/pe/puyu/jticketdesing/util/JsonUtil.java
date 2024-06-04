package pe.puyu.jticketdesing.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class JsonUtil {

	public static JsonArray normalizeToJsonArray(JsonElement jsonElement) {
		if (jsonElement.isJsonPrimitive()) {
			JsonArray jsonArray = new JsonArray();
			jsonArray.add(jsonElement.getAsString());
			return jsonArray;
		} else {
			return jsonElement.getAsJsonArray();
		}
	}
}
