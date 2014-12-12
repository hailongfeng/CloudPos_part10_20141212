package com.cynovo.sirius.fragment;

import com.cynovo.sirius.R;
import com.cynovo.sirius.core.DealModel;
import com.cynovo.sirius.util.JavaUtil;
import com.cynovo.sirius.util.pay.PayBase;
import com.cynovo.sirius.util.pay.PayManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class SentTradeInfoFragment extends BillingFragmentBase implements OnClickListener{

	
	Button newOrder = null;
	TextView sendInfoView = null;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sent_trade_info_fragment, container, false);
//      printReceiptBtn = (Button)v.findViewById(R.id.print_receipt_btn);
//      printReceiptBtn.setOnClickListener(this);
        sendInfoView = (TextView)v.findViewById(R.id.trade_sent_info);
        newOrder = (Button) v.findViewById(R.id.trade_state);
        newOrder.setOnClickListener(this);
       	int paytype = DealModel.getInstance().getOrderData().getPayType();
       	
		if(paytype == PayBase.PAYTYPE.PAY_BY_CASH.toInt())
		{
			String info = getString(R.string.receive_cash)+PayManager.getInstance().getMoneyGot() + getString(R.string.yuan)+","
					+getString(R.string.change_cash)+PayManager.getInstance().getChange()+getString(R.string.yuan)+getString(R.string.period);
	        sendInfoView.setText(info);
		}
		else if(paytype == PayBase.PAYTYPE.PAY_BY_MSR.toInt())
		{
			String info = getString(R.string.card_success)+PayManager.getInstance().getMoneyGot() + getString(R.string.yuan)+"。";
	        sendInfoView.setText(info);
		}
		else
		{

		}

        return v;
    }
    

	@Override
	public void onClick(View v) {

		switch(v.getId())
		{
		case R.id.trade_state:
			if(JavaUtil.isFastDoubleClick())
				return;
			mBilling.newDeal();
			break;
		}
	}
}
