package com.example.wanandroid.ui.login;

import com.example.wanandroid.base.BasePresenter;
import com.example.wanandroid.data.bean.BaseResponse;
import com.example.wanandroid.data.bean.UserBean;
import com.example.wanandroid.data.remote.RetrofitManager;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter {

    @Override
    public void login(String username, String password) {
        RetrofitManager.getInstance().getApiService()
                .login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<UserBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (isViewAttached()) getView().showLoading();
                    }

                    @Override
                    public void onNext(BaseResponse<UserBean> response) {
                        if (isViewAttached()) {
                            if (response.getErrorCode() == 0) {
                                getView().loginSuccess(response.getData());
                            } else {
                                getView().loginFail(response.getErrorMsg());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().hideLoading();
                            getView().loginFail(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (isViewAttached()) getView().hideLoading();
                    }
                });
    }

    @Override
    public void register(String username, String password, String repassword) {
        // 注册逻辑与登录类似，留给你自己练习实现
    }
}
