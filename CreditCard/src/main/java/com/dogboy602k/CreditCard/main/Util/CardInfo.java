package com.dogboy602k.CreditCard.main.Util;/*
 * Decompiled with CFR 0_118.
 */

import java.util.UUID;

public class CardInfo {
    private UUID owner;
    private int pin;
    private String name;
    private String PasswordHolder;
    private double debt;
    private long loanTimestamp;
    private long intrestUpdateDate;

    public CardInfo(UUID owner, int pin, String name, String PasswordHolder, double debt, long loanTimestamp, long intrestUpdateDate) {
        this.owner = owner;
        this.pin = pin;
        this.name = name;
        this.PasswordHolder = PasswordHolder;
        this.debt = debt;
        this.intrestUpdateDate = intrestUpdateDate;
        this.loanTimestamp = loanTimestamp;
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

    public long getintrestUpdateDate() {
        return this.intrestUpdateDate;
    }

    public void setintrestUpdateDate(long loanTimestamp) {
        this.loanTimestamp = loanTimestamp;
    }

}

