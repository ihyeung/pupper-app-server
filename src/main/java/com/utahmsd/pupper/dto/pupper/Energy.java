package com.utahmsd.pupper.dto.pupper;

public enum Energy {
    MIN,
    LOW,
    MED,
    HIGH,
    EXTREME;

    public String value() { return name(); }

    public static Energy fromValue(String v) { return valueOf(v); }

}
