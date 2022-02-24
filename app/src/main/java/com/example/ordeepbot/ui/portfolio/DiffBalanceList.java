package com.example.ordeepbot.ui.portfolio;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class DiffBalanceList extends DiffUtil.Callback {
    private final List<Balance> oldList;
    private final List<Balance> newList;

    public DiffBalanceList(List<Balance> oldList, List<Balance> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        Balance newContact = newList.get(newItemPosition);
        Balance oldContact = oldList.get(oldItemPosition);

        Bundle bundle = new Bundle();

        if (!newContact.getAsset().equals(oldContact.getAsset())) {
            bundle.putString("asset", newContact.getAsset());
        }

        if (newContact.getFree() != (oldContact.getFree())) {
            bundle.putString("free", String.valueOf(newContact.getFree()));
        }

        if (newContact.getLocked() != (oldContact.getLocked())) {
            bundle.putString("diff", String.valueOf(newContact.getLocked()));
        }

        if (bundle.size() == 0) {
            return null;
        }
        return bundle;
    }
}
