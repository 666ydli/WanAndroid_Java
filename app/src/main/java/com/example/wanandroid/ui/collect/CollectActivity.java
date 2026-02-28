package com.example.wanandroid.ui.collect;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.wanandroid.base.BaseActivity;
import com.example.wanandroid.data.bean.Article;
import com.example.wanandroid.databinding.ActivityCollectBinding;
import com.example.wanandroid.ui.ArticleDetailActivity;
import com.example.wanandroid.ui.home.HomeAdapter;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

public class CollectActivity extends BaseActivity<CollectPresenter, ActivityCollectBinding> implements CollectContract.View {

    private HomeAdapter mAdapter;
    private int curPage = 0;

    @Override
    protected ActivityCollectBinding getViewBinding() {
        return ActivityCollectBinding.inflate(getLayoutInflater());
    }

    @Override
    protected CollectPresenter createPresenter() {
        return new CollectPresenter();
    }

    @Override
    protected void initView() {
        // 配置 Toolbar 左上角返回键
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        binding.rvCollect.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new HomeAdapter();
        binding.rvCollect.setAdapter(mAdapter);

        // 设置点击事件
        mAdapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Article article) {
                Intent intent = new Intent(CollectActivity.this, ArticleDetailActivity.class);
                intent.putExtra("url", article.getLink());
                intent.putExtra("title", article.getTitle());
                startActivity(intent);
            }

            @Override
            public void onCollectClick(int position, int articleId, boolean isCollect) {
                // 在收藏列表中，点击红心一定是“取消收藏”
                // 注意获取 originId 和 id 的区别
                Article article = mAdapter.getData().get(position);
                int originId = article.getOriginId() != 0 ? article.getOriginId() : -1;

                // 发起取消收藏请求 (传入收藏记录id, 原文章id, 位置)
                mPresenter.uncollect(article.getId(), originId, position);
            }
        });

        binding.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                curPage = 0;
                mPresenter.getCollectList(curPage);
            }
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                curPage++;
                mPresenter.getCollectList(curPage);
            }
        });
    }

    @Override
    protected void initData() {
        // 为了让收藏列表里的红心默认是亮起的，我们在获取数据后，手动把状态设为 true
        binding.refreshLayout.autoRefresh();
    }

    // 处理 Toolbar 返回键
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // --- 接口实现 ---

    @Override
    public void showCollectList(List<Article> articles) {
        binding.refreshLayout.finishRefresh();
        binding.refreshLayout.finishLoadMore();

        // 收藏列表接口返回的数据里，collect 字段默认可能不是 true
        // 为了 UI 显示正确，我们手动将其设为 true
        for (Article article : articles) {
            article.setCollect(true);
        }

        if (curPage == 0) {
            mAdapter.setNewData(articles);
        } else {
            mAdapter.addData(articles);
        }
    }

    @Override
    public void showCollectListFail(String errorMsg) {
        binding.refreshLayout.finishRefresh();
        binding.refreshLayout.finishLoadMore();
        showToast("加载失败: " + errorMsg);
    }

    @Override
    public void showUncollectSuccess(int position) {
        showToast("已取消收藏");

        // 1. 获取这篇被取消的文章的原ID (必须在 remove 之前获取！)
        Article article = mAdapter.getData().get(position);
        // 在“我的收藏”中，真实的首页文章ID存在 originId 中
        int originId = article.getOriginId() != 0 ? article.getOriginId() : article.getId();

        // 2. 从当前列表中移除
        mAdapter.getData().remove(position);
        mAdapter.notifyItemRemoved(position);
        mAdapter.notifyItemRangeChanged(position, mAdapter.getData().size() - position);

        // 3. [修改这里] 携带具体的文章ID和最新状态(false)发送事件
        org.greenrobot.eventbus.EventBus.getDefault().post(new com.example.wanandroid.common.CollectEvent(originId, false));
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void showUncollectFail(String errorMsg) {
        showToast("操作失败: " + errorMsg);
        mAdapter.notifyDataSetChanged(); // 失败则把红心恢复
    }
}
