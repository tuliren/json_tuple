package com.github.tuliren.json_kiwi;

import java.util.Objects;
import java.util.Optional;

/**
 * Key path for json non-array elements.
 */
public class ElementKeyPath implements KeyPath {

  private final String name;

  public ElementKeyPath(String name) {
    this.name = name;
  }

  @Override
  public Optional<String> getName() {
    return Optional.of(name);
  }

  @Override
  public boolean isArray() {
    return false;
  }

  @Override
  public Optional<Integer> getListIndex() {
    return Optional.empty();
  }

  @Override
  public Optional<Integer> getListSize() {
    return Optional.empty();
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public int hashCode() {
    return 19 + name.hashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof ElementKeyPath)) {
      return false;
    }
    return Objects.equals(this.name, ((ElementKeyPath)other).name);
  }

}
