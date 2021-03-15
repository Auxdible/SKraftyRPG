package com.auxdible.skrpg.player.quests;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.CraftingIngrediant;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.mobs.npcs.NPC;
import com.auxdible.skrpg.mobs.npcs.NpcType;
import com.auxdible.skrpg.mobs.npcs.npcs.TutorialNPCVillager;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.player.skills.Level;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
        return 6;
    }

    @Override
    public Level levelRequired() {
        return Level._0;
    }

    @Override
    public ArrayList<Integer> xpRewards() {
        return null;
    }

    public void giveItems(Player player, PlayerData playerData, ItemStack itemStack, Items itemType, SKRPG skrpg) {
        if (playerData.getActiveQuest() != Quests.TUTORIAL) { return; }

        if (itemType.equals(Items.WOOD) && itemStack.getAmount() >= 5 && playerData.getQuestPhase() == 2) {
            executePhase(3, player, skrpg);
        } else if (itemType.equals(Items.WOODEN_PICKAXE) && itemStack.getAmount() >= 1 && playerData.getQuestPhase() == 3) {
            executePhase(4, player, skrpg);
        } else if (itemType.equals(Items.STONE) && itemStack.getAmount() >= 25 && playerData.getQuestPhase() == 4) {
            executePhase(5, player, skrpg);
        } else if (itemType.equals(Items.WHEAT) && itemStack.getAmount() >= 50 && playerData.getQuestPhase() == 5) {
            executePhase(6, player, skrpg);
        }
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
                    Text.applyText(player, "&e&lNPC &r&8| &7Welcome to SKRPG, a world of adventure. Meet me at the pathway to SKVille.");
                }
            }.runTaskLater(skrpg, 30);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!player.isOnline()) {
                        cancel();
                    }
                    for (NPC npc : skrpg.getNpcManager().getNpcs()) {
                        if (npc instanceof TutorialNPCVillager && npc.getLocation().distance(player.getLocation()) <= 7) {
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
                    "&7skills and collections by mining items, &7farming crops, &7crafting items, &7killing monsters, and more!");
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);
                    Text.applyText(player, "&e&lNPC &r&8| &7Can you get me &e5 &fWood&7?");
                }
            }.runTaskLater(skrpg, 30);
        } else if (phase == 3) {
            playerData.setQuestPhase(3);
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1.0f, 1.0f);
                Text.applyText(player, "&e&lNPC &r&8| &7That's it! Thank you!");
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);
                        Text.applyText(player, "&e&lNPC &r&8| &7You can use your wood to craft planks, and make sticks to craft a basic hilt. Use this to craft tools. " +
                                "I will show you the recipe. Take this &fBasic Hilt&7.");
                        player.performCommand("recipe BASIC_HILT");
                        player.getInventory().addItem(Items.buildItem(Items.BASIC_HILT));
                    }
                }.runTaskLater(skrpg, 30);
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);
                        Text.applyText(player, "&e&lNPC &r&8| &7Can you craft me a &fWooden Pickaxe? &7If you want a list of every recipe in the game, you can do /recipe list! &7Do /recipe WOODEN_PICKAXE to view the recipe to craft a &fWooden Pickaxe. &7Do /menu to access the crafting table.");
                    }
                }.runTaskLater(skrpg, 40);

        } else if (phase == 4) {
            playerData.setQuestPhase(4);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1.0f, 1.0f);
            Text.applyText(player, "&e&lNPC &r&8| &7That's it! Thank you!");
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);
                    Text.applyText(player, "&e&lNPC &r&8| &7Can you bring me 25 &fStone &7with the pickaxe you crafted?");
                }
            }.runTaskLater(skrpg, 30);
        } else if (phase == 5) {
            playerData.setQuestPhase(5);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1.0f, 1.0f);
            Text.applyText(player, "&e&lNPC &r&8| &7That's it! Thank you!");
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);
                    Text.applyText(player, "&e&lNPC &r&8| &7I think I'm hungry... Can you bring me &e50 &fWheat&7?");
                }
            }.runTaskLater(skrpg, 30);
        } else if (phase == 6) {
            Quests.completeQuest(Quests.TUTORIAL, player, skrpg.getPlayerManager().getPlayerData(player.getUniqueId()), skrpg);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1.0f, 1.0f);
            Text.applyText(player, "&e&lNPC &r&8| &7That's it! Thank you!");
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 1.0f);
                    Text.applyText(player, "&e&lNPC &r&8| &7Farther down the &7pathway is the way to &eSKVille&7... " +
                            "But you have to run &7through the &cHostile Plains &7to get there... &7Good luck!");

                }
            }.runTaskLater(skrpg, 60);
        }



}
}

