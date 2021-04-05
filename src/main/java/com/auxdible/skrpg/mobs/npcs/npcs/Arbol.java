package com.auxdible.skrpg.mobs.npcs.npcs;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.mobs.npcs.NPC;
import com.auxdible.skrpg.mobs.npcs.NpcType;
import com.auxdible.skrpg.mobs.npcs.PurchasableItem;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.Text;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.PlayerInteractManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Arbol implements NPC {
    private EntityPlayer entityPlayer;
    private int id;
    private Location location;
    public Arbol(int id, Location location) {
        this.id = id; this.location = location;
    }
    @Override
    public void buildNPC(SKRPG skrpg) {
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "arbol" + getId());
        gameProfile.getProperties().put("textures", new Property("textures", "eyJ0aW1lc3RhbXAiOjE1ODgwMjYyMjA4NDgsInByb2ZpbGVJZCI6IjNlM2IxMzljYzZlNzRiMzhiNTQ5ODRkY2ViNWI3MjJlIiwicHJvZmlsZU5hbWUiOiJNaWN1bGdhbWVzIiwic2lnbmF0dXJlUmVxdWlyZWQiOnRydWUsInRleHR1cmVzIjp7IlNLSU4iOnsidXJsIjoiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9mYjQ0ZjliMTlmNDVmMTM1NmY5YzVjODVmZjIxMzcwMTgwNjhlNTBjODRmZjBiMmFlN2Q5Y2UwZjMzNjI5ZDM0In19fQ==",
                "hI7vcrVSu5V10aP6dOv4J9HK+Rpsc7zD/XuyEqzvofQMM5Va9hkHtBz8a3DiK44xEdKm/ro6s9ekj25oWXBEPN/nUFsLwSqeQ6LvfwCpcZCTADdOHjcGKZ3jkZhUtsZCwd/Ss4m61m1Sda3rsh6BGmAR1hjs0SPOfVyGjB2+eHckmDRbRKk50EJWsQKn/u8bofTLDHdEsHHwBUzO5+vkqLCbpcRRHze8mqV7CRiUxJ+lKb0NkYeaxB1GUM73jgc39j5Sof39008tTELPcSo0BzWZ2Ig9Rf7b6HgThG63KP9tBz7iX8+mLutParceOkngnd9REBzDDMUk1lO2TqUYhKYdKNS7kpsn4t5Cw7qaTkyyLBVW8GfXkN/2/J4crhKRi8ji3BxjKYJE2VuSCjt0vfak5gFwCt/Y9r/Stp8QOAYzZCTvKG6klNAqxBWpXKwGlKtlPc2j032SzWexpgu3cu0nGw/42EC3kqNW/20mXUKi6zXQ2VSR8AIa75k4tIprFo4UsmoIxKO26961PmdFu+EB+k3RpebxvyDUlJWHp5/strkV6KswcCNnTcXcELmS1HHy5uMT4fSRXZ0VpucVFb7/phcSXlMpi/252AWOamCy29JPQ74ugE3pfPID01aO5/UDUrTR9FXHm886taY9nxSQS5lOV5BU/9Gz2fK4zHk="));

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
        nameStand.setInvulnerable(true);
        nameStand.setCustomName(Text.color("&aArbol"));
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
        slime.setCustomName("arbol");
        slime.setRemoveWhenFarAway(false);
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getTypeID() {
        return "arbol";
    }

    @Override
    public void onInteract(Player p, PlayerData playerData, SKRPG skrpg) {
        Inventory inventory = Bukkit.createInventory(null, 54, "Arbol | Salesman");
        List<Integer> outlineSlots = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53);
        for (int i : outlineSlots) {
            inventory.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
        }
            for (PurchasableItem purchasableItem : getPurchasableItems()) {
                boolean found = false;
                List<ItemStack> contents = Arrays.asList(inventory.getContents());
                for (ItemStack itemStack : contents) {
                    if (itemStack == null && !found) {
                        ItemStack itemStackBuild = Items.buildItem(purchasableItem.getItem());
                        ItemMeta iM = itemStackBuild.getItemMeta();
                        List<String> lore = iM.getLore();
                        lore.add(" ");
                        lore.add(Text.color("&7Cost: &6" + purchasableItem.getCost() + " Nuggets"));
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
        return Arrays.asList(new PurchasableItem(Items.NATURE_STAFF, 25000),
                new PurchasableItem(Items.COMPACT_WOOD, 1000));
    }

    @Override
    public Material getItemInHand() {
        return null;
    }
    @Override
    public NpcType getNpcType() {
        return NpcType.ARBOL;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

}
