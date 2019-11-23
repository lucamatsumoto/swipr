package com.example.myapplication.Shared;

import android.net.Uri;
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
    private static Uri m_profPicString;
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
            m_firstName = checkNull(reader.getString("firstName"));
            m_lastName = checkNull(reader.getString("lastName"));
            m_email = checkNull(reader.getString("email"));
            m_venmo = checkNull(reader.getString("venmo"));
            here = Boolean.valueOf(reader.getString("here"));
            matchedOffer = Boolean.valueOf(reader.getString("matchedOffer"));
            //m_profPicString =
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
    public void setVenmo(String s) {m_venmo = s;}
    public void setHere(boolean b) {here = b;}
    public void setMatched(boolean b) {matchedOffer = b;}


    private static String checkNull(String s)
    {
        if(s.toLowerCase() == "null")
            return null;
        else
            return s;
    }

    public static JSONObject asJSON()
    {
        JSONObject json = new JSONObject();
        try
        {
            json.put("id", m_id);
            json.put("firstName", m_firstName);
            json.put("lastName", m_lastName);
            json.put("email", m_email);
            json.put("profilePicUrl", m_profPicString);
            json.put("venmo", m_venmo);
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        return json;
    }
}
