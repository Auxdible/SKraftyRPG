package com.auxdible.skrpg.mobs.npcs.npcs;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.ItemInfo;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.mobs.npcs.NPC;
import com.auxdible.skrpg.mobs.npcs.NpcType;
import com.auxdible.skrpg.mobs.npcs.PurchasableItem;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.player.quests.Quests;
import com.auxdible.skrpg.player.quests.TutorialQuest;
import com.auxdible.skrpg.utils.Text;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;


import java.util.List;

public class TutorialNPCVillager implements NPC {
    private Entity entity;
    private int id;
    private Location location;
    public TutorialNPCVillager(int id, Location location) {
        this.id = id; this.location = location;
    }
    @Override
    public void buildNPC(SKRPG skrpg) {
        Villager villager = (Villager) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")).spawnEntity(location, EntityType.VILLAGER);
        villager.setAI(false);
        villager.setProfession(Villager.Profession.NITWIT);
        villager.teleport(location);
        villager.setInvulnerable(true);
        org.bukkit.Location entLoc = new org.bukkit.Location(Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")),
                location.getX(), location.getY() - 0.3, location.getZ());
        ArmorStand nameStand = (ArmorStand) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                .spawnEntity(location, EntityType.ARMOR_STAND);
        nameStand.teleport(location);
        nameStand.setInvisible(true);
        nameStand.setGravity(false);
        nameStand.setInvulnerable(true);
        nameStand.setCustomName(Text.color("&aTutorial NPC"));
        nameStand.setCustomNameVisible(true);
        ArmorStand clickStand = (ArmorStand) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                .spawnEntity(entLoc, EntityType.ARMOR_STAND);
        clickStand.teleport(entLoc);
        clickStand.setInvisible(true);
        clickStand.setGravity(false);
        clickStand.setCustomName(Text.color("&e&lTALK"));
        clickStand.setCustomNameVisible(true);
        Slime slime = (Slime) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                .spawnEntity(location, EntityType.SLIME);
        slime.setSize(4);
        slime.setAI(false);
        slime.setInvisible(true);
        slime.setInvulnerable(true);
        slime.setGravity(false);
        slime.setCollidable(false);
        slime.setCustomName("tutorialnpc");
        slime.setRemoveWhenFarAway(false);
        this.entity = villager;

    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public int getId() { return id; }

    @Override
    public String getTypeID() {
        return "tutorialnpc";
    }

    @Override
    public void onInteract(Player p, PlayerData playerData, SKRPG skrpg) {
        if (p.getInventory().getItemInMainHand().getType() == Material.AIR) { return; }
        ItemInfo itemInfo = ItemInfo.parseItemInfo(p.getInventory().getItemInMainHand());
        if (itemInfo != null) {
            Items items = itemInfo.getItem();
                ((TutorialQuest) Quests.TUTORIAL.getQuest()).giveItems(p, playerData, items, skrpg);
            }
        }

    @Override
    public EntityPlayer getEntityPlayer() {
        return null;
    }

    @Override
    public Entity getEntity() {
        return entity;
    }

    @Override
    public List<PurchasableItem> getPurchasableItems() {
        return null;
    }

    @Override
    public Material getItemInHand() {
        return null;
    }
    @Override
    public NpcType getNpcType() {
        return NpcType.TUTORIAL_NPC_VILLAGER;
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
