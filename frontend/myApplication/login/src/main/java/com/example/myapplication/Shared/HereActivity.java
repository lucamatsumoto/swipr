package com.example.myapplication.Shared;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

public class HereActivity extends AppCompatActivity {

    private Button hereButton;
    private NetworkManager networkManager = NetworkManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String userJsonString = getIntent().getStringExtra("UserJSON");
        setContentView(R.layout.activity_here);
        subscribeToHereTopic();
        hereButton = findViewById(R.id.here_button);
    }

    private void subscribeToHereTopic() {
        networkManager.subscribe("/user/queue/here", hereResponder);
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

    private NetworkResponder hereResponder = new NetworkResponder() {
        @Override
        public void onMessageReceived(String json) {
            Log.d("Received: ", json);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Change so that it dynamically uses the dining hall and user's first name
                    Toast toast = Toast.makeText(getApplicationContext(), "User has Arrived!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, 300);
                    toast.show();
                }
            });
        }
    };
}
