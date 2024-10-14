package com.minecraft.core.proxy.util.reward.storage;

import com.minecraft.core.Constants;
import com.minecraft.core.database.redis.Redis;
import com.minecraft.core.proxy.util.reward.GiftCode;
import redis.clients.jedis.Jedis;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class GiftCodeStorage {

    private final List<GiftCode> giftCodes = new ArrayList<>();
    private final Set<UUID> notExists = new HashSet<>();

    public GiftCode get(String key) {
        return giftCodes.stream().filter(c -> c.getKey().equals(key)).findFirst().orElse(load(key));
    }

    public boolean delete(GiftCode giftCode) {
        giftCodes.remove(giftCode);
        try (Jedis redis = Constants.getRedis().getResource(Redis.GIFTCODE_CACHE)) {
            UUID keyUniqueid = UUID.nameUUIDFromBytes((giftCode.getKey()).getBytes(StandardCharsets.UTF_8));
            redis.del("key-" + keyUniqueid);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean push(GiftCode giftCode) {

        try (Jedis redis = Constants.getRedis().getResource(Redis.GIFTCODE_CACHE)) {
            UUID keyUniqueid = UUID.nameUUIDFromBytes((giftCode.getKey()).getBytes(StandardCharsets.UTF_8));
            redis.setex("key-" + keyUniqueid, 86400, Constants.GSON.toJson(giftCode));
        } catch (Exception e) {
            return false;
        }

        giftCodes.add(giftCode);
        return true;
    }

    private GiftCode load(String key) {

        UUID uuid = UUID.nameUUIDFromBytes((key.toLowerCase()).getBytes(StandardCharsets.UTF_8));

        if (notExists.contains(uuid))
            return null;

        try (Jedis redis = Constants.getRedis().getResource(Redis.GIFTCODE_CACHE)) {

            String keyJson = redis.get("key-" + uuid);

            if (keyJson == null) { // 404
                return null;
            }

            return Constants.GSON.fromJson(keyJson, GiftCode.class);
        }
    }
}
