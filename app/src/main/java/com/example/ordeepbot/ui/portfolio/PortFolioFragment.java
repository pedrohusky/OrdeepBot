package com.example.ordeepbot.ui.portfolio;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.ordeepbot.MainActivity;
import com.example.ordeepbot.databinding.FragmentPortfolioBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import binance.ApiCallback;
import jp.wasabeef.recyclerview.animators.LandingAnimator;

public class PortFolioFragment extends Fragment {

    private PortfolioViewModel portfolioViewModel;
    private FragmentPortfolioBinding binding;
    private WeakReference<MainActivity> mainActivityWeakReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        portfolioViewModel =
                new ViewModelProvider(this).get(PortfolioViewModel.class);

        mainActivityWeakReference = new WeakReference<>(((MainActivity) getContext()));

        binding = FragmentPortfolioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.portfolioRecycler.setLayoutManager(new GridLayoutManager(getContext(), 3));
        binding.portfolioRecycler.setHasFixedSize(true);
        binding.portfolioRecycler.setItemAnimator(new LandingAnimator());
        PorfolioAdapter porfolioAdapter = new PorfolioAdapter(new ArrayList<>());
        binding.portfolioRecycler.setAdapter(porfolioAdapter);

        SharedPreferences preferences = mainActivityWeakReference.get().getPreferences();

        String prefValue = preferences.getString("testWalletAssets", "USDT, BTC, ETH");

        if (preferences.getBoolean("testWalletOn", false)) {

            String[] splittedValues = prefValue.trim().split(",");

            ArrayList<Balance> balanceArrayList = new ArrayList<>();
            for (String s : splittedValues) {
                balanceArrayList.add(new Balance(s, 1000, 0));
            }
            porfolioAdapter.updateSymbolsInfo(balanceArrayList);

        } else {
            mainActivityWeakReference.get().binance.getCount(new ApiCallback() {
                @Override
                public void onSuccess(String s) {
                    JSONObject jsonObject = null;
                    JSONArray jsonArray = null;
                    ArrayList<Balance> balanceArrayList = new ArrayList<>();
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
                                if (free != 0 | locked != 0) {
                                    balanceArrayList.add(new Balance(asset, free, locked));
                                }

                                // Pulling items from the array

                            } catch (JSONException e) {
                                // Oops
                            }
                        }
                        porfolioAdapter.updateSymbolsInfo(balanceArrayList);
                    }
                }

                @Override
                public void onFailure(String s) {
                    System.out.println(s);

                }
            });

        }


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}