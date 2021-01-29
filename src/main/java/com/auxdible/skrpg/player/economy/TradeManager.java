package com.auxdible.skrpg.player.economy;

import com.auxdible.skrpg.SKRPG;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.Hash;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class TradeManager {
    private HashMap<Player, Trade> trades;
    private SKRPG skrpg;

    public TradeManager(SKRPG skrpg) {
        this.skrpg = skrpg;
        trades = new HashMap<>();
    }

    public void addTrade(Player player, Trade trade) {
        trades.put(player, trade);
    }

    public void removeTrade(Player player, Trade trade) {
        trades.remove(player, trade);
    }

    public Trade getTrade(Player player) {
        if (trades.containsKey(player)) {
            return trades.get(player);
        }
        return null;
    }
}
