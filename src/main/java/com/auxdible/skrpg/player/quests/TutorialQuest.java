package com.auxdible.skrpg.player.quests;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.items.SKRPGItemStack;
import com.auxdible.skrpg.player.skills.Level;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class TutorialQuest implements Quest {
    private int phase;
    private List<Integer> totemsInteracted;
    private List<String> crystalsGathered;
    private Player player;
    private int monolithWarriorsKilled;
    private int woodCollected;
    private SKRPG skrpg;
    private PlayerData playerData;
    private boolean hasMetTable;

    public boolean isHasMetTable() { return hasMetTable; }

    public void setHasMetTable(boolean hasMetTable) { this.hasMetTable = hasMetTable; }

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

    public void setMonolithWarriorsKilled(int monolithWarriorsKilled) { this.monolithWarriorsKilled = monolithWarriorsKilled;
        this.player = Bukkit.getPlayer(playerData.getUuid());
    if (getMonolithWarriorsKilled() >= 5) {
            Text.applyText(player, "&b&lForgotten Sword &r&8| &r&7I see you are no ordinary warrior, my comrade. Go forth.");
            new BukkitRunnable() {

                @Override
                public void run() {
                    obtainCrystal("COMBAT");
                }
            }.runTaskLater(skrpg, 100);
        }
    }

    public int getMonolithWarriorsKilled() { return monolithWarriorsKilled; }

    public void setWoodCollected(int woodCollected) { this.woodCollected = woodCollected;
        this.player = Bukkit.getPlayer(playerData.getUuid());
    if (getWoodCollected() >= 5) {
            Text.applyText(player, "&7&lRugged Axe &r&8| &r&7It's working! The Crystal of Collecting is powering up.");
            new BukkitRunnable() {

                @Override
                public void run() {
                    obtainCrystal("COLLECTING");
                }
            }.runTaskLater(skrpg, 100);
        }
    }

    public int getWoodCollected() { return woodCollected; }

    public void addTotem(int totem) {
        this.player = Bukkit.getPlayer(playerData.getUuid());
        getTotemsInteracted().add(totem);
        if (getTotemsInteracted().size() == 4 && !getTotemsInteracted().contains(-1)) {
            getTotemsInteracted().add(-1);
            Text.applyText(player, "&6&lTotems &r&8| &r&7We see you know my knowledge. Go forth.");
            new BukkitRunnable() {

                @Override
                public void run() {
                    obtainCrystal("COMPLEXITY");
                }
            }.runTaskLater(skrpg, 100);

        }
    }
    public List<Integer> getTotemsInteracted() { return totemsInteracted; }

    public List<String> getCrystalsGathered() { return crystalsGathered; }

    public void obtainCrystal(String crystal) {
        this.player = Bukkit.getPlayer(playerData.getUuid());
        if (crystal.equals("COMPLEXITY")) {
            new BukkitRunnable() {

                @Override
                public void run() {
                    Text.applyTypingTitle(skrpg, player, "&e[COMPLEXITY CRYSTAL ACTIVATED]", 5, 30);
                }
            }.runTaskLater(skrpg, 40);
            crystalsGathered.add(crystal);
        } else if (crystal.equals("COMBAT")) {
            new BukkitRunnable() {

                @Override
                public void run() {
                    Text.applyTypingTitle(skrpg, player, "&c[COMBAT CRYSTAL ACTIVATED]", 5, 30);
                }
            }.runTaskLater(skrpg, 40);
            crystalsGathered.add(crystal);
        } else if (crystal.equals("CRAFTING")) {
            new BukkitRunnable() {

                @Override
                public void run() {
                    Text.applyTypingTitle(skrpg, player, "&6[CRAFTING CRYSTAL ACTIVATED]", 5, 30);
                }
            }.runTaskLater(skrpg, 40);
            crystalsGathered.add(crystal);
        } else if (crystal.equals("COLLECTING")) {
            new BukkitRunnable() {

                @Override
                public void run() {
                    Text.applyTypingTitle(skrpg, player, "&b[COLLECTING CRYSTAL ACTIVATED]", 5, 30);
                }
            }.runTaskLater(skrpg, 40);
            crystalsGathered.add(crystal);
        }
        if (crystalsGathered.size() == 1) {
            Text.applyText(player, "&a&lLife Crystal &r&8| &r&7Good job! Return to the main hallway and find the next crystal.");
        } else if (crystalsGathered.size() == 2) {
            Text.applyText(player, "&a&lLife Crystal &r&8| &r&7Onto the next crystal.");
        } else if (crystalsGathered.size() == 3) {
            Text.applyText(player, "&a&lLife Crystal &r&8| &r&7Almost there! One more crystal needs to be powered in order to proceed into the &aIslands of Kaleiden&7.");
        } else if (crystalsGathered.size() == 4) {
            Text.applyText(player, "&a&lLife Crystal &r&8| &r&7You did it! The Monolith Door has been unlocked. &7Once you are sure you wish to leave, open the door. You cannot return here.");
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BREATH, 1.0f, 0.2f);
        }
    }
    @Override
    public String parseData() {
        StringJoiner crystalsGatheredJoiner = new StringJoiner("#");
        if (getCrystalsGathered() != null) {
            for (String s : getCrystalsGathered()) {
                crystalsGatheredJoiner.add(s);
            }
            if (crystalsGatheredJoiner.length() == 0) {
                crystalsGatheredJoiner.add(" ");
            }
        } else {
            crystalsGatheredJoiner.add(" ");
        }

        StringJoiner totemsFoundJoiner = new StringJoiner("#");
        if (getTotemsInteracted() != null) {
            for (Integer i : getTotemsInteracted()) {
                totemsFoundJoiner.add(i + "");
            }
            if (crystalsGatheredJoiner.length() == 0) {
                totemsFoundJoiner.add(" ");
            }
        } else {
            totemsFoundJoiner.add(" ");
        }
        return phase + ":" + totemsFoundJoiner.toString() + ":" + crystalsGatheredJoiner.toString();
    }

    @Override
    public void stringToData(String data) {
        List<String> dataList = Arrays.asList(data.split(":"));
        phase = Integer.parseInt(dataList.get(0));
        List<Integer> totems = new ArrayList<>();
        if (!dataList.get(1).equals(" ")) {
            for (String s : dataList.get(1).split("#")) {
                totems.add(Integer.parseInt(s));
            }

        }
        if (totemsInteracted == null) {
            totemsInteracted = new ArrayList<>();
        }
        totemsInteracted = totems;
        if (crystalsGathered == null) {
            crystalsGathered = new ArrayList<>();
        }
        if (!dataList.get(2).equals(" ")) {

            crystalsGathered = new ArrayList<>(Arrays.asList(dataList.get(2).split("#")));
        }
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
    public ArrayList<Double> xpRewards() {
        return null;
    }


    @Override
    public void executePhase(Player player, SKRPG skrpg) {
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(player.getUniqueId());
        this.player = player;
        this.playerData = playerData;
        this.skrpg = skrpg;
        totemsInteracted = new ArrayList<>();
        crystalsGathered = new ArrayList<>();
        if (phase == 1) {
            player.teleport(skrpg.getLocationManager().getMonolithSpawnLocation());
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200000, 180));
            setPhase(2);
            new BukkitRunnable() {

                @Override
                public void run() {
                    Text.applyTypingTitle(skrpg, player, "&a[LIFE CRYSTAL ACTIVATED]", 5, 30);
                }
            }.runTaskLater(skrpg, 100);

            new BukkitRunnable() {

                @Override
                public void run() {
                    Text.applyText(player, "&a&lLife Crystal &r&8| &7Ah, you're alive. I am the crystal of life. I have protected you. You've been dead for over &a&k600 &r&7years.");
                    new BukkitRunnable() {

                        @Override
                        public void run() {
                            Text.applyText(player, "&a&lLife Crystal &r&8| &7Where are you? This is the &aMonolith&7. It's currently out of power, and can only be opened once. There are &a4 Monolith Crystals &7you have to collect, in order to power the &aMonolith Gate&7. Each zone of the monolith contains a crystal.");
                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    Text.applyText(player, "&a&lLife Crystal &r&8| &7Proceed to the main hall.");
                                    player.removePotionEffect(PotionEffectType.SLOW);
                                }
                            }.runTaskLater(skrpg, 100);
                        }
                    }.runTaskLater(skrpg, 100);
                }
            }.runTaskLater(skrpg, 260);
        }
    }

    @Override
    public String name() {
        return "The Monolith [Tutorial Pt. 1]";
    }

    @Override
    public Quests getQuestType() {
        return Quests.TUTORIAL;
    }
}

