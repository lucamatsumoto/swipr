package com.example.myapplication.Shared;

import android.util.Log;

import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class Offer {
    private static final String TAG = "Offer";
    public Offer(){}
    public int userId;
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public double price;
    public List<String> diningHallList;
    public long offerId;
    public boolean loadFromBuyQuery(String buyQuery)
    {
        try {
            JSONObject temp = new JSONObject(buyQuery);
            userId = temp.getInt("userId");
            startTime = LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(temp.getLong("timeRangeStart")),
                    ZoneId.systemDefault());
            endTime = LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(temp.getLong("timeRangeEnd")),
                    ZoneId.systemDefault());
            price = temp.getLong("priceCents")/100.0;
            diningHallList = DiningHalls.getNameFromBitField(temp.getLong("diningHallBitfield"));
        } catch (Exception e)
        {
            Log.d(TAG, e.getMessage());
            return false;
        }
        return true;
    }
}
