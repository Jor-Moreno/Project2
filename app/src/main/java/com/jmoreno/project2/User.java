package com.jmoreno.project2;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.jmoreno.project2.db.AppDataBase;

@Entity(tableName = AppDataBase.USER_TABLE)
public class User {

    @PrimaryKey(autoGenerate = true)
    private int mUserid;

    private String mUserName;
    private String mPassword;

    private int mClearance;

    public User(String userName, String password, int clearance) {
        mUserName = userName;
        mPassword = password;
        mClearance = clearance;
    }

    @Override
    public String toString() {
        String ad;
        if(mClearance == 0){
            ad = "User";
        } else {
            ad = "Admin";
        }
        return "User ID: " + mUserid + "\n" +
                "UserName: " + mUserName + "\n" +
                "Password: " + mPassword + "\n" +
                "Account level: " + ad;
    }

    public int getClearance() {
        return mClearance;
    }

    public void setClearance(int clearance) {
        mClearance = clearance;
    }

    public int getUserid() {
        return mUserid;
    }

    public void setUserid(int userid) {
        mUserid = userid;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }
}
