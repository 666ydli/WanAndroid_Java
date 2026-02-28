package com.example.wanandroid.common;

public class CollectEvent {
    private int articleId; // 文章的原始ID
    private boolean isCollect; // 最新状态

    public CollectEvent(int articleId, boolean isCollect) {
        this.articleId = articleId;
        this.isCollect = isCollect;
    }

    public int getArticleId() {
        return articleId;
    }

    public boolean isCollect() {
        return isCollect;
    }
}
