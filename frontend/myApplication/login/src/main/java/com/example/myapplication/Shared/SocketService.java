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
import androidx.core.app.NotificationCompat;

import com.example.myapplication.R;
import com.example.myapplication.Seller.SellerActivity;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;


public class SocketService extends Service {
    public static final String CHANNEL_ID = "SocketServiceChannel";
    public static final String TAG = "SocketService";
    private static IBinder mBinder;

    private StompClient mStompClient;
    private CompositeDisposable compositeDisposable;

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

    public void terminate()
    {
        if(mStompClient !=  null)
        mStompClient.disconnect();
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
            compositeDisposable = null;
        }}

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel();
        Intent notificationIntent = new Intent(this, SellerActivity.class); //Apparently this is where we get taken
                                                                                           //Open clicking the notification
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText(input)
                .setSmallIcon(R.drawable.swipr_square) //TODO: make an all white and transparent version of the icon and put it here.
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (mBinder == null)
            mBinder = new SocketBinder();
        return mBinder;
    }

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
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    public class SocketBinder extends android.os.Binder
    {
        SocketService getService(){return SocketService.this;}
    }

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

    public void send(String endPoint, String payload)
    {
        compositeDisposable.add(mStompClient.send(endPoint, payload)
                .subscribe(() -> {
                    Log.d("SendSuccess", "STOMP echo send successfully");
                }, throwable -> {
                    Log.e("SendFail", "Error send STOMP echo", throwable);
                }));
    }

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

}
