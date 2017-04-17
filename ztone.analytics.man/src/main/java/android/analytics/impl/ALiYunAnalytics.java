package android.analytics.impl;

import android.analytics.Analytics;
import android.analytics.NetworkAnalytics;
import android.app.Activity;
import android.app.Application;

import com.alibaba.sdk.android.man.MANAnalytics;
import com.alibaba.sdk.android.man.MANPageHitHelper;
import com.alibaba.sdk.android.man.MANService;
import com.alibaba.sdk.android.man.MANServiceProvider;

import java.util.Map;
import java.util.Properties;

public class ALiYunAnalytics implements Analytics {
    private static final String APP_KEY = "alibaba_appkey";
    private static final String APP_SECRET = "alibaba_appsecret";
    private static final String CHANNEL = "alibaba_channel";

    private MANService mMANService;
    private MANAnalytics mMANAnalytics;
    private MANPageHitHelper mMANPageHitHelper;

    /**
     * 【注意】建议您在Application中初始化MAN，以保证正常获取MANService
     */
    @Override
    public void onInit(Application app, Properties properties) {
        if (app != null && properties != null && properties.size() > 0) {
            // 获取MAN服务
            mMANService = MANServiceProvider.getService();
            mMANAnalytics = mMANService.getMANAnalytics();
            mMANPageHitHelper = mMANService.getMANPageHitHelper();

            // 打开调试日志，线上版本建议关闭
            // mMANAnalytics.turnOnDebug();

            // MAN初始化方法之一，从AndroidManifest.xml中获取appKey和appSecret初始化
            // mMANAnalytics.init(SuperApplication.sApplication, E.sAppContext);

            // MAN另一初始化方法，手动指定appKey和appSecret
            // String appKey = "******";
            // String appSecret = "******";
            mMANAnalytics.init(app, app.getApplicationContext(), properties.getProperty(APP_KEY), properties.getProperty(APP_SECRET));

            // 若需要关闭 SDK 的自动异常捕获功能可进行如下操作,详见文档5.4
            // mMANAnalytics.turnOffCrashHandler();

            // 通过此接口关闭页面自动打点功能，详见文档4.2
            // mMANAnalytics.turnOffAutoPageTrack();

            // 设置渠道（用以标记该app的分发渠道名称），如果不关心可以不设置即不调用该接口，渠道设置将影响控制台【渠道分析】栏目的报表展现。如果文档3.3章节更能满足您渠道配置的需求，就不要调用此方法，按照3.3进行配置即可
            mMANAnalytics.setChannel(properties.getProperty(CHANNEL));

            // 若AndroidManifest.xml 中的 android:versionName 不能满足需求，可在此指定
            // 若在上述两个地方均没有设置appversion，上报的字段默认为null
            // mMANAnalytics.setAppVersion("3.1.1");
        }
    }

    @Override
    public void register(String userNick) {
        if (mMANAnalytics != null) {
            mMANAnalytics.userRegister(userNick);
        }
    }

    @Override
    public void login(String userNick, String userId) {
        if (mMANAnalytics != null) {
            mMANAnalytics.updateUserAccount(userNick, userId);
        }
    }

    @Override
    public void logout(String userNick, String userId) {
        if (mMANAnalytics != null) {
            mMANAnalytics.updateUserAccount(userNick, userId);
        }
    }

    @Override
    public void startPageTracking(Activity activity) {
        if (mMANPageHitHelper != null) {
            mMANPageHitHelper.pageAppear(activity);
        }
    }

    @Override
    public void stopPageTracking(Activity activity) {
        if (mMANPageHitHelper != null) {
            mMANPageHitHelper.pageDisAppear(activity);
        }
    }

    @Override
    public NetworkAnalytics createNetworkAnalytics(String host, String httpType) {

        return new ALiYunNetworkAnalytics(host, httpType);
    }

    /**
     * 确认上报数据
     */
    public void completeNetworkAnalytics(NetworkAnalytics networkAnalytics) {
        if (networkAnalytics != null) {
            networkAnalytics.complete(mMANAnalytics);
        }
    }

    @Override
    public void logEvent(String eventId) {

    }

    @Override
    public void logEvent(String eventId, Map<String, String> parameters) {

    }

    @Override
    public void logEvent(String eventId, boolean timed) {

    }

    @Override
    public void logEvent(String eventId, Map<String, String> parameters, boolean timed) {

    }

    @Override
    public void onError(String errorId, String message, Throwable exception) {

    }

    /**
     * 设置年龄
     */
    @Override
    public void setAge(int age) {

    }

    /**
     * 设置性别
     */
    @Override
    public void setGender(byte gender) {

    }

}
