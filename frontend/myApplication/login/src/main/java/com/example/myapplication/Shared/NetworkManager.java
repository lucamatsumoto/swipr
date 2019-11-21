package com.example.myapplication.Shared;

import android.util.Log;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class NetworkManager {
    private String TAG = "NetworkManager";
    private StompClient mStompClient;
    private CompositeDisposable compositeDisposable;

    private boolean isConnected;
    private static NetworkManager instance;
    private NetworkManager(){
        isConnected = false;
    }
    public static NetworkManager getInstance()
    {
        if(instance == null)
            instance = new NetworkManager();
        return instance;
    }
    public boolean connect()
    {
        if(compositeDisposable == null)
            compositeDisposable = new CompositeDisposable();
        try {
            mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://157.245.235.19:3000/index");
            mStompClient.connect();
        }
        catch(Exception e)
        {
            Log.e("STOMP_FAIL", "ERROR connecting to server");
            return false;
        }
        Disposable dispLifecycle = mStompClient.lifecycle()
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
                            Log.i("Success", "Stomp connection opened");
                            break;
                        case ERROR:
                            Log.e(TAG, "Stomp connection error", lifecycleEvent.getException());
                            break;
                        case CLOSED:
                            Log.i("Close", "Stomp connection closed");
                            break;
                    }
                });
        compositeDisposable.add(dispLifecycle);
        isConnected = true;
        return true;
    }
    public void disconnect()
    {
        if(mStompClient !=  null)
            mStompClient.disconnect();
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
            compositeDisposable = null;
        }
        isConnected = false;
    }
    public boolean isConnected(){return isConnected;}
    public void subscribe(String topic, NetworkResponder command)
    {
        Disposable dispTopic = mStompClient.topic(topic)
                .subscribeOn(Schedulers.io())
                .subscribe(topicMessage -> {
                    Log.d("SubSuccess", "Received " + topicMessage.getPayload());
                    command.onMessageReceived(topicMessage.getPayload());
                }, throwable -> {
                    Log.e("SubFail", "Error on subscribe topic", throwable);
                });

        compositeDisposable.add(dispTopic);
    }
    public void send(String endPoint, String payload)
    {
        compositeDisposable.add(mStompClient.send(endPoint, payload)
                .subscribe(() -> {
                    Log.d("SendSuccess", "STOMP echo send successfully");
                }, throwable -> {
                    Log.e("SendFail", "Error send STOMP echo", throwable);
                }));
    }
}
