package com.minecraft.core.account.datas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class KitData {

    private final String kit;
    private long boughtAt, expiration;

    public boolean hasExpired() {
        return !isPermanent() && expiration < System.currentTimeMillis();
    }

    public boolean isPermanent() {
        return expiration == -1;
    }

}