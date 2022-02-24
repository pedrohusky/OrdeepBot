package com.example.ordeepbot.symbol;

import android.content.Context;

import com.example.ordeepbot.ui.global_options.GlobalDefaultSettings;
import com.example.ordeepbot.MainActivity;

/**
 * Created by Mohsen on 12/2/2016.
 */

public class SymbolInfoWithWs {
    private final String name;
    private String description;
    private int symbolIconId;
    private int assetIconId;
    private GlobalDefaultSettings globalDefaultSettings;
    private SymbolWsData webSocketData;

    public SymbolInfoWithWs(Context context, String symbol, String description, GlobalDefaultSettings globalDefaultSettings, SymbolWsData webSocketData) {
        this.name = symbol;
        this.description = description;
        this.globalDefaultSettings = globalDefaultSettings;
        this.webSocketData = webSocketData;
        this.symbolIconId = setSymbolIconId(((MainActivity) context).sanitizeSymbol(name)[0], context);
        this.assetIconId = setAssetIconId(((MainActivity) context).sanitizeSymbol(name)[1], context);
    }

    public GlobalDefaultSettings getTrade() {
        return globalDefaultSettings;
    }

    public void setTrade(GlobalDefaultSettings globalDefaultSettings) {
        this.globalDefaultSettings = globalDefaultSettings;
    }

    public int setSymbolIconId(String symbol, Context context) {

        this.symbolIconId = context.getResources().getIdentifier("drawable/" + symbol, "drawable", context.getPackageName());
        return this.symbolIconId;
    }

    public int getAssetIconId() {
        return assetIconId;
    }

    public int getSymbolIconId() {
        return symbolIconId;
    }

    public int setAssetIconId(String symbol, Context context) {
        this.assetIconId = context.getResources().getIdentifier("drawable/" + symbol, "drawable", context.getPackageName());
        return this.assetIconId;
    }

    public String getSymbolName() {
        return name;
    }

    public String getSymbolDescription() {
        return description;
    }

    public void setSymbolDescription(String description) {
        this.description = description;
    }

    public SymbolWsData getWebSocketData() {
        return webSocketData;
    }

    public void setWebSocketData(SymbolWsData webSocketData) {
        this.webSocketData = webSocketData;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (!(obj instanceof SymbolInfoWithWs))
            return false;
        SymbolInfoWithWs symbolInfo = (SymbolInfoWithWs) obj;
        return symbolInfo.webSocketData.toString().equals(this.webSocketData.toString());
    }
}
