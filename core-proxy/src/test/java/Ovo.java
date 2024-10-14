import com.minecraft.core.account.Account;
import com.minecraft.core.database.enums.Columns;

import java.util.UUID;

public class Ovo {

    public static void main(String[] args) {

        Account account = new Account(UUID.randomUUID(), "");
        account.getDataStorage().getColumnsLoaded().put(Columns.PERMISSIONS, true);

        account.givePermission("command.playerfinder", -1, "[SERVER]");
        System.out.println(account.getData(Columns.PERMISSIONS).getAsJsonArray());
    }
}
