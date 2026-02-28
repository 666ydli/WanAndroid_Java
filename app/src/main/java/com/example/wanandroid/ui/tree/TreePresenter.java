package com.example.wanandroid.ui.tree;

import com.example.wanandroid.base.BasePresenter;
import com.example.wanandroid.data.bean.BaseResponse;
import com.example.wanandroid.data.bean.TreeBean;
import com.example.wanandroid.data.remote.RetrofitManager;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TreePresenter extends BasePresenter<TreeContract.View> implements TreeContract.Presenter {
    @Override
    public void getTree() {
        RetrofitManager.getInstance().getApiService().getTree()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<List<TreeBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(BaseResponse<List<TreeBean>> response) {
                        if (isViewAttached()) {
                            if (response.getErrorCode() == 0 && response.getData() != null) {
                                getView().showTree(response.getData());
                            } else {
                                getView().showTreeFail(response.getErrorMsg());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) getView().showTreeFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() { }
                });
    }
}
