package com.auxdible.skrpg.mobs.npcs.npcs;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.mobs.npcs.NPC;
import com.auxdible.skrpg.mobs.npcs.NpcType;
import com.auxdible.skrpg.mobs.npcs.PurchasableItem;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.player.economy.Bank;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.ItemTweaker;
import com.auxdible.skrpg.utils.Text;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.PlayerInteractManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.*;


import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Banker implements NPC {
    private EntityPlayer entityPlayer;
    private int id;
    private Location location;
    public Banker(int id, Location location) {
        this.id = id;
        this.location = location;
    }
    @Override
    public void buildNPC(SKRPG skrpg) {
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "weaponsales" + getId());
        gameProfile.getProperties().put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYxMDkyMzcyMDA2MywKICAicHJvZmlsZUlkIiA6ICJkZTE0MGFmM2NmMjM0ZmM0OTJiZTE3M2Y2NjA3MzViYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJTUlRlYW0iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDhhOWJkNmNmZDY2NWM5ODM3M2RjN2EzMGVkZDU0ODdjMjI0YmMyN2RmY2IwYmU3ZWU2NDVkMGZjYTgxOTA1OSIKICAgIH0KICB9Cn0=",
                "SC9YzCWeqVLdF+7bMDuaZ7pS61GGc2lqjq4eX6/LDOVulmmls7rCZB1ZXD7Xu8fBAaZEZIVgmdyeGaLC1KT0nUn2GiLmHwyxfiIUsQRtSFjAeLzR6R/WEXQsfkr713JAtKJCZCky5C8GIfrXAzm85+5ZLFkaet1LqrEAnMk+4hRM1kwARIO0tbuGJzGYQN3hJktZXWeN1+gVdsxnhb8Ls49DPXF5ZLZ15mrm46F6i4u2zaZePQlgEN06PNyYttrLAYRanvKxuVWTL3is8NivVo4K+qqf4jB3iBHpqXVWGJu4G7H/cK5ZhlqYDp4TpUWvqMmb7YzkinAKfdi50gcxYG68e8HWzU+RE9xoOGmlHxVc/QFOOjtiiEqrFanpLP9uPiY2mfM8lxVA8hqGFIKareT0Lt4IJsC3PMgO5qFP9AsznAzt8mmNPe9SutRJjSqTRq0AAbz6+ncfBT9QrsBgZ+anZD9+Y76Vvo68sVrLnQdCTSv/nTsMQAH3p3YDdXcZ0CM2hRDxdeoF6tcIPxRDK3K+gBqfHfVKWSScW9aJz3WvfPf7PteYB+GBc6ZrvWYWz3GxkWpFgJzl5xoZektEQcn4fN/id135gOVxvh+WeeZjLZHY22HLFYye/0ffukeOwAZ28QqSP06lCkDF3LFRQsX9HE73ZhNDRw6FL6q4YJs="));

        EntityPlayer entityPlayer = new EntityPlayer(((CraftServer) Bukkit.getServer()).getServer(),
                ((CraftWorld) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))).getHandle(), gameProfile,
                new PlayerInteractManager(((CraftWorld) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")))
                        .getHandle()));
        this.entityPlayer = entityPlayer;

        entityPlayer.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        org.bukkit.Location entLoc = new org.bukkit.Location(Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")),
                location.getX(), location.getY() - 0.3, location.getZ());
        ArmorStand nameStand = (ArmorStand) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                .spawnEntity(location, EntityType.ARMOR_STAND);
        nameStand.teleport(location);
        nameStand.setInvisible(true);
        nameStand.setInvulnerable(true);
        nameStand.setGravity(false);
        nameStand.setCustomName(Text.color("&aBanker"));
        nameStand.setCustomNameVisible(true);
        ArmorStand clickStand = (ArmorStand) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                .spawnEntity(entLoc, EntityType.ARMOR_STAND);
        clickStand.teleport(entLoc);
        clickStand.setInvisible(true);
        clickStand.setGravity(false);
        clickStand.setInvulnerable(true);
        clickStand.setCustomName(Text.color("&e&lBANK"));
        clickStand.setCustomNameVisible(true);
        Slime slime = (Slime) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                .spawnEntity(location, EntityType.SLIME);
        slime.setSize(4);
        slime.setAI(false);
        slime.setInvisible(true);
        slime.setInvulnerable(true);
        slime.setGravity(false);
        slime.setCollidable(false);
        slime.setCustomName("banker");
        slime.setRemoveWhenFarAway(false);
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
        return "banker";
    }

    @Override
    public void onInteract(Player p, PlayerData playerData, SKRPG skrpg) {
        Inventory inv = Bukkit.createInventory(null, 27, "Banker");
        for (int i = 0; i <= 26; i++) {
            inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
        }
        List<Integer> slots = Arrays.asList(11, 12, 13, 14, 15);
        for (int i = 0; i < playerData.getBanks().size(); i++) {
            ItemStack bank = ItemTweaker.createPlayerHeadFromData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmY3NWQxYjc4NWQxOGQ0N2IzZWE4ZjBhN2UwZmQ0YTFmYWU5ZTdkMzIzY2YzYjEzOGM4Yzc4Y2ZlMjRlZTU5In19fQ==", "1d2bf3fe-1b67-495f-995d-435693e90fa0");
            ItemMeta iM = bank.getItemMeta();
            iM.setDisplayName(Text.color("&7Bank &a") + (i + 1));
            iM.setLore(Arrays.asList(" ", Text.color("&7Level: " + playerData.getBanks().get(i).getLevel()
                    .getNameColored()), Text.color("&7Nuggets: &6" + playerData.getBanks().get(i).getCredits())));
            bank.setItemMeta(iM);
            inv.setItem(slots.get(i), bank);
        }
        if (playerData.getBanks().size() != 5) {
            inv.setItem(22, new ItemBuilder(Material.LIME_DYE, 0).setName("&7Buy Bank: &6" + (
                    2500000 * playerData.getBanks().size())).asItem());
        }
        p.openInventory(inv);
    }

    @Override
    public EntityPlayer getEntityPlayer() {
        return entityPlayer;
    }

    @Override
    public Entity getEntity() {
        return null;
    }

    @Override
    public List<PurchasableItem> getPurchasableItems() {
        return null;
    }

    @Override
    public Material getItemInHand() {
        return Material.GOLD_NUGGET;
    }

    @Override
    public NpcType getNpcType() {
        return NpcType.BANKER;
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
