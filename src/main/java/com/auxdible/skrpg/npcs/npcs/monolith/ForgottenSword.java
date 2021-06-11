package com.auxdible.skrpg.npcs.npcs.monolith;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.npcs.NPC;
import com.auxdible.skrpg.npcs.NpcType;
import com.auxdible.skrpg.npcs.PurchasableItem;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.player.quests.Quests;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.Text;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import java.util.List;

public class ForgottenSword implements NPC {
    private Entity entity;
    private SKRPG skrpg;
    private Location location;
    private Entity hitbox, clickStand;
    public ForgottenSword(SKRPG skrpg, Location location) {
        this.skrpg = skrpg;
        this.location = location;
    }
    @Override
    public void buildNPC(SKRPG skrpg) {
        ArmorStand swordStand = (ArmorStand) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                .spawnEntity(location, EntityType.ARMOR_STAND);
        swordStand.teleport(swordStand.getLocation().subtract(0, 1.5934, 0.512695));
        swordStand.setGravity(false);
        swordStand.setAI(false);
        swordStand.setInvulnerable(true);
        swordStand.setInvisible(true);
        swordStand.setCollidable(false);
        swordStand.getEquipment().setHelmet(new ItemBuilder(Material.DIAMOND_SWORD, 0).asItem());
        swordStand.setHeadPose(new EulerAngle(Math.toRadians(90f), 0f, 0f));
        Location entLoc = new Location(Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")),
                swordStand.getEyeLocation().getX(), location.getY() - 1.6, swordStand.getEyeLocation().getZ());

        ArmorStand clickStand = (ArmorStand) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                .spawnEntity(entLoc, EntityType.ARMOR_STAND);
        clickStand.teleport(entLoc.add(0.3, 0.0, 0.0));
        clickStand.setInvisible(true);
        clickStand.setGravity(false);
        clickStand.setCustomName(Text.color("&e&lLEARN"));
        clickStand.setCustomNameVisible(true);
        ArmorStand nameStand = (ArmorStand) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                .spawnEntity(clickStand.getLocation(), EntityType.ARMOR_STAND);
        nameStand.teleport(nameStand.getLocation().add(0.0, 0.3, 0.0));
        nameStand.setInvisible(true);
        nameStand.setGravity(false);
        nameStand.setInvulnerable(true);
        nameStand.setCustomName(Text.color("&aForgotten Sword"));
        nameStand.setCustomNameVisible(true);
        Slime slime = (Slime) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                .spawnEntity(location, EntityType.SLIME);
        slime.setSize(4);
        slime.setAI(false);
        slime.setInvisible(true);
        slime.setInvulnerable(true);
        slime.setGravity(false);
        slime.setCollidable(false);
        slime.setCustomName("forgottensword");
        slime.setRemoveWhenFarAway(false);
        entity = swordStand;
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
        return "forgottensword";
    }

    @Override
    public void onInteract(Player p, PlayerData playerData, SKRPG skrpg) {
        if (playerData.hasQuest(Quests.TUTORIAL)) {
            Text.applyText(p, "&b&lForgotten Sword &r&8| &r&7I am a sword, forged by a great warrior, lost to time.");
            new BukkitRunnable() {

                @Override
                public void run() {
                    Text.applyText(p, "&b&lForgotten Sword &r&8| &r&7These monolith dummies are my only practice. Fight 5 of them, and I will use my power.");
                }
            }.runTaskLater(skrpg, 60);
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
        return NpcType.FORGOTTEN_SWORD;
    }

    @Override
    public Entity getHitbox() { return hitbox; }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }
}
