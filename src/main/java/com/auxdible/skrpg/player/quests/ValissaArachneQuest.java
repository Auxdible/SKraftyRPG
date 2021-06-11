package com.auxdible.skrpg.player.quests;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.items.SKRPGItemStack;

import com.auxdible.skrpg.player.skills.Level;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

public class ValissaArachneQuest implements Quest {
    private int phase;
    private SKRPG skrpg;
    private PlayerData playerData;
    @Override
    public void setPlayerData(PlayerData playerData) {
        this.playerData = playerData;
    }

    @Override
    public PlayerData getPlayerData() {
        return playerData;
    }
    @Override
    public void setSKRPG(SKRPG skrpg) { this.skrpg = skrpg; }
    @Override
    public SKRPG getSKRPG() { return skrpg; }
    @Override
    public String parseData() {
        return phase + "";
    }

    @Override
    public void stringToData(String data) {
        phase = Integer.parseInt(data);
    }
    @Override
    public void setPhase(int phase) {
        this.phase = phase;
    }

    @Override
    public int getPhase() {
        return phase;
    }

    @Override
    public List<SKRPGItemStack> getItemRewards() {
        return null;
    }

    @Override
    public int getCreditsReward() {
        return 100000;
    }

    @Override
    public int questPhases() {
        return 4;
    }

    @Override
    public Level levelRequired() {
        return Level._15;
    }

    @Override
    public List<Double> xpRewards() {
        return Arrays.asList(5000.0, 0.0, 0.0, 0.0, 0.0);
    }
    @Override
    public void executePhase(Player player, SKRPG skrpg) {
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(player.getUniqueId());
        if (phase == 1) {
            setPhase(1);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);
            Text.applyText(player, "&e&lHerold &r&8| &7Go into the depths, and gather the materials needed to craft her beloved gem.");
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);
                    Text.applyText(player, "&e&lHerold &r&8| &7Here is the recipe. This will be a very time consuming process.");
                    player.performCommand("recipe VALISSA_ARACHNE_CRYSTAL");
                }
            }.runTaskLater(skrpg, 30);
        } else if (phase == 2) {
            setPhase(2);
            Text.applyText(player, "&7&oThe wicked spider queen, Valissa Arachne, is defeated.");
        } else if (phase == 3) {
            setPhase(3);
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1.0f, 2.0f);
                Text.applyText(player, "&e&lHerold &r&8| &7You did it? You say the spider queen is defeated?");
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);
                        Text.applyText(player, "&e&lHerold &r&8| &7Congratulations adventurer! The wicked queen is dead! &7Have you seen my comrade, &eBryan&7, anywhere? Go find him and speak of this great joy.");
                        player.getInventory().addItem(Items.buildItem(Items.BASIC_HILT));
                    }
                }.runTaskLater(skrpg, 30);
        } else if (phase == 4) {
            Quests.completeQuest(Quests.VALISSA_ARACHNE_QUEST, player, skrpg.getPlayerManager().getPlayerData(player.getUniqueId()), skrpg);
            setPhase(4);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1.0f, 1.0f);
            Text.applyText(player, "&6&lLord Bryan &r&8| &7Hello, my noble subject.");
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);
                    Text.applyText(player, "&6&lLord Bryan &r&8| &7Valissa has been slain? How?");
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1.0f, 1.0f);
                            Text.applyText(player, "&6&lLord Bryan &r&8| &7Oh great joy! Our kingdom no longer lives in fear of the infested spiders. Thank you, adventurer. Here is your bounty, as promised.");
                        }
                    }.runTaskLater(skrpg, 30);
                }
            }.runTaskLater(skrpg, 30);
        }



}

    @Override
    public String name() {
        return "The Hunt";
    }

    @Override
    public Quests getQuestType() {
        return Quests.VALISSA_ARACHNE_QUEST;
    }
}

