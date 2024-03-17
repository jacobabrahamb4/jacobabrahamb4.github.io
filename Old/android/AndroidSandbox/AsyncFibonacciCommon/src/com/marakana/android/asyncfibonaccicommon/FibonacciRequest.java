package com.marakana.android.asyncfibonaccicommon;

import android.os.Parcel;
import android.os.Parcelable;

/* */
public class FibonacciRequest implements Parcelable {
	  public static enum Type {
	    RECURSIVE_JAVA, ITERATIVE_JAVA, RECURSIVE_NATIVE, ITERATIVE_NATIVE
	  }
	  private final long n;
	  private final Type type;
	  public FibonacciRequest(long n, Type type) {
	    this.n = n;
	    if (type == null) {
	      throw new NullPointerException("Type must not be null");
	    }
	    this.type = type;
	  }
	  public long getN() {
	    return n;
	  }
	  public Type getType() {
	    return type;
	  }
	  public int describeContents() {
	    return 0;
	  }
	  
	  /* Describe how you write to Parcel*/
	  public void writeToParcel(Parcel parcel, int flags) {
		  
		/* this is how to write long*/
	    parcel.writeLong(this.n);
	    /* and how to write enum to parcel */
	    parcel.writeInt(this.type.ordinal());
	  }
	  
	  /* How to recreate the original request from parcel */
	  public static final Parcelable.Creator<FibonacciRequest> CREATOR = new 
	Parcelable.Creator<FibonacciRequest>() {
	    public FibonacciRequest createFromParcel(Parcel in) {
	      long n = in.readLong();
	      Type type = Type.values()[in.readInt()];
	      return new FibonacciRequest(n, type);
	    }
	    public FibonacciRequest[] newArray(int size) {
	      return new FibonacciRequest[size];
	    }
	  };
	}
