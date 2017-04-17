package android.analytics;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;
import android.util.Log;

import java.util.Map;
import java.util.Properties;

public interface Analytics {
    public static final long CONTINUE_SESSION_MILLIS = 2 * 60 * 1000;

    public static final Analytics.Wrapper Impl = new Analytics.Wrapper();

    void onInit(Application app, Properties properties);

    /**
     * 用户注册
     *
     * @param userNick
     */
    void register(String userNick);

    /**
     * 登录
     *
     * @param userNick
     * @param userId
     */
    void login(String userNick, String userId);

    /**
     * 登出
     *
     * @param userNick
     * @param userId
     */
    void logout(String userNick, String userId);

    /**
     * Start or continue a analytics session for the project denoted by apiKey on the given Context.
     *
     * @param activity
     */
    void startPageTracking(Activity activity);

    /**
     * End a analytics session for the given Context
     *
     * @param activity
     */
    void stopPageTracking(Activity activity);

    /**
     * NetworkAnalytics
     *
     * @param host
     * @param httpType
     * @return
     */
    NetworkAnalytics createNetworkAnalytics(String host, String httpType);

    /**
     * 确认上报数据
     */
    void completeNetworkAnalytics(NetworkAnalytics networkAnalytics);

    /**
     * Log an event
     *
     * @param eventId
     */
    void logEvent(String eventId);

    /**
     * Log an event with parameters
     *
     * @param eventId
     * @param parameters
     */
    void logEvent(String eventId, Map<String, String> parameters);

    /**
     * Log a timed event
     *
     * @param eventId
     * @param timed
     */
    void logEvent(String eventId, boolean timed);

    /**
     * Log a timed event with parameters. Log a timed event with parameters with the analytics service. To end the
     * timer, call endTimedEvent(String) with this eventId.
     *
     * @param eventId
     * @param parameters
     * @param timed
     */
    void logEvent(String eventId, Map<String, String> parameters, boolean timed);

    /**
     * Log an error
     *
     * @param errorId
     * @param message
     * @param exception
     */
    void onError(String errorId, String message, Throwable exception);

    /**
     * Sets the age of the user at the time of this session.
     *
     * @param age
     */
    void setAge(int age);

    /**
     * Sets the gender of the user.
     *
     * @param gender
     */
    void setGender(byte gender);

    public class Wrapper implements Analytics {
        private static final String TAG = "Analytics.Wrapper";

        private static final String CLAZZ_ANALYTICS = "impl_analytics";

        private Analytics mAnalytics;

        @Override
        public void onInit(Application app, Properties properties) {
            if (properties != null && properties.containsKey(CLAZZ_ANALYTICS)) {
                mAnalytics = newInstance(properties.getProperty(CLAZZ_ANALYTICS));
            }

            if (mAnalytics != null) {
                mAnalytics.onInit(app, properties);
            }
        }

        /**
         * 用户注册
         */
        @Override
        public void register(String userNick) {
            if (mAnalytics != null) {
                mAnalytics.register(userNick);
            }
        }

        /**
         * 登录
         *
         * @param userNick
         * @param userId
         */
        @Override
        public void login(String userNick, String userId) {
            if (mAnalytics != null) {
                mAnalytics.login(userNick, userId);
            }
        }

        /**
         * 登出
         *
         * @param userNick
         * @param userId
         */
        @Override
        public void logout(String userNick, String userId) {
            if (mAnalytics != null) {
                mAnalytics.logout(userNick, userId);
            }
        }

        /**
         * Start or continue a analytics session for the project denoted by apiKey on the given Context.
         */
        @Override
        public void startPageTracking(Activity activity) {
            if (mAnalytics != null) {
                mAnalytics.startPageTracking(activity);
            }
        }

        /**
         * End a analytics session for the given Context
         */
        @Override
        public void stopPageTracking(Activity activity) {
            if (mAnalytics != null) {
                mAnalytics.stopPageTracking(activity);
            }
        }

        @Override
        public NetworkAnalytics createNetworkAnalytics(String host, String httpType) {

            return mAnalytics != null ? mAnalytics.createNetworkAnalytics(host, httpType) : null;
        }

        /**
         * 确认上报数据
         */
        @Override
        public void completeNetworkAnalytics(NetworkAnalytics networkAnalytics) {
            if (mAnalytics != null) {
                mAnalytics.completeNetworkAnalytics(networkAnalytics);
            }
        }

