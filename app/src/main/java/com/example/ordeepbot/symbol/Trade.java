package com.example.ordeepbot.symbol;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Trade {
    @PrimaryKey(autoGenerate = true)
    private int id;

    public String getSymbol() {
        return symbol;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    @ColumnInfo(name = "symbol")
    String symbol;

    @ColumnInfo(name = "lastBuyPrice")
    double lastBuyPrice;

    @ColumnInfo(name = "currentPrice")
    double currentPrice;

    @ColumnInfo(name = "profitPrice")
    double profitPrice;

    @ColumnInfo(name = "lossPrice")
    double lossPrice;

    @ColumnInfo(name = "amountBought")
    double amountBought;

    @ColumnInfo(name = "differenceToProfit")
    double differenceToProfit;

    @ColumnInfo(name = "differenceToLoss")
    double differenceToLoss;


    public void updateCurrentPrice(double newPrice) {
        currentPrice = newPrice;
    }

    public Trade() {}

    public Trade(SymbolInfoWithWs symbolInfoWithWs, String symbol, double lastBuyPrice, double amountBought) {
        this.symbol = symbol;
        this.lastBuyPrice = lastBuyPrice;
        this.currentPrice = lastBuyPrice;
        this.amountBought = amountBought;

        this.profitPrice = lastBuyPrice * symbolInfoWithWs.getTrade().getProfitPercent();
        this.lossPrice = lastBuyPrice * symbolInfoWithWs.getTrade().getLossPercent();
        this.differenceToProfit = getDifference(lastBuyPrice, profitPrice);
        this.differenceToLoss = getDifference(lastBuyPrice, lossPrice);
    }

    private static double getDifference(double a, double b) {
        double result = 0;
        result = ((b - a) * 100) / a;

        return result;
    }

    public double getLastBuyPrice() {
        return lastBuyPrice;
    }

    public double getProfitPrice() {
        return profitPrice;
    }

    public double getLossPrice() {
        return lossPrice;
    }

    public double getAmountBought() {
        return amountBought;
    }

    public double getDifferenceToProfit() {
        return differenceToProfit;
    }

    public double getDifferenceToLoss() {
        return differenceToLoss;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
