package com.cynovo.sirius;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cynovo.sirius.aidl.IPaySDK;
import com.cynovo.sirius.constants.MyConstants;
import com.cynovo.sirius.core.order.OrderData;
import com.cynovo.sirius.core.order.OrderData.OrderStatus;
import com.cynovo.sirius.core.order.OrderManager;
import com.cynovo.sirius.core.order.OrderProcess;
import com.cynovo.sirius.sqlite.MySharedPreferencesEdit;
import com.cynovo.sirius.util.Common;
import com.cynovo.sirius.util.JavaUtil;
import com.cynovo.sirius.util.MyLog;
import com.cynovo.sirius.util.NumberFormater;
import com.cynovo.sirius.util.ViewHelper;
import com.cynovo.sirius.util.Printer.OrderPrint;
import com.cynovo.sirius.util.Printer.PrinterManager;
import com.cynovo.sirius.util.pay.PayBase.PAYTYPE;
import com.cynovo.sirius.util.pay.SmartPosPayEx;
import com.cynovo.sirius.widget.MoneyView;

public class RefundActivity extends SaleBaseActivity implements OnClickListener {
	OrderData mOrderData = null;
	Button mCancel = null;
	Button mConfirm = null;
	TextView mStoreInfo = null;
	TextView mAlertText = null;
	MoneyView mRefundMoney = null;
	MoneyView mTotalAmount = null;
	Button mSwipCardButton = null;
	View mSucceedView = null;
	View mNormalView = null;
	Button paySuccess = null;
	private String payResult;
	private IPaySDK iPay;

