package com.marakana.android.fibonacciservice;



import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;


/* With this service we expose this to the clien. this is a tranditional service.*/
public class FibonacciService extends Service{

	private static final String TAG = "FibonacciService";
	private IFibonacciServiceImpl service; // 
	@Override
	public void onCreate() {
		super.onCreate();
		this.service = new IFibonacciServiceImpl(); // 
		Log.d(TAG, "onCreate()'ed"); // 
	}
	/*
	 * we return the service. this is the actually service the client want to use 
	 * here the activity manager asking us to give reference to the service to the client
	 * Only at that point the binder driver learn about our service and start mapping it
	 * You register the service in the manifest file. You register it under costum intent
	 * 
	 * */
	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "onBind()'ed"); // 
		return (IBinder) this.service; // 
	}
	@Override
	public boolean onUnbind(Intent intent) {
		Log.d(TAG, "onUnbind()'ed"); // 
		return super.onUnbind(intent);
	}
	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy()'ed");
		this.service = null;
		super.onDestroy();
	}
}
