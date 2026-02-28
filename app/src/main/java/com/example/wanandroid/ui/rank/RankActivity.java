package com.example.wanandroid.ui.rank;

import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.wanandroid.base.BaseActivity;
import com.example.wanandroid.data.bean.CoinInfoBean;
import com.example.wanandroid.databinding.ActivityRankBinding; // 确保创建了 activity_rank.xml
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.List;

public class RankActivity extends BaseActivity<RankPresenter, ActivityRankBinding> implements RankContract.View {

    private RankAdapter mAdapter;
    private int curPage = 1; // 积分排行榜接口页码从 1 开始

    @Override
    protected ActivityRankBinding getViewBinding() {
        return ActivityRankBinding.inflate(getLayoutInflater());
    }

    @Override
    protected RankPresenter createPresenter() {
        return new RankPresenter();
    }

    @Override
    protected void initView() {
        // 配置 Toolbar
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("积分排行榜");
        }

        // 配置 RecyclerView
        binding.rvRank.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RankAdapter();
        binding.rvRank.setAdapter(mAdapter);

        // 配置刷新加载
        binding.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                curPage = 1;
                mPresenter.getRankList(curPage);
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                curPage++;
                mPresenter.getRankList(curPage);
            }
        });
    }

    @Override
    protected void initData() {
        binding.refreshLayout.autoRefresh();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // --- MVP 回调 ---

    @Override
    public void showRankList(List<CoinInfoBean> list) {
        binding.refreshLayout.finishRefresh();
        binding.refreshLayout.finishLoadMore();
        if (curPage == 1) {
            mAdapter.setNewData(list);
        } else {
            mAdapter.addData(list);
        }
    }

    @Override
    public void showRankListFail(String errorMsg) {
        binding.refreshLayout.finishRefresh();
        binding.refreshLayout.finishLoadMore();
        showToast("加载失败: " + errorMsg);
    }
}
