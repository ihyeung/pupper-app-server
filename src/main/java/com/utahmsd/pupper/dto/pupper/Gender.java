package com.utahmsd.pupper.dto.pupper;

public enum Gender {
    FEMALE,
    MALE,
    UNKNOWN;

    public String value() { return name(); }

    public static Gender fromValue(String v) { return valueOf(v); }
}
