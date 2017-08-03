package com.dogboy602k.CreditCard.main.Commands;/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 */

import com.dogboy602k.CreditCard.main.Main.Main;
import com.dogboy602k.CreditCard.main.Util.SendConsoleMessage;
import com.dogboy602k.CreditCard.main.Util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Random;

public class Commands
implements CommandExecutor {
    private Main plugin;
    Random rn = new Random();
    public String Password;

    public Commands(Main plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(args.length == 1 && args[0].equals("rewards")){
            Util.sendMsg((Player)sender, ChatColor.AQUA + "List Rewards");
            this.plugin.getManager().listRewards(((Player) sender).getPlayer());
            return true;
        }
        if(args.length == 1 && args[0].equals("reload")){
            if (!sender.hasPermission("creditcard.admin.reload")) {
                Util.sendMsg((Player)sender, ChatColor.GOLD + "[CC]" + ChatColor.RED + " You do not have permission to save the yml file");
                return true;
            }else {
                File rewardDataFile = new File(this.plugin.getDataFolder(), "rewards.yml");
                File playerDataFile = new File(this.plugin.getDataFolder(), "playerdata.yml");

                this.plugin.getFileManager().reloadConfiguration(rewardDataFile);
                this.plugin.getFileManager().reloadConfiguration(playerDataFile);
                plugin.reloadConfig();
                Util.sendMsg((Player)sender, ChatColor.AQUA + "You reloaded the rewards and playerdata files ");

            }
            return true;
        }

        if (args.length == 0 || args.length == 1 || args.length > 4) {
            Util.sendMsg((Player)sender, ChatColor.AQUA + "Usage /CreditCard loan " + ChatColor.AQUA + "<username>" + ChatColor.GREEN + " <need amount>" + ChatColor.GOLD + " <four pin code>");
            Util.sendMsg((Player)sender, ChatColor.AQUA + "Usage /CreditCard owe " + ChatColor.AQUA + "<username> ");
            Util.sendMsg((Player)sender, ChatColor.AQUA + "Usage /CreditCard pay " + ChatColor.AQUA + "<username> " + ChatColor.GREEN + " <amount>");
            Util.sendMsg((Player)sender, ChatColor.AQUA + "Usage /CreditCard pay " + ChatColor.AQUA + "<username> " + ChatColor.GREEN + " all");
            Util.sendMsg((Player)sender, ChatColor.AQUA + "Usage /CreditCard register " + ChatColor.AQUA + "<username> " + ChatColor.RED + "<password>");
            Util.sendMsg((Player)sender, ChatColor.AQUA + "Usage /CreditCard forgot" + ChatColor.AQUA + " <username> " + ChatColor.RED + "<password>");
            Util.sendMsg((Player)sender, ChatColor.AQUA + "Usage /CreditCard gui" + ChatColor.AQUA + " <username> " + ChatColor.GOLD + " <four pin code>");
            Util.sendMsg((Player)sender, ChatColor.AQUA + "Usage /CreditCard rewards");
            Util.sendMsg((Player)sender, ChatColor.AQUA + "Usage /CreditCard rewards" + ChatColor.AQUA + " <reward name> " + ChatColor.GOLD + " <four pin code>");
            Util.sendMsg((Player)sender, ChatColor.AQUA + "Usage /CreditCard auto " + ChatColor.GREEN + " <auto pay amount>" + ChatColor.GOLD + " <four pin code>");

            return true;
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("rewards")) {
            String rewardsName = args[1];
            int pin;
            try {
                pin = Integer.valueOf(args[2]);
            }
            catch (NumberFormatException e) {
                Util.sendMsg((Player)sender, " You must enter a number for pin.");
                return true;
            }
            Player player = ((Player) sender).getPlayer();

            this.plugin.getManager().giveReward(rewardsName,player, pin);
            return true;
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("gui")) {
            String playerName = args[1];
            Player player = Bukkit.getPlayer(playerName);
            if (!player.hasPermission("creditcard.use.gui")) {
                Util.sendMsg((Player)sender, ChatColor.RED + " You do not have permission to Use the GUI");
                return true;
            }
            int pin = 0;
            try {
                pin = Integer.valueOf(args[2]);
            }
            catch (NumberFormatException e) {
                Util.sendMsg((Player)sender, " You must enter a number for pin.");
                return true;
            }
            if (player != null) {
                if (player.equals(sender)) {
                    this.plugin.getManager().pickGui(player, pin);
                } else {
                    Util.sendMsg((Player)sender, ChatColor.RED + "The player " + playerName + " is not online or is not real!");
                }
            } else {
                Util.sendMsg((Player)sender,  ChatColor.RED + " YOU ARE NOT THE OWNER");
                if (this.plugin.getFileManager().getHackerDetect() && playerName != null) {
                    Util.sendMsg(player, ChatColor.RED + " PLAYER: " + ChatColor.AQUA + sender.getName() + ChatColor.RED + " IS TRYING TO ACCESS YOUR ACCOUNT ");
                }
            }
            return true;
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("forgot")) {
            String playerName = args[1];
            Player player = Bukkit.getPlayer(playerName);
            this.Password = args[2];
            if (!player.hasPermission("creditcard.use.forgot")) {
                Util.sendMsg((Player)sender,  ChatColor.RED + " You do not have permission to retrieve pin ");
                return true;
            }
            if (player.equals(sender)) {
                if (player != null) {
                    this.plugin.getManager().ForgotPin(player, this.Password);
                } else {
                    Util.sendMsg((Player)sender, ChatColor.RED + "The player " + playerName + " is not online or is not real!");
                }
            } else {
                Util.sendMsg((Player)sender,  ChatColor.RED + " YOU ARE NOT THE OWNER");
                if (this.plugin.getFileManager().getHackerDetect() && playerName != null) {
                    Util.sendMsg(player, ChatColor.RED + " PLAYER: " + ChatColor.AQUA + sender.getName() + ChatColor.RED + " IS TRYING TO ACCESS YOUR ACCOUNT ");
                }
            }
            return true;
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("owe")) {
            String playerName = args[1];
            Player player = Bukkit.getPlayer(playerName);
            if (!player.hasPermission("creditcard.use.owe")) {
                Util.sendMsg((Player)sender,  ChatColor.RED + " You do not have permission to see debt");
                return true;
            }
            if (player != null) {
                if (player.equals(sender)) {
                    this.plugin.getManager().Debt(player);
                } else {
                    Util.sendMsg((Player)sender, ChatColor.RED + "The player " + playerName + " is not online or is not real!");
                }
            } else {
                Util.sendMsg((Player)sender, ChatColor.RED + " YOU ARE NOT THE OWNER");
                if (this.plugin.getFileManager().getHackerDetect() && playerName != null) {
                    Util.sendMsg(player, ChatColor.RED + " PLAYER: " + ChatColor.AQUA + sender.getName() + ChatColor.RED + " IS TRYING TO ACCESS YOUR ACCOUNT ");
                }
            }
            return true;
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("pay")) {
            String playerName = args[1];
            String ans = args[2];
            if(ans.equalsIgnoreCase("all")) {
                Player player = Bukkit.getPlayer(playerName);
                if (!player.hasPermission("creditcard.use.pay")) {
                    Util.sendMsg((Player)sender,  ChatColor.RED + " You do not have permission to pay");
                    return true;
                }
                if (player != null) {
                    if (player.equals(sender)) {
                        double payoff = 0.0;

                        this.plugin.getManager().payOffDebt(player, ans);

                    } else {
                        Util.sendMsg((Player) sender, ChatColor.RED + "The player " + playerName + " is not online or is not real!");
                    }
                } else {
                    Util.sendMsg((Player)sender,  ChatColor.RED + " YOU ARE NOT THE OWNER");
                    if (this.plugin.getFileManager().getHackerDetect() && playerName != null) {
                        Util.sendMsg(player, ChatColor.RED + " PLAYER: " + ChatColor.AQUA + sender.getName() + ChatColor.RED + " IS TRYING TO ACCESS YOUR ACCOUNT ");
                    }
                }
            }
            else {
                Player player = Bukkit.getPlayer(playerName);
                if (!player.hasPermission("creditcard.use.pay")) {
                    Util.sendMsg((Player)sender,  ChatColor.RED + " You do not have permission to pay");
                    return true;
                }
                if (player != null) {
                    if (player.equals(sender)) {
                        double payoff = 0.0;
                        try {
                            payoff = Double.valueOf(args[2]);
                        }
                        catch (NumberFormatException e) {
                            Util.sendMsg((Player) sender, ChatColor.RED + " ERROR: You must enter a number for the pay or the key word \"all\".");
                            return true;
                        }
                        this.plugin.getManager().payOffDebt(player, payoff);
                    } else {
                        Util.sendMsg((Player)sender, ChatColor.RED + "The player " + playerName + " is not online or is not real!");
                    }
                } else {
                    Util.sendMsg((Player)sender,  ChatColor.RED + " YOU ARE NOT THE OWNER");
                    if (this.plugin.getFileManager().getHackerDetect() && playerName != null) {
                        Util.sendMsg(player, ChatColor.RED + " PLAYER: " + ChatColor.AQUA + sender.getName() + ChatColor.RED + " IS TRYING TO ACCESS YOUR ACCOUNT ");
                    }
                }
            }
            return true;
        }


        if (!(sender instanceof Player)) {
            SendConsoleMessage.warning( ChatColor.RED + "You cannot use this command as console!");
            return true;
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("register")) {
            String playerName = args[1];
            Player player = Bukkit.getPlayer(playerName);
            this.Password = args[2];
            if (!player.hasPermission("creditcard.use.reg")) {
                Util.sendMsg((Player)sender,  ChatColor.RED + " You do not have permission to register ");
                return true;
            }
            if (player != null) {
                if (player.equals(sender)) {
                    this.plugin.getManager().register(player, this.Password);
                } else {
                    Util.sendMsg((Player)sender, ChatColor.RED + "The player " + playerName + " is not online or is not real!");
                }
            } else {
                Util.sendMsg(player, ChatColor.RED + " PLAYER: " + ChatColor.AQUA + sender.getName() + ChatColor.RED + " IS TRYING TO ACCESS YOUR ACCOUNT ");
                if (this.plugin.getFileManager().getHackerDetect() && playerName != null) {
                    Util.sendMsg(player, ChatColor.RED + " PLAYER: " + ChatColor.AQUA + sender.getName() + ChatColor.RED + " IS TRYING TO ACCESS YOUR ACCOUNT ");
                }
            }
            return true;
        }
        if (!(sender instanceof Player)) {
            Util.sendMsg((Player)sender, ChatColor.RED + "You cannot use this command as console!");
            return true;
        }
        if (args.length == 4 && args[0].equalsIgnoreCase("loan")) {
            String playerName = args[1];
            Player player = Bukkit.getPlayer(playerName);
            if (!player.hasPermission("creditcard.use.loan")) {
                Util.sendMsg((Player)sender,  ChatColor.RED + "You do not have permission to take a loan");
                return true;
            }
            if (player.equals(sender)) {
                double loan = 0.0;
                try {
                    loan = Double.valueOf(args[2]);
                }
                catch (NumberFormatException e) {
                    Util.sendMsg((Player)sender, ChatColor.RED + " ERROR: You must enter a number for loan.");
                    return true;
                }
                int pin = 0;
                try {
                    pin = Integer.valueOf(args[3]);
                }
                catch (NumberFormatException e) {
                    Util.sendMsg((Player)sender, " You must enter a number for pin.");
                    return true;
                }
                if (player != null) {
                    this.plugin.getManager().request(player, loan, pin);
                } else {
                    Util.sendMsg((Player)sender, ChatColor.RED + "Error : Can not loan.");
                }
            } else {
                Util.sendMsg((Player)sender,  ChatColor.RED + " YOU ARE NOT THE OWNER");
                if (this.plugin.getFileManager().getHackerDetect() && playerName != null) {
                    Util.sendMsg(player, ChatColor.RED + " PLAYER: " + ChatColor.AQUA + sender.getName() + ChatColor.RED + " IS TRYING TO ACCESS YOUR ACCOUNT ");
                }
            }
            return true;
        }
        if (!(sender instanceof Player)) {
            Util.sendMsg((Player)sender, ChatColor.RED + "You cannot use this command as console!");
            return true;
        }
        if (args.length == 3 && args[0].equalsIgnoreCase("auto")) {
            Player player = ((Player) sender).getPlayer();
            if (!player.hasPermission("creditcard.use.auto")) {
                Util.sendMsg((Player)sender,  ChatColor.RED + "You do not have permission to take a loan");
                return true;
            }
            if (player.equals(sender)) {
                double amount = 0.0;
                try {
                    amount = Double.valueOf(args[1]);
                }
                catch (NumberFormatException e) {
                    Util.sendMsg((Player)sender, ChatColor.RED + " ERROR: You must enter a number for loan.");
                    return true;
                }
                int pin = 0;
                try {
                    pin = Integer.valueOf(args[2]);
                }
                catch (NumberFormatException e) {
                    Util.sendMsg((Player)sender, " You must enter a number for pin.");
                    return true;
                }
                if (player != null) {
                    this.plugin.getManager().addAutoAmount(player,amount,pin );
                    //TODO: AUTO PAY METHOD
                } else {
                    Util.sendMsg((Player)sender, ChatColor.RED + "Error : Can not allow auto pay.");
                }
            } else {
                Util.sendMsg((Player)sender,  ChatColor.RED + " YOU ARE NOT THE OWNER");
                if (this.plugin.getFileManager().getHackerDetect() && player.getName() != null) {
                    Util.sendMsg(player, ChatColor.RED + " PLAYER: " + ChatColor.AQUA + sender.getName() + ChatColor.RED + " IS TRYING TO ACCESS YOUR ACCOUNT ");
                }
            }
            return true;
        }

        Util.sendMsg((Player)sender, ChatColor.RED + "Unknown Command! Or you do not have permission");
        return true;
    }


}

