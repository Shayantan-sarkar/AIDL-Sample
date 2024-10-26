package com.example.myclientapplication;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.example.aidl_shared.ISimpleService;

public class ClientService extends Service {
    private static final String TAG = "ClientService";
    private ISimpleService serverService;

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "Connected to ServerService");
            serverService = ISimpleService.Stub.asInterface(service);

            try {
                String message = serverService.getMessage();
                Log.d(TAG, "Received message from ServerService: " + message);
            } catch (RemoteException e) {
                Log.e(TAG, "Error calling getMessage", e);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "Disconnected from ServerService");
            serverService = null;
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "ClientService Created");
    }

    void startServerService()
    {
        Intent intent = new Intent("com.example.myserverapplication.ServerService");
        intent.setPackage("com.example.myserverapplication");
        try {
            this.startService(intent);
        } catch (SecurityException e) {
            // Handle the exception (e.g., show an error message)
            Log.e(TAG, "Error starting service: " + e.getMessage());
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "ClientService Started");

        // Bind to the ServerService from the server project
        Intent serverIntent = new Intent("com.example.myserverapplication.ServerService");
        serverIntent.setPackage("com.example.myserverapplication"); // Server app's package name
        Log.d(TAG, "Attempting to bind to ServerService");
        boolean bound = bindService(serverIntent, connection, BIND_AUTO_CREATE);
        if (bound) {
            Log.d(TAG, "Successfully bound to ServerService");
        } else {
            Log.e(TAG, "Failed to bind to ServerService");
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "ClientService Destroyed");
        unbindService(connection);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}