package com.jmoreno.project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jmoreno.project2.databinding.ActivityPastOrdersBinding;
import com.jmoreno.project2.db.AppDataBase;
import com.jmoreno.project2.db.CongoDAO;

public class PastOrdersActivity extends AppCompatActivity {

    private static final String USER_ID_KEY = "com.jmoreno.project2.userIdKey";
    private static final String PREFERENCE_KEY = "com.jmoreno.project2.PREFERENCES_KEY";

    ActivityPastOrdersBinding binding;

    CongoDAO mCongoDAO;
    User mUser;

    private int mUserId = -1;

    private SharedPreferences mPreference = null;

    TextView orderTextView;
    TextView instructionsTextView;

    Button backButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_orders);

        binding = ActivityPastOrdersBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        orderTextView = binding.ordersText;
        instructionsTextView = binding.textView12;

        backButton = binding.backButton;

        getDatabase();
        addUserToPref(mUserId);

        mUserId = getIntent().getIntExtra(USER_ID_KEY,-1);
        loginUser(mUserId);

        instructionsTextView.setText("You are looking at " + mUser.getUserName() + "'s orders");

        showOrders();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LandingPageActivity.intentFactory(getApplicationContext(), mUserId));
                startActivity(intent);
                finish();
            }
        });

    }

    private void showOrders() {
        StringBuilder str = new StringBuilder();
        for(Cart cart: mCongoDAO.getUserCarts(mUserId)){
            Congo congo = mCongoDAO.getCongoById(cart.getCongoId());
            str.append("\nItem Name: " + congo.getItemName());
            str.append("\nAmount: " + cart.getQuantity());
            str.append("\nTotal: $" + cart.getQuantity()*congo.getPrice());
            str.append("\n___________\n");
            orderTextView.setText(str.toString());
        }
        orderTextView.setText(str.toString());
    }

    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, PastOrdersActivity.class);
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
//            Toast.makeText(this, "Hi " + mUser.getUserName(), Toast.LENGTH_SHORT).show();
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