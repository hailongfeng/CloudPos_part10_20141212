package com.cynovo.sirius.line;

import java.util.List;

import com.cynovo.sirius.core.user.Account;
import com.cynovo.sirius.core.user.AccountManager;
import com.cynovo.sirius.util.communicate.OperateTimer;

public class Offline extends Line {

	@Override
	public void pollTimerJob(List<OperateTimer> ot) {
	}

	@Override
	public void orderTimerJob() {
	}

	@Override
	public void productTimerJob() {
	}

	@Override
	public int loginProceess(Account mAccount) {
		
		Account tmpAccount = AccountManager.getInstance().getAccountByName(mAccount.getAccount());
		
		if(tmpAccount != null && mAccount.getPass().equals(tmpAccount.getPass()))
		{
			return 2;
		}
		return 3;
	}

}
