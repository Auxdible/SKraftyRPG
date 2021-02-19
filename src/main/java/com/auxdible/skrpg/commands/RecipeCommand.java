package com.auxdible.skrpg.commands;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.CraftingIngrediant;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.Hash;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class RecipeCommand implements CommandExecutor {
    private SKRPG skrpg;
    public RecipeCommand(SKRPG skrpg) { this.skrpg = skrpg; }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            skrpg.getLogger().info("You cannot use this command as the console!");
            return false;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            Text.applyText(player, "&cPlease name an item!");
            return false;
        }

        for (Items items : EnumSet.allOf(Items.class)) {
            if (args[0].equalsIgnoreCase(items.getId()) && items.getCraftingRecipe() != null) {
                Inventory inventory = Bukkit.createInventory(null, 45, Text.color("Recipe for " + items.getRarity().getColor() + items.getName()));
                for (int i = 0; i <= 44; i++) {
                    inventory.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
                }
                ItemStack builtResult = Items.buildItem(items);
                builtResult.setAmount(items.getCraftingAmount());
                inventory.setItem(10, Items.buildItem(items.getCraftingRecipe().get(1).getItems()));
                inventory.setItem(11, Items.buildItem(items.getCraftingRecipe().get(2).getItems()));
                inventory.setItem(12, Items.buildItem(items.getCraftingRecipe().get(3).getItems()));
                inventory.setItem(19, Items.buildItem(items.getCraftingRecipe().get(4).getItems()));
                inventory.setItem(20, Items.buildItem(items.getCraftingRecipe().get(5).getItems()));
                inventory.setItem(21, Items.buildItem(items.getCraftingRecipe().get(6).getItems()));
                inventory.setItem(28, Items.buildItem(items.getCraftingRecipe().get(7).getItems()));
                inventory.setItem(29, Items.buildItem(items.getCraftingRecipe().get(8).getItems()));
                inventory.setItem(30, Items.buildItem(items.getCraftingRecipe().get(9).getItems()));
                inventory.setItem(25, builtResult);
                player.openInventory(inventory);
                return false;
            }
        }
        Text.applyText(player, "&cPlease name an item!");
        return false;
    }
}
