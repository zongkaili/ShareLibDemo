package com.idealsee.share.wxapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import com.idealsee.share.platform.WeixinShareApi;

public class WXReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		final IWXAPI api = WXAPIFactory.createWXAPI(context, null);
		// 将该app注册到微信
		api.registerApp(WeixinShareApi.WEIXIN_APP_ID);
	}
}
