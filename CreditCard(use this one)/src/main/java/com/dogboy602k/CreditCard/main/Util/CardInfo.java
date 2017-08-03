package com.dogboy602k.CreditCard.main.Util;/*
 * Decompiled with CFR 0_118.
 */

import java.util.UUID;

public class CardInfo {
    private UUID owner;
    private int pin;
    private String name;
    private String PasswordHolder;
    private double debt, points,maxPointsToday,auto;
    private long loanTimestamp;




    public CardInfo(UUID owner, int pin, String name, String PasswordHolder, double debt, long loanTimestamp, double points, long maxPointsToday, double auto) {
        this.owner = owner;
        this.pin = pin;
        this.name = name;
        this.PasswordHolder = PasswordHolder;
        this.debt = debt;
        this.loanTimestamp = loanTimestamp;
        this.points = points;
        this.maxPointsToday = maxPointsToday;
        this.auto = auto;

    }

    public double getAuto() {
        return auto;
    }

    public void setAuto(double auto) {
        this.auto = auto;
    }

    public double getMaxPointsToday() {
        return maxPointsToday;
    }

    public void setMaxPointsToday(double maxPointsToday) {
        this.maxPointsToday = maxPointsToday;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public UUID getOwner() {
        return this.owner;
    }

    public int getpin() {
        return this.pin;
    }

    public String getName() {
        return this.name;
    }

    public String getPassword() {
        return this.PasswordHolder;
    }

    public double getDebt() {
        return this.debt;
    }

    public void setDebt(double debtAmount) {
        this.debt = debtAmount;
    }

    public void setloanTimestamp(long time) {
        this.loanTimestamp = time;
    }

    public long getloanTimestamp() {
        return this.loanTimestamp;
    }





}

