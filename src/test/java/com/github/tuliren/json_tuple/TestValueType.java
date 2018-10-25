package com.github.tuliren.json_tuple;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestValueType extends BaseTestCase {

  @Test
  public void testValidValue() {
    for (ValueType valueType : ValueType.values()) {
      assertEquals(valueType, ValueType.findByValue(valueType.value));
    }
  }

  @Test
  public void testUniqueValue() {
    Set<Integer> values = Arrays.stream(ValueType.values()).map(v -> v.value).collect(Collectors.toSet());
    assertEquals(ValueType.values().length, values.size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidValue() {
    ValueType.findByValue(5000);
  }

}
