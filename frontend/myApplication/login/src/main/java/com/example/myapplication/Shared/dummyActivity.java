package com.example.myapplication.Shared;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class DummyActivity extends AppCompatActivity {

    NetworkManager networkManager;
    ProfileSingleton profile;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        networkManager = NetworkManager.getInstance();
        profile = ProfileSingleton.getInstance();

        setContentView(R.layout.dummy);


        networkManager.subscribe("/user/queue/error", new TempResponder(R.id.textView5));
        networkManager.subscribe("/topic/average", new TempResponder(R.id.textView6));
        networkManager.subscribe("/user/queue/buyerFind", new TempResponder(R.id.textView10));
        networkManager.subscribe("/user/queue/buyerInterest", new TempResponder(R.id.textView8));
        networkManager.subscribe("/user/queue/sellerUpdate", new TempResponder(R.id.textView9));
        networkManager.subscribe("/user/queue/sellerInterest", new TempResponder(R.id.textView12));
        networkManager.subscribe("/user/queue/sellerCancel", new TempResponder(R.id.textView11));
    }

    public void updateOffer(View view) {
        Offer offer = new Offer();
        offer.diningHallList = new ArrayList<>();
        offer.diningHallList.add(true);
        offer.diningHallList.add(false);
        offer.diningHallList.add(false);
        offer.diningHallList.add(false);
        offer.userId = profile.getID();
        offer.startTime = LocalDateTime.now();
        offer.endTime = offer.startTime.plusHours(2);
        offer.price = 420;
        networkManager.send("/swipr/updateOffer", offer.generateQuery());
    }

    public void findOffers(View view) {
        Offer offer = new Offer();
        offer.diningHallList = new ArrayList<>();
        offer.diningHallList.add(true);
        offer.diningHallList.add(false);
        offer.diningHallList.add(false);
        offer.diningHallList.add(false);
        offer.userId = profile.getID();
        offer.startTime = LocalDateTime.now();
        offer.endTime = offer.startTime.plusHours(1);
        offer.price = 421;
        networkManager.send("/swipr/findOffers", offer.generateQuery());
    }

    public void cancelOffer(View view) {
        networkManager.send("/swipr/cancelOffer");
    }

    public void resetViews(View view) {
        ( (TextView) findViewById(R.id.textView5)).setText("here");
        ( (TextView) findViewById(R.id.textView6)).setText("here");
        ( (TextView) findViewById(R.id.textView11)).setText("here");
        ( (TextView) findViewById(R.id.textView8)).setText("here");
        ( (TextView) findViewById(R.id.textView9)).setText("here");
        ( (TextView) findViewById(R.id.textView10)).setText("here");
        ( (TextView) findViewById(R.id.textView12)).setText("here");
    }

    class TempResponder implements NetworkResponder{
        TextView textView;
        public TempResponder(int id)
        {
            textView = findViewById(id);
        }
        @Override
        public void onMessageReceived(String json) {
            textView.setText(json);
        }
    }
}
