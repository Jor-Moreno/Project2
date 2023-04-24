package com.jmoreno.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.jmoreno.project2.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final String USER_ID_KEY = "com.jmoreno.project2.userIdKey";
    ActivityMainBinding binding;

    Button loginButton;
    Button signupButton;

    static final String ID = "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        loginButton = binding.loginButton;



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = LoginActivity.getIntent(getApplicationContext());
                startActivity(intent);
                finish();
            }
        });
    }

    public static Intent intentFactory(Context packageContext, int userId){
        Intent intent = new Intent(packageContext, MainActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }

}