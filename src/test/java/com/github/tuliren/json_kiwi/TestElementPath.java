package com.github.tuliren.json_kiwi;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestElementPath extends BaseTestCase {

  @Test
  public void testElementPath() throws Exception {
    TuplePath elementPath = new ElementPath(ELEMENT_PATH_NAME);
    assertEquals(elementPath, TuplePaths.create(elementPath.toString()));
  }

}
