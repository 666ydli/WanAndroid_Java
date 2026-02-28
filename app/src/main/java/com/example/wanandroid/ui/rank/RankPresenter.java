package com.example.wanandroid.ui.rank;

import com.example.wanandroid.base.BasePresenter;
import com.example.wanandroid.data.bean.BaseResponse;
import com.example.wanandroid.data.bean.CoinRankResponse;
import com.example.wanandroid.data.remote.RetrofitManager;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RankPresenter extends BasePresenter<RankContract.View> implements RankContract.Presenter {

    @Override
    public void getRankList(int page) {
        RetrofitManager.getInstance().getApiService().getCoinRank(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<CoinRankResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onNext(BaseResponse<CoinRankResponse> response) {
                        if (isViewAttached()) {
                            if (response.getErrorCode() == 0 && response.getData() != null) {
                                getView().showRankList(response.getData().getDatas());
                            } else {
                                getView().showRankListFail(response.getErrorMsg());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) getView().showRankListFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() { }
                });
    }
}
