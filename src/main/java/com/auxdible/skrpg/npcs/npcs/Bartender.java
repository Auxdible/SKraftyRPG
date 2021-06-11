package com.auxdible.skrpg.npcs.npcs;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.npcs.NPC;
import com.auxdible.skrpg.npcs.NpcType;
import com.auxdible.skrpg.npcs.PurchasableItem;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.player.quests.royaltyquests.RoyaltyUpgrades;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.Text;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Bartender implements NPC {
    private EntityPlayer entityPlayer;
    private SKRPG skrpg;
    private Location location;
    private Entity hitbox, clickStand;
    public Bartender(SKRPG skrpg, Location location) {
        this.skrpg = skrpg;
        this.location = location;
    }
    @Override
    public void buildNPC(SKRPG skrpg) {
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "bartender" + getId());
        gameProfile.getProperties().put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYwNTAxMTYzODE5OCwKICAicHJvZmlsZUlkIiA6ICIwNmE4NjAyZDAwODk0YWQxOTcyMGQ3NGE1OGU1MDZjZiIsCiAgInByb2ZpbGVOYW1lIiA6ICJfMW5kcmFfIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzhiNDliNWQzYmJmOTQ3N2Q2MDQyMDdiMWY0YzlhYjIzNmQyYzZjNzk2Mzk3OTFkMTY5N2FiZDQzYTZiNjY2ZWEiCiAgICB9CiAgfQp9",
                "Gf7cvIlHyGeI1ybqwpDKS2qpGNZuhO8XSb5kj5m+hOmsNgKaubqvBgPgJ4L/48wCowjcCJJYstlRgYu5r1kERAh3HaEUqpKepcIZGIX2oC8y6vJkZ6ZnfbknFCTI3bqLcUC9PhPJGoVxWpLV8EoJ9t1mhgtO54bV43PUyIV3hHihNjK3TiKmfuxiF4s4qITDPKGcKFRU9fGrxGNo8DANQpZMXWwpB7uhlU7QK2jQN2o6dvxMi+0szMpUeVxk29iyCzOxToUjww7VicSqxnatDnNFJXwr2mJnO+6c3ud6T2UXTvu/t+duHUPg0mK45QgGeIv7K/8UyMYschtugOZPExJDbdeZCeM+ce4Z9urFm1f/23+2cU81XQikbKAVQrJtmGD0bauAOPgTn5joO0LWhA3mVZWseg7jAs98AC1LXdZ4hik+j2YRwijhNRQqA/6z+VzCLPGY85N8Za6wL7ODY5l5P9pz8Su2BZV4Yo07ay4s2jzdNjon3gDocZbridj6NtiBDVX0GbMLnxl3IJcezsjA75dlMQFo9c4Tz1vILGjKBLOxPe3VnyyFZ7bGHHCJkgW1SLtw/tqUiVAGeFd+O1g8OERT44O2E5psjTjatliPbOQo5fE0obvuZPTqdYRMNpQmWs4tp1qyDKEZk4qU4f/f9yyXW7TtGKTfgLDUdXA="));

        EntityPlayer entityPlayer = new EntityPlayer(((CraftServer) Bukkit.getServer()).getServer(),
                ((CraftWorld) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))).getHandle(), gameProfile,
                new PlayerInteractManager(((CraftWorld) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")))
                        .getHandle()));
        this.entityPlayer = entityPlayer;
        entityPlayer.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        Location entLoc = new Location(Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")),
                location.getX(), location.getY() - 0.3, location.getZ());
        ArmorStand nameStand = (ArmorStand) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                .spawnEntity(location, EntityType.ARMOR_STAND);
        nameStand.teleport(location);
        nameStand.setInvisible(true);
        nameStand.setGravity(false);
        nameStand.setInvulnerable(true);
        nameStand.setCustomName(Text.color("&aBartender"));
        nameStand.setCustomNameVisible(true);
        ArmorStand clickStand = (ArmorStand) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                .spawnEntity(entLoc, EntityType.ARMOR_STAND);
        clickStand.teleport(entLoc);
        clickStand.setInvisible(true);
        clickStand.setGravity(false);
        clickStand.setInvulnerable(true);
        clickStand.setCustomName(Text.color("&e&lBUY"));
        clickStand.setCustomNameVisible(true);
        Slime slime = (Slime) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                .spawnEntity(location, EntityType.SLIME);
        slime.setSize(4);
        slime.setAI(false);
        slime.setInvisible(true);
        slime.setInvulnerable(true);
        slime.setGravity(false);
        slime.setCollidable(false);
        slime.setCustomName("bartender");
        slime.setRemoveWhenFarAway(false);
        hitbox = slime;
        this.clickStand = clickStand;
    }
    @Override
    public void deleteNPC() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerConnection playerConnection = ((CraftPlayer) player).getHandle().playerConnection;
            playerConnection.sendPacket(new PacketPlayOutPlayerInfo(
                    PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, getEntityPlayer()));
            playerConnection.sendPacket(new PacketPlayOutEntityDestroy(getEntityPlayer().getId()));
            PlayerData playerData = skrpg.getPlayerManager().getPlayerData(player.getUniqueId());
            if (playerData.getRenderedNPCs() != null && playerData.getRenderedNPCs().contains(this)) {
                playerData.getRenderedNPCs().remove(this);
            }
        }
        hitbox.remove();
        clickStand.remove();
    }
    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public int getId() {
        return skrpg.getNpcManager().getNpcs().indexOf(this);
    }

    @Override
    public String getTypeID() {
        return "bartender";
    }

    @Override
    public void onInteract(Player p, PlayerData playerData, SKRPG skrpg) {
        Inventory inventory = Bukkit.createInventory(null, 54, "Bartender | Salesman");
        List<Integer> outlineSlots = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53);
        for (int i : outlineSlots) {
            inventory.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
        }
            for (PurchasableItem purchasableItem : getPurchasableItems()) {
                boolean found = false;
                List<ItemStack> contents = Arrays.asList(inventory.getContents());
                for (ItemStack itemStack : contents) {
                    if (itemStack == null && !found) {
                        double cost = purchasableItem.getCost();
                        if (playerData.getRoyaltyUpgrades().containsKey(RoyaltyUpgrades.REDUCE_PRICE)) {
                            cost = Math.round(((cost * (playerData.getRoyaltyUpgrades().get(RoyaltyUpgrades.REDUCE_PRICE) * 0.5)) * 100.0)) / 100.0;
                        }
                        ItemStack itemStackBuild = Items.buildItem(purchasableItem.getItem());
                        ItemMeta iM = itemStackBuild.getItemMeta();
                        List<String> lore = iM.getLore();
                        lore.add(" ");
                        lore.add(Text.color("&7Cost: &6" + cost + " Nuggets"));
                        iM.setLore(lore);
                        itemStackBuild.setItemMeta(iM);
                        inventory.setItem(contents.indexOf(itemStack), itemStackBuild);
                        found = true;
                    }
                }
            }
        p.openInventory(inventory);
        }



    @Override
    public EntityPlayer getEntityPlayer() {
        return entityPlayer;
    }

    @Override
    public Entity getEntity() {
        return null;
    }

    @Override
    public List<PurchasableItem> getPurchasableItems() {
        return Arrays.asList(new PurchasableItem(Items.STRENGTH_COLA, 500),
                new PurchasableItem(Items.SUPER_CANDY, 500),
                new PurchasableItem(Items.LEMON_TEA, 500),
                new PurchasableItem(Items.OVERLY_CAFFINATED_COFFEE, 500));
    }

    @Override
    public Material getItemInHand() {
        return null;
    }
    @Override
    public NpcType getNpcType() {
        return NpcType.BARTENDER;
    }

    @Override
    public Entity getHitbox() { return hitbox; }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

}
