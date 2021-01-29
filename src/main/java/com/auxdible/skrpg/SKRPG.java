package com.auxdible.skrpg;

import com.auxdible.skrpg.commands.*;
import com.auxdible.skrpg.commands.admin.*;
import com.auxdible.skrpg.items.ItemType;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.mobs.*;
import com.auxdible.skrpg.mobs.npcs.NPCManager;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.player.PlayerJoinLeaveListener;
import com.auxdible.skrpg.player.PlayerListener;
import com.auxdible.skrpg.player.PlayerManager;
import com.auxdible.skrpg.player.economy.TradeManager;
import com.auxdible.skrpg.regions.Region;
import com.auxdible.skrpg.regions.RegionManager;
import com.auxdible.skrpg.utils.Text;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;

public class SKRPG extends JavaPlugin {
    public File playerData;
    public File getPlayerDataFile() { return playerData; }
    public FileConfiguration playerDataConfig;
    public FileConfiguration getPlayerData() { return playerDataConfig; }

    public PlayerManager playerManager;
    public PlayerManager getPlayerManager() { return playerManager; }

    public MobManager mobManager;
    public MobManager getMobManager() { return mobManager; }

    public RegionManager regionManager;
    public RegionManager getRegionManager() { return regionManager; }

    public NPCManager npcManager;
    public NPCManager getNpcManager() { return npcManager; }

    public MobSpawnManager mobSpawnManager;
    public MobSpawnManager getMobSpawnManager() { return mobSpawnManager; }

    public TradeManager tradeManager;
    public TradeManager getTradeManager() { return tradeManager; }

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Loading SKRPG core plugin...");
        saveDefaultConfig();
        playerData = new File(getDataFolder(), "playerdata.yml");
        if (!playerData.exists()) {
            playerData.getParentFile().mkdirs();
            saveResource("playerdata.yml", false);
        }
        playerDataConfig = new YamlConfiguration();
        try {
            playerDataConfig.load(playerData);
        } catch (IOException | InvalidConfigurationException x) {
            x.printStackTrace();
        }

