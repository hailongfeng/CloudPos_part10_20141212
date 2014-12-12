package com.cynovo.sirius.util.communicate;

import com.cynovo.sirius.line.LineFactory;
import com.cynovo.sirius.util.MyLog;

public class ProductConnectTimer extends OperateTimer {

	@Override
	protected void timerJob() {
		MyLog.e("ProductConnectTimer", "enter ProductConnectTimer timerJob");
		LineFactory.getCurrentLine().productTimerJob();
	}

}
