package com.auxdible.skrpg;

import com.auxdible.skrpg.commands.*;
import com.auxdible.skrpg.commands.admin.*;
import com.auxdible.skrpg.commands.inventory.*;
import com.auxdible.skrpg.items.*;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.items.enchantments.Enchantments;
import com.auxdible.skrpg.items.forage.ForageLocation;
import com.auxdible.skrpg.items.forage.ForageManager;
import com.auxdible.skrpg.locations.portals.Portal;
import com.auxdible.skrpg.locations.portals.PortalManager;
import com.auxdible.skrpg.mobs.*;
import com.auxdible.skrpg.mobs.boss.scrollboss.ScrollBossManager;
import com.auxdible.skrpg.npcs.NPC;
import com.auxdible.skrpg.npcs.NPCManager;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.player.PlayerJoinLeaveListener;
import com.auxdible.skrpg.player.PlayerListener;
import com.auxdible.skrpg.player.PlayerManager;
import com.auxdible.skrpg.player.actions.BrokenBlock;
import com.auxdible.skrpg.player.economy.TradeManager;
import com.auxdible.skrpg.player.effects.Effects;
import com.auxdible.skrpg.player.guilds.GuildInviteManager;
import com.auxdible.skrpg.player.guilds.GuildManager;
import com.auxdible.skrpg.player.guilds.raid.RaidManager;
import com.auxdible.skrpg.locations.regions.Region;
import com.auxdible.skrpg.locations.regions.RegionFlags;
import com.auxdible.skrpg.locations.regions.RegionManager;
import com.auxdible.skrpg.locations.regions.SKQuestSoundtrack;
import com.auxdible.skrpg.locations.locations.LocationManager;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.Text;
import com.mojang.datafixers.util.Pair;
import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.*;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.IOUtils;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SKRPG extends JavaPlugin {
    private HashMap<BrokenBlock, Integer> brokenBlocks;
    public HashMap<BrokenBlock, Integer> getBrokenBlocks() { return brokenBlocks; }

    private ArrayList<Integer> renderedSpawns;
    private ArrayList<RegionSetup> playersInRegionSetup;

    private HashMap<Player, Integer> cooldownsAbility;
    public HashMap<Player, Integer> getCooldownsAbility() { return cooldownsAbility; }

    private HashMap<Player, Integer> npcCooldown;
    public HashMap<Player, Integer> getNpcCooldown() { return npcCooldown; }

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

    public LocationManager locationManager;
    public LocationManager getLocationManager() { return locationManager; }

    public ForageManager forageManager;
    public ForageManager getForageManager() { return forageManager; }

    public PortalManager portalManager;
    public PortalManager getPortalManager() { return portalManager; }

    private String host, database, username, password;
    private int port;
    private static Connection connection;

    private HashMap<Player, RadioSongPlayer> skrpgSongPlayer = new HashMap<>();

    private SKQuestSoundtrack skQuestSoundtrack;

    public SKQuestSoundtrack getSkQuestSoundtrack() { return skQuestSoundtrack; }

    public ArrayList<PortalSetup> playersInPortalSetup;

    public String version;

    public String getVersion() { return version; }

    public HashMap<Player, Integer> hitCooldown;

    public HashMap<Player, Integer> getHitCooldown() { return hitCooldown; }

    @Override
    public void onEnable() {
        // Plugin startup logic
        boolean NoteBlockAPI = true;
        if (!Bukkit.getPluginManager().isPluginEnabled("NoteBlockAPI")){
            getLogger().severe("SKQuest requires NoteBlockAPI to run the soundtrack for the game.");
            NoteBlockAPI = false;
            return;
        }
        version = "DEVELOPMENT_SNAPSHOT";
        getLogger().info("Loading SKQuest core plugin... v" + getVersion());
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
        if (!new File(getDataFolder() + "/music/willowofthewoods.nbs").exists()) {
            boolean mkdir = new File(getDataFolder() + "/music/").mkdirs();
        }
        try {
            InputStream inputStream = new URL("https://drive.google.com/uc?export=download&id=144ItrIq_upWPepA0wPYRIG_zfuDniQxe").openStream();
            FileOutputStream fileOS = new FileOutputStream(getDataFolder() + "/music/willowofthewoods.nbs");
            int i = IOUtils.copy(inputStream, fileOS);
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            prepareStatement("CREATE TABLE IF NOT EXISTS stat_table(" +
                    "UUID varchar(36), baseHP int(11), baseDefence int(11), baseStrength int(11), " +
                    "baseEnergy int(11), baseSpeed int(11), credits int(11), interest varchar(36), canTrade tinyint(1), raritySell varchar(36), questsCompleted longtext, runicPoints int(11), runicUpgrades longtext, currentQuestState longtext, currentQuest longtext, royaltyQuestSlots int(5), royaltyPoints int(11), royaltyUpgrades longtext, canDrop tinyint(1), PRIMARY KEY (UUID));").executeUpdate();
            prepareStatement("ALTER TABLE stat_table ADD COLUMN IF NOT EXISTS royaltyPoints INT(11) NOT NULL AFTER royaltyQuestSlots;").executeUpdate();
            prepareStatement("ALTER TABLE stat_table ADD COLUMN IF NOT EXISTS royaltyUpgrades LONGTEXT NOT NULL AFTER royaltyPoints;").executeUpdate();
            prepareStatement("ALTER TABLE stat_table ADD COLUMN IF NOT EXISTS inventoryData LONGTEXT NOT NULL AFTER royaltyUpgrades;").executeUpdate();
            prepareStatement("ALTER TABLE stat_table ADD COLUMN IF NOT EXISTS location LONGTEXT NOT NULL AFTER inventoryData;").executeUpdate();
            prepareStatement("ALTER TABLE stat_table MODIFY currentQuest LONGTEXT;").executeUpdate();
            prepareStatement("ALTER TABLE stat_table MODIFY currentQuestState LONGTEXT;").executeUpdate();
            prepareStatement("ALTER TABLE stat_table ADD COLUMN IF NOT EXISTS canDrop TINYINT(1) NOT NULL AFTER royaltyUpgrades;").executeUpdate();
            prepareStatement("CREATE TABLE IF NOT EXISTS skills_table(UUID varchar(36), miningLevel int(11), miningXpTill int(11), miningXpTotal int(11), herbalismLevel int(11), herbalismXpTill int(11), " +
                    "herbalismXpTotal int(11), craftingLevel int(11), craftingXpTill int(11), craftingXpTotal int(11), combatLevel int(11), combatXpTill int(11), combatXpTotal int(11), runicLevel int(11), runicXpTill int(11)," +
                    "runicXpTotal int(11), PRIMARY KEY (UUID));").executeUpdate();
            prepareStatement("ALTER TABLE skills_table ADD COLUMN IF NOT EXISTS fishingLevel INT(11) NOT NULL AFTER runicXpTotal;").executeUpdate();
            prepareStatement("ALTER TABLE skills_table ADD COLUMN IF NOT EXISTS fishingXpTill INT(11) NOT NULL AFTER fishingLevel;").executeUpdate();
            prepareStatement("ALTER TABLE skills_table ADD COLUMN IF NOT EXISTS fishingXpTotal INT(11) NOT NULL AFTER fishingXpTill;").executeUpdate();
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
        locationManager = new LocationManager(this);
        forageManager = new ForageManager(this);
        skQuestSoundtrack = new SKQuestSoundtrack(this);
        portalManager = new PortalManager(this);
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
        locationManager.enable();
        forageManager.enable();
        portalManager.enable();
        Bukkit.getPluginManager().registerEvents(new PlayerJoinLeaveListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
        Bukkit.getWorld(getConfig().getString("rpgWorld")).setGameRule(GameRule.NATURAL_REGENERATION, false);
        Bukkit.getWorld(getConfig().getString("rpgWorld")).setGameRule(GameRule.DO_MOB_SPAWNING, false);
        getCommand("giveitem").setExecutor(new GiveItemCommand(this));
        getCommand("menu").setExecutor(new MenuCommand(this));
        getCommand("region").setExecutor(new RegionCommand(this));
        getCommand("ct").setExecutor(new CraftingTableCommand(this));
        getCommand("stats").setExecutor(new StatsCommand(this));
        getCommand("skills").setExecutor(new SkillsCommand(this));
        getCommand("npc").setExecutor(new NPCCommand(this));
        getCommand("mobspawn").setExecutor(new MobSpawnCommand(this));
        getCommand("addCredits").setExecutor(new AddCreditsCommand(this));
        getCommand("trade").setExecutor(new TradeCommand(this));
        getCommand("spawnEntity").setExecutor(new SpawnEntityCommand(this));
        getCommand("accumulations").setExecutor(new CollectionsCommand(this));
        getCommand("settings").setExecutor(new SettingsCommand(this));
        getCommand("guild").setExecutor(new GuildCommand(this));
        getCommand("skquest").setExecutor(new SKRPGCommand(this));
        getCommand("recipe").setExecutor(new RecipeCommand(this));
        getCommand("itemInfo").setExecutor(new ItemDataCommand(this));
        getCommand("stash").setExecutor(new StashCommand(this));
        getCommand("forage").setExecutor(new CreateForageCommand(this));
        getCommand("quests").setExecutor(new QuestCommand(this));
        getCommand("portal").setExecutor(new PortalCommand(this));
        getLogger().info("Loaded SKQuest core plugin!");
        renderedSpawns = new ArrayList<>();
        playersInRegionSetup = new ArrayList<>();
        npcCooldown = new HashMap<>();
        cooldownsAbility = new HashMap<>();
        brokenBlocks = new HashMap<>();
        skrpgSongPlayer = new HashMap<>();
        playersInPortalSetup = new ArrayList<>();
        hitCooldown = new HashMap<>();
        SKRPG skrpg = this;
        new BukkitRunnable() {
            int oneSecond = 0;
            int fourSeconds = 0;
            int twentySeconds = 0;
            int fiveMinutes = 0;
            @Override
            public void run() {
                // per 1 second
                for (Player player : hitCooldown.keySet()) {
                    if ((hitCooldown.get(player) - 1) > 0) {
                        hitCooldown.put(player, hitCooldown.get(player) - 1);
                    } else {
                        hitCooldown.remove(player);
                    }
                }
                if (oneSecond == 20) {
                    oneSecond = 0;
                    if (!brokenBlocks.isEmpty()) {
                        List<BrokenBlock> removedBlocks = new ArrayList<>();
                        for (BrokenBlock brokenBlock : brokenBlocks.keySet()) {
                            brokenBlocks.put(brokenBlock, brokenBlocks.get(brokenBlock) - 1);
                            if (brokenBlocks.get(brokenBlock) <= 0) {
                                removedBlocks.add(brokenBlock);
                                brokenBlock.create();
                            }
                        }
                        for (BrokenBlock brokenBlock : removedBlocks) {
                            brokenBlocks.remove(brokenBlock);
                        }
                    }

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        PlayerData playerData = getPlayerManager().getPlayerData(player.getUniqueId());
                        if (playerData == null) {
                            player.kickPlayer(Text.color("&cERROR: There was no account found under this UUID! Please contact an admin if this is an error. "));
                            getPlayerManager().createPlayer(player.getUniqueId());
                        } else {
                            if (player.getScoreboard().getTeam("regionOwner") == null) {
                                PlayerJoinLeaveListener.buildScoreboard(player, skrpg);
                            } else {
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                                        Text.color(
                                                "&c" + playerData.getHp() + "/" + playerData.getMaxHP() + " ♥ &8&l| " +
                                                        "&a" + playerData.getDefence() + " ✿ Defense &8&l|" +
                                                        " &e" + playerData.getEnergy() + "/" + playerData.getMaxEnergy() + " ☢ Energy &8&l|" +
                                                        " &5" + playerData.getRunicPoints() + " RP ஐ"
                                        )
                                ));
                                playerData.updateEffects();
                            }
                        }
                    }
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player != null) {}
                        if (player.getScoreboard().getTeam("regionOwner") == null) {
                            PlayerJoinLeaveListener.buildScoreboard(player, skrpg);
                        } else {
                            PlayerData playerData = getPlayerManager().getPlayerData(player.getUniqueId());
                            if (playerData.getHp() <= 0) {
                                if (getPlayerManager().getPlayerData(player.getUniqueId()).getHp() <= 0) {
                                    playerData.getPlayerActionManager().killPlayer();
                                }
                            }
                            boolean regionFound = false;
                            for (Region region : getRegionManager().getRegions()) {
                                if (locationIsInCuboid(player.getLocation(),
                                        new Location(region.getSpawnLocation().getWorld(),
                                                Math.min(region.getX(), region.getX2()), 0, Math.min(region.getZ(), region.getZ2())),
                                        new Location(region.getSpawnLocation().getWorld(), Math.max(region.getX(), region.getX2()), 256, Math.max(region.getZ(), region.getZ2())))) {
                                    if (playerData.getRegion() != region) {
                                        if (region.getRegionFlagsList().contains(RegionFlags.PLAY_THICKETS_MUSIC)) {
                                            RadioSongPlayer rsp = new RadioSongPlayer(skQuestSoundtrack.getWillowOfTheWoodsSong());
                                            rsp.addPlayer(player);
                                            rsp.setPlaying(true);
                                            rsp.setRepeatMode(RepeatMode.ALL);
                                            skrpgSongPlayer.put(player, rsp);
                                            Text.applyText(player, "&7Now Playing: &aWillow of The Woods - Auxdible (Looped)");
                                        } else {
                                           if (skrpgSongPlayer.containsKey(player)) {
                                               skrpgSongPlayer.get(player).setPlaying(false);
                                               skrpgSongPlayer.remove(player);
                                           }
                                        }
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
                            for (Portal portal : portalManager.getPortals()) {
                                if (locationIsInCuboid(player.getLocation(), portal.getBound1(),
                                        portal.getBound2())) {
                                    player.teleport(portal.getTeleportLocation());
                                }
                            }
                            if (playerData.getRenderedNPCs() != null) {
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
                                                player.getScoreboard().getTeam("npcs").addEntry(npc.getEntityPlayer().getName());
                                                new BukkitRunnable() {
                                                    @Override
                                                    public void run() {
                                                        playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc.getEntityPlayer()));
                                                    }
                                                }.runTaskLater(skrpg, 50);
                                                playerData.getRenderedNPCs().add(npc);
                                            }
                                        } else {
                                            if (playerData.getRenderedNPCs() == null) {
                                                playerData.setRenderedNPCs(new ArrayList<>());
                                            }
                                            if (playerData.getRenderedNPCs().contains(npc)) {
                                                playerData.getRenderedNPCs().remove(npc);
                                                if (npc.getEntityPlayer() != null) {
                                                    PlayerConnection playerConnection = ((CraftPlayer) player).getHandle().playerConnection;
                                                    playerConnection.sendPacket(new PacketPlayOutPlayerInfo(
                                                            PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc.getEntityPlayer()));
                                                    playerConnection.sendPacket(new PacketPlayOutEntityDestroy(npc.getEntityPlayer().getId()));
                                                    playerData.getRenderedNPCs().remove(npc);
                                                }
                                            }

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
                                if (skrpgSongPlayer.containsKey(player)) {
                                    skrpgSongPlayer.get(player).setPlaying(false);
                                    skrpgSongPlayer.remove(player);
                                }
                            }
                            int strengthIncrease = 0;
                            int defenceIncrease = 0;
                            int energyIncrease = 0;
                            int hpIncrease = 0;
                            int speedIncrease = 0;
                            int marineCreatureIncrease = 0;
                            boolean wearingCrabHat = false;
                            SKRPGItemStack iteminfoHand = new SKRPGItemStack(ItemInfo.parseItemInfo(player.getInventory().getItemInMainHand()), player.getInventory().getItemInMainHand().getAmount());
                            SKRPGItemStack itemInfoHelmet = playerData.getPlayerInventory().getInventoryEquipment().getHelmet();
                            SKRPGItemStack itemInfoChestplate = playerData.getPlayerInventory().getInventoryEquipment().getChestplate();
                            SKRPGItemStack itemInfoLeggings = playerData.getPlayerInventory().getInventoryEquipment().getLeggings();
                            SKRPGItemStack itemInfoBoots = playerData.getPlayerInventory().getInventoryEquipment().getBoots();
                            SKRPGItemStack itemInfoRing = playerData.getPlayerInventory().getInventoryEquipment().getRing();
                            SKRPGItemStack itemInfoHeadband = playerData.getPlayerInventory().getInventoryEquipment().getHeadband();
                            SKRPGItemStack itemInfoNecklace = playerData.getPlayerInventory().getInventoryEquipment().getNecklace();
                            SKRPGItemStack itemInfoArtifact = playerData.getPlayerInventory().getInventoryEquipment().getArtifact();

                            if (iteminfoHand.getItemInfo() != null) {
                                if (iteminfoHand.getItemInfo().getItem().getItemType().getItemCatagory() == ItemCatagory.WEAPON ||
                                        iteminfoHand.getItemInfo().getItem().getItemType().getItemCatagory() == ItemCatagory.BOW) {
                                    if (iteminfoHand.getItemInfo().getItem().getAttackSpeed().getHasteAmount() != 0) {
                                        if (player.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
                                            player.removePotionEffect(PotionEffectType.FAST_DIGGING);
                                        }
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 999999, iteminfoHand.getItemInfo().getItem().getAttackSpeed().getHasteAmount()));
                                    } else {
                                        if (player.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
                                            player.removePotionEffect(PotionEffectType.FAST_DIGGING);
                                        }
                                    }
                                    if (iteminfoHand.getItemInfo().getItem().getAttackSpeed().getMiningFatigueAmount() != 0) {
                                        if (player.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) {
                                            player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
                                        }
                                        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 999999, iteminfoHand.getItemInfo().getItem().getAttackSpeed().getMiningFatigueAmount()));
                                    } else {
                                        if (player.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) {
                                            player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
                                        }
                                    }
                                        strengthIncrease = strengthIncrease + iteminfoHand.getItemInfo().getItem().getStrength() + iteminfoHand.getItemInfo().getBonusStrength();
                                        defenceIncrease = defenceIncrease + iteminfoHand.getItemInfo().getItem().getDefence() + iteminfoHand.getItemInfo().getBonusDefence();
                                        energyIncrease = energyIncrease + iteminfoHand.getItemInfo().getItem().getEnergy() + iteminfoHand.getItemInfo().getBonusEnergy();
                                        hpIncrease = hpIncrease + iteminfoHand.getItemInfo().getItem().getHp() + iteminfoHand.getItemInfo().getBonusHealth();
                                        speedIncrease = speedIncrease + iteminfoHand.getItemInfo().getItem().getSpeed() + iteminfoHand.getItemInfo().getBonusSpeed();
                                        marineCreatureIncrease = marineCreatureIncrease + iteminfoHand.getItemInfo().getItem().getMarineLifeCatchChance();
                                } else {
                                    if (player.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) {
                                        player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
                                    }
                                    if (player.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
                                        player.removePotionEffect(PotionEffectType.FAST_DIGGING);
                                    }
                                }
                            } else {
                                if (player.hasPotionEffect(PotionEffectType.SLOW_DIGGING)) {
                                    player.removePotionEffect(PotionEffectType.SLOW_DIGGING);
                                }
                                if (player.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
                                    player.removePotionEffect(PotionEffectType.FAST_DIGGING);
                                }
                            }
                            if (itemInfoHelmet != null) {
                                strengthIncrease = strengthIncrease + itemInfoHelmet.getItemInfo().getItem().getStrength() + itemInfoHelmet.getItemInfo().getBonusStrength();
                                defenceIncrease = defenceIncrease + itemInfoHelmet.getItemInfo().getItem().getDefence() + itemInfoHelmet.getItemInfo().getBonusDefence();
                                energyIncrease = energyIncrease + itemInfoHelmet.getItemInfo().getItem().getEnergy() + itemInfoHelmet.getItemInfo().getBonusEnergy();
                                hpIncrease = hpIncrease + itemInfoHelmet.getItemInfo().getItem().getHp() + itemInfoHelmet.getItemInfo().getBonusHealth();
                                speedIncrease = speedIncrease + itemInfoHelmet.getItemInfo().getItem().getSpeed() + itemInfoHelmet.getItemInfo().getBonusSpeed();
                                marineCreatureIncrease = marineCreatureIncrease + itemInfoHelmet.getItemInfo().getItem().getMarineLifeCatchChance();
                                if (itemInfoHelmet.getItemInfo().getItem() == Items.CRAB_CROWN) {
                                    wearingCrabHat = true;
                                }
                                if (itemInfoHelmet.getItemInfo().hasEnchantment(Enchantments.PROTECTION)) {
                                    defenceIncrease = defenceIncrease + (itemInfoHelmet.getItemInfo().getEnchantment(Enchantments.PROTECTION).getLevel() * 25);
                                } else if (itemInfoHelmet.getItemInfo().hasEnchantment(Enchantments.HEALTHY)) {
                                    hpIncrease = hpIncrease + (itemInfoHelmet.getItemInfo().getEnchantment(Enchantments.HEALTHY).getLevel() * 25);
                                }
                            }
                            if (itemInfoChestplate != null) {
                                strengthIncrease = strengthIncrease + itemInfoChestplate.getItemInfo().getItem().getStrength() + itemInfoChestplate.getItemInfo().getBonusStrength();
                                defenceIncrease = defenceIncrease + itemInfoChestplate.getItemInfo().getItem().getDefence() + itemInfoChestplate.getItemInfo().getBonusDefence();
                                energyIncrease = energyIncrease + itemInfoChestplate.getItemInfo().getItem().getEnergy() + itemInfoChestplate.getItemInfo().getBonusEnergy();
                                hpIncrease = hpIncrease + itemInfoChestplate.getItemInfo().getItem().getHp() + itemInfoChestplate.getItemInfo().getBonusHealth();
                                speedIncrease = speedIncrease + itemInfoChestplate.getItemInfo().getItem().getSpeed() + itemInfoChestplate.getItemInfo().getBonusSpeed();
                                marineCreatureIncrease = marineCreatureIncrease + itemInfoChestplate.getItemInfo().getItem().getMarineLifeCatchChance();
                                if (itemInfoChestplate.getItemInfo().hasEnchantment(Enchantments.PROTECTION)) {
                                    defenceIncrease = defenceIncrease + (itemInfoChestplate.getItemInfo().getEnchantment(Enchantments.PROTECTION).getLevel() * 25);
                                } else if (itemInfoChestplate.getItemInfo().hasEnchantment(Enchantments.HEALTHY)) {
                                    hpIncrease = hpIncrease + (itemInfoChestplate.getItemInfo().getEnchantment(Enchantments.HEALTHY).getLevel() * 25);
                                }
                            }
                            if (itemInfoLeggings != null) {
                                strengthIncrease = strengthIncrease + itemInfoLeggings.getItemInfo().getItem().getStrength() + itemInfoLeggings.getItemInfo().getBonusStrength();
                                defenceIncrease = defenceIncrease + itemInfoLeggings.getItemInfo().getItem().getDefence() + itemInfoLeggings.getItemInfo().getBonusDefence();
                                energyIncrease = energyIncrease + itemInfoLeggings.getItemInfo().getItem().getEnergy() + itemInfoLeggings.getItemInfo().getBonusEnergy();
                                hpIncrease = hpIncrease + itemInfoLeggings.getItemInfo().getItem().getHp() + itemInfoLeggings.getItemInfo().getBonusHealth();
                                speedIncrease = speedIncrease + itemInfoLeggings.getItemInfo().getItem().getSpeed() + itemInfoLeggings.getItemInfo().getBonusSpeed();
                                if (itemInfoLeggings.getItemInfo().hasEnchantment(Enchantments.PROTECTION)) {
                                    defenceIncrease = defenceIncrease + (itemInfoLeggings.getItemInfo().getEnchantment(Enchantments.PROTECTION).getLevel() * 25);
                                } else if (itemInfoLeggings.getItemInfo().hasEnchantment(Enchantments.HEALTHY)) {
                                    hpIncrease = hpIncrease + (itemInfoLeggings.getItemInfo().getEnchantment(Enchantments.HEALTHY).getLevel() * 25);
                                }
                            }
                            if (itemInfoBoots != null) {
                                strengthIncrease = strengthIncrease + itemInfoBoots.getItemInfo().getItem().getStrength() + itemInfoBoots.getItemInfo().getBonusStrength();
                                defenceIncrease = defenceIncrease + itemInfoBoots.getItemInfo().getItem().getDefence() + itemInfoBoots.getItemInfo().getBonusDefence();
                                energyIncrease = energyIncrease + itemInfoBoots.getItemInfo().getItem().getEnergy() + itemInfoBoots.getItemInfo().getBonusEnergy();
                                hpIncrease = hpIncrease + itemInfoBoots.getItemInfo().getItem().getHp() + itemInfoBoots.getItemInfo().getBonusHealth();
                                speedIncrease = speedIncrease + itemInfoBoots.getItemInfo().getItem().getSpeed() + itemInfoBoots.getItemInfo().getBonusSpeed();
                                marineCreatureIncrease = marineCreatureIncrease + itemInfoBoots.getItemInfo().getItem().getMarineLifeCatchChance();
                                if (itemInfoBoots.getItemInfo().hasEnchantment(Enchantments.PROTECTION)) {
                                    defenceIncrease = defenceIncrease + (itemInfoBoots.getItemInfo().getEnchantment(Enchantments.PROTECTION).getLevel() * 25);
                                } else if (itemInfoBoots.getItemInfo().hasEnchantment(Enchantments.HEALTHY)) {
                                    hpIncrease = hpIncrease + (itemInfoBoots.getItemInfo().getEnchantment(Enchantments.HEALTHY).getLevel() * 25);
                                }
                                if (itemInfoBoots.getItemInfo().getItem() == Items.SPIDER_BOOTS) {
                                    player.setAllowFlight(true);

                                } else {
                                    if (player.getGameMode() != GameMode.CREATIVE && !skrpg.getRaidManager().isInRaid(player)) {
                                        player.setAllowFlight(false);
                                        player.setFlying(false);
                                    }
                                }
                            } else {
                                if (player.getGameMode() != GameMode.CREATIVE && !skrpg.getRaidManager().isInRaid(player)) {
                                    player.setAllowFlight(false);
                                    player.setFlying(false);
                                }
                            }
                            if (itemInfoRing != null) {
                                strengthIncrease = strengthIncrease + itemInfoRing.getItemInfo().getItem().getStrength() + itemInfoRing.getItemInfo().getBonusStrength();
                                defenceIncrease = defenceIncrease + itemInfoRing.getItemInfo().getItem().getDefence() + itemInfoRing.getItemInfo().getBonusDefence();
                                energyIncrease = energyIncrease + itemInfoRing.getItemInfo().getItem().getEnergy() + itemInfoRing.getItemInfo().getBonusEnergy();
                                hpIncrease = hpIncrease + itemInfoRing.getItemInfo().getItem().getHp() + itemInfoRing.getItemInfo().getBonusHealth();
                                speedIncrease = speedIncrease + itemInfoRing.getItemInfo().getItem().getSpeed() + itemInfoRing.getItemInfo().getBonusSpeed();
                            }
                            if (itemInfoHeadband != null) {
                                strengthIncrease = strengthIncrease + itemInfoHeadband.getItemInfo().getItem().getStrength() + itemInfoHeadband.getItemInfo().getBonusStrength();
                                defenceIncrease = defenceIncrease + itemInfoHeadband.getItemInfo().getItem().getDefence() + itemInfoHeadband.getItemInfo().getBonusDefence();
                                energyIncrease = energyIncrease + itemInfoHeadband.getItemInfo().getItem().getEnergy() + itemInfoHeadband.getItemInfo().getBonusEnergy();
                                hpIncrease = hpIncrease + itemInfoHeadband.getItemInfo().getItem().getHp() + itemInfoHeadband.getItemInfo().getBonusHealth();
                                speedIncrease = speedIncrease + itemInfoHeadband.getItemInfo().getItem().getSpeed() + itemInfoHeadband.getItemInfo().getBonusSpeed();
                            }
                            if (itemInfoNecklace != null) {
                                strengthIncrease = strengthIncrease + itemInfoNecklace.getItemInfo().getItem().getStrength() + itemInfoNecklace.getItemInfo().getBonusStrength();
                                defenceIncrease = defenceIncrease + itemInfoNecklace.getItemInfo().getItem().getDefence() + itemInfoNecklace.getItemInfo().getBonusDefence();
                                energyIncrease = energyIncrease + itemInfoNecklace.getItemInfo().getItem().getEnergy() + itemInfoNecklace.getItemInfo().getBonusEnergy();
                                hpIncrease = hpIncrease + itemInfoNecklace.getItemInfo().getItem().getHp() + itemInfoNecklace.getItemInfo().getBonusHealth();
                                speedIncrease = speedIncrease + itemInfoNecklace.getItemInfo().getItem().getSpeed() + itemInfoNecklace.getItemInfo().getBonusSpeed();
                            }
                            if (itemInfoArtifact != null) {
                                strengthIncrease = strengthIncrease + itemInfoArtifact.getItemInfo().getItem().getStrength() + itemInfoArtifact.getItemInfo().getBonusStrength();
                                defenceIncrease = defenceIncrease + itemInfoArtifact.getItemInfo().getItem().getDefence() + itemInfoArtifact.getItemInfo().getBonusDefence();
                                energyIncrease = energyIncrease + itemInfoArtifact.getItemInfo().getItem().getEnergy() + itemInfoArtifact.getItemInfo().getBonusEnergy();
                                hpIncrease = hpIncrease + itemInfoArtifact.getItemInfo().getItem().getHp() + itemInfoArtifact.getItemInfo().getBonusHealth();
                                speedIncrease = speedIncrease + itemInfoArtifact.getItemInfo().getItem().getSpeed() + itemInfoArtifact.getItemInfo().getBonusSpeed();
                            }
                            if (playerData.getEffect(Effects.STRENGTH) != null) {
                                strengthIncrease = strengthIncrease + ((20 + (playerData.getStrength() / 10)) *
                                        playerData.getEffect(Effects.STRENGTH).getLevel());
                            }
                            if (playerData.getEffect(Effects.PLATED) != null) {
                                defenceIncrease = defenceIncrease + ((20 + (playerData.getDefence() / 10)) *
                                        playerData.getEffect(Effects.PLATED).getLevel());
                            }
                            if (playerData.getEffect(Effects.ENERGETIC) != null) {
                                energyIncrease = energyIncrease + ((20 + (playerData.getEnergy() / 10)) *
                                        playerData.getEffect(Effects.ENERGETIC).getLevel());
                            }
                            if (playerData.getEffect(Effects.SPEED) != null) {
                                speedIncrease = speedIncrease + (20 *
                                        playerData.getEffect(Effects.SPEED).getLevel());
                            }
                            List<Entity> entitiesNearby = player.getNearbyEntities(5, 10, 5);
                            for (Entity entity : entitiesNearby) {
                                if (entity instanceof Player) {
                                    Player p = (Player) entity;
                                    if (p.getUniqueId() != player.getUniqueId()) {
                                        ItemInfo itemInfo = playerData.getPlayerInventory().getItemInMainHand().getItemInfo();
                                        if (itemInfo != null) {
                                            if (itemInfo.getItem() == Items.TANK_PLATE) {
                                                p.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, p.getLocation(), 5);
                                                defenceIncrease = defenceIncrease + 250;
                                            }
                                        }
                                    }
                                }
                            }
                            playerData.setMarineLifeCatchChance(marineCreatureIncrease);
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
                    for (Player players : cooldownsAbility.keySet()) {
                        cooldownsAbility.put(players, cooldownsAbility.get(players) - 1);
                        if (cooldownsAbility.get(players) <= 0) {
                            cooldownsAbility.remove(players);
                        }
                    }
                    for (Player players : npcCooldown.keySet()) {
                        npcCooldown.put(players, npcCooldown.get(players) - 1);
                        if (npcCooldown.get(players) <= 0) {
                            npcCooldown.remove(players);
                        }
                    }
                }
                if (fourSeconds == 80) {
                    fourSeconds = 0;
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        PlayerData playerData = getPlayerManager().getPlayerData(player.getUniqueId());
                        if (playerData == null) {
                            player.kickPlayer("&cERROR: There was no account found under this UUID! Please contact an admin if this is an error. ");
                            getPlayerManager().createPlayer(player.getUniqueId());
                        } else {
                            if (playerData.getPlayerInventory().getItemInMainHand() != null) {
                                ItemInfo iteminfoHand = playerData.getPlayerInventory().getItemInMainHand().getItemInfo();
                                if (iteminfoHand != null) {
                                    if (iteminfoHand.getItem().equals(Items.HEALERS_BOOK)) {
                                        List<Entity> entitiesNearby = player.getNearbyEntities(5, 10, 5);
                                        for (Entity entity : entitiesNearby) {
                                            if (entity instanceof Player) {
                                                Player p = (Player) entity;
                                                if (p.getUniqueId() != player.getUniqueId()) {
                                                    p.getWorld().spawnParticle(Particle.HEART, p.getEyeLocation(), 5);
                                                    PlayerData playerData1 = skrpg.getPlayerManager().getPlayerData(p.getUniqueId());
                                                    playerData1.setHp(playerData1.getHp() + (playerData1.getHp() / 10));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (playerData.getHp() < playerData.getMaxHP()) {
                                if ((int) Math.round(playerData.getMaxHP() * 0.1) > playerData.getMaxHP()) {
                                    playerData.setHp(playerData.getMaxHP());
                                } else {
                                    if (playerData.getEffect(Effects.REGENERATION) != null) {
                                        playerData.setHp(playerData.getHp() +
                                                (int) (Math.round(playerData.getMaxHP() * 0.1) * playerData.getEffect(Effects.REGENERATION).getLevel()));
                                    }
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
                    npcCooldown.clear();
                }
                if (twentySeconds == 400) {
                    twentySeconds = 0;
                    for (ForageLocation forageLocation : skrpg.getForageManager().getLocations()) {
                        double forageChance = Math.random();
                        if (forageChance <= 0.20) {
                            forageLocation.spawn();
                        }

                    }
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
                if (fiveMinutes == 12000) {
                    fiveMinutes = 0;
                    for (PlayerData playerData : skrpg.getPlayerManager().getPlayers()) {
                        playerData.getGlobal().calculateGlobalLevel(playerData, skrpg);
                    }
                }
                oneSecond++;
                fourSeconds++;
                twentySeconds++;
                fiveMinutes++;
            }
        }.runTaskTimer(skrpg, 0, 1);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        playerManager.disableMySQL();
        regionManager.disable();
        npcManager.disable();
        mobSpawnManager.disable();
        guildManager.disableGuilds();
        locationManager.disable();
        forageManager.disable();
        portalManager.disable();
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.kickPlayer("The server is reloading!");

        }
        if (brokenBlocks != null) {
            for (BrokenBlock brokenBlock : brokenBlocks.keySet()) {

                brokenBlock.create();
            }
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
    public ArrayList<Location> getBlocksInArea(Location loc1, Location loc2){
        int lowX = (loc1.getBlockX()<loc2.getBlockX()) ? loc1.getBlockX() : loc2.getBlockX();
        int lowY = (loc1.getBlockY()<loc2.getBlockY()) ? loc1.getBlockY() : loc2.getBlockY();
        int lowZ = (loc1.getBlockZ()<loc2.getBlockZ()) ? loc1.getBlockZ() : loc2.getBlockZ();

        ArrayList<Location> locs = new ArrayList<>();

        for(int x = 0; x<Math.abs(loc1.getBlockX()-loc2.getBlockX()); x++){
            for(int y = 0; y<Math.abs(loc1.getBlockY()-loc2.getBlockY()); y++){
                for(int z = 0; z<Math.abs(loc1.getBlockZ()-loc2.getBlockZ()); z++){
                    locs.add(new Location(loc1.getWorld(),lowX+x, lowY+y, lowZ+z));
                }
            }
        }

        return locs;
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

    public ArrayList<PortalSetup> getPlayersInPortalSetup() { return playersInPortalSetup; }
    public PortalSetup getPortalSetup(Player p) {
        for (PortalSetup portalSetup : getPlayersInPortalSetup()) {
            if (portalSetup.getSetupPlayer().getUniqueId() == p.getUniqueId()) { return portalSetup; }
        }
        return null;
    }
    public ArrayList<RegionSetup> getPlayersInRegionSetup() { return playersInRegionSetup; }
    public RegionSetup getRegionSetup(Player p) {
        for (RegionSetup regionSetup : getPlayersInRegionSetup()) {
            if (regionSetup.getSetupPlayer().getUniqueId() == p.getUniqueId()) { return regionSetup; }
        }
        return null;
    }
    public static void downloadFile(URL url, String fileName) throws Exception {
        try (InputStream in = url.openStream()) {
            Files.copy(in, Paths.get(fileName));
        }
    }
}
