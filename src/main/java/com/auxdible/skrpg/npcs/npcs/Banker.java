package com.auxdible.skrpg.npcs.npcs;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.npcs.NPC;
import com.auxdible.skrpg.npcs.NpcType;
import com.auxdible.skrpg.npcs.PurchasableItem;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.utils.Text;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.*;


import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.List;
import java.util.UUID;

public class Banker implements NPC {
    private EntityPlayer entityPlayer;
    private SKRPG skrpg;
    private Location location;
    private Entity hitbox, clickStand;
    public Banker(SKRPG skrpg, Location location) {
        this.skrpg = skrpg;
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
        hitbox = slime;
        this.clickStand = clickStand;
    }
    @Override
    public void deleteNPC() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerConnection playerConnection = ((CraftPlayer) player).getHandle().playerConnection;
            playerConnection.sendPacket(new PacketPlayOutPlayerInfo(
                    PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, getEntityPlayer()));
            playerConnection.sendPacket(new PacketPlayOutEntityDestroy(getEntityPlayer().getId()));
            PlayerData playerData = skrpg.getPlayerManager().getPlayerData(player.getUniqueId());
            if (playerData.getRenderedNPCs() != null && playerData.getRenderedNPCs().contains(this)) {
                playerData.getRenderedNPCs().remove(this);
            }
        }
        hitbox.remove();
        clickStand.remove();
    }
    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public int getId() {
        return skrpg.getNpcManager().getNpcs().indexOf(this);
    }

    @Override
    public String getTypeID() {
        return "banker";
    }

    @Override
    public void onInteract(Player p, PlayerData playerData, SKRPG skrpg) {
        playerData.getPlayerActionManager().openBankMenu();
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
    public Entity getHitbox() { return hitbox; }

    @Override
    public NpcType getNpcType() {
        return NpcType.BANKER;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }
}
