package me.lluiscamino.multiversehardcore.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public final class MessageSender {

    private static final String prefix = ChatColor.DARK_RED + "[MV-HARDCORE]" + ChatColor.RESET + " ";

    private MessageSender() {
    }

    public static void sendError(@NotNull CommandSender user, @NotNull String message) {
        user.sendMessage(prefix + ChatColor.RED + message + ChatColor.RESET);
    }

    public static void sendNormal(@NotNull CommandSender user, @NotNull String message) {
        user.sendMessage(prefix + message);
    }

    public static void sendInfo(@NotNull CommandSender user, @NotNull String message) {
        user.sendMessage(prefix + ChatColor.BLUE + message + ChatColor.RESET);
    }

    public static void sendSuccess(@NotNull CommandSender user, @NotNull String message) {
        user.sendMessage(prefix + ChatColor.GREEN + message + ChatColor.RESET);
    }

    public static void broadcast(@NotNull String message) {
        Bukkit.broadcastMessage(prefix + message);
    }
}
