package com.auxdible.skrpg.npcs.npcs;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.ItemInfo;
import com.auxdible.skrpg.npcs.NPC;
import com.auxdible.skrpg.npcs.NpcType;
import com.auxdible.skrpg.npcs.PurchasableItem;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.player.quests.LordBryanQuest;
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

import java.util.List;
import java.util.UUID;

public class Fisherman implements NPC {
    private EntityPlayer entityPlayer;
    private SKRPG skrpg;
    private Location location;
    private Entity hitbox, clickStand;
    public Fisherman(SKRPG skrpg, Location location) {
        this.skrpg = skrpg;
        this.location = location;
    }
    @Override
    public void buildNPC(SKRPG skrpg) {
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "fisherman" + getId());
        gameProfile.getProperties().put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYxODE4NDU3ODg2MCwKICAicHJvZmlsZUlkIiA6ICI5MThhMDI5NTU5ZGQ0Y2U2YjE2ZjdhNWQ1M2VmYjQxMiIsCiAgInByb2ZpbGVOYW1lIiA6ICJCZWV2ZWxvcGVyIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzI5NzI2ZjYzOTY1YjI1NTI2NWU1MTk4ZDNlZjZhYTE5ZjM4MTM5ZGJjYjg5MTk4YWZjNzNjMmJhYzRjYjM3ZjQiCiAgICB9CiAgfQp9",
                "gMACSn6H2RT4qIfhAeYgCwIXfg74ZvwyszIZKBiYab8GFptNP6+t03Z9JBe60KXSMflefyLLQucSKhTshuRvFKhZB0DtQCfswvSY7g0bK9WaqREkK7IbUbuNHWdAGyukFRv3nXBXQwedQuQteaygHTzp85xx1Wj5Rj2v6DoapfWzkx8C6fuDNA97prMXR/zv7WUNCaLQzMOsjBEHJoRr2+j+FkPKbgSsUCSSSF2igP17g3h4nGKJDD0SON9lxaiD7RNyzwqjc9RhjMtS/sIKLWj4acMHco8rzKs/1BCIh9g6N7v28k0tcOe9BJWeyYJyFGKGhroj8jasQ5nGkkTWN1cqRJgtp9WyilG5bUjLYVHlnfMKk1arwGt3NYhQJH5axwt3id7hIIinLiU9HQ2Bx21KCZ0cELr0Wqxz2iENzQ8jxVgxRz0IeOq/Yb12+FziinCW4e2DJ8PeOb+ZhnoZRG57+lhmEjCMtkhJWVYnN6URtLQbiuqmnKnsyQh9b7FQLks0WYYp6rZMlfBCRucHmpWqoA/ECJRd9Vu8NJxO/lu3T7LDtYlmB7m4CBOrxBv7btawCmczA/y5N/+2WIMG5MSwzmzk1QPEX81qlgKjZEAnaZpy2iUOO95PAiNwCHjY/F5GWmS19EbeYuWPBFhlsVqhwPGC1W+WV4wMDK40Yp4="));

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
        nameStand.setCustomName(Text.color("&aFisherman"));
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
        slime.setCustomName("fisherman");
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
    public String getTypeID() { return "fisherman"; }

    @Override
    public void onInteract(Player p, PlayerData playerData, SKRPG skrpg) {
        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 0.4f);
        if (!playerData.hasQuest(Quests.FISHERMAN_QUEST) && !playerData.getCompletedQuests().contains(Quests.FISHERMAN_QUEST)) {
            Quests.startQuest(Quests.FISHERMAN_QUEST, p, playerData, skrpg);
        } else if (playerData.hasQuest(Quests.FISHERMAN_QUEST)) {
            ItemInfo itemInfo = playerData.getPlayerInventory().getItemInMainHand().getItemInfo();
            if (itemInfo != null) {
                ((LordBryanQuest) Quests.FISHERMAN_QUEST.getQuest()).giveItems(p, playerData, itemInfo.getItem(), skrpg);
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
        return Material.FISHING_ROD;
    }
    @Override
    public NpcType getNpcType() {
        return NpcType.FISHERMAN;
    }

    @Override
    public Entity getHitbox() { return hitbox; }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }
}
