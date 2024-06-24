package com.github.tuliren.json_tuple;

import java.util.Optional;
import java.util.regex.Pattern;

import org.apache.commons.lang3.math.NumberUtils;

/**
 * {@link KeyPath} factory.
 */
final class KeyPaths {

  private KeyPaths() {
  }

  static KeyPath create(String path, String keylessArrayName, String listPathSeparator) {
    String[] listPathSplits = path.split(Pattern.quote(listPathSeparator));
    if (listPathSplits.length == 3 && NumberUtils.isDigits(listPathSplits[1]) && NumberUtils.isDigits(listPathSplits[2])) {
      int index = Integer.valueOf(listPathSplits[1]);
      int size = Integer.valueOf(listPathSplits[2]);
      if (listPathSplits[0].equals(keylessArrayName)) {
        return new ArrayKeyPath(Optional.empty(), index, size);
      } else {
        return new ArrayKeyPath(Optional.of(listPathSplits[0]), index, size);
      }
    } else {
      return new ElementKeyPath(path);
    }
  }

  static KeyPath create(String path) {
    return create(path, Constants.KEYLESS_ARRAY_NAME, Constants.LIST_PATH_SEPARATOR);
  }

}
