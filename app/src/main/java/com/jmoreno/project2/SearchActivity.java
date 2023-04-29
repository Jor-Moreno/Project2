package com.jmoreno.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class SearchActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.jmoreno.project2.userIdKey";
    private static final String PREFERENCE_KEY = "com.jmoreno.project2.PREFERENCES_KEY";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    }

    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }

}