package com.example.myapplication;

import android.util.Log;

import com.example.myapplication.Shared.NetworkResponder;

public class OneBufferResponder implements NetworkResponder {
    public volatile boolean flag = false;
    public volatile String response;
    private String mTopic;

    OneBufferResponder(String topic)
    {
        mTopic = topic;
    }

    public String getTopic()
    {return mTopic;}

    @Override
    public void onMessageReceived(String json) {
        synchronized (this)
        {
            Log.d("wherever" + mTopic, "we have received this shit");
            response = json;
            flag = true;
            notify();
        }
    }

    public String getBuffer()
    {
        synchronized (this)
        {
            if(flag == false)
                try{Log.d("wherever" + mTopic, "we are waiting now");wait();}catch(Exception e) { Log.d("OneBufferResponder", e.getMessage());}
            flag = false;
            return response;
        }
    }
}
