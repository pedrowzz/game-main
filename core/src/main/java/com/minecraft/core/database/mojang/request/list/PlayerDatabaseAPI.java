package com.minecraft.core.database.mojang.request.list;

import com.google.gson.JsonObject;
import com.minecraft.core.Constants;
import com.minecraft.core.database.HttpRequest;
import com.minecraft.core.database.mojang.request.API;

import java.util.UUID;

public class PlayerDatabaseAPI extends API {

    public PlayerDatabaseAPI() {
        setURL("https://playerdb.co/api/player/minecraft/");
    }

    @Override
    public UUID request(String name) {
        HttpRequest request = HttpRequest.get(getURL() + name).connectTimeout(6500).readTimeout(6500).userAgent("Administrator/1.0.0").acceptJson();

        if (request.ok()) {

            JsonObject object = Constants.JSON_PARSER.parse(request.reader()).getAsJsonObject();

            if (!object.get("success").getAsString().equals("true"))
                return null;

            JsonObject details = object.get("data").getAsJsonObject().get("player").getAsJsonObject();

            String id = details.get("id").getAsString();
            String uniqueId = id.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");

            return UUID.fromString(uniqueId);
        } else if (request.notFound() || request.noContent() || request.code() == 500)
            return null;
        else
            throw new NullPointerException("Failed to check player username! (" + name + ") [response-code: " + request.code() + "]");
    }
}
