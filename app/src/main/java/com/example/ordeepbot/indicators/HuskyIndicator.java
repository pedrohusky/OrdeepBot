package com.example.ordeepbot.indicators;

import com.example.ordeepbot.symbol.Candlestick;

import java.util.ArrayList;

public class HuskyIndicator {

    float strength = 0;
    String state = "No data";
    boolean isRising = false;

    public HuskyIndicator(ArrayList<Candlestick> candlesticks) {

        int newCandle = 1;
        float diff = 0;
        float positiveMultiplier = 1f;
        float negativeMultiplier = -1.1f;

        for (Candlestick candle : candlesticks) {
            if (newCandle < candlesticks.size()) {
                Candlestick newCandleToTest = candlesticks.get(newCandle);
                float calc = 0;
                if (candle.getClose() <= newCandleToTest.getClose()) {
                    calc = (newCandleToTest.getClose() - candle.getClose()) * positiveMultiplier;
                } else {
                    calc = (candle.getClose() - newCandleToTest.getClose()) * negativeMultiplier;
                }
                float finalCalc = (calc / candle.getClose()) * 100;
                diff += finalCalc;
            }
            newCandle++;
        }

        strength = diff;

        isRising = strength > 0;

        if (isRising) {
            this.state = "Rising";
        } else {
            this.state = "Falling";
        }
    }

    public float getStrength() {
        return strength;
    }

    public String getState() {
        return state;
    }
}
