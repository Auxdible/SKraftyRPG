package com.auxdible.skrpg.mobs.npcs.npcs;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.mobs.npcs.NPC;
import com.auxdible.skrpg.mobs.npcs.NpcType;
import com.auxdible.skrpg.mobs.npcs.PurchasableItem;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.Text;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Hiltcrafter implements NPC {
    private Entity entity;
    private int id;
    private Location location;
    public Hiltcrafter(int id, Location location) {
        this.id = id;
        this.location = location;
    }
    @Override
    public void buildNPC(SKRPG skrpg) {
        Villager villager = (Villager) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")).spawnEntity(location, EntityType.VILLAGER);
        villager.setAI(false);
        villager.setProfession(Villager.Profession.CARTOGRAPHER);
        villager.teleport(location);
        org.bukkit.Location entLoc = new org.bukkit.Location(Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")),
                location.getX(), location.getY() - 0.3, location.getZ());
        ArmorStand nameStand = (ArmorStand) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                .spawnEntity(location, EntityType.ARMOR_STAND);
        nameStand.teleport(location);
        nameStand.setInvisible(true);
        nameStand.setGravity(false);
        nameStand.setInvulnerable(true);
        nameStand.setCustomName(Text.color("&aHiltcrafter"));
        nameStand.setCustomNameVisible(true);
        ArmorStand clickStand = (ArmorStand) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                .spawnEntity(entLoc, EntityType.ARMOR_STAND);
        clickStand.teleport(entLoc);
        clickStand.setInvisible(true);
        clickStand.setGravity(false);
        clickStand.setInvulnerable(true);
        clickStand.setCustomName(Text.color("&e&lBUY"));
        clickStand.setCustomNameVisible(true);
        Slime slime = (Slime) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                .spawnEntity(location, EntityType.SLIME);
        slime.setSize(4);
        slime.setAI(false);
        slime.setInvisible(true);
        slime.setInvulnerable(true);
        slime.setGravity(false);
        slime.setCollidable(false);
        slime.setCustomName("hiltcraftersalesman");
        slime.setRemoveWhenFarAway(false);
        this.entity = villager;

    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getTypeID() {
        return "hiltcraftersalesman";
    }

    @Override
    public void onInteract(Player p, PlayerData playerData, SKRPG skrpg) {
        Inventory inventory = Bukkit.createInventory(null, 54, "Weapon Salesman");
        // HILTCRAFTER <<< INV NAME
        List<Integer> outlineSlots = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53);
        for (int i : outlineSlots) {
            inventory.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
        }
        for (PurchasableItem purchasableItem : getPurchasableItems()) {
            boolean found = false;
            List<ItemStack> contents = Arrays.asList(inventory.getContents());
            for (ItemStack itemStack : contents) {
                if (itemStack == null && !found) {
                    ItemStack itemStackBuild = Items.buildItem(purchasableItem.getItem());
                    ItemMeta iM = itemStackBuild.getItemMeta();
                    List<String> lore = iM.getLore();
                    lore.add(" ");
                    lore.add(Text.color("&7Cost: &6" + purchasableItem.getCost() + " Nuggets"));
                    iM.setLore(lore);
                    itemStackBuild.setItemMeta(iM);
                    inventory.setItem(contents.indexOf(itemStack), itemStackBuild);
                    found = true;
                }
            }
        }
        p.openInventory(inventory);
    }

    @Override
    public EntityPlayer getEntityPlayer() {
        return null;
    }

    @Override
    public Entity getEntity() {
        return entity;
    }

    @Override
    public List<PurchasableItem> getPurchasableItems() {
        return Arrays.asList(new PurchasableItem(Items.BASIC_HILT, 100), new PurchasableItem(Items.ADVANCED_HILT, 1000), new PurchasableItem(Items.EXPERTISE_HILT, 15000), new PurchasableItem(Items.LEGENDS_HILT, 100000), new PurchasableItem(Items.MASTERS_HILT, 200000));
    }

    @Override
    public Material getItemInHand() {
        return null;
    }
    @Override
    public NpcType getNpcType() {
        return NpcType.HILTCRAFTER;
    }
    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }
}
