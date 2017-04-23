package com.dogboy602k.CreditCard.main.Main;/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  net.milkbowl.vault.economy.Economy
 *  org.bukkit.Bukkit
 *  org.bukkit.Server
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.PluginCommand
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.RegisteredServiceProvider
 *  org.bukkit.plugin.ServicesManager
 *  org.bukkit.plugin.java.JavaPlugin
 */

import com.dogboy602k.CreditCard.main.Commands.Commands;
import com.dogboy602k.CreditCard.main.Util.FileManager;
import com.dogboy602k.CreditCard.main.Util.Manager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main
extends JavaPlugin {
    private Manager manager;
    private Economy economy = null;
    private FileManager fileManager;
    private Commands Commands;

    public void onEnable() {
        File Configfile;
        this.fileManager = new FileManager(this);
        this.manager = new Manager(this);
        this.Commands = new Commands(this);
        this.setupEconomy();
        this.getCommand("CreditCard").setExecutor(new Commands(this));
        File playerDataFile = new File(this.getDataFolder(), "playerdata.yml");
        if (!playerDataFile.exists()) {
            this.getFileManager().saveDefaultConfiguration(playerDataFile);
        }
        if (!(Configfile = new File(this.getDataFolder(), "config.yml")).exists()) {
            this.saveDefaultConfig();
        }
        this.fileManager.loadPlayerData();
        Bukkit.getPluginManager().registerEvents(this.manager, this);
        this.getManager().addIntrest();
    }

    public void onDisable() {
        this.fileManager.savePlayerData();
    }

    public Manager getManager() {
        return this.manager;
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider economyProvider = this.getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            this.economy = (Economy)economyProvider.getProvider();
        }
        return this.economy != null;
    }

    public Economy getEconomy() {
        return this.economy;
    }

    public FileManager getFileManager() {
        return this.fileManager;
    }

    public Commands getCommands() {
        return this.Commands;
    }
}

