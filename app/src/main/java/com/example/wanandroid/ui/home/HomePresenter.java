package com.example.wanandroid.ui.home;

import com.example.wanandroid.base.BasePresenter;
import com.example.wanandroid.data.bean.ArticleListResponse;
import com.example.wanandroid.data.bean.BannerBean;
import com.example.wanandroid.data.bean.BaseResponse;
import com.example.wanandroid.data.remote.RetrofitManager;
import com.example.wanandroid.utils.UserManager;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract.Presenter {

    @Override
    public void getArticles(int page) {
        // 1. 调用 Retrofit 接口
        RetrofitManager.getInstance().getApiService()
                .getHomeArticles(page)
                // 2. 指定在 IO 线程进行网络请求
                .subscribeOn(Schedulers.io())
                // 3. 指定在主线程处理结果 (更新 UI)
                .observeOn(AndroidSchedulers.mainThread())
                // 4. 发起订阅
                .subscribe(new Observer<BaseResponse<ArticleListResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // 可以在这里处理请求前的逻辑，比如显示 Loading
                        if (isViewAttached()) {
                            getView().showLoading();
                        }
                    }

                    @Override
                    public void onNext(BaseResponse<ArticleListResponse> response) {
                        // 请求成功回调
                        if (isViewAttached()) {
                            // 检查 errorCode 是否为 0 (成功)
                            if (response.getErrorCode() == 0 && response.getData() != null) {
                                // 将文章列表传给 View
                                getView().showArticles(response.getData().getDatas());
                            } else {
                                getView().showArticlesFail(response.getErrorMsg());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 请求失败回调 (网络错误、解析错误等)
                        if (isViewAttached()) {
                            getView().hideLoading();
                            getView().showArticlesFail(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                        // 请求结束回调
                        if (isViewAttached()) {
                            getView().hideLoading();
                        }
                    }
                });
    }

    @Override
    public void getBanner() {
        RetrofitManager.getInstance().getApiService()
                .getBanner()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<List<BannerBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(BaseResponse<List<BannerBean>> response) {
                        if (isViewAttached()) {
                            if (response.getErrorCode() == 0 && response.getData() != null) {
                                getView().showBanner(response.getData());
                            } else {
                                getView().showBannerFail(response.getErrorMsg());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().showBannerFail(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() { }
                });
    }

    @Override
    public void collectArticle(int id, int position, boolean isCollect) {
        // 根据状态选择调用收藏还是取消收藏
        Observable<BaseResponse<Object>> observable;
        if (isCollect) {
            observable = RetrofitManager.getInstance().getApiService().collectArticle(id);
        } else {
            observable = RetrofitManager.getInstance().getApiService().uncollectArticle(id);
        }

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<Object>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(BaseResponse<Object> response) {
                        if (isViewAttached()) {
                            if (response.getErrorCode() == 0) {
                                // 成功后回调 View
                                getView().showCollectSuccess(position, isCollect);
                            } else {
                                getView().showCollectFail(response.getErrorMsg());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) getView().showCollectFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() { }
                });
    }

    @Override
    public void logout() {
        RetrofitManager.getInstance().getApiService().logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<Object>>() {
                    @Override
                    public void onSubscribe(Disposable d) {}

                    @Override
                    public void onNext(BaseResponse<Object> response) {
                        if(isViewAttached()) {
                            // 清除本地用户数据
                            UserManager.getInstance().logout();
                            // 清除 Cookie (PersistentCookieJar 会自动处理，但手动清除是个好习惯)
                            // RetrofitManager.getInstance().clearCookie(); // 如果你封装了这个方法
                            getView().showLogoutSuccess();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {}

                    @Override
                    public void onComplete() {}
                });
    }
}
