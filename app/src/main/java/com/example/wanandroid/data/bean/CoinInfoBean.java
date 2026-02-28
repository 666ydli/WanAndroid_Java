package com.example.wanandroid.data.bean;

public class CoinInfoBean {
    private int coinCount; // 积分
    private int level;     // 等级
    private String rank;   // 排名
    private String username; // 用户名

    // 记得生成 Getter 和 Setter
    public int getCoinCount() { return coinCount; }
    public void setCoinCount(int coinCount) { this.coinCount = coinCount; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public String getRank() { return rank; }
    public void setRank(String rank) { this.rank = rank; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
}
