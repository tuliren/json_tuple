package com.github.tuliren.json_tuple;

import java.util.Optional;

/**
 * Json key. It is named path because it is usually a sequence of keys (for nested elements).
 */
public interface KeyPath {

  Optional<String> getName();

  boolean isArray();

  Optional<Integer> getListIndex();

  Optional<Integer> getListSize();

  @Override
  boolean equals(Object that);

  @Override
  int hashCode();

  @Override
  String toString();

}
