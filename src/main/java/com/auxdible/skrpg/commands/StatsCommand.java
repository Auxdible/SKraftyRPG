package com.auxdible.skrpg.commands;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.player.PlayerData;
import com.auxdible.skrpg.utils.ItemBuilder;
import com.auxdible.skrpg.utils.ItemTweaker;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class StatsCommand implements CommandExecutor {
    private SKRPG skrpg;
    public StatsCommand(SKRPG skrpg) {
        this.skrpg = skrpg;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            skrpg.getLogger().info("You must be a player to use this command!");
        }
        Player p = (Player) sender;
        PlayerData playerData = skrpg.getPlayerManager().getPlayerData(p.getUniqueId());
        Inventory inv = Bukkit.createInventory(null, 36, "Your Statistics");
        for (int i = 0; i <= 35; i++) {
            inv.setItem(i, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE, 0).setName(" ").asItem());
        }
        ItemStack hpHead = ItemTweaker.createPlayerHeadFromData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly9" +
                "0ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjJlOGUwYjQ0ZDFlY" +
                "zFhYTY2ODRjNTQxMTI4ODczOGM5M2E5OTdjZmViZDE1ZmI3NmJmODZhOWVlZGE3NTMifX19", "80fb3aac-e118-4f75-b" +
                "a42-312fd6ca885d");
        ItemMeta hpiM = hpHead.getItemMeta();
        hpiM.setDisplayName(Text.color("&cYour HP ♥"));
        hpiM.setLore(Arrays.asList(" ", Text.color("&7Max HP: &c" + playerData.getMaxHP() +  " ♥"), Text.color("&7Base HP: &c" +
                playerData.getBaseHP() + " ♥"), " ", " ", Text.color("&7HP Score: &a" + (playerData.getMaxHP() *
                ((playerData.getDefence() / 100.0) + 1))), Text.color("&7&o(Your HP Score is your HP with"),
                Text.color("&7&oyour defence factored in.)")));
        hpHead.setItemMeta(hpiM);
        inv.setItem(10, hpHead);
        inv.setItem(11, new ItemBuilder(Material.SHIELD, 0).setName("&aYour Defence ✿")
                .setLore(Arrays.asList(" ", Text.color("&7Total Defence: &a" + playerData.getDefence() + " ✿"),
                        Text.color("&7Base Defence: &a" + playerData.getBaseDefence() + " ✿"))).asItem());
        inv.setItem(12, new ItemBuilder(Material.RED_DYE,0).setName("&4Your Strength ☄")
                .setLore(Arrays.asList(" ", Text.color("&7Total Strength: &4" + playerData.getStrength() + " ☄"),
                        Text.color("&7Base Strength: &4" + playerData.getBaseStrength() + " ☄"))).asItem());
        ItemStack energyHead = ItemTweaker.createPlayerHeadFromData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6I" +
                        "mh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzhmZGI2Mzc2NDc" +
                        "2ODg4ODMyODE3MDI4M2I0NGRiZjExNTc1ZDZkMmQ5MDBiN2ViNjY0OGUwMTE4Mjk4In19fQ==",
                "c2d6306d-6101-478c-8aed-7fa4b84e093d");
        ItemMeta energyMeta = energyHead.getItemMeta();
        energyMeta.setDisplayName(Text.color("&eYour Energy ☢"));
        energyMeta.setLore(Arrays.asList(" ", Text.color("&7Max Energy: &e" + playerData.getMaxEnergy() + " ☢"),
                Text.color("&7Base (Max) Energy: &e" +
                        playerData.getBaseEnergy() + " ☢")));
        energyHead.setItemMeta(energyMeta);
        inv.setItem(13, energyHead);
        ItemStack creditsHead = ItemTweaker.createPlayerHeadFromData("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA" +
                "6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmQzNzI2YzFiMzY4OTNmZ" +
                "TMzNDQ5Mzk1ODQ0ZGJhMTZlZjIyNDFkZGI1MzM1NWMyOTdmMDQxMzhmZWJjY2FmIn19fQ==",
                "8fa29c20-1475-44c7-a77b-1c87bc10b3b2");
        ItemMeta creditsMeta = creditsHead.getItemMeta();
        creditsMeta.setDisplayName(Text.color("&7Your Credits: &b" + playerData.getCredits() + " C$"));
        creditsHead.setItemMeta(creditsMeta);
        inv.setItem(14, creditsHead);
        inv.setItem(15, new ItemBuilder(Material.SUGAR, 0).setName("&fYour Speed ≈")
                .setLore(Arrays.asList(" ", Text.color("&7Total Speed: &f" + playerData.getSpeed() + " ≈"),
                        Text.color("&7Base Speed: &f" + playerData.getBaseSpeed() + " ≈"))).asItem());
        inv.setItem(27, new ItemBuilder(Material.ARROW, 0).setName("&aBack to Menu").asItem());
        p.openInventory(inv);
        return false;
    }
}
