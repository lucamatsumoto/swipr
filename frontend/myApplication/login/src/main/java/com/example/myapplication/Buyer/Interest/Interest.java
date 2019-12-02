package com.example.myapplication.Buyer.Interest;

import android.net.Uri;
import android.util.Log;

import com.example.myapplication.Shared.DiningHalls;
import com.example.myapplication.Shared.Offer;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class Interest {
    public LocalDateTime meetTime;
    public long preferredDiningHall;
    public JSONObject Buyer;
    public String name;
    public String venmo;
    public Uri profilePicture;

    // public String status = "pending";

    public Interest(String jsonString){
        setFromQuery(jsonString);
    }

    public boolean setFromQuery(String query)
    {
        try {
            JSONObject temp = new JSONObject(query);
            long longValue = temp.getInt("meetTime");
            Log.d("dining bitfield", Long.toString(longValue));
            meetTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(longValue), ZoneId.systemDefault());

            Log.d("Date Time", LocalDateTime.ofInstant(Instant.ofEpochSecond(temp.getLong("meetTime")), ZoneId.systemDefault()).toString());

            preferredDiningHall = temp.getLong("preferredDiningHall");

            Buyer = temp.getJSONObject("buyer");

            name = Buyer.getString("firstName") + " " + Buyer.getString("lastName");
            venmo = Buyer.getString("venmo");
            if(venmo.equals("null") || venmo == null)
                venmo = "N/A";
            String profPicString = Buyer.getString("profilePicUrl");

            if(profPicString != null  && !profPicString.isEmpty())
                profilePicture = Uri.parse(profPicString);
            else {
                profilePicture = Uri.parse("android.resource://com.example.myapplication/drawable/swipr_square");
            }

        } catch (Exception e) {
            Log.d("Interest JSON ERR", e.getMessage());
            return false;
        }
        return true;
    }

    public String getPreferredDiningHall()
    {
        switch((int) preferredDiningHall)
        {
            case 1:
                return "Bruin Plate";
            case 2:
                return "Covel";
            case 4:
                return "De Neve";
            case 8:
                return "Feast";
        }

        return null;
    }
}
