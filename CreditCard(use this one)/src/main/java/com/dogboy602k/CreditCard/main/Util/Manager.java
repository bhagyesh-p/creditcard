package com.dogboy602k.CreditCard.main.Util;


import com.dogboy602k.CreditCard.main.Main.Main;
import net.milkbowl.vault.chat.Chat;
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
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Manager implements Listener {
    private Main plugin;
    private MessageManager mm;
    private Player p;
    private List<MaxLoan> loan;
    private List<CardInfo> cardInfos = new ArrayList<CardInfo>();
    private List<RewardInfo> rewards = new ArrayList<RewardInfo>();
    private int pinHolder;
    private double debt, points;
    private String PasswordHolder;
    private Double loanamount1 = 0.0;
    private Double payoff1 = 0.0;
    private int Gperspin = 0;
    private Player Gowner = null;
    private ArrayList<UUID> UUIDs = new ArrayList();
    private HashMap<Player,Boolean> whoHasPaidIntrestOn = new HashMap<Player,Boolean>();
    private HashMap<OfflinePlayer,Boolean> whoHasPaidIntrestOff = new HashMap<OfflinePlayer,Boolean>();
    private int count =0;
    private String hasNotRegistered,wrongPassword,alreadyRegisterd,notEnoughCashtoPay,payAmountOverDebt,
    wrongPin,maxLoanReached,maxPointsReached,wantLoanLargerThanLimit,commandDidntSayAll,dontHaveEnoughToPayAllDebt,
    DebtCantBePaidItsZero,payAmountLessThanZero,notEnoughPoints,loanLessthanZero,foundPin,registered,autoPayMSG,
    moneyIsBeingAdded,newTotalPoints,youHaveZeroDebtpPayInZeroDays,overDeadLine,youOwnAmountInDays,pleaseWait,
    howMuchIsPaidOff,youHavePoints,pointsDescription,youGotReward,closedGUIMenu,pointsYouCanGet,
    couldNotFindReward,joinMSGNoDebt,joinMSG;

    public Manager(Main plugin) {
        this.plugin = plugin;
        this.loan = new ArrayList<MaxLoan>();
        this.mm = new MessageManager(plugin);
        hasNotRegistered = mm.gethasNotRegistered();
        wrongPassword = mm.getwrongPassword();
        alreadyRegisterd = mm.getalreadyRegisterd();
        notEnoughCashtoPay = mm.getnotEnoughCashtoPay();
        payAmountOverDebt = mm.getpayAmountOverDebt();
        wrongPin = mm.getwrongPin();
        maxLoanReached = mm.getmaxLoanReached();
        maxPointsReached = mm.getmaxPointsReached();
        wantLoanLargerThanLimit = mm.getwantLoanLargerThanLimit();
        commandDidntSayAll = mm.getcommandDidntSayAll();
        dontHaveEnoughToPayAllDebt = mm.getdontHaveEnoughToPayAllDebt();
        DebtCantBePaidItsZero = mm.getDebtCantBePaidItsZero();
        payAmountLessThanZero = mm.getpayAmountLessThanZero();
        notEnoughPoints = mm.getnotEnoughPoints();
        loanLessthanZero = mm.getloanLessthanZero();
        foundPin = mm.getfoundPin();
        registered = mm.getregistered();
        autoPayMSG = mm.getautoPayMSG();
        moneyIsBeingAdded = mm.getmoneyIsBeingAdded();
        newTotalPoints = mm.getnewTotalPoints();
        youHaveZeroDebtpPayInZeroDays = mm.getyouHaveZeroDebtpPayInZeroDays();
        overDeadLine = mm.getoverDeadLine();
        youOwnAmountInDays = mm.getyouOwnAmountInDays();
        pleaseWait = mm.getpleaseWait();
        howMuchIsPaidOff = mm.gethowMuchIsPaidOff();
        youHavePoints = mm.getyouHavePoints();
        pointsDescription = mm.getpointsDescription();
        youGotReward = mm.getyouGotReward();
        closedGUIMenu = mm.getclosedGUIMenu();
        pointsYouCanGet = mm.getpointsYouCanGet();
        couldNotFindReward = mm.getcouldNotFindReward();
        joinMSGNoDebt = mm.getjoinMSGNoDebt();
        joinMSG = mm.getjoinMSG();

    }

    public void addArray(ArrayList<UUID> list){
        for(UUID a : list){

            UUIDs.add(a);
        }
    }

    public CardInfo getCard(UUID owner) {
        for (CardInfo cardInfo : this.cardInfos) {
            if (!cardInfo.getOwner().equals(owner)) continue;
            return cardInfo;
        }
        return null;
    }

    public CardInfo getPinForgot(UUID owner) {
        for (CardInfo cardInfo : this.cardInfos) {
            if (!cardInfo.getOwner().equals(owner)) continue;
            return cardInfo;
        }
        return null;
    }

    public void ForgotPin(Player owner, String password) {
        UUID ownerUUID = owner.getUniqueId();
        if (!this.hasRegistered(ownerUUID)) {
            Util.sendErrorMsg(owner, hasNotRegistered);
            return;
        }
        CardInfo cardInfo = this.getPinForgot(owner.getUniqueId());
        if (cardInfo == null) {
            return;
        }
        CardInfo cardInfoPass = this.getCard(ownerUUID);
        if (password.equalsIgnoreCase(cardInfoPass.getPassword())) {
            Util.sendMsg(owner, ChatColor.GREEN + foundPin);
            Util.sendMsg(owner, ChatColor.AQUA + "" + cardInfo.getpin() + "");
        } else {
            Util.sendErrorMsg(owner, wrongPassword);
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
            Util.sendErrorMsg(owner, ChatColor.RED + alreadyRegisterd);
            return;
        }
        String ownerName = owner.getName();
        this.PasswordHolder = p;
        this.pinHolder = this.generatePIN();
        long loanTimestamp = 0;
        long maxPointsToday = 0;
        UUIDs.add(ownerUUID);
        double auto =0;
        this.cardInfos.add(new CardInfo(ownerUUID, this.pinHolder, ownerName, this.PasswordHolder, this.debt, loanTimestamp,points,maxPointsToday, auto));
        Util.sendMsg(owner, ChatColor.GREEN + registered + ChatColor.GOLD + this.pinHolder);
    }

    public void resentTheMaxPoints(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                for(UUID uuid: UUIDs){
                        CardInfo cardInfo = getCard(uuid);
                        cardInfo.setMaxPointsToday(0);
                }
            }
        }, 0l, 1728000l);
    }

    public void autoPayExecute(){
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                for(UUID uuid: UUIDs){
                    Player player;
                    if( Bukkit.getPlayer(uuid) == null){
                         player = Bukkit.getPlayer(uuid);
                    }
                    else {
                        player = Bukkit.getPlayer(uuid);
                    }
                    CardInfo cardInfo = getCard(uuid);
                    ConfigToMsg configToMsg = new ConfigToMsg(player,cardInfo);
                    if(cardInfo.getAuto() !=1) {
                        if (count != 0) {
                            if(cardInfo.getAuto() == 0 || cardInfo.getDebt() ==0){
                            }
                            else if (plugin.getEconomy().getBalance(player) < cardInfo.getAuto()) {

                                String command = plugin.getFileManager().getPunishCommand().replace("%player%", player.getName());
                                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), command);
                                Util.sendErrorMsg(player, notEnoughCashtoPay);

                            }
                            else {
                                if(cardInfo.getDebt()< cardInfo.getAuto()){
                                    payOver(player,cardInfo.getAuto(),cardInfo.getDebt());

                                }else {
                                    payOffDebt(player, cardInfo.getAuto());
                                    Util.sendMsg(player, ChatColor.GREEN + configToMsg.convertDefaults(autoPayMSG));

                                }
                            }
                        }
                    }
                }
                count++;
            }
        }, 0l, 72000);
    }

    public void payOver(Player player, double amount, double debt ){
        if(amount > debt){
            payOffDebt(player, debt);
        }
        ConfigToMsg configToMsg = new ConfigToMsg(player, getCard(player.getUniqueId()));
        Util.sendErrorMsg(player, configToMsg.convertNonDefaults( configToMsg.convertDefaults(payAmountOverDebt),amount ));
    }

    public void addAutoAmount(Player player, double amount, int pin){
        CardInfo cardInfo =getCard(player.getUniqueId());
        if(cardInfo == null) {
            Util.sendErrorMsg(player, hasNotRegistered);
        }
        if(pin != cardInfo.getpin()){
            Util.sendErrorMsg(player, wrongPin);
        }
        cardInfo.setAuto(amount);
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
            Util.sendErrorMsg(owner, hasNotRegistered);
            return;
        }
        CardInfo cardInfo = this.getCard(ownerUUID);
        int pin = cardInfo.getpin();
        if (pin != perspin) {
            Util.sendErrorMsg(owner, wrongPin);

            return;
        }
        if (pin == perspin) {
            double debtAmount = cardInfo.getDebt();
            double seeIfDebtIsOk = debtAmount += loanamount;
            ConfigToMsg configToMsg = new ConfigToMsg(owner, getCard(owner.getUniqueId()));

            if (seeIfDebtIsOk <= maxloana) {
                if (this.checkmaxlimit(loanamount, owner)) {
                    if (cardInfo.getDebt() == maxloana || cardInfo.getDebt() > maxloana) {
                        Util.sendErrorMsg(owner, configToMsg.convertNonDefaults(maxLoanReached,maxloana) );
                        return;
                    }
                    Util.sendMsg(owner, configToMsg.convertNonDefaults(moneyIsBeingAdded,loanamount));
                    if(cardInfo.getloanTimestamp() == 0 ) {
                        cardInfo.setloanTimestamp(System.currentTimeMillis() / 1000);
                    }

                    this.plugin.getEconomy().depositPlayer(owner, loanamount);
                    if(cardInfo.getMaxPointsToday() >= this.plugin.getFileManager().getmaxpoints()){
                        Util.sendErrorMsg(owner, configToMsg.convertDefaults(maxPointsReached));
                        cardInfo.setDebt(cardInfo.getDebt() + loanamount);
                        return;
                    }
                    if(cardInfo.getPoints() == 0){
                        double points = (loanamount/this.plugin.getFileManager().getpointsper() ) * this.plugin.getFileManager().getpointsrecived();
                        cardInfo.setPoints(points );
                        cardInfo.setMaxPointsToday(points);
                    }else {
                        double points = (cardInfo.getPoints() + ((loanamount / this.plugin.getFileManager().getpointsper()) * this.plugin.getFileManager().getpointsrecived()));
                        cardInfo.setPoints(points );
                        cardInfo.setMaxPointsToday(points);

                    }
                    Util.sendMsg(owner, configToMsg.convertDefaults(newTotalPoints));
                    cardInfo.setDebt(cardInfo.getDebt() + loanamount);
                }
            } else {
                Util.sendErrorMsg(owner, configToMsg.convertNonDefaults(wantLoanLargerThanLimit, maxloana) );
            }
            return;
        }
    }

    public void Debt(Player owner) {
        UUID ownerUUID = owner.getUniqueId();
        ConfigToMsg configToMsg = new ConfigToMsg(owner, getCard(ownerUUID));
        if (!this.hasRegistered(ownerUUID)) {
            Util.sendErrorMsg(owner, hasNotRegistered);
            return;
        }
        CardInfo cardInfo = this.getCard(ownerUUID);
        double debtamount = cardInfo.getDebt();
        Date loanDate = new Date(Long.parseLong(String.valueOf(cardInfo.getloanTimestamp()))* 1000);
        Date currentDate= new Date(System.currentTimeMillis());
        long difference = currentDate.getTime() - loanDate.getTime();
        //difference in time between today and the day player took the loan, give in timestamp format
        int numOfDays = (int) (30 - TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS));
        // gives the difference in physical days
        if(debtamount == 0){
            Util.sendMsg(owner,ChatColor.AQUA + youHaveZeroDebtpPayInZeroDays);
            // the player doesnt owe any cash
        }
        else if( numOfDays < 0) {
            Util.sendMsg(owner, configToMsg.convertNonDefaults(configToMsg.convertDefaults( overDeadLine),Math.abs(numOfDays)) );
            // if the number is negative meaning that the person  is over the dead line
        }
        else {
            Util.sendMsg(owner, configToMsg.convertNonDefaults(configToMsg.convertDefaults( youOwnAmountInDays),numOfDays));
            //player is not over the dead line
        }

    }

    public void payOffDebt(Player owner, String all) {
        ConfigToMsg configToMsg = new ConfigToMsg(owner, getCard(owner.getUniqueId()));

        if(all.equalsIgnoreCase("all")){
            Util.sendMsg(owner, pleaseWait);
        }
        else {
            Util.sendMsg(owner, commandDidntSayAll);
            return;
        }
        UUID ownerUUID = owner.getUniqueId();
        if (!this.hasRegistered(ownerUUID)) {
            Util.sendErrorMsg(owner, hasNotRegistered);
            return;
        }
        CardInfo cardInfo = this.getCard(ownerUUID);
        double debtAmount = cardInfo.getDebt();
        double payOff = debtAmount;
        if (payOff > this.plugin.getEconomy().getBalance(owner)) {
            Util.sendErrorMsg(owner,  configToMsg.convertDefaults(dontHaveEnoughToPayAllDebt));
            return;
        }
        if (this.plugin.getEconomy().getBalance(owner) < payOff) {// =< to <
            Util.sendErrorMsg(owner, notEnoughCashtoPay);
            return;
        }
        if (this.plugin.getEconomy().getBalance(owner) < payOff) {
            return;
        }
        if (debtAmount == 0.0) {
            Util.sendErrorMsg(owner,  DebtCantBePaidItsZero);
            return;
        }
        if (payOff > debtAmount) {
            payOver(owner,payOff,cardInfo.getDebt());
            return;
        }
        if (payOff == debtAmount) {
            this.getCard(ownerUUID).setloanTimestamp(0);
                for(Map.Entry<Player, Boolean> e: whoHasPaidIntrestOn.entrySet()){
                    if(owner.getName().equals(e.getKey().getName())){
                        whoHasPaidIntrestOn.remove(e.getKey());
                    }
                }
        }
        if (payOff < 0.0 || payOff == 0.0) {
            Util.sendErrorMsg(owner, payAmountLessThanZero);
            return;
        }
        Util.sendMsg(owner, configToMsg.convertNonDefaults(howMuchIsPaidOff, payOff));
        this.plugin.getEconomy().withdrawPlayer(owner, payOff);
        cardInfo.setDebt(debtAmount - payOff);


    }

    public void payOffDebt(Player owner, double payOff) {
        UUID ownerUUID = owner.getUniqueId();
        if (!this.hasRegistered(ownerUUID)) {
            Util.sendErrorMsg(owner, hasNotRegistered);
            return;
        }
        CardInfo cardInfo = this.getCard(ownerUUID);
        ConfigToMsg configToMsg = new ConfigToMsg(owner,cardInfo);
        double debtAmount = cardInfo.getDebt();
        if (payOff > this.plugin.getEconomy().getBalance(owner)) {
            Util.sendErrorMsg(owner, notEnoughCashtoPay);
            return;
        }
        if (this.plugin.getEconomy().getBalance(owner) <= payOff) {
            Util.sendErrorMsg(owner, notEnoughCashtoPay);
            return;
        }
        if (this.plugin.getEconomy().getBalance(owner) < payOff) {
            return;
        }
        if (debtAmount == 0.0) {
            Util.sendErrorMsg(owner, DebtCantBePaidItsZero);
            return;
        }
        if (payOff > debtAmount) {
            payOver(owner,payOff,cardInfo.getDebt());

            return;
        }
        if (payOff == debtAmount) {
            this.getCard(ownerUUID).setloanTimestamp(0);
            for(Map.Entry<Player, Boolean> e: whoHasPaidIntrestOn.entrySet()){
                if(owner.getName().equals(e.getKey().getName())){
                    whoHasPaidIntrestOn.remove(e.getKey());
                }
            }
        }
        if (payOff < 0.0 || payOff == 0.0) {
            Util.sendErrorMsg(owner, payAmountLessThanZero);
            return;
        }
        Util.sendMsg(owner, configToMsg.convertNonDefaults(howMuchIsPaidOff, payOff));
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
        return this.cardInfos;
    }

    public void addCardInfo(CardInfo cardInfo) {
        this.cardInfos.add(cardInfo);
    }

    public void addRewardInfo(RewardInfo rewardInfo) {
        this.rewards.add(rewardInfo);
    }

    public void listRewards(Player player){
        CardInfo PlayerCard =getCard(player.getUniqueId());
        if(PlayerCard == null){
            Util.sendErrorMsg(player, hasNotRegistered);

            return;
        }
        ConfigToMsg configToMsg = new ConfigToMsg(player,PlayerCard);
        Util.sendMsg(player,  configToMsg.convertDefaults(youHavePoints));
        for(RewardInfo reward:rewards){
            Util.sendMsg(player, "");
            Util.sendMsg(player, configToMsg.convertNonDefaults(configToMsg.convertNonDefaults(configToMsg.convertDefaults(youGotReward), reward.getName()), reward.getCost() ));
            Util.sendMsg(player, configToMsg.convertNonDefaults(pointsDescription, reward.getDescription()));
            Util.sendMsg(player, "");
        }
    }

    public boolean hasEnoughPoint (Player player, double points, int pin){
        CardInfo PlayerCard =getCard(player.getUniqueId());
        if(PlayerCard== null){
            Util.sendErrorMsg(player, hasNotRegistered);
            return false;
        }
        if(PlayerCard.getPoints() >= points){
            if(!(PlayerCard.getpin() == pin)){
                Util.sendErrorMsg(player, wrongPin);
                return false;
            }
            PlayerCard.setPoints(PlayerCard.getPoints()-points);
            return true;
        }
        else {
            return false;
        }
    }

    public void giveReward(String rewardName, Player player, int pin){
        int count = 0;
        CardInfo PlayerCard =getCard(player.getUniqueId());
        ConfigToMsg configToMsg = new ConfigToMsg(player, PlayerCard);
        RewardInfo r = null;

        for(RewardInfo reward:rewards){
            if(reward.getName().equals(rewardName)){
                count++;
                if(!hasEnoughPoint(player, reward.getCost(), pin)){
                    Util.sendErrorMsg(player, notEnoughPoints);
                    return;
                }

                Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), reward.getCommandsToUse(player.getName()));
                r = reward;
                break;
            }
        }
        if(count == 0){
            Util.sendErrorMsg(player, couldNotFindReward);
        }
        else{

            Util.sendMsg(player, configToMsg.convertNonDefaults(configToMsg.convertNonDefaults(configToMsg.convertDefaults(youGotReward), rewardName), r.getCost() ));
            Util.sendMsg(player, configToMsg.convertDefaults(newTotalPoints));

        }
    }

    public void requestGui(Player owner, int perspin) {
        UUID ownerUUID = owner.getUniqueId();
        double ButtonAdd1 = this.plugin.getFileManager().getButtonAdd1();
        double ButtonAdd2 = this.plugin.getFileManager().getButtonAdd2();
        double ButtonSubtract1 = this.plugin.getFileManager().getButtonSubtract1();
        double ButtonSubtract2 = this.plugin.getFileManager().getButtonSubtract2();
        if (hasRegistered(ownerUUID)== false) {
            Util.sendErrorMsg(owner, hasNotRegistered);
            return;
        }
        CardInfo cardInfo = this.getCard(ownerUUID);

        int pin = cardInfo.getpin();
        if (pin != perspin) {
            Util.sendErrorMsg(owner, wrongPin);
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
            Util.sendErrorMsg(owner, hasNotRegistered);
            return;
        }
        CardInfo cardInfo = this.getCard(ownerUUID);

        int pin = cardInfo.getpin();
        if (pin != perspin) {
            Util.sendErrorMsg(owner, wrongPin);
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
            Util.sendErrorMsg(owner, hasNotRegistered);
            return;
        }
        CardInfo cardInfo = this.getCard(ownerUUID);

        int pin = cardInfo.getpin();
        if (pin != perspin) {
            Util.sendErrorMsg(owner, wrongPin);
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
                Util.sendMsg((Player)e.getWhoClicked(), closedGUIMenu);
                e.getWhoClicked().closeInventory();
            }
            if (e.getRawSlot() == 22) {
                if (!this.hasRegistered(ownerUUID)) {
                    Util.sendErrorMsg((Player) e.getWhoClicked(), hasNotRegistered);
                    e.getWhoClicked().closeInventory();
                    this.payoff1 = 0.0;
                    return;
                }
                CardInfo cardInfo = this.getCard(ownerUUID);
                ConfigToMsg configToMsg = new ConfigToMsg((Player) e.getWhoClicked(),cardInfo);
                double debtAmount = cardInfo.getDebt();
                if (this.payoff1 > this.plugin.getEconomy().getBalance(this.Gowner)) {
                    Util.sendMsg(this.Gowner, notEnoughCashtoPay);
                    e.getWhoClicked().closeInventory();
                    this.payoff1 = 0.0;
                    return;
                }
                if (this.plugin.getEconomy().getBalance(this.Gowner) <= this.payoff1) {
                    Util.sendMsg(this.Gowner, notEnoughCashtoPay);
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
                    Util.sendErrorMsg(this.Gowner, DebtCantBePaidItsZero);
                    e.getWhoClicked().closeInventory();
                    this.payoff1 = 0.0;
                    return;
                }
                if (this.payoff1 < 0.0 || this.payoff1 == 0.0) {
                    Util.sendErrorMsg(this.Gowner, payAmountLessThanZero);
                    e.getWhoClicked().closeInventory();
                    this.payoff1 = 0.0;
                    return;
                }
                if (this.payoff1 > debtAmount) {
                    Util.sendErrorMsg(Gowner, configToMsg.convertNonDefaults( configToMsg.convertDefaults(payAmountOverDebt),payoff1 ));
                    payOver(Gowner,cardInfo.getAuto(),cardInfo.getDebt());
                    Util.sendMsg(Gowner, configToMsg.convertNonDefaults(howMuchIsPaidOff, this.payoff1));

                    e.getWhoClicked().closeInventory();
                    this.payoff1 = 0.0;
                    return;
                }
                if (this.payoff1 == debtAmount) {
                    this.getCard(ownerUUID).setloanTimestamp(0);
                }
                Util.sendMsg(Gowner, configToMsg.convertNonDefaults(howMuchIsPaidOff, this.payoff1.doubleValue()));
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
            ConfigToMsg configToMsg = new ConfigToMsg(playerName, getCard(playerName.getUniqueId()));
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
                Util.sendMsg((Player)e.getWhoClicked(), closedGUIMenu);
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
                    Util.sendErrorMsg(playerName, configToMsg.convertNonDefaults(wantLoanLargerThanLimit, maxloana) );
                    e.getWhoClicked().closeInventory();
                    this.loanamount1 = 0.0;
                    return;
                }
                if (!this.checkmaxlimit(this.loanamount1, playerName)) {
                    Util.sendErrorMsg((Player)e.getWhoClicked(), configToMsg.convertNonDefaults(maxLoanReached,maxloana) );
                    e.getWhoClicked().closeInventory();
                    this.loanamount1 = 0.0;
                    return;
                }
                if (cardInfo.getDebt() == maxloana || cardInfo.getDebt() > maxloana) {
                    Util.sendErrorMsg((Player)e.getWhoClicked(), configToMsg.convertNonDefaults(maxLoanReached,maxloana) );
                    e.getWhoClicked().closeInventory();
                    this.loanamount1 = 0.0;
                    return;
                }
                if (this.loanamount1 != 0.0 && this.loanamount1 >= 0.0) {
                    Util.sendMsg((Player) e.getWhoClicked(), configToMsg.convertNonDefaults(moneyIsBeingAdded,loanamount1));
                    if(this.getCard(ownerUUID).getloanTimestamp() == 0 ) {
                        this.getCard(ownerUUID).setloanTimestamp(System.currentTimeMillis());
                    }
                    this.plugin.getEconomy().depositPlayer(e.getWhoClicked().getName(), this.loanamount1.doubleValue());
                    cardInfo.setDebt(cardInfo.getDebt() + this.loanamount1);
                    e.getWhoClicked().closeInventory();
                    this.loanamount1 = 0.0;
                    return;
                }
                Util.sendMsg((Player)e.getWhoClicked(), loanLessthanZero);
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
                ConfigToMsg configToMsg = new ConfigToMsg(player,cardInfo);
                e.setJoinMessage(ChatColor.GOLD + "[CC]" + ChatColor.GREEN + "=========[Credit Card]=========\n" + ChatColor.GOLD + "\n[CC]" + ChatColor.GREEN + configToMsg.convertDefaults(joinMSG) + ChatColor.GOLD + "\n[CC]" + ChatColor.GREEN + "=========[Credit Card]=========");
                for(Map.Entry<OfflinePlayer, Boolean> data: whoHasPaidIntrestOff.entrySet()){
                    whoHasPaidIntrestOn.put(player,data.getValue());
                    whoHasPaidIntrestOff.remove(data.getKey(),data.getValue());
                }


            } else {
                e.setJoinMessage(joinMSGNoDebt);
            }
        }
        else{
            Util.sendErrorMsg((Player)e.getPlayer(), hasNotRegistered);

        }
    }

    @EventHandler
    public void onExit(final PlayerQuitEvent e) {
        OfflinePlayer player = e.getPlayer();
        UUID ownerUUID = player.getUniqueId();
        if(hasRegistered(ownerUUID)) {
            CardInfo cardInfo = this.getCard(ownerUUID);
            final double debtamount = cardInfo.getDebt();
            if (debtamount > 0.0) {
                for(Map.Entry<Player, Boolean> data: whoHasPaidIntrestOn.entrySet()){
                    whoHasPaidIntrestOff.put(player, data.getValue());
                    whoHasPaidIntrestOn.remove(data.getKey(),data.getValue());
                }


            }
        }
        else{
            Util.sendErrorMsg(e.getPlayer(), hasNotRegistered);

        }
    }

    public void addIntrest() {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
                public void run(){
                boolean areOnList = true;

                for(UUID u : UUIDs){
                    CardInfo cardInfo = getCard(u);
                    long dayOfLoan = cardInfo.getloanTimestamp();

                    Date loanDate = new Date(Long.parseLong(String.valueOf(cardInfo.getloanTimestamp()))* 1000);
                    Date currentDate= new Date(System.currentTimeMillis());
                    long difference = currentDate.getTime() - loanDate.getTime();
                    //difference in time between today and the day player took the loan, give in timestamp format
                    int numOfDays = (int) (30 - TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS));
                    // gives the difference in physical days

                    if(Bukkit.getPlayer(u) != null){
                        if(numOfDays < 0 && Math.abs(numOfDays)%7 == 0){
                            if(whoHasPaidIntrestOn.get(Bukkit.getPlayer(u)) == null){
                                whoHasPaidIntrestOn.put(Bukkit.getPlayer(u), false);

                            }

                        }
                    }
                    else if(Bukkit.getOfflinePlayer(u)!= null) {
                        if(numOfDays < 0 && Math.abs(numOfDays)%7 == 0){
                            if(whoHasPaidIntrestOff.get(Bukkit.getOfflinePlayer(u))==null ){
                                whoHasPaidIntrestOff.put(Bukkit.getOfflinePlayer(u), false);
                            }
                        }
                    }

                }
                for(Map.Entry<Player, Boolean> e: whoHasPaidIntrestOn.entrySet()){
                    Player player =  e.getKey();
                    Boolean haspaid= e.getValue();

                    if(haspaid == false){
                        CardInfo cardInfo = getCard(player.getUniqueId());
                        double debtAmount = cardInfo.getDebt();
                        debtAmount =  debtAmount + (debtAmount * plugin.getFileManager().getIntrestRate());
                        cardInfo.setDebt(debtAmount);
                        whoHasPaidIntrestOn.remove(player);
                        whoHasPaidIntrestOn.put(player, true);

                    }
                }
                for(Map.Entry<OfflinePlayer, Boolean> e: whoHasPaidIntrestOff.entrySet()){
                    OfflinePlayer player =  e.getKey();
                    Boolean haspaid= e.getValue();
                    if(haspaid == false){
                        CardInfo cardInfo = getCard(player.getUniqueId());
                        double debtAmount = cardInfo.getDebt();
                        debtAmount =  debtAmount + (debtAmount * plugin.getFileManager().getIntrestRate());
                        cardInfo.setDebt(debtAmount);
                        whoHasPaidIntrestOff.remove(player);
                        whoHasPaidIntrestOff.put(player, true);

                    }
                }

            }
        }, 0l, 20l);


    }

    public void clearIntrestMaps(){
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run(){
                int count =0;
                if(count > 0) {
                    whoHasPaidIntrestOff.clear();
                    whoHasPaidIntrestOn.clear();
                }
                count++;
            }
        }, 0l, 1728000l);

    }
}