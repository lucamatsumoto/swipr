package com.example.myapplication.Shared;

import android.util.Log;

public class ConcreteNetworkResponder implements NetworkResponder {
    public void onMessageReceived(String json)
    {
        Log.d("Received", json);
    }

}
