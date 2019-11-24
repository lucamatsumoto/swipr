package com.example.myapplication.Seller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.myapplication.Buyer.BuyerActivity;
import com.example.myapplication.R;
import com.example.myapplication.Shared.DrawerBaseActivity;

public class SellerActivity extends DrawerBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("here", "1");
        super.onCreate(savedInstanceState);

        //ADD THESE LINES TO ADD DRAWER FOR PROFILE INFO
        //setContentView(R.layout.activity_buyer);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_seller, null, false);
        Log.d("here", "2");
        dl.addView(contentView, 0);
        //END
    }

    public void launchBuyerActivity(View view) {
        //launch buyer tab
        Intent intent = new Intent(this, BuyerActivity.class);
        startActivity(intent);
    }
}
