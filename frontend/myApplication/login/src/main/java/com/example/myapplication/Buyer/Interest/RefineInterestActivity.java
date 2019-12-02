package com.example.myapplication.Buyer.Interest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Buyer.BuyerBacker;
import com.example.myapplication.Buyer.Result.ResultActivity;
import com.example.myapplication.Buyer.Result.ResultBacker;
import com.example.myapplication.R;
import com.example.myapplication.Shared.ConcreteNetworkResponder;
import com.example.myapplication.Shared.DiningHalls;
import com.example.myapplication.Shared.NetworkManager;
import com.example.myapplication.Shared.Offer;
import com.example.myapplication.Shared.ProfileSingleton;
import com.example.myapplication.Shared.SimpleSpinAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Result;

public class RefineInterestActivity extends AppCompatActivity {

    String offer_json_string;
    Offer m_offer;
    private Spinner diningHallSpinner;
    private ResultBacker resultBacker = ResultBacker.getInstance();
    SeekBar s_time;
    LocalDateTime preferredTime;
    String preferredDiningHallString;
    NetworkManager networkManager;
    LocalDateTime today = LocalDate.now().atTime(0, 0);
    long today_epoch = today.atZone(ZoneId.systemDefault()).toEpochSecond();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refine_interest);
        getSupportActionBar().setTitle("Confirm Swipe Request"); // for set actionbar title
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // for add back arrow in action bar

        m_offer = new Offer();
        Bundle extras = getIntent().getExtras();
        if(extras !=null) {
            offer_json_string = extras.getString("Offer");
            Log.d("Received from interests", offer_json_string);
            m_offer.setFromQuery(offer_json_string);
        }

        diningHallSpinner = (Spinner) findViewById(R.id.b_filter_dining_spinner);
        diningHallSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                Object item = adapterView.getItemAtPosition(position);
                preferredDiningHallString = item.toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // TODO Auto-generated method stub

            }
        });
        ArrayAdapter<String> diningHallAdapter = new SimpleSpinAdapter(this, getValidDiningHalls(m_offer.diningHallList));
        diningHallSpinner.setAdapter(diningHallAdapter);


        //Time slider
        int step_value_price = 30; //Number of minutes to change the total by
        TextView textView_time = findViewById(R.id.time_value);
        s_time=findViewById(R.id.s_time);

        long start_epoch = m_offer.startTime.atZone(ZoneId.systemDefault()).toEpochSecond();
        long start_minutes = (start_epoch - today_epoch) / 60;
        Log.d("Start epoch", Long.toString(start_epoch) + " " +  Long.toString(today_epoch));
        Log.d("Start Minutes", Integer.toString((int) start_minutes));

        long end_epoch = m_offer.endTime.atZone(ZoneId.systemDefault()).toEpochSecond();
        long end_minutes = (end_epoch - today_epoch) / 60;
        s_time.setMax((int) (end_minutes - start_minutes));

        // perform seek bar change listener event used for getting the progress value
        s_time.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            {
                s_time.setProgress((int) ((start_minutes + end_minutes) / 2 - start_minutes));
                int real_progress = s_time.getProgress() + (int) start_minutes;
                String hours = String.format("%2d",real_progress / 60);
                String mins = String.format("%02d", real_progress % 60);
                textView_time.setText(hours + ":" + mins);
                preferredTime = convertTime(real_progress /60, real_progress % 60);
            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progress = ((int)Math.round(progress/step_value_price )) * step_value_price;
                int real_progress = progress + (int) start_minutes;
                seekBar.setProgress(progress);
                String hours = String.format("%2d", real_progress / 60);
                String mins = String.format("%02d", real_progress % 60);
                textView_time.setText(hours + ":" + mins);
                Log.d("TIME", Integer.toString(real_progress));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress() + (int) start_minutes;
                Log.d("PRICE", Integer.toString(progress));
                preferredTime = convertTime(progress /60, progress % 60);
            }
        });

        networkManager = NetworkManager.getInstance();
    }

    public     LocalDateTime convertTime(int hour, int minute)
    {
        return LocalDate.now().atTime(hour, minute);
    }


    public List<String> getValidDiningHalls(List<Boolean> DiningHallBools)
    {
        List<String> returnList = new ArrayList<String>(4);
        for(int i = 0; i < DiningHallBools.size(); i++)
        {
            if(DiningHallBools.get(i))
                returnList.add(DiningHalls.diningHallList.get(i));
        }
        Log.d("Valid Dining Halls", returnList.toString());
        return returnList;
    }


    public void sendInterest(View view)
    {
        JSONObject sellquery;
        try {
            sellquery = new JSONObject(offer_json_string);
        }
        catch (JSONException e)
        {
            sellquery = new JSONObject();
            Log.d("JSON ERROR", e.getMessage());
        }

        long preferredTimeEpoch = preferredTime.atZone(ZoneId.systemDefault()).toEpochSecond();
        BuyerBacker.getInstance().confirmed_epoch = preferredTimeEpoch;
        JSONObject interestJSON = new JSONObject();

        long preferredDiningHallLong = 0;
        switch(preferredDiningHallString.toLowerCase())
        {
            case "bruin plate":
                preferredDiningHallLong = 1;
                break;
            case "covel":
                preferredDiningHallLong = 2;
                break;
            case "de neve":
                preferredDiningHallLong = 4;
                break;
            case "feast":
                preferredDiningHallLong = 8;
                break;
        }
        BuyerBacker.getInstance().confirmed_hall = preferredDiningHallLong;

        try
        {
            interestJSON.put("buyerId", ProfileSingleton.getInstance().getID());
            interestJSON.put("meetTime", preferredTimeEpoch);
            interestJSON.put("preferredDiningHall", preferredDiningHallLong);
            interestJSON.put("sellQuery", sellquery);
        }
        catch (JSONException e)
        {
            Log.d("JSON ERROR", e.getMessage());
        }
        resultBacker.setCancelled(m_offer, true);
        Log.d("JSON from buyer", interestJSON.toString());
        networkManager.send("/swipr/showInterest", interestJSON.toString());
        Intent i = new Intent(this, ResultActivity.class);
        startActivity(i);
    }
}
