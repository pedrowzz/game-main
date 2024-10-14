package com.minecraft.core.clan.invite;

import com.minecraft.core.clan.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class Invite {

    private String inviteName;
    private UUID invite;
    private final long release = System.currentTimeMillis();
    private Status status;
    private Member invitor;

    public boolean expired() { // 7 minutes
        return release + 420000 < System.currentTimeMillis();
    }

    @RequiredArgsConstructor
    @Getter
    public enum Status {

        PENDING("§fPendente"), DECLINED("§cRecusado"), ACCEPTED("§aAceito");

        private final String name;

    }

}