package com.github.tuliren.json_kiwi;

import java.util.Optional;

/**
 * Json tuple path. This serves as the key in a json key value pair.
 */
public interface TuplePath {

  Optional<String> getName();

  boolean isArray();

  Optional<Integer> getListIndex();

  Optional<Integer> getListSize();

  @Override
  String toString();

}
