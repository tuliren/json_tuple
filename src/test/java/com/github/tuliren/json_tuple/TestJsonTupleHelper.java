package com.github.tuliren.json_tuple;

import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

public class TestJsonTupleHelper extends BaseTestCase {

  private final JsonTupleHelper defaultHelper = JsonTupleHelper.create().get();
  private final JsonTupleHelper customHelper = JsonTupleHelper.create()
      .setKeylessArrayName(CUSTOM_KEYLESS_ARRAY_NAME)
      .setPathSeparator(CUSTOM_PATH_SEPARATOR)
      .setListPathSeparator(CUSTOM_LIST_PATH_SEPARATOR)
      .get();

  private final JsonParser parser = new JsonParser();
  private String jsonString;

  private void testJson() {
    if (jsonString == null) {
      throw new NullPointerException("Testing json string is null");
    }
    testJson(defaultHelper);
    testJson(customHelper);
    jsonString = null;
  }

  private void testJson(JsonTupleHelper helper) {
    JsonObject expected = parser.parse(jsonString).getAsJsonObject();
    List<JsonTuple> jsonTuples = helper.toTupleList(expected);
    JsonObject actual = helper.fromTupleList(jsonTuples);
    Assert.assertEquals(expected, actual);
    // the number of tuples equal to the number of comma + 1
    Assert.assertEquals(StringUtils.countMatches(jsonString, ",") + 1, jsonTuples.size());
  }

  @Test
  public void testString() {
    jsonString = "{key: \"string\"}";
    testJson();
  }

  @Test
  public void testLong() {
    jsonString = "{k1: 1, k2: 2}";
    testJson();
  }

  @Test
  public void testDouble() {
    jsonString = "{k1: 1.1, k2: 2.2, k3: 3.33333}";
    testJson();
  }

  @Test
  public void testBoolean() {
    jsonString = "{k1: true, k2: false, k3: true}";
    testJson();
  }

  @Test
  public void testEmptyString() {
    jsonString = "{key: \"\"}";
    testJson();

    jsonString = "{key: [\"\", \"\"]}";
    testJson();
  }

  @Test
  public void testNullString() {
    jsonString = "{key: \"null\"}";
    testJson();

    jsonString = "{key: [\"null\"]}";
    testJson();
  }

  @Test
  public void testNull() {
    jsonString = "{key: null}";
    testJson();

    jsonString = "{key: [null]}";
    testJson();
  }

  @Test
  public void testNullKey() {
    jsonString = "{null: value}";
    testJson();
  }

  @Test
  public void testNullStringKey() {
    jsonString = "{\"null\": value}";
    testJson();
  }

  @Test
  public void testEmptyKey() {
    jsonString = "{\"\": value}";
    testJson();

    jsonString = "{\"\": [1, 2, 3]}";
    testJson();
  }

  @Test
  public void testEmptyArray() {
    jsonString = "{key: []}";
    testJson();
  }

  @Test
  public void testEmptyObject() {
    jsonString = "{key: {}}";
    testJson();
  }

  @Test
  public void testEmptyObjectInArray() {
    jsonString = "{key: [{}]}";
    testJson();

    jsonString = "{key: [{}, {}, {}]}";
    testJson();

    jsonString = "{key: [{}, 1, 2, 3]}";
    testJson();

    jsonString = "{key: [1, 2, 3, {}]}";
    testJson();

    jsonString = "{key: [1, 2, {}, 3]}";
    testJson();
  }

  @Test
  public void testSimpleObject() {
    jsonString = "{object: {k1: v1}, str: value, n1: 1, n2: 2.2, b1: true, b2: false}";
    testJson();
  }

  @Test
  public void testSimpleArray() {
    jsonString = "{key: [1.1, 2.2, str, true, false]}";
    testJson();
  }

  @Test
  public void testArrayInArray() {
    jsonString = "{key: [[1, 2, 3], [4, 5, 6], 1, 2.2, string, true, false]}";
    testJson();

    jsonString = "{key: [[[[[[1]]]]]]}";
    testJson();

    jsonString = "{key: [1, [2, [3, [4, [5, [6]]]]]]}";
    testJson();

    jsonString = "{key: [1, 2, [3], 4, [[5]], 6]}";
    testJson();
  }

  @Test
  public void testObjectInObject() {
    jsonString = "{key: {key: {key: {key: {key: value}}}}}";
    testJson();

    jsonString = "{key0: value, key1: {key0: value, key1: {key0: value, key1: {key0: value, key1: {key0: value, key1: value}}}}}";
    testJson();
  }

  @Test
  public void testObjectInArray() {
    jsonString = "{key: [{l1: v1}, {l2: v2}]}";
    testJson();

    jsonString = "{key: [[[[{l1: v1}, {l2: v2}]], {l3: v3}]]}";
    testJson();
  }

  @Test
  public void testComplexObject() {
    jsonString = "{" +
        "key1: val1," +
        "key2 :" +
        "  {" +
        "    array1: [1, 2, 3]," +
        "    array2: [four, five]," +
        "    key3: val2," +
        "    deepObject:" +
        "       {" +
        "         deepKey: 0.54," +
        "         deepArray: [false, true, false]," +
        "         emptyObject: {}" +
        "       }," +
        "    deepArray: [21, 22, 23]" +
        "  }," +
        "arraysInArray :" +
        "  [" +
        "    [1, 2, 3]," +
        "    [4, 5, 6]," +
        "    [7, 8, 9]" +
        "  ]," +
        "objectsInArray :" +
        "  [" +
        "    {" +
        "      k1: [true, false, true]," +
        "      k2: 11" +
        "    }," +
        "    {" +
        "      k1: [true, false, true]," +
        "      k2: 12," +
        "      emptyArray: []" +
        "    }" +
        "  ]," +
        "array3: [6,7,8,9,10]," +
        "bool1: true," +
        "bool2: false," +
        "nullKey: null," +
        "nullStrKey: \"null\"" +
        "}";
    testJson();
  }

}
