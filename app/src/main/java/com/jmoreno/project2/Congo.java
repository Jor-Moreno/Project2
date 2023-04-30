package com.jmoreno.project2;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.jmoreno.project2.db.AppDataBase;

@Entity(tableName = AppDataBase.CONGO_TABLE)
public class Congo {

    @PrimaryKey(autoGenerate = true)
    private int mItemId;

    private String mUserId;

    private String mItemName;
    private double mPrice;
    private int mAmount;

    public Congo(String itemName, double price, int amount) {
        mItemName = itemName;
        mPrice = price;
        mAmount = amount;

    }

    @Override
    public String toString() {
        return "Item #" + mItemId + "\n"+
                "ItemName: " + mItemName + "\n"+
                "Price: $" + mPrice +"\n"+
                "Amount Available: " + mAmount;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public int getItemId() {
        return mItemId;
    }

    public void setItemId(int itemId) {
        mItemId = itemId;
    }

    public String getItemName() {
        return mItemName;
    }

    public void setItemName(String itemName) {
        mItemName = itemName;
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double price) {
        mPrice = price;
    }

    public int getAmount() {
        return mAmount;
    }

    public void setAmount(int amount) {
        mAmount = amount;
    }
}
