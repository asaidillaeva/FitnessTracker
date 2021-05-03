package com.example.fitnesstracker.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.fitnesstracker.RunItem;

import java.util.List;


@Dao
public interface RunDao {

    @Query("SELECT * FROM runItem WHERE rid = :id LIMIT 1")
    RunItem findItemById(long id);


    @Query("DELETE FROM runItem WHERE rid = :id")
    void delete(long id);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(RunItem item);

    @Query("DELETE FROM runItem")
    void deleteAll();

    @Query("SELECT * FROM runItem ORDER BY nameOfRun ASC")
    List<RunItem> getAllRunItems();
}