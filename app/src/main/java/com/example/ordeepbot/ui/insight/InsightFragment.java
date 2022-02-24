package com.example.ordeepbot.ui.insight;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.ordeepbot.MainActivity;
import com.example.ordeepbot.R;
import com.example.ordeepbot.databinding.FragmentInsightBinding;

import java.lang.ref.WeakReference;

import jp.wasabeef.recyclerview.animators.LandingAnimator;

public class InsightFragment extends Fragment {

    private FragmentInsightBinding binding;
    private WeakReference<MainActivity> mainActivityWeakReference;
    private String lastPairWhitelist = "";


    @SuppressLint("ClickableViewAccessibility")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        binding = FragmentInsightBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mainActivityWeakReference = new WeakReference<>(((MainActivity) getContext()));

        String pairWhitelist = mainActivityWeakReference.get().getPreferences().getString("pairWhitelist", "");

        if (binding.recyclerInsight.getAdapter() == null || !lastPairWhitelist.equals(pairWhitelist)) {
            binding.recyclerInsight.setItemAnimator(new LandingAnimator());
            binding.recyclerInsight.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.recyclerInsight.setAdapter(mainActivityWeakReference.get().symbolAdapter);
            binding.recyclerInsight.setHasFixedSize(true);
        }

        binding.recyclerInsight.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == 2 && mainActivityWeakReference.get().coins.size() > 0) {
                mainActivityWeakReference.get().canUpdateList = false;
                mainActivityWeakReference.get().period = 1000;
            } else {
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    mainActivityWeakReference.get().canUpdateList = true;
                    mainActivityWeakReference.get().period = 5000;
                }, 750);

            }
            return false;
        });

        mainActivityWeakReference.get().currentFragment = this;
       // mainActivityWeakReference.get().connectWebsocketForPairWhitelist();
        lastPairWhitelist = pairWhitelist;

        return root;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Toast.makeText(getContext(), item.getTitle(), Toast.LENGTH_SHORT).show();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.insight_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        mainActivityWeakReference.get().connectWebsocketForPairWhitelist();
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}