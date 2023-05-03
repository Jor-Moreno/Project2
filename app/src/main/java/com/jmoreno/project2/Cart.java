package com.jmoreno.project2;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.jmoreno.project2.db.AppDataBase;

@Entity(tableName = AppDataBase.CART_TABLE)
public class Cart {

    @PrimaryKey(autoGenerate = true)
    private int mCartId;

    private int mUserId;
    private int mCongoId;
    private int mQuantity;

    public Cart(int userId, int congoId, int quantity) {
        mUserId = userId;
        mCongoId = congoId;
        this.mQuantity = quantity;
    }

    @Override
    public String toString() {
        return "User Id: " + mUserId +
                "Item Id: " + mCongoId +
                " Quantity: " + mQuantity;
    }

    public int getCartId() {
        return mCartId;
    }

    public void setCartId(int cartId) {
        mCartId = cartId;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public int getCongoId() {
        return mCongoId;
    }

    public void setCongoId(int congoId) {
        mCongoId = congoId;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {
        this.mQuantity = quantity;
    }
}
