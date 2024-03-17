package com.example.jacob.aidlsampleone;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

public class MyService extends Service {

    final RemoteCallbackList<ITaskCallback> mCallbacks = new RemoteCallbackList<ITaskCallback>();

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("Test", "onCreate!");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.e("Test", "onStart");
        callBack(startId);
    }

    void callBack(int val) {
        final int N = mCallbacks.beginBroadcast();
        for (int i = 0; i < N; i++) {
            try {
                mCallbacks.getBroadcastItem(i).actionPerformed(val);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        mCallbacks.finishBroadcast();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return mBinder;
    }

    private final ITaskBinder.Stub mBinder = new ITaskBinder.Stub() {

        public void stopRunningTask() {

        }

        public boolean isTaskRunning() {
            return false;
        }

        public void registerCallback(ITaskCallback cb) {
            if (cb != null)
                mCallbacks.register(cb);
        }

        public void unregisterCallback(ITaskCallback cb) {
            if (cb != null)
                mCallbacks.unregister(cb);
        }

    };

}
