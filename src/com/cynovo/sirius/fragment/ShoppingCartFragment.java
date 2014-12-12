package com.cynovo.sirius.fragment;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ViewFlipper;

import com.cynovo.sirius.R;
import com.cynovo.sirius.core.DealModel;
import com.cynovo.sirius.core.TotalMoneyObserver;
import com.cynovo.sirius.core.store.StoreManager;
import com.cynovo.sirius.util.BasicArithmetic;
import com.cynovo.sirius.util.DealOnTouch;
import com.cynovo.sirius.widget.MoneyView;

/**
 * 购物车fragment
 */
public class ShoppingCartFragment extends AbstractFragment implements TotalMoneyObserver {

	private View saleList = null;
	private ViewFlipper mSaleListViewFlipper = null;
	private MoneyView totalButton = null;
	private MoneyView hangupBtn = null;
	private Button mImageViewBtn1;
	private Button mImageVIewBtn2;

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 注册观察者
		DealModel.getInstance().getShoppingCart().registerObs(this);
		saleList = inflater.inflate(R.layout.sale_show_list, container, false);
		totalButton = (MoneyView) saleList
				.findViewById(R.id.salelisttotalmoney);
		hangupBtn = (MoneyView) saleList.findViewById(R.id.hangup_order_btn);

		mImageViewBtn1 = (Button) saleList
				.findViewById(R.id.salelisttop_show_picture1);
		mImageVIewBtn2 = (Button) saleList
				.findViewById(R.id.salelisttop_show_picture2);

		if (totalButton != null)
			totalButton.setPreString(this.getString(R.string.accounts) + " ");
		if (hangupBtn != null)
			hangupBtn.setPreString(this.getString(R.string.hangup) + " ");

		mSaleListViewFlipper = (ViewFlipper) saleList
				.findViewById(R.id.salelistviewflliper);
		touch_image();
		return saleList;
	}
	
	@Override
	public void onResume() {
		Drawable mdrawable = StoreManager.getStore()
				.getPhotoToShow();
		if (mdrawable != null) {
			mImageViewBtn1.setBackground(mdrawable);
			mImageVIewBtn2.setBackground(mdrawable);
		}
		super.onResume();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		totalButton.setClickable(false);
		hangupBtn.setClickable(false);
	}

	@Override
	public void totalMoneyChanged(String money) {
		if (totalButton != null)
			totalButton.setMoney(money);
		if (hangupBtn != null)
			hangupBtn.setMoney(money);
		if (money == null || money.isEmpty()
				|| BasicArithmetic.compare(money, "0.00") < 0) {
			totalButton.setClickable(false);
		} else {
			if (DealModel.getInstance().getShoppingCart().getCount() > 0)
				totalButton.setClickable(true);
			else
				totalButton.setClickable(false);

			if (BasicArithmetic.compare(money, "0.00") == 0) {
				hangupBtn.setClickable(false);
			} else {
				hangupBtn.setClickable(true);
			}
		}
	}

	private void touch_image() {
		DealOnTouch dt = new DealOnTouch(mSaleListViewFlipper, 0);
		DealOnTouch dt1 = new DealOnTouch(mSaleListViewFlipper, 1);
		mImageViewBtn1.setOnTouchListener(dt);
		mImageVIewBtn2.setOnTouchListener(dt1);
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		// 注销观察者
		DealModel.getInstance().getShoppingCart().unRegisterObs(this);
	}
}
