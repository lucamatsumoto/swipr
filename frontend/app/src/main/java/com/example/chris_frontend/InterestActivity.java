package com.example.chris_frontend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.chris_frontend.Interest.InterestAdapter;

public class InterestActivity extends AppCompatActivity {

    private RecyclerView interestRecycler;
    private InterestAdapter interestAdapter;
    private InterestBacker interestBacker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);
        interestBacker = InterestBacker.getInstance();
        interestRecycler = findViewById(R.id.interestRecycler);
        interestAdapter = new InterestAdapter(this, interestBacker.getInterests());
        interestRecycler.setAdapter(interestAdapter);
        interestRecycler.setLayoutManager(new LinearLayoutManager(this));
    }
}
