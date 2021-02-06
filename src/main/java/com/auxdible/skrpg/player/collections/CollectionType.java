package com.auxdible.skrpg.player.collections;

import com.auxdible.skrpg.items.Items;
import org.bukkit.entity.LightningStrike;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public enum CollectionType {
    WOOD(Items.WOOD, Arrays.asList(Tiers._1, Tiers._2, Tiers._3), Arrays.asList(Items.BASIC_HILT, Items.ADVANCED_HILT, Items.COMPACT_WOOD)),
    STONE(Items.STONE, Arrays.asList(Tiers._1), Arrays.asList(Items.STONE_SWORD));
    /*IRON(Items.IRON_INGOT, Arrays.asList(Tiers._1, Tiers._2, Tiers._3, Tiers._4, Tiers._5)),
    GOLD(Items.GOLD_INGOT, Arrays.asList(Tiers._1, Tiers._2, Tiers._3, Tiers._4, Tiers._5)),
    DIAMOND(Items.DIAMOND, Arrays.asList(Tiers._1, Tiers._2, Tiers._3, Tiers._4, Tiers._5)),
    EMERALD(Items.EMERALD, Arrays.asList(Tiers._1, Tiers._2, Tiers._3, Tiers._4, Tiers._5)),
    LAPIS(Items.LAPIS_LAZULI, Arrays.asList(Tiers._1, Tiers._2, Tiers._3, Tiers._4, Tiers._5)),
    OBSIDIAN(Items.OBSIDIAN, Arrays.asList(Tiers._1, Tiers._2, Tiers._3, Tiers._4, Tiers._5)),
    ROTTEN_FLESH(Items.ROTTEN_FLESH, Arrays.asList(Tiers._1, Tiers._2, Tiers._3, Tiers._4, Tiers._5)); */
    private Items item;
    private List<Tiers> tiers;
    private List<Items> tierRewards;
    CollectionType(Items item, List<Tiers> tiers, List<Items> tierRewards) {
        this.item = item;
        this.tiers = tiers;
        this.tierRewards = tierRewards;
    }
    public Items getItem() { return item; }
    public List<Items> getTierRewards() { return tierRewards; }
    public List<Tiers> getTiers() { return tiers; }

    public static HashMap<Tiers, Items> generateRewardsMap(CollectionType collectionType) {
        HashMap<Tiers, Items> rewardsMap = new HashMap<>();
        for (int i = 0; i < collectionType.getTiers().size(); i++) {
            rewardsMap.put(collectionType.getTiers().get(i), collectionType.getTierRewards().get(i));
        }
        return rewardsMap;
    }
}
