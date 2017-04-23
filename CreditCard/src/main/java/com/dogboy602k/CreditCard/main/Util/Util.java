package com.dogboy602k.CreditCard.main.Util;/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.World
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Util {
    private static Player sender;

    public static List<Player> getOnlinePlayers() {
        ArrayList list = Lists.newArrayList();
        for (World world : Bukkit.getWorlds()) {
            list.addAll(world.getPlayers());
        }
        return Collections.unmodifiableList(list);
    }

    public static void sendMsg(Player sender, String message) {
        sender.sendMessage(ChatColor.GOLD + "[CC] " + message);
    }

    public static void sendMsg(CommandSender sender, String message) {
        sender.sendMessage(message);
    }

    public static void sendEmptyMsg(CommandSender sender, String message) {
        sender.sendMessage(message);
    }
}

