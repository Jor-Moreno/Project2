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

import com.jmoreno.project2.databinding.ActivityAddRemoveBinding;
import com.jmoreno.project2.databinding.ActivityLandingPageBinding;
import com.jmoreno.project2.db.AppDataBase;
import com.jmoreno.project2.db.CongoDAO;

public class AddRemoveActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.jmoreno.project2.userIdKey";
    private static final String PREFERENCE_KEY = "com.jmoreno.project2.PREFERENCES_KEY";

    private int mUserId = -1;
    private SharedPreferences mPreference = null;

    ActivityAddRemoveBinding binding;

    CongoDAO mCongoDAO;
    User mUser;

    Button backButton;
    Button addButton;
    Button removeButton;

    EditText nameInput;
    EditText amountInput;
    EditText priceInput;

    String name;
    int amount;
    double price;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remove);

        binding = ActivityAddRemoveBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getDatabase();
        addUserToPref(mUserId);

        mUserId = getIntent().getIntExtra(USER_ID_KEY,-1);
        loginUser(mUserId);

        backButton = binding.backButton;
        addButton = binding.addButton;
        removeButton = binding.removeButton;

        nameInput = binding.nameInput;
        amountInput = binding.amountInput;
        priceInput = binding.priceInput;

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LandingPageActivity.intentFactory(getApplicationContext(), mUserId));
                startActivity(intent);
                finish();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean create = true;
                name = nameInput.getText().toString();

                if(name.isEmpty()){
                    Toast.makeText(AddRemoveActivity.this, "No name provided", Toast.LENGTH_SHORT).show();
                    create = false;
                }

                String num = amountInput.getText().toString();
                amount = makeInt(num);

                if(amount < 0){
                    Toast.makeText(AddRemoveActivity.this, "Issue getting amount, check number", Toast.LENGTH_SHORT).show();
                    create = false;
                }

                num = priceInput.getText().toString();
                price = makeDouble(num);

                if(price < 0){
                    Toast.makeText(AddRemoveActivity.this, "Issue getting price, check number", Toast.LENGTH_SHORT).show();
                    create = false;
                }

                if(create) {
                    makeCongo();
                }
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean del = true;
                name = nameInput.getText().toString();

                String num = amountInput.getText().toString();
                amount = makeInt(num);

                if(amount < 0){
                    Toast.makeText(AddRemoveActivity.this, "Issue getting amount, check number", Toast.LENGTH_SHORT).show();
                    del = false;
                }

                if(del){
                    removeCongo();
                }
            }
        });

    }

    private void removeCongo() {
        Congo c = mCongoDAO.getCongoByName(name);
        if(c == null){
            Toast.makeText(this, "Not an item in database, cannot remove", Toast.LENGTH_SHORT).show();
            return;
        }
        if(amount >= c.getAmount()){
            mCongoDAO.delete(c);
            clearText();
            Toast.makeText(this, "Successfully removed all items", Toast.LENGTH_SHORT).show();
        } else {
            int newAmount = c.getAmount() - amount;
            Congo newCongo = new Congo(name, c.getPrice(), newAmount);

            mCongoDAO.delete(c);
            mCongoDAO.insert(newCongo);
            clearText();
            Toast.makeText(this, "Successfully removed " + newAmount, Toast.LENGTH_SHORT).show();
        }
    }

    private void makeCongo() {
        if(!checkForCongo(name)) {
            Congo c = new Congo(name, price, amount);
            mCongoDAO.insert(c);
            Toast.makeText(AddRemoveActivity.this, "Item has been added Successfully", Toast.LENGTH_SHORT).show();

            clearText();
        } else {
            Toast.makeText(AddRemoveActivity.this, "Already an Item in database", Toast.LENGTH_SHORT).show();
        }
    }

    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, AddRemoveActivity.class);
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

    private double makeDouble(String num){
        double parsedNum = 0.0;
        try{
            parsedNum = Double.parseDouble(num);
        } catch (NumberFormatException e){
            parsedNum = -1.0;
        }
        return parsedNum;
    }

    private int makeInt(String num){
        int parsedNum = 0;
        try{
            parsedNum = Integer.parseInt(num);
        } catch (NumberFormatException e){
            parsedNum = -1;
        }
        return parsedNum;
    }

    private boolean checkForCongo(String name){
        Congo c = mCongoDAO.getCongoByName(name);
        if(c == null){
            return false;
        }
        return true;
    }

    private void clearText(){
        nameInput.getText().clear();
        amountInput.getText().clear();
        priceInput.getText().clear();
    }

}