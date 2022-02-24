package com.example.ordeepbot.ui.global_options;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.androidbuts.multispinnerfilter.KeyPairBoolData;
import com.example.ordeepbot.MainActivity;
import com.example.ordeepbot.databinding.FragmentGlobalOptionsBinding;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GlobalOptions extends Fragment {
    String[] testFullAssets;
    String[] testAssets;
    String[] pairAssets;

    private FragmentGlobalOptionsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentGlobalOptionsBinding.inflate(inflater, container, false);

        WeakReference<MainActivity> mainActivityWeakReference = new WeakReference<>(((MainActivity) getContext()));

        SharedPreferences prefs = mainActivityWeakReference.get().getPreferences();

        binding.optionEnableBuyChip.setChecked(prefs.getBoolean("buyOn", false));
        binding.optionEnableBuyChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("buyOn", b);
                mainActivityWeakReference.get().setPreferences(bundle, "buyOn", 2);

            }
        });

        binding.optionEnableSellChip.setChecked(prefs.getBoolean("sellOn", false));
        binding.optionEnableSellChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("sellOn", b);
                mainActivityWeakReference.get().setPreferences(bundle, "sellOn", 2);

            }
        });

        binding.optionTestWalletOnChip.setChecked(prefs.getBoolean("testWalletOn", false));
        if (prefs.getBoolean("testWalletOn", false)) {
            binding.testExpandableLayout.expand(false);
            //binding.cardTestWalletAssets.setVisibility(View.VISIBLE);
        } else {
            binding.testExpandableLayout.collapse(false);
        }
        binding.optionTestWalletOnChip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("testWalletOn", b);
                mainActivityWeakReference.get().setPreferences(bundle, "testWalletOn", 2);

                binding.testExpandableLayout.toggle();

            }
        });

        binding.optionProfitValue.setText(String.valueOf(prefs.getFloat("profitPercent", 1.1f)));
        binding.optionProfitValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Bundle bundle = new Bundle();
                String editableString = editable.toString();
                if (editableString.equals("")) {
                    editableString = String.valueOf(0.0);
                }
                bundle.putFloat("profitPercent", Float.parseFloat(editableString));
                mainActivityWeakReference.get().setPreferences(bundle, "profitPercent", 0);

            }
        });

        binding.optionLossPercentageValue.setText(String.valueOf(prefs.getFloat("lossPercent", 0.9f)));
        binding.optionLossPercentageValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Bundle bundle = new Bundle();
                String editableString = editable.toString();
                if (editableString.equals("")) {
                    editableString = String.valueOf(0.0);
                }
                bundle.putFloat("lossPercent", Float.parseFloat(editableString));
                mainActivityWeakReference.get().setPreferences(bundle, "lossPercent", 0);

            }
        });

        binding.optionBalancePercentValue.setText(String.valueOf(prefs.getFloat("balancePercent", 10f)));
        binding.optionBalancePercentValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                Bundle bundle = new Bundle();
                String editableString = editable.toString();
                if (editableString.equals("")) {
                    editableString = String.valueOf(0.0);
                }
                bundle.putFloat("balancePercent", Float.parseFloat(editableString));
                mainActivityWeakReference.get().setPreferences(bundle, "balancePercent", 0);

            }
        });

        testFullAssets = mainActivityWeakReference.get().balanceAssets.toArray(new String[0]);
        testAssets = mainActivityWeakReference.get().getPreferences().getString("testWalletAssets", "").replace(" ", "").split(",");
        pairAssets = mainActivityWeakReference.get().getPreferences().getString("pairsWhitelist", "").replace(" ", "").split(",");

        List<String> testList = Arrays.asList(testAssets);
        List<String> pairList = Arrays.asList(pairAssets);
        List<KeyPairBoolData> pairBoolData = new ArrayList<>();
        List<KeyPairBoolData> testBoolData = new ArrayList<>();
        for (String a : mainActivityWeakReference.get().allSymbols) {
            pairBoolData.add(new KeyPairBoolData(a, pairList.contains(a)));
        }
        for (String a : testFullAssets) {
            testBoolData.add(new KeyPairBoolData(a, testList.contains(a)));
        }
        /**
         * Search MultiSelection Spinner (With Search/Filter Functionality)
         *
         *  Using MultiSpinnerSearch class
         */

        // Pass true If you want searchView above the list. Otherwise false. default = true.
        binding.spinnerPairs.setSearchEnabled(true);
        // A text that will display in search hint.
        binding.spinnerPairs.setSearchHint("Select your assets");
        // Set text that will display when search result not found...
        binding.spinnerPairs.setEmptyTitle("Not Data Found!");
        // If you will set the limit, this button will not display automatically.
        binding.spinnerPairs.setShowSelectAllButton(true);
        //A text that will display in clear text button
        binding.spinnerPairs.setClearText("Close & Clear");

        // Pass true If you want searchView above the list. Otherwise false. default = true.
        binding.spinnerTestWallet.setSearchEnabled(true);
        // A text that will display in search hint.
        binding.spinnerTestWallet.setSearchHint("Select your assets");
        // Set text that will display when search result not found...
        binding.spinnerTestWallet.setEmptyTitle("Not Data Found!");
        // If you will set the limit, this button will not display automatically.
        binding.spinnerTestWallet.setShowSelectAllButton(true);
        //A text that will display in clear text button
        binding.spinnerTestWallet.setClearText("Close & Clear");

        // Removed second parameter, position. Its not required now..
        // If you want to pass preselected items, you can do it while making listArray,
        // Pass true in setSelected of any item that you want to preselect
        List<String> testSelectedToString = new ArrayList<>();
        binding.spinnerTestWallet.setItems(testBoolData, items -> {
            Bundle bundle = new Bundle();
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).isSelected() && !testSelectedToString.contains(items.get(i).getName())) {

                    testSelectedToString.add(items.get(i).getName());

                } else if (!items.get(i).isSelected())
                    testSelectedToString.remove(items.get(i).getName());
            }
            bundle.putString("testWalletAssets", String.valueOf(testSelectedToString).replace(" ", "").replace("[", "").replace("]", ""));

            mainActivityWeakReference.get().setPreferences(bundle, "testWalletAssets", 1);
        });

        List<String> pairsSelectedToString = new ArrayList<>();
        binding.spinnerPairs.setItems(pairBoolData, items -> {
            Bundle bundle = new Bundle();
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).isSelected() && !pairsSelectedToString.contains(items.get(i).getName())) {

                    pairsSelectedToString.add(items.get(i).getName());

                } else if (!items.get(i).isSelected())
                    pairsSelectedToString.remove(items.get(i).getName());
            }
            bundle.putString("pairsWhitelist", String.valueOf(pairsSelectedToString).replace(" ", "").replace("[", "").replace("]", ""));

            mainActivityWeakReference.get().setPreferences(bundle, "pairsWhitelist", 1);


            mainActivityWeakReference.get().closeWsAndClearData();
            mainActivityWeakReference.get().connectWebsocketForPairWhitelist();

        });

        /**
         * If you want to set limit as maximum item should be selected is 2.
         * For No limit -1 or do not call this method.
         *
         */
        binding.spinnerTestWallet.setLimit(10, data -> Toast.makeText(binding.spinnerTestWallet.getContext(),
                "Limit exceed ", Toast.LENGTH_LONG).show());
        binding.spinnerPairs.setLimit(10, data -> Toast.makeText(binding.spinnerPairs.getContext(),
                "Limit exceed ", Toast.LENGTH_LONG).show());

        return binding.getRoot();

    }
}