package com.example.wanandroid.ui.project.list;

import com.example.wanandroid.base.IBaseView;
import com.example.wanandroid.data.bean.Article;

import java.util.List;

public interface ProjectListContract {
    interface View extends IBaseView {
        // 获取项目列表成功
        void showProjects(List<Article> articles);
        // 获取项目列表失败
        void showProjectsFail(String errorMsg);
    }

    interface Presenter {
        // 请求项目列表 (需要页码和分类ID)
        void getProjects(int page, int cid);
    }
}
