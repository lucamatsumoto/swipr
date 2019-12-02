package com.example.myapplication.Shared;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.example.myapplication.Buyer.Result.ResultActivity;
import com.example.myapplication.Seller.SellerActivity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

/**
 * Intended as a wrapper for the SocketService.
 * Simplifies starting the service, registering callbacks for responses, and sending requests.
 */
public class NetworkManager {
    private String TAG = "NetworkManager";
    private SocketService mService;
    private ServiceConnection mServiceConnection;
    private boolean mServiceBound = false;
    public boolean showBuyerUpdate = true;

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
        temp.put("/user/queue/here", new HashSet<>());
        topicToSubset = temp;
    }

    private static NetworkManager instance;
    private NetworkManager(){ }

    /**
     * NetworkManager is a Singleton.
     * @return  Returns the instance of the NetworkManager
     */
    public static NetworkManager getInstance()
    {
        if(instance == null)
            instance = new NetworkManager();
        return instance;
    }

    /**
     * Starts up the SocketService and attempts to login by creating/updating a user
     *  on the server.
     * @param context           The context of the calling AppCompatActivity.
     * @param loginResponder    The callback upon successful login.
     * @param loginPayload      The user info object used to login.
     */
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
                subscribe("/user/queue/buyerFind", buyerUpdateResponder);
                subscribe("/user/queue/buyerInterest", interestConfirmedResponder);
                subscribe("/user/queue/sellerInterest", interestIncomingResponder);
                //TODO: subscribe to I'm here notifications
                subscribe("/user/queue/here", imHereResponder);
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

    /**
     * Destroys the SocketService. Must be called to stop the Application's Server listener
     *  from running.
     * @param context   The calling AppCompatActivity.
     */
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

    /**
     * Adds a callback for a specific topic.
     * Does not auto unsubscribe. Call "unsubscribe" if your callback should not survive
     * destruction of the containing Activity.
     * @param topic     The topic to listen and react to.
     * @param command   The callback.
     */
    public void subscribe(String topic, NetworkResponder command)
    {
        topicToSubset.get(topic).add(command);
        Log.d("here", "added to hash set for " + topic);
    }

    /**
     * Unsubscribes a callback for a specific topic.
     * Make sure to keep the original "command" parameter used to subscribe with.
     * @param topic     The topic the original callback was subscribed to.
     * @param command   The original callback used in the call to "subscribe".
     */
    public void unsubscribe(String topic, NetworkResponder command)
    {
        topicToSubset.get(topic).remove(command);
        Log.d("here", "removed from hash set for " + topic);
    }

    /**
     * Sends a request to the server.
     * @param endPoint  The endpoint the request should go to.
     * @param payload   The request body, if it exists.
     */
    public void send(String endPoint, String payload)
    {
        mService.send(endPoint, payload);
    }

    /**
     * Sends a request to the server.
     * Overload for payload-less requests.
     * @param endPoint  The endpoint the request should go to.
     */
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


    private NetworkResponder imHereResponder = new NetworkResponder() {
        @Override
        public void onMessageReceived(String json) {
            mService.createNotification(SellerActivity.class,
                    "Your Daddy is here!",
                    "Your Swipe Seller is at the designated location.",
                    mService.IM_HERE_NOTIFID);
        }
    };
    private NetworkResponder buyerUpdateResponder = new NetworkResponder() {
        @Override
        public void onMessageReceived(String json) {
            if(showBuyerUpdate)
                mService.createNotification(ResultActivity.class,
                        "New Daddies in your area!",
                        "New offers have been posted matching your search criteria",
                        mService.BUYER_UPDATE_NOTIFID);
        }
    };
    private NetworkResponder interestConfirmedResponder = new NetworkResponder() {
        @Override
        public void onMessageReceived(String json) {
            mService.createNotification(SellerActivity.class,
                    "Interest Confirmed",
                    "A seller has agreed to sell you a swipe. Click for details.",
                    mService.INTEREST_CONFIRMED_NOTFID);
        }
    };
    private NetworkResponder interestIncomingResponder = new NetworkResponder() {
        @Override
        public void onMessageReceived(String json) {
            mService.createNotification(SellerActivity.class,
                    "Incoming Interest",
                    "A Hungry Single is interested in your offer. Click to Confirm.",
                    mService.INTEREST_INCOMING_NOTIFID);
        }
    };
}
