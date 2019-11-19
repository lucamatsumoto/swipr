package com.example.chris_frontend.Buyer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.chris_frontend.R;


public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launchBuyerActivity(View view) {
        Intent intent = new Intent(this, BuyerActivity.class);
        startActivity(intent);
    }
}
