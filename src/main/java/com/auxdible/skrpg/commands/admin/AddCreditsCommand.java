package com.auxdible.skrpg.commands.admin;

import com.auxdible.skrpg.SKRPG;
import com.auxdible.skrpg.items.Items;
import com.auxdible.skrpg.utils.Text;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.parser.ParseException;

import java.util.EnumSet;

public class AddCreditsCommand implements CommandExecutor {
    private SKRPG skrpg;
    public AddCreditsCommand(SKRPG skrpg) {
        this.skrpg = skrpg;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            skrpg.getLogger().info("You cannot use this command as the console!");
            return false;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            Text.applyText(player, "&cPlease enter an amount!");
        }
        try {
            Integer.parseInt(args[0]);
        } catch (NumberFormatException x) {
            Text.applyText(player, "&cPlease enter a valid number!");
            return false;
        }
        skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).setCredits(
                skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getCredits() + Integer.parseInt(args[0]));
        return false;
    }
}
