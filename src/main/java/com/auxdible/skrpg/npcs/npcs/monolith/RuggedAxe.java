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

public class RuggedAxe implements NPC {
    private Entity entity;
    private SKRPG skrpg;
    private Location location;
    private Entity hitbox, clickStand;
    public RuggedAxe(SKRPG skrpg, Location location) {
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
        swordStand.getEquipment().setHelmet(new ItemBuilder(Material.IRON_AXE, 0).asItem());
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
        nameStand.setCustomName(Text.color("&aRugged Axe"));
        nameStand.setCustomNameVisible(true);
        Slime slime = (Slime) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                .spawnEntity(location, EntityType.SLIME);
        slime.setSize(4);
        slime.setAI(false);
        slime.setInvisible(true);
        slime.setInvulnerable(true);
        slime.setGravity(false);
        slime.setCollidable(false);
        slime.setCustomName("ruggedaxe");
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
        return "ruggedaxe";
    }

    @Override
    public void onInteract(Player p, PlayerData playerData, SKRPG skrpg) {
        if (playerData.hasQuest(Quests.TUTORIAL)) {
            Text.applyText(p, "&7&lRugged Axe &r&8| &r&7I remember my days on the islands of Kaleiden, where I would find exotic resources and harvest them for my owner.");
            new BukkitRunnable() {

                @Override
                public void run() {
                    Text.applyText(p, "&7&lRugged Axe &r&8| &r&7I will pass my knowledge to you. In order to obtain resources, you must harvest blocks. &7There are many different types of harvestable blocks, ranging from wheat, to stone, and wood! &7Stone requires a &aPickaxe &7to mine.");
                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            Text.applyText(p, "&7&lRugged Axe &r&8| &r&7Accumulating a certain amount of items allows you to unlock new recipes and buffs! &7These are called &aAccumulations&7, found in your menu or by doing &a/acccumulations&7.");
                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    Text.applyText(p, "&7&lRugged Axe &r&8| &r&7If you wish to power the Crystal of Crafting, collect 5 &fWood &7to power it.");
                                }
                            }.runTaskLater(skrpg, 200);
                        }
                    }.runTaskLater(skrpg, 200);
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
        return NpcType.RUGGED_AXE;
    }

    @Override
    public Entity getHitbox() { return hitbox; }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }
}
