package com.utahmsd.pupper.dto.pupper;

public enum MaritalStatus {
    SINGLE,
    RELATIONSHIP,
    MARRIED,
    DIVORCED,
    UNKNOWN;

    public String value() { return name(); }

    public static MaritalStatus fromValue(String v) { return valueOf(v); }
}
