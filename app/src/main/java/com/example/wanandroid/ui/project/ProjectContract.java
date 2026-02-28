package com.example.wanandroid.ui.project;

import com.example.wanandroid.base.IBaseView;
import com.example.wanandroid.data.bean.TreeBean;

import java.util.List;

public interface ProjectContract {

    interface View extends IBaseView {
        // 获取项目分类树成功
        void showProjectTree(List<TreeBean> treeList);

        // 获取项目分类树失败
        void showProjectTreeFail(String errorMsg);
    }

    interface Presenter {
        // 发起网络请求获取分类树
        void getProjectTree();
    }
}
