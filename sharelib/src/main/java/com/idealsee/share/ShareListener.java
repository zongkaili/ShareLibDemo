package com.idealsee.share;

/**
 * Created by zongkaili on 2017/2/16.
 * 调用ShareApi中的share接口时传入的Listener，收到分享回调后，会调用对应的方法
 */

public interface ShareListener {
    void onComplete();
    void onCancel();
    void onError(ShareError shareError);
}
