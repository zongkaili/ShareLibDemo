package com.kelly.sharelibdemo.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.idealsee.share.ShareApi;
import com.idealsee.share.ShareError;
import com.idealsee.share.platform.WeixinShareApi;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WeixinShareApi.getWeixinApi(this);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        api.handleIntent(intent, this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        // TODO Auto-generated method stub
        Log.d("WXEntryActivity sdk"," onReq req.toString ：　" + req.toString());

    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        Log.d("WXEntryActivity sdk"," onResp resp.errCode ：　" + resp.errCode);
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                ShareApi.shareComplete();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                ShareApi.shareCanceled();
                break;
            default:
                ShareError error = new ShareError(resp.errCode, resp.errStr, resp.errStr);
                ShareApi.shareError(error);
                break;
        }
        finish();
    }
}
