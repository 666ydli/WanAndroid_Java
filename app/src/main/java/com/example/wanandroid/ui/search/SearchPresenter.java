package com.example.wanandroid.ui.search;

import com.example.wanandroid.base.BasePresenter;
import com.example.wanandroid.data.bean.ArticleListResponse;
import com.example.wanandroid.data.bean.BaseResponse;
import com.example.wanandroid.data.bean.HotKeyBean;
import com.example.wanandroid.data.remote.RetrofitManager;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchPresenter extends BasePresenter<SearchContract.View> implements SearchContract.Presenter {

    @Override
    public void getHotKeys() {
        RetrofitManager.getInstance().getApiService().getHotKeys()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<List<HotKeyBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }
                    @Override
                    public void onNext(BaseResponse<List<HotKeyBean>> response) {
                        if (isViewAttached() && response.getErrorCode() == 0) {
                            getView().showHotKeys(response.getData());
                        }
                    }
                    @Override
                    public void onError(Throwable e) { }
                    @Override
                    public void onComplete() { }
                });
    }

    @Override
    public void search(int page, String keyword) {
        RetrofitManager.getInstance().getApiService().searchArticles(page, keyword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<ArticleListResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) { if(isViewAttached()) getView().showLoading(); }

                    @Override
                    public void onNext(BaseResponse<ArticleListResponse> response) {
                        if (isViewAttached()) {
                            if (response.getErrorCode() == 0 && response.getData() != null) {
                                getView().showSearchArticles(response.getData().getDatas());
                            } else {
                                getView().showSearchFail(response.getErrorMsg());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().hideLoading();
                            getView().showSearchFail(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (isViewAttached()) getView().hideLoading();
                    }
                });
    }

    @Override
    public void collectArticle(int id, int position, boolean isCollect) {
        // ... (请复制 HomePresenter 里的 collectArticle 逻辑)
    }
}
