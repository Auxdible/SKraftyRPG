package com.auxdible.skrpg.mobs.npcs.npcs;

import com.auxdible.skrpg.SKRPG;
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


import java.util.List;

public class CombatSeller implements NPC {
    private Entity entity;
    private int id;
    private org.bukkit.Location location;
    public CombatSeller(int id, Location location) {
        this.id = id;
        this.location = location;
    }
    @Override
    public void buildNPC(SKRPG skrpg) {
        Villager villager = (Villager) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")).spawnEntity(location, EntityType.VILLAGER);
        villager.setAI(false);
        villager.setProfession(Villager.Profession.BUTCHER);
        villager.teleport(location);
        villager.setInvulnerable(true);
        org.bukkit.Location entLoc = new org.bukkit.Location(Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")),
                location.getX(), location.getY() - 0.3, location.getZ());
        ArmorStand nameStand = (ArmorStand) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                .spawnEntity(location, EntityType.ARMOR_STAND);
        nameStand.teleport(location);
        nameStand.setInvisible(true);
        nameStand.setGravity(false);
        nameStand.setInvulnerable(true);
        nameStand.setCustomName(Text.color("&aAdventurer"));
        nameStand.setCustomNameVisible(true);
        ArmorStand clickStand = (ArmorStand) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                .spawnEntity(entLoc, EntityType.ARMOR_STAND);
        clickStand.teleport(entLoc);
        clickStand.setInvisible(true);
        clickStand.setGravity(false);
        clickStand.setCustomName(Text.color("&e&lSELL"));
        clickStand.setCustomNameVisible(true);
        Slime slime = (Slime) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                .spawnEntity(location, EntityType.SLIME);
        slime.setSize(4);
        slime.setAI(false);
        slime.setInvisible(true);
        slime.setInvulnerable(true);
        slime.setGravity(false);
        slime.setCollidable(false);
        slime.setCustomName("combatseller");
        slime.setRemoveWhenFarAway(false);
        entity = villager;

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
        return "combatseller";
    }

    @Override
    public void onInteract(Player p, PlayerData playerData, SKRPG skrpg) {
        Inventory inv = Bukkit.createInventory(null, 54, "Adventurer");
        for (int i = 0; i <= 53; i++) {
            inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
        }
        inv.setItem(49, null);
        inv.setItem(40, new ItemBuilder(Material.GOLD_NUGGET, 0)
                .setName("&7I'll buy any of your &6Combat &7related items for &6Nuggets!").asItem());
        inv.setItem(31, new ItemBuilder(Material.GOLD_NUGGET, 0)
                .setName("&7I'll buy any of your &6Combat &7related items for &6Nuggets!").asItem());

        inv.setItem(22, new ItemBuilder(Material.GOLD_INGOT, 0).setName("&7Click an item to sell.").asItem());
        p.openInventory(inv);
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
        return null;
    }

    @Override
    public Material getItemInHand() {
        return null;
    }
    @Override
    public NpcType getNpcType() {
        return NpcType.COMBAT_SELLER;
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
