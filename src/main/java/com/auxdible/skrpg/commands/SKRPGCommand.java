package com.auxdible.skrpg.commands;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SKRPGCommand implements CommandExecutor {
    private SKRPG skrpg;
    public SKRPGCommand(SKRPG skrpg) {
        this.skrpg = skrpg;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            skrpg.getLogger().info("You need to be a player to use this!");
        }
        Player p = (Player) sender;
        if (args.length == 0) {
            p.performCommand("skquest help");
        } else if (args[0].equalsIgnoreCase("help")) {
            Text.applyText(p, "&8&m>        &r&b⚔ &e&lSKQuest HELP &r&b⚔&8&m        <");
            Text.applyTextWithHover(p, "&b• &e/skquest", "&7Use the basic SKQuest commands.");
            Text.applyTextWithHover(p, "&b• &e/menu", "&7Access the SKQuest menu.");
            Text.applyTextWithHover(p, "&b• &e/guild", "&7Access Guilds. &c(Must be Combat 10!)");
            Text.applyTextWithHover(p, "&b• &e/trade", "&7Trade with another player.");
            Text.applyText(p, "&8&m>                              <");
        }
        return false;
    }
}
