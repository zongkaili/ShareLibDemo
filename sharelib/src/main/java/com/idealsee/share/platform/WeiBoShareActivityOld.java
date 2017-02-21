//package com.idealsee.share.platform;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
////import android.support.annotation.Nullable;
////import android.support.v7.app.AppCompatActivity;
//
//import com.idealsee.share.R;
//import com.idealsee.share.content.BaseShareContent;
//import com.sina.weibo.sdk.WeiboAppManager;
//import com.sina.weibo.sdk.api.ImageObject;
//import com.sina.weibo.sdk.api.TextObject;
//import com.sina.weibo.sdk.api.WebpageObject;
//import com.sina.weibo.sdk.api.WeiboMultiMessage;
//import com.sina.weibo.sdk.api.share.BaseResponse;
//import com.sina.weibo.sdk.api.share.IWeiboHandler;
//import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
//import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
//import com.sina.weibo.sdk.api.share.WeiboShareSDK;
//import com.sina.weibo.sdk.constant.WBConstants;
//import com.sina.weibo.sdk.utils.Utility;
//
//import com.idealsee.share.ShareApi;
//import com.idealsee.share.ShareError;
//import com.idealsee.share.ShareHelper;
//
//
///**
// * 微博分享功能，调用微博SDK相关的Api
// * 分享完成后，回调的时候需要原先的Activity实现IWeiboHandler.Respons接口，为了避免每个分享的activity都要这样做一遍，
// * 这里封装一个透明的activity，用来作为调用分享api的activity
// *
// * TODO 由于之前已经在app中的WXEntryActivity中实现了IWeiboHandler.Respons接口，故微博回调时存在冲突，故暂时不让回调到此界面
// */
//public class WeiBoShareActivity extends Activity implements IWeiboHandler.Response {
//    // 新浪微博的APP ID
//    private static final String WEIBO_APP_KEY = "891974957";
//
//    private IWeiboShareAPI mWeiboShareAPI;
//    private String mShareTitle;
//    private String mShareDetail;
//    private String mShareImage;
//    private String mShareURL;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_transparent);
//
//        // 创建微博分享接口实例
//        mWeiboShareAPI = getWeiboApi(this);
//
//        // 当 Activity 被重新初始化时（该 Activity 处于后台时，可能会由于内存不足被杀掉了），
//        // 需要调用 {@link IWeiboShareAPI#handleWeiboResponse} 来接收微博客户端返回的数据。
//        // 执行成功，返回 true，并调用 {@link IWeiboHandler.Response#onResponse}；
//        // 失败返回 false，不调用上述回调
//        if (savedInstanceState != null) {
//            mWeiboShareAPI.handleWeiboResponse(getIntent(),this);
//        }
//
//        // 获取要分享的内容
//        Intent intent = getIntent();
//        mShareTitle = intent.getStringExtra(BaseShareContent.title);
//        mShareDetail = intent.getStringExtra(BaseShareContent.detail);
//        mShareImage = intent.getStringExtra(BaseShareContent.imageFile);
//        mShareURL = intent.getStringExtra(BaseShareContent.shareURL);
//        // 分享
//        share();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//    }
//
//    private void share() {
//        SendMultiMessageToWeiboRequest multiRequest = getWeiboShareMultiMessage();
//        // 发送分享请求
//        mWeiboShareAPI.sendRequest(this, multiRequest);
//
//        /**
//         * TODO
//         * 此处添加finish是权宜之计，为了不和之前app中WXEntryActivity中设置的微博回调冲突，
//         * 故不让回调到此界面，所以为解决接收不到回调时此界面无法finish ,暂时在发送完分享请求后，就finish这个界面
//         */
//        finish();
//    }
//
//    /**
//     * Tips:分享链接至微博时，图片一定也要传入，因为分享至微博设置WebpageObject时，其每个成员变量都需要设置值
//     * @return
//     */
//    private SendMultiMessageToWeiboRequest getWeiboShareMultiMessage() {
//        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
//
//        // 要分享的文本内容
//        TextObject textObject = new TextObject();
//        textObject.text = mShareDetail;
//
//        Bitmap normalBm = null,thumbBm = null;
//        if(!TextUtils.isEmpty(mShareImage)){//图片分享
//            normalBm = BitmapFactory.decodeFile(mShareImage);
//            thumbBm = ShareHelper.getThumbBitmapFromFile(mShareImage);
//            // 要分享的图片内容
//            ImageObject imageObject = null;
//            if (normalBm != null) {
//                imageObject = new ImageObject();
//                imageObject.setImageObject(normalBm);
//            }
//
//            if (imageObject != null) {
//                weiboMessage.imageObject = imageObject;
//            }
//        }
//
//        if(!TextUtils.isEmpty(mShareURL)) {//链接分享
//            // 要分享的链接（设置WebpageObject时必须设置每个成员变量，不过最终显示的只有title）
//            WebpageObject webpageObject = new WebpageObject();
//            webpageObject.identify = Utility.generateGUID();
//            if (thumbBm != null) {
//                webpageObject.setThumbImage(thumbBm);
//            }
//            webpageObject.title = mShareTitle;
//            webpageObject.description = mShareDetail;
//            webpageObject.actionUrl = mShareURL;
//            webpageObject.defaultText = mShareDetail;
//
//            weiboMessage.mediaObject = webpageObject;
//        }
//
//        weiboMessage.textObject = textObject;
//
//        // 将要分享的内容封装到SendMultiMessageToWeiboRequest中
//        SendMultiMessageToWeiboRequest multiRequest = new SendMultiMessageToWeiboRequest();
//        multiRequest.transaction = String.valueOf(System.currentTimeMillis());
//        multiRequest.multiMessage = weiboMessage;
//        return multiRequest;
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//
//        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
//        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
//        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
//        mWeiboShareAPI.handleWeiboResponse(intent, this);
//    }
//
//    // 分享完成后收到的回调信息
//    @Override
//    public void onResponse(BaseResponse baseResponse) {
//        Log.d("WeiBoShareActivity"," onResponse baseResponse.errCode ：　" + baseResponse.errCode);
//        switch (baseResponse.errCode) {
//            case WBConstants.ErrorCode.ERR_OK:
//                ShareApi.shareComplete();
//                break;
//            case WBConstants.ErrorCode.ERR_CANCEL:
//                ShareApi.shareCanceled();
//                break;
//            case WBConstants.ErrorCode.ERR_FAIL:
//            default:
//                ShareApi.shareError(new ShareError(baseResponse.errCode, baseResponse.errMsg, baseResponse.errMsg));
//                break;
//        }
//        finish();
//    }
//
//    // 这个函数是复制的WeiboShareAPIImpl的isWeiboAppInstalled()方法，但是避免了创建IWeiboShareAPI对象，
//    // 在创建IWeiboShareAPI对象时会有一些额外的操作，还有一个异步的线程在运行。
//    public static boolean isAppInstalled(Context context) {
//        WeiboAppManager.WeiboInfo weiboInfo = WeiboAppManager.getInstance(context).getWeiboInfo();
//        return weiboInfo != null && weiboInfo.isLegal();
//    }
//
//    private static IWeiboShareAPI getWeiboApi(Context context) {
//        IWeiboShareAPI api = WeiboShareSDK.createWeiboAPI(context, WEIBO_APP_KEY);
//        // 注册第三方应用到微博客户端中，注册成功后该应用将显示在微博的应用列表中。
//        // 但该附件栏集成分享权限需要合作申请，详情请查看 Demo 提示
//        // NOTE：请务必提前注册，即界面初始化的时候或是应用程序初始化时，进行注册
//        api.registerApp();
//        return api;
//    }
//}
