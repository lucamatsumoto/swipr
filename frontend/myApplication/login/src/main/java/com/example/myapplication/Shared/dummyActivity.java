package com.example.myapplication.Shared;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.Buyer.BuyerActivity;

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

        Offer offer = new Offer();
        offer.diningHallList = new ArrayList<>();
        offer.diningHallList.add(true);
        offer.diningHallList.add(false);
        offer.diningHallList.add(true);
        offer.diningHallList.add(false);
        offer.userId = profile.getID();
        offer.startTime = LocalDateTime.now();
        offer.endTime = offer.startTime.plusHours(1);
        offer.price = 420;
        networkManager.subscribe("/user/queue/sellerUpdate", new tempResponder());
        networkManager.send("/swipr/updateOffer", offer.generateQuery());
        networkManager.subscribe("/user/queue/sellerCancel", new tempResponder());
    }

    class tempResponder implements NetworkResponder{
        @Override
        public void onMessageReceived(String json) {
            Log.d("here", json);
        }
    }
}
