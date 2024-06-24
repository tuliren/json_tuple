package com.github.tuliren.json_tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * Convert a {@link JsonObject} to a list of {@link JsonTuple}s.
 */
final class JsonObjectConverter {
  /**
   * @return a list of {@link JsonTuple} representing the input {@code json} under {@code parentPaths}.
   */
  static List<JsonTuple> toTupleList(List<KeyPath> parentPaths, JsonObject json) {
    List<JsonTuple> tuples = new ArrayList<>();

    Set<Map.Entry<String, JsonElement>> entrySet = json.entrySet();
    if (entrySet.size() == 0) {
      tuples.add(JsonTuple.createEmpty(parentPaths));
    }

    for (Map.Entry<String, JsonElement> entry : entrySet) {
      List<KeyPath> childPaths = new ArrayList<>(parentPaths);
      String key = entry.getKey();
      JsonElement jsonElement = entry.getValue();

      if (jsonElement.isJsonArray()) {
        tuples.addAll(getTupleListFromArray(parentPaths, Optional.of(key), jsonElement.getAsJsonArray()));
        continue;
      }

      childPaths.add(new ElementKeyPath(entry.getKey()));
      if (jsonElement.isJsonPrimitive()) {
        tuples.add(createPrimitiveTuple(childPaths, jsonElement.getAsJsonPrimitive()));
      } else if (jsonElement.isJsonObject()) {
        tuples.addAll(toTupleList(childPaths, jsonElement.getAsJsonObject()));
      } else if (jsonElement.isJsonNull()) {
        tuples.add(JsonTuple.createNull(childPaths));
      } else {
        throw new IllegalArgumentException("Unexpected json element: " + jsonElement);
      }
    }

    return tuples;
  }

  private static List<JsonTuple> getTupleListFromArray(List<KeyPath> parentPaths, Optional<String> arrayName, JsonArray jsonArray) {
    List<JsonTuple> tuples = new ArrayList<>(jsonArray.size());
    int size = jsonArray.size();

    if (size == 0) {
      List<KeyPath> childPaths = new ArrayList<>(parentPaths);
      childPaths.add(new ArrayKeyPath(arrayName, 0, 0));
      tuples.add(JsonTuple.createEmpty(childPaths));
    }

    for (int i = 0; i < jsonArray.size(); i++) {
      List<KeyPath> childPaths = new ArrayList<>(parentPaths);
      childPaths.add(new ArrayKeyPath(arrayName, i, size));

      JsonElement jsonElement = jsonArray.get(i);

      if (jsonElement.isJsonPrimitive()) {
        tuples.add(createPrimitiveTuple(childPaths, jsonElement.getAsJsonPrimitive()));
      } else if (jsonElement.isJsonArray()) {
        JsonArray keylessArray = jsonElement.getAsJsonArray();
        tuples.addAll(getTupleListFromArray(childPaths, Optional.empty(), keylessArray));
      } else if (jsonElement.isJsonObject()) {
        tuples.addAll(toTupleList(childPaths, jsonElement.getAsJsonObject()));
      } else if (jsonElement.isJsonNull()) {
        tuples.add(JsonTuple.createNull(childPaths));
      } else {
        throw new IllegalArgumentException("Unexpected json element: " + jsonElement);
      }
    }

    return tuples;
  }

  private static JsonTuple createPrimitiveTuple(List<KeyPath> childPaths, JsonPrimitive jsonPrimitive) {
    if (jsonPrimitive.isBoolean()) {
      return JsonTuple.createBoolean(childPaths, jsonPrimitive.getAsString());
    } else if (jsonPrimitive.isNumber()) {
      return JsonTuple.createNumber(childPaths, jsonPrimitive.getAsString());
    } else {
      return JsonTuple.createString(childPaths, jsonPrimitive.getAsString());
    }
  }
}
