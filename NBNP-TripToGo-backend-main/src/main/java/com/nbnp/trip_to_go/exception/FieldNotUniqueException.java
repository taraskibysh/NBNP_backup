package com.nbnp.trip_to_go.exception;

public class FieldNotUniqueException extends RuntimeException {
    private final String fieldName;
    private final String fieldValue;

    public FieldNotUniqueException(String fieldName, String fieldValue) {
        super("The field '" + fieldName + "' with value '" + fieldValue + "' already exists.");
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }
}