package com.example.ordeepbot;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.room.Room;

import com.example.ordeepbot.databinding.ActivityMainBinding;
import com.example.ordeepbot.indicators.EMA;
import com.example.ordeepbot.indicators.HuskyIndicator;
import com.example.ordeepbot.indicators.Indicators;
import com.example.ordeepbot.indicators.RSI;
import com.example.ordeepbot.symbol.BinanceInterval;
import com.example.ordeepbot.symbol.Candlestick;
import com.example.ordeepbot.symbol.SymbolAdapter;
import com.example.ordeepbot.symbol.SymbolInfoDatabase;
import com.example.ordeepbot.symbol.SymbolInfoWithWs;
import com.example.ordeepbot.symbol.SymbolWsData;
import com.example.ordeepbot.symbol.Trade;
import com.example.ordeepbot.ui.global_options.GlobalDefaultSettings;
import com.example.ordeepbot.ui.portfolio.Balance;
import com.example.ordeepbot.ui.trades.TradesFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import binance.ApiCallback;
import binance.ApiMethods;
import binance.BinanceApi;
import nl.joery.animatedbottombar.AnimatedBottomBar;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class MainActivity extends AppCompatActivity {

    public final String[] fiatNames = {"AED", "ARS", "AUD", "BHD", "BOB", "BRL", "BUSD", "CAD", "CLP", "CNY",
            "COP", "CZK", "DZD", "EGP", "EUR", "GBP", "GEL", "GHS", "HKD", "IDR", "INR", "JPY",
            "KES", "KZT", "LBP", "LKR", "MAD", "MXN", "NGN", "OMR", "PAB", "PEN", "PHP", "PKR",
            "PLN", "PYG", "RUB", "SAR", "SDG", "SEK", "SGD", "THB", "TND", "TRY", "TWD", "UAH",
            "UGX", "USDT", "TUSD", "USD", "UYU", "VES", "VND", "ZAR", "CHF", "DKK", "NZD", "AZN", "BGN", "HRK",
            "HUF", "ILS", "ISK", "RON"};
    public final Handler handler = new Handler(Looper.myLooper());
    public final SymbolAdapter symbolAdapter = new SymbolAdapter(new ArrayList<>());
    public final ArrayList<SymbolInfoWithWs> coins = new ArrayList<>();
    private final String[] namesToRemove = {"UP", "DOWN", "BULL", "BEAR"};
    private final ArrayList<WebSocket> sockets = new ArrayList<>();
    public ApiMethods binance;
    public NavController navController;
    public SymbolInfoWithWs clickedSymbol;
    public Fragment currentFragment;
    public boolean canUpdateList = true;
    public int period = 5000;
    public final Runnable updateFromServer = new Runnable() {
        @Override
        public void run() {
            if (canUpdateList) {
                //connectWebsocketForPairWhitelist();
            }
            handler.postDelayed(updateFromServer, period);

        }
    };
    public GlobalDefaultSettings globalDefaultSettings;
    public ArrayList<Balance> balances;
    public ArrayList<String> balanceAssets = new ArrayList<>();
    public ArrayList<String> allSymbols = new ArrayList<>();
    public ArrayList<String> monitoredSymbols = new ArrayList<>();
    public LinkedHashMap<String, ArrayList<Candlestick>> coinsCandles = new LinkedHashMap<>();
    public LinkedHashMap<String, String> coinsPriceDifference = new LinkedHashMap<>();
    public LinkedHashMap<String, Indicators> coinsIndicators = new LinkedHashMap<>();
    public LinkedHashMap<String, ArrayList<Trade>> coinsTrades = new LinkedHashMap<>();
    public boolean isCurrentUpdatingList = false;
    private ActivityMainBinding binding;
    private SymbolInfoDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        generateGlobalSettings();
        monitoredSymbols.addAll(Arrays.asList(getPreferences().getString("monitored", "").split(",")));

        BinanceApi.init("pgorbGAX1ZN2SR7JAstO0MSDmS6xY2TDPVlaDehh9Z5TqXpqZVoTiBe78NskC8g2", "fH5pzAVEvawR2h8qv1wVeTYs5F3MtCL01hsZHfjtP07YesKv7b9gIlHghkEFCTgC");
        binance = ApiMethods.getInstance();
        db = Room.databaseBuilder(getApplicationContext(), SymbolInfoDatabase.class, "trades").build();
      /*  new StyleableToast
                .Builder(this)
                .text("Binance API connected successfully!")
                .show();
      */
        getAccountBalanceAssets();
        getAllSymbols();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        AnimatedBottomBar bottom_bar = findViewById(R.id.bottom_bar);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_portfolio, R.id.navigation_trades, R.id.navigation_insight, R.id.navigation_globalOptions)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        //NavigationUI.setupWithNavController(binding.bottomBar, navController);

        bottom_bar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int i, @Nullable AnimatedBottomBar.Tab tab, int i1, @NonNull AnimatedBottomBar.Tab tab1) {
                assert tab != null;
                switch (tab1.getTitle()) {
                    case "Portfolio":
                        navController.navigate(R.id.navigation_portfolio);
                        break;
                    case "Trades":
                        navController.navigate(R.id.navigation_trades);
                        break;
                    case "Insight":
                        navController.navigate(R.id.navigation_insight);
                        break;
                    case "Configure":
                        navController.navigate(R.id.navigation_globalOptions);
                }
            }

            @Override
            public void onTabReselected(int i, @NonNull AnimatedBottomBar.Tab tab) {

            }
        });
    }

    public boolean filterSymbols(String symbol) {
        boolean canPass = true;
        String[] names = getPreferences().getString("pairsWhitelist", Arrays.toString(fiatNames)).trim().split(",");
        for (String fiatName : names) {
            if (symbol.endsWith(fiatName) | symbol.startsWith(fiatName)) {
                for (String nameToRemove : namesToRemove) {
                    if (symbol.contains(nameToRemove)) {
                        canPass = false;
                        break;
                    }
                }
            }
        }
        return canPass;
    }

    public String[] sanitizeSymbol(String symbol) {
        String sanitizedSymbol = "";
        String sanitizedAsset = "";

        for (String fiat : fiatNames) {
            if (symbol.endsWith(fiat)) {
                sanitizedSymbol = symbol.split(fiat)[0].toLowerCase();
                sanitizedAsset = symbol.replaceFirst(sanitizedSymbol.toUpperCase(), "").toLowerCase();
                break;
            }
        }
        for (String nameToRemove : namesToRemove) {
            if (symbol.endsWith(nameToRemove)) {
                sanitizedSymbol = sanitizedSymbol.replaceFirst(nameToRemove, "");
                break;
            }
        }
        return new String[]{sanitizedSymbol, sanitizedAsset};
    }

    public void getAllSymbols() {
        allSymbols.clear();
        binance.getAllPrices(new ApiCallback() {
            @Override
            public void onSuccess(String s) {
                JSONArray jArray = null;
                try {
                    jArray = new JSONArray(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < Objects.requireNonNull(jArray).length(); i++) {
                    try {
                        JSONObject oneObject = jArray.getJSONObject(i);
                        // Pulling items from the array
                        String symbol = oneObject.getString("symbol");

                        allSymbols.add(symbol);
                    } catch (JSONException e) {
                        // Oops
                    }
                }
            }

            @Override
            public void onFailure(String s) {

            }
        });

    }

    public void connectWebsocketForPairWhitelist() {
        clearSockets();
        for (String symbol : getPreferences().getString("pairsWhitelist", "").split(",")) {
            binance.wsKline(symbol, "1m", new WebSocketListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onMessage(WebSocket webSocket, String text) {
                    super.onMessage(webSocket, text);
                    JSONObject jArray = null;
                    try {
                        jArray = new JSONObject(text);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject kData = Objects.requireNonNull(jArray).getJSONObject("k");
                        JSONArray data = kData.toJSONArray(kData.names());
                        String[] klineData = Objects.requireNonNull(data).toString().replace("[", "").replace("]", "").split(",");
                        SymbolInfoWithWs symbolInfoWithWs = new SymbolInfoWithWs(
                                MainActivity.this,
                                symbol,
                                "lorem ipsum",
                                new GlobalDefaultSettings(getPreferences()), new SymbolWsData(symbol,
                                Float.parseFloat(klineData[6].replaceAll("^\"|\"$", "")),
                                Float.parseFloat(klineData[7].replaceAll("^\"|\"$", "")),
                                Float.parseFloat(klineData[8].replaceAll("^\"|\"$", "")),
                                Float.parseFloat(klineData[9].replaceAll("^\"|\"$", "")),
                                Float.parseFloat(klineData[10].replaceAll("^\"|\"$", "")),
                                coinsPriceDifference.getOrDefault(symbol, "0%"),
                                coinsIndicators.getOrDefault(symbol, new Indicators())));

                        int i = 0;
                        boolean toAdd = true;
                        for (SymbolInfoWithWs symbolInfo : coins) {
                            if (symbolInfo.getSymbolName().equals(symbol)) {
                                coins.set(i, symbolInfoWithWs);
                                toAdd = false;
                                break;
                            }
                            i++;
                        }
                        if (toAdd) {
                            coins.add(symbolInfoWithWs);
                        }

                        updateCoinInfo(symbolInfoWithWs);

                        symbolAdapter.updateSymbolsInfo(coins);




                    } catch (JSONException e) {
                        System.out.println(e);
                        // Oops
                    }
                }

                @Override
                public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
                    super.onFailure(webSocket, t, response);
                    System.out.println(t);
                }

                @Override
                public void onOpen(WebSocket webSocket, okhttp3.Response response) {
                    if (!sockets.contains(webSocket)) {
                        sockets.add(webSocket);
                    }
                    super.onOpen(webSocket, response);
                }
            });
        }
    }

    public void getPriceChange(String symbol) {
        // for (String symbol : getPreferences().getString("pairsWhitelist", "").split(",")) {
        binance.get24hr(symbol, new ApiCallback() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(String s) {
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    String change = jsonObject.getString("priceChangePercent");
                    if (coinsPriceDifference.containsKey(symbol)) {
                        coinsPriceDifference.replace(symbol, change + "%");
                    } else {
                        coinsPriceDifference.put(symbol, change + "%");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String s) {

            }
        });
        // }
    }

    /**
     * Kline/candlestick bars for a symbol. Klines are uniquely identified by their open time.
     * <p>
     * map      Name	        Type	Mandatory	Description
     * symbol	    STRING	YES
     * interval	    ENUM	YES
     * limit	    INT 	NO	Default 500; max 500.
     * startTime	LONG	NO
     * endTime	    LONG	NO
     * <p>
     * [
     * [
     * 1499040000000,      // Open time
     * "0.01634790",       // Open
     * "0.80000000",       // High
     * "0.01575800",       // Low
     * "0.01577100",       // Close
     * "148976.11427815",  // Volume
     * 1499644799999,      // Close time
     * "2434.19055334",    // Quote asset volume
     * 308,                // Number of trades
     * "1756.87402397",    // Taker buy base asset volume
     * "28.46694368",      // Taker buy quote asset volume
     * "17928899.62484339" // Ignore.
     * ]
     * ]
     */
    public void getSymbolCandles(String symbol, BinanceInterval interval, int limit) {
        ArrayList<Candlestick> candles = new ArrayList<>();
        Map map = new HashMap();
        map.put("symbol", symbol);
        map.put("interval", interval);
        map.put("limit", limit);
        binance.getKlines(map, new ApiCallback() {
            @Override
            public void onSuccess(String s) {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String[] data = jsonArray.get(i).toString().replace("[", "").replace("]", "").split(",");

                        candles.add(new Candlestick(
                                String.valueOf(data[1]).replaceAll("^\"|\"$", ""),
                                String.valueOf(data[2]).replaceAll("^\"|\"$", ""),
                                String.valueOf(data[3]).replaceAll("^\"|\"$", ""),
                                String.valueOf(data[4]).replaceAll("^\"|\"$", ""),
                                String.valueOf(data[5]).replaceAll("^\"|\"$", ""),
                                String.valueOf(data[7]).replaceAll("^\"|\"$", "")
                        ));
                    }
                    coinsCandles.put(symbol, candles);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String s) {

            }
        });
    }

    public void saveTrade(SymbolInfoWithWs symbolInfoWithWs, Trade trade) {

        class SaveTrade extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                db.taskDao().insertOrUpdate(trade);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
            }
        }

        SaveTrade st = new SaveTrade();
        st.execute();
    }

    public SharedPreferences getPreferences() {
        SharedPreferences sharedPref = getSharedPreferences(
                "globalSettings", Context.MODE_PRIVATE);
        return sharedPref;
    }

    public void setPreferences(Bundle prefs, String name, int type) {
        SharedPreferences sharedPref = getSharedPreferences(
                "globalSettings", Context.MODE_PRIVATE);

        if (type == 0) {

            sharedPref.edit().putFloat(name, prefs.getFloat(name, -999)).apply();
        } else if (type == 1) {
            sharedPref.edit().putString(name, prefs.getString(name, "")).apply();

        } else if (type == 2) {
            sharedPref.edit().putBoolean(name, prefs.getBoolean(name, false)).apply();

        } else if (type == 3) {
            sharedPref.edit().putString(name, prefs.getString(name, "")).apply();
        }

    }

    public void generateGlobalSettings() {
        SharedPreferences sharedPreferences = getPreferences();
        globalDefaultSettings = new GlobalDefaultSettings(sharedPreferences);
    }

    public void getAccountBalance() {
        binance.getCount(new ApiCallback() {
            @Override
            public void onSuccess(String s) {
                JSONObject jsonObject = null;
                JSONArray jsonArray = null;
                ArrayList<Balance> balanceList = new ArrayList<>();
                try {
                    jsonObject = new JSONObject(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jsonArray = jsonObject.getJSONArray("balances");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject oneObject = jsonArray.getJSONObject(i);
                            String asset = oneObject.getString("asset");
                            float free = Float.parseFloat(oneObject.getString("free"));
                            float locked = Float.parseFloat(oneObject.getString("locked"));
                            //if (free != 0 | locked != 0) {
                            balanceList.add(new Balance(asset, free, locked));
                            //}

                            // Pulling items from the array

                        } catch (JSONException e) {
                            // Oops
                        }
                    }
                    balances = balanceList;
                }
            }

            @Override
            public void onFailure(String s) {
                System.out.println(s);

            }
        });

    }

    public void getAccountBalanceAssets() {
        binance.getCount(new ApiCallback() {
            @Override
            public void onSuccess(String s) {
                JSONObject jsonObject = null;
                JSONArray jsonArray = null;
                ArrayList<String> assetList = new ArrayList<>();
                try {
                    jsonObject = new JSONObject(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    jsonArray = jsonObject.getJSONArray("balances");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject oneObject = jsonArray.getJSONObject(i);
                            String asset = oneObject.getString("asset");
                            //if (free != 0 | locked != 0) {
                            assetList.add(asset);
                            //}

                            // Pulling items from the array

                        } catch (JSONException e) {
                            // Oops
                        }
                    }
                    balanceAssets = assetList;
                }
            }

            @Override
            public void onFailure(String s) {
                System.out.println(s);

            }
        });

    }

    public void increaseViewSize(View view, int size) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(view.getMeasuredHeight(), size);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Integer animatedValue = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = animatedValue;
                view.setLayoutParams(layoutParams);
            }
        });
        valueAnimator.start();
    }

    public void decreaseViewSize(View view) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(view.getMeasuredHeight(), 0);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Integer animatedValue = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = animatedValue;
                view.setLayoutParams(layoutParams);
            }
        });
        valueAnimator.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // inflater.inflate(R.menu.app_bar_menus, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setupIndicators(String symbol, ArrayList<Candlestick> candlesticks) {
        Indicators indicator = new Indicators();
        HuskyIndicator husky = new HuskyIndicator(candlesticks);
        EMA ema = new EMA(candlesticks);
        RSI rsi = new RSI(candlesticks);
        indicator.setEmaIndicator(ema);
        indicator.setHuskyIndicator(husky);
        indicator.setRsiIndicator(rsi);

        if (coinsIndicators.containsKey(symbol)) {
            coinsIndicators.replace(symbol, indicator);
        } else {
            coinsIndicators.put(symbol, indicator);
        }
    }

    public void clearSockets() {
        for (WebSocket ws : sockets) {
            if (ws != null) {
                ws.cancel();
                binance.closeWs();
            }
        }
       // Toast.makeText(this, "Sockets size: " + sockets.size(), Toast.LENGTH_SHORT).show();
        sockets.clear();
    }

    public void closeWsAndClearData() {
        clearSockets();
        coins.clear();
        coinsIndicators.clear();
        coinsPriceDifference.clear();
        coinsCandles.clear();
        symbolAdapter.updateSymbolsInfo(coins);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void canBuy(SymbolInfoWithWs symbolInfoWithWs) {
        Indicators indicators = symbolInfoWithWs.getWebSocketData().getIndicators();
        if (symbolInfoWithWs.getWebSocketData().getClose() <= indicators.getEmaIndicator().getEma5()
                && indicators.getEmaIndicator().getEma5() <= indicators.getEmaIndicator().getEma10()
                && indicators.getEmaIndicator().getEma10() <= indicators.getEmaIndicator().getEma20()
                && indicators.getEmaIndicator().getEma5() <= indicators.getEmaIndicator().getEma20()
                && indicators.getHuskyIndicator().getState().equals("Rising")
                && indicators.getRsiIndicator().getRsiResult() <= 50) {
            Trade trade = new Trade(symbolInfoWithWs, symbolInfoWithWs.getSymbolName(), symbolInfoWithWs.getWebSocketData().getClose(), 10/symbolInfoWithWs.getWebSocketData().getClose());
            System.out.println("Should buy now...");
            if (!coinsTrades.containsKey(symbolInfoWithWs.getSymbolName())) {
                ArrayList<Trade> symbolTrades = new ArrayList<>();
                symbolTrades.add(trade);
                coinsTrades.put(symbolInfoWithWs.getSymbolName(), symbolTrades);
                //saveTrade(symbolInfoWithWs, trade);
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void refreshTrades(SymbolInfoWithWs symbolInfoWithWs) {

        if (currentFragment instanceof TradesFragment) {
            TradesFragment tradesFragment = ((TradesFragment)currentFragment);
            for (int i = 0; i < coinsTrades.size(); i++) {
                for (Trade trade : Objects.requireNonNull(coinsTrades.get(i))) {
                    trade.updateCurrentPrice(symbolInfoWithWs.getWebSocketData().getClose());
                }
            }
            tradesFragment.updateRecyclerWithTrades();
        }



    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateCoinInfo(SymbolInfoWithWs symbolInfoWithWs) {
        getPriceChange(symbolInfoWithWs.getSymbolName());
        getSymbolCandles(symbolInfoWithWs.getSymbolName(), BinanceInterval.THREE_MIN, 10);
        setupIndicators(symbolInfoWithWs.getSymbolName(), coinsCandles.getOrDefault(symbolInfoWithWs.getSymbolName(), new ArrayList<>()));
        canBuy(symbolInfoWithWs);
        refreshTrades(symbolInfoWithWs);
    }
    public String generateUniqueID() {
        return UUID.randomUUID().toString();
    }
}