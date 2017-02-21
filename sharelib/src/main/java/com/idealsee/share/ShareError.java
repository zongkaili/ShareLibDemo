package com.idealsee.share;

/**
 * 分享收到回调中的错误信息
 */

public class ShareError {
    public int errorCode;           // 错误码
    public String errorMessage;     // 错误消息
    public String errorDetail;      // 错误详情(只有QQ分享回调的错误信息中会带有一个详情信息,本应用目前的分享不涉及QQ分享，故回调错误信息中不包含此参数)

    public ShareError(int errorCode, String errorMessage, String errorDetail) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.errorDetail = errorDetail;
    }

    @Override
    public String toString() {
        return " Error Code: " + errorCode + " Error meessage: " + errorMessage + " Error Detail: " + errorDetail;
    }
}
