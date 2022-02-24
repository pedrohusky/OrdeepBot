package com.example.ordeepbot.symbol;

import androidx.annotation.NonNull;

import com.example.ordeepbot.indicators.Indicators;

public class SymbolWsData {
    private final Indicators indicators;
    private float open;
    private float close;
    private float high;
    private float low;
    private String symbol;
    private float volume;
    private String percentDifference;

    public SymbolWsData(String symbol, float open, float close, float high, float low, float volume, String percentDifference, Indicators indicators) {
        this.symbol = symbol;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
        this.volume = volume;
        this.percentDifference = percentDifference;
        this.indicators = indicators;
    }

    public Indicators getIndicators() {
        return indicators;
    }

    public float getOpen() {
        return open;
    }

    public void setOpen(float open) {
        this.open = open;
    }

    public float getClose() {
        return close;
    }

    public void setClose(float close) {
        this.close = close;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public String getPercentDifference() {
        return percentDifference;
    }

    public void setPercentDifference(String percentDifference) {
        this.percentDifference = percentDifference;
    }

    @NonNull
    @Override
    public String toString() {
        return "SymbolWsData{" +
                "open=" + open +
                ", close=" + close +
                ", high=" + high +
                ", low=" + low +
                ", symbol='" + symbol + '\'' +
                ", volume=" + volume +
                ", percentDifference='" + percentDifference + '\'' +
                '}';
    }
}
