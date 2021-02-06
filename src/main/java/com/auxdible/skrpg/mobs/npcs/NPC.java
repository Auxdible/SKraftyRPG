package com.auxdible.skrpg.mobs.npcs;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.utils.Text;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.PlayerInteractManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.util.UUID;

public class NPC {
    private NpcType npcType;
    private Location location;
    private int id;
    private EntityPlayer entityPlayer;
    private Entity entity;
    public NPC(NpcType npcType, Location location, int id) {
        this.npcType = npcType;
        this.location = location;
        this.id = id;
    }
    public Location getLocation() { return location; }
    public NpcType getNpcType() { return npcType; }
    public int getId() { return id; }

    public void buildNPC(SKRPG skrpg) {
        if (npcType.toString().contains("PLAYER")) {
            if (npcType == NpcType.PLAYER_EXPORT_MERCHANT) {
                GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "importmer" + getId());
                gameProfile.getProperties().put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAi" +
                        "IDogMTU5Njg3OTA5NTUzNiwKICAicHJvZmlsZUlkIiA6ICI3MjI2Mjg2NzYyZWY0YjZlODRlMzc2Y2J" +
                        "kYWNhZjU1NyIsCiAgInByb2ZpbGVOYW1lIiA6ICJicmFuZG9uZDI2IiwKICAic2lnbm" +
                        "F0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iI" +
                        "DogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90" +
                        "ZXh0dXJlL2I4YmM5Njg2YWM5Y2NlYTQxMjhlNDI2ZjIzMzk0MTZhM" +
                        "DBiOWIzMmJlZDRmNzlmZmU1Zjg3OGJiZTJkZWRjIgogICAgfQogIH0KfQ==", "Rvyiec71Sruv6IzPpDOqr0iVT1n" +
                        "52IsYrEYE+UoYle8btqNuoRBJN/tOtv/dOX5QxiyUeLYucKFW/2a58DIHQfetA9YURjwUaOf1+1Q4bg9SHae3sTVYi+E5rTJKWC" +
                        "AFDWlsid3k4IwTkorVa0XPMZexTkone/qbP64a8/FnXe5DabIO0g2hos8OMFBs2xbXpHFTBVWhxxl60NuWUq2Sbh5zHuUxLTNF++" +
                        "iFT8HOba2NFU2We1wTgnQlJWliQlSRZImgiYIHLVoxH0+qL3CvGG6mTOHf0FiZuZoy/hN8I9NGe2XAnNt3F98nEnucKfIP1UtE0Pj5" +
                        "/vXk1hezjc9HATZu1+7NbU6uG9t2a9tqUEEAMjBbtyarFC8Rl8uUmHvB1UkLV7Xfu47BMHKMf1HaffrwZPuKlnZyaGcV0URqdSqIRR3w" +
                        "OVi2tQQzKgJ2O+7nv0U1l605pDr7TMjtP0toSMPDkTsnLazT778q0W81GLIW9hUCHyi5iQweIAbo7pEOSwPjAXnXO5Oa50Mp0fuRi4" +
                        "RlplWq5PH8hLwLb+AHywzj0SKquGR+7UunG38xzpvnJX5nwID2U0+kuKXkDBQc9ghm8bMAQtbg0K" +
                        "gY2UEYVA3xPu6REdBLNS1lzWmeHhDbVID6qQ32jBYX2HYvzlYf1S+D42oagJUpiLUZrCrUdSU="));

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
                nameStand.setCustomName(Text.color("&aExport Merchant"));
                nameStand.setCustomNameVisible(true);
                ArmorStand clickStand = (ArmorStand) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                        .spawnEntity(entLoc, EntityType.ARMOR_STAND);
                clickStand.teleport(entLoc);
                clickStand.setInvisible(true);
                clickStand.setGravity(false);
                clickStand.setCustomName(Text.color("&e&lSELL"));
                clickStand.setCustomNameVisible(true);
                Slime slime = (Slime) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                        .spawnEntity(location, EntityType.SLIME);
                slime.setSize(4);
                slime.setAI(false);
                slime.setInvisible(true);
                slime.setInvulnerable(true);
                slime.setGravity(false);
                slime.setCollidable(false);
                slime.setCustomName("exportmerchant");
                slime.setRemoveWhenFarAway(false);
            } else if (npcType == NpcType.PLAYER_BANKER) {
                GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "weaponsales" + getId());
                gameProfile.getProperties().put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYxMDkyMzcyMDA2MywKICAicHJvZmlsZUlkIiA6ICJkZTE0MGFmM2NmMjM0ZmM0OTJiZTE3M2Y2NjA3MzViYiIsCiAgInByb2ZpbGVOYW1lIiA6ICJTUlRlYW0iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDhhOWJkNmNmZDY2NWM5ODM3M2RjN2EzMGVkZDU0ODdjMjI0YmMyN2RmY2IwYmU3ZWU2NDVkMGZjYTgxOTA1OSIKICAgIH0KICB9Cn0=",
                        "SC9YzCWeqVLdF+7bMDuaZ7pS61GGc2lqjq4eX6/LDOVulmmls7rCZB1ZXD7Xu8fBAaZEZIVgmdyeGaLC1KT0nUn2GiLmHwyxfiIUsQRtSFjAeLzR6R/WEXQsfkr713JAtKJCZCky5C8GIfrXAzm85+5ZLFkaet1LqrEAnMk+4hRM1kwARIO0tbuGJzGYQN3hJktZXWeN1+gVdsxnhb8Ls49DPXF5ZLZ15mrm46F6i4u2zaZePQlgEN06PNyYttrLAYRanvKxuVWTL3is8NivVo4K+qqf4jB3iBHpqXVWGJu4G7H/cK5ZhlqYDp4TpUWvqMmb7YzkinAKfdi50gcxYG68e8HWzU+RE9xoOGmlHxVc/QFOOjtiiEqrFanpLP9uPiY2mfM8lxVA8hqGFIKareT0Lt4IJsC3PMgO5qFP9AsznAzt8mmNPe9SutRJjSqTRq0AAbz6+ncfBT9QrsBgZ+anZD9+Y76Vvo68sVrLnQdCTSv/nTsMQAH3p3YDdXcZ0CM2hRDxdeoF6tcIPxRDK3K+gBqfHfVKWSScW9aJz3WvfPf7PteYB+GBc6ZrvWYWz3GxkWpFgJzl5xoZektEQcn4fN/id135gOVxvh+WeeZjLZHY22HLFYye/0ffukeOwAZ28QqSP06lCkDF3LFRQsX9HE73ZhNDRw6FL6q4YJs="));

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
                nameStand.setCustomName(Text.color("&aBanker"));
                nameStand.setCustomNameVisible(true);
                ArmorStand clickStand = (ArmorStand) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                        .spawnEntity(entLoc, EntityType.ARMOR_STAND);
                clickStand.teleport(entLoc);
                clickStand.setInvisible(true);
                clickStand.setGravity(false);
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
            } else if (npcType == NpcType.WEAPON_FORGER_SALESMAN) {
            Villager villager = (Villager) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")).spawnEntity(location, EntityType.VILLAGER);
            villager.setAI(false);
            villager.setProfession(Villager.Profession.WEAPONSMITH);
            villager.teleport(location);
            Location entLoc = new Location(Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")),
                    location.getX(), location.getY() - 0.3, location.getZ());
            ArmorStand nameStand = (ArmorStand) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                    .spawnEntity(location, EntityType.ARMOR_STAND);
            nameStand.teleport(location);
            nameStand.setInvisible(true);
            nameStand.setGravity(false);
            nameStand.setCustomName(Text.color("&aWeapon Salesman"));
            nameStand.setCustomNameVisible(true);
            ArmorStand clickStand = (ArmorStand) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                    .spawnEntity(entLoc, EntityType.ARMOR_STAND);
            clickStand.teleport(entLoc);
            clickStand.setInvisible(true);
            clickStand.setGravity(false);
            clickStand.setCustomName(Text.color("&e&lBUY"));
            clickStand.setCustomNameVisible(true);
            Slime slime = (Slime) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                    .spawnEntity(location, EntityType.SLIME);
            slime.setSize(4);
            slime.setAI(false);
            slime.setInvisible(true);
            slime.setInvulnerable(true);
            slime.setGravity(false);
            slime.setCollidable(false);
            slime.setCustomName("weaponsalesman");
            slime.setRemoveWhenFarAway(false);
            entity = villager;
        }

        }
    public EntityPlayer getEntityPlayer() { return entityPlayer; }
    public Entity getEntity() { return entity; }
}
