package com.auxdible.skrpg.items.abilities;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.EnumSet;

public enum Abilities {
    SPEED_BOOST(new SpeedBoostAbility()),
    NATURE_LAUNCH(new NatureLaunchAbility()),
    CRAB_KING_SUMMON(new CrabKingSummonAbility()),
    VALISSA_ARACHNE_SUMMON(new ValissaArachneSummonAbility()),
    STUN(new StunAbility());
    private Ability ability;
    Abilities(Ability ability) {
        this.ability = ability;
    }
    public Ability getAbility() { return ability; }
    public static void executeAbility(PlayerData playerData, SKRPG skrpg) {
        Player p = Bukkit.getPlayer(playerData.getUuid());
        Items playerItem = null;
        if (p == null) { return; }
        if (p.getInventory().getItemInMainHand().getType().equals(Material.AIR)) { return; }
        if (p.getInventory().getItemInMainHand().getItemMeta() == null) { return; }
        for (Items items : EnumSet.allOf(Items.class)) {
            if (ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getDisplayName()).contains(items.getName())) {
                playerItem = items;
            }
        }
        if (playerItem == null) { return; }
        Ability ability = null;
        for (Abilities abilitiesSet : EnumSet.allOf(Abilities.class)) {
            if (abilitiesSet.getAbility().getItem() == playerItem) {
                ability = abilitiesSet.getAbility();
            }
        }
        if (ability == null) { return; }
        int cost = ability.getCost();
        if (cost == -1) {
            cost = playerData.getMaxEnergy();
        }
        if (playerData.getEnergy() < cost) {
            Text.applyText(p, "&cYou need more Energy to do this!");
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0f, 1.0f);
            return;
        }
        playerData.setEnergy(playerData.getEnergy() - cost);
        Text.applyText(p, "&7Used &e" + ability.getName() + "&7! &7[&e-" + cost + " Energy&7]");
        ability.ability(playerData, skrpg);
    }
}