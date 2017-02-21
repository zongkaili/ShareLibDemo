package com.idealsee.share.platform;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.idealsee.share.R;
import com.idealsee.share.ShareApi;
import com.idealsee.share.ShareError;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.constant.WBConstants;

//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;


/**
 * 微博分享功能，调用微博SDK相关的Api
 * 分享完成后，回调的时候需要原先的Activity实现IWeiboHandler.Respons接口，为了避免每个分享的activity都要这样做一遍，
 * 这里封装一个透明的activity，用来作为调用分享api的activity
 *
 * TODO 由于之前已经在app中的WXEntryActivity中实现了IWeiboHandler.Respons接口，故微博回调时存在冲突，故暂时不让回调到此界面
 * Tips:若要回调到此类，必须先打开AndroidManifest.xml中此类下的intent-filter
 */
public class WeiBoShareActivity extends Activity implements IWeiboHandler.Response {
    private IWeiboShareAPI mWeiboShareAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transparent);

        // 创建微博分享接口实例
        mWeiboShareAPI = WeiboShareApi.getWeiboApi(this);

        // 当 Activity 被重新初始化时（该 Activity 处于后台时，可能会由于内存不足被杀掉了），
        // 需要调用 {@link IWeiboShareAPI#handleWeiboResponse} 来接收微博客户端返回的数据。
        // 执行成功，返回 true，并调用 {@link IWeiboHandler.Response#onResponse}；
        // 失败返回 false，不调用上述回调
        if (savedInstanceState != null) {
            mWeiboShareAPI.handleWeiboResponse(getIntent(),this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
        mWeiboShareAPI.handleWeiboResponse(intent, this);
    }

    // 分享完成后收到的回调信息
    @Override
    public void onResponse(BaseResponse baseResponse) {
        Log.d("WeiBoShareActivity"," onResponse baseResponse.errCode ：　" + baseResponse.errCode);
        switch (baseResponse.errCode) {
            case WBConstants.ErrorCode.ERR_OK:
                ShareApi.shareComplete();
                break;
            case WBConstants.ErrorCode.ERR_CANCEL:
                ShareApi.shareCanceled();
                break;
            case WBConstants.ErrorCode.ERR_FAIL:
            default:
                ShareApi.shareError(new ShareError(baseResponse.errCode, baseResponse.errMsg, baseResponse.errMsg));
                break;
        }
        finish();
    }
}
