package com.idealsee.share.platform;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.idealsee.share.content.BaseShareContent;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXVideoObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import com.idealsee.share.ShareHelper;

/**
 * 微信分享功能，调用微信SDK相关的Api
 */
public class WeixinShareApi {
    // 微信的APP ID，由于微信在回调Receiver（WXReceiver）里面需要用到APP ID，所以这里定义为public
    public static final String WEIXIN_APP_ID = "wx537feebd640931cc";

    /**
     * 普通图片或链接分享
     *
     * @param context    context
     * @param content      要分享的内容对象
     * @param isShareVideo  是否时视频分享
     * @param isTimeline 是否分享到朋友圈
     */
    public static void share(Context context, BaseShareContent content, boolean isShareVideo, boolean isTimeline) {
        IWXAPI api = getWeixinApi(context);
        SendMessageToWX.Req req = getWeixinShareReq(content, isShareVideo, isTimeline);
        api.sendReq(req);
    }

    private static SendMessageToWX.Req getWeixinShareReq(BaseShareContent content, boolean isShareVideo, boolean isTimeline) {
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        WXMediaMessage msg = new WXMediaMessage();

        if (isShareVideo) {//视频分享
            setShareVideoReq(content, req, msg);
        } else {//图片或者链接分享
            setShareImgUrlReq(content, req, msg);
        }

        msg.title = content.shareTitle;
        msg.description = content.shareDetail;

        req.message = msg;
        req.scene = isTimeline ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        return req;
    }

    private static void setShareImgUrlReq(BaseShareContent content, SendMessageToWX.Req req, WXMediaMessage msg) {
        if (!TextUtils.isEmpty(content.shareUrl)) {//链接分享
            WXWebpageObject webObj = new WXWebpageObject();
            webObj.webpageUrl = content.shareUrl;
            msg.mediaObject = webObj;
            req.transaction = buildTransaction("webpage");
        }

        if (!TextUtils.isEmpty(content.shareImage)) {//图文分享
            WXImageObject imgObj = new WXImageObject();
            imgObj.setImagePath(content.shareImage);

            Bitmap bmp = ShareHelper.getThumbBitmapFromFile(content.shareImage);
            if (bmp != null) {
                msg.thumbData = ShareHelper.bmpToByteArray(bmp);
            }
            msg.mediaObject = imgObj;
            req.transaction = buildTransaction("img");
        }
    }

    private static void setShareVideoReq(BaseShareContent content, SendMessageToWX.Req req, WXMediaMessage msg) {
        WXVideoObject videoObj = new WXVideoObject();
        videoObj.videoUrl = content.shareVideoUrl;
        msg.mediaObject = videoObj;
        Bitmap bmp = ShareHelper.getThumbBitmapFromFile(content.shareVideoThumbPath);
        if (bmp != null) {
            msg.thumbData = ShareHelper.bmpToByteArray(bmp);
        }
        req.transaction = buildTransaction("video");
    }

    public static boolean isAppInstalled(Activity activity) {
        return getWeixinApi(activity).isWXAppInstalled();
    }

    public static IWXAPI getWeixinApi(Context context) {
        IWXAPI api = WXAPIFactory.createWXAPI(context, WEIXIN_APP_ID, false);
        api.registerApp(WEIXIN_APP_ID);
        return api;
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
