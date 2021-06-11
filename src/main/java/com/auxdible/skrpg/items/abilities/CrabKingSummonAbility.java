package com.auxdible.skrpg.items.abilities;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.ItemInfo;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.items.SKRPGItemStack;
import com.auxdible.skrpg.mobs.boss.scrollboss.KingCrabScrollBoss;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.locations.regions.RegionFlags;
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
        if (playerData.getRegion() != null) {
            if (playerData.getRegion().getRegionFlagsList().contains(RegionFlags.NO_SPAWNING_MONSTERS)) {
                Text.applyText(p, "&cYou cannot spawn a boss in this region!");
                p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.1f);
                return;
            }
        }
        if (playerData.getPlayerInventory().getItemInMainHand().getAmount() > 1) {
            SKRPGItemStack itemStack = playerData.getPlayerInventory().getItemInMainHand();
            itemStack.setAmount(itemStack.getAmount() - 1);
            playerData.getPlayerInventory().setItemInMainHand(itemStack);
        } else {

            playerData.getPlayerInventory().setItemInMainHand(null);
        }
        playerData.getPlayerInventory().updateInventory(p);
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
