package com.auxdible.skrpg.items.abilities;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.mobs.MobType;
import com.auxdible.skrpg.mobs.boss.scrollboss.KingCrabScrollBoss;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class CrabKingSummonAbility implements Ability {
    @Override
    public String getName() {
        return "&cSummon";
    }

    @Override
    public void ability(PlayerData playerData, SKRPG skrpg) {
        Player p = Bukkit.getPlayer(playerData.getUuid());
        if (p.getInventory().getItemInMainHand().getAmount() > 1) {
            ItemStack itemStack = p.getInventory().getItemInMainHand();
            itemStack.setAmount(p.getInventory().getItemInMainHand().getAmount() - 1);
            p.getInventory().setItemInMainHand(itemStack);
        } else {
            ItemStack itemStack = p.getInventory().getItemInMainHand();
            itemStack.setType(Material.AIR);
            p.getInventory().setItemInMainHand(itemStack);
        }
        p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_SCREAM, 1.0f, 0.2f);
        Location spawnLocation = new Location(p.getLocation().getWorld(), p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ());
        for (Player player : Bukkit.getOnlinePlayers()) {
            Text.applyText(player, "&4&l! &r&8&l| &r&c" + p.getDisplayName() + " &7is summoning the &cCrab King &7in 10 seconds!");
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                skrpg.getScrollBossManager().createBoss(new KingCrabScrollBoss(spawnLocation, p, skrpg));
            }
        }.runTaskLater(skrpg, 200);
    }

    @Override
    public int getCost() {
        return 0;
    }

    @Override
    public boolean isAttack() {
        return false;
    }

    @Override
    public Items getItem() {
        return Items.CRAB_SCROLL;
    }
}
