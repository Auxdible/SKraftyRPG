package com.auxdible.skrpg.player.skills;

import com.auxdible.skrpg.items.Items;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public enum MineCrafting {
    OAK_LOG(Items.WOOD, null, Material.OAK_LOG, 6),
    BIRCH_LOG(Items.WOOD, null, Material.BIRCH_LOG, 6),
    SPRUCE_LOG(Items.WOOD, null, Material.SPRUCE_LOG, 6),
    DARK_OAK_LOG(Items.WOOD, null, Material.DARK_OAK_LOG, 6),
    ACACIA_LOG(Items.WOOD, null, Material.ACACIA_LOG, 6),
    JUNGLE_LOG(Items.WOOD, null, Material.JUNGLE_LOG, 6),
    OAK_WOOD(Items.WOOD, null, Material.OAK_WOOD, 6),
    BIRCH_WOOD(Items.WOOD, null, Material.BIRCH_WOOD, 6),
    SPRUCE_WOOD(Items.WOOD, null, Material.SPRUCE_WOOD, 6),
    DARK_OAK_WOOD(Items.WOOD, null, Material.DARK_OAK_WOOD, 6),
    ACACIA_WOOD(Items.WOOD, null, Material.ACACIA_WOOD, 6),
    JUNGLE_WOOD(Items.WOOD, null, Material.JUNGLE_WOOD, 6);
    private Items commonDrop;

    private Material blockObtainedFrom;
    private int xpObtained;
    private List<Drop> drops;
    MineCrafting(Items commonDrop, List<Drop> drops, Material blockObtainedFrom, int xpObtained) {
        this.commonDrop = commonDrop;
        this.drops = drops;
        this.blockObtainedFrom = blockObtainedFrom;
        this.xpObtained = xpObtained;
    }
    public List<Drop> getDrops() { return drops; }
    public Items getCommonDrop() { return commonDrop; }
    public Material getBlockObtainedFrom() { return blockObtainedFrom; }
    public int getXpObtained() { return xpObtained; }
}
