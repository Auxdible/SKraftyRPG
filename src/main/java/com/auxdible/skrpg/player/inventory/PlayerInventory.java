package com.auxdible.skrpg.player.inventory;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.ItemInfo;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.items.SKRPGItemStack;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.Text;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class PlayerInventory {
    private List<SKRPGItemStack> inventorySlots;
    private InventoryEquipment inventoryEquipment;
    private PlayerData holder;
    private SKRPG skrpg;
    public PlayerInventory(SKRPG skrpg, PlayerData holder, List<SKRPGItemStack> inventorySlots, List<SKRPGItemStack> equipment) {
        this.holder = holder;
        this.inventorySlots = inventorySlots;
        this.inventoryEquipment = new InventoryEquipment(equipment);
        this.skrpg = skrpg;
    }

    public SKRPGItemStack getItemInMainHand() {
            return new SKRPGItemStack(ItemInfo.parseItemInfo(Bukkit.getPlayer(holder.getUuid()).getInventory().getItemInMainHand()), Bukkit.getPlayer(holder.getUuid()).getInventory().getItemInMainHand().getAmount());
         }
    public void setItemInMainHand(SKRPGItemStack skrpgItemStack) {
        inventorySlots.set(Bukkit.getPlayer(holder.getUuid()).getInventory().getHeldItemSlot(), skrpgItemStack);
        updateInventory(Bukkit.getPlayer(holder.getUuid()));
    }

    public void addItem(SKRPGItemStack skrpgItemStack) {
        if (inventorySlots.size() <= 37) {
            inventorySlots.add(skrpgItemStack);
        }
        updateInventory(Bukkit.getPlayer(holder.getUuid()));
    }
    public PlayerData getHolder() { return holder;  }

    public void setItem(int i, SKRPGItemStack item) { getContents().set(i, item);
    updateInventory(Bukkit.getPlayer(holder.getUuid())); }

    public SKRPGItemStack getItem(int i) {  return getContents().get(i);  }

    public List<SKRPGItemStack> getContents() { return inventorySlots; }

    public InventoryEquipment getInventoryEquipment() { return inventoryEquipment; }

    public void removeItem(Items item) {
        for (SKRPGItemStack skrpgItemStack : inventorySlots) {

            if (skrpgItemStack != null && skrpgItemStack.getItemInfo().getItem().equals(item)) {
                skrpg.getLogger().info("item removed | " + skrpgItemStack.getItemInfo().getItem() + " | " + skrpgItemStack.getAmount() + " | " + inventorySlots.indexOf(skrpgItemStack));
                inventorySlots.set(inventorySlots.indexOf(skrpgItemStack), null);
                break;
            }
        }
        updateInventory(Bukkit.getPlayer(holder.getUuid()));
    }
    public void setContents(List<SKRPGItemStack> contents) { this.inventorySlots = contents; updateInventory(Bukkit.getPlayer(holder.getUuid())); }
    public String generateInventorySave() {
        StringJoiner items = new StringJoiner(",");
        for (SKRPGItemStack itemStack : getContents()) {
                if (itemStack == null) {
                    items.add("0*0");
                } else {
                    items.add(itemStack.getAmount() + "*" + itemStack.getItemInfo().generateItemInfoString());
                }
        }
        if (inventoryEquipment == null) {
            inventoryEquipment = new InventoryEquipment(new ArrayList<>());
        }
        for (SKRPGItemStack itemStack : inventoryEquipment.getContentsAsList()) {
            if (itemStack == null) {
                items.add("0*0");
            } else {
                items.add(itemStack.getAmount() + "*" + itemStack.getItemInfo().generateItemInfoString());
            }
        }
        return items.toString();
    }
    public org.bukkit.inventory.PlayerInventory updateInventory(Player p) {

        if (p.getGameMode() == GameMode.CREATIVE) {
            return p.getInventory();
        }
        List<ItemStack> itemstacks = new ArrayList<>();

        for (SKRPGItemStack skrpgItemStack : getContents()) {

            ItemStack itemStack = null;
            if (skrpgItemStack != null) {
                if (skrpgItemStack.getItemInfo() != null) {
                    itemStack = Items.buildItem(skrpgItemStack.getItemInfo().getItem());
                    skrpg.getLogger().info(skrpgItemStack.getItemInfo().getItem().getName() + " update item");
                    Items.updateItem(itemStack, skrpgItemStack.getItemInfo());
                    itemStack.setAmount(skrpgItemStack.getAmount());
                }
            }
            itemstacks.add(itemStack);
        }
        for (int i = 0; i < getContents().size(); i++) {
            if (i >= 9) {
                p.getInventory().setItem(i + 4, itemstacks.get(i));
            } else {
                p.getInventory().setItem(i, itemstacks.get(i));
            }
        }
        if (getInventoryEquipment() == null) {
            inventoryEquipment = new InventoryEquipment(new ArrayList<>());
        }
        if (getInventoryEquipment().getHelmet() != null && getInventoryEquipment().getHelmet().getItemInfo() != null) {
            ItemStack itemStack = Items.buildItem(getInventoryEquipment().getHelmet().getItemInfo().getItem());
            Items.updateItem(itemStack, getInventoryEquipment().getHelmet().getItemInfo());
            itemStack.setAmount(getInventoryEquipment().getHelmet().getAmount());
            p.getInventory().setHelmet(itemStack);
        }
        if (getInventoryEquipment().getChestplate() != null && getInventoryEquipment().getChestplate().getItemInfo() != null) {
            ItemStack itemStack = Items.buildItem(getInventoryEquipment().getChestplate().getItemInfo().getItem());
            Items.updateItem(itemStack, getInventoryEquipment().getChestplate().getItemInfo());
            itemStack.setAmount(getInventoryEquipment().getChestplate().getAmount());
            p.getInventory().setChestplate(itemStack);
        }
        if (getInventoryEquipment().getLeggings() != null && getInventoryEquipment().getLeggings().getItemInfo() != null) {
            ItemStack itemStack = Items.buildItem(getInventoryEquipment().getLeggings().getItemInfo().getItem());
            Items.updateItem(itemStack, getInventoryEquipment().getLeggings().getItemInfo());
            itemStack.setAmount(getInventoryEquipment().getLeggings().getAmount());
            p.getInventory().setLeggings(itemStack);
        }
        if (getInventoryEquipment().getBoots() != null && getInventoryEquipment().getBoots().getItemInfo() != null) {
            ItemStack itemStack = Items.buildItem(getInventoryEquipment().getBoots().getItemInfo().getItem());
            Items.updateItem(itemStack, getInventoryEquipment().getBoots().getItemInfo());
            itemStack.setAmount(getInventoryEquipment().getBoots().getAmount());
            p.getInventory().setBoots(itemStack);
        }
        if (getInventoryEquipment().getRing() != null && getInventoryEquipment().getRing().getItemInfo() != null) {
            ItemStack itemStack = Items.buildItem(getInventoryEquipment().getRing().getItemInfo().getItem());
            Items.updateItem(itemStack, getInventoryEquipment().getRing().getItemInfo());
            itemStack.setAmount(getInventoryEquipment().getRing().getAmount());
            net.minecraft.server.v1_16_R3.ItemStack ringNMS = CraftItemStack.asNMSCopy(itemStack);
            NBTTagCompound ringTagCompound = (ringNMS.hasTag()) ? ringNMS.getTag() : new NBTTagCompound();
            ringTagCompound.setString("item", "RING");
            p.getInventory().setItem(9, CraftItemStack.asBukkitCopy(ringNMS));
        } else {
            org.bukkit.inventory.ItemStack ring = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 0).setName("&cNO RING").setLore(
                    Arrays.asList(" ", Text.color("&7&oPut a ring in this slot and it's buffs"), Text.color("&7&owill be activated!"))).asItem();
            net.minecraft.server.v1_16_R3.ItemStack ringNMS = CraftItemStack.asNMSCopy(ring);
            NBTTagCompound ringTagCompound = (ringNMS.hasTag()) ? ringNMS.getTag() : new NBTTagCompound();
            ringTagCompound.setString("item", "RING");
            p.getInventory().setItem(9, CraftItemStack.asBukkitCopy(ringNMS));
        }
        if (getInventoryEquipment().getHeadband() != null && getInventoryEquipment().getHeadband().getItemInfo() != null) {
            ItemStack itemStack = Items.buildItem(getInventoryEquipment().getHeadband().getItemInfo().getItem());
            Items.updateItem(itemStack, getInventoryEquipment().getHeadband().getItemInfo());
            itemStack.setAmount(getInventoryEquipment().getHeadband().getAmount());
            net.minecraft.server.v1_16_R3.ItemStack ringNMS = CraftItemStack.asNMSCopy(itemStack);
            NBTTagCompound ringTagCompound = (ringNMS.hasTag()) ? ringNMS.getTag() : new NBTTagCompound();
            ringTagCompound.setString("item", "HEADBAND");
            p.getInventory().setItem(10, CraftItemStack.asBukkitCopy(ringNMS));
        } else {
            org.bukkit.inventory.ItemStack headband = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 0).setName("&cNO HEADBAND").setLore(
                    Arrays.asList(" ", Text.color("&7&oPut a headband in this slot and it's buffs"), Text.color("&7&owill be activated!"))).asItem();
            net.minecraft.server.v1_16_R3.ItemStack headbandNMS = CraftItemStack.asNMSCopy(headband);
            NBTTagCompound headbandTagCompound = (headbandNMS.hasTag()) ? headbandNMS.getTag() : new NBTTagCompound();
            headbandTagCompound.setString("item", "HEADBAND");
            p.getInventory().setItem(10, CraftItemStack.asBukkitCopy(headbandNMS));
        }
        if (getInventoryEquipment().getNecklace() != null && getInventoryEquipment().getNecklace().getItemInfo() != null) {
            ItemStack itemStack = Items.buildItem(getInventoryEquipment().getNecklace().getItemInfo().getItem());
            Items.updateItem(itemStack, getInventoryEquipment().getNecklace().getItemInfo());
            itemStack.setAmount(getInventoryEquipment().getNecklace().getAmount());
            net.minecraft.server.v1_16_R3.ItemStack ringNMS = CraftItemStack.asNMSCopy(itemStack);
            NBTTagCompound ringTagCompound = (ringNMS.hasTag()) ? ringNMS.getTag() : new NBTTagCompound();
            ringTagCompound.setString("item", "NECKLACE");
            p.getInventory().setItem(11, CraftItemStack.asBukkitCopy(ringNMS));
        } else {
            org.bukkit.inventory.ItemStack necklace = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 0).setName("&cNO NECKLACE").setLore(
                    Arrays.asList(" ", Text.color("&7&oPut a necklace in this slot and it's buffs"), Text.color("&7&owill be activated!"))).asItem();
            net.minecraft.server.v1_16_R3.ItemStack necklaceNMS = CraftItemStack.asNMSCopy(necklace);
            NBTTagCompound necklaceTagCompound = (necklaceNMS.hasTag()) ? necklaceNMS.getTag() : new NBTTagCompound();
            necklaceTagCompound.setString("item", "NECKLACE");
            p.getInventory().setItem(11, CraftItemStack.asBukkitCopy(necklaceNMS));
        }
        if (getInventoryEquipment().getArtifact() != null && getInventoryEquipment().getArtifact().getItemInfo() != null) {
            ItemStack itemStack = Items.buildItem(getInventoryEquipment().getArtifact().getItemInfo().getItem());
            Items.updateItem(itemStack, getInventoryEquipment().getArtifact().getItemInfo());
            itemStack.setAmount(getInventoryEquipment().getArtifact().getAmount());
            net.minecraft.server.v1_16_R3.ItemStack ringNMS = CraftItemStack.asNMSCopy(itemStack);
            NBTTagCompound ringTagCompound = (ringNMS.hasTag()) ? ringNMS.getTag() : new NBTTagCompound();
            ringTagCompound.setString("item", "ARTIFACT");
            p.getInventory().setItem(12, CraftItemStack.asBukkitCopy(ringNMS));
        } else {
            org.bukkit.inventory.ItemStack artifact = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 0).setName("&cNO ARTIFACT").setLore(
                    Arrays.asList(" ", Text.color("&7&oPut a artifact in this slot and it's buffs"), Text.color("&7&owill be activated!"))).asItem();
            net.minecraft.server.v1_16_R3.ItemStack artifactNMS = CraftItemStack.asNMSCopy(artifact);
            NBTTagCompound artifactTagCompound = (artifactNMS.hasTag()) ? artifactNMS.getTag() : new NBTTagCompound();
            artifactTagCompound.setString("item", "ARTIFACT");
            p.getInventory().setItem(12, CraftItemStack.asBukkitCopy(artifactNMS));
        }
        skrpg.getLogger().info(getInventoryEquipment().getRing() + " " +
                getInventoryEquipment().getHeadband() + " " +
                getInventoryEquipment().getNecklace() + " " +
                getInventoryEquipment().getArtifact() + " " +
                getInventoryEquipment().getHelmet() + " " +
                getInventoryEquipment().getChestplate() + " " +
                getInventoryEquipment().getLeggings() + " " +
                getInventoryEquipment().getBoots() + " UPDATE");
        new BukkitRunnable() {

            @Override
            public void run() {
                p.updateInventory();
            }
        }.runTaskLater(skrpg, 1L);
        return p.getInventory();
    }
}
