
package com.example.myapplication.Seller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;

import com.example.myapplication.Buyer.Result.ResultActivity;
import com.example.myapplication.Buyer.Result.ResultBacker;
import com.example.myapplication.Shared.ConcreteNetworkResponder;
import com.example.myapplication.Shared.DiningHalls;
import com.example.myapplication.Shared.DummyActivity;
import com.example.myapplication.Shared.NetworkManager;
import com.example.myapplication.Shared.NetworkResponder;
import com.example.myapplication.Shared.Offer;
import com.example.myapplication.Shared.ProfileSingleton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.innovattic.rangeseekbar.RangeSeekBar;

import com.example.myapplication.Buyer.BuyerActivity;
import com.example.myapplication.R;
import com.example.myapplication.Shared.DrawerBaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SellerActivity extends DrawerBaseActivity {

    SeekBar s_price;
    RangeSeekBar s_time;
    RangeSeekBar.SeekBarChangeListener L;

    private Button post;
    private ViewGroup buyerFrame;
    private ViewGroup sellerFrame;
    boolean seller_flag = false; // 1 if on Buyer section, 0 else

    long priceCents;
    LocalDateTime startTime, endTime;
    LocalDate today = LocalDate.now();
    List<Boolean> diningHalls;

    long average_price = 800;

    ResultBacker results;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ADD THESE LINES TO ADD DRAWER FOR PROFILE INFO
        //setContentView(R.layout.activity_buyer);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_seller, null, false);
        dl.addView(contentView, 0);
        //END

        //member variable initialization
        diningHalls = new ArrayList<Boolean>(4);
        for(int i = 0; i < 4; i++)
            diningHalls.add(Boolean.FALSE);
        priceCents = 800;
        startTime = convertTime(8,0);
        endTime = convertTime(12, 0);
        results = ResultBacker.getInstance();



        //Filters
        buyerFrame = findViewById(R.id.buyer_tags);
        sellerFrame = findViewById(R.id.seller_tags);
        post = findViewById(R.id.post_swipe);
        if (seller_flag) {
            post.setText(R.string.post_button);

        }
        else {
            post.setText(R.string.search_button);
        }

        // Sliders
        int step_value_price = 50; //Number of cents to change the total by
        TextView textView_price = findViewById(R.id.price_value);
        s_price=findViewById(R.id.s_price);
        s_price.setMax(1500);
        // perform seek bar change listener event used for getting the progress value
        s_price.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            {
                s_price.setProgress(800);
                textView_price.setText("$" + String.format("%.02f", s_price.getProgress() / (float) 100));
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = ((int)Math.round(progress/step_value_price )) * step_value_price;
                seekBar.setProgress(progress);
                textView_price.setText("$" + String.format("%.02f", s_price.getProgress() / (float) 100));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                Log.d("PRICE", Integer.toString(progress));

            }
        });

        //int step_value_time = 30; //Number of min per step
        s_time=findViewById(R.id.s_time);
        TextView textView_time = findViewById(R.id.time_value);
        s_time.setMax(47); //Minutes for 23:59 hours
        s_time.setMinThumbValue(16);
        s_time.setMaxThumbValue(24);
        s_time.setMinRange(1);
        // perform seek bar change listener event used for getting the progress value
        textView_time.setText("8:00 - 12:00");
        L = new RangeSeekBar.SeekBarChangeListener() {
            @Override
            public void onStartedSeeking() {
                Log.d("TIME", "Started seeking.");
            }

            @Override
            public void onStoppedSeeking() {
                Log.d("TIME", "Stopped seeking.");
            }

            @Override
            public void onValueChanged(int left_thumb, int right_thumb) {
                int left_thumb_value = left_thumb * 30;
                int right_thumb_value = right_thumb * 30;
                String left_hours = String.format("%2d", left_thumb_value / 60);
                String left_mins = String.format("%02d", left_thumb_value % 60);
                String right_hours = String.format("%2d", right_thumb_value / 60);
                String right_mins = String.format("%02d", right_thumb_value % 60);
                textView_time.setText(left_hours + ":" + left_mins + " - " + right_hours + ":" + right_mins);
                startTime = convertTime(left_thumb_value/60, left_thumb_value % 60);
                endTime = convertTime(right_thumb_value/60, right_thumb_value % 60);
                Log.d("TIME", startTime.toString() + " " + endTime.toString());
            }
        };
        s_time.setSeekBarChangeListener(L);


        //Dining Halls
        CompoundButton.OnCheckedChangeListener filterChipListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("CHIP", buttonView.getText() + "");
                String diningHall = buttonView.getText().toString();
                switch(diningHall.toLowerCase())
                {
                    case "b plate":
                        diningHalls.set(0, !diningHalls.get(0));
                        break;
                    case "covel":
                        diningHalls.set(1, !diningHalls.get(1));
                        break;
                    case "de neve":
                        diningHalls.set(2, !diningHalls.get(2));
                        break;
                    case "feast":
                        diningHalls.set(3, !diningHalls.get(3));
                        break;
                }
                Log.d("HALLS", diningHalls.toString());
            }
        };

        Chip bplate = findViewById(R.id.b_plate);
        bplate.setOnCheckedChangeListener(filterChipListener);
        Chip covel = findViewById(R.id.covel);
        covel.setOnCheckedChangeListener(filterChipListener);
        Chip de_neve = findViewById(R.id.de_neve);
        de_neve.setOnCheckedChangeListener(filterChipListener);
        Chip feast = findViewById(R.id.feast);
        feast.setOnCheckedChangeListener(filterChipListener);

        //Networking
        networkManager = NetworkManager.getInstance();
        networkManager.subscribe("/user/queue/sellerUpdate", new PostResponder());
        networkManager.subscribe("/user/queue/buyerFind", new FindOfferResponder());
        networkManager.subscribe("/user/queue/sellerInterest", new ConcreteNetworkResponder());
        //TODO: FIX UNSUBSCRIBE ERROR
        networkManager.subscribe("/topic/average", new AverageOfferResponder());
    }

    LocalDateTime convertTime(int hour, int minute)
    {
        return today.atTime(hour, minute);
    }


    @Override
    protected void onResume() {
        super.onResume();
        //networkManager.subscribe("/topic/average", new AverageOfferResponder());
        networkManager.send("/swipr/averageOffer");
        if (seller_flag) {
            sellerFrame.setVisibility(View.VISIBLE);
            buyerFrame.setVisibility(View.GONE);
            post.setText(R.string.post_button);
        }
        else {
            sellerFrame.setVisibility(View.GONE);
            buyerFrame.setVisibility(View.VISIBLE);
            post.setText(R.string.search_button);
        }
    }

    public void launchSellerActivity(View view) {
        sellerFrame.setVisibility(View.VISIBLE);
        buyerFrame.setVisibility(View.GONE);
        seller_flag = true;
        post.setText(R.string.post_button);
    }


    public void launchBuyerActivity(View view) {
        sellerFrame.setVisibility(View.GONE);
        buyerFrame.setVisibility(View.VISIBLE);
        seller_flag = false;
        post.setText(R.string.search_button);
    }
    public void launchButtonAcvitity(View view)
    {
        if(seller_flag)
            launchPostActivity();
        else
            //launchDummyActivity();
            launchSearchResultActivity();

    }

    public void launchSearchResultActivity()
    {
        Log.d("Post", "Launching Result Activity");
        results.clearOffers();
        networkManager.send("/swipr/findOffers", createOffer().generateQuery());
        Intent intent = new Intent(this, ResultActivity.class);
        startActivity(intent);
    }

    public void launchPostActivity()
    {
        Log.d("Post", "Launching Post Activity");
        networkManager.send("/swipr/updateOffer", createOffer().generateQuery());
    }


    public void launchDummyActivity() {
        Intent intent = new Intent(this, DummyActivity.class);
        startActivity(intent);
    }


    private Offer createOffer()
    {
        Offer returnOffer = new Offer();
        returnOffer.userId = ProfileSingleton.getInstance().getID();
        returnOffer.diningHallList = diningHalls;
        returnOffer.startTime = startTime;
        returnOffer.endTime = endTime;
        returnOffer.price = priceCents;
        return returnOffer;
    }


    public class PostResponder implements NetworkResponder {
        public void onMessageReceived(String json)
        {
            Log.d("Post Received: ", json);
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast toast = Toast.makeText(getApplicationContext(), "Swipe Offer Posted! We'll notify you when you've been matched.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, 300);
                    toast.show();
                }
            });
        }
    }

    public class FindOfferResponder implements NetworkResponder {
        public void onMessageReceived(String json)
        {
            Log.d("Find Offer Received: ", json);
            Log.d("Received", json);
            try {
                JSONArray jsonarray = new JSONArray(json);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonobject = jsonarray.getJSONObject(i);
                    Offer offer = new Offer(jsonobject.toString());
                    results.addOffer(offer);
                }
            }
            catch (JSONException e)
            {
                Log.e("JSON", e.getMessage());
            }
        }
    }


    private class AverageOfferResponder implements NetworkResponder {
        public void onMessageReceived(String value)
        {
            Log.d("Average Received: ", value);
            average_price = Long.valueOf(value);
            TextView average=findViewById(R.id.average_value);
            average.setText("$" + String.format("%.2f", Long.valueOf(average_price) / (float) 100));
        }
    }
}