package com.example.wanandroid.ui.tree;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.wanandroid.data.bean.TreeBean;
import com.example.wanandroid.databinding.ActivityTreeDetailBinding;
import com.example.wanandroid.ui.tree.list.TreeListFragment;
import com.google.android.material.tabs.TabLayoutMediator;

public class TreeDetailActivity extends AppCompatActivity {

    private ActivityTreeDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTreeDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 1. 获取传递过来的 TreeBean 数据
        TreeBean treeBean = (TreeBean) getIntent().getSerializableExtra("tree_bean");
        if (treeBean == null || treeBean.getChildren() == null) return;

        // 2. 初始化 Toolbar
        binding.toolbar.setTitle(treeBean.getName());
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // 3. 配置 ViewPager2 的适配器
        binding.viewPager.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                // 每次滑动到新的一页，就创建一个传入对应 cid 的 Fragment
                int cid = treeBean.getChildren().get(position).getId();
                return TreeListFragment.newInstance(cid);
            }

            @Override
            public int getItemCount() {
                return treeBean.getChildren().size();
            }
        });

        // 4. 将 TabLayout 和 ViewPager2 绑定联动！
        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> {
                    // 设置每个 Tab 显示的标题名字
                    tab.setText(treeBean.getChildren().get(position).getName());
                }
        ).attach();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }
}
