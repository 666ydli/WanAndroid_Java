# 🚀 WanAndroid_Java

Java语言编写的基于经典的 **MVP (Model-View-Presenter)** 架构，从零开始搭建的 [玩Android](https://www.wanandroid.com/) 第三方客户端。本项目旨在作为 Android 架构学习、主流第三方库接入以及全流程闭环开发的实战模板。

## ✨ Features

- **标准 MVP 架构**：严格的职责分离（View层与Model层零耦合），封装 `BaseActivity/Fragment` 与 `BasePresenter`，极大降低重复代码，防范内存泄漏。
- **全局状态与事件解耦**：使用 `EventBus` 实现跨页面（如：侧滑删除/取消收藏 -> 首页状态同步）的无缝状态同步，并通过局部刷新解决网络竞态问题。
- **高级 UI 交互与极致 UX**：
  - 采用 `TabLayout + ViewPager2` 完美实现多 Tab 滑动页面与按需加载。
  - 集成 Android 原生 `ItemTouchHelper` 实现丝滑的列表**侧滑删除**。
  - 运用 `RecyclerView` 滑动监听结合 FAB 实现平滑的**一键悬浮置顶**。
- **现代化数据存储**：
  - 引入 `PersistentCookieJar` 自动管理用户 Session，实现持久化无感登录。
  - 基于 `SharedPreferences` 配合 `Gson` 序列化，实现**本地搜索历史记录**（含去重与上限淘汰机制）。
- **系统生态深度融合**：灵活运用**隐式 Intent (Implicit Intent)** 无缝拉起系统级分享面板与外部浏览器。

## 🛠️ Tech Stack

- **语言**: Java
- **架构**: MVP (Model - View - Presenter)
- **网络**: OkHttp3 + Retrofit2 + Gson
- **异步**: RxJava2 + RxAndroid
- **数据持久化**: SharedPreferences + CookieJar
- **UI 组件**: ViewPager2, TabLayout, ItemTouchHelper, FlexboxLayout, AgentWeb, SmartRefreshLayout
- **图片加载**: Glide (配合圆角转换)
- **事件总线**: EventBus

## 📱 Screenshots

| 首页 (Home) | 体系分类 (Knowledge Tree) | 项目列表 (Projects) |
|:---:|:---:|:---:|
| <img src="/pic/home.jpg" width="250"/> | <img src="/pic/tree.jpg" width="250"/> | <img src="/pic/project.jpg" width="250"/> |
| **搜索热词 (Search)** | **个人中心 (Profile)** | **积分排行榜 (Rank)** |
| <img src="/pic/search.jpg" width="250"/> | <img src="/pic/profile.jpg" width="250"/> | <img src="/pic/rank.jpg" width="250"/> |

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
