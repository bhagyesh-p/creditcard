package com.dogboy602k.CreditCard.main.Util;

import com.dogboy602k.CreditCard.main.Main.Main;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Created by JARVIS-MAIN on 7/27/2017.
 */
public class ConfigToMsg {

    private Player player;
    private CardInfo cardInfo;
    private Main plugin ;

    public ConfigToMsg(Main plugin){
        this.plugin = plugin;
    }
    public ConfigToMsg( Player player, CardInfo cardInfo){

        this.cardInfo = cardInfo;
        this.player = player;

    }


    /*
    # You can adjust the messages that are sent to players to your
# liking and into any language, please do note for some
# use %player% for player name
# use %debt% for player debt amount
# use %point% for player point amount
# use %pointcost% for reward cost point amount
# use %reward% for reward name
# use %rewarddes% for reward description
# use %bal% for balance
# use %auto% for player auto pay amount
# use %payamount% for player pay amount
# use %maxloanable% for player max loanable amount
# use %loanamount% for player requested loan amount
# use %timeover% for days over the due deadline
# use %daysLeft% for days left till the due deadline
# use %timeover% for days over the due deadline
     */
    public String convertDefaults(String msg){
        if(msg.contains("%player%")){
            msg = msg.replace("%player%", player.getName());
        }
        if(msg.contains("%debt%")){
            msg = msg.replace("%debt%",  String.valueOf( cardInfo.getDebt()) );
        }
        if(msg.contains("%point%")){
            msg = msg.replace("%point%", String.valueOf( cardInfo.getPoints()) );
        }
        if(msg.contains("%bal%")){
            msg = msg.replace("%bal%", String.valueOf(plugin.getEconomy().getBalance(player) ) );
        }
        if(msg.contains("%auto%")){
            msg = msg.replace("%auto%", String.valueOf( cardInfo.getAuto()) );
        }
        return  msg;
    }

    public String convertNonDefaults(String msg,Object replace){
        SendConsoleMessage.debug("think to change: " +replace);
        if(msg.contains("%pointcost%")){
            if(replace instanceof Double){
                msg = msg.replace("%pointcost%", String.valueOf( replace) );
            }
            SendConsoleMessage.debug("Change the cost");

        }
        if(msg.contains("%reward%")){
            msg = msg.replace("%reward%", String.valueOf( replace) );
            SendConsoleMessage.debug("Change the name");

        }
        if(msg.contains("%rewarddes%")){
            msg = msg.replace("%rewarddes%", String.valueOf( replace) );

        }
        if(msg.contains("%payamount%")){
            msg = msg.replace("%payamount%", String.valueOf( replace) );

        }
        if(msg.contains("%maxloanable%")){
            msg = msg.replace("%maxloanable%", String.valueOf( replace) );

        }
        if(msg.contains("%loanamount%")){
            msg = msg.replace("%loanamount%", String.valueOf( replace) );

        }
        if(msg.contains("%timeover%")){
            msg = msg.replace("%timeover%", String.valueOf( replace) );
        }
        if(msg.contains("%daysLeft%")){
            msg = msg.replace("%daysLeft%", String.valueOf( replace) );

        }
        if(msg.contains("%timeover%")){
            msg = msg.replace("%timeover%", String.valueOf( replace) );

        }

        return  msg;
    }

}
