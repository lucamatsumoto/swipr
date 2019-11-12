package com.example.chris_frontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class BuyerActivity extends AppCompatActivity {
    private RecyclerView interestRecycler;
    private InterestAdapter interestAdapter;
    private Interests interests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);
        interests = Interests.getInstance();
        interests.addInterests(new Interest());
        interests.addInterests(new Interest());
        interests.addInterests(new Interest());
        interestRecycler = findViewById(R.id.buyer_interest_view);
        interestAdapter = new InterestAdapter(this, interests.getInterests());
        interestRecycler.setAdapter(interestAdapter);
        interestRecycler.setLayoutManager(new LinearLayoutManager(this));
    }

    public void launchLoginActivity(View view) {
        //launch login tab
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void launchSellerActivity(View view) {
        //launch seller tab.
    }
}
