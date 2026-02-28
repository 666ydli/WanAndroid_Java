package com.example.wanandroid.ui.tree.list;

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
import com.example.wanandroid.ui.home.HomeAdapter;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

public class TreeListFragment extends BaseFragment<TreeListPresenter, FragmentListOnlyBinding> implements TreeListContract.View {

    private HomeAdapter mAdapter; // 完美复用首页的 Adapter！
    private int curPage = 0;
    private int cid = 0; // 当前页面的分类 ID

    // 经典传参模式
    public static TreeListFragment newInstance(int cid) {
        TreeListFragment fragment = new TreeListFragment();
        Bundle args = new Bundle();
        args.putInt("cid", cid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected FragmentListOnlyBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentListOnlyBinding.inflate(inflater, container, false);
    }

    @Override
    protected TreeListPresenter createPresenter() {
        return new TreeListPresenter();
    }

    @Override
    protected void initView() {
        if (getArguments() != null) {
            cid = getArguments().getInt("cid", 0);
        }

        binding.rvList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new HomeAdapter();
        binding.rvList.setAdapter(mAdapter);

        // ... 此处设置 mAdapter 的点击事件 (与 HomeFragment 完全一致，点击跳转详情、点击收藏) ...
        mAdapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Article article) {
                Intent intent = new Intent(getContext(), ArticleDetailActivity.class);
                intent.putExtra("url", article.getLink());
                intent.putExtra("title", article.getTitle());
                startActivity(intent);
            }
            @Override
            public void onCollectClick(int position, int articleId, boolean isCollect) {
                if (!com.example.wanandroid.utils.UserManager.getInstance().isLogin()) {
                    showToast("请先登录");
                    startActivity(new Intent(getContext(), com.example.wanandroid.ui.login.LoginActivity.class));
                    mAdapter.notifyItemChanged(position); // 还原红心状态
                    return;
                }
                mPresenter.collectArticle(articleId, position, isCollect);
            }
        });

        binding.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                curPage = 0;
                mPresenter.getArticles(curPage, cid);
            }
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                curPage++;
                mPresenter.getArticles(curPage, cid);
            }
        });
    }

    @Override
    protected void initData() {
        binding.refreshLayout.autoRefresh(); // 首次进入自动刷新
    }

    // --- 实现 View 接口 ---
    @Override
    public void showArticles(List<Article> articles) {
        binding.refreshLayout.finishRefresh();
        binding.refreshLayout.finishLoadMore();
        if (curPage == 0) mAdapter.setNewData(articles);
        else mAdapter.addData(articles);
    }

    @Override
    public void showArticlesFail(String errorMsg) {
        binding.refreshLayout.finishRefresh();
        binding.refreshLayout.finishLoadMore();
        showToast("加载失败: " + errorMsg);
    }

    @Override
    public void showCollectSuccess(int position, boolean isCollect) {
        // 1. 获取文章对象并修改状态
        Article article = mAdapter.getData().get(position);
        article.setCollect(isCollect);
        // 2. 必须刷新这一行 Item，红心才会变亮！
        mAdapter.notifyItemChanged(position);
        // 3. 显示提示
        showToast(isCollect ? "收藏成功" : "取消收藏");
        // 4. [关键同步] 发送全局事件，让首页和“我的收藏”页面知道这里变了
        org.greenrobot.eventbus.EventBus.getDefault().post(
                new com.example.wanandroid.common.CollectEvent(article.getId(), isCollect)
        );
    }

    @Override
    public void showCollectFail(String errorMsg) {
        showToast("操作失败");
        mAdapter.notifyDataSetChanged();
    }
}
