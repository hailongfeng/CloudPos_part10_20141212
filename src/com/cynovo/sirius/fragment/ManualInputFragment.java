package com.cynovo.sirius.fragment;

 
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ViewFlipper;

import com.cynovo.sirius.R;
import com.cynovo.sirius.core.DealModel;
import com.cynovo.sirius.core.TotalMoneyObserver;
import com.cynovo.sirius.core.order.OrderContentData;
import com.cynovo.sirius.core.product.ProductManager;
import com.cynovo.sirius.util.BasicArithmetic;
import com.cynovo.sirius.widget.MoneyView;

public class ManualInputFragment extends AbstractFragment implements TotalMoneyObserver{

	View mManualInput = null;
	ViewFlipper mManualViewFlipper = null;
	
	MoneyView mInput = null;
	boolean isLastZero = true;
	
	boolean isInThisPage = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 注册观察者
		DealModel.getInstance().getShoppingCart().registerObs(this);
		mManualInput = inflater.inflate(R.layout.manualinput, container, false);
		
		mInput  = (MoneyView) mManualInput.findViewById(R.id.moneyInputView);
		
		ArrayList<Button> custom_digtal_buttons = new ArrayList<Button>();
		custom_digtal_buttons.add((Button) mManualInput.findViewById(R.id.button1));
		custom_digtal_buttons.add((Button) mManualInput.findViewById(R.id.button2));
		custom_digtal_buttons.add((Button) mManualInput.findViewById(R.id.button3));
		custom_digtal_buttons.add((Button) mManualInput.findViewById(R.id.button4));
		custom_digtal_buttons.add((Button) mManualInput.findViewById(R.id.button5));
		custom_digtal_buttons.add((Button) mManualInput.findViewById(R.id.button6));
		custom_digtal_buttons.add((Button) mManualInput.findViewById(R.id.button7));
		custom_digtal_buttons.add((Button) mManualInput.findViewById(R.id.button8));
		custom_digtal_buttons.add((Button) mManualInput.findViewById(R.id.button9));
		custom_digtal_buttons.add((Button) mManualInput.findViewById(R.id.button10));
		custom_digtal_buttons.add((Button) mManualInput.findViewById(R.id.button11));
		custom_digtal_buttons.add((Button) mManualInput.findViewById(R.id.button12));
		custom_digtal_buttons.add((Button) mManualInput.findViewById(R.id.button13));
		custom_digtal_buttons.add((Button) mManualInput.findViewById(R.id.button14));
		
		for (Button button : custom_digtal_buttons) {
			if(button.getId() == R.id.button4)
			{
				button.setOnLongClickListener(new OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						if(v.getId() == R.id.button4)
						{
							mInput.clear();
						}
						return false;
					}
				});
			}
			button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(!isInThisPage)
						return;
					if (v.getId() == R.id.button12) {
//						orderListAdapter.addCustomItem("0.00");
//						orderList.moveToLastLine();
					} else if (v.getId() == R.id.button8) {
						mInput.modifyMoney('-');
					}  else if (v.getId() == R.id.button4) {
						mInput.deleteMoney();
					}else if (v.getId() == R.id.button14) {
						mInput.modifyMoney('0');
					} else if (v.getId() == R.id.button1) {
						mInput.modifyMoney('1');
					} else if (v.getId() == R.id.button2) {
						mInput.modifyMoney('2');
					} else if (v.getId() == R.id.button3) {
						mInput.modifyMoney('3');
					} else if (v.getId() == R.id.button5) {
						mInput.modifyMoney('4');
					} else if (v.getId() == R.id.button6) {
						mInput.modifyMoney('5');
					} else if (v.getId() == R.id.button7) {
						mInput.modifyMoney('6');
					} else if (v.getId() == R.id.button9) {
						mInput.modifyMoney('7');
					} else if (v.getId() == R.id.button10) {
						mInput.modifyMoney('8');
					} else if (v.getId() == R.id.button11) {
						mInput.modifyMoney('9');
					} else if (v.getId() == R.id.button13) {
						mInput.modifyMoney('.');
					}
					if (v.getId() == R.id.button12) {
						List<OrderContentData> mList = new ArrayList<OrderContentData>();
						OrderContentData mOc = new OrderContentData();
						mInput.clear();
						mOc.setProduct(ProductManager.getCustomProduct("0.00"));
						mOc.setCount(1);
						
						mList.add(mOc);
						DealModel.getInstance().addNewOrderContent(mList);
					}
					else
					{
						OrderContentData mOc = new OrderContentData();
						mOc.setProduct(ProductManager.getCustomProduct(mInput.getMoney()));
						mOc.setCount(1);
						DealModel.getInstance().addOrUpdateOrderContent(mOc);
					}
				}
			});
		}
		return mManualInput;
	}
	
	public void InputisShowen()
	{
		isInThisPage = true;
		List<OrderContentData> mList = new ArrayList<OrderContentData>();
		OrderContentData mOc = new OrderContentData();
		mOc.setProduct(ProductManager.getCustomProduct("0.00"));
		mOc.setCount(1);
		mList.add(mOc);
		DealModel.getInstance().addNewOrderContent(mList);
	}
	
	public void InputisHidden() {
		isInThisPage = false;
		DealModel.getInstance().removeUnused();
		if(mInput != null)
			mInput.clear();
	}
	
	@Override
	public void totalMoneyChanged(String money) {
		if(BasicArithmetic.compare(money, "0") == 0 && mInput != null) {
			if(!isLastZero)
				mInput.clear();
			// add this to fix a bug when clear the manuinput
			isLastZero = true;
		} else {
			isLastZero = false;
		}
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		// 注销观察者
		DealModel.getInstance().getShoppingCart().unRegisterObs(this);
	}
}
