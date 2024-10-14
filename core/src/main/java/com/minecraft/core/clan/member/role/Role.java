package com.minecraft.core.clan.member.role;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {

    OWNER(3, "Dono"),
    ADMINISTRATOR(2, "Administrador"),
    MEMBER(1, "Membro");

    private final int id;
    private final String display;

}
