package com.minecraft.core.account.system;

import com.google.gson.*;
import com.minecraft.core.account.Account;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AccountDeposit {

    String PLAYER_KEY = "playerAccount";

    private final Jedis jedis;
    private final JsonParser jsonParser;
    private final Gson gson;

    public AccountDeposit(Jedis jedis) {
        this.jedis = jedis;
        this.jsonParser = new JsonParser();
        this.gson = new GsonBuilder().create();
    }

    public Account getAccount(UUID uniqueId) {
        Account profile;
        if (!jedis.exists(PLAYER_KEY + uniqueId.toString()))
            return null;
        Map<String, String> fields = jedis.hgetAll(PLAYER_KEY + uniqueId);
        if (fields == null || fields.isEmpty() || fields.size() < 40)
            return null;
        JsonObject jsonObject = new JsonObject();
        for (Map.Entry<String, String> entry : fields.entrySet())
            jsonObject.add(entry.getKey(), this.jsonParser.parse(entry.getValue()));
        profile = this.gson.fromJson(jsonObject.toString(), Account.class);
        return profile;
    }

    public void saveAccount(Account account) {
        JsonObject jsonObject = this.jsonParser.parse(this.gson.toJson(account)).getAsJsonObject();
        Map<String, String> playerElements = new HashMap<>();
        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet())
            playerElements.put(entry.getKey(), this.gson.toJson(entry.getValue()));
        jedis.hmset(PLAYER_KEY + account.getUniqueId().toString(), playerElements);
    }

}