        playerManager = new PlayerManager(this);
        mobManager = new MobManager(this);
        regionManager = new RegionManager(this);
        npcManager = new NPCManager(this);
        mobSpawnManager = new MobSpawnManager(this);
        tradeManager = new TradeManager(this);
        mobSpawnManager.enable();
        npcManager.enable();
        playerManager.enable();
        regionManager.enable();
        Bukkit.getPluginManager().registerEvents(new PlayerJoinLeaveListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
        Bukkit.getWorld(getConfig().getString("rpgWorld")).setGameRule(GameRule.NATURAL_REGENERATION, false);
        Bukkit.getWorld(getConfig().getString("rpgWorld")).setGameRule(GameRule.DO_MOB_SPAWNING, false);
        getCommand("giveitem").setExecutor(new GiveItemCommand(this));
        getCommand("menu").setExecutor(new MenuCommand(this));
        getCommand("createregion").setExecutor(new CreateRegionCommand(this));
        getCommand("ct").setExecutor(new CraftingTableCommand(this));
        getCommand("stats").setExecutor(new StatsCommand(this));
        getCommand("skills").setExecutor(new SkillsCommand(this));
        getCommand("createnpc").setExecutor(new CreateNPCCommand(this));
        getCommand("createMobSpawn").setExecutor(new CreateMobSpawnCommand(this));
        getCommand("addCredits").setExecutor(new AddCreditsCommand(this));
        getCommand("setSpeed").setExecutor(new SpeedCommand(this));
        getCommand("trade").setExecutor(new TradeCommand(this));
        getCommand("spawnEntity").setExecutor(new SpawnEntityCommand(this));
        playerManager.enable();
        getLogger().info("Loaded SKRPG core plugin!");
        new BukkitRunnable() {

            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    PlayerData playerData = getPlayerManager().getPlayerData(player.getUniqueId());
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            Text.color(
                                    "&c" + playerData.getHp() + "/" + playerData.getMaxHP() + " ♥ " +
                                            "&a" + playerData.getDefence() + " ✿ Defence " +
                                            "&e" + playerData.getEnergy() + "/" + playerData.getMaxEnergy() + " ☢ Energy"
                            )
                    ));
                    if (playerData.getHp() <= 0) {
                        if (getPlayerManager().getPlayerData(player.getUniqueId()).getHp() <= 0) {
                            player.teleport(Bukkit.getWorld(getConfig().getString("rpgWorld")).getSpawnLocation());
                            player.playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, 1.0f, 0.5f);
                            player.sendTitle(Text.color("&4&l☠"), Text.color("&c&lYOU DIED"), 40, 100, 10);
                            getPlayerManager().getPlayerData(player.getUniqueId()).setHp(
                                    getPlayerManager().getPlayerData(player.getUniqueId()).getMaxHP());
                        }
                    }
                    boolean regionFound = false;
                    for (Region region : getRegionManager().getRegions()) {
                        if (player.getLocation().getX() >= region.getX() &&
                                player.getLocation().getX() <= region.getX2() &&
                                player.getLocation().getZ() >= region.getZ() &&
                                player.getLocation().getZ() <= region.getZ2()) {
                            if (playerData.getRegion() != region) {
                                player.sendTitle(Text.color("&e&lYou are now entering"),
                                        Text.color(region.getName()), 20, 40, 20);
                            }
                            getPlayerManager().getPlayerData(player.getUniqueId()).setRegion(region);
                            regionFound = true;
                            break;
                        }
                    }
                    if (!regionFound) {
                            getPlayerManager().getPlayerData(player.getUniqueId()).setRegion(null);
                    }
                    int strengthIncrease = 0;
                    int defenceIncrease = 0;
                    int energyIncrease = 0;
                    int hpIncrease = 0;
                    int speedIncrease = 0;
                    for (Items item : Items.values()) {
                        if (player.getInventory().getItemInMainHand().getType() != Material.AIR) {
                            if (player.getInventory().getItemInMainHand().getItemMeta().getDisplayName().contains(
                                    Text.color(item.getName())) && item.getItemType().equals(ItemType.SWORD)) {
                                strengthIncrease = strengthIncrease + item.getStrength();
                                defenceIncrease = defenceIncrease + item.getDefence();
                                energyIncrease = energyIncrease + item.getEnergy();
                                hpIncrease = hpIncrease + item.getHp();
                                speedIncrease = speedIncrease + item.getSpeed();
                            }
                        }
                        if (player.getInventory().getHelmet() != null) {
                            if (player.getInventory().getHelmet().getItemMeta().getDisplayName().contains(
                                    Text.color(item.getName())) && item.getItemType().equals(ItemType.ARMOR)) {
                                strengthIncrease = strengthIncrease + item.getStrength();
                                defenceIncrease = defenceIncrease + item.getDefence();
                                energyIncrease = energyIncrease + item.getEnergy();
                                hpIncrease = hpIncrease + item.getHp();
                                speedIncrease = speedIncrease + item.getSpeed();
                            }
                        }
                        if (player.getInventory().getChestplate() != null) {
                            if (player.getInventory().getChestplate().getItemMeta().getDisplayName().contains(
                                    Text.color(item.getName())) && item.getItemType().equals(ItemType.ARMOR)) {
                                strengthIncrease = strengthIncrease + item.getStrength();
                                defenceIncrease = defenceIncrease + item.getDefence();
                                energyIncrease = energyIncrease + item.getEnergy();
                                hpIncrease = hpIncrease + item.getHp();
                                speedIncrease = speedIncrease + item.getSpeed();
                            }
                        }
                        if (player.getInventory().getLeggings() != null) {
                            if (player.getInventory().getLeggings().getItemMeta().getDisplayName().contains(
                                    Text.color(item.getName())) && item.getItemType().equals(ItemType.ARMOR)) {
                                strengthIncrease = strengthIncrease + item.getStrength();
                                defenceIncrease = defenceIncrease + item.getDefence();
                                energyIncrease = energyIncrease + item.getEnergy();
                                hpIncrease = hpIncrease + item.getHp();
                                speedIncrease = speedIncrease + item.getSpeed();
                            }
                        }
                        if (player.getInventory().getBoots() != null) {
                            if (player.getInventory().getBoots().getItemMeta().getDisplayName().contains(
                                    Text.color(item.getName())) && item.getItemType().equals(ItemType.ARMOR)) {
                                strengthIncrease = strengthIncrease + item.getStrength();
                                defenceIncrease = defenceIncrease + item.getDefence();
                                energyIncrease = energyIncrease + item.getEnergy();
                                hpIncrease = hpIncrease + item.getHp();
                                speedIncrease = speedIncrease + item.getSpeed();
                            }
                        }

                    }
                    playerData.setStrength(playerData.getBaseStrength() + strengthIncrease);
                    playerData.setDefence(playerData.getBaseDefence() + defenceIncrease);
                    playerData.setMaxEnergy(playerData.getBaseEnergy() + energyIncrease);
                    playerData.setMaxHP(playerData.getBaseHP() + hpIncrease);
                    playerData.setSpeed(playerData.getBaseSpeed() + speedIncrease);
                }
            }
        }.runTaskTimer(this, 0, 20);
        new BukkitRunnable() {

            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    PlayerData playerData = getPlayerManager().getPlayerData(player.getUniqueId());
                    if (playerData.getHp() < playerData.getMaxHP()) {
                        if ((int) Math.round(playerData.getMaxHP() * 0.1) > playerData.getMaxHP()) {
                            playerData.setHp(playerData.getMaxHP());
                        } else {
                            playerData.setHp(playerData.getHp() + (int) Math.round(playerData.getMaxHP() * 0.1));
                        }


                    }
                    if (playerData.getEnergy() < playerData.getMaxEnergy()) {
                        if ((int) Math.round(playerData.getMaxEnergy() * 0.1) > playerData.getMaxEnergy()) {
                            playerData.setEnergy(playerData.getMaxEnergy());
                        } else {
                            playerData.setEnergy(playerData.getEnergy() + (int) Math.round(playerData.getMaxEnergy() * 0.1));
                        }


                    }
                }
            }
        }.runTaskTimer(this, 0, 40);
        SKRPG skrpg = this;
        new BukkitRunnable() {
            @Override
            public void run() {
                for (MobSpawn mobSpawn : mobSpawnManager.getMobSpawns()) {
                    if (mobSpawn.getCurrentlySpawnedMobs() == null || mobSpawn.getCurrentlySpawnedMobs().isEmpty()) {
                        MobType.buildMob(mobSpawn.getMob().getId(), skrpg, mobSpawn);
                    }
                }
            }
        }.runTaskTimer(this, 0, 300);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        playerManager.disable();
        regionManager.disable();
        npcManager.disable();
        mobSpawnManager.disable();
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.kickPlayer("The server is reloading!");
        }
        for (Entity entity : Bukkit.getWorld(getConfig().getString("rpgWorld")).getEntities()) {
            entity.remove();
        }
    }
}
