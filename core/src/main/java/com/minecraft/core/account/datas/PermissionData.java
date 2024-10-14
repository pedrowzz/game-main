package com.minecraft.core.account.datas;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PermissionData {

    private final String name;
    private String addedBy;
    private long addedAt, expiration;

    public boolean hasExpired() {
        return !isPermanent() && expiration < System.currentTimeMillis();
    }

    public boolean isPermanent() {
        return expiration == -1;
    }

    public static PermissionData fromJsonObject(JsonObject object) {
        String name = object.get("permission").getAsString();
        String added_by = object.get("added_by").getAsString();

        long expiresAt = object.get("expiration").getAsLong();
        long added_at = object.get("added_at").getAsLong();

        return new PermissionData(name, added_by, added_at, expiresAt);
    }

}