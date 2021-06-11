package com.auxdible.skrpg.player.quests;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.ItemInfo;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.items.SKRPGItemStack;
import com.auxdible.skrpg.player.skills.Level;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;

public class FishermanQuest implements Quest {
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
        return 1000;
    }

    @Override
    public int questPhases() {
        return 2;
    }

    @Override
    public Level levelRequired() {
        return Level._4;
    }

    @Override
    public List<Double> xpRewards() {
        return Arrays.asList(0.0, 0.0, 0.0, 0.0, 200.0);
    }

    public void giveItems(Player player, PlayerData playerData, Items itemType, SKRPG skrpg) {
        if (!playerData.hasQuest(getQuestType()))  { return; }
        int amount = 0;
        for (ItemStack itemStack : Arrays.asList(player.getInventory().getContents())) {
            ItemInfo itemInfo = ItemInfo.parseItemInfo(itemStack);
            if (itemInfo != null) {
                if (itemInfo.getItem() == itemType) {
                    amount = amount + itemStack.getAmount();
                }
            }
        }
        if (itemType.equals(Items.FISHING_ROD) && phase == 1) {
            setPhase(2);
            executePhase(player, skrpg);
        } else if (phase == 2) {
            List<Items> items = Arrays.asList(Items.COD, Items.SALMON, Items.PUFFERFISH, Items.LILY_PAD,
                    Items.SEASHELL, Items.SEAGRASS, Items.INK_SAC);
            if (items.contains(itemType)) {
                setPhase(3);
                executePhase(player, skrpg);
            }
        }
    }
    @Override
    public void executePhase(Player player, SKRPG skrpg) {
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(player.getUniqueId());
        if (phase == 1) {
            setPhase(1);
            player.teleport(player.getWorld().getSpawnLocation());
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 0.4f);
            Text.applyText(player, "&e&lFisherman &r&8| &7Hello. I am one of the oldest fishermen in SKVille. I train newcomers the art of &eFishing&7. &7Have you seen the &eBeachmaster &7around recently? &7He is one of my students. &7I am fascinated with his interest in &ecrabs&7.");
            new BukkitRunnable() {

                @Override
                public void run() {
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 0.4f);
                    Text.applyText(player, "&e&lFisherman &r&8| &7Erhm, excuse me. Sorry. In order to start fishing, you must craft a &fFishing Rod&7. You can find the string required to obtain a fishing rod in the &eSpider Caves&7, which are located in the bottom of &eThe Dark Thickets&7.");
                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 0.2f);
                            player.performCommand("recipe FISHING_ROD");
                            Text.applyText(player, "&e&lCrafting &r&8| &7To see this crafting recipe again, type &e/recipe FISHING_ROD&7.");
                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 0.4f);
                                    Text.applyText(player, "&e&lFisherman &r&8| &7Bring me back your &fFishing Rod&7, wont'ya?");
                                }
                            }.runTaskLater(skrpg, 60);
                        }
                    }.runTaskLater(skrpg, 40);

                }
            }.runTaskLater(skrpg, 40);
        } else if (phase == 2) {
            setPhase(2);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 0.4f);
            Text.applyText(player, "&e&lFisherman &r&8| &7That's one good rod you got yourself. Toss your hook into the pond and bring me back what you catch.");
        } else if (phase == 3) {
            setPhase(3);
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1.0f, 0.4f);
            Text.applyText(player, "&e&lFisherman &r&8| &7Holy guacamole! That smells of the sea. Good catch, traveller. Take some of my spare nuggets, and treat yourself, wont'ya?");

            Quests.completeQuest(Quests.FISHERMAN_QUEST, player, playerData, skrpg);
        }
    }

    @Override
    public String name() {
        return "Smells of the Sea";
    }

    @Override
    public Quests getQuestType() {
        return Quests.FISHERMAN_QUEST;
    }
}

