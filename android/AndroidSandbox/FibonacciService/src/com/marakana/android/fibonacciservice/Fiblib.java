package com.marakana.android.fibonacciservice;

public class Fiblib {
	
	/*we will start with NDK with c++*/
	
	/*Java version of fibonacci */
	public static long fibJ(long n){		
		if(n == 0)return 0;
		if(n == 1)return 1;		
		return fibJ(n-1) + fibJ(n-2);		
	}
	
	/* this is how to define the native function - this function is 
	 * implemented in c/c++ thats why there is no body to it
	 * 
	 * */
	public static native long fibN(long N); 
	
	/* go to Android tool Add Native Support Option (Right click on FibNative Project)*/
	
	/* There is a bug in eclipse that every time that you move to new workspace, 
	 * you need to reassign the location of NDK. go to properties --> android NDK*/
	
	/* after Run Add Native support option, we will get to files under JNI folder,
	 * FibNative.cpp and Android.mk 
	 * */
	
	/* we need to load the library. if not , we will get 
	 * unsatisfied link error exception. the delvik VM does not know to resolve fibN*/
	
	
	/* static - only happen once ever when java code is loaded 
	 * update comment to check git hub*/
	static {
		/* the name of the module */
		System.loadLibrary("FibNative");
	}
		
}
