package com.minecraft.core.util.anticheat;

import com.minecraft.core.util.anticheat.information.Information;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class AntiCheatAlert {

    private String displayName;
    private final UUID target;
    private final int maximumAlerts;
    private List<Information> informations = new ArrayList<>();

    public AntiCheatAlert(String name, UUID target, int maximumAlerts) {
        this.displayName = name;
        this.target = target;
        this.maximumAlerts = maximumAlerts;
    }

    public void addInformation(String display, Object value) {
        informations.add(new Information(display, value.toString()));
    }
}
