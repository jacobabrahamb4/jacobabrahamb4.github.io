
package com.marakana.android.asyncfibonaccicommon;
import com.marakana.android.asyncfibonaccicommon.FibonacciRequest;
import com.marakana.android.asyncfibonaccicommon.FibonacciResponse;
import com.marakana.android.asyncfibonaccicommon.IFibonacciServiceResponseListener;

oneway interface IFibonacciService {
    void fib(in FibonacciRequest request, in IFibonacciServiceResponseListener listener);
}