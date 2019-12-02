package com.example.myapplication.Shared;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.example.myapplication.R;
import com.example.myapplication.Seller.SellerInterestActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class Popup extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout( (int) (width * .8), (int) (height * .4));
    }

    public void launchSellerInterest(View v)
    {
        Intent i = new Intent(getApplicationContext(), SellerInterestActivity.class);
        startActivity(i);
    }

}
