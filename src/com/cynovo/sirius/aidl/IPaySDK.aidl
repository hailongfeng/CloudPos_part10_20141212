package com.cynovo.sirius.aidl;

interface IPaySDK {
	String payCash(String jsonData);
	String registerPOS(String jsonData);
	String getPOSInfo(String jsonData);
	String getHardWareState();
}