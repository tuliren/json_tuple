package com.github.tuliren.json_tuple;

import java.util.Collections;
import java.util.List;

import com.google.gson.JsonObject;

/**
 * Json helper that converts json objects {@link JsonObject} from and to key value tuple ({@link JsonTuple}).
 * <p>
 * To convert json object to tuples, use {@link JsonTupleHelper#toTupleList}.
 * To convert json tuples to object, use {@link JsonTupleHelper#fromTupleList}.
 */
public final class JsonTupleHelper {

  private final String pathSeparator;
  private final String keylessArrayName;
  private final String listPathSeparator;

  public static Builder create() {
    return new Builder();
  }

  private JsonTupleHelper(String pathSeparator, String keylessArrayName, String listPathSeparator) {
    this.pathSeparator = pathSeparator;
    this.keylessArrayName = keylessArrayName;
    this.listPathSeparator = listPathSeparator;
  }

  /**
   * @return a list of {@link JsonTuple} representing the input {@code json}.
   */
  public List<JsonTuple> toTupleList(JsonObject json) {
    return JsonObjectConverter.toTupleList(Collections.emptyList(), json);
  }

  /**
   * @return a json object from input {@code tuples}.
   */
  public JsonObject fromTupleList(List<JsonTuple> tuples) {
    return JsonTupleConverter.fromTupleList(tuples);
  }

  public static final class Builder {
    private String pathSeparator = Constants.PATH_SEPARATOR;
    private String keylessArrayName = Constants.KEYLESS_ARRAY_NAME;
    private String listPathSeparator = Constants.LIST_PATH_SEPARATOR;

    private Builder() {
    }

    public Builder setPathSeparator(String pathSeparator) {
      this.pathSeparator = pathSeparator;
      return this;
    }

    public Builder setKeylessArrayName(String keylessArrayName) {
      this.keylessArrayName = keylessArrayName;
      return this;
    }

    public Builder setListPathSeparator(String listPathSeparator) {
      this.listPathSeparator = listPathSeparator;
      return this;
    }

    public JsonTupleHelper get() {
      return new JsonTupleHelper(pathSeparator, keylessArrayName, listPathSeparator);
    }
  }

}
