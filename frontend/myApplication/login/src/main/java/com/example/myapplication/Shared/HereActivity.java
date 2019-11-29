package com.example.myapplication.Shared;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

public class HereActivity extends AppCompatActivity {

    private Button hereButton;
    private NetworkManager networkManager = NetworkManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_here);
        subscribeToHerTopic();
        hereButton = findViewById(R.id.here_button);
    }

    private void subscribeToHerTopic() {
        networkManager.subscribe("/user/queue/here", null);
    }

    public void launchHereButtonActivity(View v) {
        Log.d("HERE", "Sent here message to seller");
        networkManager.send("/swipr/here", dummyUserJSON().toString());
    }


    public static JSONObject dummyUserJSON()
    {
        JSONObject json = new JSONObject();
        try
        {
            json.put("id", 1307);
            json.put("firstName", "Luca");
            json.put("lastName", "Matsumoto");
            json.put("email", "luca@matsumoto.us");
            json.put("profilePicUrl", null);
            json.put("venmo", null);
        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }
        return json;
    }
}
