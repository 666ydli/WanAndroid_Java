package com.example.wanandroid.ui.login;

import com.example.wanandroid.base.IBaseView;
import com.example.wanandroid.data.bean.UserBean;

public interface LoginContract {

    interface View extends IBaseView {
        // 登录成功
        void loginSuccess(UserBean userBean);
        // 登录失败
        void loginFail(String errorMsg);
    }

    interface Presenter {
        // 发起登录
        void login(String username, String password);
        // 发起注册 (可选，逻辑类似)
        void register(String username, String password, String repassword);
    }
}
