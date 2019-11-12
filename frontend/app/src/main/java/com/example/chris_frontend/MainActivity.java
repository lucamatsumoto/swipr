package com.example.chris_frontend;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private Results results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        results = Results.getInstance();
    }

    public void launchBuyerActivity(View view) {
        Intent intent = new Intent(this, BuyerActivity.class);
        startActivity(intent);
    }
}
