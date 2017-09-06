package tuliren.json_db_lib;

public enum ValueType {
  JSON_STRING(Category.JSON, 100),
  JSON_BOOLEAN(Category.JSON, 110),
  JSON_NUMBER(Category.JSON, 120),
  JSON_EMPTY(Category.JSON, 130),
  JSON_NULL(Category.JSON, 140);

  public final Category category;
  public final int value;

  ValueType(Category category, int value) {
    this.category = category;
    this.value = value;
  }

  public boolean isPrimitive() {
    return category == Category.PRIMITIVE;
  }

  public boolean isJson() {
    return category == Category.JSON;
  }

  public boolean isList() {
    return category == Category.LIST;
  }

  public static ValueType findByValue(int value) {
    switch (value) {
      case 100:
        return JSON_STRING;
      case 110:
        return JSON_BOOLEAN;
      case 120:
        return JSON_NUMBER;
      case 130:
        return JSON_EMPTY;
      case 140:
        return JSON_NULL;
      default:
        throw new IllegalArgumentException("Unexpected value " + value);
    }
  }

  public enum Category {
    PRIMITIVE, JSON, LIST
  }

}
