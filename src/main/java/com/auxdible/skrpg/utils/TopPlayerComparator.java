package com.auxdible.skrpg.utils;

import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.Map;

public class TopPlayerComparator implements Comparator<Player> {

    Map<Player, Integer> base;
    public TopPlayerComparator(Map<Player, Integer> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.

    public int compare(Player a, Player b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }

}
