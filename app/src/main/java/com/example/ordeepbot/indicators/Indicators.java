package com.example.ordeepbot.indicators;

import java.util.ArrayList;

public class Indicators {
    HuskyIndicator huskyIndicator;
    EMA emaIndicator;
    RSI rsiIndicator;

    public Indicators() {
        this.huskyIndicator = new HuskyIndicator(new ArrayList<>());
        this.emaIndicator = new EMA(new ArrayList<>());
        this.rsiIndicator = new RSI(new ArrayList<>());
    }

    public HuskyIndicator getHuskyIndicator() {
        return huskyIndicator;
    }

    public void setHuskyIndicator(HuskyIndicator huskyIndicator) {
        this.huskyIndicator = huskyIndicator;
    }

    public EMA getEmaIndicator() {
        return emaIndicator;
    }

    public void setEmaIndicator(EMA emaIndicator) {
        this.emaIndicator = emaIndicator;
    }

    public RSI getRsiIndicator() {
        return rsiIndicator;
    }

    public void setRsiIndicator(RSI rsiIndicator) {
        this.rsiIndicator = rsiIndicator;
    }
}
