package com.example.myapplication.Shared;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.myapplication.R;

import java.time.LocalDateTime;
import java.util.ArrayList;


public class DummyActivity extends AppCompatActivity {

    NetworkManager networkManager;
    ProfileSingleton profile;
    public SocketService mService;

    Button btnStartService, btnStopService, btnBindService, btnTestService, btnTestNotif;

    SocketServiceConnection mServiceConnection;
    public boolean mServiceBound = false;

    @Override
    protected void onStop() {
        if(mServiceBound) {
            unbindService(mServiceConnection);
            mServiceBound = false;
        }
        super.onStop();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        networkManager = NetworkManager.getInstance();
        profile = ProfileSingleton.getInstance();

        setContentView(R.layout.dummy);

        btnStartService = findViewById(R.id.buttonStartService);
        btnStopService = findViewById(R.id.buttonStopService);
        btnBindService = findViewById(R.id.buttonBindService);
        btnTestService = findViewById(R.id.buttonTestService);
        btnTestNotif = findViewById(R.id.buttonTestNotif);
        btnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService();
            }
        });
        btnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService();
            }
        });
        btnBindService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bind();
            }
        });
        btnTestService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Offer offer = new Offer();
                offer.startTime = LocalDateTime.now().minusHours(2);
                offer.endTime = LocalDateTime.now().plusHours(2);
                offer.diningHallList = new ArrayList<>(4);
                offer.diningHallList.add(true);
                offer.diningHallList.add(true);
                offer.diningHallList.add(true);
                offer.diningHallList.add(true);
                offer.price = 100;
                offer.userId = 7;
                networkManager.send("/swipr/updateOffer", offer.generateQuery());
            }
        });
        btnTestNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Offer offer = new Offer();
                offer.startTime = LocalDateTime.now().minusHours(4);
                offer.endTime = LocalDateTime.now().plusHours(4);
                offer.diningHallList = new ArrayList<>(4);
                offer.diningHallList.add(true);
                offer.diningHallList.add(true);
                offer.diningHallList.add(true);
                offer.diningHallList.add(true);
                offer.price = 1500;
                offer.userId = 1304;
                networkManager.send("/swipr/findOffers", offer.generateQuery());
            }
        });
    }

    public void startService() {
        Intent serviceIntent = new Intent(this, SocketService.class);
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, SocketService.class);
        stopService(serviceIntent);
    }

    void bind() {
        mServiceConnection = new SocketServiceConnection();
        Intent bindIntent = new Intent(this, SocketService.class);
        bindService(
                bindIntent,
                mServiceConnection,
                BIND_AUTO_CREATE);
    }

    void test() {
        if(mService.getTest())
            btnTestService.setText("true");
        else
            btnTestService.setText("false");
    }

    private class SocketServiceConnection implements ServiceConnection
    {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            SocketService.SocketBinder binder = (SocketService.SocketBinder) service;
            mService = binder.getService();
            mServiceBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }
    }
}
