package com.example.wanandroid.ui.collect;

import com.example.wanandroid.base.IBaseView;
import com.example.wanandroid.data.bean.Article;

import java.util.List;

public interface CollectContract {
    interface View extends IBaseView {
        void showCollectList(List<Article> articles);
        void showCollectListFail(String errorMsg);

        // 取消收藏成功/失败
        void showUncollectSuccess(int position);
        void showUncollectFail(String errorMsg);
    }

    interface Presenter {
        void getCollectList(int page);
        // 需要传入 收藏记录id, 原文章id, 以及列表索引(用于刷新UI)
        void uncollect(int id, int originId, int position);
    }
}
