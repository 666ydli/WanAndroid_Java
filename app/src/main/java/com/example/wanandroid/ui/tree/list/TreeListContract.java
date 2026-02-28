package com.example.wanandroid.ui.tree.list;

import com.example.wanandroid.base.IBaseView;
import com.example.wanandroid.data.bean.Article;
import java.util.List;

public interface TreeListContract {
    interface View extends IBaseView {
        void showArticles(List<Article> articles);
        void showArticlesFail(String errorMsg);
        // 复用首页的收藏逻辑回调
        void showCollectSuccess(int position, boolean isCollect);
        void showCollectFail(String errorMsg);
    }

    interface Presenter {
        void getArticles(int page, int cid);
        void collectArticle(int id, int position, boolean isCollect);
    }
}
