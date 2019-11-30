package com.example.myapplication.Shared;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class NetworkManager {
    private String TAG = "NetworkManager";
    private SocketService mService;
    private ServiceConnection mServiceConnection;
    private boolean mServiceBound = false;

    private static final Map<String, Set<NetworkResponder>> topicToSubset;
    static {
        Map<String, Set<NetworkResponder>> temp = new HashMap<>();
        temp.put("/user/queue/reply", new HashSet<>());
        temp.put("/user/queue/error", new HashSet<>());
        temp.put("/topic/average", new HashSet<>());
        temp.put("/user/queue/buyerFind", new HashSet<>());
        temp.put("/user/queue/buyerInterest", new HashSet<>());
        temp.put("/user/queue/sellerUpdate", new HashSet<>());
        temp.put("/user/queue/sellerInterest", new HashSet<>());
        temp.put("/user/queue/sellerCancel", new HashSet<>());
        topicToSubset = temp;
    }

    private static NetworkManager instance;
    private NetworkManager(){ }
    public static NetworkManager getInstance()
    {
        if(instance == null)
            instance = new NetworkManager();
        return instance;
    }

    public void connect(Context context, NetworkResponder loginResponder, String loginPayload)
    {
        startService(context.getApplicationContext());
        mServiceConnection = new ServiceConnection(){
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                SocketService.SocketBinder binder = (SocketService.SocketBinder) service;
                mService = binder.getService();
                mServiceBound = true;

                mService.initialize();

                for ( String topic : topicToSubset.keySet() )
                    mService.stompSubscribe(topic, new NetworkResponder() {
                        @Override
                        public void onMessageReceived(String json) {
                            for(NetworkResponder command : topicToSubset.get(topic))
                                command.onMessageReceived(json);
                        }
                    });
                subscribe("/user/queue/reply", loginResponder);
                send("/swipr/create", loginPayload);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mServiceBound = false;
            }
        };
        Intent bindIntent = new Intent(context.getApplicationContext(), SocketService.class);
        context.getApplicationContext().bindService(
                bindIntent,
                mServiceConnection,
                context.BIND_AUTO_CREATE);
    }

    public void disconnect(Context context)
    {
        if(mServiceBound) {
            context.getApplicationContext().unbindService(mServiceConnection);
            mServiceBound = false;
        }
        for ( String topic : topicToSubset.keySet() )
            topicToSubset.get(topic).clear();
        mService.terminate();
        stopService(context.getApplicationContext());
    }

    public void subscribe(String topic, NetworkResponder command)
    {
        topicToSubset.get(topic).add(command);
        Log.d("here", "added to hash set for " + topic);
    }

    public void unsubscribe(String topic, NetworkResponder command)
    {
        topicToSubset.get(topic).remove(command);
        Log.d("here", "removed from hash set for " + topic);
    }
    public void send(String endPoint, String payload)
    {
        mService.send(endPoint, payload);
    }
    public void send(String endPoint)
    {
        mService.send(endPoint);
    }

    private void startService(Context context) {
        Intent serviceIntent = new Intent(context.getApplicationContext(), SocketService.class);
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
        ContextCompat.startForegroundService(context.getApplicationContext(), serviceIntent);
    }

    private void stopService(Context context) {
        Intent serviceIntent = new Intent(context.getApplicationContext(), SocketService.class);
        context.getApplicationContext().stopService(serviceIntent);
    }
}
