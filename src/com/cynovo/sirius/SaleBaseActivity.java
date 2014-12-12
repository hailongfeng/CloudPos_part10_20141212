package com.cynovo.sirius;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.cynovo.sirius.R;

public class SaleBaseActivity extends AbstractActivity implements OnClickListener {
	Button useraccountBtn = null;
	Button unlockButton = null;
	View titalButton = null;
	View adminBtn = null;
	Button changeaccountBtn = null;
	FragmentManager fragmentManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final ActionBar bar = getActionBar();

		bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
		bar.setDisplayOptions(0, ActionBar.DISPLAY_USE_LOGO);
		bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_HOME);
		bar.setDisplayOptions(1, ActionBar.DISPLAY_SHOW_CUSTOM);
		bar.setDisplayOptions(0, ActionBar.DISPLAY_USE_LOGO);

		fragmentManager = getFragmentManager();
		titalButton = getLayoutInflater().inflate(R.layout.sale_title_buttons, null);
		useraccountBtn = (Button) titalButton.findViewById(R.id.sale_user_name);

		useraccountBtn.setOnClickListener(this);
		
		bar.setCustomView(titalButton, new ActionBar.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
		bar.setDisplayShowCustomEnabled(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sale_user_name:
				user_account_setup();
			break;
		case R.id.userDialogchangeaccount:
			change_useraccount();
			break;
		default:
			break;
		}
	}

	protected void user_account_setup() {
		
	}

	private void change_useraccount() {
		Intent intent = new Intent();
		intent.setClass(SaleBaseActivity.this, LoginInActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public void finish() {
		super.finish();
	}
}
