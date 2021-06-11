package com.auxdible.skrpg.npcs.npcs;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.items.SKRPGItemStack;
import com.auxdible.skrpg.npcs.NPC;
import com.auxdible.skrpg.npcs.NpcType;
import com.auxdible.skrpg.npcs.PurchasableItem;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.utils.Text;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;


import java.util.List;
import java.util.UUID;

public class Beachmaster implements NPC {
    private EntityPlayer entityPlayer;
    private SKRPG skrpg;
    private Location location;
    private Entity hitbox, clickStand;
    public Beachmaster(SKRPG skrpg, Location location) {
        this.skrpg = skrpg;
        this.location = location;
    }
    @Override
    public void buildNPC(SKRPG skrpg) {
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "beachmaster" + getId());
        gameProfile.getProperties().put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYxMjM0NTc1NTIzNSwKICAicHJvZmlsZUlkIiA6ICJiNWRkZTVmODJlYjM0OTkzYmMwN2Q0MGFiNWY2ODYyMyIsCiAgInByb2ZpbGVOYW1lIiA6ICJsdXhlbWFuIiwKICAic2lnbmF0dXJlUmVxdWlyZWQiIDogdHJ1ZSwKICAidGV4dHVyZXMiIDogewogICAgIlNLSU4iIDogewogICAgICAidXJsIiA6ICJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlL2ZiZWUxMjRmYjMyNjNkYzkzMzk5Y2U3NTU3ZDRiMjZkYWQ3M2MzNmVkZWFjYzQ5OTBhZmM1YzdmNmM1OTQwNjgiLAogICAgICAibWV0YWRhdGEiIDogewogICAgICAgICJtb2RlbCIgOiAic2xpbSIKICAgICAgfQogICAgfQogIH0KfQ==",
                "Boou2x2PMNNY0bvAazUrZBP7y68j5LbPxzA/UkVLVRqrRqDJjJBX903nNBCKh2rtIgn0NhM/LvoQFdcZOF4m6Fh78SgqtSIGf3Lx+X+yE/9ueD3xmPDFT6WvFSlrgMVPU764juMuZdSfMC8OXazkR0oaCbO6z2uxEgXa9MB6+8R6ajbUUKrLV4Exf3m/DSWRmjhn9k/nun5nMD9qxiFLtqo3zR6PrtHmNoKRa2j6y4VR2imWloawD1Zooys/tmz/Mzxk68+c2uS2klFisW5AGedFe8GYNwJIFbYeUC60rlj4gRKBbU10Ntu1si9P8H2DVMorOooG9vp6jwkFwNizdd1YX1KyAIz4tJLKiI/69eHSprwcKIk9IGva9edzm+IXNA0YnpWqvbU8j2tEcX9jGpLOpe/D5W02MP8N0u/9MGb1kqxQhuP1Hw/9DTJepFzxtJd7Q0ITg4891+dJtwS6yzmCwwslb6fednq5cHQoIKimJI81L1hgbUZNK2nPV3jsmID/bxnloY8WdBJkPV8oVE9AXBdrRUS8YxgZI1YnMmuq5ij5MMKxeoxp4Kz55lfqyMtMKALBH2/gIuFhDWITGjB+DVKchF9bzieuujbqWeRWDeKCBDOyI8OM+68rzArcBarTMdXR1/qM5kD9jIrf3TBIn9hEHCbhjd0wAHmmO7Y="));

        EntityPlayer entityPlayer = new EntityPlayer(((CraftServer) Bukkit.getServer()).getServer(),
                ((CraftWorld) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))).getHandle(), gameProfile,
                new PlayerInteractManager(((CraftWorld) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")))
                        .getHandle()));
        this.entityPlayer = entityPlayer;
        entityPlayer.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        org.bukkit.Location entLoc = new org.bukkit.Location(Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld")),
                location.getX(), location.getY() - 0.3, location.getZ());
        ArmorStand nameStand = (ArmorStand) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                .spawnEntity(location, EntityType.ARMOR_STAND);
        nameStand.teleport(location);
        nameStand.setInvisible(true);
        nameStand.setGravity(false);
        nameStand.setCustomName(Text.color("&aBeachmaster"));
        nameStand.setCustomNameVisible(true);
        nameStand.setInvulnerable(true);
        ArmorStand clickStand = (ArmorStand) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                .spawnEntity(entLoc, EntityType.ARMOR_STAND);
        clickStand.teleport(entLoc);
        clickStand.setInvisible(true);
        clickStand.setGravity(false);
        clickStand.setCustomName(Text.color("&e&lTRADE"));
        clickStand.setCustomNameVisible(true);
        clickStand.setInvulnerable(true);
        Slime slime = (Slime) Bukkit.getWorld(skrpg.getConfig().getString("rpgWorld"))
                .spawnEntity(location, EntityType.SLIME);
        slime.setSize(4);
        slime.setAI(false);
        slime.setInvisible(true);
        slime.setInvulnerable(true);
        slime.setGravity(false);
        slime.setCollidable(false);
        slime.setCustomName("beachmaster");
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
    public String getTypeID() { return "beachmaster"; }

    @Override
    public void onInteract(Player p, PlayerData playerData, SKRPG skrpg) {
        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1.0f, 0.5f);
        if (playerData.getPlayerInventory().getItemInMainHand() != null && playerData.getPlayerInventory().getItemInMainHand().getItemInfo().getItem().equals(Items.CRAB_FRAGMENT)) {
            if (playerData.getPlayerInventory().getItemInMainHand().getAmount() >= 8) {
                if (playerData.getPlayerInventory().getItemInMainHand().getAmount() == 8) {
                    playerData.getPlayerInventory().setItemInMainHand(null);
                    playerData.getPlayerInventory().updateInventory(p);
                } else {
                    playerData.getPlayerInventory().getItemInMainHand().setAmount(playerData.getPlayerInventory().getItemInMainHand().getAmount() - 8);
                    playerData.getPlayerInventory().updateInventory(p);
                }
                playerData.getPlayerInventory().addItem(new SKRPGItemStack(Items.CRAB_SCROLL.generateItemInfo(), 1));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1.0f, 0.5f);
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                Text.applyText(p, "&e&lBeachmaster &r&8| &7Thank you for your business. Stay safe, my friend.");
            }
            Text.applyText(p, "&e&lBeachmaster &r&8| &7This isn't enough &aCrab Fragments&7. Come back when you have more.");
        } else {
            Text.applyText(p, "&e&lBeachmaster &r&8| &7Hello. After all my years of fishing, I've discovered a way to obtain a &eCrab Scroll&7. &7This scroll has dangerous powers to summon... something. &7I would like &e8 &aCrab Fragments&7.");
        }
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
        return null;
    }

    @Override
    public Material getItemInHand() {
        return Material.FISHING_ROD;
    }
    @Override
    public NpcType getNpcType() {
        return NpcType.BEACHMASTER;
    }

    @Override
    public Entity getHitbox() { return hitbox; }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }
}
