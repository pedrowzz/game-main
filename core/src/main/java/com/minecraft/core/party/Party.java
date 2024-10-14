package com.minecraft.core.party;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class Party {

    private final UUID uniqueId = UUID.randomUUID();
    private final Set<UUID> members = new HashSet<>();
    private final UUID owner;
    private boolean open;

}
