package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.Shared.Offer;
import com.example.myapplication.Shared.Popup;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONObject;

public class DisplayMessageActivity extends AppCompatActivity  {

    JSONObject test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        try{
            test = new JSONObject();
            test.put("meetTime", 1575216000);
            test.put("preferredDiningHall", 1);
            JSONObject BuyerJSON = new JSONObject();
            BuyerJSON.put("firstName", "Trevor");
            BuyerJSON.put("lastName", "Holt");
            BuyerJSON.put("email", "foo");
            BuyerJSON.put("venmo", "bar");
            BuyerJSON.put("profilePicUrl", "null");
                    //long meetTime
            //long preferredDiningHall
            //Buyer buyer
        }
        catch (Exception e)
        {
            Log.e("JSON", e.getMessage());
        }
    }

    public void launchPopup(View view)
    {
        Intent i = new Intent(getApplicationContext(), Popup.class);
        i.putExtra("Offer", "null");
        startActivity(i);
    }



}
