package com.example.ordeepbot;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ordeepbot.symbol.Trade;

import java.util.ArrayList;

public class TradeAdapter extends RecyclerView.Adapter<TradeAdapter.ViewHolder> {

    private final ArrayList<Trade> balanceArrayList;

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public TradeAdapter(ArrayList<Trade> dataSet) {
        balanceArrayList = dataSet;
    }

   /* public void updateSymbolsInfo(ArrayList<Balance> newsymbolInfoArrayList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffBalanceList(balanceArrayList, newsymbolInfoArrayList));
        diffResult.dispatchUpdatesTo(this);
        balanceArrayList.clear();
        balanceArrayList.addAll(newsymbolInfoArrayList);
    }
*/
    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trade_view, viewGroup, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.tradeSymbol.setText(balanceArrayList.get(position).getSymbol());
        viewHolder.tradeLastBuyPrice.setText("Last Buy Price: " + String.valueOf(balanceArrayList.get(position).getLastBuyPrice()));
        viewHolder.tradeAmount.setText("Amount bought: " +String.valueOf(balanceArrayList.get(position).getAmountBought()));
        viewHolder.tradeProfitPrice.setText("Profit price: " + balanceArrayList.get(position).getProfitPrice());
        viewHolder.tradeLossPrice.setText("Loss price: " + balanceArrayList.get(position).getLossPrice());
        viewHolder.tradeProfitDifference.setText("Difference for profit: " + balanceArrayList.get(position).getDifferenceToProfit());
        viewHolder.tradeLossDifference.setText("Difference for loss: " + balanceArrayList.get(position).getDifferenceToLoss());
        viewHolder.tradeCurrentPrice.setText("Current Price: " + balanceArrayList.get(position).getCurrentPrice());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return balanceArrayList.size();
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tradeSymbol;
        private final TextView tradeLastBuyPrice;
        private final TextView tradeAmount;
        private final TextView tradeProfitPrice;
        private final TextView tradeLossPrice;
        private final TextView tradeProfitDifference;
        private final TextView tradeLossDifference;
        private final TextView tradeCurrentPrice;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            tradeSymbol = view.findViewById(R.id.tradeSymbol);
            tradeLastBuyPrice = view.findViewById(R.id.tradeLastBuyPrice);
            tradeAmount = view.findViewById(R.id.tradeAmount);
            tradeProfitPrice = view.findViewById(R.id.tradeProfitPrice);
            tradeLossPrice = view.findViewById(R.id.tradeLossPrice);
            tradeProfitDifference = view.findViewById(R.id.tradeProfitDifference);
            tradeLossDifference = view.findViewById(R.id.tradeLossDiference);
            tradeCurrentPrice = view.findViewById(R.id.tradeCurrentPrice);

        }

    }

}