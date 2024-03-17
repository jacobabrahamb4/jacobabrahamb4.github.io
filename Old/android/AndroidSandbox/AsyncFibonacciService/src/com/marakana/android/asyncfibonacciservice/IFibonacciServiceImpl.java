package com.marakana.android.asyncfibonacciservice;

import com.marakana.android.asyncfibonaccicommon.FibonacciRequest;
import com.marakana.android.asyncfibonaccicommon.FibonacciResponse;
import com.marakana.android.asyncfibonaccicommon.IFibonacciService;
import com.marakana.android.asyncfibonaccicommon.IFibonacciServiceResponseListener;
import com.marakana.android.asyncfibonacciservice.Fiblib;

import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;


/* This is the actuall implementation which extends from the stub. the stub takes
 * care on the low level unmarshalling data. we only need to implement the buisness method you want 
 * other to use
 * !!! The service will not block waiting for the listerner to return because the listener
 * itsdelf is also onway
 * */

public class IFibonacciServiceImpl extends IFibonacciService.Stub {
	  private static final String TAG = "IFibonacciServiceImpl";
	  @Override
	  public void fib(FibonacciRequest request,
	      IFibonacciServiceResponseListener listener) throws RemoteException {
	    long n = request.getN();
	    Log.d(TAG, "fib(" + n + ")");
	    long timeInMillis = SystemClock.uptimeMillis();
	    long result;
	    switch (request.getType()) {
	    case ITERATIVE_JAVA:
	    	result = 10;
	      break;
	    case RECURSIVE_JAVA:
	    	result = 20;
	      break;
	    case ITERATIVE_NATIVE:
	      result = 30;
	      break;
	    case RECURSIVE_NATIVE:
	      /* only recursive native has been implemented */
	      result = Fiblib.fibN(n);
	      break;
	    default:
	      result = 0;
	    }
	    timeInMillis = SystemClock.uptimeMillis() - timeInMillis;
	    Log.d(TAG, String.format("Got fib(%d) = %d in %d ms", n, result,
	        timeInMillis));
	    listener.onResponse(new FibonacciResponse(result, timeInMillis));
	  }
}
