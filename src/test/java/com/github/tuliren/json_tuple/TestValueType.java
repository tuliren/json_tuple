package com.github.tuliren.json_tuple;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestValueType {

  @Test
  public void testValidValue() {
    for (ValueType valueType : ValueType.values()) {
      assertEquals(valueType, ValueType.findByValue(valueType.value));
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidValue() {
    ValueType.findByValue(5000);
  }

}
