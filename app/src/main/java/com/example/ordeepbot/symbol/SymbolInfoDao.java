package com.example.ordeepbot.symbol;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SymbolInfoDao {

    @Query("SELECT * FROM Trade")
    List<Trade> getAll();

    @Delete
    void delete(Trade Trade);

    @Update
    void update(Trade Trade);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Trade Trades);

    @Query("SELECT * from Trade WHERE id= :id")
    List<Trade> getItemById(int id);

    @Query("UPDATE Trade SET currentPrice = currentPrice + 1 WHERE id = :id")
    void updateQuantity(int id);

    default void insertOrUpdate(Trade item) {
        List<Trade> itemsFromDB = getItemById(item.getId());
        if (itemsFromDB.isEmpty())
            insert(item);
        else
            updateQuantity(item.getId());
    }
}

