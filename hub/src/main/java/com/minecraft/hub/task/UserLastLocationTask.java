package com.minecraft.hub.task;

import com.minecraft.hub.user.User;
import com.minecraft.hub.user.storage.UserStorage;

public class UserLastLocationTask implements Runnable {

    private final UserStorage storage;

    public UserLastLocationTask(final UserStorage storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        for (final User user : storage.getUsers()) {
            if (user == null)
                continue;
            user.setLastLocation(user.getPlayer().getLocation());
        }
    }

}