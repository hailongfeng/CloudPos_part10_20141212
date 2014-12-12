package com.cynovo.sirius;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ViewFlipper;

import com.cynovo.sirius.constants.MyConstants;
import com.cynovo.sirius.core.DealModel;
import com.cynovo.sirius.fragment.CheckSelfFragment;
import com.cynovo.sirius.fragment.LeftBarDealInfoFragment;
import com.cynovo.sirius.fragment.OrderRemarkFragment;
import com.cynovo.sirius.util.CloudPosApp;
import com.cynovo.sirius.util.CreatDialogClass;
import com.cynovo.sirius.util.JavaUtil;
import com.cynovo.sirius.widget.MoneyView;

public class SaleActivity extends SaleBaseActivity implements OnLongClickListener, DrawerListener {
	private static int checkTimes = 0;
	public MoneyView billingBtn = null;
	public MoneyView hangUpBtn = null;
	private Button recent_Btn = null;
	private Button mProudctBtn = null;
	private ViewFlipper mSaleListViewFlipper = null;
	private DrawerLayout mDrawerLayout = null;
	
	Handler handler = new Handler();
	private volatile boolean enableCheck = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.sale_activity);
		billingBtn = (MoneyView) this.findViewById(R.id.salelisttotalmoney);
		hangUpBtn = (MoneyView) this.findViewById(R.id.hangup_order_btn);
		billingBtn.setOnClickListener(this);
		hangUpBtn.setOnClickListener(this);
		billingBtn.setOnLongClickListener(this);
		hangUpBtn.setOnLongClickListener(this);

		recent_Btn = (Button) findViewById(R.id.sale_recent);
		recent_Btn.setOnClickListener(this);
		mProudctBtn = (Button) findViewById(R.id.productmanager);
		mProudctBtn.setOnClickListener(this);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		// set a custom shadow that overlays the main content when the drawer opens
//		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
		mDrawerLayout.setDrawerListener(this);
		mSaleListViewFlipper = (ViewFlipper) this.findViewById(R.id.salelistviewflliper);
		DealModel.getInstance().SetContext(this);
//		WorkKeyThread.LinkWorkInit();
		handler.postDelayed(runnableUi, 100);
		handler.postDelayed(mDeviceCheck, 10);
		checkTimes = 0;
	}
	
	private void enableButton(boolean status) {
		billingBtn.setClickable(status);
		hangUpBtn.setClickable(status);
	}
	
	
	Runnable runnableUi = new Runnable() {
		@Override
		public void run() {
			enableCheck = true;
		}
	};
	
	
	@Override
	protected void onResume() {
		super.onResume();
		enableButton(true);
	}
	
	Runnable mDeviceCheck = new Runnable() {
		@Override
		public void run() {
			if(checkTimes == 0) {
				checkTimes++;
				if(MyConstants.SHOULD_CHECK_SELF == 0) {
					SaleActivity.this.showCheckDialog();
				}
			}
		}
	};
	 
	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacks(runnableUi);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if(MyConstants.SHOULD_CHECK_SELF == 0) {
		    MenuInflater inflater = getMenuInflater();
		    inflater.inflate(R.menu.check_status, menu);
		}
	    return true;
	}
	
	public void hideDialog() {
		mDrawerLayout.closeDrawers();
	}
	
	protected void user_account_setup() {
		if(mDrawerLayout.isDrawerOpen(Gravity.LEFT))
			mDrawerLayout.closeDrawers();
		new CreatDialogClass(this).show();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == R.id.menu_checkself && enableCheck 
				&& !CloudPosApp.getInstance().isCheckDeviceInProcess()) {
			showCheckDialog();
		}
		return super.onOptionsItemSelected(item);
	}

	private void showCheckDialog() {
		if(CloudPosApp.getInstance().isWorkkeyInprocess())
			return;
		CheckSelfFragment checkSelf = CheckSelfFragment.newInstance(0);
		checkSelf.show(getFragmentManager(), "dialog");
	}
	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		super.onClick(v);
		if(JavaUtil.isFastDoubleClick())
			return;
		switch(v.getId()) {
		case R.id.hangup_order_btn:
			enableButton(false);
			mSaleListViewFlipper.setDisplayedChild(0);
			DealModel.getInstance().HangUpCurrentOrder();
			break;
		case R.id.salelisttotalmoney:
			enableButton(false);
			DealModel.getInstance().beginToPayTheOrder();
			break;
		case R.id.sale_recent:
			if(mDrawerLayout.isDrawerOpen(Gravity.LEFT))
				mDrawerLayout.closeDrawers();
			else
				mDrawerLayout.openDrawer(Gravity.LEFT);
			break;
		case R.id.productmanager:
			Intent nextIntent = new Intent(this,ProductManagerActivity.class);
			Bundle translateBundle = ActivityOptions.makeCustomAnimation(this,android.R.anim.fade_in,
					android.R.anim.fade_out).toBundle();
			this.startActivity(nextIntent,translateBundle);
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return false;
	}

	@Override
	public boolean onLongClick(View v) {
		switch (v.getId()) {
		case R.id.salelisttotalmoney:
		case R.id.hangup_order_btn:
			OrderRemarkFragment mOrderRemarkFragment = OrderRemarkFragment.newInstance(-1);
			mOrderRemarkFragment.show(getFragmentManager(), "dialog");
		}
		return false;
	}

	@Override
	public void onDrawerClosed(View arg0) {
	}
	
	private boolean IsFragment(String name){
		FragmentManager mfragmentManager = getFragmentManager();
		Fragment fragment = mfragmentManager.findFragmentById(R.id.leftbar_container);
		if(fragment != null &&fragment.getClass().getSimpleName().equals(name) )
			return true;
		return false;
	}
	
	@Override
	public void onDrawerOpened(View arg0) {
		if(IsFragment("LeftBarDealInfoFragment")) {
			LeftBarDealInfoFragment mLeftBarDealInfoFragment = (LeftBarDealInfoFragment)
					getFragmentManager().findFragmentById(R.id.leftbar_container);
			mLeftBarDealInfoFragment.updateList();
		}
	}

	@Override
	public void onDrawerSlide(View arg0, float arg1) {
	}

	@Override
	public void onDrawerStateChanged(int arg0) {
	}
}
