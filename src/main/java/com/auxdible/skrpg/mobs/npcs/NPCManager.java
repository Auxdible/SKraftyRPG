package com.auxdible.skrpg.mobs.npcs;

import com.auxdible.skrpg.SKRPG;

import com.auxdible.skrpg.utils.ItemBuilder;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class NPCManager {
    private ArrayList<NPC> npcs;
    private SKRPG skrpg;
    public NPCManager(SKRPG skrpg) {
        npcs = new ArrayList<>();
        this.skrpg = skrpg;
    }

    public void enable() throws InstantiationException, IllegalAccessException {
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
            addNpc(npcType, Integer.parseInt(s), x, y, z, yaw, pitch);
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
    public void addNpc(String type, int id, double x, double y, double z, float yaw, float pitch) throws IllegalAccessException, InstantiationException {
        Class npcClass = NpcType.valueOf(type).getNpc();
        NPC npc = null;
        try {
            Class[] cArg = new Class[2];
            cArg[0] = int.class;
            cArg[1] = Location.class;
            npc = (NPC) npcClass.getDeclaredConstructor(cArg).newInstance(id, new Location(Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")), x, y, z, yaw, pitch));
        } catch (NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        //npc.setId(id);
        //npc.setLocation(new Location(Bukkit.getWor`ld(skrpg.getConfig().getString("rpgWorld")), x, y, z, yaw, pitch));
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
                if (npc.getItemInHand() != null) {
                    List<Pair<EnumItemSlot, ItemStack>> itemList = new ArrayList<>();
                    itemList.add(Pair.of(EnumItemSlot.MAINHAND, CraftItemStack.asNMSCopy(new ItemBuilder(npc.getItemInHand(), 1).asItem())));
                    playerConnection.sendPacket(new PacketPlayOutEntityEquipment(npc.getEntityPlayer().getId(), itemList));
                }

            }
        }
    }
}
