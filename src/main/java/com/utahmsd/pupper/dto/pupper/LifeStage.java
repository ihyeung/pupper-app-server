package com.utahmsd.pupper.dto.pupper;


public enum LifeStage {
    PUPPY,
    YOUNG,
    ADULT,
    MATURE;

    public String value() { return name(); }

    public static LifeStage fromValue(String v) { return valueOf(v); }
}
