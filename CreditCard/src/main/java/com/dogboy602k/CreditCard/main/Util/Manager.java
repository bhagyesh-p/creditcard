package com.dogboy602k.CreditCard.main.Util;


import com.dogboy602k.CreditCard.main.Main.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Manager
implements Listener {
    private Main plugin;
    private MessageManager mm;
    private Player p;
    private List<MaxLoan> loan;
    private List<CardInfo> pin = new ArrayList<CardInfo>();
    private int pinHolder;
    private double debt;
    private String PasswordHolder;
    private Double loanamount1 = 0.0;
    private Double payoff1 = 0.0;
    private int Gperspin = 0;
    private Player Gowner = null;
    private ArrayList<UUID> UUIDs = new ArrayList();
    private ArrayList<Player > overDuePlayersON = new ArrayList<Player>();
    private ArrayList<OfflinePlayer > overDuePlayersOFF = new ArrayList<OfflinePlayer>();

    public Manager(Main plugin) {
        this.plugin = plugin;
        this.loan = new ArrayList<MaxLoan>();
    }

    public void addArray(ArrayList<UUID> list){
        for(UUID a : list){

            UUIDs.add(a);
        }
    }

    public CardInfo getCard(UUID owner) {
        for (CardInfo cardInfo : this.pin) {
            if (!cardInfo.getOwner().equals(owner)) continue;
            return cardInfo;
        }
        return null;
    }

    public CardInfo getPinForgot(UUID owner) {
        for (CardInfo cardInfo : this.pin) {
            if (!cardInfo.getOwner().equals(owner)) continue;
            return cardInfo;
        }
        return null;
    }

    public void ForgotPin(Player owner, String password) {
        UUID ownerUUID = owner.getUniqueId();
        if (!this.hasRegistered(ownerUUID)) {
            Util.sendMsg(owner, ChatColor.RED + "[ERROR] " + ChatColor.GREEN + "You have not been registered, please register first, Thank You.");
            return;
        }
        CardInfo cardInfo = this.getPinForgot(owner.getUniqueId());
        if (cardInfo == null) {
            return;
        }
        CardInfo cardInfoPass = this.getCard(ownerUUID);
        if (password.equalsIgnoreCase(cardInfoPass.getPassword())) {
            Util.sendMsg(owner, ChatColor.GREEN + "HEY luckily the IT guys found you pin:");
            Util.sendMsg(owner, ChatColor.AQUA + "" + cardInfo.getpin() + "");
        } else {
            Util.sendMsg(owner, ChatColor.RED + " [ERROR] " + ChatColor.GREEN + "The password you have entered is invalid.");
        }
    }

    public boolean hasRegistered(UUID playerUUID) {
        int count = 0;
        for(UUID a: UUIDs){
            if(playerUUID.equals(a)){
                count++;
            }
        }
        if(count > 0){
            return true;
        }
        else {
            return false;
        }
    }

    public int generatePIN() {
        int randomPIN = (int) ((Math.random() * 9000.0) + 1000);
        return randomPIN;
    }

    public void register(Player owner, String p) {
        UUID ownerUUID = owner.getUniqueId();
        if (this.hasRegistered(ownerUUID)) {
            Util.sendMsg(owner, ChatColor.RED + " [ERROR] You have a card already ");
            return;
        }
        String ownerName = owner.getName();
        this.PasswordHolder = p;
        this.pinHolder = this.generatePIN();
        long loanTimestamp = 0;
        long intrestUpdateDate = 0;
        UUIDs.add(ownerUUID);
        this.pin.add(new CardInfo(ownerUUID, this.pinHolder, ownerName, this.PasswordHolder, this.debt, loanTimestamp, intrestUpdateDate));
        Util.sendMsg(owner, ChatColor.GREEN + "You have been registered, your pin is: " + ChatColor.GOLD + this.pinHolder);
    }

    public void request(Player owner, double loanamount, int perspin) {
        UUID ownerUUID;
        double maxloana = 0.0;
        if (owner.hasPermission("creditcard.use.loan.rankone")) {
            maxloana = this.plugin.getFileManager().getMaxLoanRankOne();
        }
        if (owner.hasPermission("creditcard.use.loan.defaultamount")) {
            maxloana = this.plugin.getFileManager().getMaxLoanRankDefault();
        }
        if (owner.hasPermission("creditcard.use.loan.ranktwo")) {
            maxloana = this.plugin.getFileManager().getMaxLoanRankTwo();
        }
        if (owner.hasPermission("creditcard.use.loan.rankthree")) {
            maxloana = this.plugin.getFileManager().getMaxLoanRankThree();
        }
        if (owner.hasPermission("creditcard.use.loan.rankfour")) {
            maxloana = this.plugin.getFileManager().getMaxLoanRankFour();
        }
        if (owner.hasPermission("creditcard.use.loan.rankfive")) {
            maxloana = this.plugin.getFileManager().getMaxLoanRankFive();
        }
        if (owner.hasPermission("creditcard.use.loan.ranksix")) {
            maxloana = this.plugin.getFileManager().getMaxLoanRankSix();
        }
        if (!this.hasRegistered(ownerUUID = owner.getUniqueId())) {
            Util.sendMsg(owner, ChatColor.RED + "[ERROR] " + ChatColor.GREEN + "You have not been registered, please register first, Thank You.");
            return;
        }
        CardInfo cardInfo = this.getCard(ownerUUID);
        int pin = cardInfo.getpin();
        if (pin != perspin) {
            Util.sendMsg(owner, ChatColor.RED + " [ERROR] Pin does not match original pin.");
            Util.sendMsg(owner, ChatColor.GREEN + "If you have forgotten your pin please do /creditcard forgot.");
            return;
        }
        if (pin == perspin) {
            double debtAmount = cardInfo.getDebt();
            double seeIfDebtIsOk = debtAmount += loanamount;
            if (seeIfDebtIsOk <= maxloana) {
                if (this.checkmaxlimit(loanamount, owner)) {
                    if (cardInfo.getDebt() == maxloana || cardInfo.getDebt() > maxloana) {
                        Util.sendMsg(owner, ChatColor.GOLD + "[CC]" + ChatColor.RED + " [ERROR] you have reached you max loan limit of : " + ChatColor.GREEN + "$" + maxloana);
                        return;
                    }
                    Util.sendMsg(owner, ChatColor.GOLD + "Money is being added to your account : " + ChatColor.GREEN + "$" + loanamount);
                    if(this.getCard(ownerUUID).getloanTimestamp() == 0 ) {
                        this.getCard(ownerUUID).setloanTimestamp(System.currentTimeMillis() / 1000);
                    }
                    this.plugin.getEconomy().depositPlayer(owner, loanamount);
                    cardInfo.setDebt(cardInfo.getDebt() + loanamount);
                }
            } else {
                Util.sendMsg(owner, ChatColor.RED + " [ERROR] loan amount is larger than your limit , your limit is: " + ChatColor.GREEN + "$" + maxloana);
            }
            return;
        }
    }

    public void Debt(Player owner) {
        UUID ownerUUID = owner.getUniqueId();
        if (!this.hasRegistered(ownerUUID)) {
            Util.sendMsg(owner, ChatColor.RED + "[ERROR] " + ChatColor.GREEN + "You have not been registered, please register first, Thank You.");
            return;
        }
        CardInfo cardInfo = this.getCard(ownerUUID);
        double debtamount = cardInfo.getDebt();
        LocalDate now = LocalDate.now();
        long dayOfLoan =getCard(ownerUUID).getloanTimestamp();
        long chargeDate = this.plugin.getFileManager().getperdays();

        int DOL = this.convertion(dayOfLoan);

        int differance = (DOL+30) - now.getDayOfYear();

        if(differance == 0 || debtamount == 0){
            Util.sendMsg(owner, ChatColor.AQUA + "You owe: " + ChatColor.GREEN + "$" + ChatColor.GREEN + debtamount + ChatColor.AQUA + " you have " + ChatColor.RED + "0" + ChatColor.AQUA + " day(s) to pay up");
        }
        else if( differance > (DOL+30) || differance < 0) {
            int timeOver = differance - (int)chargeDate;
            Util.sendMsg(owner, ChatColor.AQUA + "You are over the deadline date ,you now owe: " + ChatColor.GREEN + "$" + ChatColor.GREEN + debtamount + ChatColor.AQUA + " you are " + ChatColor.RED + timeOver + ChatColor.AQUA + " day(s) over");//       } else {
        }
        else if(differance < (DOL+30) ){
            this.getCard(ownerUUID).setintrestUpdateDate(System.currentTimeMillis()/1000);
            int daysLeft =  differance ;
            Util.sendMsg(owner, ChatColor.AQUA + "You owe: " + ChatColor.GREEN + "$" + ChatColor.GREEN + debtamount + ChatColor.AQUA + " you have " + ChatColor.RED + daysLeft + ChatColor.AQUA + " day(s) to pay up");
        }
    }

    public void payOffDebt(Player owner, String all) {
        if(all.equalsIgnoreCase("all")){
            Util.sendMsg(owner, "Please wait a moment...");
            SendConsoleMessage.debug("IN PAY DEBT METHOD");
        }
        else {
            Util.sendMsg(owner, "Error command did not say all");
            return;
        }
        UUID ownerUUID = owner.getUniqueId();
        if (!this.hasRegistered(ownerUUID)) {
            Util.sendMsg(owner, ChatColor.RED + "[ERROR] " + ChatColor.GREEN + "You have not been registered, please register first, Thank You.");
            return;
        }
        CardInfo cardInfo = this.getCard(ownerUUID);
        double debtAmount = cardInfo.getDebt();
        double payOff = debtAmount;
        if (payOff > this.plugin.getEconomy().getBalance(owner)) {
            Util.sendMsg(owner, ChatColor.RED + "You do not have " + payOff + " in your account.");
            return;
        }
        if (this.plugin.getEconomy().getBalance(owner) <= payOff) {
            Util.sendMsg(owner, ChatColor.GREEN + " Your bal is less than the amount you would like to pay.");
            return;
        }
        if (this.plugin.getEconomy().getBalance(owner) < payOff) {
            return;
        }
        if (debtAmount == 0.0) {
            Util.sendMsg(owner, ChatColor.GREEN + "You can not pay any more your debt is : $0");
            return;
        }
        if (payOff > debtAmount) {
            Util.sendMsg(owner, ChatColor.RED + "Your pay amount is higher than you debt.");
            return;
        }
        if (payOff == debtAmount) {
            this.getCard(ownerUUID).setloanTimestamp(0);

                for (int i = overDuePlayersON.size() - 1; i >= 0; i--) {
                    if (overDuePlayersON.get(i).getName().equals(p.getName())) {
                        overDuePlayersON.remove(i);
                    }
                }

        }
        if (payOff < 0.0 || payOff == 0.0) {
            Util.sendMsg(owner, ChatColor.RED + "Your pay amount is less than 0");
            return;
        }
        Util.sendMsg(owner, ChatColor.GREEN + "$" + payOff + " Has been paid off.");
        this.plugin.getEconomy().withdrawPlayer(owner, payOff);
        cardInfo.setDebt(debtAmount - payOff);


    }

    public void payOffDebt(Player owner, double payOff) {
        UUID ownerUUID = owner.getUniqueId();
        if (!this.hasRegistered(ownerUUID)) {
            Util.sendMsg(owner, ChatColor.RED + "[ERROR] " + ChatColor.GREEN + "You have not been registered, please register first, Thank You.");
            return;
        }
        CardInfo cardInfo = this.getCard(ownerUUID);
        double debtAmount = cardInfo.getDebt();
        if (payOff > this.plugin.getEconomy().getBalance(owner)) {
            Util.sendMsg(owner, ChatColor.RED + "You do not have " + payOff + " in your account.");
            return;
        }
        if (this.plugin.getEconomy().getBalance(owner) <= payOff) {
            Util.sendMsg(owner, ChatColor.GREEN + " Your bal is less than the amount you would like to pay.");
            return;
        }
        if (this.plugin.getEconomy().getBalance(owner) < payOff) {
            return;
        }
        if (debtAmount == 0.0) {
            Util.sendMsg(owner, ChatColor.GREEN + "You can not pay any more your debt is : $0");
            return;
        }
        if (payOff > debtAmount) {
            Util.sendMsg(owner, ChatColor.RED + "Your pay amount is higher than you debt.");
            return;
        }
        if (payOff == debtAmount) {
            this.getCard(ownerUUID).setloanTimestamp(0);
            for (int i = overDuePlayersON.size() - 1; i >= 0; i--) {
                if (overDuePlayersON.get(i).getName().equals(p.getName())) {
                    overDuePlayersON.remove(i);
                }
            }
        }
        if (payOff < 0.0 || payOff == 0.0) {
            Util.sendMsg(owner, ChatColor.RED + "Your pay amount is less than 0");
            return;
        }
        Util.sendMsg(owner, ChatColor.GREEN + "$" + payOff + " Has been paid off.");
        this.plugin.getEconomy().withdrawPlayer(owner, payOff);
        cardInfo.setDebt(debtAmount - payOff);


    }

    public boolean checkmaxlimit(double loanamount, Player owner) {
        double maxloana;
        boolean returnValue = false;
        if (owner.hasPermission("creditcard.use.loan.defaultamount")) {
            maxloana = this.plugin.getFileManager().getMaxLoanRankDefault();
            boolean bl = returnValue = loanamount <= maxloana;
        }
        if (owner.hasPermission("creditcard.use.loan.rankone")) {
            maxloana = this.plugin.getFileManager().getMaxLoanRankOne();
            boolean bl = returnValue = loanamount <= maxloana;
        }
        if (owner.hasPermission("creditcard.use.loan.ranktwo")) {
            maxloana = this.plugin.getFileManager().getMaxLoanRankTwo();
            boolean bl = returnValue = loanamount <= maxloana;
        }
        if (owner.hasPermission("creditcard.use.loan.rankthree")) {
            maxloana = this.plugin.getFileManager().getMaxLoanRankThree();
            boolean bl = returnValue = loanamount <= maxloana;
        }
        if (owner.hasPermission("creditcard.use.loan.rankfour")) {
            maxloana = this.plugin.getFileManager().getMaxLoanRankFour();
            boolean bl = returnValue = loanamount <= maxloana;
        }
        if (owner.hasPermission("creditcard.use.loan.rankfive")) {
            maxloana = this.plugin.getFileManager().getMaxLoanRankFive();
            boolean bl = returnValue = loanamount <= maxloana;
        }
        if (owner.hasPermission("creditcard.use.loan.ranksix")) {
            maxloana = this.plugin.getFileManager().getMaxLoanRankSix();
            returnValue = loanamount <= maxloana;
        }
        return returnValue;
    }

    public List<CardInfo> getCardinfo() {
        return this.pin;
    }

    public void addCardInfo(CardInfo cardInfo) {
        this.pin.add(cardInfo);
    }

    public  int convertion(long time) {
        String date = new java.text.SimpleDateFormat("DD").format(new java.util.Date (time*1000));
        int convTime = Integer.parseInt(date);

        return convTime;
    }

    public void requestGui(Player owner, int perspin) {
        UUID ownerUUID = owner.getUniqueId();
        double ButtonAdd1 = this.plugin.getFileManager().getButtonAdd1();
        double ButtonAdd2 = this.plugin.getFileManager().getButtonAdd2();
        double ButtonSubtract1 = this.plugin.getFileManager().getButtonSubtract1();
        double ButtonSubtract2 = this.plugin.getFileManager().getButtonSubtract2();
        if (hasRegistered(ownerUUID)== false) {
            Util.sendMsg(owner, ChatColor.RED + "[ERROR] " + ChatColor.GREEN + "You have not been registered, please register first, Thank You.");
            return;
        }
        CardInfo cardInfo = this.getCard(ownerUUID);

        int pin = cardInfo.getpin();
        if (pin != perspin) {
            Util.sendMsg(owner, ChatColor.RED + " [ERROR] Pin does not match original pin.");
            Util.sendMsg(owner, ChatColor.GREEN + "If you have forgotten your pin please do /creditcard forgot.");
            return;
        }
        if (pin == perspin) {
            ItemStack add1 = new ItemStack(Material.STAINED_GLASS_PANE, 1,(short) 13);
            ItemMeta add1meta = add1.getItemMeta();
            add1meta.setDisplayName(ChatColor.GREEN + "ADD " + ButtonAdd1 + " dollars");
            add1.setItemMeta(add1meta);
            ItemStack add2 = new ItemStack(Material.STAINED_GLASS_PANE, 1,(short) 13);
            ItemMeta add2meta = add2.getItemMeta();
            add2meta.setDisplayName(ChatColor.GREEN + "ADD " + ButtonAdd2 + " dollars");
            add2.setItemMeta(add2meta);
            ItemStack sub1 = new ItemStack(Material.STAINED_GLASS_PANE, 1,(short) 14);
            ItemMeta sub1meta = sub1.getItemMeta();
            sub1meta.setDisplayName(ChatColor.RED + "Subtract " + ButtonSubtract1 + " dollars");
            sub1.setItemMeta(sub1meta);
            ItemStack sub2 = new ItemStack(Material.STAINED_GLASS_PANE, 1,(short) 14);
            ItemMeta sub2meta = sub2.getItemMeta();
            sub2meta.setDisplayName(ChatColor.RED + "Subtract " + ButtonSubtract2 + " dollars");
            sub2.setItemMeta(sub2meta);
            ItemStack cancel = new ItemStack(Material.BARRIER);
            ItemMeta cancelmeta = cancel.getItemMeta();
            cancelmeta.setDisplayName(ChatColor.RED + "Cancel");
            cancel.setItemMeta(cancelmeta);
            ItemStack confirm = new ItemStack(Material.SIGN);
            ItemMeta confirmmeta = confirm.getItemMeta();
            confirmmeta.setDisplayName(ChatColor.GREEN + "Confirm");
            confirm.setItemMeta(confirmmeta);
            Inventory loanInv = Bukkit.createInventory(null, 45, (ChatColor.GOLD + "CREDIT CARD LOAN GUI"));
            loanInv.setItem(9, add1);
            loanInv.setItem(18, add2);
            loanInv.setItem(17, sub1);
            loanInv.setItem(26, sub2);
            loanInv.setItem(36, cancel);
            loanInv.setItem(22, confirm);
            owner.openInventory(loanInv);
        }
    }

    public void payGui(Player owner, int perspin) {
        UUID ownerUUID = owner.getUniqueId();
        double ButtonAdd1 = this.plugin.getFileManager().getButtonAdd1();
        double ButtonAdd2 = this.plugin.getFileManager().getButtonAdd2();
        double ButtonSubtract1 = this.plugin.getFileManager().getButtonSubtract1();
        double ButtonSubtract2 = this.plugin.getFileManager().getButtonSubtract2();
        if (hasRegistered(ownerUUID) == false) {
            Util.sendMsg(owner, ChatColor.RED + "[ERROR] " + ChatColor.GREEN + "You have not been registered, please register first, Thank You.");
            return;
        }
        CardInfo cardInfo = this.getCard(ownerUUID);

        int pin = cardInfo.getpin();
        if (pin != perspin) {
            Util.sendMsg(owner, ChatColor.RED + " [ERROR] Pin does not match original pin.");
            Util.sendMsg(owner, ChatColor.GREEN + "If you have forgotten your pin please do /creditcard forgot.");
            return;
        }
        if (pin == perspin) {
            ItemStack add1 = new ItemStack(Material.STAINED_GLASS_PANE, 1,(short) 13);
            ItemMeta add1meta = add1.getItemMeta();
            add1meta.setDisplayName(ChatColor.GREEN + "ADD " + ButtonAdd1 + " dollars");
            add1.setItemMeta(add1meta);
            ItemStack add2 = new ItemStack(Material.STAINED_GLASS_PANE, 1,(short) 13);
            ItemMeta add2meta = add2.getItemMeta();
            add2meta.setDisplayName(ChatColor.GREEN + "ADD " + ButtonAdd2 + " dollars");
            add2.setItemMeta(add2meta);
            ItemStack sub1 = new ItemStack(Material.STAINED_GLASS_PANE, 1,(short) 14);
            ItemMeta sub1meta = sub1.getItemMeta();
            sub1meta.setDisplayName(ChatColor.RED + "Subtract " + ButtonSubtract1 + " dollars");
            sub1.setItemMeta(sub1meta);
            ItemStack sub2 = new ItemStack(Material.STAINED_GLASS_PANE, 1,(short) 14);
            ItemMeta sub2meta = sub2.getItemMeta();
            sub2meta.setDisplayName(ChatColor.RED + "Subtract " + ButtonSubtract2 + " dollars");
            sub2.setItemMeta(sub2meta);
            ItemStack cancel = new ItemStack(Material.BARRIER);
            ItemMeta cancelmeta = cancel.getItemMeta();
            cancelmeta.setDisplayName(ChatColor.RED + "Cancel");
            cancel.setItemMeta(cancelmeta);
            ItemStack confirm = new ItemStack(Material.SIGN);
            ItemMeta confirmmeta = confirm.getItemMeta();
            confirmmeta.setDisplayName(ChatColor.GREEN + "Confirm");
            confirm.setItemMeta(confirmmeta);
            Inventory loanInv = Bukkit.createInventory(null, 45, (ChatColor.GOLD + "CREDIT CARD PAY GUI"));
            loanInv.setItem(9, add1);
            loanInv.setItem(18, add2);
            loanInv.setItem(17, sub1);
            loanInv.setItem(26, sub2);
            loanInv.setItem(36, cancel);
            loanInv.setItem(22, confirm);
            owner.openInventory(loanInv);
        }
    }

    public void pickGui(Player owner, int perspin) {
        UUID ownerUUID = owner.getUniqueId();

        if (hasRegistered(ownerUUID) == false) {
            Util.sendMsg(owner, ChatColor.RED + "[ERROR] " + ChatColor.GREEN + "You have not been registered, please register first, Thank You.");
            return;
        }
        CardInfo cardInfo = this.getCard(ownerUUID);

        int pin = cardInfo.getpin();
        if (pin != perspin) {
            Util.sendMsg(owner, ChatColor.RED + " [ERROR] Pin does not match original pin.");
            Util.sendMsg(owner, ChatColor.GREEN + "If you have forgotten your pin please do /creditcard forgot.");
            return;
        }
        else {
            ItemStack loan = new ItemStack(Material.DIAMOND);
            ItemMeta loan1meta = loan.getItemMeta();
            loan1meta.setDisplayName(ChatColor.GREEN + "Take a loan");
            loan.setItemMeta(loan1meta);
            ItemStack pay = new ItemStack(Material.GOLD_INGOT);
            ItemMeta paymeta = pay.getItemMeta();
            paymeta.setDisplayName(ChatColor.GREEN + "Pay your loan");
            pay.setItemMeta(paymeta);
            Inventory pickInv = Bukkit.createInventory(null, 9, (ChatColor.GOLD + "CREDIT CARD GUI PICKER"));
            pickInv.setItem(3, loan);
            pickInv.setItem(5, pay);
            owner.openInventory(pickInv);
            this.Gowner = owner;
            this.Gperspin = perspin;
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        double add2;
        double subtract1;
        double subtract2;
        double add1;
        if (e.getInventory().getTitle().equals(ChatColor.GOLD + "CREDIT CARD GUI PICKER")) {
            if (e.getRawSlot() == 3) {
                e.setCancelled(true);
                this.requestGui(this.Gowner, this.Gperspin);
            }
            if (e.getRawSlot() == 5) {
                e.setCancelled(true);
                this.payGui(this.Gowner, this.Gperspin);
            }
        }
        if (e.getInventory().getTitle().equals(ChatColor.GOLD + "CREDIT CARD PAY GUI")) {
            UUID ownerUUID = e.getWhoClicked().getUniqueId();
            add1 = this.plugin.getFileManager().getButtonAdd1();
            add2 = this.plugin.getFileManager().getButtonAdd2();
            subtract1 = this.plugin.getFileManager().getButtonSubtract1();
            subtract2 = this.plugin.getFileManager().getButtonSubtract2();
            if (e.getRawSlot() == 9) {
                e.setCancelled(true);
                this.payoff1 = this.payoff1 + add1;
            }
            if (e.getRawSlot() == 18) {
                e.setCancelled(true);
                this.payoff1 = this.payoff1 + add2;
            }
            if (e.getRawSlot() == 17) {
                e.setCancelled(true);
                this.payoff1 = this.payoff1 - subtract1;
            }
            if (e.getRawSlot() == 26) {
                e.setCancelled(true);
                this.payoff1 = this.payoff1 - subtract2;
            }
            if (e.getRawSlot() == 36) {
                Util.sendMsg(e.getWhoClicked(), ChatColor.GOLD + "[CC] You have closed the GUI menu");
                e.getWhoClicked().closeInventory();
            }
            if (e.getRawSlot() == 22) {
                if (!this.hasRegistered(ownerUUID)) {
                    Util.sendMsg(e.getWhoClicked(), ChatColor.RED + "[ERROR] " + ChatColor.GREEN + "You have not been registered, please register first, Thank You.");
                    e.getWhoClicked().closeInventory();
                    this.payoff1 = 0.0;
                    return;
                }
                CardInfo cardInfo = this.getCard(ownerUUID);
                double debtAmount = cardInfo.getDebt();
                if (this.payoff1 > this.plugin.getEconomy().getBalance(this.Gowner)) {
                    Util.sendMsg(this.Gowner, ChatColor.RED + "You do not have " + this.payoff1 + " in your account.");
                    e.getWhoClicked().closeInventory();
                    this.payoff1 = 0.0;
                    return;
                }
                if (this.plugin.getEconomy().getBalance(this.Gowner) <= this.payoff1) {
                    Util.sendMsg(this.Gowner, ChatColor.GREEN + " Your bal is less than the amount you would like to pay.");
                    e.getWhoClicked().closeInventory();
                    this.payoff1 = 0.0;
                    return;
                }
                if (this.plugin.getEconomy().getBalance(this.Gowner) < this.payoff1) {
                    e.getWhoClicked().closeInventory();
                    this.payoff1 = 0.0;
                    return;
                }
                if (debtAmount == 0.0) {
                    Util.sendMsg(this.Gowner, ChatColor.GREEN + "You can not pay any more your debt is : $0");
                    e.getWhoClicked().closeInventory();
                    this.payoff1 = 0.0;
                    return;
                }
                if (this.payoff1 < 0.0 || this.payoff1 == 0.0) {
                    Util.sendMsg(this.Gowner, ChatColor.RED + "Your pay amount is less than 0.");
                    e.getWhoClicked().closeInventory();
                    this.payoff1 = 0.0;
                    return;
                }
                if (this.payoff1 > debtAmount) {
                    Util.sendMsg(this.Gowner, ChatColor.RED + "Your pay amount is higher than you debt.");
                    e.getWhoClicked().closeInventory();
                    this.payoff1 = 0.0;
                    return;
                }
                if (this.payoff1 == debtAmount) {
                    this.getCard(ownerUUID).setloanTimestamp(0);
                }
                Util.sendMsg(this.Gowner, ChatColor.GREEN + "$" + this.payoff1 + " Has been paid off.");
                this.plugin.getEconomy().withdrawPlayer(this.Gowner, this.payoff1.doubleValue());
                cardInfo.setDebt(debtAmount - this.payoff1);
                e.getWhoClicked().closeInventory();
                this.payoff1 = 0.0;
            }
        }
        if (e.getInventory().getTitle().equals(ChatColor.GOLD + "CREDIT CARD LOAN GUI")) {
            Player playerName = (Player)e.getWhoClicked();
            add1 = this.plugin.getFileManager().getButtonAdd1();
            add2 = this.plugin.getFileManager().getButtonAdd2();
            subtract1 = this.plugin.getFileManager().getButtonSubtract1();
            subtract2 = this.plugin.getFileManager().getButtonSubtract2();
            if (e.getRawSlot() == 9) {
                e.setCancelled(true);
                this.loanamount1 = this.loanamount1 + add1;
            }
            if (e.getRawSlot() == 18) {
                e.setCancelled(true);
                this.loanamount1 = this.loanamount1 + add2;
            }
            if (e.getRawSlot() == 17) {
                e.setCancelled(true);
                this.loanamount1 = this.loanamount1 - subtract1;
            }
            if (e.getRawSlot() == 26) {
                e.setCancelled(true);
                this.loanamount1 = this.loanamount1 - subtract2;
            }
            if (e.getRawSlot() == 36) {
                Util.sendMsg(e.getWhoClicked(), ChatColor.GOLD + "[CC] You have closed the GUI menu");
                e.getWhoClicked().closeInventory();
            }
            double maxloana = 0.0;
            if (playerName.hasPermission("creditcard.use.loan.rankone")) {
                maxloana = this.plugin.getFileManager().getMaxLoanRankOne();
            }
            if (playerName.hasPermission("creditcard.use.loan.defaultamount")) {
                maxloana = this.plugin.getFileManager().getMaxLoanRankDefault();
            }
            if (playerName.hasPermission("creditcard.use.loan.ranktwo")) {
                maxloana = this.plugin.getFileManager().getMaxLoanRankTwo();
            }
            if (playerName.hasPermission("creditcard.use.loan.rankthree")) {
                maxloana = this.plugin.getFileManager().getMaxLoanRankThree();
            }
            if (playerName.hasPermission("creditcard.use.loan.rankfour")) {
                maxloana = this.plugin.getFileManager().getMaxLoanRankFour();
            }
            if (playerName.hasPermission("creditcard.use.loan.rankfive")) {
                maxloana = this.plugin.getFileManager().getMaxLoanRankFive();
            }
            if (playerName.hasPermission("creditcard.use.loan.ranksix")) {
                maxloana = this.plugin.getFileManager().getMaxLoanRankSix();
            }
            if (e.getRawSlot() == 22) {
                e.setCancelled(true);
                UUID ownerUUID = e.getWhoClicked().getUniqueId();
                CardInfo cardInfo = this.getCard(ownerUUID);
                int pin = cardInfo.getpin();
                double debtAmount = cardInfo.getDebt();
                double seeIfDebtIsOk = debtAmount += this.loanamount1.doubleValue();
                if (seeIfDebtIsOk > maxloana) {
                    Util.sendMsg(e.getWhoClicked(), ChatColor.GOLD + "[CC]" + ChatColor.RED + " [ERROR] loan amount is larger than your limit , your limit is: " + ChatColor.GREEN + "$" + maxloana);
                    e.getWhoClicked().closeInventory();
                    this.loanamount1 = 0.0;
                    return;
                }
                if (!this.checkmaxlimit(this.loanamount1, playerName)) {
                    Util.sendMsg(e.getWhoClicked(), ChatColor.GOLD + "[CC]" + ChatColor.RED + " [ERROR] Over max limit");
                    e.getWhoClicked().closeInventory();
                    this.loanamount1 = 0.0;
                    return;
                }
                if (cardInfo.getDebt() == maxloana || cardInfo.getDebt() > maxloana) {
                    Util.sendMsg(e.getWhoClicked(), ChatColor.GOLD + "[CC]" + ChatColor.RED + " [ERROR] you have reached you max loan limit of : " + ChatColor.GREEN + "$" + maxloana);
                    e.getWhoClicked().closeInventory();
                    this.loanamount1 = 0.0;
                    return;
                }
                if (this.loanamount1 != 0.0 && this.loanamount1 >= 0.0) {
                    Util.sendMsg(e.getWhoClicked(), ChatColor.GOLD + "[CC]" + ChatColor.GOLD + " Money is being added to your account : " + ChatColor.GREEN + "$" + this.loanamount1);
                    if(this.getCard(ownerUUID).getloanTimestamp() == 0 ) {
                        this.getCard(ownerUUID).setloanTimestamp(System.currentTimeMillis());
                    }
                    this.plugin.getEconomy().depositPlayer(e.getWhoClicked().getName(), this.loanamount1.doubleValue());
                    cardInfo.setDebt(cardInfo.getDebt() + this.loanamount1);
                    e.getWhoClicked().closeInventory();
                    this.loanamount1 = 0.0;
                    return;
                }
                Util.sendMsg(e.getWhoClicked(), ChatColor.GOLD + "[CC]" + ChatColor.RED + " [ERROR] loan amount less then 0");
                e.getWhoClicked().closeInventory();
                this.loanamount1 = 0.0;
                return;
            }
        }
    }

    @EventHandler
    public void onCloseInventory(InventoryCloseEvent e) {
        if (e.getInventory().getTitle().equals(ChatColor.GOLD + "CREDIT CARD LOAN GUI")) {
            this.loanamount1 = 0.0;
        }
        if (e.getInventory().getTitle().equals(ChatColor.GOLD + "CREDIT CARD GUI PICKER")) {
            this.payoff1 = 0.0;
        }
    }

    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        Player player = e.getPlayer();
        UUID ownerUUID = player.getUniqueId();
        if(hasRegistered(ownerUUID)) {
            CardInfo cardInfo = this.getCard(ownerUUID);
            final double debtamount = cardInfo.getDebt();
            if (debtamount > 0.0) {
                e.setJoinMessage(ChatColor.GOLD + "[CC]" + ChatColor.GREEN + "=========[Credit Card]=========\n" + ChatColor.GOLD + "\n[CC]" + ChatColor.GREEN + " You own $" + debtamount + ChatColor.GOLD + "\n[CC]" + ChatColor.GREEN + "=========[Credit Card]=========");
                for(int i = overDuePlayersOFF.size()-1; i <= 0; i++){
                    if(overDuePlayersOFF.get(i).getName().equals(p.getName())){
                        overDuePlayersON.add(player);
                        overDuePlayersOFF.remove(i);
                    }
                }


            } else {
                e.setJoinMessage(ChatColor.GOLD + "[CC]" + ChatColor.GREEN + " You do not owe any debt ");
            }
        }
        else{
            Util.sendMsg(e.getPlayer(), ChatColor.RED + "[ERROR] " + ChatColor.GREEN + "You have not been registered, please register first, Thank You.");

        }
    }

    @EventHandler
    public void onExit(final PlayerQuitEvent e) {
        Player player = e.getPlayer();
        UUID ownerUUID = player.getUniqueId();
        if(hasRegistered(ownerUUID)) {
            CardInfo cardInfo = this.getCard(ownerUUID);
            final double debtamount = cardInfo.getDebt();
            if (debtamount > 0.0) {
                for(int i = overDuePlayersON.size()-1; i <= 0; i++){
                    if(overDuePlayersON.get(i).getName().equals(p.getName())){
                        overDuePlayersOFF.add(player);
                        overDuePlayersON.remove(i);
                    }
                }


            }
        }
        else{
            Util.sendMsg(e.getPlayer(), ChatColor.RED + "[ERROR] " + ChatColor.GREEN + "You have not been registered, please register first, Thank You.");

        }
    }

    public void addIntrest() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
                public void run(){
                LocalDate now = LocalDate.now();

                for(UUID u : UUIDs){

                    if(Bukkit.getPlayer(u) != null){
                        CardInfo cardInfo = getCard(u);
                        long dayOfLoan = cardInfo.getloanTimestamp();
                        int DOL = convertion(dayOfLoan);
                        int differance = (DOL+30) - now.getDayOfYear();

                        if( differance > (DOL+30) || differance < 0) {
                            overDuePlayersON.add(Bukkit.getPlayer(u));
                            //SendConsoleMessage.info("Interest for Player: " + Bukkit.getPlayer(u).getName()+ " was added");
                        }
                    }
                    else if(Bukkit.getOfflinePlayer(u)!= null) {
                        CardInfo cardInfo = getCard(u);
                        long dayOfLoan = cardInfo.getloanTimestamp();
                        int DOL = convertion(dayOfLoan);
                        int differance = (DOL+30) - now.getDayOfYear();
                        String p1 = cardInfo.getName();
                        Player p = Bukkit.getPlayer(p1);
                        if( differance > (DOL+30) || differance < 0) {

                            overDuePlayersOFF.add( Bukkit.getOfflinePlayer(u));
                        }
                    }
                    else {
                        SendConsoleMessage.warning("ERROR IN PLAYER FINDING FOR UUID: " );
                        SendConsoleMessage.warning("");
                        SendConsoleMessage.warning(""+u);
                    }

                    //SendConsoleMessage.debug("" +  Bukkit.getPlayer(u).getName() +" : "+ differance);

                }


                for(Player a: overDuePlayersON){
                    CardInfo cardInfo = getCard(a.getUniqueId());
                    double debtAmount = cardInfo.getDebt();
                    debtAmount =  debtAmount + (debtAmount * plugin.getFileManager().getIntrestRate());
                    cardInfo.setDebt(debtAmount);
                }
                for(OfflinePlayer a: overDuePlayersOFF){
                    CardInfo cardInfo = getCard(a.getUniqueId());
                    double debtAmount = cardInfo.getDebt();
                    debtAmount =  debtAmount + (debtAmount * plugin.getFileManager().getIntrestRate());
                    cardInfo.setDebt(debtAmount);
                }
            }
        }, 0l, plugin.getFileManager().getperdays());


    }
}

