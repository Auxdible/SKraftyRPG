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
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class VillagerNPC implements NPC {
    private EntityPlayer entityPlayer;
    private SKRPG skrpg;
    private Location location;
    private Entity hitbox, clickStand;
    public VillagerNPC(SKRPG skrpg, Location location) {
        this.skrpg = skrpg;
        this.location = location;
    }
    @Override
    public void buildNPC(SKRPG skrpg) {
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "villager" + getId());
        gameProfile.getProperties().put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYxNjk1NTA4MTQ5NywKICAicHJvZmlsZUlkIiA6ICJhYTZhNDA5NjU4YTk0MDIwYmU3OGQwN2JkMzVlNTg5MyIsCiAgInByb2ZpbGVOYW1lIiA6ICJiejE0IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2FlZjM2NmFkMTg5YTZkYTlmMmM4NDYxZjNjMDNlY2NhYTQ3YzA2ZmE3MWZjYzY0YjQ2MGNiNzY0NTg2MjIxMTQiCiAgICB9CiAgfQp9",
                "d2XePhm7pPIn4hRoWSNXAWRzgD56NYnb+YtLU7PyHkGl/jOT0HP7vFk2TjbiaHy1F6jYLWh6hUduY9T3qiVhFBJFTPhqS8mFRmX5Tu8egJxgoN9XiHq5nY6JMbtgOhm0KQGiaB6d327FMyufJ8B9Q/2tg4F6AOjlqw7desTkvteirZGR1FQWTW9BhbAxnoEsfM6T7CJR9QMZjg1RAqoTqfy/GYWYw/Zb8tXD05S0GJBWozXwtiS20wNIio4z2vTx5QVSxrejIMJWjEPPuflnd0yYkLbMhvQaoLnLWZ5Ieie25hqQtbpepEArN1G4w8v4iD+kA2CgXK1GYyKqJ+UGFniIWAbl859Zd/uM/RKcxFElyLPuRFcQ0BdOIw84Z08NNL99Nf3VEutwW2DS8dGlgMKPgfhn4EO0ZyQLBIgzAssNS/KuBM4VdCZu74kktgd6Yl0MTn5zor69KF/DyC561Mrw8/tQ2ssu9PfvHU7pDwD0aWV95O5IZlCrkSY4SeWAqfvaCqkV2ZsxyLczbgfa1TdP9r/6uTLHFuuLkWb5BGPxM+Rph7izSWRPjror/vfSkQ8X1qMJjyvNhJT0/G4JYYgfErQ6NBtcaOKBM0RdsVWeBkuhChzOizr7HkYkRcOBBbAJqRQNTi55HicYus9Agp6E5ZXTpND2d1PHYeSIbsU="));

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
        nameStand.setCustomName(Text.color("&aVillager"));
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
        slime.setCustomName("villager");
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
        return "villager";
    }

    @Override
    public void onInteract(Player p, PlayerData playerData, SKRPG skrpg) {
        Random random = new Random();
        int randomDialogue = random.nextInt(3);
        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_HURT, 1.0f, 0.3f);
        if (randomDialogue == 0) {
            Text.applyText(p, "&e&lVillager &r&8| &7The Tavern is my favorite place in the whole town. It's so lively, and people are always there to give advice.");
        } else if (randomDialogue == 1) {
            Text.applyText(p, "&e&lVillager &r&8| &7Have you ever heard of &aDortoh&7? It's the home of &aThe Diplomatic Council&7, albeit the journey is hard to complete. &7You can find the path to Dortoh by going through &aThe Thickets&7 .");
        } else if (randomDialogue == 2) {
            Text.applyText(p, "&e&lVillager &r&8| &7One of the oldest members of the town is &aLord Bryan&7. He may have something for you.");
        } else if (randomDialogue == 3) {
            Text.applyText(p, "&e&lVillager &r&8| &7Despite the amount of unliveliness in the mushroom plains, there is rumored to be a &ahumongous mushroom egg &7deep in the plains.");
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
        return Material.BREAD;
    }
    @Override
    public NpcType getNpcType() {
        return NpcType.VILLAGER;
    }

    @Override
    public Entity getHitbox() { return hitbox; }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }
}
