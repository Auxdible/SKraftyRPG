package com.auxdible.skrpg.items;

import com.auxdible.skrpg.items.enchantments.Enchantment;
import com.auxdible.skrpg.items.enchantments.Enchantments;
import com.auxdible.skrpg.utils.Text;

import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemInfo {
    private Items items;
    private Rarity rarity;
    private List<Enchantment> enchantmentsList;
    private RunicStones runicStones;
    private int bonusDamage, bonusStrength, bonusSpeed, bonusEnergy, bonusHealth, bonusDefence;
    public ItemInfo(String id, Rarity rarity, List<Enchantment> enchantmentsList, int bonusDamage,
                    int bonusStrength, int bonusSpeed, int bonusEnergy, int bonusHealth, int bonusDefence, RunicStones runicStones) {
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
    }
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
    public void setRunicStones(RunicStones runicStones) {
        this.runicStones = runicStones;
        bonusDamage = runicStones.getBonusDamage() * rarity.getPriority();
        bonusStrength = runicStones.getBonusStrength() * rarity.getPriority();
        bonusSpeed = runicStones.getBonusSpeed() * rarity.getPriority();
        bonusEnergy = runicStones.getBonusEnergy() * rarity.getPriority();
        bonusHealth = runicStones.getBonusHealth() * rarity.getPriority();
        bonusDefence = runicStones.getBonusDefence() * rarity.getPriority();
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
            return new ItemInfo(items, rarity, enchantmentsList, bonusDamage, bonusStrength, bonusSpeed,
                    bonusEnergy, bonusHealth, bonusDefence, runicStones);

    }
}
