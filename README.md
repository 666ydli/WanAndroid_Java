# 🚀 WanAndroid_Java

Java语言编写的基于经典的 **MVP (Model-View-Presenter)** 架构，从零开始搭建的 [玩Android](https://www.wanandroid.com/) 第三方客户端。本项目旨在作为 Android 架构学习、主流第三方库接入以及全流程闭环开发的实战模板。

## ✨ Features

- **标准 MVP 架构**：严格的职责分离（View层与Model层零耦合），封装 `BaseActivity` / `BaseFragment` / `BasePresenter`，极大降低重复代码。
- **响应式网络请求**：使用 `Retrofit2` + `RxJava2` 构建强大的异步数据流，配合自定义日志拦截器实现优雅的网络层通信。
- **全局状态与事件解耦**：使用 `EventBus` 实现跨页面（如：收藏文章 -> 首页红心状态同步、退出登录 -> 全局数据刷新）的无缝状态同步。
- **现代化 UI 联动**：采用 `TabLayout` + `ViewPager2` + `FragmentStateAdapter` 完美实现多 Tab 滑动页面机制；使用 `FlexboxLayout` 实现热搜词流式布局。
- **Cookie 持久化机制**：引入 `PersistentCookieJar` 自动管理用户 Session，实现持久化登录状态。
- **极致的列表体验**：集成 `SmartRefreshLayout` 实现流畅的下拉刷新与上拉加载更多，并解决局部刷新（`notifyItemChanged`）带来的数据竞态问题。

## 🛠️ Tech Stack

- **语言**: Java
- **架构**: MVP (Model - View - Presenter)
- **网络**: OkHttp3 + Retrofit2 + Gson
- **异步**: RxJava2 + RxAndroid
- **图片加载**: Glide (配合圆角转换)
- **事件总线**: EventBus
- **列表刷新**: SmartRefreshLayout
- **网页加载**: AgentWeb
- **UI 组件**: ViewPager2, TabLayout, ConstraintLayout, FlexboxLayout, Banner

## 📱 Screenshots

| 首页 (Home) | 体系分类 (Knowledge Tree) | 项目列表 (Projects) |
|:---:|:---:|:---:|
| <img src="/pic/home.png" width="250"/> | <img src="/pic/tree.png" width="250"/> | <img src="/pic/project.png" width="250"/> |
| **搜索热词 (Search)** | **个人中心 (Profile)** | **积分排行榜 (Rank)** |
| <img src="/pic/search.png" width="250"/> | <img src="/pic/profile.png" width="250"/> | <img src="/pic/rank.png" width="250"/> |

## 📂 Modules

1. **首页模块**：包含 Banner 轮播图、文章列表、点赞收藏功能。
2. **体系模块**：处理多层级（嵌套）JSON 结构，使用 `ViewPager2` 实现二级分类横向滑动。
3. **项目模块**：结合 `Glide` 的图文列表展示，同样支持多级分类。
4. **导航与搜索**：包含完善的搜索历史（热搜词展示）与状态驱动（State UI）切换。
5. **用户系统**：登录注册、Cookie 自动管理、个人积分展示与我的收藏列表。

## 🚀 How to Run

1. 确保已安装 **Android Studio** (推荐 Arctic Fox 或更高版本)。
2. 克隆本仓库：
   ```bash
   git clone https://github.com/你的用户名/WanAndroid-MVP.git
3. 使用 Android Studio 打开项目。

4. 点击右上角 Sync Project with Gradle Files。

5. 点击 Run 'app' 编译并在模拟器或真机上运行。
