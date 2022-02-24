package com.example.ordeepbot.ui.portfolio;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ordeepbot.R;

import java.util.ArrayList;

import libs.mjn.scaletouchlistener.ScaleTouchListener;

public class PorfolioAdapter extends RecyclerView.Adapter<PorfolioAdapter.ViewHolder> {

    private final ArrayList<Balance> balanceArrayList;

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public PorfolioAdapter(ArrayList<Balance> dataSet) {
        balanceArrayList = dataSet;
    }

    public void updateSymbolsInfo(ArrayList<Balance> newsymbolInfoArrayList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffBalanceList(balanceArrayList, newsymbolInfoArrayList));
        diffResult.dispatchUpdatesTo(this);
        balanceArrayList.clear();
        balanceArrayList.addAll(newsymbolInfoArrayList);
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.balance, viewGroup, false);
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.setCurrentPosition(position);

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        int loadedAsset = viewHolder.recyclerAssetIcon.getContext().getResources().getIdentifier("drawable/" + balanceArrayList.get(position).getAsset().toLowerCase(), "drawable", viewHolder.recyclerAssetIcon.getContext().getPackageName());
        if (loadedAsset != 0) {
            Glide.with(viewHolder.recyclerAssetIcon).load(loadedAsset).into(viewHolder.recyclerAssetIcon);
        } else {
            Glide.with(viewHolder.recyclerAssetIcon).load(R.drawable.ic_home_black_24dp).into(viewHolder.recyclerAssetIcon);
        }
        viewHolder.recyclerAsset.setText(balanceArrayList.get(position).getAsset());
        viewHolder.recyclerFree.setText(String.valueOf(balanceArrayList.get(position).getFree()));
        viewHolder.recyclerLocked.setText(String.valueOf(balanceArrayList.get(position).getLocked()));

        viewHolder.itemView.setOnTouchListener(new ScaleTouchListener() {
            @Override
            public void onClick(View v) {
                //OnClickListener

            }
        });


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
        private final TextView recyclerAsset;
        private final TextView recyclerFree;
        private final TextView recyclerLocked;

        private final ImageView recyclerAssetIcon;
        private int currentPosition;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            recyclerAsset = view.findViewById(R.id.recyclerAsset);
            recyclerFree = view.findViewById(R.id.recyclerFree);
            recyclerLocked = view.findViewById(R.id.recyclerLocked);
            recyclerAssetIcon = view.findViewById(R.id.recyclerBalanceAsset);

        }

        public int getCurrentPosition() {
            return currentPosition;
        }

        public void setCurrentPosition(int currentPosition) {
            this.currentPosition = currentPosition;
        }

    }

}