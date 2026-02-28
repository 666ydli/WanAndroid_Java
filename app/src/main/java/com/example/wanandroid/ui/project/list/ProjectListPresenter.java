package com.example.wanandroid.ui.project.list;

import com.example.wanandroid.base.BasePresenter;
import com.example.wanandroid.data.bean.ArticleListResponse;
import com.example.wanandroid.data.bean.BaseResponse;
import com.example.wanandroid.data.remote.RetrofitManager;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProjectListPresenter extends BasePresenter<ProjectListContract.View> implements ProjectListContract.Presenter {

    @Override
    public void getProjects(int page, int cid) {
        RetrofitManager.getInstance().getApiService().getProjectList(page, cid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<ArticleListResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        // 列表本身的 SmartRefreshLayout 有动画，这里不再调用统一的 showLoading()
                    }

                    @Override
                    public void onNext(BaseResponse<ArticleListResponse> response) {
                        if (isViewAttached()) {
                            if (response.getErrorCode() == 0 && response.getData() != null) {
                                getView().showProjects(response.getData().getDatas());
                            } else {
                                getView().showProjectsFail(response.getErrorMsg());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().showProjectsFail(e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() { }
                });
    }
}
