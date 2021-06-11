package com.auxdible.skrpg.items.forage.forageclass;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.items.forage.Forage;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

import java.util.Arrays;
import java.util.List;

public class Apple implements Forage {
    private Entity entity;
    private ArmorStand clickStand;
    public Apple() {
    }

    @Override
    public void onSpawn(SKRPG skrpg, Location location) {
        ArmorStand armorStand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        this.entity = armorStand;
        armorStand.setInvisible(true);
        armorStand.setInvulnerable(true);
        armorStand.setCustomName(Text.color("&aApple"));
        armorStand.setCustomNameVisible(true);
        armorStand.setHeadPose(new EulerAngle(Math.toRadians(-90.0), 0,0));
        armorStand.getEquipment().setHelmet(new ItemBuilder(Material.APPLE, 0).asItem());

        new BukkitRunnable() {
            int down = 0;
            @Override
            public void run() {
                if (armorStand.getEyeLocation().subtract(0.0, 1.80, 0.0).getBlock().getType() != Material.AIR || armorStand.getEyeLocation().getBlock().getType() != Material.AIR) {
                        ArmorStand clickStand = (ArmorStand) location.getWorld().spawnEntity(entity.getLocation(), EntityType.ARMOR_STAND);
                        clickStand.setCustomName(Text.color("&e&lCLICK"));
                        clickStand.setCustomNameVisible(true);
                        clickStand.setInvisible(true);
                        clickStand.setGravity(false);
                        clickStand.setInvulnerable(true);
                        clickStand.setSmall(true);
                        setClickStand(clickStand);
                        clickStand.teleport(clickStand.getLocation().subtract(0.0, 0.6, 0.0));
                        location.getWorld().playSound(location, Sound.BLOCK_GRASS_BREAK, 1.0f, 2.0f);
                        location.getWorld().playSound(location, Sound.ENTITY_ITEM_FRAME_BREAK, 1.0f, 0.4f);
                        cancel();
                        return;
                    }

                entity.teleport(entity.getLocation().subtract(0.0, 0.2, 0.0));

            }
        }.runTaskTimer(skrpg, 0, 1);
    }

    @Override
    public void onRemove(SKRPG skrpg, Location location) {
        if (entity != null) {
            entity.remove();
            if (clickStand != null) {
                clickStand.remove();
            }

        }
    }

    public void setClickStand(ArmorStand clickStand) { this.clickStand = clickStand; }

    @Override
    public Items itemDrop() {
        return Items.LOW_QUALITY_FRUIT;
    }

    @Override
    public Entity clickEntity() {
        return entity;
    }

    @Override
    public ArmorStand clickStand() {
        return clickStand;
    }

    @Override
    public List<Double> expEarned() {
        return Arrays.asList(0.0, 0.0, 50.0, 0.0, 0.0);
    }
}
