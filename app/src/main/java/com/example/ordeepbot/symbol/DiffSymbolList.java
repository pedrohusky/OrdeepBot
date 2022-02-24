package com.example.ordeepbot.symbol;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class DiffSymbolList extends DiffUtil.Callback {
    private final List<SymbolInfoWithWs> oldList;
    private final List<SymbolInfoWithWs> newList;

    public DiffSymbolList(List<SymbolInfoWithWs> oldList, List<SymbolInfoWithWs> newList) {
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
        return oldList.get(oldItemPosition).getSymbolName().equals(newList.get(newItemPosition).getSymbolName());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getWebSocketData().toString().equals(newList.get(newItemPosition).getWebSocketData().toString());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        SymbolInfoWithWs newContact = newList.get(newItemPosition);
        SymbolInfoWithWs oldContact = oldList.get(oldItemPosition);

        Bundle bundle = new Bundle();

        if (!newContact.getSymbolName().equals(oldContact.getSymbolName())) {
            bundle.putString("name", newContact.getSymbolName());
        }

        if (newContact.getWebSocketData().getClose() != (oldContact.getWebSocketData().getClose())) {
            bundle.putString("price", "$" + newContact.getWebSocketData().getClose());
        }

        if (!newContact.getWebSocketData().getPercentDifference().equals(oldContact.getWebSocketData().getPercentDifference())) {
            bundle.putString("diff", "%" + newContact.getWebSocketData().getPercentDifference());
        }

        if (newContact.getWebSocketData().getVolume() != (oldContact.getWebSocketData().getVolume())) {
            bundle.putFloat("volume", newContact.getWebSocketData().getVolume());
        }

        if (!newContact.getSymbolDescription().equals(oldContact.getSymbolDescription())) {
            bundle.putString("description", newContact.getSymbolDescription());
        }

        if (bundle.size() == 0) {
            return null;
        }
        return bundle;
    }
}
