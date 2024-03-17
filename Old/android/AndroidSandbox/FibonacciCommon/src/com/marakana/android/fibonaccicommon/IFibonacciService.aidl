/// this is an interface definition

package com.marakana.android.fibonaccicommon;
import com.marakana.android.fibonaccicommon.FibonacciRequest;
import com.marakana.android.fibonaccicommon.FibonacciResponse;

// this interface defines 5 methods.
interface IFibonacciService {
    long fibJR(in long n);
    long fibJI(in long n);
    long fibNR(in long n);
    long fibNI(in long n);
    
    // taks an request object and return response object
    FibonacciResponse fib(in FibonacciRequest request);
 }