package com.cynovo.sirius.util;

import android.content.Context;
import android.view.Gravity;

import com.cynovo.sirius.R;
import com.cynovo.sirius.widget.Swiped_Dialog;

public class LockScreen {
	
	private Swiped_Dialog transparentCover = null;

	public LockScreen(Context mContext)
	{
		transparentCover = new Swiped_Dialog(mContext,
		800, 1280, 0, 0, R.layout.swiped_card_layout,
		R.style.Theme_dialog1, Gravity.LEFT | Gravity.TOP, false);
	}
	
	public void lock()
	{
		if(!transparentCover.isShowing())
			transparentCover.show();
	}
	
	public void unlock()
	{
		if (transparentCover.isShowing())
			transparentCover.dismiss();
	}

}
