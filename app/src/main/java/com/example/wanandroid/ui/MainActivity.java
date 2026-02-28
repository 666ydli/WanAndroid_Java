package com.example.wanandroid.ui;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.wanandroid.R;
import com.example.wanandroid.databinding.ActivityMainBinding;
import com.example.wanandroid.ui.home.HomeFragment;
import com.example.wanandroid.ui.profile.ProfileFragment;
import com.example.wanandroid.ui.project.ProjectFragment;
import com.example.wanandroid.ui.tree.TreeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;
    private Fragment currentFragment;
    private TreeFragment treeFragment;
    private ProjectFragment projectFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 初始化 Fragment
        homeFragment = new HomeFragment();
        treeFragment = new TreeFragment();
        profileFragment = new ProfileFragment();
        projectFragment = new ProjectFragment();

        // 默认显示首页
        switchFragment(homeFragment);

        // 设置底部导航栏点击事件
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                switchFragment(homeFragment);
                return true;
            } else if (itemId == R.id.nav_tree) {
                switchFragment(treeFragment);
                return true;
            } else if (itemId == R.id.nav_project) { // 新增
                switchFragment(projectFragment);
                return true;
            } else if (itemId == R.id.nav_profile) {
                switchFragment(profileFragment);
                return true;
            }
            return false;
        });
    }

    /**
     * 切换 Fragment 的经典方法 (使用 show/hide 而不是 replace，防止 Fragment 每次都重新创建)
     */
    private void switchFragment(Fragment targetFragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!targetFragment.isAdded()) {
            // 如果目标 Fragment 还没添加过，就添加它，并隐藏当前的
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
            transaction.add(R.id.fragment_container, targetFragment).commit();
        } else {
            // 如果已经添加过，直接显示目标 Fragment，隐藏当前的
            transaction.hide(currentFragment).show(targetFragment).commit();
        }
        currentFragment = targetFragment;
    }
}
