package com.cynovo.sirius.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.cynovo.sirius.R;
import com.cynovo.sirius.core.store.StoreManager;

public class ViewHelper {
	
	public static void setStoreInfo(Context context ,TextView mStoreInfo)
	{
		if(mStoreInfo != null)
		{
			String mName = StoreManager.getStoreName();
			mStoreInfo.setText(mName);
			
			Drawable mPhoto = StoreManager.getStore().getPhotoToShow();
			
			if( mPhoto == null)
			{
				mPhoto = FileManager.readBitMapDrawable(context, R.drawable.logo);
			}
			
			if (mPhoto != null) {
				mPhoto.setBounds(0, 0, 98,98);
			}
			
			mStoreInfo.setCompoundDrawables(mPhoto, null, null, null);
		}
	}
}
