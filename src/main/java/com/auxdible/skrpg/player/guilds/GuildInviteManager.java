package com.auxdible.skrpg.player.guilds;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.player.economy.Trade;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class GuildInviteManager {
    private HashMap<Player, Guild> invites;
    private SKRPG skrpg;

    public GuildInviteManager(SKRPG skrpg) {
        this.skrpg = skrpg;
        invites = new HashMap<>();
    }

    public void addInvite(Player player, Guild invite) {
        invites.put(player, invite);
    }

    public void removeInvite(Player player, Guild invite) {
        invites.remove(player, invite);
    }

    public Guild getInvite(Player player) {
        if (invites.containsKey(player)) {
            return invites.get(player);
        }
        return null;
    }
}
