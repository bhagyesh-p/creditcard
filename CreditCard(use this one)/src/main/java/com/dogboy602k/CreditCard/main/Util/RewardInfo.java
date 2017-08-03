package com.dogboy602k.CreditCard.main.Util;

/**
 * Created by JARVIS-MAIN on 7/22/2017.
 */
public class RewardInfo {

    private String name,commands,description;
    private double cost;
    public RewardInfo(String name, String command,double cost,String description){
        this.commands = command;
        this.name = name;
        this.cost = cost;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getCommands() {
        return commands;
    }

    public String getDescription(){
        return description;
    }

    public String getCommandsToUse(String username) {

        return this.commands.replace("%player%", username);
    }

    public double getCost() {
        return cost;
    }

}
