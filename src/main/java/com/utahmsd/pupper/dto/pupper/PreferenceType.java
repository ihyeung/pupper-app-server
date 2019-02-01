package com.utahmsd.pupper.dto.pupper;

/**
 * Enum class describing the different categories of matching preferences/settings.
 */
public enum PreferenceType {
    SIZE,
    AGE,
    ENERGY;

    public String value() { return name(); }

    public static PreferenceType fromValue(String v) { return valueOf(v); }
}

