package com.dogboy602k.CreditCard.main.Util;/*
 * Decompiled with CFR 0_118.
 */

import com.dogboy602k.CreditCard.main.Main.Main;

public class MessageManager {
    private Main plugin;

    public MessageManager(Main plugin) {
        this.plugin = plugin;
    }

    public String gethasNotRegistered() {
        return this.plugin.getConfig().getString("hasNotRegistered");
    }

    public String getwrongPassword() {return this.plugin.getConfig().getString("wrongPassword");}

    public String getalreadyRegisterd() {return this.plugin.getConfig().getString("alreadyRegisterd");}

    public String getnotEnoughCashtoPay() {return this.plugin.getConfig().getString("notEnoughCashtoPay");}

    public String getpayAmountOverDebt() {return this.plugin.getConfig().getString("payAmountOverDebt");}

    public String getwrongPin() {return this.plugin.getConfig().getString("wrongPin");}

    public String getmaxLoanReached() {return this.plugin.getConfig().getString("maxLoanReached");}

    public String getmaxPointsReached() {return this.plugin.getConfig().getString("maxPointsReached");}

    public String getwantLoanLargerThanLimit() {return this.plugin.getConfig().getString("wantLoanLargerThanLimit");}

    public String getcommandDidntSayAll() {return this.plugin.getConfig().getString("commandDidntSayAll");}

    public String getdontHaveEnoughToPayAllDebt() {return this.plugin.getConfig().getString("dontHaveEnoughToPayAllDebt");}

    public String getDebtCantBePaidItsZero() {return this.plugin.getConfig().getString("DebtCantBePaidItsZero");}

    public String getpayAmountLessThanZero() {return this.plugin.getConfig().getString("payAmountLessThanZero");}

    public String getnotEnoughPoints() {return this.plugin.getConfig().getString("notEnoughPoints");}

    public String getloanLessthanZero() {return this.plugin.getConfig().getString("loanLessthanZero");}

    public String getfoundPin() {return this.plugin.getConfig().getString("foundPin");}

    public String getregistered() {return this.plugin.getConfig().getString("registered");}

    public String getautoPayMSG() {return this.plugin.getConfig().getString("autoPayMSG");}

    public String getmoneyIsBeingAdded() {return this.plugin.getConfig().getString("moneyIsBeingAdded");}

    public String getnewTotalPoints() {return this.plugin.getConfig().getString("newTotalPoints");}

    public String getyouHaveZeroDebtpPayInZeroDays() {return this.plugin.getConfig().getString("youHaveZeroDebtpPayInZeroDays");}

    public String getoverDeadLine() {return this.plugin.getConfig().getString("overDeadLine");}

    public String getyouOwnAmountInDays() {return this.plugin.getConfig().getString("youOwnAmountInDays");}

    public String getpleaseWait() {return this.plugin.getConfig().getString("pleaseWait");}

    public String gethowMuchIsPaidOff() {return this.plugin.getConfig().getString("howMuchIsPaidOff");}

    public String getyouHavePoints() {return this.plugin.getConfig().getString("youHavePoints");}

    public String getpointsDescription() {return this.plugin.getConfig().getString("pointsDescription");}

    public String getyouGotReward() {return this.plugin.getConfig().getString("youGotReward");}

    public String getclosedGUIMenu() {return this.plugin.getConfig().getString("closedGUIMenu");}

    public String getpointsYouCanGet() {return this.plugin.getConfig().getString("pointsYouCanGet");}

    public String getcouldNotFindReward() {return this.plugin.getConfig().getString("couldNotFindReward");}

    public String getjoinMSGNoDebt() {return this.plugin.getConfig().getString("joinMSGNoDebt");}

    public String getjoinMSG() {return this.plugin.getConfig().getString("joinMSG");}
}

