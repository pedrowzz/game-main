package com.minecraft.hub.lobby.hg.modules;

import com.minecraft.core.bukkit.util.hologram.Hologram;
import com.minecraft.core.bukkit.util.npc.NPC;
import com.minecraft.hub.lobby.Lobby;
import com.minecraft.hub.user.User;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.TimeZone;

public class Tournament {

    private final Lobby lobby;
    private final Location location, hologramLocation;
    private final NPC npc;

    private final long releaseDate, tournamentDate, salesDate;
    private boolean hasStarted, hasSalesStared, isTournamentHappening;

    public Tournament(final Lobby lobby) {
        this.lobby = lobby;
        this.location = new Location(lobby.getWorld(), 10, 10, 10, 0, 0);
        this.hologramLocation = this.location.clone().add(0, 2.25, 0);

        this.npc = NPC.builder().location(location).interactExecutor(this::interactExecutor).build();

        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.set(2022, Calendar.JANUARY, 7, 18, 0);

        this.releaseDate = calendar.getTimeInMillis();

        calendar.set(2022, Calendar.JANUARY, 10, 18, 0);

        this.salesDate = calendar.getTimeInMillis();

        calendar.set(2022, Calendar.JANUARY, 28, 18, 0);

        this.tournamentDate = calendar.getTimeInMillis();
    }

    public void handle(final User user) {
        if (!hasStarted)
            return;

        final Player player = user.getPlayer();

        final Hologram hologram = new Hologram(player, this.hologramLocation, "§6§lTOURNAMENT §7- §a§l" + dateFormat.format(this.tournamentDate), isTournamentHappening ? "§e§lEM PROGRESSO" : this.hasSalesStared ? "§e§lADQUIRA SEU INGRESSO" : "§e§lMAIS INFORMAÇÕES");
        hologram.show();

        npc.clone(player).spawn(true);
    }

    public void interactExecutor(final Player player, final NPC npc, final NPC.Interact.ClickType clickType) {

    }

    public void verifyDate() {
        if (!hasStarted && hasToStart()) {
            hasStarted = true;
        }

        if (!hasSalesStared && hasToSell()) {
            hasSalesStared = true;
        }
    }

    public boolean hasToStart() {
        return Calendar.getInstance(timeZone).getTimeInMillis() >= this.releaseDate;
    }

    public boolean hasToSell() {
        return Calendar.getInstance(timeZone).getTimeInMillis() >= this.salesDate;
    }

    protected final TimeZone timeZone = TimeZone.getTimeZone(ZoneId.of("America/Brazil"));
    protected final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd");

}