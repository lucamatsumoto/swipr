package com.example.myapplication.Seller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;

import com.example.myapplication.Buyer.Interest.InterestAdapter;
import com.example.myapplication.Buyer.InterestBacker;
import com.example.myapplication.R;

public class SellerInterestActivity extends AppCompatActivity {

    private RecyclerView interestRecycler;
    private InterestAdapter interestAdapter;
    private InterestBacker interestBacker;
    private Button interestButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_interest);
        interestBacker = InterestBacker.getInstance();
        interestRecycler = findViewById(R.id.interestRecycler);
        interestAdapter = new InterestAdapter(this, interestBacker.getInterests());
        interestRecycler.setAdapter(interestAdapter);
        interestRecycler.setLayoutManager(new LinearLayoutManager(this));
//        interestButton = findViewById(R.id.interest_button);
//        interestButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("Interested Button", "clicked");
//                showInterestDialog();
//            }
//        });
    }
}
