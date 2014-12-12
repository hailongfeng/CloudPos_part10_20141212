package com.cynovo.sirius.util.Printer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import android.graphics.Bitmap;
import android.util.Log;

import com.cynovo.sirius.R;
import com.cynovo.sirius.core.ShoppingCart;
import com.cynovo.sirius.core.order.OrderChangeInfo;
import com.cynovo.sirius.core.order.OrderContentData;
import com.cynovo.sirius.core.order.OrderData;
import com.cynovo.sirius.core.product.Product;
import com.cynovo.sirius.core.product.ProductCategory;
import com.cynovo.sirius.core.product.ProductManager;
import com.cynovo.sirius.core.product.ProductSubAttribute;
import com.cynovo.sirius.core.setting.SettingsManager;
import com.cynovo.sirius.core.store.StoreManager;
import com.cynovo.sirius.core.user.AccountManager;
import com.cynovo.sirius.util.BasicArithmetic;
import com.cynovo.sirius.util.Common;
import com.cynovo.sirius.util.MoneyInputer;
import com.cynovo.sirius.util.MyLog;
import com.cynovo.sirius.util.Printer.PrinterHelper.PrintPare;
import com.cynovo.sirius.util.pay.PayBase;
import com.cynovo.sirius.util.pay.PayManager;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class OrderPrint {
	
	OrderData mOrderData = null;
	public static final int NORMAL_MODE = 0;
	public static final int ADDTIONAL_MODE = 1;
	public static final int HANGUP_MODE = 2;
	public static final int REFUND_MODE = 3;
	public static final int ALREADYUND_MODE = 4;
	public static final int SALENEEDREVERSE = 5;
	public static final int SALEVOIDNEEDREVERSE = 6;
	
	void  printOrder(OrderData mOrderData)
	{
//		PrinterHelper mPrinterHelper = new PrinterHelper();
//		SPRTPrinter mPrinter =  new SPRTPrinter();
//		mPrinter.openPrinter();
//		if(mPrinter.isPrinterUsable() != 0)
//		{
//			mPrinter.closePrinter();
//			return;
//		}
//		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(mPrinterHelper.getNString("<>", 16),PrinterHelper.alignCenter,false));
//		mPrinter.PrintLineFeed();
//		mPrinter.closePrinter();
//		printOrder(mOrderData,NORMAL_MODE);	
//		printOrder1();
		/*备注：小票上二维码数据格式（json格式）：{"t":2,"d":{"a":"123","si":"001","s":"addidas","u":"1928394040292049"}} 
//		t为二维码类型，固定为2。
//		d为数据key。
//		a 为消费金额，
//		s 为商户名称，
		//si 为商户id（咱们程序里的productCategoryId）
//		u 为小票的唯一编号（）
		 * */
		JsonObject jsonObject=new JsonObject();
		jsonObject.addProperty("t", "2");
		JsonObject data=new JsonObject();
		data.addProperty("a", mOrderData.getAmount());
		int orderId=mOrderData.getCurrentOrder().getOrderID();
		
		Product p=ProductManager.getProductByProductId(orderId);
		ProductCategory  pc=ProductManager.getProductCategory(p.getProductCategoryID());
		data.addProperty("si",p.getProductCategoryID() );
		data.addProperty("s",pc.getProductCategoryName());
		data.addProperty("u",mOrderData.getCurrentOrder().getBillNo());
		jsonObject.add("d", data);
		MyLog.d("二维码内容："+jsonObject.toString());
		try {
			Bitmap bitmap = Create2DCode(jsonObject.toString());
//			mPrinter.PrintLineFeed();
//			mPrinter.printImage(Bitmap2Bytes(bitmap), 800, 800);
			PrintImage mPrintImage = new PrintImage();
			mPrintImage.printBitMap(bitmap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void  printOrder(OrderData mOrderData,int mode)
	{
//		try {
//			Bitmap bitmap = Create2DCode("123456");
////			mPrinter.PrintLineFeed();
////			mPrinter.printImage(Bitmap2Bytes(bitmap), 800, 800);
//			PrintImage mPrintImage = new PrintImage();
//			mPrintImage.printBitMap(bitmap);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		if(mode == HANGUP_MODE)
		{
			printHangupOrder( mOrderData,mode);
			return ;
		}

		
		PrinterHelper mPrinterHelper = new PrinterHelper();
		SPRTPrinter mPrinter =  new SPRTPrinter();
		mPrinter.openPrinter();
		if(mPrinter.isPrinterUsable() != 0)
		{
			mPrinter.closePrinter();
			return;
		}
		if(mOrderData.getPayType() == PayBase.PAYTYPE.PAY_BY_CASH.toInt() && BasicArithmetic.compare(mOrderData.getAmount(), "0.00") != 0 &&
				(mode == NORMAL_MODE || mode == REFUND_MODE))
		{
			mPrinter.openCashBox();
		}
		mPrinter.setMode(SPRTPrinter.DOUBLE_WIDTHANDHEIGHT);
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(StoreManager.getStoreName(),PrinterHelper.alignCenter,true));
		mPrinter.setMode(SPRTPrinter.NORMAL_SIZE);
		mPrinter.PrintLineFeed();
		if(mode == HANGUP_MODE)
		{
			mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat("  ",Common.getString(R.string.hangup),PrinterHelper.alignSide,false));
			mPrinter.PrintLineFeed();
		}
		else if(mode == ADDTIONAL_MODE)
		{
			mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat("  ",Common.getString(R.string.re_app),PrinterHelper.alignSide,false));
			mPrinter.PrintLineFeed();
		}
		else if(mode == REFUND_MODE)
		{
			mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat("  ",Common.getString(R.string.me_salevoid),PrinterHelper.alignSide,false));
			mPrinter.PrintLineFeed();
		}
		else if(mode == ALREADYUND_MODE)
		{
			mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat("  ",Common.getString(R.string.voidandrefund),PrinterHelper.alignSide,false));
			mPrinter.PrintLineFeed();
		}
		else if(mode == SALENEEDREVERSE)
		{
			mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat("  ",Common.getString(R.string.sale_need_reverse),PrinterHelper.alignSide,false));
			mPrinter.PrintLineFeed();		
		}
		else if(mode == SALEVOIDNEEDREVERSE)
		{
			mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat("  ",Common.getString(R.string.void_need_reverse),PrinterHelper.alignSide,false));
			mPrinter.PrintLineFeed();	
		}
		
		
		if(mOrderData.getOrderRemark() != null )
		{
			if(!mOrderData.getOrderRemark().getSitIndex().isEmpty())
			{
				mPrinter.PrintLineStr(Common.getString(R.string.sit_index)+": "+mOrderData.getOrderRemark().getSitIndex().replace("\r\n", "\r      ").replace("\r", "\r      ").replace("\n", "\r      "));
				mPrinter.PrintLineFeed();
			}
			if(!mOrderData.getOrderRemark().getRemark().isEmpty())
			{
				mPrinter.PrintLineStr(Common.getString(R.string.remark)+": "+mOrderData.getOrderRemark().getRemark().replace("\r\n", "\r      ").replace("\r", "\r      ").replace("\n", "\r      "));
				mPrinter.PrintLineFeed();
			}
		}
		
		printColumeName(mPrinterHelper ,mPrinter);
		
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(mPrinterHelper.getNString(" -", 14),PrinterHelper.alignCenter,false));
		mPrinter.PrintLineFeed();
		MoneyInputer mMoneyInputer = new MoneyInputer();
		String productDetails =
				Common.getString(R.string.p_name) + "   " +
				Common.getString(R.string.p_count) + "   " +
				Common.getString(R.string.p_price) + "   " +
				Common.getString(R.string.p_amount) + "\n";
		for(int i = 0 ; i < mOrderData.getOrderContentList().size();i++)
		{
			String name = mOrderData.getOrderContentList().get(i).getProduct().getName();
			
			if(mOrderData.getOrderContentList().get(i).getProductId() == ShoppingCart.MANUID && 
					mOrderData.getOrderContentList().get(i).getmOrderContentRemark() != null &&
							mOrderData.getOrderContentList().get(i).getmOrderContentRemark().getRemark() != null &&
					!mOrderData.getOrderContentList().get(i).getmOrderContentRemark().getRemark().isEmpty())
			{
				name = mOrderData.getOrderContentList().get(i).getmOrderContentRemark().getRemark();
			}
			String subattrList = getSubAttriStr(mOrderData.getOrderContentList().get(i));
			mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(name + "" + subattrList,PrinterHelper.alignLeft,false));
			productDetails += name + "" + subattrList + " ";
			mPrinter.PrintLineFeed();
			
			List<PrintPare> tmplist2 = new LinkedList<PrintPare>();
			tmplist2.add(mPrinterHelper.new PrintPare(" ",8));
			tmplist2.add(mPrinterHelper.new PrintPare(String.valueOf(mOrderData.getOrderContentList().get(i).getCount()),8));
			productDetails += String.valueOf(mOrderData.getOrderContentList().get(i).getCount()) + " ";
			
			mMoneyInputer.setMoney(mOrderData.getOrderContentList().get(i).getOnePrice());
			String price = mMoneyInputer.getMoney();
			tmplist2.add(mPrinterHelper.new PrintPare(price,8));
			productDetails += price + " ";
			mMoneyInputer.setMoney(mOrderData.getOrderContentList().get(i).getItemAmount());
			String amount = mMoneyInputer.getMoney();
			tmplist2.add(mPrinterHelper.new PrintPare(amount,8));
			productDetails += amount + "\n";
			mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(tmplist2,PrinterHelper.alignCenter,false));
			mPrinter.PrintLineFeed();
			
		}
		
		if(mode == HANGUP_MODE || mode == ALREADYUND_MODE)
		{
			mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(mPrinterHelper.getNString("-", 32),PrinterHelper.alignCenter,false));
			String serialNo = "";
			if(Common.checkValidString(mOrderData.getCurrentOrder().getBillNo()))
			{
				serialNo = Common.getSerialNumberFromBillNo(mOrderData.getCurrentOrder().getBillNo());
			}
			else
			{
				serialNo = SettingsManager.querySettings().getTraceNo();
			}
			
			mPrinter.PrintLineStr(Common.getString(R.string.r_number)+":  "+ serialNo);
			mPrinter.PrintLineFeed();
			mPrinter.PrintLineFeed();
			mPrinter.PrintLineFeed();
			mPrinter.PrintLineFeed();
			mPrinter.PrintLineFeed();
			mPrinter.closePrinter();
			return;
		}
		
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(mPrinterHelper.getNString(" -", 14),PrinterHelper.alignCenter,false));
		mPrinter.PrintLineFeed();
		
		mMoneyInputer.setMoney(mOrderData.getAmount());
		String total = mMoneyInputer.getMoney();
		mPrinter.setMode(SPRTPrinter.DOUBLE_HEIGHT);
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(Common.getString(R.string.totalprice)+":",total,PrinterHelper.alignSide,false));
		
		if( mode == NORMAL_MODE)
		{
			//现金支付并且金额不为0
			if(mOrderData.getPayType() == PayBase.PAYTYPE.PAY_BY_CASH.toInt() && BasicArithmetic.compare(mOrderData.getAmount(), "0.00") != 0)
			{
				mPrinter.PrintLineFeed();
				mMoneyInputer.setMoney(PayManager.getInstance().getMoneyGot());
				String cash = mMoneyInputer.getMoney();
				mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(
						Common.getString(R.string.p_cash)+":",cash,PrinterHelper.alignSide,false));
				mPrinter.setMode(SPRTPrinter.NORMAL_SIZE);
				mPrinter.PrintLineFeed();
				mMoneyInputer.setMoney(PayManager.getInstance().getChange());
				String change = mMoneyInputer.getMoney();
				mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(
						Common.getString(R.string.p_change)+":",change,PrinterHelper.alignSide,false));
			}
			else if(mOrderData.getPayType() == PayBase.PAYTYPE.PAY_BY_MSR.toInt())
			{
				mPrinter.PrintLineFeed();
				mMoneyInputer.setMoney(PayManager.getInstance().getMoneyGot());
				String cash = mMoneyInputer.getMoney();
				mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(
						Common.getString(R.string.p_card)+":",cash,PrinterHelper.alignSide,false));
				mPrinter.setMode(SPRTPrinter.NORMAL_SIZE);
			}
			else
			{
				mPrinter.setMode(SPRTPrinter.NORMAL_SIZE);
			}
		}
		mPrinter.setMode(SPRTPrinter.NORMAL_SIZE);
		mPrinter.PrintLineFeed();
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(mPrinterHelper.getNString(" -", 14),PrinterHelper.alignCenter,false));
		mPrinter.PrintLineFeed();
		mPrinter.setMode(SPRTPrinter.DOUBLE_HEIGHT);
		
		String serialNo = "";
		if(Common.checkValidString(mOrderData.getCurrentOrder().getBillNo()))
		{
			serialNo = Common.getSerialNumberFromBillNo(mOrderData.getCurrentOrder().getBillNo());
		}
		else
		{
			serialNo = SettingsManager.querySettings().getTraceNo();
		}
		mPrinter.PrintLineStr(Common.getString(R.string.r_number)+":  "+ serialNo);
		mPrinter.setMode(SPRTPrinter.NORMAL_SIZE);
		mPrinter.PrintLineFeed();
		if(AccountManager.getInstance().getCurrentAccount() != null)
			mPrinter.PrintLineStr(Common.getString(R.string.cashier)+":  "+AccountManager.getInstance().getCurrentAccount().getOperatorID());
		mPrinter.PrintLineFeed();
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(Common.getCurrentDateTimeString(),PrinterHelper.alignCenter,false));
		mPrinter.PrintLineFeed();
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(mPrinterHelper.getNString("<>", 16),PrinterHelper.alignCenter,false));
		mPrinter.PrintLineFeed();
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(Common.getString(R.string.saddress)+": "+StoreManager.getStoreAddress(),PrinterHelper.alignLeft,false));
		
		
		mPrinter.PrintLineFeed();
		mPrinter.closePrinter();

		try {
			Bitmap bitmap = Create2DCode(productDetails);
//			mPrinter.PrintLineFeed();
//			mPrinter.printImage(Bitmap2Bytes(bitmap), 800, 800);
			PrintImage mPrintImage = new PrintImage();
			mPrintImage.printBitMap(bitmap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		PrinterImpl mPrinterImpl = new PrinterImpl("SP-POS58IVU");
//		try {
//			mPrinterImpl.open();
//		} catch (AccessException e) {
//			e.printStackTrace();
//		}
//		try {
//			mPrinterImpl.printTwoDBarCode(productDetails, 100, 100);
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
//		try {
//			mPrinterImpl.close();
//		} catch (AccessException e) {
//			e.printStackTrace();
//		}
	}
	
	public byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte pics[] = baos.toByteArray();
		try {
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return pics;
	}
	
	// 生成QR图
	private Bitmap Create2DCode(String str) throws WriterException {
		// 生成二维矩阵,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		Hashtable<EncodeHintType,String> hints = new Hashtable<EncodeHintType,String>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, 200, 200, hints);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		// 二维矩阵转为一维像素数组,也就是一直横着排了
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = 0xff000000;
				}

			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		// 通过像素数组生成bitmap,具体参考api
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}
	
	private String getSubAttriStr(OrderContentData mdata)
	{
		StringBuilder retStr = new StringBuilder();
		List<ProductSubAttribute> tmpSubAttrlist = mdata.getProductSubAttrList();
		if(tmpSubAttrlist != null && !tmpSubAttrlist.isEmpty())
		{
			retStr.append("(");
			for(int i = 0 ; i < tmpSubAttrlist.size() ; i++)
			{
				retStr.append(tmpSubAttrlist.get(i).getName());
				if(i != tmpSubAttrlist.size() -1)
					retStr.append("/");
			}
			retStr.append(")");
		}
		return retStr.toString();
	}
	
	private void printColumeName(PrinterHelper mPrinterHelper ,SPRTPrinter mPrinter)
	{		
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(mPrinterHelper.getNString("-", 32),PrinterHelper.alignCenter,false));
		mPrinter.PrintLineFeed();
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(mPrinterHelper.getNString(" -", 14),PrinterHelper.alignCenter,false));
		mPrinter.PrintLineFeed();
		List<PrintPare> tmplist = new LinkedList<PrintPare>();
		tmplist.add(mPrinterHelper.new PrintPare(Common.getString(R.string.p_name),8));
		tmplist.add(mPrinterHelper.new PrintPare(Common.getString(R.string.p_count),8));
		tmplist.add(mPrinterHelper.new PrintPare(Common.getString(R.string.p_price),8));
		tmplist.add(mPrinterHelper.new PrintPare(Common.getString(R.string.p_amount),8));
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(tmplist,PrinterHelper.alignCenter,false));
		mPrinter.PrintLineFeed();
	}
	
	private void printColumeNameTwo(PrinterHelper mPrinterHelper ,SPRTPrinter mPrinter)
	{		
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(mPrinterHelper.getNString("-", 32),PrinterHelper.alignCenter,false));
		mPrinter.PrintLineFeed();
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(mPrinterHelper.getNString(" -", 14),PrinterHelper.alignCenter,false));
		mPrinter.PrintLineFeed();
		List<PrintPare> tmplist = new LinkedList<PrintPare>();
		tmplist.add(mPrinterHelper.new PrintPare(Common.getString(R.string.p_name),16));
		tmplist.add(mPrinterHelper.new PrintPare(Common.getString(R.string.p_count),16));
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(tmplist,PrinterHelper.alignCenter,false));
		mPrinter.PrintLineFeed();
	}
	
	private void printHangupOrder(OrderData mOrderData, int mode)
	{
		if(mOrderData.getOrderChanged() != null)
		{
			printOrderChange( mOrderData);
			return ;
		}
		
		PrinterHelper mPrinterHelper = new PrinterHelper();
		SPRTPrinter mPrinter =  new SPRTPrinter();
		mPrinter.openPrinter();
		if(mPrinter.isPrinterUsable() != 0)
		{
			mPrinter.closePrinter();
			return;
		}
		mPrinter.setMode(SPRTPrinter.DOUBLE_WIDTHANDHEIGHT);
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(StoreManager.getStoreName(),PrinterHelper.alignCenter,true));
		mPrinter.setMode(SPRTPrinter.NORMAL_SIZE);
		mPrinter.PrintLineFeed();
		if(mode == HANGUP_MODE)
		{
			mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat("  ",Common.getString(R.string.hangup),PrinterHelper.alignSide,false));
			mPrinter.PrintLineFeed();
		}

		if(mOrderData.getOrderRemark() != null )
		{
			if(!mOrderData.getOrderRemark().getSitIndex().isEmpty())
			{
				mPrinter.PrintLineStr(Common.getString(R.string.sit_index)+": "+mOrderData.getOrderRemark().getSitIndex().replace("\r\n", "\r      ").replace("\r", "\r      ").replace("\n", "\r      "));
				mPrinter.PrintLineFeed();
			}
			if(!mOrderData.getOrderRemark().getRemark().isEmpty())
			{
				mPrinter.PrintLineStr(Common.getString(R.string.remark)+": "+mOrderData.getOrderRemark().getRemark().replace("\r\n", "\r      ").replace("\r", "\r      ").replace("\n", "\r      "));
				mPrinter.PrintLineFeed();
			}
		}
		
		printColumeNameTwo(mPrinterHelper ,mPrinter);
		
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(mPrinterHelper.getNString(" -", 14),PrinterHelper.alignCenter,false));
		mPrinter.PrintLineFeed();
		for(int i = 0 ; i < mOrderData.getOrderContentList().size();i++)
		{
			String name = mOrderData.getOrderContentList().get(i).getProduct().getName();
			
			if(mOrderData.getOrderContentList().get(i).getProductId() == ShoppingCart.MANUID && 
					mOrderData.getOrderContentList().get(i).getmOrderContentRemark() != null &&
							mOrderData.getOrderContentList().get(i).getmOrderContentRemark().getRemark() != null &&
					!mOrderData.getOrderContentList().get(i).getmOrderContentRemark().getRemark().isEmpty())
			{
				name = mOrderData.getOrderContentList().get(i).getmOrderContentRemark().getRemark();
			}
			String subattrList = getSubAttriStr(mOrderData.getOrderContentList().get(i));
			mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(name + "" + subattrList,PrinterHelper.alignLeft,false));
			mPrinter.PrintLineFeed();
			
			List<PrintPare> tmplist2 = new LinkedList<PrintPare>();
			tmplist2.add(mPrinterHelper.new PrintPare(" ",16));
			tmplist2.add(mPrinterHelper.new PrintPare(String.valueOf(mOrderData.getOrderContentList().get(i).getCount()),16));
			mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(tmplist2,PrinterHelper.alignCenter,false));
			mPrinter.PrintLineFeed();
			
		}
		
		if(mode == HANGUP_MODE)
		{
			mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(mPrinterHelper.getNString("-", 32),PrinterHelper.alignCenter,false));
			String serialNo = "";
			if(Common.checkValidString(mOrderData.getCurrentOrder().getBillNo()))
			{
				serialNo = Common.getSerialNumberFromBillNo(mOrderData.getCurrentOrder().getBillNo());
			}
			else
			{
				serialNo = SettingsManager.querySettings().getTraceNo();
			}
			
			mPrinter.PrintLineStr(Common.getString(R.string.r_number)+":  "+ serialNo);
			mPrinter.PrintLineFeed();
			mPrinter.PrintLineFeed();
			mPrinter.PrintLineFeed();
			mPrinter.PrintLineFeed();
			mPrinter.PrintLineFeed();
			mPrinter.closePrinter();
			return;
		}
		
	}
	private void printOrderChange(OrderData mOrderData)
	{
		PrinterHelper mPrinterHelper = new PrinterHelper();
		SPRTPrinter mPrinter =  new SPRTPrinter();
		mPrinter.openPrinter();
		if(mPrinter.isPrinterUsable() != 0)
		{
			mPrinter.closePrinter();
			return;
		}
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat("  ",Common.getString(R.string.addinfo),PrinterHelper.alignSide,false));
		mPrinter.PrintLineFeed();
		
		if(mOrderData.getOrderRemark() != null )
		{
			if(!mOrderData.getOrderRemark().getSitIndex().isEmpty())
			{
				mPrinter.PrintLineStr(Common.getString(R.string.sit_index)+": "+mOrderData.getOrderRemark().getSitIndex().replace("\r\n", "\r      ").replace("\r", "\r      ").replace("\n", "\r      "));
				mPrinter.PrintLineFeed();
			}
			if(!mOrderData.getOrderRemark().getRemark().isEmpty())
			{
				mPrinter.PrintLineStr(Common.getString(R.string.remark)+": "+mOrderData.getOrderRemark().getRemark().replace("\r\n", "\r      ").replace("\r", "\r      ").replace("\n", "\r      "));
				mPrinter.PrintLineFeed();
			}
		}
		
		printColumeNameTwo(mPrinterHelper ,mPrinter);
		List<OrderChangeInfo> mChangeList = mOrderData.getOrderChanged();
		for(OrderChangeInfo mOrderChangeInfo : mChangeList)
		{
			Log.d("=========>",mOrderChangeInfo.toString());
			if(mOrderChangeInfo.isAllSame())
				continue;
			
			String name = mOrderData.getOrderContentData(mOrderChangeInfo).getProduct().getName();
			
			if(mOrderData.getOrderContentData(mOrderChangeInfo).getProductId() == ShoppingCart.MANUID && 
					mOrderData.getOrderContentData(mOrderChangeInfo).getmOrderContentRemark() != null &&
							mOrderData.getOrderContentData(mOrderChangeInfo).getmOrderContentRemark().getRemark() != null &&
					!mOrderData.getOrderContentData(mOrderChangeInfo).getmOrderContentRemark().getRemark().isEmpty())
			{
				name = mOrderData.getOrderContentData(mOrderChangeInfo).getmOrderContentRemark().getRemark();
			}
			String subattrList = getSubAttriStr(mOrderData.getOrderContentData(mOrderChangeInfo));
			mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(name + "" + subattrList,PrinterHelper.alignLeft,false));
			mPrinter.PrintLineFeed();
			
			List<PrintPare> tmplist = new LinkedList<PrintPare>();
			if(mOrderChangeInfo.isOnlyCountChange() || mOrderChangeInfo.getChangeFlag() == OrderChangeInfo.ADD)
			{
				if(mOrderChangeInfo.getCountChanged() > 0)
					tmplist.add(mPrinterHelper.new PrintPare("+",16));
				else if(mOrderChangeInfo.getCountChanged() < 0)
					tmplist.add(mPrinterHelper.new PrintPare("-",16));
				else
					tmplist.add(mPrinterHelper.new PrintPare("!",16));
			}
			else if(mOrderChangeInfo.getChangeFlag() == OrderChangeInfo.CHANGE)
			{
				if(mOrderChangeInfo.getCountChanged() > 0)
					tmplist.add(mPrinterHelper.new PrintPare("+!",16));
				else if(mOrderChangeInfo.getCountChanged() < 0)
					tmplist.add(mPrinterHelper.new PrintPare("-!",16));
				else
					tmplist.add(mPrinterHelper.new PrintPare("!",16));
			}
			else if(mOrderChangeInfo.getChangeFlag() == OrderChangeInfo.DELETE)
			{
				tmplist.add(mPrinterHelper.new PrintPare("-",16));
			}
			tmplist.add(mPrinterHelper.new PrintPare(String.valueOf(mOrderChangeInfo.getCountChanged()),16));
			mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(tmplist,PrinterHelper.alignCenter,false));
			mPrinter.PrintLineFeed();
		}
		
		mPrinter.PrintLineStr(mPrinterHelper.getPrintStringWithFormat(mPrinterHelper.getNString("-", 32),PrinterHelper.alignCenter,false));
		String serialNo = "";
		if(Common.checkValidString(mOrderData.getCurrentOrder().getBillNo()))
		{
			serialNo = Common.getSerialNumberFromBillNo(mOrderData.getCurrentOrder().getBillNo());
		}
		else
		{
			serialNo = SettingsManager.querySettings().getTraceNo();
		}
		
		mPrinter.PrintLineStr(Common.getString(R.string.r_number)+":  "+ serialNo);
		
		mPrinter.PrintLineFeed();
		mPrinter.PrintLineFeed();
		mPrinter.PrintLineFeed();
		mPrinter.PrintLineFeed();	
		mPrinter.PrintLineFeed();
		mPrinter.closePrinter();
	}
}