        @Override
        public void logEvent(String eventId) {
            if (mAnalytics != null && !TextUtils.isEmpty(eventId)) {
                mAnalytics.logEvent(eventId);

                logEvent(eventId, (ArrayMap<String, String>) null);
            }
        }

        public void logEvent(String eventId, String key, String value) {
            logEvent(eventId, new String[][]{{key, value}});
        }

        private void logEvent(String eventId, String[][] parameters) {
            logEvent(eventId, getParameterMap(parameters));
        }

        private ArrayMap<String, String> getParameterMap(String[][] parameters) {
            ArrayMap<String, String> paramsMap = new ArrayMap<String, String>();

            if (parameters != null && parameters.length > 0) {
                for (String[] params : parameters) {
                    if (params != null && params.length >= 2 && !TextUtils.isEmpty(params[0]) && !TextUtils.isEmpty(params[1])) {
                        paramsMap.put(params[0], params[1]);
                    }
                }
            }

            return paramsMap;
        }

        /**
         * Log an event with parameters
         */
        @Override
        public void logEvent(String eventId, Map<String, String> parameters) {
            if (mAnalytics != null && !TextUtils.isEmpty(eventId)) {
                if (parameters == null) {
                    parameters = new ArrayMap<>();
                }

                mAnalytics.logEvent(eventId, parameters);
            }
        }

        @Override
        public void logEvent(String eventId, boolean timed) {
            if (mAnalytics != null && !TextUtils.isEmpty(eventId)) {
                mAnalytics.logEvent(eventId, timed);
            }
        }

        /**
         * Log a timed event with parameters. Log a timed event with parameters with the analytics service. To end the
         * timer, call endTimedEvent(String) with this eventId.
         *
         * @param eventId
         * @param key
         * @param value
         * @param timed
         */
        public void logEvent(String eventId, String key, String value, boolean timed) {
            logEvent(eventId, new String[][]{{key, value}}, timed);
        }

        /**
         * Log a timed event with parameters. Log a timed event with parameters with the analytics service. To end the
         * timer, call endTimedEvent(String) with this eventId.
         *
         * @param eventId
         * @param parameters
         * @param timed
         */
        public void logEvent(String eventId, String[][] parameters, boolean timed) {
            logEvent(eventId, getParameterMap(parameters), timed);
        }

        /**
         * Log a timed event with parameters. Log a timed event with parameters with the analytics service. To end the
         * timer, call endTimedEvent(String) with this eventId.
         *
         * @param eventId
         * @param parameters
         * @param timed
         */
        @Override
        public void logEvent(String eventId, Map<String, String> parameters, boolean timed) {
            if (mAnalytics != null && !TextUtils.isEmpty(eventId)) {
                if (parameters == null) {
                    parameters = new ArrayMap<>();
                }

                mAnalytics.logEvent(eventId, parameters, timed);
            }
        }

        @Override
        public void onError(String errorId, String message, Throwable exception) {
            if (mAnalytics != null) {
                mAnalytics.onError(errorId, message, exception);
            }
        }

        @Override
        public void setAge(int age) {
            if (mAnalytics != null) {
                mAnalytics.setAge(age);
            }
        }

        @Override
        public void setGender(byte gender) {
            if (mAnalytics != null) {
                mAnalytics.setGender(gender);
            }
        }

        private <Z> Z newInstance(String className) {
            Z z = null;

            Class<Z> clazz = (Class<Z>) forName(className);
            if (clazz != null) {
                try {
                    z = clazz.newInstance();
                } catch (Throwable t) {
                    Log.e(TAG, String.format("class: '%s' instanced fail!", clazz.getName()));
                }
            }

            return z;
        }

        /**
         * 加载类
         *
         * @param className
         * @return
         */
        private <Z> Class<Z> forName(String className) {
            Class<Z> clazz = null;

            if (!TextUtils.isEmpty(className)) {
                try {
                    Class<?> tempClazz = null;

                    ClassLoader classLoader = Context.class.getClassLoader();
                    if (classLoader != null) {
                        tempClazz = classLoader.loadClass(className);
                    } else {
                        tempClazz = Class.forName(className);
                    }

                    if (tempClazz != null) {
                        clazz = (Class<Z>) tempClazz;
                    }
                } catch (Throwable t) {
                    Log.e(TAG, String.format("Class.forName(%s) error!", className));
                }
            }

            return clazz;
        }
    }

    ;
}
