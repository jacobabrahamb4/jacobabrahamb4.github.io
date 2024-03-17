package com.marakana.android.asyncfibonaccicommon;

import com.marakana.android.asyncfibonaccicommon.FibonacciRequest;
import com.marakana.android.asyncfibonaccicommon.FibonacciResponse;


oneway interface IFibonacciServiceResponseListener {
    void onResponse(in FibonacciResponse response);
}