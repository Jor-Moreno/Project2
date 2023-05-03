package com.jmoreno.project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jmoreno.project2.databinding.ActivityAddRemoveBinding;
import com.jmoreno.project2.databinding.ActivityAllIitemsBinding;
import com.jmoreno.project2.db.AppDataBase;
import com.jmoreno.project2.db.CongoDAO;

public class AllIItemsActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.jmoreno.project2.userIdKey";
    private static final String PREFERENCE_KEY = "com.jmoreno.project2.PREFERENCES_KEY";

    CongoDAO mCongoDAO;
    User mUser;

    private int mUserId = -1;

    ActivityAllIitemsBinding binding;

    private SharedPreferences mPreference = null;

    Button backButton;
    Button userButton;
    Button itemButton;

    TextView itemText;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_iitems);

        binding = ActivityAllIitemsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getDatabase();
        addUserToPref(mUserId);

        mUserId = getIntent().getIntExtra(USER_ID_KEY,-1);
        loginUser(mUserId);


        backButton = binding.backButton;
        itemButton = binding.congoButton;
        userButton = binding.usersButton;

        itemText = binding.itemText;
        itemText.setMovementMethod(new ScrollingMovementMethod());

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LandingPageActivity.intentFactory(getApplicationContext(), mUserId));
                startActivity(intent);
                finish();
            }
        });

        itemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populateTextItems();
            }
        });

        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populateTextUsers();
            }
        });


    }

    private void populateTextUsers() {
        StringBuilder sb = new StringBuilder();
        for(User u: mCongoDAO.getAllUsers()){
            sb.append(u.toString() + "\n\n");
            itemText.setText(sb.toString());
        }
    }

    private void populateTextItems() {
        StringBuilder sb = new StringBuilder();
        for(Congo c: mCongoDAO.getCongo()){
            sb.append(c.toString() + "\n\n");
            itemText.setText(sb.toString());
        }
    }

    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, AllIItemsActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }

    private void getDatabase(){
        mCongoDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build().getCongoDAO();
    }

    private void getPrefs() {
        mPreference = this.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);
    }

    private void loginUser(int userId){
        mUser = mCongoDAO.getUserByUserId(userId);
        if(mUser == null){
            Toast.makeText(this, " Error Getting User", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(this, "Hi " + mUser.getUserName(), Toast.LENGTH_SHORT).show();
        }
        invalidateMenu();
    }

    private void addUserToPref(int userId) {
        if(mPreference == null){
            getPrefs();
        }
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt(USER_ID_KEY, userId);
    }

}