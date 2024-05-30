package com.iapps.IappsReader.enums;

public enum FieldEnum {

    ID("id"),
    PAPER_NAME("paperName"),
    VERSION("version"),
    WIDTH("width"),
    HEIGHT("height"),
    DPI("dpi"),
    UPLOADED_DATE("uploadedDate"),
    UPDATED_DATE("updatedDate"),
    FILE_NAME("fileName");

    private final String fieldName;

    FieldEnum(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public static boolean contains(String testFieldName) {
        for (FieldEnum field : FieldEnum.values()) {
            if (field.getFieldName().equalsIgnoreCase(testFieldName)) {
                return true;
            }
        }
        return false;
    }
}
