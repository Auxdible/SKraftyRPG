package com.auxdible.skrpg.npcs;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.player.PlayerData;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;



import java.util.List;

public interface NPC {
    void buildNPC(SKRPG skrpg);
    void deleteNPC();
    Location getLocation();
    int getId();
    String getTypeID();
    void onInteract(Player p, PlayerData playerData, SKRPG skrpg);
    EntityPlayer getEntityPlayer();
    Entity getEntity();
    Entity getHitbox();
    List<PurchasableItem> getPurchasableItems();
    Material getItemInHand();
    NpcType getNpcType();

    void setLocation(Location location);
}
