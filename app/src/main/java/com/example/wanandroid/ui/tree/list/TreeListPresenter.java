package com.example.wanandroid.ui.tree.list;

import com.example.wanandroid.base.BasePresenter;
import com.example.wanandroid.data.bean.ArticleListResponse;
import com.example.wanandroid.data.bean.BaseResponse;
import com.example.wanandroid.data.remote.RetrofitManager;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TreeListPresenter extends BasePresenter<TreeListContract.View> implements TreeListContract.Presenter {

    @Override
    public void getArticles(int page, int cid) {
        RetrofitManager.getInstance().getApiService().getTreeArticles(page, cid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<ArticleListResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(BaseResponse<ArticleListResponse> response) {
                        if (isViewAttached()) {
                            if (response.getErrorCode() == 0 && response.getData() != null) {
                                getView().showArticles(response.getData().getDatas());
                            } else {
                                getView().showArticlesFail(response.getErrorMsg());
                            }
                        }
                    }
                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) getView().showArticlesFail(e.getMessage());
                    }
                    @Override
                    public void onComplete() { }
                });
    }

    @Override
    public void collectArticle(int id, int position, boolean isCollect) {
        Observable<BaseResponse<Object>> observable;
        if (isCollect) {
            observable = RetrofitManager.getInstance().getApiService().collectArticle(id);
        } else {
            // 注意：这是站内文章取消收藏的接口
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
                                // 只有调用了这个方法，View 才会显示“收藏成功”
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
}
