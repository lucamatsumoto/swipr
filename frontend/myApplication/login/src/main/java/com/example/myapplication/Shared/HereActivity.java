package com.example.myapplication.Shared;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.myapplication.Buyer.BuyerBacker;
import com.example.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class HereActivity extends AppCompatActivity {

    private Button hereButton;
    private NetworkManager networkManager = NetworkManager.getInstance();
    String fullName, venmo, profPicString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_here);
        JSONObject userJson = BuyerBacker.getInstance().confirmed_seller;
        Log.d("seller", userJson.toString());
        
        String DiningHall = getDiningHallString(getIntent().getLongExtra("DiningHall", 0));
        long longValue = getIntent().getLongExtra("Time", 0);

        Log.d("Epoch", Long.toString(longValue));

        LocalDateTime time =
                LocalDateTime.ofInstant(Instant.ofEpochSecond(longValue), ZoneId.systemDefault());


        try
        {
            fullName = userJson.getString("firstName") + " " + userJson.getString("lastName");
            venmo = userJson.getString("venmo");
        }
        catch(JSONException e)
        {
            Log.e("JSON Here Error", e.getMessage());
        }

        setContentView(R.layout.activity_here);
        subscribeToHereTopic();
        hereButton = findViewById(R.id.here_button);

        TextView nameView = findViewById(R.id.name_value);
        nameView.setText(fullName);

        TextView timeView = findViewById(R.id.time_value);
        timeView.setText(time.toString());

        TextView venmoView = findViewById(R.id.venmo_value);
        venmoView.setText(venmo);

        Uri profileUri;
        if(profPicString != null )
            profileUri = Uri.parse(profPicString);
        else {
            profileUri = Uri.parse("android.resource://com.example.myapplication/drawable/swipr_square");
            }
        ImageView imgView = (ImageView) findViewById(R.id.here_profile_pic);
                Glide.with(getApplicationContext()).load(profileUri)
                                .thumbnail(0.5f)
                                .crossFade()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imgView);

    }

    private String getDiningHallString(long bitField)
    {
        switch((int) bitField)
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
            json.put("profilePicUrl", "https://lh3.googleusercontent.com/a-/AAuE7mDxNb7wKqRjR-1YZTgCy17m_3AyonadpYsFNxYgCw");
            json.put("venmo", "@test");
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
