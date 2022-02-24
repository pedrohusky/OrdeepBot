package com.example.ordeepbot.ui.global_options;

import android.content.SharedPreferences;

import androidx.annotation.Nullable;

public class GlobalDefaultSettings {

    private int id;

    private boolean buyOn;

    private boolean sellOn;

    private float profitPercent;

    private float lossPercent;

    private float balancePercent;

    private int percentBalance = 10;

    public GlobalDefaultSettings(@Nullable SharedPreferences prefs) {
        if (prefs != null) {
            if (prefs.getFloat("profitPercent", -99) != -99) {
                this.profitPercent = prefs.getFloat("profitPercent", -99);
            }

            if (prefs.getFloat("lossPercent", -99) != -99) {
                this.lossPercent = prefs.getFloat("lossPercent", -99);
            }

            if (prefs.getInt("balancePercent", -99) != -99) {
                this.balancePercent = prefs.getInt("balancePercent", 10);
            }

            if (prefs.getBoolean("buyOn", false)) {
                this.buyOn = prefs.getBoolean("buyOn", false);
            }

            if (prefs.getBoolean("sellOn", false)) {
                this.sellOn = prefs.getBoolean("sellOn", false);
            }
        }
    }

    /*
     * Getters and Setters
     * */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isBuyOn() {
        return buyOn;
    }

    public void setBuyOn(boolean buyOn) {
        this.buyOn = buyOn;
    }

    public boolean isSellOn() {
        return sellOn;
    }

    public void setSellOn(boolean sellOn) {
        this.sellOn = sellOn;
    }

    public float getProfitPercent() {
        return profitPercent;
    }

    public void setProfitPercent(float profitPercent) {
        this.profitPercent = profitPercent;
    }

    public float getLossPercent() {
        return lossPercent;
    }

    public void setLossPercent(float lossPercent) {
        this.lossPercent = lossPercent;
    }

    public float getBalancePercent() {
        return balancePercent;
    }

    public void setBalancePercent(float balancePercent) {
        this.balancePercent = balancePercent;
    }

    public int getPercentBalance() {
        return percentBalance;
    }

    public void setPercentBalance(int percentBalance) {
        this.percentBalance = percentBalance;
    }
}