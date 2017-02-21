package com.idealsee.share.platform;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;

import com.idealsee.share.ShareHelper;
import com.idealsee.share.content.BaseShareContent;

/**
 * Android系统分享
 */

public class SystemShareApi {


    public static void shareToWeixin(Context context, String title, String detail, String imageFile, String shareURL) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.tools.ShareImgUI");
        intent.setComponent(comp);
        intent.setAction(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("image/jpg");
        // 添加图片内容
        if (imageFile != null && !imageFile.isEmpty()) {
            File file = new File(imageFile);
            if (file.exists() && file.isFile()) {
                Uri uri = Uri.fromFile(file);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
            }
        }
        context.startActivity(intent);
    }
    public static void share(Context context, BaseShareContent content, boolean isShareVideo) {
        //TODO 暂时未做视频分享
        if(isShareVideo)
            return;
        Intent intent = new Intent(Intent.ACTION_SEND);

        // 设置目标类型
        if (content.shareImage != null && !content.shareImage.isEmpty()) {
            File file = new File(content.shareImage);
            if (file.exists() && file.isFile()) {
                intent.setType("image/*");
            } else {
                intent.setType("text/plain");
            }
        } else {
            intent.setType("text/plain");
        }

        // 添加文本内容
        String text = ShareHelper.mergeString(content.shareTitle, content.shareDetail, content.shareUrl);
        if (!text.isEmpty()) {
            intent.putExtra(Intent.EXTRA_TEXT, text);
            // 有些Android系统有个bug，
            // 当设置的分享类型为image/*，并且在系统分享的列表中选择分享到短信的时候，
            // 短信文本中出现的是sms_body，而不是Intent.EXTRA_TEXT
            // 这个问题在新的Android系统（不清楚是什么版本修了这个bug），或者分享类型是text/plain的时候均不会出现
            intent.putExtra("sms_body", text);
        }

        // 添加图片内容
        if (content.shareImage != null && !content.shareImage.isEmpty()) {
            File file = new File(content.shareImage);
            if (file.exists() && file.isFile()) {
                Uri uri = Uri.fromFile(file);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
            }
        }

        context.startActivity(Intent.createChooser(intent, "分享到"));
    }
}
