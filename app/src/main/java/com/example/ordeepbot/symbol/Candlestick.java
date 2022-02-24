package com.example.ordeepbot.symbol;

import androidx.annotation.NonNull;

public class Candlestick {
    float open;
    float high;
    float low;
    float close;
    float volume;
    float quoteAssetVolume;

    public Candlestick(String open, String high, String low, String close, String volume, String quoteAssetVolume) {
        this.open = Float.parseFloat(open);
        this.high = Float.parseFloat(high);
        this.low = Float.parseFloat(low);
        this.close = Float.parseFloat(close);
        this.volume = Float.parseFloat(volume);
        this.quoteAssetVolume = Float.parseFloat(quoteAssetVolume);
    }

    public float getOpen() {
        return open;
    }

    public float getHigh() {
        return high;
    }

    public float getLow() {
        return low;
    }

    public float getClose() {
        return close;
    }

    public float getVolume() {
        return volume;
    }

    public float getQuoteAssetVolume() {
        return quoteAssetVolume;
    }

    @NonNull
    @Override
    public String toString() {
        return "Candlestick{" +
                "open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", volume=" + volume +
                ", quoteAssetVolume=" + quoteAssetVolume +
                '}';
    }
}
