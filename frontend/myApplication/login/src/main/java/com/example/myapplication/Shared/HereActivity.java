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

        String DiningHall = getDiningHallString(BuyerBacker.getInstance().confirmed_hall);
        long longValue = BuyerBacker.getInstance().confirmed_epoch;

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
        hereButton = findViewById(R.id.here_button);

        TextView nameView = findViewById(R.id.name_value);
        nameView.setText(fullName);

        TextView diningHallView = findViewById((R.id.dining_hall_value));
        diningHallView.setText(DiningHall);

        TextView timeView = findViewById(R.id.time_value);
        timeView.setText(convertToHourMinutes(time));

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

    private String convertToHourMinutes(LocalDateTime time) {
        int hour = time.getHour();
        String hourString = hour < 9 ? String.format("0%d", hour) : String.format("%d", hour);
        int minute = time.getMinute();
        String minuteString = minute < 9 ? String.format("0%d", minute) : String.format("%d", minute);
        return String.format("%s:%s", hourString, minuteString);
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


    public void launchHereButtonActivity(View v) {
        networkManager.send("/swipr/here", BuyerBacker.getInstance().confirmed_seller.toString());
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
