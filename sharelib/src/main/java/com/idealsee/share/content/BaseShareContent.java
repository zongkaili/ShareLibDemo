package com.idealsee.share.content;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 分享的内容
 */

public class BaseShareContent implements Parcelable {
    public static final String title = "title";
    public static final String detail = "detail";
    public static final String imageFile = "imageFile";
    public static final String shareURL = "shareURL";
    public static final String shareVideoURL = "shareVideoURL";
    public static final String shareVideoThuPath = "shareVideoThuPath";

    public static final Parcelable.Creator<BaseShareContent> CREATOR = new Parcelable.Creator<BaseShareContent>() {
        public BaseShareContent createFromParcel(Parcel in) {
            return new BaseShareContent(in);
        }

        public BaseShareContent[] newArray(int size) {
            return new BaseShareContent[size];
        }
    };

    public String shareTitle;       // 要分享的标题
    public String shareDetail;      // 要分享的详情（这里不同平台的叫法不一样，QQ叫summary，微博叫text，微信和易信叫description）
    public String shareImage;       // 要分享的图片
    public String shareUrl;         // 要分享的URL
    public String shareVideoUrl;         // 要分享的视频URL
    public String shareVideoThumbPath;         // 要分享的视频缩略图路径

    public BaseShareContent(String shareTitle, String shareDetail, String shareImage, String shareUrl,String shareVideoUrl,String shareVideoThumbPath) {
        this.shareTitle = shareTitle;
        this.shareDetail = shareDetail;
        this.shareImage = shareImage;
        this.shareUrl = shareUrl;
        this.shareVideoUrl = shareVideoUrl;
        this.shareVideoThumbPath = shareVideoThumbPath;
    }

    public BaseShareContent(Parcel in) {
        this.shareTitle = in.readString();
        this.shareDetail = in.readString();
        this.shareImage = in.readString();
        this.shareUrl = in.readString();
        this.shareVideoUrl = in.readString();
        this.shareVideoThumbPath = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.shareTitle);
        dest.writeString(this.shareDetail);
        dest.writeString(this.shareImage);
        dest.writeString(this.shareUrl);
        dest.writeString(this.shareVideoUrl);
        dest.writeString(this.shareVideoThumbPath);
    }

    public boolean isValid() {
        return true;
    }
}
