package com.example.wanandroid.data.bean;

import java.util.List;

/**
 * 分页数据包装类
 */
public class ArticleListResponse {
    private int curPage;
    private List<Article> datas;

    public List<Article> getDatas() {
        return datas;
    }

    public void setDatas(List<Article> datas) {
        this.datas = datas;
    }
}
