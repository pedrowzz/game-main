package com.minecraft.generator.util.schematic.data;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * The {@code TAG_String} tag.
 */
public final class StringTag extends Tag {

    private final String value;

    /**
     * Creates the tag with an empty name.
     *
     * @param value the value of the tag
     */
    public StringTag(String value) {
        super();
        checkNotNull(value);
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "TAG_String(" + value + ")";
    }

}
