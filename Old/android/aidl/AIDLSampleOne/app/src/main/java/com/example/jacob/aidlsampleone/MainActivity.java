package com.example.jacob.aidlsampleone;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button buttonStart;
    private Button buttonStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonStart = (Button) findViewById(R.id.btnStart);
        buttonStop = (Button) findViewById(R.id.btnStop);
        buttonStart.setOnClickListener(this);
        buttonStop.setOnClickListener(this);

    }

    private ITaskBinder mService;

    private ITaskCallback mCallBack = new ITaskCallback.Stub() {
        public void actionPerformed(int actionId) throws RemoteException {
            Log.e("Test", "actionPerformed!: " + actionId);

        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mService = ITaskBinder.Stub.asInterface(service);
            try {
                mService.registerCallback(mCallBack);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
        }
    };

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btnStart) {
            Bundle bundle = new Bundle();
            Intent intent = new Intent(this, MyService.class);
            intent.putExtras(bundle);
            bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
            startService(intent);

        } else if (v.getId() == R.id.btnStop) {
            Intent intent = new Intent(this, MyService.class);
            unbindService(mConnection);
        }

    }
}
