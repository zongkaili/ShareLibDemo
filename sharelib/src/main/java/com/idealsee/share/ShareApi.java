package com.idealsee.share;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.lang.ref.SoftReference;

import com.idealsee.share.content.BaseShareContent;
import com.idealsee.share.platform.ShortMessageShareApi;
import com.idealsee.share.platform.SystemShareApi;
import com.idealsee.share.platform.WeiboShareApi;
import com.idealsee.share.platform.WeixinShareApi;


/**
 * 分享api，根据要分享的目标平台来决定使用哪个sdk下面的接口
 */
public class ShareApi {
    // 成员变量
    private static SoftReference<ShareListener> mListener; // 分享完成后的回调，它是static的，只能保存最近一次调用share接口的回调对象

    public ShareApi() {

    }

    // 判断要分享的目标app是否已经安装
    public static boolean isPlatformInstalled(Activity activity, SharePlatform platform) {
        switch (platform) {
            case SINA_WEIBO:
//                return WeiBoShareActivity.isAppInstalled(activity);
                return WeiboShareApi.isAppInstalled(activity);
            case WEIXIN:
            case WEIXIN_PYQ:
                return WeixinShareApi.isAppInstalled(activity);
            case SHORT_MESSAGE:
            case SYSTEM_SHARE:
                return true;
        }
        return false;
    }

    // 当收到回调消息，表示分享被取消时调用
    public static void shareCanceled() {
        Log.d("ShareApi", " shareCanceled mListener ：　" + mListener);
        if (mListener != null) {
            ShareListener listener = mListener.get();
            Log.d("ShareApi", " shareCanceled listener ：　" + listener);
            if (listener != null) {
                listener.onCancel();
            }
            mListener = null;
        }
    }

    // 当收到回调消息，表示分享完成时调用
    public static void shareComplete() {
        if (mListener != null) {
            ShareListener listener = mListener.get();
            if (listener != null) {
                listener.onComplete();
            }
            mListener = null;
        }
    }

    // 当收到回调消息，表示分享失败时调用
    public static void shareError(ShareError error) {
        if (mListener != null) {
            ShareListener listener = mListener.get();
            if (listener != null) {
                listener.onError(error);
            }
            mListener = null;
        }
    }

    // 分享
    public static void share(Activity activity, SharePlatform platform, BaseShareContent content, ShareListener listener, ShareType shareType) {
        // 没有分享内容，返回
        if (content == null)
            return;

        mListener = new SoftReference<>(listener);

        switch (platform) {
            case SINA_WEIBO:
                shareToWeibo(activity, content, shareType);
                break;
            case WEIXIN:
                shareToWeixin(activity, content, shareType, false);
                break;
            case WEIXIN_PYQ:
                shareToWeixin(activity, content, shareType, true);
                break;
            case SHORT_MESSAGE:
                shareToShortMessage(activity, content, shareType);
                break;
            case SYSTEM_SHARE:
                shareToSystemShare(activity, content, shareType);
                break;
        }
    }

    // 分享到新浪微博
    private static void shareToWeibo(Activity activity, BaseShareContent content, ShareType shareType) {
//        Intent intent = new Intent(activity, WeiBoShareActivity.class);
//        intent.putExtra(BaseShareContent.title, title);
//        intent.putExtra(BaseShareContent.detail, detail);
//        intent.putExtra(BaseShareContent.imageFile, imageFile);
//        intent.putExtra(BaseShareContent.shareURL, shareURL);
//        activity.startActivity(intent);
        WeiboShareApi.share(activity, content, shareType);
    }

    // 分享到微信
    private static void shareToWeixin(Context context, BaseShareContent content, ShareType shareType, boolean isTimeline) {
        WeixinShareApi.share(context, content, shareType, isTimeline);
    }

    // 分享到短信
    private static void shareToShortMessage(Context context, BaseShareContent content, ShareType shareType) {
        ShortMessageShareApi.share(context, content, shareType);
    }

    // 分享到系统分享
    private static void shareToSystemShare(Context context, BaseShareContent content, ShareType shareType) {
        SystemShareApi.share(context, content, shareType);
    }
}
