package com.auxdible.skrpg.npcs.npcs;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.npcs.NPC;
import com.auxdible.skrpg.npcs.NpcType;
import com.auxdible.skrpg.npcs.PurchasableItem;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.player.quests.Quests;
import com.auxdible.skrpg.utils.ItemBuilder;
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
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class OldMiner implements NPC {
    private EntityPlayer entityPlayer;
    private SKRPG skrpg;
    private Location location;
    private Entity hitbox, clickStand;
    public OldMiner(SKRPG skrpg, Location location) {
        this.skrpg = skrpg;
        this.location = location;
    }
    @Override
    public void buildNPC(SKRPG skrpg) {
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "oldminer" + getId());
        gameProfile.getProperties().put("textures", new Property("textures", "eyJ0aW1lc3RhbXAiOjE1NzgyNTUzNDk1MzEsInByb2ZpbGVJZCI6ImU3OTNiMmNhN2EyZjQxMjZhMDk4MDkyZDdjOTk0MTdiIiwicHJvZmlsZU5hbWUiOiJUaGVfSG9zdGVyX01hbiIsInNpZ25hdHVyZVJlcXVpcmVkIjp0cnVlLCJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDA5ZjZhMzQxZjc3YTM1YTg1YTExYmNjYzNmNWUwM2UwNjU4MTAzZDlhZGM2NDI4OTc1ZDVhMTYzYWRiMWY5NCIsIm1ldGFkYXRhIjp7Im1vZGVsIjoic2xpbSJ9fX19",
                "\n" +
                        "Oz5KSrxEbbrgN2YvxZAQkJ0bmNyRHfESbolQtwQITkG6FH+hLlObRWqD5UpOkIFcV0gH8fR83Iz9UXCG4MdN/LocsgRPGZe+RT4Xg5uktQDWvrZqQ0rmg0sDk7R3NaCozuCYJKYygKhufQF90j6NBEHWfew/5WU1NWHUNcsWELsHuaCZEEudH58zFbux6Onv5R8F8jFZxgRxuCaNJZto3L11Zk8O5LJBlUg+Ij4EkRv3y0olQR6dNrkF7dRvfspgZelGZHU2ilA9oUd8c6q3/6TnoEuhJk/uFCPO+MItEvkg449kdxTFo1Wp6w2lFn8yxpQIW7E561XxgErE+ayuq9A0HKiaQRPuWQk0s4CjzogXnrAYCFE/S+atllqKB4CYafJz2OFqdoavE7LoTtMCVEDkmlfwRa6g1gxiBD/cXQAoGLakkLi4k0AnIZ1M5lN0VcY347x25uFMpZf1FHQ0UtAgWjn7ikvENdye5a4BOSJeeLuFLScIAz0x/xz2Ar3e3ApT0v2Zi65wi9RLH5mt2h68wDBOrmvK+yEcviQr/nrXMcLJoT8COrCfOzbw7VUtyS/Bxy02UD8u9evvrdkziuEas701kUyruxBYmciwV2jXD/DK54zNtNcvRXPoQqDTwCO1e+nyhzdRDwd2SigAq8e+X7+3pbSVUFM6U+UzegU="));

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
        nameStand.setCustomName(Text.color("&aOld Miner"));
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
        slime.setCustomName("oldminer");
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
        return "oldminer";
    }

    @Override
    public void onInteract(Player p, PlayerData playerData, SKRPG skrpg) {
        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_HURT, 1.0f, 0.3f);
        if (!playerData.hasQuest(Quests.ABANDONED_MINES) && !playerData.getCompletedQuests().contains(Quests.ABANDONED_MINES)) {
            Text.applyText(p, "&e&lOld Miner &r&8| &r&7Hello. These mines have been long abandoned, but many adventurers wish to explore them.");
            new BukkitRunnable() {

                @Override
                public void run() {
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_HURT, 1.0f, 0.3f);
                    Text.applyText(p, "&e&lOld Miner &r&8| &7I see you are intrested as well, come with me.");
                }
            }.runTaskLater(skrpg, 30);
            new BukkitRunnable() {

                @Override
                public void run() {
                    Quests.startQuest(Quests.ABANDONED_MINES, p, playerData, skrpg);
                }
            }.runTaskLater(skrpg, 60);
            new BukkitRunnable() {

                @Override
                public void run() {
                    playerData.getQuest(Quests.ABANDONED_MINES).setPhase(2);
                }
            }.runTaskLater(skrpg, 200);
        } else if (playerData.hasQuest(Quests.ABANDONED_MINES) && playerData.getQuest(Quests.ABANDONED_MINES).getPhase() == 2) {
            Quests.ABANDONED_MINES.getQuest().executePhase(p, skrpg);
        } else {
            Inventory inv = Bukkit.createInventory(null, 54, "The Lift");
            for (int i = 0; i <= 17; i++) {
                inv.setItem(i, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE, 0).asItem());
            }
            for (int i = 18; i <= 26; i++) {
                inv.setItem(i, new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS_PANE, 0).asItem());
            }
            for (int i = 18; i <= 44; i++) {
                inv.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 0).asItem());
            }
            for (int i = 45; i <= 53; i++) {
                inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).asItem());
            }
            inv.setItem(3, new ItemBuilder(Material.GRASS_BLOCK, 0).setName("&aSurface")
                    .setLore(Arrays.asList(" ", Text.color("&7Return to the &aSurface"), " ")).asItem());
            inv.setItem(4, new ItemBuilder(Material.COAL_ORE, 0).setName("&aThe Coal Mines")
                    .setLore(Arrays.asList(" ", Text.color("&7Required Mining Level: &60"), " ")).asItem());
            inv.setItem(13, new ItemBuilder(Material.IRON_ORE, 0).setName("&aIron Quarry")
                    .setLore(Arrays.asList(" ", Text.color("&7Required Mining Level: &62"), " ")).asItem());
            inv.setItem(22, new ItemBuilder(Material.GOLD_ORE, 0).setName("&aGold Caves")
                    .setLore(Arrays.asList(" ", Text.color("&7Required Mining Level: &65"), " ")).asItem());
            inv.setItem(31, new ItemBuilder(Material.LAPIS_ORE, 0).setName("&aLapis Tunnels")
                    .setLore(Arrays.asList(" ", Text.color("&7Required Mining Level: &67"), " ")).asItem());
            inv.setItem(33, new ItemBuilder(Material.REDSTONE_ORE, 0).setName("&aRedstone Ravine")
                    .setLore(Arrays.asList(" ", Text.color("&7Required Mining Level: &68"), " ")).asItem());
            inv.setItem(40, new ItemBuilder(Material.WHITE_STAINED_GLASS, 0).setName("&aCrystallite Cavern")
                    .setLore(Arrays.asList(" ", Text.color("&7Required Mining Level: &610"), " ")).asItem());
            inv.setItem(49, new ItemBuilder(Material.DIAMOND_ORE, 0).setName("&aDiamond Depths")
                    .setLore(Arrays.asList(" ", Text.color("&7Required Mining Level: &612"), " ")).asItem());
            inv.setItem(51, new ItemBuilder(Material.OBSIDIAN, 0).setName("&aObsidian Reserve")
                    .setLore(Arrays.asList(" ", Text.color("&7Required Mining Level: &614"), " ")).asItem());
            p.openInventory(inv);
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
        return Material.STONE_PICKAXE;
    }
    @Override
    public NpcType getNpcType() {
        return NpcType.OLD_MINER;
    }

    @Override
    public Entity getHitbox() { return hitbox; }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }
}
