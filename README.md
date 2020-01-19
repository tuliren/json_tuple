Json Tuple
===

[![Build Status](https://github.com/tuliren/json_tuple/workflows/build/badge.svg)](https://github.com/tuliren/json_tuple/actions)

[![Build Status](https://travis-ci.org/tuliren/json_tuple.svg?branch=master)](https://travis-ci.org/tuliren/json_tuple)

## Overview
Json Tuple is a json tool that can **convert json objects from / to key value tuples**. This tool was originally created to parse and store json objects in database tables for [Jack](https://github.com/LiveRamp/jack).

## How to Use
```java
JsonObject json1 = new JsonParser()
    .parse("{k1: v1, k2: [v1, v2, v3], ...}")
    .getAsJsonObject();

// from json object to tuples
List<JsonTuple> tuples = JsonTuples.toTuples(json);

// from tuples to json object
JsonObject json2 = JsonTuples.fromTuples(tuples);

// json1 is equal to json2
assert json1.equals(json2)
```

## How It Works
Each json key value pair is converted to a [`JsonTuple`](https://github.com/tuliren/json_tuple/blob/master/src/main/java/com/github/tuliren/json_tuple/JsonTuple.java), which keeps track of the key ([`KeyPath`](https://github.com/tuliren/json_tuple/blob/master/src/main/java/com/github/tuliren/json_tuple/KeyPath.java)), value and value type ([`ValueType`](https://github.com/tuliren/json_tuple/blob/master/src/main/java/com/github/tuliren/json_tuple/ValueType.java)). There are five different value types: string, boolean, number, empty, and null.

### Basic Element

A simple and flat json object will be resolved to the following key value pairs:
```java
{
  "k1": "string",
  "k2": 10,
  "k3": 5.5,
  "k4": true,
  "k5": "",
  "k6": null,
  null: "null-key",
  "null": "null-string-key"
}
```

Tuple | Key | Value | Value Type
---- | ---- | ---- | ----
1 | "k1" | "string" | JSON_STRING
2 | "k2" | 10 | JSON_NUMBER
3 | "k3" | 5.5 | JSON_NUMBER
4 | "k4" | `true` | JSON_BOOLEAN
5 | "k5" | | JSON_EMPTY
6 | "k6" | `null` | JSON_NULL
7 | `null` | "null-key" | JSON_STRING
8 | "null" | "null-string-key" | JSON_STRING

Their key is wrapped in [`ElementKeyPath`](https://github.com/tuliren/json_tuple/blob/master/src/main/java/com/github/tuliren/json_tuple/ElementKeyPath.java).

### Nested Element
A nested json element has more complicated keys (still wrapped in [`ElementKeyPath`](https://github.com/tuliren/json_tuple/blob/master/src/main/java/com/github/tuliren/json_tuple/ElementKeyPath.java)). Its key path is a concatenation of all parent keys joined by dots (`.`).

```json
{
  "k1": {
    "nest1": 10,
    "nest2": 20
  },
  "k2": {
    "nest1": 55,
    "nest2": 56,
    "nest3": {
      "double-nest1": 100,
      "double-nest2": 200
    }
  }
}
```

Tuple | Key | Value | Value Type
---- | ---- | ---- | ----
1 | "k1.nest1" | 10 | JSON_NUMBER
2 | "k1.nest2" | 20 | JSON_NUMBER
3 | "k2.nest1" | 55 | JSON_NUMBER
4 | "k2.nest2" | 56 | JSON_NUMBER
5 | "k2.nest3.double-nest1" | 100 | JSON_NUMBER
6 | "k2.nest3.double-nest2" | 100 | JSON_NUMBER

The order of the tuples matters. The parser assumes that their order remains the same as they are generated. If they are disarranged, exception may be thrown, or an incorrect json may be created.

### Array
Elements in a json array have even more complicated keys. It is wrapped in [`ArrayKeyPath`](https://github.com/tuliren/json_tuple/blob/master/src/main/java/com/github/tuliren/json_tuple/ArrayKeyPath.java), which keeps track of the total size of the array in which the element resides, as well as the index of this element. In this way, tuples can be constructed back into a json array.

```json
{
  "key": [
      1,
      2.5,
      true,
      "string",
      [100, 200]
  ]
}
```

Tuple | Key | Value | Value Type
---- | ---- | ---- | ----
1 | "key\|0\|5" | 1 | JSON_NUMBER
2 | "key\|1\|5" | 2.5 | JSON_NUMBER
3 | "key\|2\|5" | `true` | JSON_BOOLEAN 
4 | "key\|3\|5" | "string" | JSON_STRING 
5 | "key\|4\|5.\|0\|2" | 100 | JSON_NUMBER
6 | "key\|4\|5.\|1\|2" | 200 | JSON_NUMBER

The array key path is composed of three parts: array name, index, and size. Each part is separated by `|`. Nested array may not have a name, in that case the array name is empty. That's why the key for tuple 5 is `key|4|5.|1|2`. In this path, `key|4|5` is its parent key. `|0|2` is the nameless array element key, meaning that it is the `0` element in an array of size `2`. The two keys are joined with a `.`.

### More Examples
For more examples, please see [`TestJsonTuples`](https://github.com/tuliren/json_tuple/blob/master/src/test/java/com/github/tuliren/json_tuple/TestJsonTuples.java).

## Gotcha
- Neither key or values should include `.` or `|`, as these two characters are used as key path separator and array path separator, respectively. If a json object has these characters, it cannot be processed correctly. This issue can be fixed in a future version that allows users to customize these separators.
- The order of the tuples matters. The parser assumes that their order remains the same as they are generated. If they are disarranged, exception may be thrown, or an incorrect json may be created.
