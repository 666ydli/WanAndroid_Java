package com.example.wanandroid.ui.rank;

import com.example.wanandroid.base.IBaseView;
import com.example.wanandroid.data.bean.CoinInfoBean;
import java.util.List;

public interface RankContract {
    interface View extends IBaseView {
        void showRankList(List<CoinInfoBean> list);
        void showRankListFail(String errorMsg);
    }

    interface Presenter {
        void getRankList(int page);
    }
}
