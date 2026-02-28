package com.example.wanandroid.data.bean;

public class Article {
    private int id;
    private int originId;
    private String chapterName;
    private String title;
    private String author;
    private String shareUser; // 有时候作者是 shareUser
    private String link;
    private String niceDate; // 格式化好的时间，如 "1天前"
    private boolean collect; // 是否已收藏
    private String desc; // 项目描述
    private String envelopePic; // 封面图片地址

    // Getter & Setter (可以使用 AS 快捷键 Alt+Insert 生成，或者使用 Lombok)
    // 这里为了演示，省略 Getter/Setter，请务必生成它们！

    public int getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }
    public String getNiceDate() { return niceDate; }
    public void setNiceDate(String niceDate) { this.niceDate = niceDate; }

    // 获取显示的作者名（优先显示 author，没有则显示 shareUser）
    public String getShowAuthor() {
        return (author != null && !author.isEmpty()) ? author : shareUser;
    }
    public boolean isCollect() { return collect; }
    public void setCollect(boolean collect) { this.collect = collect; }

    public int getOriginId() {
        return originId;
    }

    public void setOriginId(int originId) {
        this.originId = originId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public void setShareUser(String shareUser) {
        this.shareUser = shareUser;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getEnvelopePic() {
        return envelopePic;
    }

    public void setEnvelopePic(String envelopePic) {
        this.envelopePic = envelopePic;
    }
}
