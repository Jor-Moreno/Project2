package com.jmoreno.project2.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.jmoreno.project2.Cart;
import com.jmoreno.project2.Congo;
import com.jmoreno.project2.User;

@Database(entities = {Congo.class, User.class, Cart.class}, version = 5)
public abstract class AppDataBase extends RoomDatabase {
    public static final String DATABASE_NAME = "Congo.gb";
    public static final String CONGO_TABLE = "congo_table";
    public static final String USER_TABLE = "USER_TABLE";
    public static final String CART_TABLE = "CART_TABLE";


    public static volatile AppDataBase instance;
    private static final Object LOCK = new Object();
    public abstract CongoDAO getCongoDAO();

    public static AppDataBase getInstance(Context context){
        if(instance == null){
            synchronized (LOCK){
                if(instance == null){
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDataBase.class,
                    DATABASE_NAME).fallbackToDestructiveMigration().build();
                }
            }
        }
        return instance;
    }

}
