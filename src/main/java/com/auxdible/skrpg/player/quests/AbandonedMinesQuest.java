package com.auxdible.skrpg.player.quests;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.CraftingIngrediant;
import com.auxdible.skrpg.player.skills.Level;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class AbandonedMinesQuest implements Quest {
    @Override
    public ArrayList<CraftingIngrediant> getItemRewards() {
        return null;
    }

    @Override
    public int getCreditsReward() {
        return 5000;
    }

    @Override
    public int questPhases() {
        return 1;
    }

    @Override
    public Level levelRequired() {
        return null;
    }

    @Override
    public ArrayList<Integer> xpRewards() {
        return null;
    }

    @Override
    public void executePhase(int phase, Player player, SKRPG skrpg) {
        if (phase == 1) {
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1.0f, 0.2f);
            Text.applyText(player, "&e&lOld Miner &r&8| &7The long abandoned mines are waiting for you. I will take you down the &elift. &7Meet me at the end of the mines.");
            new BukkitRunnable() {

                @Override
                public void run() {
                    player.teleport(skrpg.getLocationManager().getAbandonedMinesLocation());
                    player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 0.2f);
                }
            }.runTaskLater(skrpg, 60);
        } else if (phase == 2) {
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1.0f, 0.2f);
            Text.applyText(player, "&e&lOld Miner &r&8| &7You made it! The mines are now cleared out, thanks to you. Thank you adventurer. I will take you to the &esurface &7now.");
            Quests.completeQuest(Quests.ABANDONED_MINES, player, skrpg.getPlayerManager().getPlayerData(player.getUniqueId()), skrpg);
            new BukkitRunnable() {

                @Override
                public void run() {
                    player.teleport(skrpg.getLocationManager().getAbandonedMinesSurfaceLocation());
                    player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0f, 0.2f);
                }
            }.runTaskLater(skrpg, 60);
            new BukkitRunnable() {

                @Override
                public void run() {
                    Text.applyText(player, "&e&lOld Miner &r&8| &7The mines contain many types of &eores. &7Click me to access &eThe Lift&7.");
                }
            }.runTaskLater(skrpg, 60);
        }
    }

    @Override
    public String name() {
        return "The Abandoned Mines";
    }
}
