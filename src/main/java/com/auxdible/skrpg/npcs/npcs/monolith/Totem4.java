package com.auxdible.skrpg.npcs.npcs.monolith;

import com.auxdible.skrpg.SKRPG;
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

import java.util.List;

public class Totem4 implements NPC {
    private Entity entity;
    private SKRPG skrpg;
    private Location location;
    private Entity hitbox, clickStand;
    public Totem4(SKRPG skrpg, Location location) {
        this.skrpg = skrpg;
        this.location = location;

    }
    @Override
    public void buildNPC(SKRPG skrpg) {
        Location entLoc = new Location(Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")),
                Math.round(location.getX()) - 0.5, location.getY() - 0.3, Math.round(location.getZ()) - 0.5);
        ArmorStand nameStand = (ArmorStand) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                .spawnEntity(entLoc.add(0, 0.3,0), EntityType.ARMOR_STAND);
        nameStand.setInvisible(true);
        nameStand.setGravity(false);
        nameStand.setInvulnerable(true);
        nameStand.setCustomName(Text.color("&aTotem 4"));
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
        slime.setCustomName("totem4");
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
        return "totem4";
    }

    @Override
    public void onInteract(Player p, PlayerData playerData, SKRPG skrpg) {
        if (playerData.hasQuest(Quests.TUTORIAL)) {
            TutorialQuest tutorialQuest = (TutorialQuest) playerData.getQuest(Quests.TUTORIAL);
            if (!tutorialQuest.getTotemsInteracted().contains(4)) {
                Text.applyTypingTitle(skrpg, p, Text.color("&7/recipe (recipe name)"), 5, 20);
                Text.applyTextWithCommand(p, "&a&lCLICK ME! &r&7/recipe (recipe name)", "/recipe list");
                tutorialQuest.addTotem(4);
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
        return NpcType.TOTEM4;
    }

    @Override
    public Entity getHitbox() { return hitbox; }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }
}
