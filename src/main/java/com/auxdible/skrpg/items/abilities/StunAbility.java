package com.auxdible.skrpg.items.abilities;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

public class StunAbility implements Ability {
    @Override
    public String getName() {
        return "&cStun";
    }
    @Override
    public void ability(PlayerData playerData, SKRPG skrpg) {
        Player p = Bukkit.getPlayer(playerData.getUuid());
        for (Entity entity : p.getNearbyEntities(50, 256, 50)) {
            if (p.hasLineOfSight(entity)) {
                BlockIterator i = new BlockIterator(p, 20);
                while (i.hasNext()) {
                     Block block = i.next();
                     p.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, block.getLocation(), 1);
                }
                p.playSound(p.getLocation(), Sound.ITEM_SHIELD_BREAK, 2.0f, 0.2f);
                if (entity instanceof Creature) {
                    ((Creature) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 100));
                    ArmorStand damageIndictator = (ArmorStand) entity.getWorld()
                            .spawnEntity(entity.getLocation(), EntityType.ARMOR_STAND);
                    damageIndictator.setInvulnerable(true);
                    damageIndictator.setCollidable(false);
                    damageIndictator.setInvisible(true);
                    damageIndictator.setCustomName(Text.color("&e&lSTUNNED"));
                    damageIndictator.setCustomNameVisible(true);
                    damageIndictator.setSmall(true);
                    damageIndictator.setVelocity(new Vector(0, 0.5, 0));
                    new BukkitRunnable() {
                        int quarterSecondsAlive = 0;

                        @Override
                        public void run() {
                            quarterSecondsAlive++;
                            if (quarterSecondsAlive == 8) {
                                damageIndictator.remove();
                                cancel();
                            }
                        }
                    }.runTaskTimer(skrpg, 0, 5);
                }
            }
        }

    }

    @Override
    public int getCost() {
        return -1;
    }

    @Override
    public boolean isAttack() {
        return false;
    }

    @Override
    public Items getItem() {
        return Items.TRICKSTER_WAND;
    }
}
