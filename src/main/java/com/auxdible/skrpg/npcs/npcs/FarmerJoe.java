package com.auxdible.skrpg.npcs.npcs;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.ItemInfo;
import com.auxdible.skrpg.npcs.NPC;
import com.auxdible.skrpg.npcs.NpcType;
import com.auxdible.skrpg.npcs.PurchasableItem;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.player.quests.FarmingQuest;
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
import java.util.UUID;

public class FarmerJoe implements NPC {
    private EntityPlayer entityPlayer;
    private SKRPG skrpg;
    private Location location;
    private Entity hitbox, clickStand;
    public FarmerJoe(SKRPG skrpg, Location location) {
        this.skrpg = skrpg;
        this.location = location;
    }
    @Override
    public void buildNPC(SKRPG skrpg) {
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "farmerjoe" + getId());
        gameProfile.getProperties().put("textures", new Property("textures",
                "ewogICJ0aW1lc3RhbXAiIDogMTYxMjI5ODQ4OTUxNSwKICAicHJvZmlsZUlkIiA6ICIyM2YxYTU5ZjQ2OWI0M2RkYmRiNTM3YmZlYzEwNDcxZiIsCiAgInByb2ZpbGVOYW1lIiA6ICIyODA3IiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzdhOGFiMjI2N2FlNDQzZTgzYjA1ZTNhOGQ2N2I4Yjc2MGFkZmFhZDMzNWM5MDQ3MzM4YzBlMTc3NGE2NWMzNzIiCiAgICB9CiAgfQp9",
                "sum5f646L4LZyHWg1UiGFUU6pj7MlCleTgvMTAKiqx44Sj+kqPK6zq8nVLkCxlV1gsA3aXxjLbrmKR3pbYcZuOKOwrA+u9YU8RAzWOWOI6lECRIsK+WKtX7jqFLd+JesJUptTyis4qA548zkdho/LigVXRfBZs9SwVuxnx5aB+GqztTZpYP8e+cXSka/bE0zapxNIbDJd7hJUYvHY//rP0eLY8bBSYakXkrQA4CLCEt9nGF6vtkqo5imiZIyI6lSA4H4lr3gwYrI+BkwQKV0HwWAy5mWQW9tfjUoT/LOEfVdD6ctxGrZ9emg9hmAiuPUt5E/AJSeeZBoc/D8r97ez4CvOtJ4ZdWkSrrUW/7HE68LTJxIOmIizdNeyGXAnysFf26+PV/kDGhcu1L76o1fqD1SANc3AXIIDiE352K5I7aIU/SH5dBOCFzEPT9s1uFMsKajxCiYhhmgcSs4lyCSyhThho164p/N7QMrOUcYJyN5pnQvccwakvBSYhZJs4DKMSYIh3a1IgISRBjIJxoEJ+T2zZYT0NrCzHSRycHEiqKEpooSJMUzcEj5fksqtcozI4ETTAdGy1JeQoCTDXf3EwJGZseRxbZSxNdZ4ifMrqRHM7SLlYDHgjoTLFtGQAJkdoEmeWERtp4O3TJOUXciuHGKOcPTqGTkzvZQAjTTB4M="));

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
        nameStand.setCustomName(Text.color("&aFarmer Joe"));
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
        slime.setCustomName("farmerjoe");
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
        return "farmerjoe";
    }

    @Override
    public void onInteract(Player p, PlayerData playerData, SKRPG skrpg) {
        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.5f);
        if (playerData.hasQuest(Quests.FARMING_QUEST)) {
            skrpg.getLogger().info("1");
            FarmingQuest farmingQuest = (FarmingQuest) Quests.FARMING_QUEST.getQuest();
            ItemInfo itemInfo = playerData.getPlayerInventory().getItemInMainHand().getItemInfo();
            if (itemInfo != null) {
                skrpg.getLogger().info("2 " + itemInfo.isProcessed());
                farmingQuest.giveItems(p, playerData, itemInfo.getItem(), itemInfo, skrpg);
            }
        }
        if (!playerData.hasQuest(Quests.FARMING_QUEST) && !playerData.getCompletedQuests().contains(Quests.FARMING_QUEST)) {
            Text.applyText(p, "&e&lFarmer Joe &r&8| &r&7Howdy! I see you're uncultured in the ways of herbalism and farming. Let me show you the way.");
            new BukkitRunnable() {
                @Override
                public void run() {
                    Quests.startQuest(Quests.FARMING_QUEST, p, playerData, skrpg);
                }
            }.runTaskLater(skrpg, 40);
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
        return Material.STONE_HOE;
    }
    @Override
    public NpcType getNpcType() {
        return NpcType.FARMER_JOE;
    }

    @Override
    public Entity getHitbox() { return hitbox; }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }
}
