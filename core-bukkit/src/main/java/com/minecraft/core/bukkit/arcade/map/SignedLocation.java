package com.minecraft.core.bukkit.arcade.map;

import com.minecraft.core.bukkit.arcade.map.synthetic.SyntheticLocation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SignedLocation {

    private final String title;
    private final SyntheticLocation location;

    @Override
    public String toString() {
        return "SignedLocation{" +
                "name='" + title + '\'' +
                ", location=" + location +
                '}';
    }
}