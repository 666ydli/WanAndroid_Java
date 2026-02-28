package com.example.wanandroid.data.remote;

import com.example.wanandroid.data.bean.ArticleListResponse;
import com.example.wanandroid.data.bean.BannerBean;
import com.example.wanandroid.data.bean.BaseResponse;
import com.example.wanandroid.data.bean.CoinInfoBean;
import com.example.wanandroid.data.bean.CoinRankResponse;
import com.example.wanandroid.data.bean.HotKeyBean;
import com.example.wanandroid.data.bean.TreeBean;
import com.example.wanandroid.data.bean.UserBean;
// 注意：这里暂时用 Object 占位，下一章我们会创建具体的文章实体类
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    /**
     * 获取首页文章列表
     * @param page 页码，从 0 开始
     */
    @GET("article/list/{page}/json")
    Observable<BaseResponse<ArticleListResponse>> getHomeArticles(@Path("page") int page);
    @GET("banner/json")
    Observable<BaseResponse<List<BannerBean>>> getBanner();
    // 登录
    @POST("user/login")
    @FormUrlEncoded
    Observable<BaseResponse<UserBean>> login(
            @Field("username") String username,
            @Field("password") String password
    );

    // 注册
    @POST("user/register")
    @FormUrlEncoded
    Observable<BaseResponse<UserBean>> register(
            @Field("username") String username,
            @Field("password") String password,
            @Field("repassword") String repassword
    );

    // 收藏站内文章
    @POST("lg/collect/{id}/json")
    Observable<BaseResponse<Object>> collectArticle(@Path("id") int id);

    // 取消收藏 (在文章列表页)
    @POST("lg/uncollect_originId/{id}/json")
    Observable<BaseResponse<Object>> uncollectArticle(@Path("id") int id);

    // 退出登录
    @GET("user/logout/json")
    Observable<BaseResponse<Object>> logout();

    // 获取我的收藏列表 (page 从 0 开始)
    @GET("lg/collect/list/{page}/json")
    Observable<BaseResponse<ArticleListResponse>> getCollectList(@Path("page") int page);

    // 在“我的收藏”页面取消收藏
    // 注意：这里用的是 id (收藏记录ID)，并且需要通过表单传入 originId (原文章ID)
    @POST("lg/uncollect/{id}/json")
    @FormUrlEncoded
    Observable<BaseResponse<Object>> uncollectInMyList(@Path("id") int id, @Field("originId") int originId);
    // 获取体系数据
    @GET("tree/json")
    Observable<BaseResponse<List<TreeBean>>> getTree();
    // 获取体系下的文章列表 (page 从 0 开始)
    @GET("article/list/{page}/json")
    Observable<BaseResponse<ArticleListResponse>> getTreeArticles(@Path("page") int page, @Query("cid") int cid);
    // 获取搜索热词
    @GET("hotkey/json")
    Observable<BaseResponse<List<HotKeyBean>>> getHotKeys();

    // 搜索文章
    @POST("article/query/{page}/json")
    @FormUrlEncoded
    Observable<BaseResponse<ArticleListResponse>> searchArticles(@Path("page") int page, @Field("k") String k);
    // 1. 获取项目分类树
    @GET("project/tree/json")
    Observable<BaseResponse<List<TreeBean>>> getProjectTree();

    // 2. 获取分类下的项目列表 (page 从 1 开始，注意这里和首页不同，项目列表是从 1 开始的！)
    @GET("project/list/{page}/json")
    Observable<BaseResponse<ArticleListResponse>> getProjectList(@Path("page") int page, @Query("cid") int cid);
    // 获取个人积分信息 (需要登录的 Cookie)
    @GET("lg/coin/userinfo/json")
    Observable<BaseResponse<CoinInfoBean>> getMyCoin();

    // 获取积分排行榜 (page 从 1 开始)
    @GET("coin/rank/{page}/json")
    Observable<BaseResponse<CoinRankResponse>> getCoinRank(@Path("page") int page);
}
