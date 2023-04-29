package com.jmoreno.project2.db;

import android.view.ViewDebug;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.jmoreno.project2.Congo;
import com.jmoreno.project2.User;

import java.util.List;

@Dao
public interface CongoDAO {

    @Insert
    void insert(Congo... congos);

    @Update
    void update(Congo... congos);

    @Delete
    void delete(Congo... congo);

    @Query("SELECT * FROM " + AppDataBase.CONGO_TABLE)
    List<Congo> getCongo();

    @Query("SELECT * FROM " + AppDataBase.CONGO_TABLE + " WHERE mItemId = :itemId")
    List<Congo> getCongoById(int itemId);

    @Query("SELECT * FROM " + AppDataBase.CONGO_TABLE + " WHERE mUserId = :userId")
    List<Congo> getCongoByUserId(int userId);

    @Query("SELECT * FROM " + AppDataBase.CONGO_TABLE + " WHERE mItemName = :itemName")
    Congo getCongoByName(String itemName);

    @Insert
    void insert(User...users);

    @Update
    void update(User...users);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE)
    List<User> getAllUsers();

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE + " WHERE mUsername = :username")
    User getUserByUsername(String username);

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE + " WHERE mUserid = :userId")
    User getUserByUserId(int userId);

}
