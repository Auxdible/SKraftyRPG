package com.auxdible.skrpg.mobs.npcs.npcs;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.mobs.npcs.NPC;
import com.auxdible.skrpg.mobs.npcs.NpcType;
import com.auxdible.skrpg.mobs.npcs.PurchasableItem;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.utils.Text;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.PlayerInteractManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class CityGuard implements NPC {
    private EntityPlayer entityPlayer;
    private int id;
    private Location location;
    public CityGuard(int id, Location location) {
        this.id = id;
        this.location = location;
    }
    @Override
    public void buildNPC(SKRPG skrpg) {
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "cityguard" + getId());
        gameProfile.getProperties().put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYxNTQyMjIyODE4MywKICAicHJvZmlsZUlkIiA6ICJhYTZhNDA5NjU4YTk0MDIwYmU3OGQwN2JkMzVlNTg5MyIsCiAgInByb2ZpbGVOYW1lIiA6ICJiejE0IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzhjOGUxMjllNzc0NGNkZjU0MGI4MTFmNTQ2ZGQ4N2IxNTU0MjIwZWI0MWYyNDRlMTZkZmJlZjI4MDllZTA0YWYiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==",
                "PVM5NdHik8ddCx4AQEe1OJY50/3p+39DkDWTk5XUqIDzaBj97Oz0doEzvND7o0iaSejFKlCXuxq+/TbrJGocuAUUn/fjGsgz1twLIsiEiCUywbqA7uPQn/npT2LYQ4LwOstD05lOtcFUnD67OLymsrSTyu2gSBMbqVAj3nEzivJDz0eu3UdMJrIUbCpYn2kzQ808lJiV+58LfV22mlghIecQ5bYifEbMyiPpTKpYC2mpNPS8p1EigC6+OAsCVWJUVHiBpKgunRJctxrGs/tTXY/IXWAJQuN5uoXThsmFMYUtR/R+2Zzjd41KRNGmoaMNaTnbZEa6//qb8LqoWDyuJNXaAMEMBwuwuAd3OE+JwSHSlSn6VOOmxucfAZbqD0HSK990CyH0eaKyH/VDIW2q0xCKEpORnBg6C+sxHr+RqeQGSXN6HoC3KkL8Ut+M3bqOmu/7bq2rbGHCi6lU1TS93+0nXp4omg56pJkvW35kSoYWvx+FyQmNm/SA0vqQJCQ17tiWKHQijGnGTN+xyMNBtbCvfcnjPpJUE6n2QOokYp3CIDRC68xk1aVSN7atVT3feIfLDlS3fcm9kGR2IU1oJeKMIobllbMVfqUdJx6TEwkXpqwRRTfL9K4/66p0NxdIlOQfY/yAi7VVMS+VAwSCVgDe7qQ+/1DYqqVDIfdoqIY="));

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
        nameStand.setGravity(false);
        nameStand.setCustomName(Text.color("&aCity Guard"));
        nameStand.setCustomNameVisible(true);
        nameStand.setInvulnerable(true);
        ArmorStand clickStand = (ArmorStand) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                .spawnEntity(entLoc, EntityType.ARMOR_STAND);
        clickStand.teleport(entLoc);
        clickStand.setInvisible(true);
        clickStand.setGravity(false);
        clickStand.setCustomName(Text.color("&e&lTALK"));
        clickStand.setCustomNameVisible(true);
        clickStand.setInvulnerable(true);
        Slime slime = (Slime) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                .spawnEntity(location, EntityType.SLIME);
        slime.setSize(4);
        slime.setAI(false);
        slime.setInvisible(true);
        slime.setInvulnerable(true);
        slime.setGravity(false);
        slime.setCollidable(false);
        slime.setCustomName("cityguard");
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
        return "cityguard";
    }

    @Override
    public void onInteract(Player p, PlayerData playerData, SKRPG skrpg) {
        Random random = new Random();
        int randomDialogue = random.nextInt(3);
        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_HURT, 1.0f, 0.3f);
        if (randomDialogue == 0) {
            Text.applyText(p, "&e&lCity Guard &r&8| &7In &eThe Thickets&7, there's an &aabandoned mine&7... " +
                    "I hear it's rich in &fIron&7, &fCoal&7, and &fRedstone. &7Some say it's deeper than they think. &7Maybe there's &amore&7?");
        } else if (randomDialogue == 1) {
            Text.applyText(p, "&e&lCity Guard &r&8| &7There are rumors of large quantities of &aspiders &7coming out of &aThe Dark Thickets&7. &7I wonder where they're coming from.");
        } else if (randomDialogue == 2) {
            Text.applyText(p, "&e&lCity Guard &r&8| &7There is a rumor of a &chighly dangerous crab &7being spotted around the beach. The &aBeachmaster &7may know a thing or two.");
        } else if (randomDialogue == 3) {
            Text.applyText(p, "&e&lCity Guard &r&8| &7Have you ever thought about going &awoodcutting&7? It's a great way to increase your &4☄ Strength &7and &a✿ Defence&7.");
        }
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
        return Material.IRON_SWORD;
    }
    @Override
    public NpcType getNpcType() {
        return NpcType.CITY_GUARD;
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
