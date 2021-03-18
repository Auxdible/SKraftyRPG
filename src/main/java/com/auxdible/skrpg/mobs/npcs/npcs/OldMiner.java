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

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class OldMiner implements NPC {
    private EntityPlayer entityPlayer;
    private int id;
    private Location location;
    public OldMiner(int id, Location location) {
        this.id = id;
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
        return "oldminer";
    }

    @Override
    public void onInteract(Player p, PlayerData playerData, SKRPG skrpg) {
        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_HURT, 1.0f, 0.3f);
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
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }
}
