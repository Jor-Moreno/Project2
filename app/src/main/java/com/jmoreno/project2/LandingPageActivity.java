package com.jmoreno.project2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jmoreno.project2.databinding.ActivityLandingPageBinding;
import com.jmoreno.project2.databinding.ActivityLoginBinding;
import com.jmoreno.project2.db.AppDataBase;
import com.jmoreno.project2.db.CongoDAO;

import java.util.List;

public class LandingPageActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.jmoreno.project2.userIdKey";
    private static final String PREFERENCE_KEY = "com.jmoreno.project2.PREFERENCES_KEY";

    CongoDAO mCongoDAO;
    User mUser;

    ActivityLandingPageBinding bindingLanding;

    Button mAddRemoveButton;
    Button mSignOutButton;
    Button mNewOrderButton;;

    TextView mWelcome;
    TextView mInfoText;

    private int mUserId = -1;

    private SharedPreferences mPreference = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        bindingLanding = ActivityLandingPageBinding.inflate(getLayoutInflater());
        View view = bindingLanding.getRoot();
        setContentView(view);

        mWelcome = bindingLanding.welcomeTextView;
        mAddRemoveButton = bindingLanding.addRemoveButton;
        mInfoText = bindingLanding.infoTextView;
        mSignOutButton = bindingLanding.logoutButton;
        mNewOrderButton = bindingLanding.newOrdersButton;


        getDatabase();
//        checkForUser();
        addUserToPref(mUserId);
        loginUser(mUserId);

        mUserId = getIntent().getIntExtra(USER_ID_KEY,-1);
        loginUser(mUserId);
        mWelcome.setText("Welcome " + mUser.getUserName() + "!");

        if(mUser.getClearance() == 0){
            mAddRemoveButton.setVisibility(View.INVISIBLE);
            mInfoText.setText("You can now add orders, view previous orders or sign out");
        } else {
            mAddRemoveButton.setVisibility(View.VISIBLE);
            mInfoText.setText("You can now add orders, view previous orders, add and/or remove items or sign out");
        }

        mSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logoutUser();
            }
        });

        defaultCongos();

        mNewOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.intentFactory(getApplicationContext(), mUserId));
                startActivity(intent);
                finish();
            }
        });

        mAddRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddRemoveActivity.intentFactory(getApplicationContext(), mUserId));
                startActivity(intent);
                finish();
            }
        });

    }

    private void addUserToPref(int userId) {
        if(mPreference == null){
            getPrefs();
        }
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt(USER_ID_KEY, userId);
    }

    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, LandingPageActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }

    private void loginUser(int userId){
        mUser = mCongoDAO.getUserByUserId(userId);
        if(mUser == null){
            Toast.makeText(this, " Error Getting User", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Hi " + mUser.getUserName(), Toast.LENGTH_SHORT).show();
        }
        invalidateMenu();
    }

    private void logoutUser(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage(R.string.logout);

        alertBuilder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        clearUserFromIntent();
                        clearUserFromPref();
                        mUserId = -1;
                        checkForUser();
                    }
                });

        alertBuilder.setNegativeButton(getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                    }
                });
        alertBuilder.create().show();


    }

    private void clearUserFromIntent() {
        getIntent().putExtra(USER_ID_KEY, -1);
    }

    private void clearUserFromPref() {
        addUserToPref(-1);
    }

    public boolean isAdmin(User user){
        if(user.getClearance() == 0){
            return false;
        }
        return true;
    }

    private void checkForUser(){
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);

        if(mUserId != -1){
            return;
        }

        if(mPreference == null){
            getPrefs();
        }
        mUserId = mPreference.getInt(USER_ID_KEY, -1);

        if(mUserId != -1){
            return;
        }

        List<User> users = mCongoDAO.getAllUsers();
        if(users.size() <= 0){
            User defaultUser = new User("testuser1", "testuser1", 0);
            User defaultAdmin = new User("admin1", "admin1", 1);
            mCongoDAO.insert(defaultUser,defaultAdmin);
        }

        Intent intent = MainActivity.intentFactory(this, -1);
        startActivity(intent);
    }


    private void getDatabase(){
        mCongoDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build().getCongoDAO();
    }

    private void getPrefs() {
        mPreference = this.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);
    }

    private void defaultCongos(){
        Congo c1 = new Congo("blanket", 15.99, 9);
        Congo c2 = new Congo("cup", 1.99, 3);
        Congo c3 = new Congo("socks", 5.99, 10);
        Congo c4 = new Congo("forks", 3.99, 5);
        Congo c5 = new Congo("shoes", 89.99, 6);

        if(!checkForCongo(c1)){
            mCongoDAO.insert(c1);
        } else if(!checkForCongo(c2)){
            mCongoDAO.insert(c2);
        } else if(!checkForCongo(c3)){
            mCongoDAO.insert(c3);
        } else if(!checkForCongo(c4)){
            mCongoDAO.insert(c4);
        } else if(!checkForCongo(c5)){
            mCongoDAO.insert(c5);
        }
    }

    private boolean checkForCongo(Congo congo){
        for(Congo c: mCongoDAO.getCongo()){
            if(c.getItemName().equals(congo.getItemName())){
                return true;
            }
        }
        return false;
    }

}