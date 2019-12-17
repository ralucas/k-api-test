package com.employees.api.model;

public enum Status {
    INACTIVE("INACTIVE"), ACTIVE("ACTIVE");

    private final String value;

    private Status(String status) {
        this.value = status;
    }

    public String getStatus() {
        return this.value;
    }

    public static Status fromString(String status) {
        for (Status s : Status.values()) {
            if (s.value.equalsIgnoreCase(status)) {
                return s;
            }
        }
        return null;
    }

    public boolean isActive() {
        return this.value.equals("ACTIVE");
    }
}

