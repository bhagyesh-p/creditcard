package com.dogboy602k.CreditCard.main.Util;/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 */

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class SendConsoleMessage {
    private static final String prefix = (Object)ChatColor.GREEN + "[CC] ";
    private static final String info = "[Info] ";
    private static final String severe = (Object)ChatColor.YELLOW + "[Severe] ";
    private static final String warning = (Object)ChatColor.RED + "[Warning] ";
    private static final String debug = (Object)ChatColor.AQUA + "[Debug] ";

    public static void info(String message) {
        Bukkit.getConsoleSender().sendMessage(prefix + "[Info] " + message);
    }

    public static void sever(String message) {
        Bukkit.getConsoleSender().sendMessage(prefix + severe + message);
    }

    public static void warning(String message) {
        Bukkit.getConsoleSender().sendMessage(prefix + warning + message);
    }

    public static void debug(String message) {
        Bukkit.getConsoleSender().sendMessage(prefix + debug + message);
    }
}

