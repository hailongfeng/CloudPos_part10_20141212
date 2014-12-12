package com.cynovo.sirius.util.send;

import com.cynovo.sirius.util.SendReceiptBase;

public class sendByEmail implements SendReceiptBase {
	
	@SuppressWarnings("unused")
	private String mailAddress;

	public sendByEmail(String email) {
		mailAddress = email;
	}

	@Override
	public void receiptSend() {
		// TODO Auto-generated method stub
		
	}

}
