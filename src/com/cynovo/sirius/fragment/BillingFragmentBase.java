package com.cynovo.sirius.fragment;

import android.os.Bundle;

import com.cynovo.sirius.BillingActivity;

public class BillingFragmentBase extends AbstractFragment {

	BillingActivity mBilling;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
    }
	
	public void setBillingActivity(BillingActivity mbm)
	{
		mBilling  = mbm;
	}
	
}
