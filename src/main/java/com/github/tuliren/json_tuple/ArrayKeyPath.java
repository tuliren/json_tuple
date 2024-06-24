package com.github.tuliren.json_tuple;

import java.util.Objects;
import java.util.Optional;

/**
 * Key path (json key) for json array elements.
 */
public class ArrayKeyPath implements KeyPath {

  private final Optional<String> name;
  private final int index;
  private final int size;

  public ArrayKeyPath(Optional<String> name, int index, int size) {
    this.name = name;
    this.index = index;
    this.size = size;
  }

  @Override
  public Optional<String> getName() {
    return name;
  }

  @Override
  public boolean isArray() {
    return true;
  }

  @Override
  public Optional<Integer> getListIndex() {
    return Optional.of(index);
  }

  @Override
  public Optional<Integer> getListSize() {
    return Optional.of(size);
  }

  public String toString(String keylessArrayName, String listPathSeparator) {
    return String.format("%s%s%d%s%d", name.orElse(keylessArrayName), listPathSeparator, index, listPathSeparator, size);
  }

  @Override
  public String toString() {
    return toString(Constants.KEYLESS_ARRAY_NAME, Constants.LIST_PATH_SEPARATOR);
  }

  @Override
  public int hashCode() {
    return name.hashCode() + 19 * index + 29 * size;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof ArrayKeyPath)) {
      return false;
    }

    ArrayKeyPath that = (ArrayKeyPath)other;
    return Objects.equals(this.name, that.name) &&
        this.index == that.index &&
        this.size == that.size;
  }

}
