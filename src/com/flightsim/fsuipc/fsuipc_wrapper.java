package com.flightsim.fsuipc;

public class fsuipc_wrapper
	{ 
	static 
		{ 
		// load library
		System.loadLibrary("lib/fsuipc_java" ); 
		} 
	public static synchronized native int Open(int mode);
	public static synchronized native void Close();
	
    public static synchronized native void ReadData(int aOffset,int aCount,byte[] aData);	
	public static synchronized native void WriteData(int aOffset,int aCount,byte[] aData);	
	public static synchronized native void Process();
    }
