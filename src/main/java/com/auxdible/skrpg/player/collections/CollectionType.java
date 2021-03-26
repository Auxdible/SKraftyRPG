package com.auxdible.skrpg.player.collections;

import com.auxdible.skrpg.items.Items;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public enum CollectionType {
    WOOD(Items.WOOD, Arrays.asList(Tiers._1, Tiers._2, Tiers._3, Tiers._4, Tiers._5, Tiers._6),
            Arrays.asList(Arrays.asList(Items.BASIC_HILT),
            Arrays.asList(Items.ADVANCED_HILT), Arrays.asList(Items.COMPACT_WOOD), Arrays.asList(Items.EXPERTISE_HILT),
                    Arrays.asList(Items.LEGENDS_HILT), Arrays.asList(Items.MASTERS_HILT))),
    STONE(Items.STONE, Arrays.asList(Tiers._1, Tiers._2), Arrays.asList(Arrays.asList(Items.COMPACT_STONE), Arrays.asList(Items.COMPACT_GEMSTONE))),
    OBSIDIAN(Items.OBSIDIAN, Arrays.asList(Tiers._1, Tiers._2), Arrays.asList(Arrays.asList(Items.COMPACT_OBSIDIAN), Arrays.asList(
            Items.MINER_HELMET, Items.MINER_CHESTPLATE, Items.MINER_LEGGINGS, Items.MINER_BOOTS))),
    IRON(Items.IRON_INGOT, Arrays.asList(Tiers._1), Arrays.asList(Arrays.asList(Items.COMPACT_IRON))),
    DIAMONDS(Items.DIAMOND, Arrays.asList(Tiers._1), Arrays.asList(Arrays.asList(Items.COMPACT_DIAMOND))),
    CRYSTALLITE(Items.CRYSTALLITE, Arrays.asList(Tiers._1), Arrays.asList(Arrays.asList(Items.COMPACT_CRYSTALLITE))),
    STRING(Items.STRING, Arrays.asList(Tiers._1), Arrays.asList(Arrays.asList(Items.COMPACT_STRING)));
    private Items item;
    private List<Tiers> tiers;
    private List<List<Items>> tierRewards;
    CollectionType(Items item, List<Tiers> tiers, List<List<Items>> tierRewards) {
        this.item = item;
        this.tiers = tiers;
        this.tierRewards = tierRewards;
    }
    public Items getItem() { return item; }
    public List<List<Items>> getTierRewards() { return tierRewards; }
    public List<Tiers> getTiers() { return tiers; }

    public static HashMap<Tiers, List<Items>> generateRewardsMap(CollectionType collectionType) {
        HashMap<Tiers, List<Items>> rewardsMap = new HashMap<>();
        for (int i = 0; i < collectionType.getTiers().size(); i++) {
            rewardsMap.put(collectionType.getTiers().get(i), collectionType.getTierRewards().get(i ));
        }
        return rewardsMap;
    }
}
