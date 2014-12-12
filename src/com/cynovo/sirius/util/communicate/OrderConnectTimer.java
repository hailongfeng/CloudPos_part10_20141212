package com.cynovo.sirius.util.communicate;

import com.cynovo.sirius.line.LineFactory;
import com.cynovo.sirius.util.MyLog;

public class OrderConnectTimer extends OperateTimer {

	@Override
	protected void timerJob() {
		MyLog.e("OrderConnectTimer", "enter OrderConnectTimer timerJob");
		LineFactory.getCurrentLine().orderTimerJob();
	}

}
