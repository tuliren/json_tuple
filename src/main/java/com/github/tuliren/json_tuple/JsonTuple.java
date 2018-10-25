package com.github.tuliren.json_tuple;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * Json key value pair.
 */
public class JsonTuple {

  // key
  private final List<KeyPath> paths;
  // value type
  private final ValueType type;
  // value
  private final String value;

  private JsonTuple(List<KeyPath> paths, ValueType type, String value) {
    if (paths.isEmpty()) {
      throw new IllegalArgumentException("Value path cannot be empty: " + value);
    }
    this.paths = paths;
    this.type = type;
    this.value = value;
  }

  public static JsonTuple create(String fullPath, ValueType type, String value) {
    if (type.category != ValueType.Category.JSON) {
      throw new IllegalArgumentException("JSON value type is expected, but the actual type is: " + type.name());
    }

    List<KeyPath> paths = new LinkedList<>();
    for (String path : fullPath.split(Pattern.quote(Constants.PATH_SEPARATOR))) {
      paths.add(KeyPaths.create(path));
    }

    switch (type) {
      case JSON_STRING:
        return JsonTuple.createString(paths, value);
      case JSON_BOOLEAN:
        return JsonTuple.createBoolean(paths, value);
      case JSON_NUMBER:
        return JsonTuple.createNumber(paths, value);
      case JSON_NULL:
        return JsonTuple.createNull(paths);
      case JSON_EMPTY:
        return JsonTuple.createEmpty(paths);
      default:
        throw new IllegalArgumentException("Unexpected json type: " + type.name());
    }
  }

  static JsonTuple createString(List<KeyPath> paths, String value) {
    return new JsonTuple(paths, ValueType.JSON_STRING, value);
  }

  static JsonTuple createBoolean(List<KeyPath> paths, String value) {
    return new JsonTuple(paths, ValueType.JSON_BOOLEAN, value);
  }

  static JsonTuple createNumber(List<KeyPath> paths, String value) {
    return new JsonTuple(paths, ValueType.JSON_NUMBER, value);
  }

  static JsonTuple createNull(List<KeyPath> paths) {
    return new JsonTuple(paths, ValueType.JSON_NULL, null);
  }

  static JsonTuple createEmpty(List<KeyPath> paths) {
    return new JsonTuple(paths, ValueType.JSON_EMPTY, null);
  }

  public String getFullPaths() {
    return StringUtils.join(paths, Constants.PATH_SEPARATOR);
  }

  public List<KeyPath> getPaths() {
    return paths;
  }

  public ValueType getType() {
    return type;
  }

  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof JsonTuple)) {
      return false;
    }
    JsonTuple that = (JsonTuple)other;
    return this.type == that.type &&
        this.value.equals(that.value) &&
        this.paths.equals(that.paths);
  }

  @Override
  public int hashCode() {
    int hash = paths.hashCode();
    hash += 19 * hash + type.hashCode();
    hash += 19 * hash + value.hashCode();
    return hash;
  }

  @Override
  public String toString() {
    return String.format("%s: %s-%s", getFullPaths(), type.name(), value);
  }

}
