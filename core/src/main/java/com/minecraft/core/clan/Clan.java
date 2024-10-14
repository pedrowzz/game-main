package com.minecraft.core.clan;

import com.minecraft.core.Constants;
import com.minecraft.core.account.Account;
import com.minecraft.core.clan.invite.Invite;
import com.minecraft.core.clan.member.Member;
import com.minecraft.core.clan.member.role.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class Clan {

    private int index;
    private final String name;
    private String tag;
    private List<Member> members;
    private List<Invite> invites;
    private int slots, points;
    private long creation;
    private String color;

    public Clan(int index, String name, String tag, int slots, long creation, int points, String color) {
        this.index = index;
        this.name = name;
        this.tag = tag;
        this.members = new ArrayList<>();
        this.invites = new ArrayList<>();
        this.slots = slots;
        this.points = points;
        this.creation = creation;
        this.color = color;
    }

    public boolean isMember(UUID uniqueId) {
        return members.stream().anyMatch(member -> member.getUniqueId().equals(uniqueId));
    }

    public Member getMember(UUID uniqueId) {
        return members.stream().filter(member -> member.getUniqueId().equals(uniqueId)).findFirst().orElse(null);
    }

    public Member getMember(String name) {
        return members.stream().filter(member -> member.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public Set<Member> getMembers(Role role) {
        return members.stream().filter(member -> member.getRole() == role).collect(Collectors.toSet());
    }


    public void join(Account member) {
        join(member, Role.MEMBER);
    }

    public void join(Account member, Role role) {
        members.add(new Member(member.getUsername(), member.getUniqueId(), role, System.currentTimeMillis()));
    }


    public void quit(UUID member) {

        if (!isMember(member))
            return;

        members.removeIf(m -> m.getUniqueId().equals(member));
    }


    /**
     * CREATE TABLE IF NOT EXISTS `clans` (
     * `index` INT UNSIGNED NOT NULL,
     * `name` VARCHAR(16) NOT NULL,
     * `tag` VARCHAR(16) NOT NULL,
     * `members` LONGTEXT NOT NULL,
     * `slots` INT(100) NOT NULL,
     * `points` INT(100) NOT NULL,
     * `creation` BIGINT NOT NULL,
     * PRIMARY KEY(`index`)
     * );
     */

    public static Clan parse(ResultSet resultSet) {
        if (resultSet == null)
            throw new NullPointerException("Clan service can not parse a null ResultSet.");

        try {
            int index = resultSet.getInt("index");
            String name = resultSet.getString("name");
            String tag = resultSet.getString("tag");
            String rawJson = resultSet.getString("members");
            int slots = resultSet.getInt("slots");
            int points = resultSet.getInt("points");
            long creation = resultSet.getLong("creation");
            String color = resultSet.getString("color");

            List<Member> memberList = new ArrayList<>(Arrays.asList(Constants.GSON.fromJson(rawJson, Member[].class))); // Converting Memebrs JSON to new List.

            return new Clan(index, name, tag, memberList, new ArrayList<>(), slots, points, creation, color.toUpperCase());
        } catch (Exception e) {
            throw new IllegalStateException("Clan parse fail.");
        }
    }

    public boolean hasPendingInvite(UUID uuid) {
        return invites.stream().anyMatch(c -> c.getStatus() == Invite.Status.PENDING && !c.expired() && c.getInvite().equals(uuid));
    }

    public boolean hasRecentInvite(UUID uuid) { // 12 minutes
        return invites.stream().anyMatch(c -> c.getStatus() != Invite.Status.ACCEPTED && !c.expired() && c.getRelease() + 720000 > System.currentTimeMillis() && !c.expired() && c.getInvite().equals(uuid));
    }

    public Invite getPendingInvite(UUID uuid) {
        return invites.stream().filter(c -> c.getStatus() == Invite.Status.PENDING && !c.expired() && c.getInvite().equals(uuid)).findFirst().orElse(null);
    }

    public boolean isFull() {
        return members.size() >= getSlots();
    }
}
