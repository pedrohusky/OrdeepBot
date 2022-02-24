package com.example.ordeepbot.symbol;

public enum BinanceInterval {

    ONE_MIN("1m"),
    THREE_MIN("3m"),
    FIVE_MIN("5m"),
    FIFTEEN_MIN("15m"),
    THIRTY_MIN("30m"),

    ONE_HOUR("1h"),
    TWO_HOURS("2h"),
    FOUR_HOURS("4h"),
    SIX_HOURS("6h"),
    EIGHT_HOURS("8h"),
    TWELVE_HOURS("12h"),

    ONE_DAY("1d"),
    THREE_DAYS("3d"),
    ONE_WEEK("1w"),
    ONE_MONTH("1M");
    private final String value;

    BinanceInterval(final String value) {
        this.value = value;
    }

    static public BinanceInterval lookup(String val) {
        if (val.equals(ONE_MIN.toString())) return ONE_MIN;
        if (val.equals(THREE_MIN.toString())) return THREE_MIN;
        if (val.equals(FIVE_MIN.toString())) return FIVE_MIN;
        if (val.equals(FIFTEEN_MIN.toString())) return FIFTEEN_MIN;
        if (val.equals(THIRTY_MIN.toString())) return THIRTY_MIN;

        if (val.equals(ONE_HOUR.toString())) return ONE_HOUR;
        if (val.equals(TWO_HOURS.toString())) return TWO_HOURS;
        if (val.equals(FOUR_HOURS.toString())) return FOUR_HOURS;
        if (val.equals(SIX_HOURS.toString())) return SIX_HOURS;
        if (val.equals(EIGHT_HOURS.toString())) return EIGHT_HOURS;
        if (val.equals(TWELVE_HOURS.toString())) return TWELVE_HOURS;

        if (val.equals(ONE_DAY.toString())) return ONE_DAY;
        if (val.equals(THREE_DAYS.toString())) return THREE_DAYS;
        if (val.equals(ONE_WEEK.toString())) return ONE_WEEK;
        return ONE_MONTH;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.getValue();
    }

}
