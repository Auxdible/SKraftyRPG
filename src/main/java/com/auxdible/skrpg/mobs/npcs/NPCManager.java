package com.auxdible.skrpg.mobs.npcs;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.regions.Region;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class NPCManager {
    private ArrayList<NPC> npcs;
    private SKRPG skrpg;
    public NPCManager(SKRPG skrpg) {
        npcs = new ArrayList<>();
        this.skrpg = skrpg;
    }

    public void enable() {
        if (skrpg.getConfig().getConfigurationSection("npcs") == null) { return;}
        if (skrpg.getConfig().getConfigurationSection("npcs").getKeys(false).isEmpty()) {
            return;
        }
        for (String s : skrpg.getConfig().getConfigurationSection("npcs").getKeys(false)) {
            double x = skrpg.getConfig().getDouble("npcs." + s + ".x");
            double z = skrpg.getConfig().getDouble("npcs." + s + ".z");
            double y = skrpg.getConfig().getDouble("npcs." + s + ".y");
            float yaw = (float) skrpg.getConfig().getDouble("npcs." + s + ".yaw");
            float pitch = (float) skrpg.getConfig().getDouble("npcs." + s + ".pitch");
            String npcType = skrpg.getConfig().getString("npcs." + s + ".type");
            addNpc(Integer.parseInt(s), NpcType.valueOf(npcType), x, y, z, yaw, pitch);
        }
    }
    public void disable() {
        for (NPC npc : npcs) {
            skrpg.getConfig().set("npcs." + npc.getId() + ".x", npc.getLocation().getX());
            skrpg.getConfig().set("npcs." + npc.getId() + ".z", npc.getLocation().getZ());
            skrpg.getConfig().set("npcs." + npc.getId() + ".y", npc.getLocation().getY());
            skrpg.getConfig().set("npcs." + npc.getId() + ".yaw", npc.getLocation().getYaw());
            skrpg.getConfig().set("npcs." + npc.getId() + ".pitch", npc.getLocation().getPitch());
            skrpg.getConfig().set("npcs." + npc.getId() + ".type", npc.getNpcType().toString());
        }
        skrpg.saveConfig();
    }

    public ArrayList<NPC> getNpcs() { return npcs; }
    public NPC getNpc(int id) {
        for (NPC npc : npcs) {
            if (npc.getId() == id) {
                return npc;
            }
        }
        return null;
    }
    public void addNpc(int id, NpcType npcType, double x, double y, double z, float yaw, float pitch) {
        NPC npc = new NPC(npcType, new Location(Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")),
                x, y, z, yaw, pitch), id);
        npcs.add(npc);
        npc.buildNPC(skrpg);
        if (npc.getEntityPlayer() != null) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerConnection playerConnection = ((CraftPlayer) player).getHandle().playerConnection;
                playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo
                        .EnumPlayerInfoAction.ADD_PLAYER, npc.getEntityPlayer()));
                playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc.getEntityPlayer()));
                DataWatcher watcher = npc.getEntityPlayer().getDataWatcher();
                Integer byteInt = 127;
                watcher.set(new DataWatcherObject<>(16, DataWatcherRegistry.a), byteInt.byteValue());
                playerConnection.sendPacket(new PacketPlayOutEntityMetadata(npc.getEntityPlayer().getId(),
                        watcher, true));
                player.getScoreboard().getTeam("npcs").addEntry(npc.getEntityPlayer().getName());
                playerConnection.sendPacket(new PacketPlayOutEntityHeadRotation(npc.getEntityPlayer(), (byte) (yaw * 256 / 360)));
            }
        }
    }
}
