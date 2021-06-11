package com.auxdible.skrpg.player.inventory;

import com.auxdible.skrpg.items.ItemInfo;
import com.auxdible.skrpg.items.ItemType;
import com.auxdible.skrpg.items.SKRPGItemStack;

import java.util.Arrays;
import java.util.List;

public class InventoryEquipment {
    private SKRPGItemStack helmet, chestplate, leggings, boots, ring, necklace, headband, artifact;
    public InventoryEquipment(List<SKRPGItemStack> equipment) {
        if (!equipment.isEmpty()) {
            for (SKRPGItemStack equipmentItem : equipment) {

                addItem(equipmentItem);
            }
        } else {
            System.out.println("empty list!");
        }
    }

    public SKRPGItemStack getBoots() { return boots; }

    public SKRPGItemStack getLeggings() { return leggings; }

    public SKRPGItemStack getChestplate() { return chestplate; }

    public SKRPGItemStack getHelmet() { return helmet; }

    public SKRPGItemStack getRing() { return ring; }

    public SKRPGItemStack getArtifact() { return artifact; }

    public SKRPGItemStack getHeadband() { return headband; }

    public SKRPGItemStack getNecklace() { return necklace; }

    public void setArtifact(SKRPGItemStack artifact) { this.artifact = artifact; }

    public void setBoots(SKRPGItemStack boots) { this.boots = boots; }

    public void setChestplate(SKRPGItemStack chestplate) { this.chestplate = chestplate; }

    public void setHeadband(SKRPGItemStack headband) { this.headband = headband; }

    public void setHelmet(SKRPGItemStack helmet) { this.helmet = helmet; }

    public void setLeggings(SKRPGItemStack leggings) { this.leggings = leggings; }

    public void setNecklace(SKRPGItemStack necklace) { this.necklace = necklace; }

    public void setRing(SKRPGItemStack ring) { this.ring = ring; }
    public void removeItem(ItemType itemType) {
        if (itemType == ItemType.RING) {
            setRing(null);
        } else if (itemType == ItemType.NECKLACE) {
            setNecklace(null);
        } else if (itemType == ItemType.HEADBAND) {
            setHeadband(null);
        }  else if (itemType == ItemType.ARTIFACT) {
            setArtifact(null);
        } else if (itemType == ItemType.HELMET) {
            setHelmet(null);
        } else if (itemType == ItemType.CHESTPLATE) {
            setChestplate(null);
        } else if (itemType == ItemType.LEGGINGS) {
            setLeggings(null);
        } else if (itemType == ItemType.BOOTS) {
            setBoots(null);
        }
    }
    public void addItem(SKRPGItemStack item) {
        if (item.getItemInfo().getItem().getItemType() == ItemType.RING) {
            setRing(item);
        } else if (item.getItemInfo().getItem().getItemType() == ItemType.NECKLACE) {
            setNecklace(item);
        } else if (item.getItemInfo().getItem().getItemType() == ItemType.HEADBAND) {
            setHeadband(item);
        }  else if (item.getItemInfo().getItem().getItemType() == ItemType.ARTIFACT) {
            setArtifact(item);
        } else if (item.getItemInfo().getItem().getItemType() == ItemType.HELMET) {
            setHelmet(item);
        } else if (item.getItemInfo().getItem().getItemType() == ItemType.CHESTPLATE) {
            setChestplate(item);
        } else if (item.getItemInfo().getItem().getItemType() == ItemType.LEGGINGS) {
            setLeggings(item);
        } else if (item.getItemInfo().getItem().getItemType() == ItemType.BOOTS) {
            setBoots(item);
        }
    }
    public List<SKRPGItemStack> getContentsAsList() {
        return Arrays.asList(helmet, chestplate, leggings, boots, ring, headband, necklace, artifact);
    }
}
