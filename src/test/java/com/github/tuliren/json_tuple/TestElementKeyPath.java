package com.github.tuliren.json_tuple;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestElementKeyPath extends BaseTestCase {

  private static final String ELEMENT_PATH_NAME = "test_element_path_name";

  @Test
  public void testElementPath() {
    KeyPath elementPath = new ElementKeyPath(ELEMENT_PATH_NAME);
    assertEquals(elementPath, KeyPaths.create(elementPath.toString()));
  }

}
