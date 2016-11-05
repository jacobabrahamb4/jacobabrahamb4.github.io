package com.marakana.android.fibonacciclient;


import com.marakana.android.fibonaccicommon.FibonacciRequest;
import com.marakana.android.fibonaccicommon.FibonacciResponse;
import com.marakana.android.fibonaccicommon.IFibonacciService;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FibonacciActivity extends Activity implements ServiceConnection{

	private static final String TAG = "FiboncacciActivity";
	
	/* How is the client wants to use a service? by its interface.
	 * This interface came from an aidl file.
	 * the service implenet the interface and the client use the service  */
	private IFibonacciService service;
	private Button button ; //trigger the fibonacci calculation
	private TextView output;
	private EditText input;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fibonacci);
		button = (Button)findViewById(R.id.Go);
		output = (TextView)findViewById(R.id.Output);
		input = (EditText)findViewById(R.id.Input);
	}
	

	@Override
	protected void onResume() {		
		Log.d(TAG, "onResum()");
		super.onResume();
		
		// Bind to our FibonacciService service, by looking it up by its name
	    // and passing ourselves as the ServiceConnection object
	    // We'll get the actual IFibonacciService via a callback to
	    // onServiceConnected() below
		// we ask the activity manager to bind a service. This is a Async call  
		// When it comes to system services directlly asks the system manager - hi
		// give me the service and right away we get a reference to that service.
		// The 'this' is ServiceConnection
	    if (!super.bindService(new Intent(IFibonacciService.class.getName()),
	        this, BIND_AUTO_CREATE)) {
	      Log.w(TAG, "Failed to bind to service");
	    }
	    
	}

	 @Override
	  protected void onPause() {
	    Log.d(TAG, "onPause()'ed");
	    super.onPause();
	    // No need to keep the service bound (and alive) any longer than
	    // necessary
	    super.unbindService(this);
	  }

	/* Handels button GO clicks to start Fibonacci Calculation*/
	public void onClick(View v){
		
	   final long n = Long.parseLong(input.getText().toString());
	 
	   /* Fibonacci Calculation Algorithem */
	   final FibonacciRequest.Type type = FibonacciRequest.Type.RECURSIVE_NATIVE;
	   	   
	   /* the client builds the request */
	   final FibonacciRequest request = new FibonacciRequest(n, type);
	   
	// showing the user that the calculation is in progress
	    final ProgressDialog dialog = ProgressDialog.show(this, "",
	        super.getText(R.string.progress_text), true);
	    // since the calculation can take a long time, we do it in a separate
	    // thread to avoid blocking the UI
	    new AsyncTask<Void, Void, String>() {
	      @Override
	      protected String doInBackground(Void... params) {
	        // this method runs in a background thread
	        try {
	          long totalTime = SystemClock.uptimeMillis();
	          
	          /* THIS IS ACTUALLY THE REMOTE CALL. THE CLIENT HAS NO KNOLADGE THAT THIS IS A REMOTE 
	           * CALL - THIS IS A BINDER CALL 
	           * WE BLOCK UNTILL THE SERVICE GO BACK !!! */
	          FibonacciResponse response = FibonacciActivity.this.service.fib(request);
	          totalTime = SystemClock.uptimeMillis() - totalTime;
			//We should avoid having an implicit (but strong) reference from our AsyncTask AsyncTask to our activity. Here
			//we took a shortcut for brevity reasons.
			//Our activity should already be registered in our AndroidManifest.xml AndroidManifest.xml file
			//FibonacciClient/AndroidManifest.xml FibonacciClient/AndroidManifest.xml
			//And the result should look like
	          totalTime = SystemClock.uptimeMillis() - totalTime;
	          // generate the result
	          return String.format(
	              "fibonacci(%d)=%d\nin %d ms\n(+ %d ms)", n,
	              response.getResult(), response.getTimeInMillis(),
	              totalTime - response.getTimeInMillis());
	        } catch (RemoteException e) {
	          Log.wtf(TAG, "Failed to communicate with the service", e);
	          return null;
	        }
	      }
	      @Override
	      protected void onPostExecute(String result) {
	        // get rid of the dialog
	        dialog.dismiss();
	        if (result == null) {
	          // handle error
	          Toast.makeText(FibonacciActivity.this, "Error",Toast.LENGTH_SHORT).show();
	        } else {
	          // show the result to the user
	          FibonacciActivity.this.output.setText(result);
	        }
	      }
	    }.execute(); // run our AsyncTask	  
   }


	/* we recieve the service as a ibinder object.  */
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
	    Log.d(TAG, "onServiceConnected()'ed to " + name);
	    // we do not know how to use ibinder object. we ask the stub to give us a proxy over that object 
	    // which matches the type you expecting over the service. 
	    // finally we can get to our IFibonacciService.
	    // The proxy will use the generic object(ibinder) and submit to it generic transactions
	    // with pacels.
	    this.service = IFibonacciService.Stub.asInterface(service);
	    
	    // enable the button, because the IFibonacciService is initialized
	    button.setEnabled(true);	   
		
	}
	
	@Override
	public void onServiceDisconnected(ComponentName name) {
		Log.d(TAG, "onServiceDisconnected()'ed to " + name);
	    // our IFibonacciService service is no longer connected
	    this.service = null;
	    // disabled the button, since we cannot use IFibonacciService	    
	    this.button.setEnabled(false);
		
	}

	
}
