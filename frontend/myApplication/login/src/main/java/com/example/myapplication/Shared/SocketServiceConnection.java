package com.example.myapplication.Shared;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.core.content.ContextCompat;

public class SocketServiceConnection implements ServiceConnection {

    public SocketService mService;
    private boolean mServiceBound = false;


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        synchronized (this) {
            SocketService.SocketBinder binder = (SocketService.SocketBinder) service;
            mService = binder.getService();
            mServiceBound = true;
            notify();
        }

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        mServiceBound = false;
    }

    public void unBind(Context context)
    {
        if(mServiceBound) {
            context.unbindService(this);
            mServiceBound = false;
        }
    }

    public SocketService getService(Context context)
    {
        if(!mServiceBound)
            synchronized (this) {
                if(!mServiceBound) {
                    Intent bindIntent = new Intent(context, SocketService.class);
                    context.bindService(
                            bindIntent,
                            this,
                            context.BIND_AUTO_CREATE);
                    try{wait();}catch (Exception e)
                    {
                        Log.d("SocketServiceConnection", e.getMessage());
                        return null;
                    }
                }
            }
        return mService;
    }

    public void startService(Context context) {
        Intent serviceIntent = new Intent(context, SocketService.class);
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
        ContextCompat.startForegroundService(context, serviceIntent);
    }

    public void stopService(Context context) {
        Intent serviceIntent = new Intent(context, SocketService.class);
        context.stopService(serviceIntent);
    }
}
