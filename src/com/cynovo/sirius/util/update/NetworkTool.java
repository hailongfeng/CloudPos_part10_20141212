package com.cynovo.sirius.util.update;

import com.cynovo.sirius.core.Result;
import com.cynovo.sirius.network.NetworkCommunication;
import com.cynovo.sirius.util.MyLog;
import com.google.gson.Gson;

public class NetworkTool {
	public static UpdateGotInfo getApkPath(String url, String storeId,
			String apkName, String version) {
		UpdateSendInfo mUpdateSendInfo = new UpdateSendInfo(storeId, apkName, version);
		Gson sgson = new Gson();
		String sendStr = sgson.toJson(mUpdateSendInfo, UpdateSendInfo.class);
		MyLog.i("sendStr", sendStr);

		String ret = NetworkCommunication.postRequest(url, sendStr);
		if (ret != null) {
			Gson gson = new Gson();
			Result result = gson.fromJson(ret, Result.class);
			UpdateGotInfo gotInfo = gson.fromJson(ret, UpdateGotInfo.class);

			if (result != null && result.getRet() == 0) {
				if (!gotInfo.getPath().equals("EMPTY")) {
					return gotInfo;
				}
			}
		}
		return null;
	}
}
