package com.marakana.android.fibonacciservice;

import com.marakana.android.fibonaccicommon.FibonacciRequest;
import com.marakana.android.fibonaccicommon.FibonacciResponse;
import com.marakana.android.fibonaccicommon.IFibonacciService;


import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

/* This is the actuall implementation which extends from the stub. the stub takes
 * care on the low level unmarshalling data. we only need to implement the buisness method you want 
 * other to use
 * */
public class IFibonacciServiceImpl extends IFibonacciService.Stub{

	private static final String TAG = "IFibonacciServiceImpl";
	
	public long fibJR(long n) throws RemoteException {
		
		Log.d(TAG, String.format("fibJR(%d)", n));		
		return 12;
	}

	public long fibJI(long n) throws RemoteException {
		Log.d(TAG, String.format("fibJI(%d)", n));
		return 13;
	}

	public long fibNR(long n) throws RemoteException {
		Log.d(TAG, String.format("fibNR(%d)", n));
		return Fiblib.fibN(n);
	}

	public long fibNI(long n) throws RemoteException {
		Log.d(TAG, String.format("fibNI(%d)", n));
		return 15;
	}

	/*reading from a costum object and once we done we return the response. The  */
	public FibonacciResponse fib(FibonacciRequest request){
		Log.d(TAG,String.format("fib(%d, %s)", request.getN(), request.getType()));
		long timeInMillis = SystemClock.uptimeMillis();
		long result = 0;
		
		/* request was sent by the client it contains type and number:
		 * type - fibonacci calculation Algo type
		 * number - fibonacci index */
		switch (request.getType()) {
		case ITERATIVE_JAVA:
			try {
				result = this.fibJI(request.getN());
			} catch (RemoteException e) {				
				e.printStackTrace();
			}
			break;
		case RECURSIVE_JAVA:
			try {
				result = this.fibJR(request.getN());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			break;
		case ITERATIVE_NATIVE:
			try {
				result = this.fibNI(request.getN());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			break;
		case RECURSIVE_NATIVE:
			try {
				result = this.fibNR(request.getN());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			break;
		default:
			return null;
		}
		timeInMillis = SystemClock.uptimeMillis() - timeInMillis;
		return new FibonacciResponse(result, timeInMillis);
	}

}
