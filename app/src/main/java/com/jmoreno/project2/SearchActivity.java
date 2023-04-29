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
import android.widget.TextView;
import android.widget.Toast;

import com.jmoreno.project2.databinding.ActivitySearchBinding;
//import com.jmoreno.project2.databinding.ActivitySignupBinding;
import com.jmoreno.project2.db.AppDataBase;
import com.jmoreno.project2.db.CongoDAO;

public class SearchActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.jmoreno.project2.userIdKey";
    private static final String PREFERENCE_KEY = "com.jmoreno.project2.PREFERENCES_KEY";

    private int mUserId = -1;

    CongoDAO mCongoDAO;
    User mUser;

    ActivitySearchBinding binding;

    Button buyButton;
    Button searchButton;
    Button backButton;
    
    EditText itemName;
    
    TextView itemText;
    
    String itemNameStr;
    
    Congo currentItem;

    private SharedPreferences mPreference = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        binding = ActivitySearchBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        buyButton = binding.buyButton;
        searchButton = binding.SearchButton;
        backButton = binding.backButton;
        itemName = binding.searchInput;
        itemText = binding.itemViewText;

        getDatabase();
        addUserToPref(mUserId);

        mUserId = getIntent().getIntExtra(USER_ID_KEY,-1);
        loginUser(mUserId);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LandingPageActivity.intentFactory(getApplicationContext(), mUserId));
                startActivity(intent);
                finish();
            }
        });
        
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemNameStr = itemName.getText().toString();
                currentItem = mCongoDAO.getCongoByName(itemNameStr);
                
                if(currentItem != null){
                    itemText.setText(currentItem.toString());
                } else {
                    Toast.makeText(SearchActivity.this, "Not an available item", Toast.LENGTH_SHORT).show();
                }
                
            }
        });
    }

    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }

    private void addUserToPref(int userId) {
        if(mPreference == null){
            getPrefs();
        }
        SharedPreferences.Editor editor = mPreference.edit();
        editor.putInt(USER_ID_KEY, userId);
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

    private void getDatabase(){
        mCongoDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build().getCongoDAO();
    }

    private void getPrefs() {
        mPreference = this.getSharedPreferences(PREFERENCE_KEY, Context.MODE_PRIVATE);
    }

}