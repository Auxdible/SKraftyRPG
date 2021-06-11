package com.auxdible.skrpg.npcs.npcs;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.npcs.NPC;
import com.auxdible.skrpg.npcs.NpcType;
import com.auxdible.skrpg.npcs.PurchasableItem;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.player.quests.Quests;
import com.auxdible.skrpg.utils.Text;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Herold implements NPC {
    private EntityPlayer entityPlayer;
    private SKRPG skrpg;
    private Location location;
    private Entity hitbox, clickStand;
    public Herold(SKRPG skrpg, Location location) {
        this.skrpg = skrpg;
        this.location = location;
    }
    @Override
    public void buildNPC(SKRPG skrpg) {
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "herold" + getId());
        gameProfile.getProperties().put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYxNTQyMjIyODE4MywKICAicHJvZmlsZUlkIiA6ICJhYTZhNDA5NjU4YTk0MDIwYmU3OGQwN2JkMzVlNTg5MyIsCiAgInByb2ZpbGVOYW1lIiA6ICJiejE0IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzhjOGUxMjllNzc0NGNkZjU0MGI4MTFmNTQ2ZGQ4N2IxNTU0MjIwZWI0MWYyNDRlMTZkZmJlZjI4MDllZTA0YWYiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==",
                "PVM5NdHik8ddCx4AQEe1OJY50/3p+39DkDWTk5XUqIDzaBj97Oz0doEzvND7o0iaSejFKlCXuxq+/TbrJGocuAUUn/fjGsgz1twLIsiEiCUywbqA7uPQn/npT2LYQ4LwOstD05lOtcFUnD67OLymsrSTyu2gSBMbqVAj3nEzivJDz0eu3UdMJrIUbCpYn2kzQ808lJiV+58LfV22mlghIecQ5bYifEbMyiPpTKpYC2mpNPS8p1EigC6+OAsCVWJUVHiBpKgunRJctxrGs/tTXY/IXWAJQuN5uoXThsmFMYUtR/R+2Zzjd41KRNGmoaMNaTnbZEa6//qb8LqoWDyuJNXaAMEMBwuwuAd3OE+JwSHSlSn6VOOmxucfAZbqD0HSK990CyH0eaKyH/VDIW2q0xCKEpORnBg6C+sxHr+RqeQGSXN6HoC3KkL8Ut+M3bqOmu/7bq2rbGHCi6lU1TS93+0nXp4omg56pJkvW35kSoYWvx+FyQmNm/SA0vqQJCQ17tiWKHQijGnGTN+xyMNBtbCvfcnjPpJUE6n2QOokYp3CIDRC68xk1aVSN7atVT3feIfLDlS3fcm9kGR2IU1oJeKMIobllbMVfqUdJx6TEwkXpqwRRTfL9K4/66p0NxdIlOQfY/yAi7VVMS+VAwSCVgDe7qQ+/1DYqqVDIfdoqIY="));

        EntityPlayer entityPlayer = new EntityPlayer(((CraftServer) Bukkit.getServer()).getServer(),
                ((CraftWorld) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))).getHandle(), gameProfile,
                new PlayerInteractManager(((CraftWorld) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")))
                        .getHandle()));
        this.entityPlayer = entityPlayer;
        entityPlayer.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        Location entLoc = new Location(Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")),
                location.getX(), location.getY() - 0.3, location.getZ());
        ArmorStand nameStand = (ArmorStand) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                .spawnEntity(location, EntityType.ARMOR_STAND);
        nameStand.teleport(location);
        nameStand.setInvisible(true);
        nameStand.setGravity(false);
        nameStand.setCustomName(Text.color("&aHerold"));
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
        slime.setCustomName("herold");
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
        return "herold";
    }

    @Override
    public void onInteract(Player p, PlayerData playerData, SKRPG skrpg) {
        Random random = new Random();
        if (!playerData.getCompletedQuests().contains(Quests.VALISSA_ARACHNE_QUEST) && !playerData.hasQuest(Quests.VALISSA_ARACHNE_QUEST)) {
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_HURT, 1.0f, 0.3f);
            Text.applyText(p, "&e&lHerold &r&8| &7You... you're him? The leader? No?");
            new BukkitRunnable() {

                @Override
                public void run() {
                    Text.applyText(p, "&e&lHerold &r&8| &7Is it you? Are you sure you're not him? How did you get here? Is the war still going on?");
                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            Text.applyText(p, "&e&lHerold &r&8| &7Oh, excuse me. I'm Herold. In the &eWar of The Webs, &7I was abandoned by my comrades in this forest. &7I will never forgive them.");

                                    new BukkitRunnable() {

                                        @Override
                                        public void run() {
                                            Text.applyText(p, "&e&lHerold &r&8| &4&lValissa Arachne &r&7attacked our unit, and slayed many of our own. &7She has been wanted for more than 50 years.");
                                            new BukkitRunnable() {

                                                @Override
                                                public void run() {
                                                    if (SKRPG.levelToInt(playerData.getCombat().getLevel().toString()) < 15) {
                                                        Text.applyText(p, "&e&lHerold &r&8| &7Sorry, adventurer. But I need someone stronger. Come back when you are Combat Level 15!");
                                                        return;
                                                    }
                                                    Text.applyText(p, "&e&lHerold &r&8| &7The world outside is peaceful and calm. But adventurer, there is turmoil inside the caves. &7Go, and claim your bounty!");
                                                    Quests.startQuest(Quests.VALISSA_ARACHNE_QUEST, p, playerData, skrpg);
                                                }
                                    }.runTaskLater(skrpg, 40);
                                }
                            }.runTaskLater(skrpg, 40);
                        }
                    }.runTaskLater(skrpg, 40);
                }
            }.runTaskLater(skrpg, 20);
        } else if (playerData.hasQuest(Quests.VALISSA_ARACHNE_QUEST)) {
            if (playerData.getQuest(Quests.VALISSA_ARACHNE_QUEST).getPhase() == 2) {
                playerData.getQuest(Quests.VALISSA_ARACHNE_QUEST).setPhase(3);
                Quests.VALISSA_ARACHNE_QUEST.getQuest().executePhase(p, skrpg);
            }
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
        return Material.CROSSBOW;
    }
    @Override
    public NpcType getNpcType() {
        return NpcType.HEROLD;
    }

    @Override
    public Entity getHitbox() { return hitbox; }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }
}
