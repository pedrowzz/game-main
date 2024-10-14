package com.minecraft.core.party;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class PartyStorage {

    private final Set<Party> parties = new HashSet<>();

}
