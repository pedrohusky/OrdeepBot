package com.example.ordeepbot.symbol;

import android.content.Context;

import androidx.room.Room;

public class SymbolInfoDatabaseClient {

    private static SymbolInfoDatabaseClient mInstance;
    private final Context mCtx;
    //our app database object
    private final SymbolInfoDatabase appDatabase;

    private SymbolInfoDatabaseClient(Context mCtx) {
        this.mCtx = mCtx;

        //creating the app database with Room database builder
        //MyToDos is the name of the database
        appDatabase = Room.databaseBuilder(mCtx, SymbolInfoDatabase.class, "MyToDos").build();
    }

    public static synchronized SymbolInfoDatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new SymbolInfoDatabaseClient(mCtx);
        }
        return mInstance;
    }

    public SymbolInfoDatabase getAppDatabase() {
        return appDatabase;
    }
}