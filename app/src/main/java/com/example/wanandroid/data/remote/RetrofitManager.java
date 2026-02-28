package com.example.wanandroid.data.remote;

import com.example.wanandroid.common.Constants;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.example.wanandroid.common.MyApplication;

public class RetrofitManager {

    private static volatile RetrofitManager instance;
    private final Retrofit retrofit;
    private  PersistentCookieJar cookieJar;

    // 私有构造方法，初始化配置
    private RetrofitManager() {
        // 1. 配置日志拦截器 (只在 Debug 模式下开启)
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        // 创建 CookieJar (自动将 Cookie 存到 SharedPreferences)
        cookieJar = new PersistentCookieJar(
                new SetCookieCache(),
                new SharedPrefsCookiePersistor(MyApplication.getContext()));

        // 2. 配置 OkHttpClient
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor) // 添加日志打印
                .cookieJar(cookieJar) //注入 CookieJar
                .connectTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(Constants.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        // 3. 配置 Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(okHttpClient)
                // 添加 Gson 转换器，自动解析 JSON
                .addConverterFactory(GsonConverterFactory.create())
                // 添加 RxJava 适配器，支持返回 Observable
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    // 单例模式 (双重检查锁)
    public static RetrofitManager getInstance() {
        if (instance == null) {
            synchronized (RetrofitManager.class) {
                if (instance == null) {
                    instance = new RetrofitManager();
                }
            }
        }
        return instance;
    }
    public void clearCookie() {
        if (cookieJar != null) {
            cookieJar.clear();
        }
    }

    /**
     * 获取 API 接口实例
     */
    public ApiService getApiService() {
        return retrofit.create(ApiService.class);
    }
}
