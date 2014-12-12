package com.cynovo.sirius.util.pay;

import android.app.Activity;
import android.content.Intent;

public class SmartPosPayEx {

	public final static String ACTION = "ex_action"; //标示给SmartPos,来自外部的调用
	public final static String ACTION_PAY = "pay"; //标示给SmartPos,是支付调用请求
	private static Intent exIntent;
	
	public SmartPosPayEx() {

	}
	
	private static Intent getExIntent(){
	
		if(exIntent == null){
			exIntent = new Intent();
			exIntent.setAction("cn.koolcloud.pos.PayExScreen"); 
		}
		
		return exIntent;
	}

	public static int startPay(Activity activity, String transAmount){
	
		Intent intent = getExIntent();
		
		intent.putExtra(ACTION, ACTION_PAY);
		intent.putExtra("transAmount", transAmount);
		
		activity.startActivityForResult(intent, 0);
		return 0;
	}

	//指定方式的支付调用，开发中，暂且请勿使用
	/*
	public static int startPay(Activity activity, String pMethod, String transAmount, String openBrh, String paymentId){
	
		Intent intent = getExIntent();
		
		intent.putExtra(ACTION, ACTION_PAY);
		intent.putExtra("pMethod", pMethod);
		intent.putExtra("transAmount", transAmount);
		intent.putExtra("openBrh", openBrh);
		intent.putExtra("paymentId", paymentId);

		activity.startActivityForResult(intent, 0);
		return 0;
	}
	*/
}
