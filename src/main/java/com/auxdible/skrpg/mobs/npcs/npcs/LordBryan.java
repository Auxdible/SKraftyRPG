package com.auxdible.skrpg.mobs.npcs.npcs;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.ItemInfo;
import com.auxdible.skrpg.mobs.npcs.NPC;
import com.auxdible.skrpg.mobs.npcs.NpcType;
import com.auxdible.skrpg.mobs.npcs.PurchasableItem;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.player.quests.LordBryanQuest;
import com.auxdible.skrpg.player.quests.Quests;
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

public class LordBryan implements NPC {
    private EntityPlayer entityPlayer;
    private int id;
    private Location location;
    public LordBryan(int id, Location location) {
        this.id = id;
        this.location = location;
    }
    @Override
    public void buildNPC(SKRPG skrpg) {
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "lordbryan" + getId());
        gameProfile.getProperties().put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTU5MTExNTI4MzY2MywKICAicHJvZmlsZUlkIiA6ICIzZmM3ZmRmOTM5NjM0YzQxOTExOTliYTNmN2NjM2ZlZCIsCiAgInByb2ZpbGVOYW1lIiA6ICJZZWxlaGEiLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODdhNTg5MTFiZTA5MDZmNzUyYjk2NmExMDVkYzc1YjdkNzIwZTEzZTE1MzVhMmNhMDFmMzZlNzg1NjUwN2NkOSIKICAgIH0KICB9Cn0=",
                "xRBQDD3jH1wt5/p1glJMtOV+AbDnIdctw8mSWhOoqY036gYkYjKg+yj2DvcMD7I++kfiegaYcmcBOvYGibzlb0je7tUh14Xv6erpA72yok3fKQIlUgCQ9hq7YxShOFq949MGVbpPQ9h0FrYHwR+iE7z/NukwiQ8RACFfirZwtNwWSq1y7v+IjRU1ZzaBZ50l40RstgnrGn65Py+HT8NEHU25cZmRMb+pLSdACLO8HmYVBAnjolIBP+B4YxgvIeqa1x2f4na9iLwJiYD+6pMByPAIIk+juhmK7wyEI8oVcTER9+eAuUORMfIlArii/ITGLsYt1kJrHD6pO8jHB0pPasI+gBkfBeFdg/SQKLdOnnLtHYrzQzHiwGRm0+R7YX1BvmlWDZjkoewmPnLu4h67AljVLg/7wwNMo5xHskOX50UQz98upIm5OiNYhSf5MaxnLXAGP7wMCvZFoGp+0IRHzdYwb9u16wJsGgsspEF4NifAL8aCo6IXTYiNAUbspHMEn9iPfy6/sNPPGyQh/4VPCyXpjWmHfvkWnIYI1bKC34n12vCWBvdHcMN9EBtMwhLD+qSpOao8K9P9P0xgR2kW4JhauxiRYau+bfNRMhrdeo6cpOJNKu+a0cYJoaXOonqL4xk2VC+uzccaQm/WoTDQOJljqc8eG7gExfB/Qt1/q7I="));

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
        nameStand.setCustomName(Text.color("&aLord Bryan"));
        nameStand.setCustomNameVisible(true);
        nameStand.setInvulnerable(true);
        ArmorStand clickStand = (ArmorStand) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                .spawnEntity(entLoc, EntityType.ARMOR_STAND);
        clickStand.teleport(entLoc);
        clickStand.setInvisible(true);
        clickStand.setGravity(false);
        clickStand.setCustomName(Text.color("&6&lROYALTY"));
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
        slime.setCustomName("lordbryan");
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
        return "lordbryan";
    }

    @Override
    public void onInteract(Player p, PlayerData playerData, SKRPG skrpg) {
        Random random = new Random();
        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);
        if (playerData.getActiveQuest() == null && !playerData.getCompletedQuests().contains(Quests.LORD_BRYAN)) {
            Quests.startQuest(Quests.LORD_BRYAN, p, playerData, skrpg);
        } else if (playerData.getActiveQuest() == Quests.LORD_BRYAN) {
            ItemInfo itemInfo = ItemInfo.parseItemInfo(p.getInventory().getItemInMainHand());
            if (itemInfo != null) {
                ((LordBryanQuest) Quests.LORD_BRYAN.getQuest()).giveItems(p, playerData, itemInfo.getItem(), skrpg);
            }
        } else {
            playerData.getPlayerActionManager().openRoyaltyMenu();
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
        return Material.GOLDEN_SHOVEL;
    }
    @Override
    public NpcType getNpcType() {
        return NpcType.LORD_BRYAN;
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
