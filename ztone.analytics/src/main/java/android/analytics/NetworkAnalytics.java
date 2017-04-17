package android.analytics;

import android.support.v4.util.ArrayMap;

public abstract class NetworkAnalytics {

    protected long mContentLenght;

    protected final String mHostName;

    protected final String mHttpType;

    public NetworkAnalytics(String host, String httpType) {
        mHostName = host;
        mHttpType = httpType;
    }

    /**
     * 记录网络请求开始
     *
     * @param action
     * @return
     */
    public abstract NetworkAnalytics start(String action);

    /**
     * 在httpURLConnection.connect()方法或者httpClient.execute()方法执行完毕时调用
     *
     * @return
     */
    public abstract NetworkAnalytics connected();

    /**
     * 在httpURLConnection.getResponseCode()方法或者HttpResponse.getStatusLine.getStatusCode()方法执行完毕后调用
     *
     * @return
     */
    public abstract NetworkAnalytics response();

    /**
     * 调用这个函数以后，整个网络性能埋点流程就结束了，该次网络请求的相关数据会上报到数据分析平台
     *
     * @return
     */
    public abstract NetworkAnalytics finish();

    /**
     * 确认上报数据
     */
    public abstract <A> void complete(A a);

    /**
     * 网络请求无法避免异常情况，如果抛出异常打断了整个网络请求的流程，那么需要对此做额外处理.
     */
    public abstract NetworkAnalytics status(int stateCode, String url, ArrayMap<String, String> errorInfoMap);

    /**
     * 网络请求无法避免异常情况，如果抛出异常打断了整个网络请求的流程，那么需要对此做额外处理.
     */
    public abstract NetworkAnalytics error(Throwable t, String url, ArrayMap<String, String> errorInfoMap);

    /**
     * 附加额外信息
     *
     * @param kay
     * @param value
     * @return
     */
    public abstract NetworkAnalytics addExtraInfo(String kay, String value);

    /**
     * 附加额外信息
     *
     * @param extraInfoMap
     * @return
     */
    public abstract NetworkAnalytics putExtraInfo(ArrayMap<String, String> extraInfoMap);

    public NetworkAnalytics setContentLenght(long contentLenght) {
        mContentLenght = contentLenght;

        return this;
    }
}
