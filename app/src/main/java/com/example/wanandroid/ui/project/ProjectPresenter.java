package com.example.wanandroid.ui.project;

import com.example.wanandroid.base.BasePresenter;
import com.example.wanandroid.data.bean.BaseResponse;
import com.example.wanandroid.data.bean.TreeBean;
import com.example.wanandroid.data.remote.RetrofitManager;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProjectPresenter extends BasePresenter<ProjectContract.View> implements ProjectContract.Presenter {

    @Override
    public void getProjectTree() {
        RetrofitManager.getInstance().getApiService().getProjectTree()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<List<TreeBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        if (isViewAttached()) {
                            getView().showLoading();
                        }
                    }

                    @Override
                    public void onNext(BaseResponse<List<TreeBean>> response) {
                        if (isViewAttached()) {
                            // 判断 errorCode 为 0 且数据不为空
                            if (response.getErrorCode() == 0 && response.getData() != null) {
                                getView().showProjectTree(response.getData());
                            } else {
                                getView().showProjectTreeFail(response.getErrorMsg());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().hideLoading();
                            getView().showProjectTreeFail(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (isViewAttached()) {
                            getView().hideLoading();
                        }
                    }
                });
    }
}
