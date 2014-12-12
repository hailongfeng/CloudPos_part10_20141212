package com.cynovo.sirius.util;

import com.cynovo.sirius.PAR10Main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

	static final String ACTION = "android.intent.action.BOOT_COMPLETED";

	public BootReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		MyLog.d("onReceive");
		if (intent.getAction().equals(ACTION)) {
			MyLog.d(ACTION);
			Intent startintent = new Intent(context, PAR10Main.class);
			startintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(startintent);
		}
	}
}
