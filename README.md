# MyShareLibDemo
此demo是根据项目需要单独封装的分享模块
目前集成了微信，微博sdk,支持分享到微信，微信朋友圈,和微博，以及分享到短信和系统分享
主要用于总结学习使用

## 使用说明

### 1. 引入sdk
导入此module，然后在在app的build.gradle的dependencies中添加compile project(':sharelib')

### 2. AndroidManifest配置
以下权限必须添加，如果原项目中有则可不必重复添加
- \<uses-permission android:name="android.permission.INTERNET" />
- \<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
- \<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
- \<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

### 3. 处理微信回调activity
注意：微信分享回调默认是原app包名下wxapi下的WXEntryActivity,所以需要将sharelib库中的wxapi文件直接复制到包名路径下
### 4. 替换appid
替换WeiboShareApi，WeixinShareApi中的常量appid为自己应用在相应平台注册时的appid。

### 5. app层调用分享
isPlatformInstalled(): 判断要分享的目标平台是否已经安装
```java
 if(ShareApi.isPlatformInstalled(this,sharePlatform)){
            switch (sharePlatform){
                case WEIXIN:
                case WEIXIN_TIMELINE:
                    Toast.makeText(this,getResources().getString(R.string.tip_not_install_weixin),Toast.LENGTH_LONG).show();
                    break;
                case SINA_WEIBO:
                    Toast.makeText(this,getResources().getString(R.string.tip_not_install_weibo),Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
            return;
        }
```
share()：分享
```java
ShareApi.share(this, sharePlatform, content, new ShareListener() {
            @Override
            public void onComplete() {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(ShareError shareError) {

            }
        },isShareVideo);
```
## 微信分享问题

    微信分享完成后，会有一个选项，选择是继续留在微信还是返回调用的app，如果选择留在微信，之后再通过返回键返回到调用的app，这时调用的app是收不到任何回调的，无法知道分享的结果。

    此外，微信分享时，如果停在选择用户的界面，然后按下Home键，再切回到app中，这时选择用户的界面会消失,但是同样收不到任何回调。

## 参考
https://github.com/cclink/UniShare
