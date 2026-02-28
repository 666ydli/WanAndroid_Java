package com.example.wanandroid.data.bean;

import java.util.List;

public class UserBean {
    private String username;
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getCollectIds() {
        return collectIds;
    }

    public void setCollectIds(List<String> collectIds) {
        this.collectIds = collectIds;
    }

    private String icon;
    private int id;
    private List<String> collectIds; // 收藏的文章ID列表

    // 必须生成 Getter/Setter，否则无法读取数据
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    // ... 其他字段的 Getter/Setter 请补全
}
