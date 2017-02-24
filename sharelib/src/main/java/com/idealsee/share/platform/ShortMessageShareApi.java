package com.idealsee.share.platform;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.idealsee.share.ShareHelper;
import com.idealsee.share.ShareType;
import com.idealsee.share.content.BaseShareContent;


/**
 * 短信分享接口
 */

public class ShortMessageShareApi {
    public static void share(Context context, BaseShareContent content,ShareType shareType) {
        if(shareType != ShareType.SHARE_VIDEO){
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"));
            intent.putExtra("sms_body", ShareHelper.mergeString(content.shareTitle, content.shareDetail, content.shareUrl));
            context.startActivity(intent);
        }
    }
}
