package com.minecraft.core.util.skin.util;

public class CustomProperty {

    private final String name;
    private final String value;
    private final String signature;

    public CustomProperty(String name, String value, String signature) {
        this.name = name;
        this.value = value;
        this.signature = signature;
    }

    public CustomProperty(String value, String signature) {
        this("textures", value, signature);
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public String getSignature() {
        return this.signature;
    }

    public boolean hasSignature() {
        return this.signature != null;
    }

}