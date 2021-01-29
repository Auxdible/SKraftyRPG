package com.auxdible.skrpg.player.skills;

import com.auxdible.skrpg.items.Items;
import org.bukkit.Material;

import java.util.List;

public enum MinePlant {
    WHEAT(Items.WHEAT, null, Material.WHEAT, 1),
    CARROT(Items.CARROT, null, Material.CARROTS, 3),
    CANE(Items.CANE, null, Material.SUGAR_CANE, 2),
    POTATO(Items.POTATO, null, Material.POTATOES, 3),
    SWEET_BERRIES(Items.SWEET_BERRIES, null, Material.SWEET_BERRY_BUSH, 7),
    BEETROOT(Items.BEETROOT, null, Material.BEETROOTS, 5),
    PUMPKIN(Items.PUMPKIN, null, Material.PUMPKIN, 10),
    MELON(Items.MELON, null, Material.MELON, 10);
    private Items commonDrop;
    private Material blockObtainedFrom;
    private int xpObtained;
    private List<Drop> drops;
    MinePlant(Items commonDrop, List<Drop> drops, Material blockObtainedFrom, int xpObtained) {
        this.commonDrop = commonDrop;
        this.drops = drops;
        this.blockObtainedFrom = blockObtainedFrom;
        this.xpObtained = xpObtained;
    }
    public List<Drop> getDrops() { return drops; }
    public Items getObtainedItem() { return commonDrop; }
    public Material getBlockObtainedFrom() { return blockObtainedFrom; }
    public int getXpObtained() { return xpObtained; }
}
