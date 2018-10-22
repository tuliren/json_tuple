package com.github.tuliren.json_kiwi;

import java.util.Optional;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestKeyPaths extends BaseTestCase {

  @Test
  public void testElementPath() throws Exception {
    ElementKeyPath path = new ElementKeyPath(ELEMENT_PATH_NAME);
    assertEquals(path, KeyPaths.create(path.toString()));
  }

  @Test
  public void testArrayPath() throws Exception {
    ArrayKeyPath path = new ArrayKeyPath(Optional.of(ARRAY_PATH_NAME), 0, 3);
    assertEquals(path, KeyPaths.create(path.toString()));
  }

  @Test
  public void testKeylessArrayPath() throws Exception {
    ArrayKeyPath path = new ArrayKeyPath(Optional.empty(), 0, 3);
    assertEquals(path, KeyPaths.create(path.toString()));
  }

  @Test
  public void testPathCreationFromString() throws Exception {
    // element path
    assertTrue(KeyPaths.create("array") instanceof ElementKeyPath);
    assertTrue(KeyPaths.create("") instanceof ElementKeyPath);
    // only strings that match /.*\|\d+\|\d+/ will be matched to ArrayKeyPath
    // all else are matched to ElementKeyPath
    assertTrue(KeyPaths.create("|") instanceof ElementKeyPath);
    assertTrue(KeyPaths.create("||") instanceof ElementKeyPath);
    assertTrue(KeyPaths.create("||0") instanceof ElementKeyPath);
    assertTrue(KeyPaths.create("|0|") instanceof ElementKeyPath);
    assertTrue(KeyPaths.create("|||") instanceof ElementKeyPath);

    // array path
    assertTrue(KeyPaths.create("array|10|15") instanceof ArrayKeyPath);
    assertTrue(KeyPaths.create("|10|15") instanceof ArrayKeyPath);
  }

}
