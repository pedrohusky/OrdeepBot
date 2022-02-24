package com.example.ordeepbot.indicators;

import com.example.ordeepbot.symbol.Candlestick;

import java.util.ArrayList;

public class EMA {

    ArrayList<Candlestick> candlesticks;
    double ema5 = 0;
    double ema10 = 0;
    double ema20 = 0;

    public EMA(ArrayList<Candlestick> candlesticks) {
        this.candlesticks = candlesticks;
        if (candlesticks.size() != 0) {
            double[] result = getResult(5);
            this.ema5 = result[result.length - 1];
            result = getResult(10);
            this.ema10 = result[result.length - 1];
            result = getResult(20);
            this.ema20 = result[result.length - 1];
        }
    }

    /**
     * Calculates the Exponential moving average (EMA) of the given data
     *
     * @param candlesticks
     * @param n            : number of time periods to use in calculating the smoothing factor of the EMA
     * @return an array of EMA values
     */
    public static double[] calculateEmaValues(ArrayList<Candlestick> candlesticks, double n) {

        if (candlesticks.size() == 0) {
            return new double[0];
        }

        double[] results = new double[candlesticks.size()];

        calculateEmasHelper(candlesticks, n, candlesticks.size() - 1, results);
        return results;
    }

    public static double calculateEmasHelper(ArrayList<Candlestick> candlesticks, double n, int i, double[] results) {

        if (i == 0) {
            results[0] = Double.parseDouble(String.valueOf(candlesticks.get(0).getClose()));
            return results[0];
        } else {
            double close = Double.parseDouble(String.valueOf(candlesticks.get(i).getClose()));
            double factor = (2.0 / (n + 1));
            double ema = close * factor + (1 - factor) * calculateEmasHelper(candlesticks, n, i - 1, results);
            results[i] = ema;
            return ema;
        }


    }

    public double[] getResult(double period) {
        return calculateEmaValues(candlesticks, period);
    }

    public double getEma5() {
        return ema5;
    }

    public double getEma10() {
        return ema10;
    }

    public double getEma20() {
        return ema20;
    }
}
