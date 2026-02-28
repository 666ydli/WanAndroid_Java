package com.example.wanandroid.data.bean;
import java.util.List;

public class CoinRankResponse {
    private int curPage;
    private List<CoinInfoBean> datas;

    public List<CoinInfoBean> getDatas() { return datas; }
    public void setDatas(List<CoinInfoBean> datas) { this.datas = datas; }
}
