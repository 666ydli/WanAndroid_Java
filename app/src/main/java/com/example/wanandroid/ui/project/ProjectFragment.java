package com.example.wanandroid.ui.project;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.wanandroid.base.BaseFragment;
import com.example.wanandroid.data.bean.TreeBean;
import com.example.wanandroid.databinding.FragmentProjectBinding;
import com.example.wanandroid.ui.project.list.ProjectListFragment;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.List;

public class ProjectFragment extends BaseFragment<ProjectPresenter, FragmentProjectBinding> implements ProjectContract.View {

    @Override
    protected FragmentProjectBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentProjectBinding.inflate(inflater, container, false);
    }

    @Override
    protected ProjectPresenter createPresenter() {
        return new ProjectPresenter();
    }

    @Override
    protected void initView() {
        // UI 的基础设置可以在这里进行，比如预设一些加载状态
    }

    @Override
    protected void initData() {
        // 发起获取分类数据的网络请求
        if (mPresenter != null) {
            mPresenter.getProjectTree();
        }
    }

    // ================= 实现 Contract.View 的接口方法 =================

    @Override
    public void showProjectTree(List<TreeBean> treeList) {
        // 1. 给 ViewPager2 设置适配器
        binding.viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                // 获取当前 Tab 对应的分类 ID
                int cid = treeList.get(position).getId();

                // 【注意】这里需要你自行创建 ProjectListFragment！
                // 它的逻辑与体系详情中的 TreeListFragment 几乎完全一致
                return ProjectListFragment.newInstance(cid);
            }

            @Override
            public int getItemCount() {
                return treeList.size();
            }
        });

        // 2. 将 TabLayout 与 ViewPager2 完美联动
        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> {
                    // WanAndroid 接口返回的分类名有时包含 HTML 实体（如 &amp;），需要转换一下
                    tab.setText(Html.fromHtml(treeList.get(position).getName()));
                }
        ).attach();
    }

    @Override
    public void showProjectTreeFail(String errorMsg) {
        showToast("加载项目分类失败: " + errorMsg);
    }
}
