package pe.puyu.SweetTicketDesign.domain.table.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

@Deprecated(since = "0.1.0", forRemoval = true)
public class JsonUtil {

	public static JsonArray normalizeToJsonArray(JsonElement jsonElement) {
		JsonArray jsonArray = new JsonArray();
		if (jsonElement.isJsonNull()) {
			return jsonArray;
		}
		if (jsonElement.isJsonPrimitive()) {
			jsonArray.add(jsonElement.getAsString());
			return jsonArray;
		}
		return jsonElement.getAsJsonArray();
	}

	public static JsonObject normalizeToJsonObject(JsonElement jsonElement, String targetKey, String defaultValue, JsonObject defaults) {
		JsonObject jsonObject = new JsonObject();
		if (jsonElement.isJsonNull()) {
			jsonElement = new JsonPrimitive(defaultValue);
		}
		if (jsonElement.isJsonPrimitive()) {
			jsonObject.addProperty(targetKey, jsonElement.getAsString());
			defaults.remove(targetKey);
			var defaultEntries = defaults.entrySet();
			for (var entry : defaultEntries) {
				jsonObject.add(entry.getKey(), entry.getValue());
			}
			return jsonObject;
		}
		jsonObject = jsonElement.getAsJsonObject();
		var defaultEntries = defaults.entrySet();
		for (var defaultEntry : defaultEntries) {
			if (!jsonObject.has(defaultEntry.getKey())) {
				jsonObject.add(defaultEntry.getKey(), defaultEntry.getValue());
			}
		}
		return jsonObject;
	}

	public static JsonArray map(JsonArray target, Function<JsonElement, JsonElement> mapper) {
		JsonArray result = new JsonArray();
		for (JsonElement jsonElement : target) {
			result.add(mapper.apply(jsonElement));
		}
		return result;
	}

	public static <T> List<T> mapToList(JsonArray target, Function<JsonElement, T> mapper) {
		return mapToList(target, (ignored, currentElement) -> mapper.apply(currentElement));
	}

	public static <T> List<T> mapToList(JsonArray target, BiFunction<Integer, JsonElement, T> mapper) {
		List<T> result = new LinkedList<>();
		for (int i = 0; i < target.size(); ++i) {
			JsonElement jsonElement = target.get(i);
			result.add(mapper.apply(i, jsonElement));
		}
		return result;
	}

	public static JsonArray filter(JsonArray target, Predicate<JsonElement> predicate) {
		JsonArray result = new JsonArray();
		for (JsonElement jsonElement : target) {
			if (predicate.test(jsonElement))
				result.add(jsonElement);
		}
		return result;
	}

	public static <T> T reduce(JsonArray target, BiFunction<T, JsonElement, T> reducer, T initValue) {
		JsonArray result = new JsonArray();
		T reduceValue = initValue;
		for (JsonElement currentElement : target) {
			reduceValue = reducer.apply(reduceValue, currentElement);
		}
		return reduceValue;
	}
}
