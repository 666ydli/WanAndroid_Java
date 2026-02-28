package com.example.wanandroid.data.bean;

public class BannerBean {
    private int id;
    private String desc;
    private String imagePath;
    private String title;
    private String url;

    // 必须要有的 Getter 方法，Banner 库或者 Glide 需要读取
    public String getImagePath() { return imagePath; }
    public String getTitle() { return title; }
    public String getUrl() { return url; }
}
