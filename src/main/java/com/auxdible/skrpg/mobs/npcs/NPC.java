package com.auxdible.skrpg.mobs.npcs;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.player.PlayerData;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.ItemStack;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;



import java.util.List;

public interface NPC {
    void buildNPC(SKRPG skrpg);
    Location getLocation();
    int getId();
    String getTypeID();
    void onInteract(Player p, PlayerData playerData, SKRPG skrpg);
    EntityPlayer getEntityPlayer();
    Entity getEntity();
    List<PurchasableItem> getPurchasableItems();
    Material getItemInHand();
    NpcType getNpcType();
    void setId(int id);
    void setLocation(Location location);
}
