package com.example.wanandroid.ui.search;

import com.example.wanandroid.base.IBaseView;
import com.example.wanandroid.data.bean.Article;
import com.example.wanandroid.data.bean.HotKeyBean;
import java.util.List;

public interface SearchContract {
    interface View extends IBaseView {
        void showHotKeys(List<HotKeyBean> hotKeys);
        void showSearchArticles(List<Article> articles);
        void showSearchFail(String errorMsg);

        // 复用首页收藏回调
        void showCollectSuccess(int position, boolean isCollect);
        void showCollectFail(String errorMsg);
    }

    interface Presenter {
        void getHotKeys();
        void search(int page, String keyword);
        void collectArticle(int id, int position, boolean isCollect);
    }
}
