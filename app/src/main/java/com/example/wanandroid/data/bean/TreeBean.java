package com.example.wanandroid.data.bean;

import java.io.Serializable;
import java.util.List;

public class TreeBean implements Serializable {
    private int id;
    private String name;
    // 嵌套自身类型的 List，用来装二级分类
    private List<TreeBean> children;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<TreeBean> getChildren() { return children; }
    public void setChildren(List<TreeBean> children) { this.children = children; }
}
