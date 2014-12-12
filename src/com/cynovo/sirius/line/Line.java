package com.cynovo.sirius.line;

import java.util.List;

import com.cynovo.sirius.core.user.Account;
import com.cynovo.sirius.util.communicate.OperateTimer;

public abstract class Line {

	abstract public void pollTimerJob(List<OperateTimer> ot);
	abstract public void orderTimerJob();
	abstract public void productTimerJob();
	abstract public int loginProceess(Account mAccount);
}
