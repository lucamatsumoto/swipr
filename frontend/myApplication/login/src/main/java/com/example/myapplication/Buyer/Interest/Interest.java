package com.example.myapplication.Buyer.Interest;

import android.util.Log;

import com.example.myapplication.Shared.DiningHalls;
import com.example.myapplication.Shared.Offer;

import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Interest {
    private int buyerId;
    private LocalDateTime meetTime;
    private long preferredDiningHall;
    public Offer offer;
    // public String status = "pending";

    public Interest(String jsonString){
        setFromQuery(jsonString);
    }

    public boolean setFromQuery(String query)
    {
        try {
            JSONObject temp = new JSONObject(query);
            buyerId = temp.getInt("buyerId");
            meetTime = LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(temp.getLong("meetTime")),
                    ZoneId.systemDefault());

            preferredDiningHall = temp.getLong("preferredDiningHall");
            offer.setFromQuery(temp.getJSONObject("sellQuery").toString());
        } catch (Exception e) {
            Log.d("Interest JSON ERR", e.getMessage());
            return false;
        }
        return true;
    }
}
