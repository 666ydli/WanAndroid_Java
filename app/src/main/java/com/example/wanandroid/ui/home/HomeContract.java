package com.example.wanandroid.ui.home;

import com.example.wanandroid.base.IBaseView;
import com.example.wanandroid.data.bean.Article;
import com.example.wanandroid.data.bean.BannerBean;

import java.util.List;

public interface HomeContract {

    // 首页 View 需要实现的功能：显示文章列表
    interface View extends IBaseView {
        // 获取文章成功，UI 显示数据
        void showArticles(List<Article> articles);

        // 获取文章失败
        void showArticlesFail(String errorMsg);

        void showBanner(List<BannerBean> banners);
        void showBannerFail(String errorMsg);
        //收藏成功/取消收藏成功
        void showCollectSuccess(int position, boolean isCollect); // position用于刷新列表指定位置
        void showCollectFail(String errorMsg);
        //退出登录成功
        void showLogoutSuccess();
    }

    // 首页 Presenter 需要实现的功能：请求文章数据
    interface Presenter {
        // 请求第 page 页的数据
        void getArticles(int page);

        void getBanner();
        //收藏/取消收藏
        void collectArticle(int id, int position, boolean isCollect); // isCollect: 当前是想收藏还是取消
        //退出登录
        void logout();
    }
}
