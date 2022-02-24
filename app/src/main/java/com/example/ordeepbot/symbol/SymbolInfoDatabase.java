package com.example.ordeepbot.symbol;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Trade.class}, version = 1)
public abstract class SymbolInfoDatabase extends RoomDatabase {
    public abstract SymbolInfoDao taskDao();
}