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

import com.jmoreno.project2.databinding.ActivityLoginBinding;
import com.jmoreno.project2.db.AppDataBase;
import com.jmoreno.project2.db.CongoDAO;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private static final String USER_ID_KEY = "com.jmoreno.project2.userIdKey";
    private static final String PREFERENCE_KEY = "com.jmoreno.project2.PREFERENCES_KEY";

    ActivityLoginBinding bindingLogin;

    private EditText mUsernameField;
    private EditText mPasswordField;

    private Button mButton;
    private Button backButton;

    private CongoDAO mCongoDAO;

    private String mUsername;
    private String mPassword;

    private User mUser;
    private int mUserId = -1;

//    private SharedPreferences mPreferences = null;


    static final String ID = "MAIN";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindingLogin = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = bindingLogin.getRoot();
        setContentView(view);

        wireupDisplay();

        getDatabase();
        checkForUser();

    }

    public static Intent intentFactory(Context packageContext){
        Intent intent = new Intent(packageContext, LoginActivity.class);
        return intent;
    }

    private void wireupDisplay(){
        mUsernameField = findViewById(R.id.username_input);
        mPasswordField = findViewById(R.id.password_input);

        mButton = findViewById(R.id.login_button);
        backButton = findViewById(R.id.back_button);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getValuesFromDisplay();
                if(checkForUserInDatabase()){
                    if(!validatePassword()){
                        Toast.makeText(LoginActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = LandingPageActivity.intentFactory(getApplicationContext(), mUser.getUserid());
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = MainActivity.intentFactory(getApplicationContext());
                startActivity(intent);
                finish();
            }
        });


    }

    private boolean validatePassword(){
        return mUser.getPassword().equals(mPassword);
    }

    private void getValuesFromDisplay(){
        mUsername = mUsernameField.getText().toString();
        mPassword = mPasswordField.getText().toString();
    }

    private boolean checkForUserInDatabase(){
        mUser = mCongoDAO.getUserByUsername(mUsername);
        if(mUser == null){
            Toast.makeText(this, "no user" + mUsername + " found", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }



    private void getDatabase(){
        mCongoDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build().getCongoDAO();
    }

    private void checkForUser(){
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);

        if(mUserId != -1){
            return;
        }

        if(mUserId != -1){
            return;
        }

        List<User> users = mCongoDAO.getAllUsers();
        if(users.size() <= 0){
            User defaultUser = new User("testuser1", "testuser1", 0);
            User defaultAdmin = new User("admin1", "admin1", 1);
            mCongoDAO.insert(defaultUser);
            mCongoDAO.insert(defaultAdmin);
        }
    }


}