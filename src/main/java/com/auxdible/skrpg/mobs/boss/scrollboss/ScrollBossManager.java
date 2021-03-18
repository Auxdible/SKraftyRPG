package com.auxdible.skrpg.mobs.boss.scrollboss;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.mobs.Mob;
import com.auxdible.skrpg.player.collections.CollectionType;
import com.auxdible.skrpg.utils.Text;
import com.auxdible.skrpg.utils.TopPlayerComparator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;

public class ScrollBossManager {
    private ArrayList<ScrollBoss> scrollBosses;
    private SKRPG skrpg;
    public ScrollBossManager(SKRPG skrpg) {
        this.scrollBosses = new ArrayList<>();
        this.skrpg = skrpg;
    }
    public void createBoss(ScrollBoss scrollBoss) {
        this.scrollBosses.add(scrollBoss);
        scrollBoss.spawnBoss();
    }
    public Player getFromRewardsList(ArrayList<Player> list, int index) {
        try {
            list.get(index);
        } catch (IndexOutOfBoundsException x) {
            return null;
        }
        return list.get(index);
    }
    public void rewards(HashMap<Player, Integer> rewards, ScrollBoss scrollBoss) {
        TopPlayerComparator valueComparatorTop3 = new TopPlayerComparator(rewards);
        TreeMap<Player, Integer> sortedMap = new TreeMap<>(valueComparatorTop3);
        sortedMap.putAll(rewards);
        ArrayList<Player> top3List = new ArrayList<>(sortedMap.keySet());
        if (top3List.size() < 3) {
            if (top3List.size() < 2) {
                top3List.subList(0, 1);
            } else { top3List.subList(0, 0); }

        } else {
            top3List.subList(0, 2);
        }
        Player top1 = getFromRewardsList(top3List, 0), top2 = getFromRewardsList(top3List, 1), top3 = getFromRewardsList(top3List, 2);
        HashMap<Player, Integer> lootWeight = new HashMap<>();
        for (Player players : rewards.keySet()) {
            int totalWeight = 0;
            if (scrollBoss.getScrollSpawner().getUniqueId() == players.getUniqueId()) {
                totalWeight = totalWeight + 75;
            }
            if (top3List.contains(players)) {
                totalWeight = totalWeight + 50;
            }
            if (rewards.get(players) >= scrollBoss.getMob().getMobType().getMaxHP() / 4) {
                totalWeight = totalWeight + 25;
            }
            lootWeight.put(players, totalWeight);
        }
        for (Player player : rewards.keySet()) {
            skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).addRunicPoints(125, skrpg);
            Text.applyText(player, "&8&m>                                          ");
            Text.applyText(player, "&4&l           " + scrollBoss.name().toUpperCase() + " DOWN!");
            Text.applyText(player, " ");
            Text.applyText(player, "&6&l1ST ۞: &7" + top1.getDisplayName() + " &8| &4" + rewards.get(top1) + " &7Damage");
            if (top2 != null) {
                Text.applyText(player, "&e&l2ND ✦: &7" + top2.getDisplayName() + " &8| &4" + rewards.get(top2) + " &7Damage");
            }
            if (top3 != null) {
                Text.applyText(player, "&c&l3RD ★: &7" + top3.getDisplayName() + " &8| &4" + rewards.get(top3) + " &7Damage");
            }

            Text.applyText(player, " ");
            Text.applyText(player, "&7Your Damage: &4" + rewards.get(player));
            Text.applyText(player, "&8&m>                                          ");
            player.playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, 1.0f, 0.2f);
            if (lootWeight.get(player) < 50) {
                skrpg.getPlayerManager().getPlayerData(player.getUniqueId())
                        .setCredits(skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getCredits() + 2500);
            } else if (lootWeight.get(player) >= 50) {
                if (lootWeight.get(player) >= 100) {
                    Random random = new Random();
                    int randomItem = random.nextInt(2);
                    if (randomItem == 1) {
                        player.getInventory().addItem(Items.buildItem(Items.CRAB_CLAW));
                        Bukkit.broadcastMessage(Text.color("&6&lSWEET! &8| &r&a" + player.getDisplayName() + " &7picked up the &5Crab Claw&7!"));
                    } else {
                        player.getInventory().addItem(Items.buildItem(Items.CRAB_CROWN));
                        Bukkit.broadcastMessage(Text.color("&6&lSWEET! &8| &r&a" + player.getDisplayName() + " &7picked up the &5Crab Crown&7!"));
                    }

                }
                if (lootWeight.get(player) >= 75) {
                    player.getInventory().addItem(Items.buildItem(Items.CRAB_FRAGMENT));
                }
                player.getInventory().addItem(Items.buildItem(Items.CRAB_FRAGMENT));
            }
        }

    }
    public void removeBoss(ScrollBoss scrollBoss) {
        this.scrollBosses.remove(scrollBoss);
    }
    public ArrayList<ScrollBoss> getBosses() { return scrollBosses; }
    public ScrollBoss getBoss(Player player) {
        for (ScrollBoss scrollBoss : scrollBosses) {
            if (scrollBoss.damageTotal().containsKey(player)) {
                return scrollBoss;
            }
        }
        return null;
    }
    public ScrollBoss getBoss(Mob mob) {
        for (ScrollBoss scrollBoss : scrollBosses) {
            if (scrollBoss.getMob().equals(mob)) {
                return scrollBoss;
            }
        }
        return null;
    }
}