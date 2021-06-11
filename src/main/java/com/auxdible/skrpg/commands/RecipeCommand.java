package com.auxdible.skrpg.commands;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.CraftingIngrediant;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.player.skills.Crafting;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
        if (args[0].equals("list")) {
            StringJoiner items = new StringJoiner(",");
            for (Items items1 : EnumSet.allOf(Items.class)) {
                if (items1.getCraftingRecipe() != null) {
                    items.add(items1.getId());
                }

            }
            player.sendMessage("Recipes in SKQuest: " + items.toString());
        }
        for (Items items : EnumSet.allOf(Items.class)) {
            if (args[0].equalsIgnoreCase(items.getId()) && items.getCraftingRecipe() != null) {
                Inventory inventory = Bukkit.createInventory(null, 45, Text.color("Recipe for " + items.getRarity().getColor() + items.getName()));
                for (int i = 0; i <= 44; i++) {
                    inventory.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
                }
                List<Integer> slots = Arrays.asList(10, 11, 12, 19, 20, 21, 28, 29, 30);
                ItemStack builtResult = Items.buildItem(items);
                builtResult.setAmount(items.getCraftingAmount());
                inventory.setItem(10, null);
                inventory.setItem(11, null);
                inventory.setItem(12, null);
                inventory.setItem(19, null);
                inventory.setItem(20, null);
                inventory.setItem(21, null);
                inventory.setItem(28, null);
                inventory.setItem(29, null);
                inventory.setItem(30, null);
                inventory.setItem(25, builtResult);
                for (CraftingIngrediant craftingItem : items.getCraftingRecipe()) {
                    ItemStack itemStackBuilt = Items.buildItem(craftingItem.getItems());
                    if (craftingItem.getItems() != Items.NONE) {
                        itemStackBuilt.setAmount(craftingItem.getAmount());
                    }
                    inventory.setItem(slots.get(items.getCraftingRecipe().indexOf(craftingItem)), itemStackBuilt);

                }
                player.openInventory(inventory);
                return false;
            }
        }
        Text.applyText(player, "&cPlease name an item!");
        return false;
    }
}
