package com.cynovo.sirius;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

import com.cynovo.sirius.constants.MyConstants;
import com.cynovo.sirius.fragment.ActiveFragment;
import com.cynovo.sirius.fragment.LoginPageFragment;
import com.cynovo.sirius.fragment.LoginUpdateFragment;
import com.cynovo.sirius.myinterface.OnActiveCompleteListener;
import com.cynovo.sirius.sqlite.MySharedPreferencesEdit;
import com.umeng.analytics.MobclickAgent;

public class LoginInActivity extends AbstractFragmentActivity implements OnActiveCompleteListener {
	private MySharedPreferencesEdit mySharedPreferencesEdit;
	static ViewPager mViewPager;
	TabsAdapter mTabsAdapter;
	private static LoginPageFragment loginPageFragment;
	private static ActiveFragment activeFragment;
	private static LoginUpdateFragment loginUpdateFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MobclickAgent.openActivityDurationTrack(false);

		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.pager);
		mViewPager.setEnabled(false);
		setContentView(mViewPager);

		final ActionBar bar = getActionBar();

		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
		bar.setDisplayOptions(0, ActionBar.DISPLAY_USE_LOGO);
		bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_HOME);
		bar.setDisplayOptions(1, ActionBar.DISPLAY_SHOW_CUSTOM);

		bar.setIcon(R.drawable.head_home);

		mTabsAdapter = new TabsAdapter(this, mViewPager);

		mTabsAdapter.addTab(bar.newTab().setText(getString(R.string.user_login_in))
				.setIcon(R.drawable.head_user), LoginPageFragment.class, null);
		if(0 == MyConstants.SHOULD_REGISTER) {
			mTabsAdapter.addTab(bar.newTab().setText(getString(R.string.active_device))
					.setIcon(R.drawable.active_icon), ActiveFragment.class, null);
		}
		mTabsAdapter.addTab(bar.newTab().setText(getString(R.string.update_title))
				.setIcon(R.drawable.update),LoginUpdateFragment.class, null);
//		if(StoreManager.getStore().getStoreID() > 0) {
//			mTabsAdapter.addTab(bar.newTab().setText(getString(R.string.update_title))
//					.setIcon(R.drawable.update), LoginUpdateFragment.class, null);
//		}

		if (savedInstanceState != null) {
			bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
		}

		mySharedPreferencesEdit = MySharedPreferencesEdit.getInstancePublic(this);
		if(1 == MyConstants.SHOULD_REGISTER) {
			mySharedPreferencesEdit.setIsDownloadSecretKey(true);
			mySharedPreferencesEdit.setMerchantNo("1");
			mySharedPreferencesEdit.setTerminalNo("1");
		} else if(0 == MyConstants.SHOULD_REGISTER) {
			if (!mySharedPreferencesEdit.getIsDownloadSecretKey()) {
				// 此处判断机器是否已被激活
				mViewPager.setCurrentItem(1);
			}
		}
		mViewPager.setOffscreenPageLimit(2);
	}
	
	public static ViewPager getViewPager() {
		return mViewPager;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
	}

	public static class TabsAdapter extends FragmentPagerAdapter implements
			ActionBar.TabListener, ViewPager.OnPageChangeListener {
		private final ActionBar mActionBar;
		private final ViewPager mViewPager;
		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

		static final class TabInfo {
			TabInfo(Class<?> _class, Bundle _args) {
			}
		}

		public TabsAdapter(FragmentActivity activity, ViewPager pager) {
			super(activity.getSupportFragmentManager());
			mActionBar = activity.getActionBar();
			mViewPager = pager;
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
		}

		public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
			TabInfo info = new TabInfo(clss, args);
			tab.setTag(info);
			tab.setTabListener(this);
			mTabs.add(info);
			mActionBar.addTab(tab);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mTabs.size();
		}

		@Override
		public Fragment getItem(int position) {
			Fragment f = null;

			if (position == 0 && loginPageFragment == null) {
				f = new LoginPageFragment();
				loginPageFragment = (LoginPageFragment) f;
			} else if (position == 0 && loginPageFragment != null) {
				f = loginPageFragment;
			} else if (position == 1 && activeFragment == null) {
				if(1 == MyConstants.SHOULD_REGISTER) {
					f = new LoginUpdateFragment();
					loginUpdateFragment = (LoginUpdateFragment) f;
				} else if(0 == MyConstants.SHOULD_REGISTER) {
					f = new ActiveFragment();
					activeFragment = (ActiveFragment) f;
				}
			} else if (position == 1 && activeFragment != null) {
				if(MyConstants.SHOULD_REGISTER == 1) {
					f = loginUpdateFragment;
				} else if(MyConstants.SHOULD_REGISTER == 0) {
					f = activeFragment;
				}
			} else if (position == 2 && loginUpdateFragment == null) {
				f = new LoginUpdateFragment();
				loginUpdateFragment = (LoginUpdateFragment) f;
			} else if(position == 2 && loginUpdateFragment != null) {
				f = loginUpdateFragment;
			}
			return f;
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int position) {
			mActionBar.setSelectedNavigationItem(position);
		}

		@Override
		public void onPageScrollStateChanged(int state) {
		}

		public void onTabSelected(Tab tab, android.app.FragmentTransaction ft) {
			Object tag = tab.getTag();
			for (int i = 0; i < mTabs.size(); i++) {
				if (mTabs.get(i) == tag) {
					mViewPager.setCurrentItem(i);
				}
			}
		}

		public void onTabUnselected(Tab tab, android.app.FragmentTransaction ft) {
		}

		public void onTabReselected(Tab tab, android.app.FragmentTransaction ft) {
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return false;
	}

	public void refreshFirstView() {
	}

	@Override
	public void OnActiveComplete() {
		mViewPager.setCurrentItem(0);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				loginPageFragment.refresh(LoginInActivity.this);
			}
		}, 100);
	}
}