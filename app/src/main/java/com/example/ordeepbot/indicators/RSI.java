package com.example.ordeepbot.indicators;

import com.example.ordeepbot.symbol.Candlestick;

import java.util.ArrayList;

public class RSI {
    ArrayList<Candlestick> candlesticks;
    double rsiResult;

    public RSI(ArrayList<Candlestick> candlesticks) {
        this.candlesticks = candlesticks;
        double[] result;
        if (candlesticks.size() > 0) {
            result = calculateRSI(6);
            rsiResult = result[result.length - 1];/// + candlesticks.size();
        }
    }

    public static double[] calculateRSIValues(ArrayList<Candlestick> candlesticks, int n) {

        double[] results = new double[candlesticks.size()];

        double ut1 = 0;
        double dt1 = 0;
        for (int i = 0; i < candlesticks.size(); i++) {
            if (i < (n)) {
                continue;
            }

            ut1 = calcSmmaUp(candlesticks, n, i, ut1);
            dt1 = calcSmmaDown(candlesticks, n, i, dt1);

            results[i] = 100.0 - 100.0 / (1.0 +
                    calculateRS(ut1,
                            dt1));

        }

        return results;
    }

    private static double calculateRS(double avgUp, double avgDown) {
        return avgUp / avgDown;
    }

    public static double calcSmmaUp(ArrayList<Candlestick> candlesticks, double n, int i, double avgUt1) {

        if (avgUt1 == 0) {
            double sumUpChanges = 0;

            for (int j = 0; j < n; j++) {
                double change = candlesticks.get(i - j).getClose() - candlesticks.get(i - j).getOpen();

                if (change > 0) {
                    sumUpChanges += change;
                }
            }
            return sumUpChanges / n;
        } else {
            double change = candlesticks.get(i).getClose() - candlesticks.get(i).getOpen();
            if (change < 0) {
                change = 0;
            }
            return ((avgUt1 * (n - 1)) + change) / n;
        }

    }

    public static double calcSmmaDown(ArrayList<Candlestick> candlesticks, double n, int i, double avgDt1) {
        if (avgDt1 == 0) {
            double sumDownChanges = 0;

            for (int j = 0; j < n; j++) {
                double change = candlesticks.get(i - j).getClose() - candlesticks.get(i - j).getOpen();

                if (change < 0) {
                    sumDownChanges -= change;
                }
            }
            return sumDownChanges / n;
        } else {
            double change = candlesticks.get(i).getClose() - candlesticks.get(i).getOpen();
            if (change > 0) {
                change = 0;
            }
            return ((avgDt1 * (n - 1)) - change) / n;
        }

    }

    public double[] calculateRSI(int n) {
        return calculateRSIValues(candlesticks, n);
    }

    public double getRsiResult() {
        return rsiResult;
    }
}