	private ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			iPay = null;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// IPaySDK.Stub.asInterface，获取接口
			iPay = IPaySDK.Stub.asInterface(service);
		}
	};
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		Intent intent = new Intent();
		intent.setAction("com.cynovo.sirius.Service.permission.CLOUDPAY");
		bindService(intent, conn, Context.BIND_AUTO_CREATE);
                
        int orderid = this.getIntent().getIntExtra("orderid", 0);
        mOrderData = OrderManager.getOrderDataById(orderid);
        this.setContentView(R.layout.refund_activity);
        mCancel = (Button)this.findViewById(R.id.order_cancel);
        mConfirm = (Button)this.findViewById(R.id.confirm_btn);
        mAlertText = (TextView) this.findViewById(R.id.refund_alert);
        mRefundMoney = (MoneyView) this.findViewById(R.id.refund_money);
        mSwipCardButton = (Button) this.findViewById(R.id.refund_btn);
        mSwipCardButton.setOnClickListener(this);
        mSucceedView = this.findViewById(R.id.refund_succeed);
        mNormalView = this.findViewById(R.id.refund_page);
        paySuccess = (Button) this.findViewById(R.id.trade_state);
        
        mCancel.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
        paySuccess.setOnClickListener(this);
        
        mStoreInfo = (TextView)this.findViewById(R.id.user_info);
        mTotalAmount = (MoneyView)this.findViewById(R.id.order_cancel_text);
        mTotalAmount.setMoney(mOrderData.getAmount());
        mRefundMoney.setMoney(mOrderData.getAmount());
        ViewHelper.setStoreInfo(this,mStoreInfo);    
        
        if(mOrderData.getPayType() == PAYTYPE.PAY_BY_CASH.toInt())
        {
        	mSwipCardButton.setVisibility(View.GONE);
        }
        else if(mOrderData.getPayType() == PAYTYPE.PAY_BY_MSR.toInt()
        		|| mOrderData.getPayType() == PAYTYPE.PAY_BY_EMV.toInt())
        {
        	mConfirm.setVisibility(View.GONE);
        	if(mOrderData.getCardPayInfos() != null)
        	{
//	            OrderProcess mOrderProcess = mOrderData.getCardPayInfos().getOrderProcess();
//	            if(mOrderProcess != null && mOrderProcess.getReferenceNo() != null)
//	            {
//	            	String date1 =mOrderProcess.getCreateTime().substring(0, 4);
//	            	String date2 = Common.getDateTimeString(System.currentTimeMillis(), "MMdd");
//	            	if(mOrderProcess.getOrderProcessModeId() == OrderProcessMode.ORDER_PROCESS_MSR_SALE.toInt())
//	            	{
//	            		if(date1.equals(date2))
//	                	{
//	            			mSwipCardButton.setMode(OperationType.OPERATION_SALEVOID);
//	                	}
//	            		else
//	            		{
//	            			mSwipCardButton.setMode(OperationType.OPERATION_REFUND);
//	            		}
//	            		mSwipCardButton.setCardType(MSRBaseButton.MSR_TYPE);
//	            	}
//	            	else if(mOrderProcess.getOrderProcessModeId() == OrderProcessMode.ORDER_PROCESS_EMV_SALE.toInt())
//	            	{
//	            		if(date1.equals(date2))
//	                	{
//	            			mSwipCardButton.setMode(OperationType.OPERATION_SALEVOID);
//	                	}
//	            		else
//	            		{
//	            			mSwipCardButton.setMode(OperationType.OPERATION_REFUND);
//	            		}
//	            		mSwipCardButton.setCardType(MSRBaseButton.EMV_TYPE);
//	            	}
//	            	mSwipCardButton.InitService(mOrderData.getCurrentOrder().getOrderID());
//	            	if(WorkKeyThread.getWorkkeyStatus() && NetworkUtil.isConnected())
//	            		mSwipCardButton.Start();
//	            }
        	}
        }
	}
	
	@Override
	public void onClick(View v) {
		if(JavaUtil.isFastDoubleClick())
			return;
		switch (v.getId()) {
		case R.id.trade_state:
			this.finish();
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		case R.id.order_cancel:
			this.finish();
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			break;
		case R.id.confirm_btn:
			if(mOrderData.getPayType() == PAYTYPE.PAY_BY_CASH.toInt()) {
				SaleVoidSuccess();
			}
			break;
		case R.id.refund_btn:
			try {
				if(MyConstants.PAY_TYPE == 0) {
					OrderProcess mOrderProcess = mOrderData.getCardPayInfos().getOrderProcess();
					String date1 = mOrderProcess.getCreateTime().substring(0, 4);
	            	String date2 = Common.getDateTimeString(System.currentTimeMillis(), "MMdd");
					
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("ReqTransAmount",
							NumberFormater.MoneyFromTwelveNumber(mOrderProcess.getPrice()));
					SimpleDateFormat sdf1 = new SimpleDateFormat("MMdd", Locale.getDefault());
					SimpleDateFormat sdf2 = new SimpleDateFormat("HHmmss", Locale.getDefault());
					jsonObject.put("ReqTransDate", sdf1.format(new Date()));
					jsonObject.put("ReqTransTime", sdf2.format(new Date()));
					jsonObject.put("ReqTransOperator", "1");
					jsonObject.put("ReqsTransDate", date1);
					jsonObject.put("ReqsTraceNo", mOrderProcess.getTraceNo());
					jsonObject.put("ReqTraceNo",
							"" + MySharedPreferencesEdit.getInstancePublic(RefundActivity.this).getTraceNO());
					MySharedPreferencesEdit.getInstancePublic(RefundActivity.this)
							.setTraceNO(MySharedPreferencesEdit.getInstancePublic(
									RefundActivity.this).getTraceNO() + 1); // test data
					if(date1.equals(date2)) {
						jsonObject.put("ReqTransType", 1); // void
	            	} else {
	            		jsonObject.put("ReqTransType", 2);//REFUND
	        		}
					
					jsonObject.put("cardType", 0); // test data
					jsonObject.put("skinType", 0); // test data
					
					jsonObject.put("AuthorizationNo", mOrderProcess.getAuthorizationNo());
					jsonObject.put("ReferenceNo", mOrderProcess.getReferenceNo());

					payResult = iPay.payCash(jsonObject.toString());
					if (payResult != null && payResult.length() > 0) {
						Toast.makeText(RefundActivity.this, payResult, Toast.LENGTH_LONG).show();
						Log.e("payResult", payResult);

						SaleVoidSuccess();

						PrinterManager.getInstance().PrintPosSlip(true, 0);
						PrinterManager.getInstance().PrintPosSlip(false, 8000);
					} else {
						Toast.makeText(RefundActivity.this, R.string.pay_error, Toast.LENGTH_SHORT).show();
					}
				} else {
					//支付，未指定“支付机构”和“支付方式” 
					SmartPosPayEx.startPay(RefundActivity.this, null);
					MyLog.e("SmartPosPayEx.startPay");
					//支付两分钱，另外一种调用方式，未指定“支付机构”和“支付方式”，
					//SmartPosPayEx.startPay(DemoPayActivity.this, null, "2", null, null);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
	}
	
	private void SaleVoidSuccess() {
		try {
			PrinterManager.getInstance().printOrder(mOrderData.clone(), OrderPrint.REFUND_MODE);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		mOrderData.setmOrderStatus(OrderStatus.ORDER_UNDO);
		OrderManager.saveOrder(mOrderData.getCurrentOrder());
		mSucceedView.setVisibility(View.VISIBLE);
		mNormalView.setVisibility(View.GONE);
		mCancel.setVisibility(View.INVISIBLE);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return false;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// TODO @Feel
		// 这个是撤销的接口，目前收银通还没有撤销的返回值，之后会加上
//		if (data == null) {
//			Toast.makeText(this, "撤销订单失败！", Toast.LENGTH_LONG).show();
//			return;
//		}
//
//		JSONObject json = null;
//		JSONArray detailList = null;
//		String bankCardID = null;
//		String alipayAccount = null;
//		boolean payTypeTag = false;
//
//		String orderDetail = "------------支付详情------------\n";
//
//		Bundle bundle = data.getExtras();
//		String action = bundle.getString(SmartPosPayEx.ACTION);// 目前无实际用途
//		String result = bundle.getString("result"); // 支付结果
//		String totalAmount = formatAmountStr(bundle.getString("totalAmount")); // 需支付金额
//		String paidAmount = formatAmountStr(bundle.getString("paidAmount")); // 已支付金额
//
//		try {
//			detailList = new JSONArray(bundle.getString("detailList"));
//			MyLog.e("detailList:\n"+detailList);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		for (int i = 0; i < detailList.length(); i++) {
//			try {
//				json = detailList.getJSONObject(i);
//				try {
//					bankCardID = json.getString("bankCardNum");
//				} catch (JSONException e) {
//					bankCardID = "";
//				}
//				try {
//					alipayAccount = json.getString("alipayAccount");
//				} catch (JSONException e) {
//					alipayAccount = "";
//				}
//				if (!(alipayAccount.equals(""))) {
//					payTypeTag = false;
//				} else if (!bankCardID.equals("") && alipayAccount.equals("")) {
//					payTypeTag = true;
//				}
//				if (payTypeTag) {
//					orderDetail = orderDetail + "第" + (i + 1) + "笔：" + "支付金额："
//							+ formatAmountStr(json.getString("transAmount"))
//							+ "元" + "，参考号：" + json.getString("refNo")
//							+ "，支付时间：" + json.getString("payTime") + "，银行卡号："
//							+ json.getString("bankCardNum") + "，发卡行："
//							+ json.getString("issuerName") + "，支付类型："
//							+ json.getString("payTypeDesc") + "，支付状态："
//							+ json.getString("orderStateDesc") + "\n";
//				} else {
//					orderDetail = orderDetail + "第" + (i + 1) + "笔：" + "支付金额："
//							+ formatAmountStr(json.getString("transAmount"))
//							+ "元" + "，参考号：" + json.getString("refNo")
//							+ "，支付时间：" + json.getString("payTime") + "，支付宝账户："
//							+ json.getString("alipayAccount") + "，PID："
//							+ json.getString("alipayPID") + "，交易号："
//							+ json.getString("alipayTransactionID") + "，NO："
//							+ json.getString("orderID") + "，支付类型："
//							+ json.getString("payTypeDesc") + "，支付状态："
//							+ json.getString("orderStateDesc") + "\n";
//				}
//				
//				OrderProcess mOrderProcess = new OrderProcess();
//				mOrderProcess.setAckNo(/*json.getString("RespCode")*/"");
//				mOrderProcess.setAcqbankId(/*json.getString("Acquirer")*/"");
//				mOrderProcess.setAddressID(-1);
//				mOrderProcess.setAuthorizationNo(/*json.getString("AuthorizationNo")*/"");
//				mOrderProcess.setBatchId(/*json.getString("BatchNo")*/"");
//				mOrderProcess.setClearingDate(/*json.getString("ClearDate")*/"");
//				mOrderProcess.setCreateTime(json.getString("payTime"));
//				mOrderProcess.setIssuerbankId(/*json.getString("Issuer")*/"");
//				mOrderProcess.setOrderID(/*DealModel.getInstance().getOrderData().
//						getCurrentOrder().getOrderID()*/-1);
////							mOrderProcess.setOrderProcessId(1);// 自增长id
//				mOrderProcess.setOrderProcessModeId(3);// TODO
//				mOrderProcess.setPrice(json.getString("transAmount"));
//				mOrderProcess.setReferenceNo(json.getString("refNo"));
//				mOrderProcess.setTraceNo(/*json.getString("TraceNo")*/"");
//				
//				OrderManager.saveOrderProcess(mOrderProcess);
//				// 交易成功，保存数据导入数据库
//				PayManager.getInstance().addNewPayment(new PayByMsrCard(
//						DealModel.getInstance().getShoppingCart().getTotalAmount()));
//			} catch (Exception e) {
//				e.printStackTrace();
//				Toast.makeText(this, "撤销订单失败！", Toast.LENGTH_LONG).show();
//			}
//		}
//
//		// 返回支付结果以及详细流水
//		Toast.makeText(this,
//				"action:" + action + "\n" + "------------支付结果------------\n"
//						+ "支付标志:" + result + "\n" + "应付金额:" + totalAmount + "元"
//						+ "\n" + "已付金额:" + paidAmount + "元" + "\n"
//						+ orderDetail, Toast.LENGTH_LONG).show();
//		MyLog.e("action:" + action + "\n" + "------------支付结果------------\n"
//						+ "支付标志:" + result + "\n" + "应付金额:" + totalAmount + "元"
//						+ "\n" + "已付金额:" + paidAmount + "元" + "\n"
//						+ orderDetail);
	}

	// 金额单位转换 分转为元
	@SuppressWarnings("unused")
	private String formatAmountStr(String amount) {
		String floatAmount = null;
		int length = amount.length();
		if (length == 0) {
			floatAmount = "";
		} else if (length == 1) {
			floatAmount = "0.0" + amount;
		} else if (length == 2) {
			floatAmount = "0." + amount;
		} else {
			floatAmount = amount.substring(0, length - 2) + "."
					+ amount.substring(length - 2, length);
		}
		return floatAmount;
	}
	
	@Override
	protected void onDestroy() {
		if (conn != null) {
			unbindService(conn);
		}
		iPay = null;
		super.onDestroy();
	}
}
