package com.example.wanandroid.ui.home;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.wanandroid.base.BaseFragment;
import com.example.wanandroid.common.LoginEvent;
import com.example.wanandroid.data.bean.Article;
import com.example.wanandroid.data.bean.BannerBean;
import com.example.wanandroid.databinding.FragmentHomeBinding;
import com.example.wanandroid.ui.ArticleDetailActivity;
import com.example.wanandroid.ui.login.LoginActivity;
import com.example.wanandroid.utils.UserManager;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;
import com.youth.banner.adapter.BannerImageAdapter;
import com.youth.banner.holder.BannerImageHolder;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class HomeFragment extends BaseFragment<HomePresenter, FragmentHomeBinding> implements HomeContract.View {

    private HomeAdapter mAdapter;
    private int curPage = 0; // 当前页码

    @Override
    protected FragmentHomeBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        // Fragment 中 ViewBinding 的初始化方式
        return FragmentHomeBinding.inflate(inflater, container, false);
    }

    @Override
    protected HomePresenter createPresenter() {
        return new HomePresenter();
    }

    @Override
    protected void initView() {
        // 1. 设置 Toolbar 标题
        binding.toolbar.setTitle("首页");

        // 2. 初始化 RecyclerView (注意 Fragment 中使用 getContext())
        binding.rvHome.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new HomeAdapter();
        binding.rvHome.setAdapter(mAdapter);

        // 3. 设置列表和收藏的点击事件
        mAdapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Article article) {
                // 跳转到网页详情页
                Intent intent = new Intent(getContext(), ArticleDetailActivity.class);
                intent.putExtra("url", article.getLink());
                intent.putExtra("title", article.getTitle());
                startActivity(intent);
            }

            @Override
            public void onCollectClick(int position, int articleId, boolean isCollect) {
                // 判断是否登录
                if (!UserManager.getInstance().isLogin()) {
                    showToast("请先登录");
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    // 因为没登录，必须把刚才点亮的红心还原回去
                    mAdapter.notifyItemChanged(position);
                    return;
                }
                // 已登录，发起收藏/取消收藏网络请求
                mPresenter.collectArticle(articleId, position, isCollect);
            }
        });

        // 4. 初始化刷新和加载更多监听
        binding.refreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                curPage = 0;
                if (mPresenter != null) {
                    mPresenter.getBanner();
                    mPresenter.getArticles(curPage);
                }
            }

            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                curPage++;
                if (mPresenter != null) {
                    mPresenter.getArticles(curPage);
                }
            }
        });
        // 点击跳转到搜索页
        binding.ivSearch.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), com.example.wanandroid.ui.search.SearchActivity.class));
        });
        // 监听 RecyclerView 的滑动
        binding.rvHome.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 获取当前的 LayoutManager
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null) {
                    // 获取屏幕上看到的第一个 Item 的位置
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    // 当向下滑动超过第 5 个 Item 时，显示按钮
                    if (firstVisibleItemPosition > 5) {
                        // fab.show() 自带漂亮的缩放放大动画
                        binding.fabTop.show();
                    } else {
                        // 回到顶部附近时，隐藏按钮 (自带缩放缩小动画)
                        binding.fabTop.hide();
                    }
                }
            }
        });
        binding.fabTop.setOnClickListener(v -> {
            // smoothScrollToPosition 会带有平滑滚动的动画效果
            binding.rvHome.smoothScrollToPosition(0);
        });
    }

    @Override
    protected void initData() {
        // 首次进入请求数据
        showLoading();
        if (mPresenter != null) {
            mPresenter.getBanner();
            mPresenter.getArticles(curPage);
        }
    }

    // ================= 实现 HomeContract.View 的接口方法 =================

    @Override
    public void showArticles(List<Article> articles) {
        binding.refreshLayout.finishRefresh();
        binding.refreshLayout.finishLoadMore();
        hideLoading();

        if (curPage == 0) {
            mAdapter.setNewData(articles);
        } else {
            mAdapter.addData(articles);
        }
    }

    @Override
    public void showArticlesFail(String errorMsg) {
        binding.refreshLayout.finishRefresh();
        binding.refreshLayout.finishLoadMore();
        hideLoading();
        showToast("加载文章失败: " + errorMsg);
    }

    @Override
    public void showBanner(List<BannerBean> banners) {
        binding.banner.setAdapter(new MyBannerAdapter(banners))
                // 注意：Fragment 中为了防止内存泄漏，生命周期绑定用 getViewLifecycleOwner() 而不是 this
                .addBannerLifecycleObserver(getViewLifecycleOwner())
                .setIndicator(new CircleIndicator(getContext()))
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(Object data, int position) {
                        BannerBean bannerBean = banners.get(position);
                        Intent intent = new Intent(getContext(), ArticleDetailActivity.class);
                        intent.putExtra("url", bannerBean.getUrl());
                        intent.putExtra("title", bannerBean.getTitle());
                        startActivity(intent);
                    }
                });
    }

    @Override
    public void showBannerFail(String errorMsg) {
        showToast("Banner加载失败: " + errorMsg);
    }

    @Override
    public void showCollectSuccess(int position, boolean isCollect) {
        // 收藏成功，更新本地数据源
        mAdapter.getData().get(position).setCollect(isCollect);
        showToast(isCollect ? "收藏成功" : "取消收藏");
    }

    @Override
    public void showCollectFail(String errorMsg) {
        showToast("操作失败: " + errorMsg);
        // 网络请求失败，刷新列表，让勾选错的 CheckBox 恢复原状
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLogoutSuccess() {
        // 如果在其他地方退出了登录，这里可以重新刷新列表状态
        curPage = 0;
        if (mPresenter != null) {
            mPresenter.getArticles(curPage);
        }
    }

    @Override
    public void showLoading() {
        if (binding.loadingView != null) {
            binding.loadingView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading() {
        if (binding.loadingView != null) {
            binding.loadingView.setVisibility(View.GONE);
        }
    }

    // ================= Banner 适配器内部类 =================

    static class MyBannerAdapter extends BannerImageAdapter<BannerBean> {
        public MyBannerAdapter(List<BannerBean> mData) {
            super(mData);
        }

        @Override
        public void onBindView(BannerImageHolder holder, BannerBean data, int position, int size) {
            Glide.with(holder.itemView)
                    .load(data.getImagePath())
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(30))) // 圆角
                    .into(holder.imageView);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // 1. 注册 EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // 2. 解绑 EventBus，防止内存泄漏
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
    // 3. 接收事件的方法
    // @Subscribe 注解表示这是一个事件接收器，ThreadMode.MAIN 表示在主线程执行（因为要更新UI）
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginStateChange(LoginEvent event) {
        // 收到登录/登出事件后，让列表自动触发下拉刷新
        if (binding != null && binding.refreshLayout != null) {
            binding.refreshLayout.autoRefresh(); // 这个方法会触发 onRefresh 回调，重新请求第0页数据
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCollectStateChange(com.example.wanandroid.common.CollectEvent event) {
        // 新方案：不再重新请求网络，而是直接遍历本地数据修改状态！
        if (mAdapter != null && mAdapter.getData() != null) {
            List<Article> list = mAdapter.getData();
            for (int i = 0; i < list.size(); i++) {
                // 如果找到了对应的文章 ID
                if (list.get(i).getId() == event.getArticleId()) {
                    // 修改它的收藏状态
                    list.get(i).setCollect(event.isCollect());
                    // 局部刷新这一条 Item，效率极高！
                    mAdapter.notifyItemChanged(i);
                    break; // 改完就跳出循环
                }
            }
        }
    }
}
