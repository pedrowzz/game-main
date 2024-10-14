package com.minecraft.core.proxy.util.reward;

import com.minecraft.core.Constants;
import com.minecraft.core.database.redis.Redis;
import com.minecraft.core.enums.Rank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import redis.clients.jedis.Jedis;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/*
CREATE TABLE IF NOT EXISTS `codes`
 (`index` INT UNSIGNED NOT NULL, `key` VARCHAR(16) NOT NULL, `name` VARCHAR(128),
  `rank` VARCHAR(24) NOT NULL, `duration` VARCHAR(12) NOT NULL,
  `creator` VARCHAR(24) NOT NULL, `creation` BIGINT NOT NULL,
  `redeemer` VARCHAR(24), `redeem` BIGINT,
   PRIMARY KEY(`index`));
*/

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GiftCode {

    private String key, name;
    private Rank rank;
    private String duration;
    private UUID creator, redeemer;
    private long creation, redeem;

    public boolean isRedeemed() {
        return redeemer != null;
    }

    /*public void push() {
        String sql = "INSERT INTO `codes` (`key`, `name`, `rank`, `duration`, `creator`, `creation`, `redeemer`, `redeem`) VALUES (?, ?, ?, ?, ?, ?, ?);";

        try (PreparedStatement ps = Constants.getMySQL().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, key);
            ps.setString(2, name);
            ps.setString(3, rank.getUniqueCode());
            ps.setString(4, duration);
            ps.setString(5, creator.toString());
            ps.setLong(6, creation);
            ps.setString(7, redeemer.toString());
            ps.setLong(8, redeem);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }*/

    public void update() {
        try (Jedis redis = Constants.getRedis().getResource(Redis.GIFTCODE_CACHE)) {
            UUID keyUniqueid = UUID.nameUUIDFromBytes((getKey()).getBytes(StandardCharsets.UTF_8));
            redis.setex("key-" + keyUniqueid, 86400, Constants.GSON.toJson(this));
        } catch (Exception e) {
        }
    }
}
