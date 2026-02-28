package com.example.wanandroid.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.example.wanandroid.common.MyApplication;
import com.example.wanandroid.data.bean.UserBean;
import com.google.gson.Gson;

/**
 * 全局用户管理类 (单例)
 * 负责保存和获取用户基本信息
 */
public class UserManager {

    private static final String SP_NAME = "user_info";
    private static final String KEY_USER = "key_user_bean";
    private static UserManager instance;
    private UserBean userBean;

    private UserManager() {
        // 初始化时从 SP 中读取缓存的用户信息
        SharedPreferences sp = MyApplication.getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        String json = sp.getString(KEY_USER, "");
        if (!TextUtils.isEmpty(json)) {
            userBean = new Gson().fromJson(json, UserBean.class);
        }
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    // 保存用户信息 (登录成功调用)
    public void saveUser(UserBean user) {
        this.userBean = user;
        SharedPreferences sp = MyApplication.getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_USER, new Gson().toJson(user)).apply();
    }

    // 清除用户信息 (退出登录调用)
    public void logout() {
        this.userBean = null;
        SharedPreferences sp = MyApplication.getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().remove(KEY_USER).apply();
        // 注意：这里还应该清除 Cookie，后面会讲
    }

    // 获取当前用户
    public UserBean getUser() {
        return userBean;
    }

    // 判断是否已登录
    public boolean isLogin() {
        return userBean != null;
    }
}
