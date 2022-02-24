package com.example.ordeepbot.ui.trades;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ordeepbot.MainActivity;
import com.example.ordeepbot.symbol.Trade;
import com.example.ordeepbot.TradeAdapter;
import com.example.ordeepbot.databinding.FragmentTradesBinding;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.LandingAnimator;

public class TradesFragment extends Fragment {

    private final String lastPairWhitelist = "";
    private FragmentTradesBinding binding;
    private WeakReference<MainActivity> mainActivityWeakReference;
    private ArrayList<Trade> trades = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTradesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mainActivityWeakReference = new WeakReference<>(((MainActivity) this.getContext()));
        binding.tradesRecycler.setItemAnimator(new LandingAnimator());
        binding.tradesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        mainActivityWeakReference.get().currentFragment = this;
        TradeAdapter tradeAdapter = new TradeAdapter(trades);
        binding.tradesRecycler.setAdapter(tradeAdapter);
        return root;
    }

    public void updateRecyclerWithTrades() {
        trades.clear();
        if (mainActivityWeakReference.get().coinsTrades.size() > 0) {
            for (ArrayList<Trade> ws : mainActivityWeakReference.get().coinsTrades.values()) {
                trades.addAll(ws);
            }

            TradeAdapter tradeAdapter = new TradeAdapter(trades);
            binding.tradesRecycler.setAdapter(tradeAdapter);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}