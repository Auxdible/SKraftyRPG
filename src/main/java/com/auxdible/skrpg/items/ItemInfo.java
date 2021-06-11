package com.auxdible.skrpg.items;

import com.auxdible.skrpg.items.enchantments.Enchantment;
import com.auxdible.skrpg.items.enchantments.Enchantments;
import com.auxdible.skrpg.utils.Text;

import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class ItemInfo {
    private Items items;
    private Rarity rarity;
    private List<Enchantment> enchantmentsList;
    private RunicStones runicStones;
    private Quality quality;
    private int bonusDamage, bonusStrength, bonusSpeed, bonusEnergy, bonusHealth, bonusDefence;
    private boolean processed, cooked;
    private Enchantment enchantmentScrollEnchantment;
    private int runicPointCost;
    public ItemInfo(String id, Rarity rarity, List<Enchantment> enchantmentsList, int bonusDamage,
                    int bonusStrength, int bonusSpeed, int bonusEnergy, int bonusHealth, int bonusDefence, RunicStones runicStones,
                    Quality quality, boolean cooked, boolean processed, Enchantment enchantmentScrollEnchant, int runicPointCost) {
        this.items = Items.getItem(id);
        this.rarity = rarity;
        this.enchantmentsList = enchantmentsList;
        this.bonusDamage = bonusDamage;
        this.bonusStrength = bonusStrength;
        this.bonusSpeed = bonusSpeed;
        this.bonusEnergy = bonusEnergy;
        this.bonusHealth = bonusHealth;
        this.bonusDefence = bonusDefence;
        this.runicStones = runicStones;
        this.quality = quality;
        this.cooked = cooked;
        this.processed = processed;
        this.enchantmentScrollEnchantment = enchantmentScrollEnchant;
        this.runicPointCost = runicPointCost;
    }

    public int getRunicPointCost() { return runicPointCost; }

    public Enchantment getEnchantmentScrollEnchantment() { return enchantmentScrollEnchantment; }
    public Items getItem() { return items; }
    public List<Enchantment> getEnchantmentsList() { return enchantmentsList; }
    public Rarity getRarity() { return rarity; }
    public void addEnchantment(Enchantment enchantment) {
        if (hasEnchantment(enchantment.getEnchantmentType())) {
            getEnchantmentsList().remove(getEnchantment(enchantment.getEnchantmentType()));

        }
        getEnchantmentsList().add(enchantment);
    }
    public int getBonusDefence() { return bonusDefence; }
    public int getBonusDamage() { return bonusDamage; }
    public int getBonusHealth() { return bonusHealth; }
    public int getBonusSpeed() { return bonusSpeed; }
    public int getBonusStrength() { return bonusStrength; }
    public int getBonusEnergy() { return bonusEnergy; }
    public boolean isProcessed() { return processed; }
    public boolean isCooked() { return cooked; }
    public Quality getQuality() { return quality; }
    public Enchantment getEnchantment(Enchantments enchantmentType) {
        for (Enchantment enchantment : getEnchantmentsList()) {
            if (enchantment.getEnchantmentType() == enchantmentType) {
                return enchantment;
            }
        }
        return null;
    }
    public boolean hasEnchantment(Enchantments enchantments) {
        for (Enchantment enchantment : getEnchantmentsList()) {
            if (enchantment.getEnchantmentType() == enchantments) {
                return true;
            }
        }
        return false;
    }
    public RunicStones getRunicStones() { return runicStones; }
    public void setQuality(Quality quality) { this.quality = quality; }

    public void setRunicStones(RunicStones runicStones) {
        this.runicStones = runicStones;
        bonusDamage = runicStones.getBonusDamage() * rarity.getPriority();
        bonusStrength = runicStones.getBonusStrength() * rarity.getPriority();
        bonusSpeed = runicStones.getBonusSpeed() * rarity.getPriority();
        bonusEnergy = runicStones.getBonusEnergy() * rarity.getPriority();
        bonusHealth = runicStones.getBonusHealth() * rarity.getPriority();
        bonusDefence = runicStones.getBonusDefence() * rarity.getPriority();
    }

    public void setProcessed(boolean processed) { this.processed = processed; }
    public void setCooked(boolean cooked) { this.cooked = cooked; }
    public String generateItemInfoString() {
        StringJoiner data = new StringJoiner(":");
        data.add(items.getId());
        data.add(rarity.toString());
        if (getQuality() == null) {
            data.add("NONE");
        } else {
            data.add(getQuality().toString());
        }

        if (getRunicStones() == null) {
            data.add("NONE");
        } else {
            data.add(getRunicStones().toString());
        }
        if (getEnchantmentsList() == null) {
            data.add("NONE");
        } else {
            if (getEnchantmentsList().isEmpty()) {
                data.add("NONE");
            } else {
                StringJoiner enchantmentJoiner = new StringJoiner("$");
                for (Enchantment enchantment : getEnchantmentsList()) {
                    enchantmentJoiner.add(enchantment.getEnchantmentType().toString() + "#" + enchantment.getLevel());
                }
                data.add(enchantmentJoiner.toString());
            }
        }
        data.add(isProcessed() + "");
        data.add(isCooked() + "");
        if (items.getItemType().equals(ItemType.ENCHANTMENT_SCROLL)) {
            data.add("enchantmentScroll");
            data.add(getRunicPointCost() + "");
            data.add(getEnchantmentScrollEnchantment().getEnchantmentType().toString() + "#" + getEnchantmentScrollEnchantment().getLevel());
        }
        return data.toString();
    }
    public static ItemInfo parseItemInfo(String data) {
        List<String> dataList = Arrays.asList(data.split(":"));
        Items item = null;
        Rarity rarity = null;
        Quality quality = null;
        RunicStones runicStones = null;
        boolean cooked = false;
        boolean processed = false;
        int runicsPointsCost = 0;
        Enchantment scrollEnchantment = null;
        List<Enchantment> enchantments = new ArrayList<>();
        int bonusDamage = 0,
                bonusStrength = 0, bonusSpeed = 0,
                bonusEnergy = 0, bonusHealth = 0,
                bonusDefence = 0;
        if (!dataList.isEmpty()) {
            if (dataList.size() >= 2) {
                item = Items.getItem(dataList.get(0));
                rarity = Rarity.valueOf(dataList.get(1));
            }
            if (dataList.size() >= 3) {
                if (!dataList.get(2).equals("NONE")) {
                    quality = Quality.valueOf(dataList.get(2));

                }
            }
            if (dataList.size() >= 4) {
                if (!dataList.get(3).equals("NONE")) {
                    runicStones = RunicStones.valueOf(dataList.get(3));
                    bonusDamage = runicStones.getBonusDamage();
                    bonusDefence = runicStones.getBonusDefence();
                    bonusHealth = runicStones.getBonusHealth();
                    bonusEnergy = runicStones.getBonusEnergy();
                    bonusSpeed = runicStones.getBonusSpeed();
                    bonusStrength = runicStones.getBonusStrength();
                }
            }
            if (dataList.size() >= 5) {
                if (!dataList.get(4).equals("NONE")) {
                    for (String enchantment : dataList.get(4).split("$")) {
                        List<String> ench = Arrays.asList(enchantment.split("#"));
                        enchantments.add(new Enchantment(Enchantments.valueOf(ench.get(0)),
                                Integer.parseInt(ench.get(1))));
                    }
                }
            }
            if (dataList.size() >= 7) {
                 cooked = Boolean.getBoolean(dataList.get(5));
                 processed = Boolean.getBoolean(dataList.get(6));
            }
            if (dataList.contains("enchantmentScroll")) {
                runicsPointsCost = Integer.parseInt(dataList.get(8));
                scrollEnchantment = new Enchantment(Enchantments.getEnchantment(Arrays.asList(dataList.get(9).split("#")).get(0)),
                        Integer.parseInt(Arrays.asList(dataList.get(9).split("#")).get(1)));
            }
            if (item != null) {
                return new ItemInfo(item.getId(), rarity, enchantments, bonusDamage, bonusStrength, bonusSpeed,
                        bonusEnergy, bonusHealth, bonusDefence, runicStones, quality, cooked, processed, scrollEnchantment, runicsPointsCost);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
    public static ItemInfo parseItemInfo(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().equals(Material.AIR)) {

            return null;
        }
        net.minecraft.server.v1_16_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound compoundNbt = nmsStack.getTag();
        if (compoundNbt == null) {
            itemStack.setType(Material.AIR);
            return null;
        }
        if (!compoundNbt.hasKey("rpgItem")) {
            itemStack.setType(Material.AIR);
            return null;
        }

        if (!compoundNbt.hasKey("itemIdRpg")) {
            itemStack.setType(Material.AIR);
            return null;
        }
        String items = compoundNbt.getString("itemIdRpg");
        List<Enchantment> enchantmentsList = new ArrayList<>();
        if (compoundNbt.hasKey("enchantmentsRpg")) {
            String enchantments = compoundNbt.getString("enchantmentsRpg");
            if (!enchantments.contains("NONE")) {
                if (!enchantments.equals("")) {
                    for (String allEnchants : enchantments.split("#")) {
                        List<String> enchant = Arrays.asList(allEnchants.split(":"));
                        enchantmentsList.add(new Enchantment(Enchantments.valueOf(enchant.get(0)), Integer.parseInt(enchant.get(1))));
                    }
                }
            }
        }
        Rarity rarity;
        if (compoundNbt.hasKey("rarityRpg")) {
            rarity = Rarity.valueOf(compoundNbt.getString("rarityRpg"));
        } else {
            rarity = Items.getItem(items).getRarity();
        }
        RunicStones runicStones = null;
        int bonusDamage = 0,
                bonusStrength = 0, bonusSpeed = 0,
                bonusEnergy = 0, bonusHealth = 0,
                bonusDefence = 0;
        if (compoundNbt.hasKey("runicStoneRpg")) {
            if (!compoundNbt.getString("runicStoneRpg").contains("NONE")) {
                if (!compoundNbt.getString("runicStoneRpg").equals("")) {
                    runicStones = RunicStones.valueOf(compoundNbt.getString("runicStoneRpg"));
                    bonusDamage = runicStones.getBonusDamage() * rarity.getPriority();
                    bonusStrength = runicStones.getBonusStrength() * rarity.getPriority();
                    bonusSpeed = runicStones.getBonusSpeed() * rarity.getPriority();
                    bonusEnergy = runicStones.getBonusEnergy() * rarity.getPriority();
                    bonusHealth = runicStones.getBonusHealth() * rarity.getPriority();
                    bonusDefence = runicStones.getBonusDefence() * rarity.getPriority();
                }
            }

        }
        Quality quality = null;
        if (compoundNbt.hasKey("qualityRpg")) {
            quality = Quality.getQualityFromStar(compoundNbt.getInt("qualityRpg"));
        }
        boolean cooked = false, processed = false;
        if (compoundNbt.hasKey("cookedRpg")) {
            cooked = compoundNbt.getBoolean("cookedRpg");
        }
        if (compoundNbt.hasKey("processedRpg")) {
            processed = compoundNbt.getBoolean("processedRpg");
        }
        Enchantment scrollEnchantment = null;
        int runicPointCost = -1;
        if (compoundNbt.hasKey("isEnchantmentScroll")) {
            if (compoundNbt.hasKey("enchantmentScrollEnchantment")) {
                scrollEnchantment = new Enchantment(Enchantments.getEnchantment(compoundNbt.getString("enchantmentScrollEnchantment")), compoundNbt.getInt("enchantmentScrollLevel"));
                runicPointCost = compoundNbt.getInt("enchantmentScrollCost");
            }
        }
            return new ItemInfo(items, rarity, enchantmentsList, bonusDamage, bonusStrength, bonusSpeed,
                    bonusEnergy, bonusHealth, bonusDefence, runicStones, quality, cooked, processed, scrollEnchantment, runicPointCost);

    }
}
