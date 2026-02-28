package com.example.wanandroid.ui.project.list;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.wanandroid.base.BaseFragment;
import com.example.wanandroid.data.bean.Article;
import com.example.wanandroid.databinding.FragmentListOnlyBinding;
import com.example.wanandroid.ui.ArticleDetailActivity;
import com.example.wanandroid.ui.project.ProjectAdapter;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

public class ProjectListFragment extends BaseFragment<ProjectListPresenter, FragmentListOnlyBinding> implements ProjectListContract.View {

    private ProjectAdapter mAdapter;

    // 【关键点】：WanAndroid 项目列表接口是从 1 开始的！
    private int curPage = 1;
    private int cid = 0; // 当前页面的分类 ID

    /**
     * 经典传参模式，供外部 ViewPager2 实例化时调用
     */
    public static ProjectListFragment newInstance(int cid) {
        ProjectListFragment fragment = new ProjectListFragment();
        Bundle args = new Bundle();
        args.putInt("cid", cid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected FragmentListOnlyBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        // 复用之前写好的仅包含 SmartRefreshLayout + RecyclerView 的布局
        return FragmentListOnlyBinding.inflate(inflater, container, false);
    }

    @Override
    protected ProjectListPresenter createPresenter() {
        return new ProjectListPresenter();
    }

    @Override
    protected void initView() {
        // 1. 获取传入的分类 ID
        if (getArguments() != null) {
            cid = getArguments().getInt("cid", 0);
        }

        // 2. 初始化 RecyclerView 和 ProjectAdapter
        binding.rvList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new ProjectAdapter();
        binding.rvList.setAdapter(mAdapter);

        // 3. 设置点击事件，跳转到网页详情页
        mAdapter.setOnItemClickListener(new ProjectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Article article) {
                Intent intent = new Intent(getContext(), ArticleDetailActivity.class);
                intent.putExtra("url", article.getLink());
                intent.putExtra("title", article.getTitle());
                startActivity(intent);
            }
        });

        // 4. 配置下拉刷新和上拉加载
        binding.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                // 下拉刷新，重置页码为 1
                curPage = 1;
                if (mPresenter != null) {
                    mPresenter.getProjects(curPage, cid);
                }
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                // 上拉加载更多，页码 +1
                curPage++;
                if (mPresenter != null) {
                    mPresenter.getProjects(curPage, cid);
                }
            }
        });
    }

    @Override
    protected void initData() {
        // 首次进入时，自动触发刷新以请求第 1 页数据
        binding.refreshLayout.autoRefresh();
    }

    // ================= 实现 Contract.View 的接口方法 =================

    @Override
    public void showProjects(List<Article> articles) {
        // 结束刷新和加载动画
        binding.refreshLayout.finishRefresh();
        binding.refreshLayout.finishLoadMore();

        if (curPage == 1) {
            // 如果是第一页，覆盖旧数据
            mAdapter.setNewData(articles);
        } else {
            // 如果是下一页，追加新数据
            mAdapter.addData(articles);
        }
    }

    @Override
    public void showProjectsFail(String errorMsg) {
        binding.refreshLayout.finishRefresh();
        binding.refreshLayout.finishLoadMore();
        showToast("加载项目失败: " + errorMsg);
    }
}
