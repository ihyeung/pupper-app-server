package com.utahmsd.pupper.dto.pupper;

public enum Size {
    TOY,
    SMALL,
    MID,
    LARGE,
    XLARGE,
    UNKNOWN;

    public String value() { return name(); }

    public static Size fromValue(String v) { return valueOf(v); }
}
