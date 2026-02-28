package com.example.wanandroid.ui.collect;

import android.content.Intent;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanandroid.base.BaseActivity;
import com.example.wanandroid.common.CollectEvent;
import com.example.wanandroid.data.bean.Article;
import com.example.wanandroid.databinding.ActivityCollectBinding;
import com.example.wanandroid.ui.ArticleDetailActivity;
import com.example.wanandroid.ui.home.HomeAdapter;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class CollectActivity extends BaseActivity<CollectPresenter, ActivityCollectBinding> implements CollectContract.View {

    private HomeAdapter mAdapter;
    private int curPage = 0; // 收藏列表接口页码从 0 开始

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
        // 1. 配置 Toolbar
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // 2. 初始化 RecyclerView 和 Adapter
        binding.rvCollect.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new HomeAdapter();
        binding.rvCollect.setAdapter(mAdapter);

        // 3. 正常点击事件 (点击卡片看文章，点击红心取消收藏)
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
                // 点击红心取消收藏
                Article article = mAdapter.getData().get(position);
                int originId = article.getOriginId() != 0 ? article.getOriginId() : article.getId();
                mPresenter.uncollect(article.getId(), originId, position);
            }
        });

        // ================= 4. [高阶UI交互] 侧滑删除功能 =================
        ItemTouchHelper.SimpleCallback swipeCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // 不需要上下拖拽排序
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // 滑动到底松手时触发
                int position = viewHolder.getAdapterPosition();
                Article article = mAdapter.getData().get(position);
                int originId = article.getOriginId() != 0 ? article.getOriginId() : article.getId();

                // 发起取消收藏网络请求
                if (mPresenter != null) {
                    mPresenter.uncollect(article.getId(), originId, position);
                }
            }
        };
        // 绑定到当前的 RecyclerView 上
        new ItemTouchHelper(swipeCallback).attachToRecyclerView(binding.rvCollect);
        // =========================================================

        // 5. 配置下拉刷新和上拉加载
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
        // 进入页面自动刷新请求数据
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

    // ================= Contract.View 回调实现 =================

    @Override
    public void showCollectList(List<Article> articles) {
        binding.refreshLayout.finishRefresh();
        binding.refreshLayout.finishLoadMore();

        // 服务器返回的收藏列表可能没有默认 collect 字段，手动设为 true 让红心亮起
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

        // 1. 务必在 remove 前获取文章的原 ID
        Article article = mAdapter.getData().get(position);
        int originId = article.getOriginId() != 0 ? article.getOriginId() : article.getId();

        // 2. 从列表中移除并播放动画
        mAdapter.getData().remove(position);
        mAdapter.notifyItemRemoved(position);
        mAdapter.notifyItemRangeChanged(position, mAdapter.getData().size() - position);

        // 3. 通知首页进行局部刷新状态同步
        EventBus.getDefault().post(new CollectEvent(originId, false));
    }

    @Override
    public void showUncollectFail(String errorMsg) {
        showToast("操作失败: " + errorMsg);
        // 如果断网或失败，notifyDataSetChanged 能让刚才被划出屏幕的卡片“瞬间弹回原位”！
        mAdapter.notifyDataSetChanged();
    }
}
