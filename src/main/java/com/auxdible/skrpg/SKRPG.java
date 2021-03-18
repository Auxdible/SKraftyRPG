package com.auxdible.skrpg;

import com.auxdible.skrpg.commands.*;
import com.auxdible.skrpg.commands.admin.*;
import com.auxdible.skrpg.commands.inventory.*;
import com.auxdible.skrpg.items.ItemInfo;
import com.auxdible.skrpg.items.ItemType;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.items.enchantments.Enchantments;
import com.auxdible.skrpg.mobs.*;
import com.auxdible.skrpg.mobs.boss.scrollboss.ScrollBossManager;
import com.auxdible.skrpg.mobs.npcs.NPC;
import com.auxdible.skrpg.mobs.npcs.NPCManager;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.player.PlayerJoinLeaveListener;
import com.auxdible.skrpg.player.PlayerListener;
import com.auxdible.skrpg.player.PlayerManager;
import com.auxdible.skrpg.player.economy.TradeManager;
import com.auxdible.skrpg.player.guilds.GuildInviteManager;
import com.auxdible.skrpg.player.guilds.GuildManager;
import com.auxdible.skrpg.player.guilds.raid.RaidManager;
import com.auxdible.skrpg.regions.Region;
import com.auxdible.skrpg.regions.RegionManager;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.Text;
import com.mojang.datafixers.util.Pair;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SKRPG extends JavaPlugin {
    private ArrayList<Integer> renderedSpawns;
    private ArrayList<RegionSetup> playersInRegionSetup;
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

    public GuildManager guildManager;
    public GuildManager getGuildManager() { return guildManager; }

    public GuildInviteManager guildInviteManager;
    public GuildInviteManager getGuildInviteManager() { return guildInviteManager; }

    public RaidManager raidManager;
    public RaidManager getRaidManager() { return raidManager; }

    public ScrollBossManager scrollBossManager;
    public ScrollBossManager getScrollBossManager() { return scrollBossManager; }

    private String host, database, username, password;
    private int port;
    private static Connection connection;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Loading SKRPG core plugin... Runic Table Test");
        saveDefaultConfig();
        host = getConfig().getString("mySQL.host");
        database = getConfig().getString("mySQL.database");
        username = getConfig().getString("mySQL.username");
        password = getConfig().getString("mySQL.password");
        port = getConfig().getInt("mySQL.port");

        try {
            openConnection();
            getLogger().info("Connected to database.");
        } catch (SQLException x) {
            x.printStackTrace();
        }

        try {
            prepareStatement("CREATE TABLE IF NOT EXISTS stat_table(" +
                    "UUID varchar(36), baseHP int(11), baseDefence int(11), baseStrength int(11), " +
                    "baseEnergy int(11), baseSpeed int(11), credits int(11), interest varchar(36), canTrade tinyint(1), raritySell varchar(36), questsCompleted longtext, runicPoints int(11), PRIMARY KEY (UUID));").executeUpdate();
            prepareStatement("ALTER TABLE stat_table ADD COLUMN IF NOT EXISTS runicPoints INT(11) NOT NULL;").executeUpdate();
            prepareStatement("ALTER TABLE stat_table ADD COLUMN IF NOT EXISTS runicUpgrades LONGTEXT NOT NULL;").executeUpdate();
            prepareStatement("ALTER TABLE stat_table ADD COLUMN IF NOT EXISTS currentQuestState INT(11) NOT NULL;").executeUpdate();
            prepareStatement("ALTER TABLE stat_table ADD COLUMN IF NOT EXISTS currentQuest VARCHAR(36) NOT NULL;").executeUpdate();
            prepareStatement("CREATE TABLE IF NOT EXISTS skills_table(UUID varchar(36), miningLevel int(11), miningXpTill int(11), miningXpTotal int(11), herbalismLevel int(11), herbalismXpTill int(11), " +
                    "herbalismXpTotal int(11), craftingLevel int(11), craftingXpTill int(11), craftingXpTotal int(11), combatLevel int(11), combatXpTill int(11), combatXpTotal int(11), runicLevel int(11), runicXpTill int(11)," +
                    "runicXpTotal int(11), PRIMARY KEY (UUID));").executeUpdate();
            prepareStatement("ALTER TABLE skills_table ADD COLUMN IF NOT EXISTS runicLevel INT(11) NOT NULL;").executeUpdate();
            prepareStatement("ALTER TABLE skills_table ADD COLUMN IF NOT EXISTS runicXpTill INT(11) NOT NULL;").executeUpdate();
            prepareStatement("ALTER TABLE skills_table ADD COLUMN IF NOT EXISTS runicXpTotal INT(11) NOT NULL;").executeUpdate();
            prepareStatement("CREATE TABLE IF NOT EXISTS banks_table(bank1Level varchar(36), bank1Credits int(11), bank2Level varchar(36)," +
                    "bank2Credits int(11), bank3Level varchar(36), bank3Credits int(11), bank4Level varchar(36), bank4Credits int(11), bank5Level varchar(36)," +
                    "bank5Credits int(11), bankAmount int(11), UUID varchar(36), PRIMARY KEY (UUID));").executeUpdate();
            prepareStatement("CREATE TABLE IF NOT EXISTS collection_table(UUID varchar(36), collectionsTier longtext, collectionsAmount longtext, PRIMARY KEY (UUID));").executeUpdate();
            prepareStatement("CREATE TABLE IF NOT EXISTS guilds_table(ID int(11), NAME varchar(16), MEMBERS longtext, RANKS longtext, PERMISSIONS longtext, REGIONS longtext, PRIMARY KEY (ID));").executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        playerManager = new PlayerManager(this);
        mobManager = new MobManager(this);
        regionManager = new RegionManager(this);
        npcManager = new NPCManager(this);
        mobSpawnManager = new MobSpawnManager(this);
        tradeManager = new TradeManager(this);
        guildManager = new GuildManager(this);
        guildInviteManager = new GuildInviteManager(this);
        raidManager = new RaidManager(this);
        scrollBossManager = new ScrollBossManager(this);
        mobSpawnManager.enable();
        try {
            npcManager.enable();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        guildManager.enableGuilds();
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
        getCommand("trade").setExecutor(new TradeCommand(this));
        getCommand("spawnEntity").setExecutor(new SpawnEntityCommand(this));
        getCommand("collections").setExecutor(new CollectionsCommand(this));
        getCommand("settings").setExecutor(new SettingsCommand(this));
        getCommand("guild").setExecutor(new GuildCommand(this));
        getCommand("skrpg").setExecutor(new SKRPGCommand(this));
        getCommand("recipe").setExecutor(new RecipeCommand(this));
        getCommand("itemInfo").setExecutor(new ItemDataCommand(this));
        getLogger().info("Loaded SKRPG core plugin!");
        renderedSpawns = new ArrayList<>();
        playersInRegionSetup = new ArrayList<>();
        SKRPG skrpg = this;
        new BukkitRunnable() {

            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    PlayerData playerData = getPlayerManager().getPlayerData(player.getUniqueId());
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            Text.color(
                                    "&c" + playerData.getHp() + "/" + playerData.getMaxHP() + " ♥ " +
                                            "&a" + playerData.getDefence() + " ✿ Defence " +
                                            "&e" + playerData.getEnergy() + "/" + playerData.getMaxEnergy() + " ☢ Energy" +
                                            " &5" + playerData.getRunicPoints() + " RP ஐ"
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
                       if (locationIsInCuboid(player.getLocation(),
                               new Location(region.getSpawnLocation().getWorld(),
                                       Math.min(region.getX(), region.getX2()), 0, Math.min(region.getZ(), region.getZ2())),
                               new Location(region.getSpawnLocation().getWorld(), Math.max(region.getX(), region.getX2()), 256, Math.max(region.getZ(), region.getZ2())))) {
                            if (playerData.getRegion() != region) {
                                player.sendTitle(Text.color("&e&lYou are now entering"),
                                        Text.color(region.getName()), 20, 40, 20);
                            }
                            if (region.getControllingGuild() != null) {
                                player.getScoreboard().getTeam("regionOwner")
                                        .setSuffix(Text.color(region.getControllingGuild().getName()));
                            } else {
                                player.getScoreboard().getTeam("regionOwner")
                                        .setSuffix(Text.color("&8NONE"));
                            }
                            getPlayerManager().getPlayerData(player.getUniqueId()).setRegion(region);
                            regionFound = true;
                            break;
                        }
                    }

                    for (NPC npc : npcManager.getNpcs()) {
                        if (npc.getEntityPlayer() != null) {
                            if (player.getLocation().distance(npc.getLocation()) <= 80) {
                                if (!playerData.getRenderedNPCs().contains(npc)) {
                                    PlayerConnection playerConnection = ((CraftPlayer) player).getHandle().playerConnection;
                                    playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo
                                            .EnumPlayerInfoAction.ADD_PLAYER, npc.getEntityPlayer()));
                                    playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc.getEntityPlayer()));
                                    DataWatcher watcher = npc.getEntityPlayer().getDataWatcher();
                                    Integer byteInt = 127;
                                    watcher.set(new DataWatcherObject<>(16, DataWatcherRegistry.a), byteInt.byteValue());
                                    playerConnection.sendPacket(new PacketPlayOutEntityMetadata(npc.getEntityPlayer().getId(),
                                            npc.getEntityPlayer().getDataWatcher(), true));
                                    playerConnection.sendPacket(new PacketPlayOutEntityHeadRotation(npc.getEntityPlayer(), (byte) (
                                            npc.getLocation().getYaw() * 256 / 360)));
                                    if (npc.getItemInHand() != null) {
                                        List<Pair<EnumItemSlot, ItemStack>> itemList = new ArrayList<>();
                                        itemList.add(Pair.of(EnumItemSlot.MAINHAND, CraftItemStack.asNMSCopy(new ItemBuilder(npc.getItemInHand(), 1).asItem())));
                                        playerConnection.sendPacket(new PacketPlayOutEntityEquipment(npc.getEntityPlayer().getId(), itemList));
                                    }

                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc.getEntityPlayer()));
                                        }
                                    }.runTaskLater(skrpg,50);
                                    playerData.getRenderedNPCs().add(npc);
                                }
                            } else {
                                if (playerData.getRenderedNPCs().contains(npc)) {
                                    playerData.getRenderedNPCs().remove(npc);
                                }
                            }
                        }
                    }
                    for (MobSpawn mobSpawn : skrpg.getMobSpawnManager().getMobSpawns()) {
                        boolean spawnRendered = false;
                        for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                            if (onlinePlayers.getLocation().distance(mobSpawn.getLocation()) <= 80) {
                                spawnRendered = true;
                            }
                        }
                        if (!spawnRendered) {
                            if (renderedSpawns.contains(Integer.valueOf(mobSpawn.getId()))) {
                                renderedSpawns.remove(Integer.valueOf(mobSpawn.getId()));
                            }

                            for (Mob mob : mobSpawn.getCurrentlySpawnedMobs()) {
                                mob.getEnt().remove();
                            }
                            mobSpawn.getCurrentlySpawnedMobs().clear();
                        } else {
                            if (!renderedSpawns.contains(Integer.valueOf(mobSpawn.getId()))) {
                                renderedSpawns.add(Integer.valueOf(mobSpawn.getId()));
                            }
                        }
                    }
                    if (!regionFound) {
                            getPlayerManager().getPlayerData(player.getUniqueId()).setRegion(null);
                        player.getScoreboard().getTeam("regionOwner")
                                .setSuffix(Text.color("&8NONE"));
                    }
                    int strengthIncrease = 0;
                    int defenceIncrease = 0;
                    int energyIncrease = 0;
                    int hpIncrease = 0;
                    int speedIncrease = 0;
                    boolean wearingCrabHat = false;
                    ItemInfo iteminfoHand = ItemInfo.parseItemInfo(player.getInventory().getItemInMainHand());
                    ItemInfo itemInfoHelmet = ItemInfo.parseItemInfo(player.getInventory().getHelmet());
                    ItemInfo itemInfoChestplate = ItemInfo.parseItemInfo(player.getInventory().getChestplate());
                    ItemInfo itemInfoLeggings = ItemInfo.parseItemInfo(player.getInventory().getLeggings());
                    ItemInfo itemInfoBoots = ItemInfo.parseItemInfo(player.getInventory().getBoots());
                    if (iteminfoHand != null) {
                        if (iteminfoHand.getItem().getItemType() != ItemType.ARMOR) {
                            strengthIncrease = strengthIncrease + iteminfoHand.getItem().getStrength() + iteminfoHand.getBonusStrength();
                            defenceIncrease = defenceIncrease + iteminfoHand.getItem().getDefence() + iteminfoHand.getBonusDefence();
                            energyIncrease = energyIncrease + iteminfoHand.getItem().getEnergy() + iteminfoHand.getBonusEnergy();
                            hpIncrease = hpIncrease + iteminfoHand.getItem().getHp() + iteminfoHand.getBonusHealth();
                            speedIncrease = speedIncrease + iteminfoHand.getItem().getSpeed() + iteminfoHand.getBonusSpeed();
                        }
                    }
                    if (itemInfoHelmet != null) {
                        strengthIncrease = strengthIncrease + itemInfoHelmet.getItem().getStrength() + itemInfoHelmet.getBonusStrength();
                        defenceIncrease = defenceIncrease + itemInfoHelmet.getItem().getDefence() + itemInfoHelmet.getBonusDefence();
                        energyIncrease = energyIncrease + itemInfoHelmet.getItem().getEnergy() + itemInfoHelmet.getBonusEnergy();
                        hpIncrease = hpIncrease + itemInfoHelmet.getItem().getHp() + itemInfoHelmet.getBonusHealth();
                        speedIncrease = speedIncrease + itemInfoHelmet.getItem().getSpeed() + itemInfoHelmet.getBonusSpeed();
                        if (itemInfoHelmet.getItem() == Items.CRAB_CROWN) {
                            wearingCrabHat = true;
                        }
                        if (itemInfoHelmet.hasEnchantment(Enchantments.PROTECTION)) {
                            defenceIncrease = defenceIncrease + (itemInfoHelmet.getEnchantment(Enchantments.PROTECTION).getLevel() * 25);
                        } else if (itemInfoHelmet.hasEnchantment(Enchantments.HEALTHY)) {
                            hpIncrease = hpIncrease + (itemInfoHelmet.getEnchantment(Enchantments.HEALTHY).getLevel() * 25);
                        }
                    }
                    if (itemInfoChestplate != null) {
                            strengthIncrease = strengthIncrease + itemInfoChestplate.getItem().getStrength() + itemInfoChestplate.getBonusStrength();
                            defenceIncrease = defenceIncrease + itemInfoChestplate.getItem().getDefence() + itemInfoChestplate.getBonusDefence();
                            energyIncrease = energyIncrease + itemInfoChestplate.getItem().getEnergy() + itemInfoChestplate.getBonusEnergy();
                            hpIncrease = hpIncrease + itemInfoChestplate.getItem().getHp() + itemInfoChestplate.getBonusHealth();
                            speedIncrease = speedIncrease + itemInfoChestplate.getItem().getSpeed() + itemInfoChestplate.getBonusSpeed();
                        if (itemInfoChestplate.hasEnchantment(Enchantments.PROTECTION)) {
                            defenceIncrease = defenceIncrease + (itemInfoChestplate.getEnchantment(Enchantments.PROTECTION).getLevel() * 25);
                        } else if (itemInfoChestplate.hasEnchantment(Enchantments.HEALTHY)) {
                            hpIncrease = hpIncrease + (itemInfoChestplate.getEnchantment(Enchantments.HEALTHY).getLevel() * 25);
                        }
                    }
                    if (itemInfoLeggings != null) {
                            strengthIncrease = strengthIncrease + itemInfoLeggings.getItem().getStrength() + itemInfoLeggings.getBonusStrength();
                            defenceIncrease = defenceIncrease + itemInfoLeggings.getItem().getDefence() + itemInfoLeggings.getBonusDefence();
                            energyIncrease = energyIncrease + itemInfoLeggings.getItem().getEnergy() + itemInfoLeggings.getBonusEnergy();
                            hpIncrease = hpIncrease + itemInfoLeggings.getItem().getHp() + itemInfoLeggings.getBonusHealth();
                            speedIncrease = speedIncrease + itemInfoLeggings.getItem().getSpeed() + itemInfoLeggings.getBonusSpeed();
                        if (itemInfoLeggings.hasEnchantment(Enchantments.PROTECTION)) {
                            defenceIncrease = defenceIncrease + (itemInfoLeggings.getEnchantment(Enchantments.PROTECTION).getLevel() * 25);
                        } else if (itemInfoLeggings.hasEnchantment(Enchantments.HEALTHY)) {
                            hpIncrease = hpIncrease + (itemInfoLeggings.getEnchantment(Enchantments.HEALTHY).getLevel() * 25);
                        }
                    }
                    if (itemInfoBoots != null) {
                        strengthIncrease = strengthIncrease + itemInfoBoots.getItem().getStrength() + itemInfoBoots.getBonusStrength();
                        defenceIncrease = defenceIncrease + itemInfoBoots.getItem().getDefence() + itemInfoBoots.getBonusDefence();
                        energyIncrease = energyIncrease + itemInfoBoots.getItem().getEnergy() + itemInfoBoots.getBonusEnergy();
                        hpIncrease = hpIncrease + itemInfoBoots.getItem().getHp() + itemInfoBoots.getBonusHealth();
                        speedIncrease = speedIncrease + itemInfoBoots.getItem().getSpeed() + itemInfoBoots.getBonusSpeed();
                        if (itemInfoBoots.hasEnchantment(Enchantments.PROTECTION)) {
                            defenceIncrease = defenceIncrease + (itemInfoBoots.getEnchantment(Enchantments.PROTECTION).getLevel() * 25);
                        } else if (itemInfoBoots.hasEnchantment(Enchantments.HEALTHY)) {
                            hpIncrease = hpIncrease + (itemInfoBoots.getEnchantment(Enchantments.HEALTHY).getLevel() * 25);
                        }
                    }

                    playerData.setStrength(playerData.getBaseStrength() + strengthIncrease);
                    playerData.setDefence(playerData.getBaseDefence() + defenceIncrease);
                    playerData.setMaxEnergy(playerData.getBaseEnergy() + energyIncrease);
                    if (wearingCrabHat) {
                        playerData.setMaxHP(75);
                        if (playerData.getHp() > 75) {
                            playerData.setHp(75);
                        }
                    } else {
                        playerData.setMaxHP(playerData.getBaseHP() + hpIncrease);
                    }

                    playerData.setSpeed(playerData.getBaseSpeed() + speedIncrease);
                    if (playerData.getHp() > playerData.getMaxHP()) {
                        playerData.setHp(playerData.getMaxHP());
                    }
                    if (playerData.getEnergy() > playerData.getMaxEnergy()) {
                        playerData.setEnergy(playerData.getMaxEnergy());
                    }
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
        }.runTaskTimer(this, 0, 80);
        new BukkitRunnable() {
            @Override
            public void run() {
                for (int i = 0; i < skrpg.getMobSpawnManager().getMobSpawns().size(); i++) {
                    MobSpawn mobSpawn = skrpg.getMobSpawnManager().getMobSpawn(i);
                    if (mobSpawn.getCurrentlySpawnedMobs() == null || mobSpawn.getCurrentlySpawnedMobs().isEmpty()) {

                        ArrayList<Integer> mobId = new ArrayList<>();
                        mobId.add(mobSpawn.getId());
                        boolean check = renderedSpawns.containsAll(mobId);
                        if (check) {
                            MobType.buildMob(mobSpawn.getMob().getId(), skrpg, mobSpawn);
                        }

                    }
                }
            }
        }.runTaskTimer(this, 0, 300);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        playerManager.disableMySQL();
        regionManager.disable();
        npcManager.disable();
        mobSpawnManager.disable();
        guildManager.disableGuilds();
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.kickPlayer("The server is reloading!");
        }
        for (Entity entity : Bukkit.getWorld(getConfig().getString("rpgWorld")).getEntities()) {
            entity.remove();
        }
    }
    public static int levelToInt(String level) {
        return Integer.parseInt(level.replaceAll("_", ""));
    }
    private void openConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            return;
        }

        connection = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database,
                this.username, this.password);
    }
    public static PreparedStatement prepareStatement(String query) {
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(query);
        } catch (SQLException x) {
            x.printStackTrace();
        }
        return ps;
    }
    public boolean locationIsInCuboid(Location playerLocation, Location min, Location max) {
        boolean trueOrNot = false;
        if (playerLocation.getWorld() == min.getWorld() && playerLocation.getWorld() == max.getWorld()) {
            if (playerLocation.getX() >= min.getX() && playerLocation.getX() <= max.getX()) {
                if (playerLocation.getY() >= min.getY() && playerLocation.getY() <= max.getY()) {
                    if (playerLocation.getZ() >= min.getZ()
                            && playerLocation.getZ() <= max.getZ()) {
                        trueOrNot = true;
                    }
                }
            }
            if (playerLocation.getX() <= min.getX() && playerLocation.getX() >= max.getX()) {
                if (playerLocation.getY() <= min.getY() && playerLocation.getY() >= max.getY()) {
                    if (playerLocation.getZ() <= min.getZ()
                            && playerLocation.getZ() >= max.getZ()) {
                        trueOrNot = true;
                    }
                }
            }
        }
        return trueOrNot;
    }
    public ArrayList<RegionSetup> getPlayersInRegionSetup() { return playersInRegionSetup; }
    public RegionSetup getSetup(Player p) {
        for (RegionSetup regionSetup : getPlayersInRegionSetup()) {
            if (regionSetup.getSetupPlayer().getUniqueId() == p.getUniqueId()) { return regionSetup; }
        }
        return null;
    }
}
