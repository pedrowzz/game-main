package com.minecraft.core.account.system;

import com.minecraft.core.account.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

@Getter
@Setter
@AllArgsConstructor
public class UnloadTask {

    private final long unload;
    private final Consumer<Account> unloadHandler;
}
