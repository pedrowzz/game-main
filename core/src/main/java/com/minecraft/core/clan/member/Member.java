package com.minecraft.core.clan.member;

import com.google.gson.annotations.SerializedName;
import com.minecraft.core.clan.member.role.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class Member {

    private String name;
    private UUID uniqueId;
    private Role role;
    @SerializedName(value = "join")
    private long joinedAt;

    public boolean isAdmin() {
        return role.getId() >= Role.ADMINISTRATOR.getId();
    }

    @Override
    public String toString() {
        return "Member{" +
                "name=" + name +
                ", uniqueId=" + uniqueId +
                ", role=" + role +
                ", join=" + joinedAt +
                '}';
    }
}
