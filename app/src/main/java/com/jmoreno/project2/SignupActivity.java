package com.jmoreno.project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.jmoreno.project2.databinding.ActivityMainBinding;
import com.jmoreno.project2.databinding.ActivitySignupBinding;
import com.jmoreno.project2.db.AppDataBase;
import com.jmoreno.project2.db.CongoDAO;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;

    private EditText mUsername;
    private EditText mPassword;
    private EditText mAdminCode;

    private String mUsernameStr;
    private String mPasswordStr;
    private String mAdminCodeStr;

    private CongoDAO mCongoDAO;

    private User mUser;
    private int mUserId = -1;

    private Button mSignupButton;
    private Button mCancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mUsername = binding.usernameInput2;
        mPassword = binding.passwordInput2;
        mAdminCode = binding.adminInput2;
        mSignupButton = binding.signupButton2;
        mCancelButton = binding.cancelButton;

//        getValuesFromDisplay();
        getDatabase();

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.intentFactory(getApplicationContext()));
                startActivity(intent);
                finish();
            }
        });

        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getValuesFromDisplay()){
                    if(checkForUserInDatabase()){
                        createUser();
                        Toast.makeText(SignupActivity.this, "Created User", Toast.LENGTH_SHORT).show();
                        Intent intent = LoginActivity.intentFactory(getApplicationContext());
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

    }

    private void createUser() {
        if(!checkForUserInDatabase()){
            Toast.makeText(this, "Already a user, Try Username or Password", Toast.LENGTH_SHORT).show();
            return;
        }
        int clearance = 0;
        if(mAdminCodeStr.equals("123") || mAdminCodeStr.equals("admin") || mAdminCodeStr.equals("secret") || mAdminCodeStr.equals("android")){
            clearance = 1;
        }
        User newUser = new User(mUsernameStr, mPasswordStr, clearance);
        mCongoDAO.insert(newUser);
        mUser = newUser;
    }

    public static Intent intentFactory(Context packageContext){
        Intent intent = new Intent(packageContext, SignupActivity.class);
        return intent;
    }

    private boolean getValuesFromDisplay(){
        mUsernameStr = mUsername.getText().toString();
        mPasswordStr = mPassword.getText().toString();
        mAdminCodeStr = mAdminCode.getText().toString();

        if(mUsernameStr.isEmpty() || mPasswordStr.isEmpty()){
            Toast.makeText(this, "A field is empty, don't do that", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void getDatabase(){
        mCongoDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build().getCongoDAO();
    }

    private boolean checkForUserInDatabase(){
        mUser = mCongoDAO.getUserByUsername(mUsernameStr);
        if(mUser == null){
            mUser = mCongoDAO.getUserByUserPassword(mPasswordStr);
            if(mUser == null){
                Toast.makeText(this, "Creating Account " + mUsernameStr, Toast.LENGTH_SHORT).show();
                return true;
            } else {
                Toast.makeText(this, "Already a User, Try a difference Password", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Already a User, Try a difference Username", Toast.LENGTH_SHORT).show();

        }
        return false;
    }


}