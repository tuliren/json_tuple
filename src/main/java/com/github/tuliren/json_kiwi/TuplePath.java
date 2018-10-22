package com.github.tuliren.json_kiwi;

import java.util.Optional;

public interface TuplePath {

  Optional<String> getName();

  boolean isArray();

  Optional<Integer> getListIndex();

  Optional<Integer> getListSize();

  @Override
  String toString();

}
