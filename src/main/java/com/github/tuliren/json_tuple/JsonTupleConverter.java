package com.github.tuliren.json_tuple;

import java.util.List;
import java.util.Optional;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * Convert a list of {@link JsonTuple}s to a {@link JsonObject}.
 */
final class JsonTupleConverter {

  /**
   * @return a json object from input {@code tuples}.
   */
  public static JsonObject fromTupleList(List<JsonTuple> tuples) {
    JsonObject json = new JsonObject();
    for (JsonTuple tuple : tuples) {
      processTuple(json, tuple.getPaths(), tuple.getType(), tuple.getValue());
    }
    return json;
  }

  private static void processTuple(JsonElement parentElement, List<KeyPath> paths, ValueType type, String value) {
    checkArgument(!paths.isEmpty());
    KeyPath childPath = paths.get(0);
    if (childPath.isArray()) {
      addArrayPath(parentElement, (ArrayKeyPath)childPath, paths.subList(1, paths.size()), type, value);
    } else {
      addElementPath(parentElement, (ElementKeyPath)childPath, paths.subList(1, paths.size()), type, value);
    }
  }

  private static void addArrayPath(JsonElement parentElement, ArrayKeyPath childPath, List<KeyPath> tailPaths, ValueType type, String value) {
    Optional<String> childName = childPath.getName();
    Optional<Integer> childIndex = childPath.getListIndex();
    Optional<Integer> childSize = childPath.getListSize();
    checkState(childIndex.isPresent());
    checkState(childSize.isPresent());

    if (childName.isPresent()) {
      // when the child has a name, the parent must be an object
      String name = childName.get();
      checkState(parentElement.isJsonObject());

      JsonObject parentObject = parentElement.getAsJsonObject();
      final JsonArray childArray;

      if (!parentObject.has(name)) {
        childArray = new JsonArray();
        parentObject.add(name, childArray);
      } else {
        childArray = parentObject.get(name).getAsJsonArray();
      }

      if (!tailPaths.isEmpty()) {
        addChildPathToParentArray(childArray, childIndex.get(), tailPaths, type, value);
      } else {
        // tuples are sorted by name, and this element must have not been added to the array
        checkState(childArray.size() == childIndex.get());
        if (type != ValueType.JSON_EMPTY) {
          childArray.add(getJsonElement(type, value));
        } else if (childSize.get() == 0) {
          // when tail paths is empty and child size is zero,
          // it is an empty array and can be ignored: []
        } else {
          // when tail paths is empty and child size is not zero,
          // it is an empty object inside an array: [..., {}, ...]
          childArray.add(new JsonObject());
        }
      }
    } else {
      // when the child has no name, the parent must be an array
      checkState(parentElement.isJsonArray());
      JsonArray parentArray = parentElement.getAsJsonArray();

      if (!tailPaths.isEmpty()) {
        addChildPathToParentArray(parentArray, childIndex.get(), tailPaths, type, value);
      } else {
        // tuples are sorted by name, and this element must have not been added to the array
        checkState(parentArray.size() == childIndex.get());
        if (type != ValueType.JSON_EMPTY) {
          parentArray.add(getJsonElement(type, value));
        }
      }
    }
  }

  private static void addElementPath(JsonElement parentElement, ElementKeyPath childPath, List<KeyPath> tailPaths, ValueType type, String value) {
    // parent element must be an object because it has an element path
    checkState(parentElement.isJsonObject());
    // child path must have a name because it is an element path
    Optional<String> childName = childPath.getName();
    checkState(childName.isPresent());

    final JsonObject parentObject = parentElement.getAsJsonObject();
    if (!tailPaths.isEmpty()) {
      addElementChildPathToParentObject(parentObject, childName.get(), tailPaths, type, value);
    } else {
      parentObject.add(childName.get(), getJsonElement(type, value));
    }
  }

  private static void addElementChildPathToParentObject(JsonObject parentObject, String childPathName, List<KeyPath> tailPaths, ValueType type, String value) {
    KeyPath nextChildPath = tailPaths.get(0);
    final JsonElement childElement;
    if (!parentObject.has(childPathName)) {
      // current child must be an object because it is an element path
      childElement = new JsonObject();
      // next child must have a name because the current child is an object
      checkArgument(nextChildPath.getName().isPresent());
      parentObject.add(childPathName, childElement);
    } else {
      childElement = parentObject.get(childPathName);
    }
    processTuple(childElement, tailPaths, type, value);
  }

  private static void addChildPathToParentArray(JsonArray parentArray, int childPathIndex, List<KeyPath> tailPaths, ValueType type, String value) {
    KeyPath nextChildPath = tailPaths.get(0);
    final JsonElement childElement;
    if (parentArray.size() <= childPathIndex) {
      if (nextChildPath.getName().isPresent() || !nextChildPath.isArray()) {
        childElement = new JsonObject();
      } else {
        childElement = new JsonArray();
      }
      parentArray.add(childElement);
    } else {
      childElement = parentArray.get(childPathIndex);
    }
    processTuple(childElement, tailPaths, type, value);
  }

  private static JsonElement getJsonElement(ValueType type, String value) {
    switch (type) {
      case JSON_STRING:
        return new JsonPrimitive(value);
      case JSON_BOOLEAN:
        return new JsonPrimitive(Boolean.valueOf(value));
      case JSON_NUMBER:
        try {
          return new JsonPrimitive(Long.valueOf(value));
        } catch (NumberFormatException e1) {
          try {
            return new JsonPrimitive(Double.valueOf(value));
          } catch (NumberFormatException e2) {
            throw new IllegalArgumentException("Unexpected json number: " + value);
          }
        }
      case JSON_NULL:
        return JsonNull.INSTANCE;
      case JSON_EMPTY:
        return new JsonObject();
      default:
        throw new IllegalArgumentException("Unexpected value type: " + type.name());
    }
  }

  private static void checkArgument(boolean expression) {
    if (!expression) {
      throw new IllegalArgumentException();
    }
  }

  private static void checkState(boolean expression) {
    if (!expression) {
      throw new IllegalStateException();
    }
  }

}
