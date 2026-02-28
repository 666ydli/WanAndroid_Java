package com.example.wanandroid.ui.tree;

import com.example.wanandroid.base.IBaseView;
import com.example.wanandroid.data.bean.TreeBean;
import java.util.List;

public interface TreeContract {
    interface View extends IBaseView {
        void showTree(List<TreeBean> treeList);
        void showTreeFail(String errorMsg);
    }

    interface Presenter {
        void getTree();
    }
}
