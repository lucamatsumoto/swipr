package com.example.myapplication.Shared;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileSingleton {
    private static ProfileSingleton instance;
    private static int m_id;
    private static String m_firstName;
    private static String m_lastName;
    private static String m_email;
    private static String m_venmo;
    private static boolean here;
    private static boolean matchedOffer;

    private ProfileSingleton(){}
    public static ProfileSingleton getInstance(){
        if(instance == null)
            instance = new ProfileSingleton();
        return instance;
    }
    public static void setInstance(String json){
        try {
            JSONObject reader = new JSONObject(json);
            m_id = Integer.valueOf(reader.getString("id"));
            m_firstName = reader.getString("firstName");
            m_lastName = reader.getString("lastName");
            m_email = reader.getString("email");
            m_venmo = reader.getString("venmo");
            here = Boolean.valueOf(reader.getString("here"));
            matchedOffer = Boolean.valueOf(reader.getString("matchedOffer"));
            Log.d("Here", "profile created");
        }
        catch (JSONException e)
        {
            Log.e("JSON", e.getMessage());
        }
    }

    public int getID() {return m_id;}
    public String getFirstName() {return m_firstName;}
    public String getLastName() {return m_lastName;}
    public String getEmail() {return m_email;}
    public String getVenmo() {return m_venmo;}
    public boolean isHere() {return here;}
    public boolean hasMatched() {return matchedOffer;}
}
