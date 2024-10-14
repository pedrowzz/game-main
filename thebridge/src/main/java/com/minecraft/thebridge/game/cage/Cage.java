package com.minecraft.thebridge.game.cage;

import com.minecraft.core.bukkit.server.thebridge.BridgeCageConfig;
import com.minecraft.core.bukkit.server.thebridge.CageRarity;
import com.minecraft.core.bukkit.util.worldedit.Pattern;
import com.minecraft.core.enums.Rank;
import com.minecraft.thebridge.TheBridge;
import com.minecraft.thebridge.util.bo3.BO3Common;
import com.minecraft.thebridge.util.bo3.BO3Object;
import lombok.Data;
import org.bukkit.Location;

import java.io.File;

@Data
public abstract class Cage implements Cloneable {

    private final transient TheBridge theBridge;

    private transient String displayName;
    private transient Pattern icon;

    private transient CageRarity rarity;
    private transient Rank rank;

    private transient boolean exclusive;

    private transient int price;

    private transient BO3Object blueBo3Obj;
    private transient Location blueSpawned;

    private transient BO3Object redBo3Obj;
    private transient Location redSpawned;

    private final transient String directory;

    private BridgeCageConfig cageConfig;

    public Cage(final TheBridge theBridge) {
        this.theBridge = theBridge;
        this.directory = getClass().getSimpleName().toLowerCase();
        this.displayName = getClass().getSimpleName();
        this.rarity = CageRarity.COMMON;
        this.rank = Rank.MEMBER;
        this.exclusive = false;
        this.price = 15000;
    }

    public void parse() {
        try {
            setBlueBo3Obj(BO3Common.parse(new File(System.getProperty("user.home") + File.separator + "misc" + File.separator + "arcade" + File.separator + "thebridge" + File.separator + "cages", (this.directory + "_blue"))));
            setRedBo3Obj(BO3Common.parse(new File(System.getProperty("user.home") + File.separator + "misc" + File.separator + "arcade" + File.separator + "thebridge" + File.separator + "cages", (this.directory + "_red"))));
        } catch (Exception e) {
            try {
                BO3Object bo3Object = BO3Common.parse(new File(System.getProperty("user.home") + File.separator + "misc" + File.separator + "arcade" + File.separator + "thebridge" + File.separator + "cages", this.directory));

                setBlueBo3Obj(bo3Object);
                setRedBo3Obj(bo3Object);
            } catch (Exception exception) {
                System.out.println("Houve um problema ao carregar a cage " + getDirectory() + ".");
            }
        }
        this.cageConfig = new BridgeCageConfig(getDisplayName(), getIcon(), getRarity(), getRank(), isExclusive(), getPrice());
    }

    public void spawnBlue(final Location location) {
        this.blueBo3Obj.paste(location);
        this.blueSpawned = location;
    }

    public void spawnRed(final Location location) {
        this.redBo3Obj.paste(location);
        this.redSpawned = location;
    }

    public void destroyBlue() {
        this.blueBo3Obj.undo(blueSpawned);
        this.blueSpawned = null;
    }

    public void destroyRed() {
        this.redBo3Obj.undo(redSpawned);
        this.redSpawned = null;
    }

    @Override
    public Cage clone() {
        try {
            return (Cage) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

}