package com.auxdible.skrpg.commands.inventory;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.mobs.MobType;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.player.collections.Tiers;
import com.auxdible.skrpg.player.skills.Level;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.ItemTweaker;
import com.auxdible.skrpg.utils.Text;
import com.mojang.datafixers.types.Func;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SkillsCommand implements CommandExecutor {
    private SKRPG skrpg;
    public SkillsCommand(SKRPG skrpg) {
        this.skrpg = skrpg;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            skrpg.getLogger().info("You must be a player to use this command!");
        }
        Player p = (Player) sender;
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(p.getUniqueId());
        if (args.length == 0) {
            Inventory inv = Bukkit.createInventory(null, 36, "Your Skills");
            for (int i = 0; i <= 35; i++) {
                inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
            }
            playerData.getGlobal().calculateGlobalLevel(playerData, skrpg);
            inv.setItem(4, new ItemBuilder(ItemTweaker.createPlayerHeadFromData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWRmYzg5MzI4NjVmZDU3ZDlkMjM2NWYxYWUyZDQ3NTEzNWQ3NDZiMmFmMTVhYmQzM2ZmYzJhNmFiZDM2MjgyIn19fQ==", "10174993-9b01-4045-919c-bf82dbc490e1")).setName("&a&l☼ &r&7Global &a" + playerData.getGlobal().getGlobalLevel() + " &a&l☼").setLore(Arrays.asList(" ", "&7&oYour global level is all of your skill levels combined.", "&7&oThe top 10 players are put on the Global Level Leaderboard,", "&7&oviewable in Dortoh, and Descanso!", " ")).asItem());

            Level levelCombat;
            String xpTillCombat;
            if (playerData.getCombat().getLevel() != Level._50) {
                levelCombat = Level.valueOf("_" +
                        (Integer.parseInt(playerData.getCombat().getLevel().toString()
                                .replace("_", "")) + 1));
                xpTillCombat = "" + levelCombat.getXpRequired();
            } else {
                levelCombat = Level._50;
                xpTillCombat = "&6&lCOMBAT SKILL MAXXED!";
            }
            Level levelMining;
            String xpTillMining;
            if (playerData.getMining().getLevel() != Level._50) {
                levelMining = Level.valueOf("_" +
                        (Integer.parseInt(playerData.getMining().getLevel().toString()
                                .replace("_", "")) + 1));
                xpTillMining = "" + levelMining.getXpRequired();
            } else {
                levelMining = Level._50;
                xpTillMining = "&6&lMINING SKILL MAXXED!";
            }
            Level levelHerbalism;
            String xpTillHerbalism;
            if (playerData.getHerbalism().getLevel() != Level._50) {
                levelHerbalism = Level.valueOf("_" +
                        (Integer.parseInt(playerData.getHerbalism().getLevel().toString()
                                .replace("_", "")) + 1));
                xpTillHerbalism = "" + levelHerbalism.getXpRequired();
            } else {
                levelHerbalism = Level._50;
                xpTillHerbalism = "&6&lHERBALISM SKILL MAXXED!";
            }
            Level levelCrafting;
            String xpTillCrafting;
            if (playerData.getCrafting().getLevel() != Level._50) {
                levelCrafting = Level.valueOf("_" +
                        (Integer.parseInt(playerData.getCrafting().getLevel().toString()
                                .replace("_", "")) + 1));
                xpTillCrafting = "" + levelCrafting.getXpRequired();
            } else {
                levelCrafting = Level._50;
                xpTillCrafting = "&6&lCRAFTING SKILL MAXXED!";
            }
            Level levelRunics;
            String xpTillRunics;
            if (playerData.getCrafting().getLevel() != Level._50) {
                levelRunics = Level.valueOf("_" +
                        (Integer.parseInt(playerData.getRunics().getLevel().toString()
                                .replace("_", "")) + 1));
                xpTillRunics = "" + levelRunics.getXpRequired();
            } else {
                levelRunics = Level._50;
                xpTillRunics = "&5&lRUNICS SKILL MAXXED!";
            }
            Level levelFishing;
            String xpTillFishing;
            if (playerData.getCrafting().getLevel() != Level._50) {
                levelFishing = Level.valueOf("_" +
                        (Integer.parseInt(playerData.getFishing().getLevel().toString()
                                .replace("_", "")) + 1));
                xpTillFishing = "" + levelFishing.getXpRequired();
            } else {
                levelFishing = Level._50;
                xpTillFishing = "&6&lFISHING SKILL MAXXED!";
            }
            inv.setItem(13, new ItemBuilder(Material.DIAMOND_SWORD, 0).setName("&7Combat &6" +
                    levelCombat.toString().replace("_", "")).setLore(
                    Arrays.asList(Text.color("&6" + playerData.getCombat().getXpTillNext() + "&7/&6" +
                                    xpTillCombat),
                            Text.color("&7Total Combat XP: &6" + playerData.getCombat().getTotalXP()))).asItem());
            inv.setItem(12, new ItemBuilder(Material.DIAMOND_PICKAXE, 0).setName("&7Mining &6" +
                    levelMining.toString().replace("_", "")).setLore(
                    Arrays.asList(Text.color("&6" + playerData.getMining().getXpTillNext() + "&7/&6" +
                                    xpTillMining),
                            Text.color("&7Total Mining XP: &6" + playerData.getMining().getTotalXP()))).asItem());
            inv.setItem(14, new ItemBuilder(Material.WHEAT, 0).setName("&7Herbalism &6" +
                    levelHerbalism.toString().replace("_", "")).setLore(
                    Arrays.asList(Text.color("&6" + playerData.getHerbalism().getXpTillNext() + "&7/&6" +
                                    xpTillHerbalism),
                            Text.color("&7Total Herbalism XP: &6" + playerData.getHerbalism().getTotalXP()))).asItem());
            inv.setItem(23, new ItemBuilder(Material.CRAFTING_TABLE, 0).setName("&7Crafting &6" +
                    levelCrafting.toString().replace("_", "")).setLore(
                    Arrays.asList(Text.color("&6" + playerData.getCrafting().getXpTillNext() + "&7/&6" +
                                    xpTillCrafting),
                            Text.color("&7Total Crafting XP: &6" + playerData.getCrafting().getTotalXP()))).asItem());
            inv.setItem(21, new ItemBuilder(Material.FISHING_ROD, 0).setName("&7Fishing &6" +
                    levelFishing.toString().replace("_", "")).setLore(
                    Arrays.asList(Text.color("&6" + playerData.getFishing().getXpTillNext() + "&7/&6" +
                                    xpTillFishing),
                            Text.color("&7Total Fishing XP: &6" + playerData.getFishing().getTotalXP()))).asItem());
            inv.setItem(22, new ItemBuilder(Material.DRAGON_BREATH, 0).setName("&7Runics &5" +
                    levelRunics.toString().replace("_", "")).setLore(
                    Arrays.asList(Text.color("&5" + playerData.getRunics().getXpTillNext() + "&7/&5" +
                                    xpTillRunics),
                            Text.color("&7Total Runics XP: &5" + playerData.getRunics().getTotalXP()))).asItem());
            inv.setItem(27, new ItemBuilder(Material.ARROW, 0).setName("&aBack to Menu").asItem());
            p.openInventory(inv);
        } else if (args[0].equalsIgnoreCase("combat")) {
            Inventory inv = Bukkit.createInventory(null, 27, "Your Combat");
            for (int i = 0; i <= 26; i++) {
                inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
            }
            if (playerData.getCombat().getLevel().equals(Level._50)) {
                inv.setItem(13, new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE, 0).setName("&6&lCombat Skill MAXXED!").setLore(
                        Arrays.asList(" ", Text.color("&7Total Skill XP: &6" + playerData.getCombat().getTotalXP()))
                ).asItem());
            } else {
                List<Level> levelsAfter = new ArrayList<>();
                Level currentLevel = playerData.getCombat().getLevel();
                int currentLevelInt = SKRPG.levelToInt(currentLevel.toString());
                for (int i = 1; i <= 3; i++) {
                    if (currentLevelInt + i == 50) {
                        levelsAfter.add(Level.valueOf("_" + (currentLevelInt + i)));
                        break;
                    }
                    levelsAfter.add(Level.valueOf("_" + (currentLevelInt + i)));
                }
                List<Integer> slots = Arrays.asList(12, 14, 16);
                inv.setItem(10, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE, 0).setName("&7Combat &6" + currentLevelInt).asItem());
                for (Level levels : levelsAfter) {
                    int creditsReward = levels.getXpRequired() / 2;

                    inv.setItem(slots.get(levelsAfter.indexOf(levels)), new ItemBuilder(Material.BARRIER, 0)
                            .setName("&7Combat &6" + SKRPG.levelToInt(levels.toString())).setLore(
                                    Arrays.asList(" ", Text.color("&7Rewards:"), Text.color("&6" + creditsReward + " Nuggets"),
                                            Text.color("&4+2 Strength ☄"),
                                            Text.color("&c2% more damage"))).asItem());
                }

            }
            p.openInventory(inv);
        } else if (args[0].equalsIgnoreCase("herbalism")) {
            Inventory inv = Bukkit.createInventory(null, 27, "Your Herbalism");
            for (int i = 0; i <= 26; i++) {
                inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
            }
            if (playerData.getHerbalism().getLevel().equals(Level._50)) {
                inv.setItem(13, new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE, 0).setName("&6&lHerbalism Skill MAXXED!").setLore(
                        Arrays.asList(" ", Text.color("&7Total Skill XP: &6" + playerData.getHerbalism().getTotalXP()))
                ).asItem());
            } else {
                List<Level> levelsAfter = new ArrayList<>();
                Level currentLevel = playerData.getHerbalism().getLevel();
                int currentLevelInt = SKRPG.levelToInt(currentLevel.toString());
                for (int i = 1; i <= 3; i++) {
                    if (currentLevelInt + i == 50) {
                        levelsAfter.add(Level.valueOf("_" + (currentLevelInt + i)));
                        break;
                    }
                    levelsAfter.add(Level.valueOf("_" + (currentLevelInt + i)));
                }
                List<Integer> slots = Arrays.asList(12, 14, 16);
                inv.setItem(10, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE, 0).setName("&7Herbalism &6" + currentLevelInt).asItem());
                for (Level levels : levelsAfter) {
                    int creditsReward = levels.getXpRequired() / 2;

                    inv.setItem(slots.get(levelsAfter.indexOf(levels)), new ItemBuilder(Material.BARRIER, 0)
                            .setName("&7Herbalism &6" + SKRPG.levelToInt(levels.toString())).setLore(
                                    Arrays.asList(" ", Text.color("&7Rewards:"), Text.color("&6" + creditsReward + " Nuggets"),
                                            Text.color("&c+2 HP ♥"),
                                            Text.color("&64% more double drop chance from crops"))).asItem());
                }

            }
            p.openInventory(inv);
        } else if (args[0].equalsIgnoreCase("crafting")) {
            Inventory inv = Bukkit.createInventory(null, 27, "Your Crafting");
            for (int i = 0; i <= 26; i++) {
                inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
            }
            if (playerData.getCrafting().getLevel().equals(Level._50)) {
                inv.setItem(13, new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE, 0).setName("&6&lCrafting Skill MAXXED!").setLore(
                        Arrays.asList(" ", Text.color("&7Total Skill XP: &6" + playerData.getCrafting().getTotalXP()))
                ).asItem());
            } else {
                List<Level> levelsAfter = new ArrayList<>();
                Level currentLevel = playerData.getCrafting().getLevel();
                int currentLevelInt = SKRPG.levelToInt(currentLevel.toString());
                for (int i = 1; i <= 3; i++) {
                    if (currentLevelInt + i == 50) {
                        levelsAfter.add(Level.valueOf("_" + (currentLevelInt + i)));
                        break;
                    }
                    levelsAfter.add(Level.valueOf("_" + (currentLevelInt + i)));
                }
                List<Integer> slots = Arrays.asList(12, 14, 16);
                inv.setItem(10, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE, 0).setName("&7Crafting &6" + currentLevelInt).asItem());
                for (Level levels : levelsAfter) {
                    int creditsReward = levels.getXpRequired() / 2;

                    inv.setItem(slots.get(levelsAfter.indexOf(levels)), new ItemBuilder(Material.BARRIER, 0)
                            .setName("&7Crafting &6" + SKRPG.levelToInt(levels.toString())).setLore(
                                    Arrays.asList(" ", Text.color("&7Rewards:"), Text.color("&6" + creditsReward + " Nuggets"),
                                            Text.color("&4+1 Strength ☄"),
                                            Text.color("&a+1 Defense ✿"))).asItem());
                }

            }
            p.openInventory(inv);
        } else if (args[0].equalsIgnoreCase("mining")) {
            Inventory inv = Bukkit.createInventory(null, 27, "Your Mining");
            for (int i = 0; i <= 26; i++) {
                inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
            }
            if (playerData.getMining().getLevel().equals(Level._50)) {
                inv.setItem(13, new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE, 0).setName("&6&lMining Skill MAXXED!").setLore(
                        Arrays.asList(" ", Text.color("&7Total Skill XP: &6" + playerData.getMining().getTotalXP()))
                ).asItem());
            } else {
                List<Level> levelsAfter = new ArrayList<>();
                Level currentLevel = playerData.getMining().getLevel();
                int currentLevelInt = SKRPG.levelToInt(currentLevel.toString());
                for (int i = 1; i <= 3; i++) {
                    if (currentLevelInt + i == 50) {
                        levelsAfter.add(Level.valueOf("_" + (currentLevelInt + i)));
                        break;
                    }
                    levelsAfter.add(Level.valueOf("_" + (currentLevelInt + i)));
                }
                List<Integer> slots = Arrays.asList(12, 14, 16);
                inv.setItem(10, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE, 0).setName("&7Mining &6" + currentLevelInt).asItem());
                for (Level levels : levelsAfter) {
                    int creditsReward = levels.getXpRequired() / 2;

                    inv.setItem(slots.get(levelsAfter.indexOf(levels)), new ItemBuilder(Material.BARRIER, 0)
                            .setName("&7Mining &6" + SKRPG.levelToInt(levels.toString())).setLore(
                                    Arrays.asList(" ", Text.color("&7Rewards:"), Text.color("&6" + creditsReward + " Nuggets"),
                                            Text.color("&a+2 Defense ✿"),
                                            Text.color("&64% more double drop chance"))).asItem());
                }

            }
            p.openInventory(inv);
        } else if (args[0].equalsIgnoreCase("runics")) {
            Inventory inv = Bukkit.createInventory(null, 27, "Your Runics");
            for (int i = 0; i <= 26; i++) {
                inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
            }
            if (playerData.getRunics().getLevel().equals(Level._50)) {
                inv.setItem(13, new ItemBuilder(Material.PURPLE_STAINED_GLASS_PANE, 0).setName("&5&lRunics Skill MAXXED!").setLore(
                        Arrays.asList(" ", Text.color("&7Total Skill XP: &5" + playerData.getRunics().getTotalXP()))
                ).asItem());
            } else {
                List<Level> levelsAfter = new ArrayList<>();
                Level currentLevel = playerData.getRunics().getLevel();
                int currentLevelInt = SKRPG.levelToInt(currentLevel.toString());
                for (int i = 1; i <= 3; i++) {
                    if (currentLevelInt + i == 50) {
                        levelsAfter.add(Level.valueOf("_" + (currentLevelInt + i)));
                        break;
                    }
                    levelsAfter.add(Level.valueOf("_" + (currentLevelInt + i)));
                }
                List<Integer> slots = Arrays.asList(12, 14, 16);
                inv.setItem(10, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE, 0).setName("&7Runics &5" + currentLevelInt).asItem());
                for (Level levels : levelsAfter) {
                    int creditsReward = levels.getXpRequired() / 2;
                    String unlockReward = "&cNONE";
                    if (levels == Level._1) {
                        unlockReward = "&7Level &51-2 &7Enchantments";
                    } else if (levels == Level._3) {
                        unlockReward = "&7Level &5Destroying Items";
                    } else if (levels == Level._5) {
                        unlockReward = "&7Level &53-4 &7Enchantments";
                    } else if (levels == Level._7) {
                        unlockReward = "&f&lCOMMON &r&5Runic Stones";
                    } else if (levels == Level._8) {
                        unlockReward = "&a&lUNCOMMON &r&5Runic Stones";
                    } else if (levels == Level._10) {
                        unlockReward = "&5Runic Shop";
                    } else if (levels == Level._11) {
                        unlockReward = "&7Level &55 &7Enchantments";
                    } else if (levels == Level._12) {
                        unlockReward = "&1&lRARE &r&5Runic Stones";
                    } else if (levels == Level._13) {
                        unlockReward = "&5&lEPIC &r&5Runic Stones";
                    } else if (levels == Level._14) {
                        unlockReward = "&e&lLEGENDARY &r&5Runic Stones";
                    } else if (levels == Level._25) {
                        unlockReward = "&5Personal Runic Table";
                    }
                    inv.setItem(slots.get(levelsAfter.indexOf(levels)), new ItemBuilder(Material.BARRIER, 0)
                            .setName("&7Runics &5" + SKRPG.levelToInt(levels.toString())).setLore(
                                    Arrays.asList(" ", Text.color("&7Rewards:"), Text.color("&6" + creditsReward + " Nuggets"),
                                            Text.color(unlockReward))).asItem());
                }

            }
            p.openInventory(inv);
        } else if (args[0].equalsIgnoreCase("fishing")) {
            Inventory inv = Bukkit.createInventory(null, 27, "Your Fishing");
            for (int i = 0; i <= 26; i++) {
                inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
            }
            if (playerData.getFishing().getLevel().equals(Level._50)) {
                inv.setItem(13, new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE, 0).setName("&6&lFishing Skill MAXXED!").setLore(
                        Arrays.asList(" ", Text.color("&7Total Skill XP: &6" + playerData.getFishing().getTotalXP()))
                ).asItem());
            } else {
                List<Level> levelsAfter = new ArrayList<>();
                Level currentLevel = playerData.getFishing().getLevel();
                int currentLevelInt = SKRPG.levelToInt(currentLevel.toString());
                List<MobType> marineSeaCreatures = Arrays.asList(MobType.FISH, MobType.FLOPPY_FISH, MobType.SQUID,
                        MobType.WATER_WEAKLING, MobType.WATER_ARCHER, MobType.WATER_WARRIOR, MobType.CRAB_ZOMBIE,
                        MobType.WATERFISH, MobType.WATERFISH, MobType.SEA_HORSE, MobType.SPEEDSTER_OF_THE_SEA, MobType.SEA_LORD);
                List<String> marineCreatureUnlocked = new ArrayList<>();


                for (int i = 1; i <= 3; i++) {

                    if (currentLevelInt + i == 50) {
                        levelsAfter.add(Level.valueOf("_" + (currentLevelInt + i)));
                        boolean foundCreature = false;
                        for (MobType mobType : marineSeaCreatures) {
                            if (currentLevelInt + i == mobType.getLevel()) {
                                marineCreatureUnlocked.add(Text.color("&7New Marine Creature: &3" + mobType.getName()));
                                foundCreature = true;
                            }
                        }
                        if (!foundCreature) {
                            marineCreatureUnlocked.add("&cNO MARINE CREATURE");
                        }
                        break;
                    }
                    levelsAfter.add(Level.valueOf("_" + (currentLevelInt + i)));
                    boolean foundCreature = false;
                    for (MobType mobType : marineSeaCreatures) {
                        if (currentLevelInt + i == mobType.getLevel()) {
                            marineCreatureUnlocked.add(Text.color("&7Unlock Marine Creature: &3" + mobType.getName()));
                            foundCreature = true;
                        }
                    }
                    if (!foundCreature) {
                        marineCreatureUnlocked.add("&cNO MARINE CREATURE");
                    }
                }


                List<Integer> slots = Arrays.asList(12, 14, 16);
                inv.setItem(10, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE, 0).setName("&7Fishing &6" + currentLevelInt).asItem());
                for (Level levels : levelsAfter) {
                    int creditsReward = levels.getXpRequired() / 2;

                    inv.setItem(slots.get(levelsAfter.indexOf(levels)), new ItemBuilder(Material.BARRIER, 0)
                            .setName("&7Fishing &6" + SKRPG.levelToInt(levels.toString())).setLore(
                                    Arrays.asList(" ", Text.color("&7Rewards:"), Text.color("&6" + creditsReward + " Nuggets"),
                                            Text.color("&f+1 Speed ≈"),
                                            Text.color("&60.5% more treasure chance"), marineCreatureUnlocked.get(levelsAfter.indexOf(levels)))).asItem());
                }

            }
            p.openInventory(inv);
        }

        return false;
    }
}
