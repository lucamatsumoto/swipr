package com.example.myapplication.Shared;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.myapplication.Buyer.Result.ResultActivity;
import com.example.myapplication.R;
import com.example.myapplication.Seller.SellerActivity;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

/**
 * Service that is capable of sending basic notifications to the user and communicating with the
 * server via websocket.
 */
public class SocketService extends Service {
    /**
     * Channel ID for all notifications coming from this service.
     */
    public static final String CHANNEL_ID = "SocketServiceChannel";
    private static final String TAG = "SocketService";
    private static IBinder mBinder;
    private static final int REQUIRED_NOTIFID = 8978; //just a unique ID of our choosing. can be anything except 0.

    private StompClient mStompClient;
    private CompositeDisposable compositeDisposable;
    private NotificationManager notificationManager;

    //Something about how each notificatin needs a unique.
    //I'm pretty sure the number can be anything so I just picked something random.
    /**
     * Notification ID for "Im Here" notifications
     */
    public static final int IM_HERE_NOTIFID = 8979;
    /**
     * Notification ID for "Buyer Update" notifications
     */
    public static final int BUYER_UPDATE_NOTIFID = 8980;
    /**
     * Notification ID for "Interest Confirmed" notifications
     */
    public static final int INTEREST_CONFIRMED_NOTFID = 8981;
    /**
     * Notification ID for "Interest Incoming" notifications
     */
    public static final int INTEREST_INCOMING_NOTIFID = 8982;

    /**
     * Sets up the stomp client connection. Call after running starting and binding the Service
     */
    public void initialize()
    {
        if(compositeDisposable == null)
            compositeDisposable = new CompositeDisposable();
        try {
            mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://157.245.235.19:3000/index");
            mStompClient.connect();
        }
        catch(Exception e)
        {
            Log.e(TAG, "ERROR connecting to server, aborting");
            stopSelf();
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
    }

    /**
     * Breaks down the stomp client. Call before unbinding and Stopping the service
     */
    public void terminate()
    {
        if(mStompClient !=  null)
        mStompClient.disconnect();
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
            compositeDisposable = null;
        }}

    /**
     * Executes when the Service is started. Do not call this directly. See standard Android function "startForegroundService()"
     * or see NetworkManager.StartService()
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        createNotificationChannel();

        /*
            Foreground Services must be bound to a notification at all times.
            This notification stays open on the status bar.
         */
        Intent notificationIntent = new Intent(this, SellerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Swipr")
                .setContentText("The Swipr app is currently listening for notifications.\n Logout to turn off.")
                .setSmallIcon(R.drawable.swipr_square) //TODO: make an all white and transparent version of the icon and put it here.
                .setContentIntent(pendingIntent)
                .build();
        startForeground(REQUIRED_NOTIFID, notification);

        return START_NOT_STICKY;
    }

    /**
     * Executes when the Service is bound. Do not call this directly. Instead, see standard Android interface "ServiceConnection"
     * or see NetworkManager.mServiceConnection.
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (mBinder == null)
            mBinder = new SocketBinder();
        return mBinder;
    }

    /**
     * Executes when Service is unbound. Do not call this directly.
     */
    @Override
    public boolean onUnbind(Intent intent) {
        return false;
    }

    private void createNotificationChannel()
    {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
        {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(serviceChannel);
        }
    }

    /**
     * Returns a reference to the socket.
     */
    public class SocketBinder extends android.os.Binder
    {
        SocketService getService(){return SocketService.this;}
    }

    /**
     * Sets up a callback upon receiving a message from the server
     * @param topic     The topic to subscribe to on the server.
     * @param command   Implements the callback as "onMessageReceived".
     */
    public void stompSubscribe(String topic, NetworkResponder command)
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

    /**
     * Sends a request to the server.
     * @param endPoint  The endpoint to send the request to.
     * @param payload   The contents of the request
     */
    public void send(String endPoint, String payload)
    {
        compositeDisposable.add(mStompClient.send(endPoint, payload)
                .subscribe(() -> {
                    Log.d("SendSuccess", "STOMP echo send successfully");
                }, throwable -> {
                    Log.e("SendFail", "Error send STOMP echo", throwable);
                }));
    }

    /**
     * Sends a request to the server. Overload version
     *  for requests with no payload.
     * @param endPoint  The endpoint to send the request to.
     */
    public void send(String endPoint)
    {
        compositeDisposable.add(mStompClient.send(endPoint)
                .subscribe(() -> {
                    Log.d("SendSuccess", "STOMP echo send successfully (no second argument)");
                }, throwable -> {
                    Log.e("SendFail", "Error send STOMP echo (no second argument)", throwable);
                }));
    }

    private boolean testBool = false;
    public boolean getTest()
    {
        return testBool = !testBool;
    }

    /**
     * Generates a notification in the notification drawer.
     * Very simple implementation: dismiss, or click to open the Application in a specific Activity.
     * @param targetActivity    The Activity to open upon clicking the notification.
     * @param contentTitle      Heading text displayed on the notification.
     * @param contentText       Body text displayed on the notification.
     * @param notification_id   Indicates which notification to spawn.
     *                          One of either IM_HERE_NOTIFID,
     *                          BUYER_UPDATE_NOTIFID, INTEREST_CONFIRMED_NOTFID,
     *                          or INTEREST_INCOMING_NOTIFID
     */
    public void createNotification(Class targetActivity, String contentTitle, String contentText, int notification_id)
    {
        Intent notificationIntent = new Intent(this, targetActivity);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(R.drawable.swipr_square) //TODO: make an all white and transparent version of the icon and put it here.
                .setContentIntent(pendingIntent)
                .build();
        notificationManager.notify(notification_id, notification);
    }
}
