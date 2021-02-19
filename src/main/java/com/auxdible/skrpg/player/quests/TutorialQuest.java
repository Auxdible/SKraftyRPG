package com.auxdible.skrpg.player.quests;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.CraftingIngrediant;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.mobs.npcs.NPC;
import com.auxdible.skrpg.mobs.npcs.NpcType;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.player.skills.Level;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class TutorialQuest implements Quest {
    @Override
    public ArrayList<CraftingIngrediant> getItemRewards() {
        return null;
    }

    @Override
    public int getCreditsReward() {
        return 100;
    }

    @Override
    public int questPhases() {
        return 3;
    }

    @Override
    public Level levelRequired() {
        return Level._0;
    }

    @Override
    public ArrayList<Integer> xpRewards() {
        return null;
    }

    @Override
    public void executePhase(int phase, Player player, SKRPG skrpg) {
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(player.getUniqueId());
        if (phase == 1) {
            playerData.setQuestPhase(1);
            player.teleport(player.getWorld().getSpawnLocation());
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);
            Text.applyText(player, "&e&lSKRPG &r&8| &7Welcome to SKRPG!");
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);
                    Text.applyText(player, "&e&lNPC &r&8| &7Meet me at the pathway to SKVille!");
                }
            }.runTaskLater(skrpg, 30);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!player.isOnline()) {
                        cancel(); }
                    for (NPC npc : skrpg.getNpcManager().getNpcs()) {
                        if (npc.getNpcType().equals(NpcType.TUTORIAL_NPC_VILLAGER) && npc.getEntity().getLocation().distance(player.getLocation()) <= 7) {
                            executePhase(2, player, skrpg);
                            cancel();
                            return;
                        }
                    }
                }
            }.runTaskTimer(skrpg, 0, 10);
        } else if (phase == 2) {
            playerData.setQuestPhase(2);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);
            Text.applyText(player, "&e&lNPC &r&8| &7In SKRPG you can level up your " +
                    "&7skills and collections by mining items, &7farming crops, &7and killing monsters!");
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);
                    Text.applyText(player, "&e&lNPC &r&8| &7Can you get me &e20 &fWood&7?");
                }
            }.runTaskLater(skrpg, 30);
        } else if (phase == 3) {
            playerData.setQuestPhase(3);
            if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals(Text.color("&fWood")) && player.getInventory().getItemInMainHand().getAmount() >= 20) {
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1.0f, 1.0f);
                Text.applyText(player, "&e&lNPC &r&8| &7That's it! Thank you!");
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);
                        Text.applyText(player, "&e&lNPC &r&8| &7You can use your wood to craft planks, and make sticks to craft a basic hilt. Use this to craft tools." +
                                "Here's the recipe. Take this &fWooden Sword&7.");
                        player.performCommand("recipe BASIC_HILT");
                        player.getInventory().addItem(Items.buildItem(Items.WOODEN_SWORD));
                    }
                }.runTaskLater(skrpg, 30);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);
                        Text.applyText(player, "&e&lNPC &r&8| &7Down there is SKVille... " +
                                "But you have to run &7through the &cHostile Plains &7to get there... &7Good luck!");
                        Quests.completeQuest(Quests.TUTORIAL, player, skrpg.getPlayerManager().getPlayerData(player.getUniqueId()), skrpg);
                    }
                }.runTaskLater(skrpg, 60);
            }
        }
    }
}
