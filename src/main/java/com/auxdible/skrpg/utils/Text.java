package com.auxdible.skrpg.utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class Text {
   private Text() {}
   public static void applyText(Player reciever, String text) {
       TextComponent component = new TextComponent(color(text));
       reciever.spigot().sendMessage(component);
   }
   public static void applyTextWithHover(Player reciever, String text, String hoverMessage) {
        TextComponent component = new TextComponent(color(text));
        ComponentBuilder componentBuilder = new ComponentBuilder(color(hoverMessage));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, componentBuilder.create()));
        reciever.spigot().sendMessage(component);
    }
    // MUST INVOLVE SLASH.
    public static void applyTextWithCommand(Player reciever, String text, String command) {
        TextComponent component = new TextComponent(color(text));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        reciever.spigot().sendMessage(component);
    }
    public static void applyTextWithURL(Player reciever, String text, String url) {
        TextComponent component = new TextComponent(color(text));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
        reciever.spigot().sendMessage(component);
    }
    public static void applyTextAdvanced(Player reciever, String text, String hoverMessage, String command) {
        TextComponent component = new TextComponent(color(text));
        ComponentBuilder hover = new ComponentBuilder(color(hoverMessage));
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hover.create()));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        reciever.spigot().sendMessage(component);
    }

    public static String color(String s) { return ChatColor.translateAlternateColorCodes('&', s); }
    public static void applyTypingTitle(JavaPlugin javaPlugin, Player p, String titleToType, int ticksInbetweenCharacter, int ticksHold) {
       String finalTitle = Text.color(titleToType);
       StringJoiner totalString = new StringJoiner("");
       for (int i = 0; i < finalTitle.length(); i++) {
           int finalI = i;
           new BukkitRunnable() {

               @Override
               public void run() {
                   totalString.add(finalTitle.charAt(finalI) + "");
                   p.sendTitle(totalString.toString(), "", 0,  ticksHold, 0);
               }
           }.runTaskLater(javaPlugin, ticksInbetweenCharacter * i);
       }
    }
}

