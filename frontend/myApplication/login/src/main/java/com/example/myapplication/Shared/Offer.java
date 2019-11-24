package com.example.myapplication.Shared;

import android.util.Log;

import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

public class Offer {
    private static final String TAG = "Offer";
    public Offer(){offerId = -1;}
    public Offer(String query){setFromQuery(query);}
    public int userId;
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public long price;
    public List<Boolean> diningHallList;
    public long offerId;
    public boolean setFromQuery(String query)
    {
        try {
            JSONObject temp = new JSONObject(query);
            userId = temp.getInt("userId");
            startTime = LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(temp.getLong("timeRangeStart")),
                    ZoneId.systemDefault());
            endTime = LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(temp.getLong("timeRangeEnd")),
                    ZoneId.systemDefault());
            price = temp.getLong("priceCents");
            diningHallList = DiningHalls.getSelectedFromBitValue(temp.getLong("diningHallBitfield"));
            if(temp.has("offerId"))
                offerId = temp.getLong("offerId");
            else
                offerId = -1;
        } catch (Exception e)
        {
            Log.d(TAG, e.getMessage());
            return false;
        }
        return true;
    }
    public String generateQuery()
    {
        try{
            JSONObject temp = new JSONObject();
            temp.put("userId", userId);
            temp.put("timeRangeStart", startTime.atZone(ZoneId.systemDefault()).toEpochSecond());
            temp.put("timeRangeEnd", endTime.atZone(ZoneId.systemDefault()).toEpochSecond());
            temp.put("priceCents", price);
            temp.put("diningHallBitfield", DiningHalls.getBitValueFromSelected(diningHallList));
            if(offerId != -1)
                temp.put("offerId", offerId);
            return temp.toString();
        } catch (Exception e)
        {
            Log.d(TAG, e.getMessage());
            return null;
        }
    }
}
