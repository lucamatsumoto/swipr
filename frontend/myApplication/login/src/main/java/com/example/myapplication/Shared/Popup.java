package com.example.myapplication.Shared;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.example.myapplication.Buyer.Interest.Interest;
import com.example.myapplication.Buyer.InterestBacker;
import com.example.myapplication.R;
import com.example.myapplication.Seller.SellerInterestActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Popup extends Activity {

    private String interests;
    private InterestBacker interestBacker = InterestBacker.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup);
        interests = getIntent().getStringExtra("Offer");
        setInterestsArray();
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

    private void setInterestsArray() {
        try {
            JSONArray jsonarray = new JSONArray(interests);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                Interest interest = new Interest(jsonobject.toString());
                interestBacker.addInterests(interest);
            }
        }
        catch (JSONException e)
        {
            Log.e("JSON", e.getMessage());
        }
    }
}
