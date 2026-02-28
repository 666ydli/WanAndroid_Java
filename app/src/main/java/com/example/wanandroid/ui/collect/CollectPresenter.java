package com.example.wanandroid.ui.collect;

import com.example.wanandroid.base.BasePresenter;
import com.example.wanandroid.data.bean.ArticleListResponse;
import com.example.wanandroid.data.bean.BaseResponse;
import com.example.wanandroid.data.remote.RetrofitManager;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CollectPresenter extends BasePresenter<CollectContract.View> implements CollectContract.Presenter {

    @Override
    public void getCollectList(int page) {
        RetrofitManager.getInstance().getApiService().getCollectList(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<ArticleListResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(BaseResponse<ArticleListResponse> response) {
                        if (isViewAttached()) {
                            if (response.getErrorCode() == 0 && response.getData() != null) {
                                getView().showCollectList(response.getData().getDatas());
                            } else {
                                getView().showCollectListFail(response.getErrorMsg());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) getView().showCollectListFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() { }
                });
    }

    @Override
    public void uncollect(int id, int originId, int position) {
        RetrofitManager.getInstance().getApiService().uncollectInMyList(id, originId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<Object>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(BaseResponse<Object> response) {
                        if (isViewAttached()) {
                            if (response.getErrorCode() == 0) {
                                getView().showUncollectSuccess(position);
                            } else {
                                getView().showUncollectFail(response.getErrorMsg());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) getView().showUncollectFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() { }
                });
    }
}
