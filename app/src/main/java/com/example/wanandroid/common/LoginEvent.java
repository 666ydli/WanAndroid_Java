package com.example.wanandroid.common;

/**
 * 登录状态改变的事件
 */
public class LoginEvent {
    private boolean isLogin; // true: 登录成功, false: 退出登录

    public LoginEvent(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public boolean isLogin() {
        return isLogin;
    }
}
