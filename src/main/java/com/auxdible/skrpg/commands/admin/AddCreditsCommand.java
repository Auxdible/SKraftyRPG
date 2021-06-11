package com.auxdible.skrpg.commands.admin;

import com.auxdible.skrpg.SKRPG;

import com.auxdible.skrpg.utils.Text;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;


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
        /*if (!player.hasPermission("skrpg.admin")) {
            Text.applyText(player, "&cAdmin is required to run this command!");
            return false;
        }*/
        if (args.length == 0) {
            Text.applyText(player, "&cPlease enter an amount!");
        }
        try {
            Integer.parseInt(args[0]);
        } catch (NumberFormatException x) {
            if (args[0].equalsIgnoreCase("resetInterest")) {
                LocalDate localDate = LocalDate.now();
                ZoneId zoneId = ZoneId.systemDefault();
                skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).setIntrestDate(Date.from(localDate.atStartOfDay(zoneId).toInstant()));
                Text.applyText(player, "Reset your intrest date ((DEBUG))");
                return false;
            }
            Text.applyText(player, "&cPlease enter a valid number!");
            return false;
        }
        new BukkitRunnable() {
            int seconds = 1;
            @Override
            public void run() {
                if (seconds == 1) {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1.0f, 1.0f);
                } else if (seconds == 2) {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1.0f, 0.5f);
                } else if (seconds == 3) {
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HARP, 1.0f, 2.0f);
                    cancel();
                }
                seconds++;
            }
        }.runTaskTimer(skrpg, 0, 4);
        skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).setCredits(
                skrpg.getPlayerManager().getPlayerData(player.getUniqueId()).getCredits() + Integer.parseInt(args[0]));

        return false;
    }
}
