package com.auxdible.skrpg.npcs.npcs.monolith;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.items.SKRPGItemStack;
import com.auxdible.skrpg.npcs.NPC;
import com.auxdible.skrpg.npcs.NpcType;
import com.auxdible.skrpg.npcs.PurchasableItem;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.player.quests.Quests;
import com.auxdible.skrpg.player.quests.TutorialQuest;
import com.auxdible.skrpg.utils.Text;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class CraftersWorkbench implements NPC {
    private Entity entity;
    private SKRPG skrpg;
    private Location location;
    private Entity hitbox, clickStand;
    public CraftersWorkbench(SKRPG skrpg, Location location) {
        this.skrpg = skrpg;
        this.location = location;
    }
    @Override
    public void buildNPC(SKRPG skrpg) {
        location.getBlock().setType(Material.CRAFTING_TABLE);
        Location entLoc = new Location(Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")),
                Math.round(location.getX()) - 0.5, location.getY() - 0.3, Math.round(location.getZ()) - 0.5);
        ArmorStand nameStand = (ArmorStand) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                .spawnEntity(entLoc.add(0, 0.3,0), EntityType.ARMOR_STAND);
        nameStand.setInvisible(true);
        nameStand.setGravity(false);
        nameStand.setInvulnerable(true);
        nameStand.setCustomName(Text.color("&aCrafter's Workbench"));
        nameStand.setCustomNameVisible(true);
        ArmorStand clickStand = (ArmorStand) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                .spawnEntity(entLoc.subtract(0.0, 0.3, 0.0), EntityType.ARMOR_STAND);
        clickStand.teleport(entLoc.subtract(0.0, 0.3, 0.0));
        clickStand.setInvisible(true);
        clickStand.setGravity(false);
        clickStand.setCustomName(Text.color("&e&lLEARN"));
        clickStand.setCustomNameVisible(true);
        Slime slime = (Slime) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                .spawnEntity(location, EntityType.SLIME);
        slime.setSize(4);
        slime.setAI(false);
        slime.setInvisible(true);
        slime.setInvulnerable(true);
        slime.setGravity(false);
        slime.setCollidable(false);
        slime.setCustomName("craftersworkbench");
        slime.setRemoveWhenFarAway(false);
        entity = slime;
        hitbox = slime;
        this.clickStand = clickStand;
    }

    @Override
    public void deleteNPC() {
        entity.remove();
        clickStand.remove();
        hitbox.remove();
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
        return "craftersworkbench";
    }

    @Override
    public void onInteract(Player p, PlayerData playerData, SKRPG skrpg) {
        if (playerData.hasQuest(Quests.TUTORIAL)) {
            TutorialQuest tutorialQuest = (TutorialQuest) playerData.getQuest(Quests.TUTORIAL);
            if (!tutorialQuest.isHasMetTable()) {
                tutorialQuest.setHasMetTable(true);
                Text.applyText(p, "&6&lCrafter's Workbench &r&8| &r&7Hello, captain. When I was still in commission and being used, I was a workbench of great knowledge to rookies, and master crafters alike.");
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        Text.applyText(p, "&6&lCrafter's Workbench &r&8| &r&7On my table is a basic hilt, I want you to have it. Do /recipe WOODEN_PICKAXE to view the recipe for a &fWooden Pickaxe&7.");
                        playerData.getPlayerInventory().addItem(new SKRPGItemStack(Items.BASIC_HILT.generateItemInfo(), 1));

                        new BukkitRunnable() {

                            @Override
                            public void run() {
                                Text.applyText(p, "&6&lCrafter's Workbench &r&8| &r&7You should speak to the totems if you haven't yet, they will give you great advice on commands. Go to the Crafting Table and craft a &fWooden Pickaxe&7.");
                                new BukkitRunnable() {

                                    @Override
                                    public void run() {
                                        Text.applyText(p, "&6&lCrafter's Workbench &r&8| &r&7Once you craft it, mine the &fStone &7from the walls to power the &6Crafting Crystal&7.");
                                    }
                                }.runTaskLater(skrpg, 140);
                            }
                        }.runTaskLater(skrpg, 140);
                    }
                }.runTaskLater(skrpg, 60);
            }
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
        return NpcType.CRAFTERS_WORKBENCH;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public Entity getHitbox() { return hitbox; }
}
