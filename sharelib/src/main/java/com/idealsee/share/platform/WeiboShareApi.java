package com.idealsee.share.platform;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.idealsee.share.ShareHelper;
import com.idealsee.share.content.BaseShareContent;
import com.sina.weibo.sdk.WeiboAppManager;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.utils.Utility;

/**
 * 微博分享功能，调用微博SDK相关的Api
 * Created by zongkaili on 17-2-20.
 */

public class WeiboShareApi {
    // 新浪微博的APP ID
    private static final String WEIBO_APP_KEY = "891974957";

    /**
     * 分享
     *
     * @param activity     activity
     * @param content      要分享的内容
     * @param isShareVideo 是否是视频分享
     */
    public static void share(Activity activity, BaseShareContent content, boolean isShareVideo) {
        // 创建微博分享接口实例
        IWeiboShareAPI mWeiboShareAPI = getWeiboApi(activity);
        SendMultiMessageToWeiboRequest multiRequest = getWeiboShareMultiMessage(content, isShareVideo);
        // 发送分享请求
        mWeiboShareAPI.sendRequest(activity, multiRequest);
    }

    /**
     * Tips:分享链接至微博时，图片一定也要传入，因为分享至微博设置WebpageObject时，其每个成员变量都需要设置值
     *
     * @return
     */
    private static SendMultiMessageToWeiboRequest getWeiboShareMultiMessage(BaseShareContent content, boolean isShareVideo) {
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();

        // 要分享的文本内容
        TextObject textObject = new TextObject();
        textObject.text = content.shareDetail;
        weiboMessage.textObject = textObject;

        if (isShareVideo) {
            setShareVideoMultiMsg(content, weiboMessage);
        } else {
            setShareImgUrlMultiMsg(content, weiboMessage);
        }

        // 将要分享的内容封装到SendMultiMessageToWeiboRequest中
        SendMultiMessageToWeiboRequest multiRequest = new SendMultiMessageToWeiboRequest();
        multiRequest.transaction = String.valueOf(System.currentTimeMillis());
        multiRequest.multiMessage = weiboMessage;
        return multiRequest;
    }

    /**
     * 设置视频分享时的分享内容
     * @param content
     * @param weiboMessage
     */
    private static void setShareImgUrlMultiMsg(BaseShareContent content, WeiboMultiMessage weiboMessage) {
        Bitmap normalBm = null, thumbBm = null;
        if (!TextUtils.isEmpty(content.shareImage)) {//图片分享
            normalBm = BitmapFactory.decodeFile(content.shareImage);
            thumbBm = ShareHelper.getThumbBitmapFromFile(content.shareImage);
            // 要分享的图片内容
            ImageObject imageObject = new ImageObject();
            if (normalBm != null) {
                imageObject.setImageObject(normalBm);
            }
            if (imageObject != null) {
                weiboMessage.imageObject = imageObject;
            }
        }

        if (!TextUtils.isEmpty(content.shareUrl)) {//链接分享
            // 要分享的链接（设置WebpageObject时必须设置每个成员变量，不过最终显示的只有title）
            WebpageObject webpageObject = new WebpageObject();
            webpageObject.identify = Utility.generateGUID();
            if (thumbBm != null) {
                webpageObject.setThumbImage(thumbBm);
            }
            webpageObject.title = content.shareTitle;
            webpageObject.description = content.shareDetail;
            webpageObject.actionUrl = content.shareUrl;
            webpageObject.defaultText = content.shareDetail;

            weiboMessage.mediaObject = webpageObject;
        }
    }

    /**
     * 设置视频分享时的分享内容
     * @param content
     * @param weiboMessage
     */
    private static void setShareVideoMultiMsg(BaseShareContent content, WeiboMultiMessage weiboMessage) {
        WebpageObject webpageObject = new WebpageObject();
        webpageObject.identify = Utility.generateGUID();
        //分享微博时，title会作为文字描述显示在微博，微博分享页title与链接绑定不能更改，且title不能为空，考虑到体验问题做此更改
        webpageObject.title = content.shareTitle;
        webpageObject.description = content.shareDetail;
        webpageObject.actionUrl = content.shareVideoUrl;
        Bitmap thumbBm = ShareHelper.getThumbBitmapFromFile(content.shareImage);
        // 要分享的视频缩略图
        ImageObject imageObject = new ImageObject();
        if (thumbBm != null) {
            imageObject.setImageObject(thumbBm);
            webpageObject.setThumbImage(thumbBm);
        }
        if (imageObject != null) {
            weiboMessage.imageObject = imageObject;
        }
        weiboMessage.mediaObject = webpageObject;
    }

    // 这个函数是复制的WeiboShareAPIImpl的isWeiboAppInstalled()方法，但是避免了创建IWeiboShareAPI对象，
    // 在创建IWeiboShareAPI对象时会有一些额外的操作，还有一个异步的线程在运行。
    public static boolean isAppInstalled(Context context) {
        WeiboAppManager.WeiboInfo weiboInfo = WeiboAppManager.getInstance(context).getWeiboInfo();
        return weiboInfo != null && weiboInfo.isLegal();
    }


    public static IWeiboShareAPI getWeiboApi(Context context) {
        IWeiboShareAPI api = WeiboShareSDK.createWeiboAPI(context, WEIBO_APP_KEY);
        // 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
        // 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
        // NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
        api.registerApp();
        return api;
    }

    private static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
