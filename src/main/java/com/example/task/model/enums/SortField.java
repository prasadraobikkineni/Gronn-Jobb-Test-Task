package com.example.task.model.enums;

public enum SortField {
    DATE("date");

    private final String value;

    SortField(String field) {
        this.value = field;
    }

    public String getValue() {
        return value;
    }
}
