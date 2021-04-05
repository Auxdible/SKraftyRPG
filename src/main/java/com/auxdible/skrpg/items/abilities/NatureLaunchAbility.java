package com.auxdible.skrpg.items.abilities;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.ItemInfo;
import com.auxdible.skrpg.items.ItemType;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.mobs.Mob;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class NatureLaunchAbility implements Ability {
    @Override
    public String getName() {
        return "Nature Launch";
    }
    public void explode(Player p, ArmorStand natureLaunch, SKRPG skrpg) {
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(p.getUniqueId());
        p.getWorld().spawnParticle(Particle.BLOCK_CRACK, natureLaunch.getLocation(), 50, Material.OAK_LEAVES.createBlockData());
        p.playSound(p.getLocation(), Sound.BLOCK_GRASS_BREAK, 1.0f, 1.0f);
        p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.5f);
        List<Mob> mobsInRange = new ArrayList<>();
        for (Mob mobs : skrpg.getMobManager().getMobs()) {
            if (mobs.getEnt().getLocation().distance(natureLaunch.getLocation()) <= 3.0) {
                mobsInRange.add(mobs);
            }
        }
        int damage;
        for (Mob mob : mobsInRange) {
            ItemInfo itemInfo = ItemInfo.parseItemInfo(p.getInventory().getItemInMainHand());
            if (itemInfo != null) {
                damage = playerData.getPlayerActionManager().calculateDamage();

                damage = damage * (1 + (playerData.getMaxEnergy() / 100));
                mob.damage(p, damage, skrpg);
            }


        }
        natureLaunch.remove();
    }

    @Override
    public void ability(PlayerData playerData, SKRPG skrpg) {
        Player p = Bukkit.getPlayer(playerData.getUuid());
        p.setVelocity(p.getLocation().getDirection().multiply(-1).normalize().divide(new Vector(2, 0, 2)).setY(0.5));
        p.playSound(p.getLocation(), Sound.BLOCK_GRASS_BREAK, 1.0f, 0.2f);
        ArmorStand natureLaunch = (ArmorStand)
                p.getWorld().spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
        natureLaunch.setInvisible(true);
        natureLaunch.getEquipment().setHelmet(new ItemBuilder(Material.OAK_LOG, 0).asItem());
        natureLaunch.setSmall(true);
        natureLaunch.setInvulnerable(true);
        natureLaunch.setVelocity(p.getLocation().getDirection().setY(0.5));
        new BukkitRunnable() {
            int halfSeconds = 0;
            @Override
            public void run() {
                if (halfSeconds == 6) {
                    explode(p, natureLaunch, skrpg);
                    cancel();
                }
                if (natureLaunch.isOnGround()) {
                    explode(p, natureLaunch, skrpg);
                    cancel();
                }
                halfSeconds++;
            }
        }.runTaskTimer(skrpg, 0, 10);
    }

    @Override
    public int getCost() {
        return 25;
    }

    @Override
    public boolean isAttack() {
        return true;
    }

    @Override
    public Items getItem() {
        return Items.NATURE_STAFF;
    }
}
