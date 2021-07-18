package me.lluiscamino.multiversehardcore.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public final class MessageSender {

    private static String formattedPrefix = ChatColor.DARK_RED + "[MV-HARDCORE] " + ChatColor.RESET;

    private MessageSender() {
    }

    public static void setPrefix(String prefix) {
        formattedPrefix = prefix + ChatColor.RESET;
    }

    public static void sendError(@NotNull CommandSender user, @NotNull String message) {
        user.sendMessage(formattedPrefix + ChatColor.RED + message + ChatColor.RESET);
    }

    public static void sendNormal(@NotNull CommandSender user, @NotNull String message) {
        user.sendMessage(formattedPrefix + message);
    }

    public static void sendInfo(@NotNull CommandSender user, @NotNull String message) {
        user.sendMessage(formattedPrefix + ChatColor.BLUE + message + ChatColor.RESET);
    }

    public static void sendSuccess(@NotNull CommandSender user, @NotNull String message) {
        user.sendMessage(formattedPrefix + ChatColor.GREEN + message + ChatColor.RESET);
    }

    public static void broadcast(@NotNull String message) {
        Bukkit.broadcastMessage(formattedPrefix + message);
    }
}
