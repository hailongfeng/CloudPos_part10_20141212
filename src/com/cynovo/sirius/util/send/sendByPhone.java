package com.cynovo.sirius.util.send;

import com.cynovo.sirius.util.SendReceiptBase;

public class sendByPhone implements SendReceiptBase {

	@SuppressWarnings("unused")
	private String phoneNum;

	public sendByPhone(String num) {
		phoneNum = num;
	}

	@Override
	public void receiptSend() {
		// TODO Auto-generated method stub
		
	}

}
