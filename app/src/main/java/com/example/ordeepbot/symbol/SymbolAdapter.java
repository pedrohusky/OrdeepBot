package com.example.ordeepbot.symbol;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ordeepbot.R;
import com.example.ordeepbot.indicators.Indicators;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

public class SymbolAdapter extends RecyclerView.Adapter<SymbolAdapter.ViewHolder> {

    private final ArrayList<SymbolInfoWithWs> symbolInfoArrayList;

    ColorStateList cardDefaultColor;

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public SymbolAdapter(ArrayList<SymbolInfoWithWs> dataSet) {
        symbolInfoArrayList = dataSet;
    }

    private static String format(Object string, int precision) {
        return String.format("%." + precision + "f", string);
    }

    public void updateSymbolsInfo(ArrayList<SymbolInfoWithWs> newsymbolInfoArrayList) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffSymbolList(symbolInfoArrayList, newsymbolInfoArrayList));
                diffResult.dispatchUpdatesTo(SymbolAdapter.this);
                symbolInfoArrayList.clear();
                symbolInfoArrayList.addAll(newsymbolInfoArrayList);
            }
        });
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.symbol_values, viewGroup, false);
        cardDefaultColor = ((CardView) view.findViewById(R.id.cardRecycler)).getCardBackgroundColor();
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.setCurrentPosition(position);

        int currentSymbolID = symbolInfoArrayList.get(position).getSymbolIconId();
        int currentAssetID = symbolInfoArrayList.get(position).getAssetIconId();
        String currentSymbolName = symbolInfoArrayList.get(position).getSymbolName();
        float currentSymbolPrice = symbolInfoArrayList.get(position).getWebSocketData().getClose();
        String currentSymbolDiff = symbolInfoArrayList.get(position).getWebSocketData().getPercentDifference();
        Indicators indicators = symbolInfoArrayList.get(position).getWebSocketData().getIndicators();
        String currentHuskyIndicator = "Trend: " + indicators.getHuskyIndicator().getState() + " - Strength: " + format(indicators.getHuskyIndicator().getStrength(), 3);
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        if (symbolInfoArrayList.get(position) != viewHolder.getSymbolInfo()) {
            if (viewHolder.getSymbolName().getText() != currentSymbolName) {
                viewHolder.getSymbolName().setText(currentSymbolName);
            }
            if (!viewHolder.lastLoadedPrice.equals("$" + currentSymbolPrice)) {
                viewHolder.getSymbolPrice().setText("$" + currentSymbolPrice);
                if (currentSymbolPrice > Float.parseFloat(viewHolder.lastLoadedPrice.replace("$", ""))) {
                    viewHolder.getSymbolPrice().setTextColor(Color.GREEN);
                } else {
                    viewHolder.getSymbolPrice().setTextColor(Color.RED);
                }
                viewHolder.lastLoadedPrice = "$" + currentSymbolPrice;
            }
            if (!viewHolder.lastLoadedDiff.equals(currentSymbolDiff)) {
                viewHolder.getSymbolDiff().setText(currentSymbolDiff);

                if (Float.parseFloat(currentSymbolDiff.replace("%", "")) > 0) {
                    viewHolder.cardSymbolDiff.setCardBackgroundColor(Color.GREEN);
                } else {
                    viewHolder.cardSymbolDiff.setCardBackgroundColor(Color.RED);
                }

                viewHolder.lastLoadedDiff = currentSymbolDiff;
            }

            if (!viewHolder.lastHuskyIndicator.equals(currentHuskyIndicator)) {
                viewHolder.huskyTrendTextView.setText(currentHuskyIndicator);

                viewHolder.emaTextView.setText("EMA (5): " + format(indicators.getEmaIndicator().getEma5(), 4) + " - EMA (10): " + format(indicators.getEmaIndicator().getEma10(), 4) + " - EMA (20): " + format(indicators.getEmaIndicator().getEma20(), 4));
                viewHolder.rsiTextView.setText("RSI (6): " + format(indicators.getRsiIndicator().getRsiResult(), 2));
                viewHolder.lastHuskyIndicator = currentHuskyIndicator;
            }

            if (viewHolder.lastLoadedSymbolID != currentSymbolID && currentSymbolID != 0) {
                Glide.with(viewHolder.symbolIcon.getContext()).load(currentSymbolID).into(viewHolder.symbolIcon);
                viewHolder.lastLoadedSymbolID = currentSymbolID;

            }

            if (viewHolder.lastLoadedAssetID != currentAssetID && currentAssetID != 0) {
                Glide.with(viewHolder.assetIcon.getContext()).load(currentAssetID).into(viewHolder.assetIcon);
                viewHolder.lastLoadedAssetID = currentAssetID;
            }

         /*   viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    SymbolInfoWithWs s = symbolInfoArrayList.get(viewHolder.getCurrentPosition());
                    symbolInfoArrayList.set(viewHolder.getCurrentPosition(), s);
                    //notifyItemChanged(viewHolder.getCurrentPosition());
                    ObjectAnimator scaleDown = ObjectAnimator.ofFloat(view, "scaleY", 0.75f);
                    scaleDown.setDuration(150);
                    ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(view, "scaleX", 0.75f);
                    scaleUpX.setDuration(150);
                    scaleDown.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            ObjectAnimator scaleUp = ObjectAnimator.ofFloat(view, "scaleY", 1f);
                            scaleUp.setDuration(150);
                            ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(view, "scaleX", 1f);
                            scaleUpX.setDuration(150);
                            scaleUpX.start();
                            scaleUp.start();
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });
                    scaleDown.start();
                    scaleUpX.start();
                    Toast.makeText(view.getContext(), "Position: " + viewHolder.getCurrentPosition(), Toast.LENGTH_SHORT).show();
                    return true;
                }
            });
          */




        }

        viewHolder.setSymbolInfo(symbolInfoArrayList.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return symbolInfoArrayList.size();
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView symbolName;
        private final TextView symbolPrice;
        private final CardView cardView;
        private final TextView symbolDiff;
        private final TextView huskyTrendTextView;
        private final TextView emaTextView;
        private final TextView rsiTextView;
        private final CardView cardSymbolDiff;
        private final ImageView symbolIcon;
        private final ImageView assetIcon;
        private final ExpandableLayout expandableLayout;
        private SymbolInfoWithWs SymbolInfoWithWs;
        private int lastLoadedSymbolID = -1;
        private int lastLoadedAssetID = -1;
        private String lastLoadedPrice = "$0";
        private String lastLoadedDiff = "0%";
        private String lastHuskyIndicator = "";
        private int currentPosition;

        public ViewHolder(View view) {
            super(view);
            symbolName = view.findViewById(R.id.symbolName);
            symbolPrice = view.findViewById(R.id.symbolPrice);
            symbolDiff = view.findViewById(R.id.symbolDifference);
            huskyTrendTextView = view.findViewById(R.id.huskyTrendTextView);
            emaTextView = view.findViewById(R.id.emaTextView);
            rsiTextView = view.findViewById(R.id.rsiTextView);
            symbolIcon = view.findViewById(R.id.symbolIcon);
            assetIcon = view.findViewById(R.id.assetIcon);
            cardView = view.findViewById(R.id.cardRecycler);
            cardSymbolDiff = view.findViewById(R.id.cardDifference);
            expandableLayout = view.findViewById(R.id.indicatorExpandableLayout);
            view.findViewById(R.id.expandLayout).setOnClickListener(expandLayout -> {
                expandableLayout.toggle();
                //   ((MainActivity)view.getContext()).clickedSymbol = symbolInfoArrayList.get(viewHolder.getCurrentPosition());
                // viewHolder.itemView.setOnClickListener(null);
                // ((MainActivity)view.getContext()).navController.navigate(R.id.itemFragment);
                // ((MainActivity)view.getContext()).currentFragment.onPause();
            });
        }

        public int getCurrentPosition() {
            return currentPosition;
        }

        public void setCurrentPosition(int currentPosition) {
            this.currentPosition = currentPosition;
        }

        public TextView getSymbolName() {
            return symbolName;
        }

        public TextView getSymbolPrice() {
            return symbolPrice;
        }

        public ImageView getSymbolIcon() {
            return symbolIcon;
        }

        public ImageView getAssetIcon() {
            return assetIcon;
        }

        public SymbolInfoWithWs getSymbolInfo() {
            return SymbolInfoWithWs;
        }

        public void setSymbolInfo(SymbolInfoWithWs SymbolInfoWithWs) {
            this.SymbolInfoWithWs = SymbolInfoWithWs;
        }

        public TextView getSymbolDiff() {
            return symbolDiff;
        }
    }

}