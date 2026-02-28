package com.example.wanandroid.ui.tree;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.wanandroid.base.BaseFragment;
import com.example.wanandroid.data.bean.TreeBean;
import com.example.wanandroid.databinding.FragmentTreeBinding;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.List;

public class TreeFragment extends BaseFragment<TreePresenter, FragmentTreeBinding> implements TreeContract.View {

    private TreeAdapter mAdapter;

    @Override
    protected FragmentTreeBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentTreeBinding.inflate(inflater, container, false);
    }

    @Override
    protected TreePresenter createPresenter() {
        return new TreePresenter();
    }

    @Override
    protected void initView() {
        binding.toolbar.setTitle("体系");

        binding.rvTree.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new TreeAdapter();
        binding.rvTree.setAdapter(mAdapter);

        // 体系数据不分页，所以只需要下拉刷新，不需要上拉加载更多
        binding.refreshLayout.setEnableLoadMore(false);
        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                if (mPresenter != null) mPresenter.getTree();
            }
        });
        mAdapter.setOnItemClickListener(new TreeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(TreeBean treeBean) {
                Intent intent = new Intent(getContext(), TreeDetailActivity.class);
                intent.putExtra("tree_bean", treeBean); // 因为实现了 Serializable，可以直接传
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {
        binding.refreshLayout.autoRefresh();
    }

    @Override
    public void showTree(List<TreeBean> treeList) {
        binding.refreshLayout.finishRefresh();
        mAdapter.setNewData(treeList);
    }

    @Override
    public void showTreeFail(String errorMsg) {
        binding.refreshLayout.finishRefresh();
        showToast("加载失败: " + errorMsg);
    }
}
