package android.analytics.impl;

import android.analytics.NetworkAnalytics;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.alibaba.sdk.android.man.MANAnalytics;
import com.alibaba.sdk.android.man.network.MANNetworkErrorCodeBuilder;
import com.alibaba.sdk.android.man.network.MANNetworkErrorInfo;
import com.alibaba.sdk.android.man.network.MANNetworkPerformanceHitBuilder;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.Map;
import java.util.Set;

class ALiYunNetworkAnalytics extends NetworkAnalytics {

    private final MANNetworkPerformanceHitBuilder mNetworkPerfBuilder;

    public ALiYunNetworkAnalytics(String host, String httpType) {
        super(host, httpType);

        if (!TextUtils.isEmpty(host)) {
            mNetworkPerfBuilder = new MANNetworkPerformanceHitBuilder(host, httpType);

        } else {
            mNetworkPerfBuilder = null;
        }
    }

    @Override
    public NetworkAnalytics start(String action) {
        if (mNetworkPerfBuilder != null) {
            mNetworkPerfBuilder.hitRequestStart();
            mNetworkPerfBuilder.withExtraInfo("Action", action);
        }

        return this;
    }

    public NetworkAnalytics recievedFirstByte() {
        if (mNetworkPerfBuilder != null) {
            mNetworkPerfBuilder.hitRecievedFirstByte();
        }

        return this;
    }

    @Override
    public NetworkAnalytics finish() {
        if (mNetworkPerfBuilder != null) {
            mNetworkPerfBuilder.hitRequestEndWithLoadBytes(mContentLenght);
        }

        return this;
    }

    @Override
    public NetworkAnalytics connected() {
        if (mNetworkPerfBuilder != null) {
            mNetworkPerfBuilder.hitConnectFinished();
        }

        return this;
    }

    @Override
    public NetworkAnalytics response() {
        if (mNetworkPerfBuilder != null) {
            mNetworkPerfBuilder.hitRecievedFirstByte();
        }

        return this;
    }

    @Override
    public <A> void complete(A a) {
        if (a != null && a instanceof MANAnalytics && mNetworkPerfBuilder != null) {
            ((MANAnalytics) a).getDefaultTracker().send(mNetworkPerfBuilder.build());
        }
    }

    @Override
    public NetworkAnalytics status(int stateCode, String url, ArrayMap<String, String> errorInfoMap) {
        if (mNetworkPerfBuilder != null) {
            MANNetworkErrorInfo errorInfo = null;

            if (stateCode < 400) {
                // do nothing

            } else if (400 <= stateCode && stateCode < 500) {
                errorInfo = MANNetworkErrorCodeBuilder.buildHttpCodeClientError4XX();

            } else if (500 <= stateCode) {
                errorInfo = MANNetworkErrorCodeBuilder.buildHttpCodeServerError5XX();

            } else {
                errorInfo = MANNetworkErrorCodeBuilder.buildCustomErrorCode(stateCode);

            }

            error(errorInfo, url, errorInfoMap);
        }

        return this;
    }

    @Override
    public NetworkAnalytics error(Throwable t, String url, ArrayMap<String, String> errorInfoMap) {
        if (t != null && mNetworkPerfBuilder != null) {
            MANNetworkErrorInfo errorInfo = null;

            if (t instanceof IOException) {
                errorInfo = MANNetworkErrorCodeBuilder.buildIOException();

            } else if (t instanceof SocketTimeoutException) {
                errorInfo = MANNetworkErrorCodeBuilder.buildInterruptedIOException();

            } else if (t instanceof InterruptedIOException) {
                errorInfo = MANNetworkErrorCodeBuilder.buildSocketTimeoutException();

            } else if (t instanceof MalformedURLException) {
                errorInfo = MANNetworkErrorCodeBuilder.buildMalformedURLException();
            }

            error(errorInfo, url, errorInfoMap);
        }

        return this;
    }

    @Override
    public NetworkAnalytics addExtraInfo(String key, String value) {
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value) && mNetworkPerfBuilder != null) {
            mNetworkPerfBuilder.withExtraInfo(key, value);
        }

        return this;
    }

    /**
     * 附加额外信息
     *
     * @param extraInfoMap
     * @return
     */
    @Override
    public NetworkAnalytics putExtraInfo(ArrayMap<String, String> extraInfoMap) {

        if (extraInfoMap != null && !extraInfoMap.isEmpty() && mNetworkPerfBuilder != null) {
            Set<Map.Entry<String, String>> entrySet = extraInfoMap.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                if (entry != null) {
                    String key = entry.getKey(), value = entry.getValue();
                    if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                        mNetworkPerfBuilder.withExtraInfo(key, value);
                    }
                }
            }
        }

        return this;
    }

    private void error(MANNetworkErrorInfo errorInfo, String url, ArrayMap<String, String> errorInfoMap) {
        if (errorInfo != null && mNetworkPerfBuilder != null) {
            errorInfo.withExtraInfo("error_url", url);

            if (errorInfoMap != null && !errorInfoMap.isEmpty()) {
                Set<Map.Entry<String, String>> entrySet = errorInfoMap.entrySet();
                for (Map.Entry<String, String> entry : entrySet) {
                    if (entry != null) {
                        String key = entry.getKey(), value = entry.getValue();
                        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                            errorInfo.withExtraInfo(key, value);
                        }
                    }
                }
            }

            mNetworkPerfBuilder.hitRequestEndWithError(errorInfo);
        }
    }
}
