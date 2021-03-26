package com.auxdible.skrpg.commands;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.ItemInfo;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.items.Rarity;
import com.auxdible.skrpg.items.enchantments.Enchantment;
import com.auxdible.skrpg.items.enchantments.Enchantments;
import com.auxdible.skrpg.utils.Text;

import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftMagicNumbers;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class ItemDataCommand implements CommandExecutor {
    private SKRPG skrpg;
    public ItemDataCommand(SKRPG skrpg) {
        this.skrpg = skrpg;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            skrpg.getLogger().info("You can only use this command as a player!");
            return false;
        }
        Player p = (Player) sender;
        if (p.getInventory().getItemInMainHand() != null) {
            if (args.length == 0) {
                ItemInfo itemInfo = ItemInfo.parseItemInfo(p.getInventory().getItemInMainHand());
                Text.applyText(p, "&7Enchantments:");
                if (itemInfo.getEnchantmentsList().isEmpty()) {
                    Text.applyText(p, "&cNO ENCHANTMENTS");
                } else {
                    for (Enchantment enchantment : itemInfo.getEnchantmentsList()) {
                        Text.applyText(p, "&5" + enchantment.getEnchantmentType().getName() + " " + enchantment.getLevel());
                    }
                }
                Text.applyText(p, "&7Rarity: " + itemInfo.getRarity().getNameColored());
                Text.applyText(p, "&7Item Type: &e" + itemInfo.getItem().getName());
                Text.applyText(p, "&7Bonus Damage: &e" + itemInfo.getBonusDamage());
                Text.applyText(p, "&7Bonus Defence: &e" + itemInfo.getBonusDefence());
                Text.applyText(p, "&7Bonus Speed: &e" + itemInfo.getBonusSpeed());
                Text.applyText(p, "&7Bonus Energy: &e" + itemInfo.getBonusEnergy());
                Text.applyText(p, "&7Bonus Strength: &e" + itemInfo.getBonusStrength());
            }
        }
        return false;
    }
}
