package com.example.myserverapplication;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.example.aidl_shared.ISimpleService;

public class ServerService extends Service {
    private static final String TAG = "ServerService";

    // Implement the AIDL Interface
    private final ISimpleService.Stub binder = new ISimpleService.Stub() {
        @Override
        public String getMessage() throws RemoteException {
            Log.d(TAG, "getMessage() called");
            return "Hello from ServerService!";
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "ServerService Created");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "ServerService Bound");
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "ServerService Destroyed");
    }
}